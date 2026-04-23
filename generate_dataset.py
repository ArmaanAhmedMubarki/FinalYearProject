import pandas as pd
import random

# Load your base dataset
df = pd.read_csv("football_performance.csv")

new_data = []

for _ in range(10):  # multiply data ~10 times
    for _, row in df.iterrows():
        new_row = row.copy()

        # Add slight randomness
        new_row["goals"] = max(0, row["goals"] + random.randint(-2, 2))
        new_row["assists"] = max(0, row["assists"] + random.randint(-2, 2))
        new_row["pass_accuracy"] = min(95, max(50, row["pass_accuracy"] + random.randint(-5, 5)))

        for col in ["speed", "positioning", "stamina", "physicality"]:
            new_row[col] = min(10, max(1, row[col] + random.randint(-1, 1)))

        new_row["matches_played"] = max(5, row["matches_played"] + random.randint(-3, 3))

        new_data.append(new_row)

# Create new dataframe
new_df = pd.DataFrame(new_data)

# Save expanded dataset
new_df.to_csv("football_dataset_expanded.csv", index=False)

print("Dataset created with", len(new_df), "rows")