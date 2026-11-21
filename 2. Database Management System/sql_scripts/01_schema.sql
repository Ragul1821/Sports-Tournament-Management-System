-- ============================================================
-- Sports Tournament Management System - Database Schema
-- Velalar College of Engineering and Technology, Erode
-- Database: sports_tournament (MySQL 8 / SQLite compatible)
-- ============================================================

-- Table 1: Department
CREATE TABLE IF NOT EXISTS Department (
    dept_id     INTEGER PRIMARY KEY,
    dept_name   VARCHAR(100) NOT NULL UNIQUE
);

-- Table 2: Sport
CREATE TABLE IF NOT EXISTS Sport (
    sport_id    INTEGER PRIMARY KEY,
    sport_name  VARCHAR(50) NOT NULL UNIQUE
);

-- Table 3: ScoringRule (1:1 with Sport)
CREATE TABLE IF NOT EXISTS ScoringRule (
    sport_id    INTEGER PRIMARY KEY,
    win_pts     INTEGER NOT NULL DEFAULT 3,
    draw_pts    INTEGER NOT NULL DEFAULT 1,
    loss_pts    INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (sport_id) REFERENCES Sport(sport_id)
);

-- Table 4: Team
CREATE TABLE IF NOT EXISTS Team (
    team_id     INTEGER PRIMARY KEY,
    team_name   VARCHAR(100) NOT NULL,
    dept_id     INTEGER NOT NULL,
    sport_id    INTEGER NOT NULL,
    FOREIGN KEY (dept_id)  REFERENCES Department(dept_id),
    FOREIGN KEY (sport_id) REFERENCES Sport(sport_id)
);

-- Table 5: Player
CREATE TABLE IF NOT EXISTS Player (
    player_id   INTEGER PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    team_id     INTEGER NOT NULL,
    jersey_no   INTEGER NOT NULL,
    FOREIGN KEY (team_id) REFERENCES Team(team_id)
);

-- Table 6: Tournament
CREATE TABLE IF NOT EXISTS Tournament (
    tournament_id   INTEGER PRIMARY KEY,
    tournament_name VARCHAR(200) NOT NULL,
    sport_id        INTEGER NOT NULL,
    start_date      DATE NOT NULL,
    FOREIGN KEY (sport_id) REFERENCES Sport(sport_id)
);

-- Table 7: TeamRegistration (M:N between Team and Tournament)
CREATE TABLE IF NOT EXISTS TeamRegistration (
    team_id         INTEGER NOT NULL,
    tournament_id   INTEGER NOT NULL,
    registration_date DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (team_id, tournament_id),
    FOREIGN KEY (team_id)       REFERENCES Team(team_id),
    FOREIGN KEY (tournament_id) REFERENCES Tournament(tournament_id)
);

-- Table 8: MatchSchedule
CREATE TABLE IF NOT EXISTS MatchSchedule (
    match_id        INTEGER PRIMARY KEY,
    tournament_id   INTEGER NOT NULL,
    team1_id        INTEGER NOT NULL,
    team2_id        INTEGER NOT NULL,
    match_time      DATETIME NOT NULL,
    team1_score     INTEGER DEFAULT NULL,
    team2_score     INTEGER DEFAULT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    FOREIGN KEY (tournament_id) REFERENCES Tournament(tournament_id),
    FOREIGN KEY (team1_id)      REFERENCES Team(team_id),
    FOREIGN KEY (team2_id)      REFERENCES Team(team_id),
    CHECK (team1_id <> team2_id),
    CHECK (status IN ('SCHEDULED','COMPLETED','CANCELLED'))
);

-- Table 9: PlayerMatchStat
CREATE TABLE IF NOT EXISTS PlayerMatchStat (
    match_id        INTEGER NOT NULL,
    player_id       INTEGER NOT NULL,
    points_scored   INTEGER NOT NULL DEFAULT 0,
    played          BOOLEAN NOT NULL DEFAULT 1,
    PRIMARY KEY (match_id, player_id),
    FOREIGN KEY (match_id)  REFERENCES MatchSchedule(match_id),
    FOREIGN KEY (player_id) REFERENCES Player(player_id)
);

-- ============================================================
-- VIEW: v_match_results - Detailed match results with team names
-- ============================================================
CREATE VIEW IF NOT EXISTS v_match_results AS
SELECT
    ms.match_id,
    t.tournament_name,
    t1.team_name AS team1_name,
    t2.team_name AS team2_name,
    ms.team1_score,
    ms.team2_score,
    ms.match_time,
    ms.status,
    CASE
        WHEN ms.team1_score > ms.team2_score THEN t1.team_name
        WHEN ms.team2_score > ms.team1_score THEN t2.team_name
        ELSE 'DRAW'
    END AS winner
FROM MatchSchedule ms
JOIN Tournament t  ON ms.tournament_id = t.tournament_id
JOIN Team t1       ON ms.team1_id = t1.team_id
JOIN Team t2       ON ms.team2_id = t2.team_id;

-- ============================================================
-- VIEW: v_standings - Tournament standings
-- ============================================================
CREATE VIEW IF NOT EXISTS v_standings AS
SELECT
    tr.tournament_id,
    t.team_name,
    COUNT(ms.match_id) AS matches_played,
    SUM(CASE
        WHEN (ms.team1_id = t.team_id AND ms.team1_score > ms.team2_score)
          OR (ms.team2_id = t.team_id AND ms.team2_score > ms.team1_score)
        THEN 1 ELSE 0
    END) AS wins,
    SUM(CASE
        WHEN ms.team1_score = ms.team2_score THEN 1 ELSE 0
    END) AS draws,
    SUM(CASE
        WHEN (ms.team1_id = t.team_id AND ms.team1_score < ms.team2_score)
          OR (ms.team2_id = t.team_id AND ms.team2_score < ms.team1_score)
        THEN 1 ELSE 0
    END) AS losses,
    SUM(CASE
        WHEN ms.team1_id = t.team_id THEN ms.team1_score
        ELSE ms.team2_score
    END) AS goals_for,
    SUM(CASE
        WHEN ms.team1_id = t.team_id THEN ms.team2_score
        ELSE ms.team1_score
    END) AS goals_against
FROM TeamRegistration tr
JOIN Team t ON tr.team_id = t.team_id
LEFT JOIN MatchSchedule ms
    ON ms.tournament_id = tr.tournament_id
    AND (ms.team1_id = t.team_id OR ms.team2_id = t.team_id)
    AND ms.status = 'COMPLETED'
GROUP BY tr.tournament_id, t.team_id, t.team_name;
