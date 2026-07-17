package com.sports.athleticax.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.athleticax.dto.PlayerDTO;
import com.sports.athleticax.dto.SquadResponseDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // =========================================================
    // MAIN CHAT METHOD
    // =========================================================
    public SquadResponseDTO ask(String userInput, String csvPath) {
        try {
            // 1) Run Python squad selector and get JSON output
            JsonNode pythonJson = runPythonSquadSelector(csvPath);

            boolean success = pythonJson.path("success").asBoolean(false);
            if (!success) {
                List<String> errors = parseStringList(pythonJson.path("errors"));
                throw new RuntimeException("Python squad selector failed: " + errors);
            }

            // 2) Parse squad sections
            List<PlayerDTO> finalSquad = parsePlayers(pythonJson.path("final_squad"));
            List<PlayerDTO> bestBatsmen = parsePlayers(pythonJson.path("best_batsmen"));
            List<PlayerDTO> bestBowlers = parsePlayers(pythonJson.path("best_bowlers"));
            List<PlayerDTO> bestAllRounders = parsePlayers(pythonJson.path("best_allrounders"));
            List<PlayerDTO> bestWicketkeepers = parsePlayers(pythonJson.path("best_wicketkeepers"));

            List<String> warnings = parseStringList(pythonJson.path("warnings"));
            List<String> errors = parseStringList(pythonJson.path("errors"));

            // 3) Ask AI only for captain + explanation
            String squadList = buildSquadList(finalSquad);

            String prompt = """
                            You are an ODI cricket expert.

                            The squad below is FINAL.

                            Do not change the squad.

                            Select:
                            - Captain
                            - Vice Captain

                            Rules:
                            - They must be different players.
                            - Ignore selection probability.
                            - Choose based on international captaincy, leadership, experience and consistency.

                            Squad:
                            %s

                            Return ONLY JSON.

                            {
                            "captain":"",
                            "viceCaptain":"",
                            "explanation":""
                            }

                            %s
                            """.formatted(squadList, userInput);
            String rawAiResponse = chatClient
                    .prompt(prompt)
                    .call()
                    .content();

            String cleanAiJson = extractJson(rawAiResponse);
            JsonNode aiJson = objectMapper.readTree(cleanAiJson);

            // 4) Build final DTO
            SquadResponseDTO dto = new SquadResponseDTO();
            dto.setSquad(finalSquad);
            dto.setBestBatsmen(bestBatsmen);
            dto.setBestBowlers(bestBowlers);
            dto.setBestAllRounders(bestAllRounders);
            dto.setBestWicketkeepers(bestWicketkeepers);
            dto.setWarnings(warnings);
            dto.setErrors(errors);

            if (aiJson.has("captain")) {
                dto.setCaptain(aiJson.get("captain").asText());
            } else {
                dto.setCaptain("Unknown");
            }

            if(aiJson.has("viceCaptain")){
                dto.setViceCaptain(aiJson.get("viceCaptain").asText());
            }else{
                dto.setViceCaptain("Unknown");
            }

            if (aiJson.has("explanation")) {
                dto.setExplanation(aiJson.get("explanation").asText());
            } else {
                dto.setExplanation("Squad selected successfully from uploaded CSV.");
            }

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ChatService failed: " + e.getMessage(), e);
        }
    }

    // =========================================================
    // RUN PYTHON SQUAD SELECTOR
    // =========================================================
    private JsonNode runPythonSquadSelector(String csvPath) throws Exception {
        
        // Build absolute path to the Python script
        String pythonScriptPath =
                System.getProperty("user.dir")
                + "/ml-model/generalized_squad_selector.py";
        
        // Check if the file exists
        File script = new File(pythonScriptPath);
        
        System.out.println("Script path: " + script.getAbsolutePath());
        System.out.println("Script exists: " + script.exists());
        
        // Use python3 on Render (Linux)
        ProcessBuilder pb = new ProcessBuilder(
                "python3",
                pythonScriptPath,
                csvPath,
                "2022"
        );

        pb.redirectErrorStream(true);

        Process process = pb.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        String rawOutput = output.toString().trim();

        System.out.println("=== PYTHON RAW OUTPUT ===");
        System.out.println(rawOutput);

        if (exitCode != 0) {
            throw new RuntimeException("Python process failed:\n" + rawOutput);
        }

        return objectMapper.readTree(rawOutput);
    }

    // =========================================================
    // PARSE PLAYER ARRAY FROM PYTHON JSON
    // =========================================================
    private List<PlayerDTO> parsePlayers(JsonNode playersNode) {
        List<PlayerDTO> players = new ArrayList<>();

        if (playersNode == null || !playersNode.isArray()) {
            return players;
        }

        for (JsonNode node : playersNode) {
            String name = node.path("Name").asText("");
            String role = node.path("Role").asText("");
            double probability = node.path("selection_probability").asDouble(0.0);

            PlayerDTO player = new PlayerDTO();
            player.setName(name);
            player.setRole(role);
            player.setSelectionProbability(probability);

            players.add(player);
        }

        return players;
    }

    // =========================================================
    // PARSE STRING ARRAY FROM JSON
    // =========================================================
    private List<String> parseStringList(JsonNode node) {
        List<String> list = new ArrayList<>();

        if (node == null || !node.isArray()) {
            return list;
        }

        for (JsonNode item : node) {
            list.add(item.asText());
        }

        return list;
    }

    // =========================================================
    // BUILD SQUAD STRING FOR AI PROMPT
    // =========================================================
    private String buildSquadList(List<PlayerDTO> squad) {
        StringBuilder sb = new StringBuilder();

        for (PlayerDTO player : squad) {
            sb.append(player.getName())
              .append(" (")
              .append(player.getRole())
              .append(")")
              .append(", ");
        }

        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 2); // remove last ", "
        }

        return sb.toString();
    }

    // =========================================================
    // EXTRACT JSON FROM AI RESPONSE
    // =========================================================
    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }

        return """
                {
                  "captain": "Unknown",
                  "viceCaptain": "Unknown",
                  "explanation": "Unable to parse AI response."
                }
                """;
    }
}
