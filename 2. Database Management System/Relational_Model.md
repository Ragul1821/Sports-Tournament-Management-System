# Relational Model
## Sports Tournament Management System

**Velalar College of Engineering and Technology, Erode-12**  
**Department of Information Technology**

---

## Relational Schema

The database consists of **9 base tables** and **2 views**, all in Third Normal Form (3NF).

### Table Definitions

**1. Department** (<u>dept_id</u>, dept_name)
- Primary Key: dept_id
- Unique: dept_name

**2. Sport** (<u>sport_id</u>, sport_name)
- Primary Key: sport_id
- Unique: sport_name

**3. ScoringRule** (<u>sport_id*</u>, win_pts, draw_pts, loss_pts)
- Primary Key: sport_id
- Foreign Key: sport_id → Sport(sport_id)
- Cardinality: 1:1 with Sport

**4. Team** (<u>team_id</u>, team_name, dept_id*, sport_id*)
- Primary Key: team_id
- Foreign Key: dept_id → Department(dept_id)
- Foreign Key: sport_id → Sport(sport_id)

**5. Player** (<u>player_id</u>, player_name, team_id*, jersey_no)
- Primary Key: player_id
- Foreign Key: team_id → Team(team_id)

**6. Tournament** (<u>tournament_id</u>, tournament_name, sport_id*, start_date)
- Primary Key: tournament_id
- Foreign Key: sport_id → Sport(sport_id)

**7. TeamRegistration** (<u>team_id*</u>, <u>tournament_id*</u>, registration_date)
- Primary Key: (team_id, tournament_id) — Composite
- Foreign Key: team_id → Team(team_id)
- Foreign Key: tournament_id → Tournament(tournament_id)

**8. MatchSchedule** (<u>match_id</u>, tournament_id*, team1_id*, team2_id*, match_time, team1_score, team2_score, status)
- Primary Key: match_id
- Foreign Key: tournament_id → Tournament(tournament_id)
- Foreign Key: team1_id → Team(team_id)
- Foreign Key: team2_id → Team(team_id)
- Check: team1_id ≠ team2_id
- Check: status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')

**9. PlayerMatchStat** (<u>match_id*</u>, <u>player_id*</u>, points_scored, played)
- Primary Key: (match_id, player_id) — Composite
- Foreign Key: match_id → MatchSchedule(match_id)
- Foreign Key: player_id → Player(player_id)

> *Underlined* = Primary Key, *Asterisk (*)* = Foreign Key

---

## Relationship Mapping

| Relationship | Type | Entities | Implementation |
|-------------|------|----------|---------------|
| Department **has** Teams | 1:N | Department → Team | FK dept_id in Team |
| Sport **defines** ScoringRule | 1:1 | Sport → ScoringRule | FK/PK sport_id in ScoringRule |
| Sport **held in** Tournament | 1:N | Sport → Tournament | FK sport_id in Tournament |
| Team **has** Players | 1:N | Team → Player | FK team_id in Player |
| Team **registers** Tournament | M:N | Team ↔ Tournament | Junction table: TeamRegistration |
| Tournament **schedules** Matches | 1:N | Tournament → MatchSchedule | FK tournament_id in MatchSchedule |
| Team **plays** Match | 2:N | Team → MatchSchedule | FK team1_id, team2_id in MatchSchedule |
| Player **performs** in Match | M:N | Player ↔ MatchSchedule | Junction table: PlayerMatchStat |

---

## Integrity Constraints

1. **Entity Integrity:** All primary keys are NOT NULL and unique
2. **Referential Integrity:** All foreign keys reference valid primary keys
3. **Domain Integrity:** CHECK constraints on status field, scores default to NULL (unplayed)
4. **Business Rule:** team1_id ≠ team2_id (a team cannot play itself)

---

## Views

**v_match_results** — Joins MatchSchedule with Team names and derives winner
**v_standings** — Aggregates match results to compute W/D/L/GF/GA per team per tournament
