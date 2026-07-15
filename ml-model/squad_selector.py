import pandas as pd
import numpy as np
import os

from sklearn.preprocessing import MinMaxScaler

# =========================
# LOAD DATA
# =========================

BASE_DIR = os.path.dirname(__file__)

csv_path = os.path.join(
    BASE_DIR,
    "data",
    "Indian ODI Cricketers.csv"
)

df = pd.read_csv(csv_path)

# =========================
# CLEAN DATA
# =========================

df.replace('—', np.nan, inplace=True)

numeric_cols = [
    'Inn',
    'NO.1',
    'Runs',
    'Avg',
    'Balls',
    'Mdn',
    'Runs_1',
    'Wkt',
    'Avg_2',
    'Mat'
]

for col in numeric_cols:
    df[col] = pd.to_numeric(df[col], errors='coerce')

df[numeric_cols] = df[numeric_cols].fillna(0)

# =========================
# FILTER RECENT PLAYERS
# =========================

recent_df = df[df['Last'] >= 2020].copy()

# Avoid division by zero
recent_df['Balls'] = recent_df['Balls'].replace(0, 1)

# =========================
# CREATE FEATURES
# =========================

recent_df['strike_rate'] = (
    recent_df['Runs'] / recent_df['Balls']
) * 100

recent_df['consistency'] = (
    recent_df['Runs'] / recent_df['Inn'].replace(0, 1)
)

recent_df['economy'] = (
    recent_df['Runs_1'] / recent_df['Balls']
) * 6

scaler = MinMaxScaler()

# =========================================================
# BATSMEN
# =========================================================

batsmen_df = recent_df.copy()

batting_features = [
    'Avg',
    'strike_rate',
    'Runs',
    'consistency',
    'Mat'
]

batsmen_df[batting_features] = scaler.fit_transform(
    batsmen_df[batting_features]
)

batsmen_df['batsman_score'] = (
    batsmen_df['Avg'] * 0.30 +
    batsmen_df['strike_rate'] * 0.25 +
    batsmen_df['Runs'] * 0.25 +
    batsmen_df['consistency'] * 0.10 +
    batsmen_df['Mat'] * 0.10
)

top_batsmen = batsmen_df.sort_values(
    by='batsman_score',
    ascending=False
)

top_batsmen.to_csv(
    os.path.join(BASE_DIR, "top_batsmen.csv"),
    index=False
)

# =========================================================
# BOWLERS
# =========================================================

bowlers_df = recent_df.copy()

bowling_features = [
    'Wkt',
    'Avg_2',
    'Mdn',
    'economy',
    'Mat'
]

bowlers_df[bowling_features] = scaler.fit_transform(
    bowlers_df[bowling_features]
)

bowlers_df['bowler_score'] = (
    bowlers_df['Wkt'] * 0.40 +
    (1 - bowlers_df['Avg_2']) * 0.25 +
    bowlers_df['Mdn'] * 0.15 +
    (1 - bowlers_df['economy']) * 0.10 +
    bowlers_df['Mat'] * 0.10
)

top_bowlers = bowlers_df.sort_values(
    by='bowler_score',
    ascending=False
)

top_bowlers.to_csv(
    os.path.join(BASE_DIR, "top_bowlers.csv"),
    index=False
)

# =========================================================
# WICKETKEEPERS
# =========================================================

wicketkeepers = recent_df[
    recent_df['St'] > 0
].copy()

wk_features = [
    'Runs',
    'Avg',
    'strike_rate',
    'Ca',
    'St',
    'Mat'
]

wicketkeepers[wk_features] = scaler.fit_transform(
    wicketkeepers[wk_features]
)

wicketkeepers['wk_score'] = (
    wicketkeepers['Runs'] * 0.25 +
    wicketkeepers['Avg'] * 0.20 +
    wicketkeepers['strike_rate'] * 0.20 +
    wicketkeepers['Ca'] * 0.15 +
    wicketkeepers['St'] * 0.10 +
    wicketkeepers['Mat'] * 0.10
)

top_wicketkeepers = wicketkeepers.sort_values(
    by='wk_score',
    ascending=False
)

top_wicketkeepers.to_csv(
    os.path.join(BASE_DIR, "top_wicketkeepers.csv"),
    index=False
)

# =========================================================
# ALL-ROUNDERS
# =========================================================

all_rounders = recent_df[
    (recent_df['Runs'] >= 300) &
    (recent_df['Wkt'] >= 10)
].copy()

ar_features = [
    'Runs',
    'Avg',
    'strike_rate',
    'Wkt',
    'Avg_2',
    'Mdn'
]

all_rounders[ar_features] = scaler.fit_transform(
    all_rounders[ar_features]
)

all_rounders['allrounder_score'] = (
    all_rounders['Runs'] * 0.25 +
    all_rounders['Avg'] * 0.15 +
    all_rounders['strike_rate'] * 0.15 +
    all_rounders['Wkt'] * 0.25 +
    (1 - all_rounders['Avg_2']) * 0.10 +
    all_rounders['Mdn'] * 0.10
)

top_all_rounders = all_rounders.sort_values(
    by='allrounder_score',
    ascending=False
)

top_all_rounders.to_csv(
    os.path.join(BASE_DIR, "top_all_rounders.csv"),
    index=False
)

# =========================================================
# FINAL SQUAD
# =========================================================

final_squad = pd.concat([
    top_batsmen.head(5),
    top_wicketkeepers.head(2),
    top_all_rounders.head(3),
    top_bowlers.head(5)
])

final_squad = final_squad.drop_duplicates(
    subset='Name'
)

needed = 15 - len(final_squad)

remaining_players = pd.concat([
    top_batsmen,
    top_bowlers,
    top_all_rounders,
    top_wicketkeepers
])

remaining_players = remaining_players.drop_duplicates(
    subset='Name'
)

remaining_players = remaining_players[
    ~remaining_players['Name'].isin(
        final_squad['Name']
    )
]

extras = remaining_players.head(needed)

final_squad = pd.concat([
    final_squad,
    extras
])

final_squad = final_squad.drop_duplicates(
    subset='Name'
)

final_squad.to_csv(
    os.path.join(BASE_DIR, "final_squad.csv"),
    index=False
)
# ====================================
# ADD SELECTED COLUMN TO MAIN DATASET
# ====================================

selected_players = set(
    final_squad['Name'].str.strip()
)

df['Selected'] = (
    df['Name']
    .str.strip()
    .isin(selected_players)
    .astype(int)
)

# Save back to Indian ODI Cricketers.csv
df.to_csv(
    csv_path,
    index=False
)

print("\nSelected column added successfully.")
print(df[['Name', 'Selected']].head())

print("\nFinal ODI Squad:\n")

print(
    final_squad[
        [
            'Name',
            'Last'
        ]
    ]
)