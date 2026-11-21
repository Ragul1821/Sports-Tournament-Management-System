# Normalization Document
## Sports Tournament Management System

**Velalar College of Engineering and Technology, Erode-12**  
**Department of Information Technology**

---

## 1. Unnormalized Form (UNF)

In the unnormalized form, all tournament data might be stored in a single flat table:

```
TournamentData(
    match_id, tournament_name, start_date, sport_name,
    team1_name, team1_dept, team2_name, team2_dept,
    team1_score, team2_score, match_time, status,
    player_name, jersey_no, points_scored, played,
    win_pts, draw_pts, loss_pts
)
```

**Problems:** Repeating groups, data redundancy, update/insert/delete anomalies.

---

## 2. First Normal Form (1NF)

**Rule:** Eliminate repeating groups; each column must have atomic values.

After applying 1NF, we separate multi-valued attributes:

```
Match(match_id, tournament_name, sport_name, team1_name, team1_dept,
      team2_name, team2_dept, team1_score, team2_score, match_time, status)

PlayerStat(match_id, player_name, jersey_no, team_name, points_scored, played)

ScoringRule(sport_name, win_pts, draw_pts, loss_pts)
```

✅ No repeating groups  
✅ All values are atomic  
❌ Still has partial and transitive dependencies

---

## 3. Second Normal Form (2NF)

**Rule:** Must be in 1NF + no partial dependencies (every non-key attribute must depend on the whole primary key).

**Identifying partial dependencies:**
- In `Match`: `tournament_name` and `sport_name` depend only on `tournament_id`, not on `match_id`
- In `PlayerStat`: `jersey_no` and `team_name` depend only on `player_id`, not on the composite key

After applying 2NF:

```
Tournament(tournament_id, tournament_name, sport_name, start_date)

MatchSchedule(match_id, tournament_id, team1_name, team2_name,
              team1_score, team2_score, match_time, status)

Player(player_id, player_name, jersey_no, team_name)

PlayerMatchStat(match_id, player_id, points_scored, played)

ScoringRule(sport_name, win_pts, draw_pts, loss_pts)
```

✅ No partial dependencies  
❌ Still has transitive dependencies

---

## 4. Third Normal Form (3NF)

**Rule:** Must be in 2NF + no transitive dependencies (non-key attributes must not depend on other non-key attributes).

**Identifying transitive dependencies:**
- In `MatchSchedule`: `team1_name` → `team1_dept` (team name determines department)
- In `Player`: `team_name` → `dept_name` (team determines department)
- In `Tournament`: `sport_name` can be normalized to `sport_id`

After applying 3NF:

```
Department(dept_id PK, dept_name)

Sport(sport_id PK, sport_name)

ScoringRule(sport_id PK/FK, win_pts, draw_pts, loss_pts)

Team(team_id PK, team_name, dept_id FK, sport_id FK)

Player(player_id PK, player_name, team_id FK, jersey_no)

Tournament(tournament_id PK, tournament_name, sport_id FK, start_date)

TeamRegistration(team_id PK/FK, tournament_id PK/FK, registration_date)

MatchSchedule(match_id PK, tournament_id FK, team1_id FK, team2_id FK,
              match_time, team1_score, team2_score, status)

PlayerMatchStat(match_id PK/FK, player_id PK/FK, points_scored, played)
```

✅ No transitive dependencies  
✅ All tables in 3NF

---

## 5. Functional Dependencies

### Department
- `dept_id → dept_name`

### Sport
- `sport_id → sport_name`

### ScoringRule
- `sport_id → win_pts, draw_pts, loss_pts`

### Team
- `team_id → team_name, dept_id, sport_id`

### Player
- `player_id → player_name, team_id, jersey_no`

### Tournament
- `tournament_id → tournament_name, sport_id, start_date`

### TeamRegistration
- `{team_id, tournament_id} → registration_date`

### MatchSchedule
- `match_id → tournament_id, team1_id, team2_id, match_time, team1_score, team2_score, status`

### PlayerMatchStat
- `{match_id, player_id} → points_scored, played`

---

## 6. Summary

| Normal Form | Issues Addressed | Tables |
|-------------|-----------------|--------|
| UNF → 1NF | Removed repeating groups, ensured atomic values | 3 |
| 1NF → 2NF | Removed partial dependencies | 5 |
| 2NF → 3NF | Removed transitive dependencies | 9 |

**Final schema has 9 tables, all in Third Normal Form (3NF).**

All tables satisfy BCNF (Boyce-Codd Normal Form) as well, since in each table every determinant is a candidate key.
