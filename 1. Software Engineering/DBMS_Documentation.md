# Database Management Documentation

## 1. Normalization
The design follows **Third Normal Form (3NF)**:
1. **1NF** – All tables have atomic values; primary keys uniquely identify rows.
2. **2NF** – Every non‑key attribute fully depends on the whole primary key.  For example, `PlayerMatchStat` has a composite PK `(match_id, player_id)` and non‑key columns (`points_scored`, `played`) depend on both parts.
3. **3NF** – No transitive dependencies.  Department name depends only on `dept_id`; sport name depends only on `sport_id`; scoring rule fields depend only on `sport_id`.

All foreign keys enforce referential integrity, preventing orphan records.

## 2. Entity‑Relationship (ER) Diagram
The ER diagram is stored as `ER_Diagram.png` in the `diagrams/` folder of the DBMS category.

## 3. Relational Model (SQL DDL)
The full schema is defined in `sql_scripts/01_schema.sql`.  Key points:
- **Primary Keys:** `dept_id`, `sport_id`, `team_id`, `player_id`, `tournament_id`, `match_id`.
- **Foreign Keys:** Enforce relationships between `Team`↔`Department`, `Team`↔`Sport`, `MatchSchedule`↔`Tournament`/`Team`, `PlayerMatchStat`↔`MatchSchedule`/`Player`.
- **Views:** `v_match_results` and `v_standings` provide pre‑joined, readable representations for reporting and Python analysis.

## 4. Sample Queries (SQL)
The `sql_scripts/03_queries.sql` file contains ten example queries demonstrating:
- Joins (inner, left)
- Aggregations (SUM, COUNT, AVG)
- Sub‑queries and HAVING clauses
- Use of the views for concise reporting

## 5. Data Export for Data Science
The Python script `run_dbms.py` exports the following CSV files to `4. Foundation of Data Science/data`:
- `match_results.csv`
- `standings.csv`
- `player_stats.csv`
- `teams.csv`
These files are consumed by the `analysis.py` script to generate visualizations.

---
*Documentation generated on 2026‑11‑17*