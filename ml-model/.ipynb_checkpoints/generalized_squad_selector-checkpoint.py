import pandas as pd
import numpy as np
import re
from difflib import get_close_matches
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import MinMaxScaler

# ==========================================
# GLOBAL CONFIG
# ==========================================

STANDARD_COLUMNS = [
    'Name', 'Role', 'First', 'Last', 'Career',
    'Mat', 'Inn', 'NO.1', 'Runs', 'HS', 'Avg', 'Balls',
    'Mdn', 'Runs_1', 'Wkt', 'BBM', 'Avg_2', 'Ca', 'St',
    'Selected'
]

# Column aliases from different possible CSVs
COLUMN_ALIASES = {
    'Name': [
        'Name', 'Player', 'Player Name', 'Cricketer', 'Player_Name'
    ],
    'Role': [
        'Role', 'Player Role', 'Type', 'Player Type', 'Playing Role'
    ],
    'First': [
        'First', 'Start', 'Debut Year', 'First Year'
    ],
    'Last': [
        'Last', 'End', 'Last Year', 'Recent Year'
    ],
    'Career': [
        'Career', 'Span', 'Playing Span'
    ],
    'Mat': [
        'Mat', 'Matches', 'Match'
    ],
    'Inn': [
        'Inn', 'Inns', 'Innings'
    ],
    'NO.1': [
        'NO.1', 'NO', 'Not Out', 'Not Outs'
    ],
    'Runs': [
        'Runs', 'Bat Runs', 'Batting Runs'
    ],
    'HS': [
        'HS', 'High Score', 'Highest Score'
    ],
    'Avg': [
        'Avg', 'Bat Avg', 'Batting Avg', 'Batting Average'
    ],
    'Balls': [
        'Balls', 'Ball', 'Balls Faced'
    ],
    'Mdn': [
        'Mdn', 'Maidens', 'Mdns'
    ],
    'Runs_1': [
        'Runs_1', 'Runs.1', 'Bowl Runs', 'Runs Conceded', 'Bowling Runs'
    ],
    'Wkt': [
        'Wkt', 'Wickets', 'Wkts'
    ],
    'BBM': [
        'BBM', 'BB', 'Best Bowling', 'Best'
    ],
    'Avg_2': [
        'Avg_2', 'Avg.1', 'Bowl Avg', 'Bowling Avg', 'Bowling Average'
    ],
    'Ca': [
        'Ca', 'Catches', 'Catch'
    ],
    'St': [
        'St', 'Stumpings', 'Stumping'
    ],
    'Selected': [
        'Selected', 'selected', 'Picked', 'Chosen', 'In Squad', 'Label'
    ]
}

# Which columns are text vs numeric
TEXT_COLUMNS = ['Name', 'Role', 'HS', 'BBM', 'Career']
NUMERIC_COLUMNS = [
    'First', 'Last', 'Mat', 'Inn', 'NO.1', 'Runs', 'Avg', 'Balls',
    'Mdn', 'Runs_1', 'Wkt', 'Avg_2', 'Ca', 'St', 'Selected'
]

# ==========================================
# HELPER FUNCTIONS FOR COLUMN STANDARDIZATION
# ==========================================

def normalize_col_name(col):
    """
    Normalize raw column names for easier matching.
    Example:
    'Runs.1' -> 'runs1'
    'Player Name' -> 'playername'
    """
    col = str(col).strip().lower()
    col = re.sub(r'[^a-z0-9]+', '', col)
    return col


def build_alias_lookup():
    """
    Build normalized alias lookup:
    normalized alias -> standard column name
    """
    alias_lookup = {}
    for standard_col, aliases in COLUMN_ALIASES.items():
        for alias in aliases:
            alias_lookup[normalize_col_name(alias)] = standard_col
    return alias_lookup


ALIAS_LOOKUP = build_alias_lookup()

# ==========================================
# STANDARDIZE ANY UPLOADED CRICKET CSV
# ==========================================

def standardize_uploaded_csv(df, verbose=True):
    """
    Standardize uploaded cricket CSV into a common schema.

    Handles:
    - India-style columns
    - Australia/Wikipedia-style columns
    - likely England-style variants
    - alias mapping
    - Career -> First/Last fallback
    - Role cleaning
    - Selected present/absent
    - numeric cleanup

    Returns:
        df_std, info
    """

    info = {
        "original_columns": list(df.columns),
        "mapped_columns": {},
        "created_columns": [],
        "warnings": []
    }

    df = df.copy()
    df.columns = [str(c).strip() for c in df.columns]

    # --------------------------------------
    # STEP 0: REPLACE COMMON MISSING TOKENS
    # --------------------------------------
    df.replace(['—', '–', 'NA', 'N/A', 'n/a', ''], np.nan, inplace=True)

    # --------------------------------------
    # STEP 1: MAP EXISTING COLUMNS TO STANDARD NAMES
    # --------------------------------------
    new_columns = {}
    used_standard_cols = set()

    for raw_col in df.columns:
        norm = normalize_col_name(raw_col)

        if norm in ALIAS_LOOKUP:
            std_col = ALIAS_LOOKUP[norm]

            # avoid duplicate mapping to same standard column
            if std_col not in used_standard_cols:
                new_columns[raw_col] = std_col
                used_standard_cols.add(std_col)
                info["mapped_columns"][raw_col] = std_col

    df = df.rename(columns=new_columns)

    # --------------------------------------
    # REMOVE DUPLICATE COLUMN NAMES AFTER MAPPING
    # --------------------------------------
    if df.columns.duplicated().any():
        duplicate_cols = df.columns[df.columns.duplicated()].tolist()

        if verbose:
            print("\nDuplicate columns found after mapping:", duplicate_cols)
            print("Keeping first occurrence and dropping the rest.")

        df = df.loc[:, ~df.columns.duplicated()]

    # --------------------------------------
    # STEP 2: CREATE MISSING STANDARD COLUMNS
    # --------------------------------------
    for col in STANDARD_COLUMNS:
        if col not in df.columns:
            df[col] = np.nan
            info["created_columns"].append(col)

    # --------------------------------------
    # STEP 3: CLEAN TEXT COLUMNS
    # --------------------------------------
    for col in TEXT_COLUMNS:
        if col in df.columns:
            df[col] = df[col].astype("string").str.strip()

    # --------------------------------------
    # STEP 4: CAREER -> FIRST / LAST FALLBACK
    # --------------------------------------
    if 'Career' in df.columns:
        career_series = df['Career'].astype(str).str.strip()

        # normalize unicode dashes to standard hyphen
        career_series = (
            career_series
            .str.replace('–', '-', regex=False)
            .str.replace('—', '-', regex=False)
        )

        current_year = pd.Timestamp.now().year

        parsed_first = []
        parsed_last = []

        for val in career_series:
            if pd.isna(val):
                parsed_first.append(np.nan)
                parsed_last.append(np.nan)
                continue

            s = str(val).strip()

            if s == "" or s.lower() == "nan":
                parsed_first.append(np.nan)
                parsed_last.append(np.nan)
                continue

            # normalize spaces around hyphen
            s = re.sub(r'\s*-\s*', '-', s)

            first = np.nan
            last = np.nan

            # Case 1: YYYY-YYYY
            m_range = re.fullmatch(r'(\d{4})-(\d{4})', s)
            if m_range:
                first = int(m_range.group(1))
                last = int(m_range.group(2))

            else:
                # extract first year if present
                m_first = re.match(r'^(\d{4})', s)
                if m_first:
                    first = int(m_first.group(1))
                    s_lower = s.lower()

                    # Case 2: YYYY-present / YYYY-current
                    if re.fullmatch(r'\d{4}-(present|current)', s_lower):
                        last = current_year

                    # Case 3: YYYY-
                    elif re.fullmatch(r'\d{4}-', s):
                        last = current_year

                    # Case 4: YYYY only
                    elif re.fullmatch(r'\d{4}', s):
                        last = np.nan

                    # Fallback: if 2 years appear anywhere, use them
                    else:
                        years = re.findall(r'\d{4}', s)
                        if len(years) >= 2:
                            first = int(years[0])
                            last = int(years[1])

            parsed_first.append(first)
            parsed_last.append(last)

        career_first = pd.Series(parsed_first, index=df.index)
        career_last = pd.Series(parsed_last, index=df.index)

        df['First'] = pd.to_numeric(df['First'], errors='coerce')
        df['Last'] = pd.to_numeric(df['Last'], errors='coerce')

        # treat 0 as missing
        df.loc[df['First'] == 0, 'First'] = np.nan
        df.loc[df['Last'] == 0, 'Last'] = np.nan

        # fill only missing values from Career
        df['First'] = df['First'].fillna(career_first)
        df['Last'] = df['Last'].fillna(career_last)

    # --------------------------------------
    # STEP 5: NUMERIC CLEANING
    # --------------------------------------
    for col in NUMERIC_COLUMNS:
        if col in df.columns:
            # if duplicate somehow still survives, keep first column only
            if isinstance(df[col], pd.DataFrame):
                df[col] = df[col].iloc[:, 0]

            df[col] = pd.to_numeric(df[col], errors='coerce')

    # --------------------------------------
    # STEP 6: ROLE CLEANING
    # --------------------------------------
    def clean_role(x):
        if pd.isna(x):
            return np.nan

        x = str(x).strip().lower()
        x = x.replace('-', ' ').replace('_', ' ')
        x = ' '.join(x.split())

        # wicketkeeper variants
        if any(k in x for k in [
            'wicketkeeper', 'wicket keeper', 'keeper', 'wk'
        ]):
            return 'Wicketkeeper'

        # allrounder variants
        if any(k in x for k in [
            'allrounder', 'all rounder', 'all round',
            'all-rounder', 'batting all rounder', 'bowling all rounder'
        ]):
            return 'AllRounder'

        # bowler variants
        if any(k in x for k in [
            'bowler', 'fast bowler', 'spinner', 'pace bowler'
        ]):
            return 'Bowler'

        # batsman / batter variants
        if any(k in x for k in [
            'batsman', 'batter', 'opening batter', 'top order batter'
        ]):
            return 'Batsman'

        # unknown role -> NaN so we can warn properly
        return np.nan

    df['Role'] = df['Role'].apply(clean_role)

    # --------------------------------------
    # STEP 7: HANDLE SELECTED COLUMN
    # --------------------------------------
    if 'Selected' not in df.columns:
        df['Selected'] = np.nan

    df['Selected'] = pd.to_numeric(df['Selected'], errors='coerce')

    # --------------------------------------
    # STEP 8: FINAL CLEANUP
    # --------------------------------------
    # Name cleanup
    df['Name'] = df['Name'].astype("string").str.strip()

    # Drop rows with missing names
    df = df[df['Name'].notna()].copy()

    # Fill numeric columns except Selected
    numeric_fill_cols = [c for c in NUMERIC_COLUMNS if c != 'Selected']
    for col in numeric_fill_cols:
        if col in df.columns:
            df[col] = df[col].fillna(0)

    # --------------------------------------
    # STEP 9: WARNINGS / VALIDATION SIGNALS
    # --------------------------------------
    # warning if all roles missing
    if df['Role'].isna().all():
        info["warnings"].append(
            "Role column is missing or unrecognized for all players. You must provide player roles."
        )

    # warning if some roles missing / unrecognized
    missing_role_count = df['Role'].isna().sum()
    if 0 < missing_role_count < len(df):
        info["warnings"].append(
            f"{missing_role_count} players have missing or unrecognized role values after cleaning."
        )

    # warning if Last all missing / zero
    if (df['Last'].fillna(0) == 0).all():
        info["warnings"].append(
            "No usable Last/Career information found. Freshness filtering may not work."
        )

    # warning if Selected labels unavailable
    selected_non_null = df['Selected'].notna().sum()
    if selected_non_null == 0:
        info["warnings"].append(
            "Selected labels not found. System will use ranking mode instead of ML classification."
        )

    # --------------------------------------
    # STEP 10: PRINT DEBUG INFO
    # --------------------------------------
    if verbose:
        print("Original columns:")
        print(info["original_columns"])

        print("\nMapped columns:")
        print(info["mapped_columns"])

        print("\nCreated missing columns:")
        print(info["created_columns"])

        if info["warnings"]:
            print("\nWarnings:")
            for w in info["warnings"]:
                print("-", w)

    return df, info

# ==========================================
# DATASET VALIDATION
# ==========================================

def validate_dataset(df):
    """
    Validate whether dataset has enough information
    to generate a squad meaningfully.
    """
    errors = []
    warnings = []

    # Name required
    if 'Name' not in df.columns or df['Name'].isna().all():
        errors.append("Dataset must contain a usable player name column.")

    # Role required
    if 'Role' not in df.columns or df['Role'].isna().all():
        errors.append("Dataset must contain a usable Role column.")

    # Need at least some batting/bowling signal
    batting_signal = any(col in df.columns for col in ['Runs', 'Avg', 'Balls'])
    bowling_signal = any(col in df.columns for col in ['Wkt', 'Avg_2', 'Runs_1'])

    if not batting_signal and not bowling_signal:
        errors.append(
            "Dataset does not contain enough batting/bowling columns to rank players."
        )

    # If Last all missing, warn
    if 'Last' in df.columns and (df['Last'].fillna(0) == 0).all():
        warnings.append(
            "No usable Last/Career values found; freshness filtering may not work properly."
        )

    # Selected may be absent -> ranking mode
    if 'Selected' not in df.columns or df['Selected'].dropna().empty:
        warnings.append(
            "Selected labels not found. System will use ranking mode instead of ML classification."
        )

    return errors, warnings

# ==========================================
# FEATURE ENGINEERING
# ==========================================

def create_features(df):
    """
    Create robust role-agnostic cricket features.
    Works even if some columns are missing.
    """
    df = df.copy()

    # Make sure numeric columns are numeric
    numeric_candidates = [
        'Mat', 'Inn', 'NO.1', 'Runs', 'Avg', 'Balls',
        'Mdn', 'Runs_1', 'Wkt', 'Avg_2', 'Ca', 'St',
        'First', 'Last'
    ]

    for col in numeric_candidates:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors='coerce').fillna(0)

    # ----------------------------
    # Batting features
    # ----------------------------
    # strike rate approximation
    if 'Runs' in df.columns and 'Balls' in df.columns:
        df['strike_rate'] = np.where(df['Balls'] > 0, (df['Runs'] / df['Balls']) * 100, 0)
    else:
        df['strike_rate'] = 0

    # runs per match
    if 'Runs' in df.columns and 'Mat' in df.columns:
        df['runs_per_match'] = np.where(df['Mat'] > 0, df['Runs'] / df['Mat'], 0)
    else:
        df['runs_per_match'] = 0

    # batting consistency proxy
    if 'Avg' in df.columns and 'Runs' in df.columns:
        df['bat_consistency'] = (0.7 * df['Avg']) + (0.3 * df['runs_per_match'])
    else:
        df['bat_consistency'] = 0

    # ----------------------------
    # Bowling features
    # ----------------------------
    # economy approximation
    # We don't have overs, but if bowling avg and wickets exist, we still use bowling avg heavily
    # For some datasets Runs_1 = runs conceded
    if 'Runs_1' in df.columns and 'Wkt' in df.columns:
        df['runs_per_wicket_proxy'] = np.where(df['Wkt'] > 0, df['Runs_1'] / df['Wkt'], 999)
    else:
        df['runs_per_wicket_proxy'] = 999

    # bowling quality score (lower avg better)
    if 'Avg_2' in df.columns:
        df['bowling_quality'] = np.where(df['Avg_2'] > 0, 1 / (df['Avg_2'] + 1), 0)
    else:
        df['bowling_quality'] = 0

    # wickets per match
    if 'Wkt' in df.columns and 'Mat' in df.columns:
        df['wickets_per_match'] = np.where(df['Mat'] > 0, df['Wkt'] / df['Mat'], 0)
    else:
        df['wickets_per_match'] = 0

    # ----------------------------
    # Fielding features
    # ----------------------------
    df['fielding_score'] = df.get('Ca', 0) + (2 * df.get('St', 0))

    # ----------------------------
    # Freshness / experience features
    # ----------------------------
    df['career_length'] = np.where(
        (df.get('Last', 0) > 0) & (df.get('First', 0) > 0),
        df['Last'] - df['First'] + 1,
        0
    )

    # Recent player boost
    df['recent_player'] = np.where(df.get('Last', 0) >= 2020, 1, 0)

    # ----------------------------
    # Generic role scores
    # ----------------------------
    df['batsman_score'] = (
        0.35 * df.get('Runs', 0) +
        0.25 * df.get('Avg', 0) +
        0.15 * df['strike_rate'] +
        0.15 * df['bat_consistency'] +
        0.10 * df['recent_player']
    )

    df['bowler_score'] = (
        0.40 * df.get('Wkt', 0) +
        0.25 * df['wickets_per_match'] +
        0.20 * df['bowling_quality'] * 100 +
        0.10 * (1 / (df['runs_per_wicket_proxy'] + 1)) * 100 +
        0.05 * df['recent_player']
    )

    df['allrounder_score'] = (
        0.45 * df['batsman_score'] +
        0.45 * df['bowler_score'] +
        0.10 * df['fielding_score']
    )

    df['wicketkeeper_score'] = (
        0.40 * df['batsman_score'] +
        0.35 * df['fielding_score'] +
        0.25 * df.get('St', 0)
    )

    # Clean infinities
    df.replace([np.inf, -np.inf], 0, inplace=True)

    return df

# ==========================================
# ROLE-WISE PREDICTION / RANKING
# ==========================================

def get_role_feature_set(role_name):
    """
    Return features and fallback score column for each role.
    """
    if role_name == "Batsman":
        features = ['Runs', 'Avg', 'strike_rate', 'runs_per_match', 'bat_consistency', 'recent_player']
        fallback_score = 'batsman_score'

    elif role_name == "Bowler":
        features = ['Wkt', 'Avg_2', 'wickets_per_match', 'bowling_quality', 'runs_per_wicket_proxy', 'recent_player']
        fallback_score = 'bowler_score'

    elif role_name == "AllRounder":
        features = [
            'Runs', 'Avg', 'Wkt', 'Avg_2',
            'batsman_score', 'bowler_score', 'fielding_score', 'recent_player'
        ]
        fallback_score = 'allrounder_score'

    elif role_name == "Wicketkeeper":
        features = [
            'Runs', 'Avg', 'Ca', 'St',
            'batsman_score', 'fielding_score', 'recent_player'
        ]
        fallback_score = 'wicketkeeper_score'

    else:
        raise ValueError(f"Unknown role: {role_name}")

    return features, fallback_score


def generate_role_predictions(df, role_name):
    """
    For a given role:
    - filter role players
    - if Selected labels exist with both 0 and 1 -> use ML
    - otherwise -> use normalized fallback ranking score
    """
    role_df = df[df['Role'] == role_name].copy()

    if role_df.empty:
        print(f"No players found for role: {role_name}")
        return pd.DataFrame(columns=['Name', 'Role', 'selection_probability'])

    features, fallback_score = get_role_feature_set(role_name)

    # Ensure features exist
    for col in features:
        if col not in role_df.columns:
            role_df[col] = 0

    X = role_df[features].copy().fillna(0)

    # ------------------------------------------
    # CASE 1: ML MODE if usable Selected labels exist
    # ------------------------------------------
    usable_selected = role_df['Selected'].dropna()

    if len(usable_selected) > 0 and usable_selected.nunique() >= 2:
        y = role_df['Selected'].fillna(0).astype(int)

        scaler = MinMaxScaler()
        X_scaled = scaler.fit_transform(X)

        model = RandomForestClassifier(
            n_estimators=200,
            random_state=42
        )
        model.fit(X_scaled, y)

        proba = model.predict_proba(X_scaled)

        # binary class safeguard
        if proba.shape[1] == 2:
            role_df['selection_probability'] = proba[:, 1]
        else:
            # weird one-class edge case fallback
            role_df['selection_probability'] = role_df[fallback_score]

    # ------------------------------------------
    # CASE 2: RANKING MODE
    # ------------------------------------------
    else:
        score = role_df[fallback_score].fillna(0)

        # Normalize score to 0-1
        min_score = score.min()
        max_score = score.max()

        if max_score > min_score:
            role_df['selection_probability'] = (score - min_score) / (max_score - min_score)
        else:
            role_df['selection_probability'] = 0.5

    role_df = role_df.sort_values('selection_probability', ascending=False).reset_index(drop=True)

    return role_df

# ==========================================
# FINAL 15-MEMBER SQUAD BUILDER
# ==========================================

def build_final_squad(best_batsmen, best_bowlers, best_allrounders, best_wicketkeepers):
    """
    Final squad structure:
    Main XI:
        4 batsmen
        2 allrounders
        1 wicketkeeper
        4 bowlers
    Reserves:
        1 batsman
        1 wicketkeeper
        1 allrounder
        1 bowler
    """

    # Main XI
    main_batsmen = best_batsmen.head(4)
    main_allrounders = best_allrounders.head(2)
    main_wicketkeeper = best_wicketkeepers.head(1)
    main_bowlers = best_bowlers.head(4)

    # Reserves
    reserve_batsman = best_batsmen.iloc[4:5]
    reserve_wicketkeeper = best_wicketkeepers.iloc[1:2]
    reserve_allrounder = best_allrounders.iloc[2:3]
    reserve_bowler = best_bowlers.iloc[4:5]

    final_squad = pd.concat([
        main_batsmen,
        main_allrounders,
        main_wicketkeeper,
        main_bowlers,
        reserve_batsman,
        reserve_wicketkeeper,
        reserve_allrounder,
        reserve_bowler
    ], ignore_index=True)

    # Keep only expected columns if available
    output_cols = ['Name', 'Role', 'selection_probability']
    existing_output_cols = [c for c in output_cols if c in final_squad.columns]

    return final_squad[existing_output_cols]

# ==========================================
# MAIN GENERIC SQUAD PIPELINE
# ==========================================

def generate_squad_from_csv(csv_path, min_last_year=None, verbose=True):
    """
    Generic squad generator from uploaded cricket CSV.

    Parameters
    ----------
    csv_path : str
        Path to CSV file
    min_last_year : int or None
        If set, keep only players with Last >= min_last_year
        BUT if dataset has no usable Last, it will skip filtering gracefully
    verbose : bool
        Print debug info

    Returns
    -------
    dict with:
        standardized_df
        best_batsmen
        best_bowlers
        best_allrounders
        best_wicketkeepers
        final_squad
        info
        errors
        warnings
    """
    raw_df = pd.read_csv(csv_path)

    # 1) standardize
    df, info = standardize_uploaded_csv(raw_df, verbose=verbose)

    # 2) validate
    errors, warnings = validate_dataset(df)

    if errors:
        print("\nDATASET ERRORS:")
        for e in errors:
            print("-", e)
        return {
            "standardized_df": df,
            "best_batsmen": pd.DataFrame(),
            "best_bowlers": pd.DataFrame(),
            "best_allrounders": pd.DataFrame(),
            "best_wicketkeepers": pd.DataFrame(),
            "final_squad": pd.DataFrame(),
            "info": info,
            "errors": errors,
            "warnings": warnings
        }

    if warnings and verbose:
        print("\nDATASET WARNINGS:")
        for w in warnings:
            print("-", w)

    # 3) optional freshness filter
    if min_last_year is not None:
        usable_last_count = (df['Last'].fillna(0) > 0).sum()

        if usable_last_count > 0:
            df = df[df['Last'] >= min_last_year].copy()

            if verbose:
                print(f"\nApplied freshness filter: Last >= {min_last_year}")
                print("Remaining players:", len(df))
        else:
            if verbose:
                print(f"\nSkipped freshness filter because usable 'Last' values are not available.")

    # 4) create features
    df = create_features(df)

    # 5) role-wise predictions
    best_batsmen = generate_role_predictions(df, "Batsman")
    best_bowlers = generate_role_predictions(df, "Bowler")
    best_allrounders = generate_role_predictions(df, "AllRounder")
    best_wicketkeepers = generate_role_predictions(df, "Wicketkeeper")

    # 6) build final squad
    final_squad = build_final_squad(
        best_batsmen,
        best_bowlers,
        best_allrounders,
        best_wicketkeepers
    )

    return {
        "standardized_df": df,
        "best_batsmen": best_batsmen,
        "best_bowlers": best_bowlers,
        "best_allrounders": best_allrounders,
        "best_wicketkeepers": best_wicketkeepers,
        "final_squad": final_squad,
        "info": info,
        "errors": errors,
        "warnings": warnings
    }

# =========================================================
# CLI ENTRY
# Just for testing
# =========================================================

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(json.dumps({
            "success": False,
            "errors": ["CSV path not provided"]
        }))
        sys.exit(1)

    csv_path = sys.argv[1]
    min_last_year = 2022

    if len(sys.argv) >= 3:
        try:
            min_last_year = int(sys.argv[2])
        except:
            pass

    try:
        result = generate_squad_from_csv(csv_path, min_last_year=min_last_year)
        print(json.dumps(result, indent=2))
    except Exception as e:
        print(json.dumps({
            "success": False,
            "errors": [str(e)]
        }))
        sys.exit(1)
