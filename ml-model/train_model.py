import pandas as pd
import json

# -------------------------------
# LOAD DATA
# -------------------------------
df = pd.read_csv("data/players.csv")

# -------------------------------
# CLEAN DATA
# -------------------------------
# Fill missing values
df.fillna(0, inplace=True)

# Convert numeric columns safely
numeric_cols = ["Matches", "Runs", "Average", "StrikeRate", "Wickets"]

for col in numeric_cols:
    if col in df.columns:
        df[col] = pd.to_numeric(df[col], errors="coerce").fillna(0)

# -------------------------------
# FEATURE ENGINEERING (CORE LOGIC)
# -------------------------------
# Simple weighted scoring model

def calculate_score(row):
    score = 0

    # Batting contribution
    score += row.get("Runs", 0) * 0.3
    score += row.get("Average", 0) * 0.4
    score += row.get("StrikeRate", 0) * 0.2

    # Bowling contribution
    score += row.get("Wickets", 0) * 0.5

    # Experience
    score += row.get("Matches", 0) * 0.1

    return score

df["score"] = df.apply(calculate_score, axis=1)

# -------------------------------
# SELECT TOP PLAYERS
# -------------------------------
df_sorted = df.sort_values(by="score", ascending=False)

top_11 = df_sorted.head(11)

# -------------------------------
# CAPTAIN SELECTION
# -------------------------------
captain = top_11.iloc[0]["Player"]

# -------------------------------
# OUTPUT RESULT
# -------------------------------
result = {
    "squad": top_11["Player"].tolist(),
    "captain": captain,
    "explanation": "Players selected based on performance score combining batting, bowling, and experience."
}

# Save to file
with open("output/selected_team.json", "w") as f:
    json.dump(result, f, indent=4)

print("\n✅ Squad Selected:")
print(result)