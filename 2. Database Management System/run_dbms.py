"""
Sports Tournament Management System - Database Executor
Velalar College of Engineering and Technology, Erode-12
Department of Information Technology

This script creates an SQLite database, executes the SQL schema,
loads sample data, and runs all queries with clean, human-readable table outputs.
"""

import sqlite3
import os
import sys
import csv

# Human-readable column name mappings
HUMAN_COL_NAMES = {
    'team_id': 'Team ID',
    'team_name': 'Team Name',
    'dept_name': 'Department',
    'sport_name': 'Sport',
    'player_id': 'Player ID',
    'player_name': 'Player Name',
    'jersey_no': 'Jersey #',
    'match_id': 'Match ID',
    'team1_name': 'Team 1',
    'team2_name': 'Team 2',
    'team1_score': 'Score 1',
    'team2_score': 'Score 2',
    'winner': 'Match Winner',
    'match_time': 'Date & Time',
    'matches_played': 'Matches',
    'wins': 'Won',
    'draws': 'Draw',
    'losses': 'Lost',
    'goals_for': 'Goals For',
    'goals_against': 'Goals Against',
    'goal_diff': 'Goal Diff',
    'total_points': 'Points',
    'total_goals': 'Total Goals',
    'total_matches': 'Total Matches',
    'avg_goals': 'Avg Goals/Match',
    'max_goals': 'Max Match Goals',
    'min_goals': 'Min Match Goals',
    'result_type': 'Match Outcome',
    'count': 'Frequency',
    'player_count': 'Total Players'
}

def print_separator(title=""):
    print("\n" + "=" * 74)
    if title:
        print(f"  {title}")
        print("=" * 74)

def print_table(columns, rows):
    """Prints database rows in a beautiful, clean ASCII table with proper borders."""
    clean_cols = [HUMAN_COL_NAMES.get(c, c.replace('_', ' ').title()) for c in columns]
    widths = []
    for i, col in enumerate(clean_cols):
        max_w = len(col)
        for r in rows:
            val_str = str(r[i]) if r[i] is not None else "N/A"
            max_w = max(max_w, len(val_str))
        widths.append(max_w)

    border = "+-" + "-+-".join("-" * w for w in widths) + "-+"

    print("  " + border)
    header_line = "| " + " | ".join(col.ljust(widths[i]) for i, col in enumerate(clean_cols)) + " |"
    print("  " + header_line)
    print("  " + border)
    for r in rows:
        row_line = "| " + " | ".join((str(r[i]) if r[i] is not None else "N/A").ljust(widths[i]) for i in range(len(widths))) + " |"
        print("  " + row_line)
    print("  " + border)
    print(f"  Total records: {len(rows)}")

def execute_sql_file(cursor, filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        sql = f.read()
    try:
        cursor.executescript(sql)
        return sum(1 for s in sql.split(';') if s.strip() and not s.strip().startswith('--'))
    except sqlite3.Error as e:
        print(f"  Warning: {e}")
        return 0

def run_query(cursor, query, title, description=""):
    print("\n" + "-" * 74)
    print(f"  {title}")
    if description:
        print(f"  Purpose: {description}")
    print("-" * 74)

    try:
        cursor.execute(query)
        rows = cursor.fetchall()
        if not rows:
            print("  (No records found)")
            return
        columns = [desc[0] for desc in cursor.description]
        print_table(columns, rows)
    except sqlite3.Error as e:
        print(f"  SQL Error: {e}")

def main():
    print("=" * 74)
    print("  SPORTS TOURNAMENT MANAGEMENT SYSTEM")
    print("  DATABASE MANAGEMENT SYSTEM - SQL EXECUTION REPORT")
    print("  Velalar College of Engineering and Technology, Erode-12")
    print("  Department of Information Technology")
    print("=" * 74)

    script_dir = os.path.dirname(os.path.abspath(__file__))
    sql_dir = os.path.join(script_dir, "sql_scripts")
    db_path = os.path.join(script_dir, "sports_tournament.db")

    if os.path.exists(db_path):
        os.remove(db_path)
        print("\n  [INFO] Initialized fresh database.")

    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    cursor.execute("PRAGMA foreign_keys = ON;")
    print(f"  [INFO] Database created: {os.path.basename(db_path)}")

    # ========== Phase 1: Create Schema ==========
    print_separator("PHASE 1: CREATING DATABASE SCHEMA (DDL)")
    schema_file = os.path.join(sql_dir, "01_schema.sql")
    count = execute_sql_file(cursor, schema_file)
    conn.commit()

    cursor.execute("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;")
    tables = [t[0] for t in cursor.fetchall()]
    print("  Tables Created Successfully:")
    for i, t in enumerate(tables, 1):
        print(f"    {i:2d}. {t}")

    cursor.execute("SELECT name FROM sqlite_master WHERE type='view' ORDER BY name;")
    views = [v[0] for v in cursor.fetchall()]
    print("\n  SQL Views Created Successfully:")
    for i, v in enumerate(views, 1):
        print(f"    {i:2d}. {v}")

    # ========== Phase 2: Load Sample Data ==========
    print_separator("PHASE 2: LOADING SAMPLE DATA (DML)")
    data_file = os.path.join(sql_dir, "02_sample_data.sql")
    count = execute_sql_file(cursor, data_file)
    conn.commit()

    print("  Inserted Records Summary:")
    print("  +-----------------------+--------------------+")
    print("  | Table Name            | Record Count       |")
    print("  +-----------------------+--------------------+")
    for t in tables:
        cursor.execute(f"SELECT COUNT(*) FROM {t}")
        cnt = cursor.fetchone()[0]
        print(f"  | {t:<21} | {cnt:<18} |")
    print("  +-----------------------+--------------------+")

    # ========== Phase 3: Execute Queries ==========
    print_separator("PHASE 3: EXECUTING SQL QUERIES (10 QUERIES)")

    # Query 1
    run_query(cursor, """
        SELECT t.team_id, t.team_name, d.dept_name, s.sport_name
        FROM Team t
        JOIN Department d ON t.dept_id = d.dept_id
        JOIN Sport s ON t.sport_id = s.sport_id
        ORDER BY t.team_id
    """, "QUERY 1: Participating Teams, Departments & Sports",
    "INNER JOIN linking Team, Department, and Sport entities")

    # Query 2
    run_query(cursor, """
        SELECT p.player_id, p.player_name, p.jersey_no, t.team_name, d.dept_name
        FROM Player p
        JOIN Team t ON p.team_id = t.team_id
        JOIN Department d ON t.dept_id = d.dept_id
        ORDER BY p.player_id
    """, "QUERY 2: Player Registration Details & Team Roster",
    "Multi-table JOIN displaying student player details and their teams")

    # Query 3
    run_query(cursor, """
        SELECT match_id, team1_name, team2_name, team1_score, team2_score, winner, match_time
        FROM v_match_results ORDER BY match_id
    """, "QUERY 3: Match Schedule & Results (from SQL View)",
    "Retrieves formatted match results with scores and computed winners from v_match_results")

    # Query 4
    run_query(cursor, """
        SELECT team_name, matches_played, wins, draws, losses,
               goals_for, goals_against,
               (goals_for - goals_against) AS goal_diff,
               (wins * 3 + draws * 1) AS total_points
        FROM v_standings WHERE tournament_id = 1
        ORDER BY total_points DESC, goal_diff DESC
    """, "QUERY 4: Tournament Standings & Points Table",
    "Calculates points (Win=3, Draw=1, Loss=0) and ranks teams using SQL view & aggregation")

    # Query 5
    run_query(cursor, """
        SELECT p.player_name, t.team_name,
               SUM(ps.points_scored) AS total_goals,
               COUNT(ps.match_id) AS matches_played
        FROM PlayerMatchStat ps
        JOIN Player p ON ps.player_id = p.player_id
        JOIN Team t ON p.team_id = t.team_id
        WHERE ps.played = 1
        GROUP BY p.player_id, p.player_name, t.team_name
        HAVING SUM(ps.points_scored) > 0
        ORDER BY total_goals DESC
    """, "QUERY 5: Top Goal Scorers Leaderboard",
    "GROUP BY player with HAVING clause filtering active scorers")

    # Query 6
    run_query(cursor, """
        SELECT team_name, wins, goals_for
        FROM v_standings
        WHERE tournament_id = 1
          AND wins = (SELECT MAX(wins) FROM v_standings WHERE tournament_id = 1)
    """, "QUERY 6: Team(s) with Highest Wins",
    "Nested SUBQUERY finding team(s) matching maximum win count")

    # Query 7
    run_query(cursor, """
        SELECT COUNT(*) AS total_matches,
               SUM(team1_score + team2_score) AS total_goals,
               ROUND(AVG(team1_score + team2_score * 1.0), 2) AS avg_goals,
               MAX(team1_score + team2_score) AS max_goals,
               MIN(team1_score + team2_score) AS min_goals
        FROM MatchSchedule WHERE status = 'COMPLETED'
    """, "QUERY 7: Tournament Match Scoring Summary Statistics",
    "Aggregate functions COUNT, SUM, AVG, MAX, MIN over all completed matches")

    # Query 8
    run_query(cursor, """
        SELECT CASE
            WHEN team1_score > team2_score THEN 'Home Team Win'
            WHEN team2_score > team1_score THEN 'Away Team Win'
            ELSE 'Drawn Match'
        END AS result_type, COUNT(*) AS count
        FROM MatchSchedule WHERE status = 'COMPLETED'
        GROUP BY result_type
    """, "QUERY 8: Match Outcome Distribution",
    "Conditional CASE statement categorized into Home Win, Away Win, and Draw")

    # Query 9
    run_query(cursor, """
        SELECT p.player_id, p.player_name, t.team_name
        FROM Player p
        JOIN Team t ON p.team_id = t.team_id
        LEFT JOIN PlayerMatchStat ps ON p.player_id = ps.player_id
        WHERE ps.player_id IS NULL
    """, "QUERY 9: Registered Players Awaiting First Match",
    "LEFT OUTER JOIN with IS NULL filter identifying unused substitute players")

    # Query 10
    run_query(cursor, """
        SELECT d.dept_name, t.team_name,
               COALESCE(SUM(ps.points_scored), 0) AS total_goals
        FROM Department d
        JOIN Team t ON d.dept_id = t.dept_id
        LEFT JOIN Player p ON t.team_id = p.team_id
        LEFT JOIN PlayerMatchStat ps ON p.player_id = ps.player_id AND ps.played = 1
        GROUP BY d.dept_name, t.team_name
        ORDER BY total_goals DESC
    """, "QUERY 10: Department-wise Total Goals Scored",
    "Nested JOIN and GROUP BY aggregating total tournament goals by academic department")

    # ========== Phase 4: Export data for Python analysis ==========
    print_separator("PHASE 4: EXPORTING CLEAN CSV DATASETS")
    csv_dir = os.path.join(os.path.dirname(script_dir), "4. Foundation of Data Science", "data")
    os.makedirs(csv_dir, exist_ok=True)

    # 1. Matches
    cursor.execute("""
        SELECT ms.match_id, t.tournament_name, t1.team_name AS team1, t2.team_name AS team2,
               ms.team1_score, ms.team2_score, ms.match_time, ms.status
        FROM MatchSchedule ms
        JOIN Tournament t ON ms.tournament_id = t.tournament_id
        JOIN Team t1 ON ms.team1_id = t1.team_id
        JOIN Team t2 ON ms.team2_id = t2.team_id
    """)
    rows = cursor.fetchall()
    cols = [d[0] for d in cursor.description]
    with open(os.path.join(csv_dir, "match_results.csv"), 'w', newline='', encoding='utf-8') as f:
        w = csv.writer(f)
        w.writerow(cols)
        w.writerows(rows)
    print(f"  [x] Exported {len(rows)} match records  -> 4. Foundation of Data Science/data/match_results.csv")

    # 2. Standings
    cursor.execute("""
        SELECT team_name, matches_played, wins, draws, losses,
               goals_for, goals_against,
               (goals_for - goals_against) AS goal_diff,
               (wins * 3 + draws * 1) AS total_points
        FROM v_standings WHERE tournament_id = 1
        ORDER BY total_points DESC
    """)
    rows = cursor.fetchall()
    cols = [d[0] for d in cursor.description]
    with open(os.path.join(csv_dir, "standings.csv"), 'w', newline='', encoding='utf-8') as f:
        w = csv.writer(f)
        w.writerow(cols)
        w.writerows(rows)
    print(f"  [x] Exported {len(rows)} standing records -> 4. Foundation of Data Science/data/standings.csv")

    # 3. Player stats
    cursor.execute("""
        SELECT p.player_name, t.team_name, d.dept_name,
               COUNT(ps.match_id) AS matches, SUM(ps.points_scored) AS goals
        FROM Player p
        JOIN Team t ON p.team_id = t.team_id
        JOIN Department d ON t.dept_id = d.dept_id
        LEFT JOIN PlayerMatchStat ps ON p.player_id = ps.player_id AND ps.played = 1
        GROUP BY p.player_id, p.player_name, t.team_name, d.dept_name
        ORDER BY goals DESC
    """)
    rows = cursor.fetchall()
    cols = [d[0] for d in cursor.description]
    with open(os.path.join(csv_dir, "player_stats.csv"), 'w', newline='', encoding='utf-8') as f:
        w = csv.writer(f)
        w.writerow(cols)
        w.writerows(rows)
    print(f"  [x] Exported {len(rows)} player records   -> 4. Foundation of Data Science/data/player_stats.csv")

    # 4. Teams
    cursor.execute("""
        SELECT t.team_id, t.team_name, d.dept_name, s.sport_name,
               COUNT(p.player_id) AS player_count
        FROM Team t
        JOIN Department d ON t.dept_id = d.dept_id
        JOIN Sport s ON t.sport_id = s.sport_id
        LEFT JOIN Player p ON t.team_id = p.team_id
        GROUP BY t.team_id, t.team_name, d.dept_name, s.sport_name
    """)
    rows = cursor.fetchall()
    cols = [d[0] for d in cursor.description]
    with open(os.path.join(csv_dir, "teams.csv"), 'w', newline='', encoding='utf-8') as f:
        w = csv.writer(f)
        w.writerow(cols)
        w.writerows(rows)
    print(f"  [x] Exported {len(rows)} team records     -> 4. Foundation of Data Science/data/teams.csv")

    conn.close()

    print_separator("DATABASE EXECUTION COMPLETED SUCCESSFULLY")
    print(f"  Database file created: sports_tournament.db")
    print(f"  All 10 queries executed with structured human-readable tables.")
    print("=" * 74)

if __name__ == "__main__":
    main()
