package com.sports.athleticax.services;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SquadService {

    public String generateSquad() {

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "ml-model/squad_selector.py"
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();

            BufferedReader csvReader = new BufferedReader(
                    new FileReader("ml-model/final_squad.csv")
            );

            StringBuilder squadData = new StringBuilder();

            String row;

            while ((row = csvReader.readLine()) != null) {
                squadData.append(row).append("\n");
            }

            csvReader.close();

            return squadData.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating squad";
        }
    }
}