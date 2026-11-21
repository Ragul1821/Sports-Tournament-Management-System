"""
Data Science Analysis for Sports Tournament Management System
Velalar College of Engineering and Technology, Erode-12
Department of Information Technology

Statistical Analysis & Visualization Module
Requirements Satisfied:
- Direct Database Retrieval using SQLite3
- Robust Data Preprocessing (Cleaning, Imputation, Aggregation, Type Casting)
- Descriptive Statistical Analysis (Mean, Median, Mode, Variance, Standard Deviation)
- Matplotlib Performance Visualizations
"""

import os
import sys
import csv
import math
import sqlite3
import matplotlib.pyplot as plt

def retrieve_from_database(db_path):
    """
    Retrieves project dataset directly from the SQLite database.
    Satisfies Requirement 6: 'Retrieve the project dataset from the database using Python'
    """
    if not os.path.exists(db_path):
        return None

    try:
        conn = sqlite3.connect(db_path)
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()

        # 1. Matches from view v_match_results
        cursor.execute("""
            SELECT match_id, team1_name AS team1, team2_name AS team2, 
                   team1_score, team2_score, winner, status, match_time
            FROM v_match_results
            ORDER BY match_id
        """)
        matches = [dict(row) for row in cursor.fetchall()]

        # 2. Standings from view v_standings
        cursor.execute("""
            SELECT team_name, matches_played, wins AS won, draws AS draw, 
                   losses AS lost, goals_for, goals_against,
                   (goals_for - goals_against) AS goal_diff,
                   (wins * 3 + draws) AS total_points
            FROM v_standings
            WHERE tournament_id = 1
            ORDER BY total_points DESC, goal_diff DESC
        """)
        standings = [dict(row) for row in cursor.fetchall()]

        # 3. Top Scorers from PlayerMatchStat JOIN Player JOIN Team JOIN Department
        cursor.execute("""
            SELECT p.player_id, p.player_name, t.team_name, d.dept_name,
                   SUM(ps.points_scored) AS goals,
                   COUNT(ps.match_id) AS matches_played
            FROM PlayerMatchStat ps
            JOIN Player p ON ps.player_id = p.player_id
            JOIN Team t ON p.team_id = t.team_id
            JOIN Department d ON t.dept_id = d.dept_id
            WHERE ps.played = 1
            GROUP BY p.player_id, p.player_name, t.team_name, d.dept_name
            HAVING goals > 0
            ORDER BY goals DESC
        """)
        scorers = [dict(row) for row in cursor.fetchall()]

        conn.close()
        return {
            'matches': matches,
            'standings': standings,
            'scorers': scorers
        }
    except sqlite3.Error as e:
        print(f"  [DB WARNING] Database query failed: {e}. Falling back to CSV.")
        return None

def read_csv(path):
    """Fallback reader when database is not directly accessible."""
    if not os.path.exists(path):
        return []
    with open(path, newline='', encoding='utf-8') as f:
        return list(csv.DictReader(f))

def preprocess_data(data):
    """
    Performs data cleaning, validation, type conversions, and statistical aggregations.
    Satisfies Requirement 6: 'preprocess the data'
    """
    matches = data.get('matches', [])
    standings = data.get('standings', [])
    scorers = data.get('scorers', [])

    # Preprocess match records
    cleaned_matches = []
    for m in matches:
        try:
            m_id = str(m.get('match_id', ''))
            s1 = int(m.get('team1_score') or 0)
            s2 = int(m.get('team2_score') or 0)
            total = s1 + s2
            winner = m.get('winner') or ('DRAW' if s1 == s2 else (m.get('team1') if s1 > s2 else m.get('team2')))
            cleaned_matches.append({
                'match_id': m_id,
                'team1': str(m.get('team1', 'Unknown')),
                'team2': str(m.get('team2', 'Unknown')),
                'team1_score': s1,
                'team2_score': s2,
                'total_goals': total,
                'winner': winner,
                'status': m.get('status', 'COMPLETED'),
                'match_time': m.get('match_time', '')
            })
        except Exception:
            continue

    # Preprocess standings
    cleaned_standings = []
    for s in standings:
        try:
            gf = int(s.get('goals_for') or 0)
            ga = int(s.get('goals_against') or 0)
            cleaned_standings.append({
                'team_name': str(s.get('team_name', 'Unknown')),
                'matches_played': int(s.get('matches_played') or 0),
                'won': int(s.get('won') or 0),
                'draw': int(s.get('draw') or 0),
                'lost': int(s.get('lost') or 0),
                'goals_for': gf,
                'goals_against': ga,
                'goal_diff': gf - ga,
                'total_points': int(s.get('total_points') or 0)
            })
        except Exception:
            continue

    # Preprocess scorers
    cleaned_scorers = []
    for sc in scorers:
        try:
            cleaned_scorers.append({
                'player_id': str(sc.get('player_id', '')),
                'player_name': str(sc.get('player_name', 'Unknown')),
                'team_name': str(sc.get('team_name', 'Unknown')),
                'dept_name': str(sc.get('dept_name', 'Unknown')),
                'goals': int(sc.get('goals') or 0),
                'matches_played': int(sc.get('matches_played') or 0)
            })
        except Exception:
            continue

    return {
        'matches': cleaned_matches,
        'standings': cleaned_standings,
        'scorers': cleaned_scorers
    }

def compute_statistics(goals):
    """
    Computes rigorous descriptive statistics.
    Returns: mean, median, mode, variance, std_dev, min_val, max_val
    """
    if not goals:
        return 0, 0, 0, 0, 0, 0, 0

    n = len(goals)
    # Mean
    mean_val = sum(goals) / n

    # Median
    sorted_goals = sorted(goals)
    if n % 2 == 1:
        median_val = float(sorted_goals[n // 2])
    else:
        median_val = (sorted_goals[n // 2 - 1] + sorted_goals[n // 2]) / 2.0

    # Mode
    freq = {}
    for g in goals:
        freq[g] = freq.get(g, 0) + 1
    mode_val = max(freq.items(), key=lambda x: x[1])[0]

    # Variance & Standard Deviation
    variance = sum((x - mean_val) ** 2 for x in goals) / n
    std_dev = math.sqrt(variance)

    return mean_val, median_val, mode_val, variance, std_dev, min(goals), max(goals)

def generate_visualizations(vis_dir, preprocessed):
    """
    Generates high-resolution Matplotlib visualizations.
    Satisfies Requirement 6 and 10: 'Data Visualization Using Python'
    """
    matches = preprocessed['matches']
    standings = preprocessed['standings']
    scorers = preprocessed['scorers']

    # 1. Goals Per Match Bar Chart
    match_labels = [f"M#{m['match_id']}" for m in matches]
    match_goals = [m['total_goals'] for m in matches]

    plt.figure(figsize=(9, 5))
    bars = plt.bar(match_labels, match_goals, color='#2563eb', edgecolor='#1e293b', width=0.6)
    for bar in bars:
        h = bar.get_height()
        plt.text(bar.get_x() + bar.get_width() / 2.0, h + 0.1, f"{int(h)}", ha='center', va='bottom', fontsize=10, fontweight='bold')

    plt.title('Total Goals per Match - Inter-Dept Football Championship', fontsize=14, fontweight='bold', pad=12)
    plt.xlabel('Match Fixture', fontsize=12, labelpad=8)
    plt.ylabel('Goals Scored', fontsize=12, labelpad=8)
    plt.ylim(0, max(match_goals) + 1.2)
    plt.grid(axis='y', linestyle='--', alpha=0.6)
    plt.tight_layout()
    chart1_path = os.path.join(vis_dir, 'goals_per_match.png')
    plt.savefig(chart1_path, dpi=300)
    plt.close()
    print(f"  [x] Generated chart: visualizations/goals_per_match.png")

    # 2. Points Share Pie Chart
    standings_sorted = sorted(standings, key=lambda s: s['total_points'], reverse=True)
    teams = [s['team_name'] for s in standings_sorted]
    points = [s['total_points'] for s in standings_sorted]

    plt.figure(figsize=(7, 7))
    colors = ['#2563eb', '#10b981', '#f59e0b', '#ec4899', '#8b5cf6']
    plt.pie(points, labels=teams, autopct='%1.1f%%', startangle=140,
            colors=colors[:len(teams)],
            wedgeprops={'edgecolor': 'white', 'linewidth': 2})
    plt.title('Tournament Points Share by Team', fontsize=14, fontweight='bold', pad=12)
    plt.tight_layout()
    chart2_path = os.path.join(vis_dir, 'points_distribution.png')
    plt.savefig(chart2_path, dpi=300)
    plt.close()
    print(f"  [x] Generated chart: visualizations/points_distribution.png")

    # 3. Top Scorers Horizontal Bar Chart
    top5 = sorted(scorers, key=lambda sc: sc['goals'], reverse=True)[:5]
    player_names = [f"{sc['player_name']} ({sc['team_name'].split()[0]})" for sc in top5][::-1]
    player_goals = [sc['goals'] for sc in top5][::-1]

    plt.figure(figsize=(8, 5))
    hbars = plt.barh(player_names, player_goals, color='#d97706', edgecolor='#1e293b', height=0.55)
    for bar in hbars:
        w = bar.get_width()
        plt.text(w + 0.1, bar.get_y() + bar.get_height() / 2.0, f"{int(w)} Goals", ha='left', va='center', fontsize=10, fontweight='bold')

    plt.title('Golden Boot - Top 5 Goal Scorers', fontsize=14, fontweight='bold', pad=12)
    plt.xlabel('Goals Scored', fontsize=12, labelpad=8)
    plt.xlim(0, max(player_goals) + 1.5)
    plt.grid(axis='x', linestyle='--', alpha=0.6)
    plt.tight_layout()
    chart3_path = os.path.join(vis_dir, 'top_scorers.png')
    plt.savefig(chart3_path, dpi=300)
    plt.close()
    print(f"  [x] Generated chart: visualizations/top_scorers.png")

def main():
    print("=" * 74)
    print("  SPORTS TOURNAMENT MANAGEMENT AND PERFORMANCE ANALYSIS SYSTEM")
    print("  Foundation of Data Science - Database Retrieval & Statistical Analytics")
    print("  Velalar College of Engineering and Technology, Erode-12 (Autonomous)")
    print("  Department of Information Technology - 25IT3A & 25IT3B")
    print("=" * 74)

    base_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
    db_path = os.path.join(base_dir, '2. Database Management System', 'sports_tournament.db')
    data_dir = os.path.join(base_dir, '4. Foundation of Data Science', 'data')
    vis_dir = os.path.join(base_dir, '4. Foundation of Data Science', 'visualizations')
    os.makedirs(data_dir, exist_ok=True)
    os.makedirs(vis_dir, exist_ok=True)

    # --------------------------------------------------------------------------
    # Step 1: Database Retrieval (Requirement 6)
    # --------------------------------------------------------------------------
    print("\n" + "-" * 74)
    print("  [STEP 1] RETRIEVING DATASET FROM DATABASE (sqlite3)")
    print("-" * 74)

    raw_data = retrieve_from_database(db_path)
    if raw_data:
        print(f"  [SUCCESS] Successfully connected to SQLite database: sports_tournament.db")
        print(f"  - Retrieved {len(raw_data['matches'])} match records from view 'v_match_results'")
        print(f"  - Retrieved {len(raw_data['standings'])} team standing records from view 'v_standings'")
        print(f"  - Retrieved {len(raw_data['scorers'])} active player scoring records")
    else:
        print("  [INFO] Database not found or locked. Loading datasets from CSV fallback...")
        raw_data = {
            'matches': read_csv(os.path.join(data_dir, 'match_results.csv')),
            'standings': read_csv(os.path.join(data_dir, 'standings.csv')),
            'scorers': read_csv(os.path.join(data_dir, 'player_stats.csv'))
        }

    # --------------------------------------------------------------------------
    # Step 2: Data Preprocessing (Requirement 6)
    # --------------------------------------------------------------------------
    print("\n" + "-" * 74)
    print("  [STEP 2] DATA PREPROCESSING & CLEANING")
    print("-" * 74)

    preprocessed = preprocess_data(raw_data)
    matches = preprocessed['matches']
    standings = preprocessed['standings']
    scorers = preprocessed['scorers']

    print(f"  - Cleaned & type-cast {len(matches)} match fixtures (Scores, Outcomes, Winners)")
    print(f"  - Computed Goal Differentials & Point tallies for {len(standings)} teams")
    print(f"  - Ranked {len(scorers)} goal scorers across all college departments")

    # --------------------------------------------------------------------------
    # Step 3: Statistical Analysis (Requirement 6 & 10)
    # --------------------------------------------------------------------------
    print("\n" + "-" * 74)
    print("  [STEP 3] DESCRIPTIVE STATISTICAL ANALYSIS")
    print("-" * 74)

    all_goals = [m['total_goals'] for m in matches]
    mean_g, median_g, mode_g, var_g, std_g, min_g, max_g = compute_statistics(all_goals)

    print(f"  Total Matches Analyzed:          {len(matches)}")
    print(f"  Total Goals Scored:              {sum(all_goals)}")
    print(f"  Mean Goals per Match:            {mean_g:.2f}")
    print(f"  Median Goals per Match:          {median_g:.2f}")
    print(f"  Mode (Most Frequent Goals):      {mode_g:.2f}")
    print(f"  Variance of Goals:               {var_g:.2f}")
    print(f"  Standard Deviation:              {std_g:.2f}")
    print(f"  Range of Goals:                  {min_g} to {max_g} goals")

    # Outcome breakdown
    home_wins = sum(1 for m in matches if m['team1_score'] > m['team2_score'])
    away_wins = sum(1 for m in matches if m['team2_score'] > m['team1_score'])
    draws = sum(1 for m in matches if m['team1_score'] == m['team2_score'])

    print(f"\n  Match Outcome Distribution:")
    print(f"  - Home Team Wins:                {home_wins} ({home_wins/len(matches)*100:.1f}%)")
    print(f"  - Away Team Wins:                {away_wins} ({away_wins/len(matches)*100:.1f}%)")
    print(f"  - Drawn Fixtures:                {draws} ({draws/len(matches)*100:.1f}%)")

    # Standings summary
    print("\n  Official Tournament Points Standings:")
    print(f"  {'Rank':<5} | {'Team Name':<18} | {'P':<3} | {'W':<3} | {'D':<3} | {'L':<3} | {'GF':<3} | {'GA':<3} | {'GD':<4} | {'Pts':<4}")
    print(f"  {'-'*5}-+-{'-'*18}-+-{'-'*3}-+-{'-'*3}-+-{'-'*3}-+-{'-'*3}-+-{'-'*3}-+-{'-'*3}-+-{'-'*4}-+-{'-'*4}")
    standings_sorted = sorted(standings, key=lambda s: (s['total_points'], s['goal_diff']), reverse=True)
    for rank, s in enumerate(standings_sorted, 1):
        gd_str = f"+{s['goal_diff']}" if s['goal_diff'] > 0 else str(s['goal_diff'])
        print(f"  {rank:<5} | {s['team_name']:<18} | {s['matches_played']:<3} | {s['won']:<3} | {s['draw']:<3} | {s['lost']:<3} | {s['goals_for']:<3} | {s['goals_against']:<3} | {gd_str:<4} | {s['total_points']:<4}")

    # Top scorers summary
    print("\n  Top 5 Goal Scorers (Golden Boot):")
    top5 = sorted(scorers, key=lambda sc: sc['goals'], reverse=True)[:5]
    print(f"  {'Rank':<5} | {'Player Name':<16} | {'Team Name':<16} | {'Department':<28} | {'Goals':<6}")
    print(f"  {'-'*5}-+-{'-'*16}-+-{'-'*16}-+-{'-'*28}-+-{'-'*6}")
    for rank, sc in enumerate(top5, 1):
        print(f"  {rank:<5} | {sc['player_name']:<16} | {sc['team_name']:<16} | {sc['dept_name']:<28} | {sc['goals']:<6}")

    # --------------------------------------------------------------------------
    # Step 4: Python Data Visualizations (Requirement 10)
    # --------------------------------------------------------------------------
    print("\n" + "-" * 74)
    print("  [STEP 4] GENERATING DATA VISUALIZATIONS (Matplotlib)")
    print("-" * 74)

    generate_visualizations(vis_dir, preprocessed)

    # --------------------------------------------------------------------------
    # Step 5: Dashboard Verification
    # --------------------------------------------------------------------------
    dash_path = os.path.join(base_dir, "Tournament_Dashboard.html")
    if os.path.exists(dash_path):
        print(f"  [x] Verified Integrated 4-Subject Dashboard: Tournament_Dashboard.html")

    print("\n" + "=" * 74)
    print("  FOUNDATION OF DATA SCIENCE - ANALYSIS COMPLETED SUCCESSFULLY")
    print("  - SQLite Database Retrieved: sports_tournament.db")
    print("  - Visualizations Output in:  visualizations/")
    print("  - Interactive Dashboard:     Tournament_Dashboard.html")
    print("=" * 74)

if __name__ == '__main__':
    main()
