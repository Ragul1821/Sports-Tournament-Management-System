-- ============================================================
-- Sample Data for Sports Tournament Management System
-- Insert sample rows for each table to enable demo execution.
-- ============================================================

INSERT INTO Department (dept_id, dept_name) VALUES
    (1, 'Computer Science & Engineering'),
    (2, 'Information Technology'),
    (3, 'Electronics & Communication'),
    (4, 'Mechanical Engineering'),
    (5, 'Civil Engineering');

INSERT INTO Sport (sport_id, sport_name) VALUES
    (1, 'Cricket'),
    (2, 'Football'),
    (3, 'Basketball');

INSERT INTO ScoringRule (sport_id, win_pts, draw_pts, loss_pts) VALUES
    (1, 3, 1, 0),
    (2, 3, 1, 0),
    (3, 2, 0, 0);

INSERT INTO Team (team_id, team_name, dept_id, sport_id) VALUES
    (1, 'CSE Warriors', 1, 2),
    (2, 'IT Strikers', 2, 2),
    (3, 'ECE Thunders', 3, 2),
    (4, 'MECH Titans', 4, 2),
    (5, 'CIVIL Spartans', 5, 2);

INSERT INTO Player (player_id, player_name, team_id, jersey_no) VALUES
    (1, 'Arun Kumar', 1, 10),
    (2, 'Bharath S', 1, 7),
    (3, 'Chandru R', 1, 9),
    (4, 'Deepak M', 2, 11),
    (5, 'Ezhil V', 2, 8),
    (6, 'Ganesh P', 2, 5),
    (7, 'Hari K', 3, 12),
    (8, 'Inba S', 3, 3),
    (9, 'Jagan R', 3, 6),
    (10, 'Karthik N', 4, 1),
    (11, 'Lokesh T', 4, 4),
    (12, 'Mani V', 4, 2),
    (13, 'Naveen S', 5, 14),
    (14, 'Oviya R', 5, 15),
    (15, 'Priya K', 5, 13);

INSERT INTO Tournament (tournament_id, tournament_name, sport_id, start_date) VALUES
    (1, 'Inter-Dept Football Championship 2026', 2, '2026-11-20');

INSERT INTO TeamRegistration (team_id, tournament_id) VALUES
    (1,1),(2,1),(3,1),(4,1),(5,1);

INSERT INTO MatchSchedule (match_id, tournament_id, team1_id, team2_id, match_time, team1_score, team2_score, status) VALUES
    (1,1,1,2,'2026-11-20 09:00:00',3,1,'COMPLETED'),
    (2,1,3,4,'2026-11-20 12:00:00',2,2,'COMPLETED'),
    (3,1,1,3,'2026-11-20 15:00:00',1,0,'COMPLETED'),
    (4,1,2,5,'2026-11-21 09:00:00',4,2,'COMPLETED'),
    (5,1,4,5,'2026-11-21 12:00:00',0,1,'COMPLETED'),
    (6,1,1,4,'2026-11-21 15:00:00',2,3,'COMPLETED'),
    (7,1,2,3,'2026-11-22 09:00:00',1,1,'COMPLETED'),
    (8,1,3,5,'2026-11-22 12:00:00',3,0,'COMPLETED'),
    (9,1,1,5,'2026-11-22 15:00:00',2,2,'COMPLETED'),
    (10,1,2,4,'2026-11-23 09:00:00',1,4,'COMPLETED');

INSERT INTO PlayerMatchStat (match_id, player_id, points_scored, played) VALUES
    (1,1,2,1),(1,2,1,1),(1,3,0,1),(1,4,1,1),(1,5,0,1),
    (3,1,1,1),(3,7,0,1),
    (4,4,3,1),(4,5,1,1),(4,13,1,1),(4,14,1,1),
    (6,1,1,1),(6,10,2,1),(6,11,1,1),
    (8,7,2,1),(8,8,1,1),
    (10,4,1,1),(10,10,2,1),(10,11,2,1);
