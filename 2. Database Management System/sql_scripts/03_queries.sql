-- ============================================================
-- Sample Queries for Sports Tournament Management System
-- Demonstrates CRUD, joins, aggregates, subqueries, and view usage
-- ============================================================

-- 1. List all teams with their department and sport
SELECT t.team_id, t.team_name, d.dept_name, s.sport_name
FROM Team t
JOIN Department d ON t.dept_id = d.dept_id
JOIN Sport s ON t.sport_id = s.sport_id
ORDER BY t.team_id;

-- 2. List all players with team and department info
SELECT p.player_id, p.player_name, p.jersey_no, t.team_name, d.dept_name
FROM Player p
JOIN Team t ON p.team_id = t.team_id
JOIN Department d ON t.dept_id = d.dept_id
ORDER BY p.player_id;

-- 3. Show match results using view v_match_results
SELECT match_id, team1_name, team2_name, team1_score, team2_score, winner, match_time, status
FROM v_match_results
ORDER BY match_id;

-- 4. Tournament standings (view v_standings)
SELECT team_name, matches_played, wins, draws, losses, goals_for, goals_against,
       (goals_for - goals_against) AS goal_diff,
       (wins * 3 + draws) AS total_points
FROM v_standings
WHERE tournament_id = 1
ORDER BY total_points DESC, goal_diff DESC;

-- 5. Top goal scorers (players with points > 0)
SELECT p.player_name, t.team_name,
       SUM(ps.points_scored) AS total_goals,
       COUNT(ps.match_id) AS matches_played
FROM PlayerMatchStat ps
JOIN Player p ON ps.player_id = p.player_id
JOIN Team t ON p.team_id = t.team_id
WHERE ps.played = 1
GROUP BY p.player_id, p.player_name, t.team_name
HAVING total_goals > 0
ORDER BY total_goals DESC;

-- 6. Team with most wins (subquery)
SELECT team_name, wins, goals_for
FROM v_standings
WHERE tournament_id = 1
  AND wins = (SELECT MAX(wins) FROM v_standings WHERE tournament_id = 1);

-- 7. Match statistics summary (aggregate)
SELECT COUNT(*) AS total_matches,
       SUM(team1_score + team2_score) AS total_goals,
       ROUND(AVG(team1_score + team2_score), 2) AS avg_goals,
       MAX(team1_score + team2_score) AS max_goals,
       MIN(team1_score + team2_score) AS min_goals
FROM MatchSchedule
WHERE status = 'COMPLETED';

-- 8. Result distribution (home win / away win / draw)
SELECT CASE
    WHEN team1_score > team2_score THEN 'Home Win'
    WHEN team2_score > team1_score THEN 'Away Win'
    ELSE 'Draw'
  END AS result_type,
  COUNT(*) AS count
FROM MatchSchedule
WHERE status = 'COMPLETED'
GROUP BY result_type;

-- 9. Players without match stats (left join)
SELECT p.player_id, p.player_name, t.team_name
FROM Player p
JOIN Team t ON p.team_id = t.team_id
LEFT JOIN PlayerMatchStat ps ON p.player_id = ps.player_id
WHERE ps.player_id IS NULL;

-- 10. Department-wise total goals (nested joins)
SELECT d.dept_name, t.team_name,
       COALESCE(SUM(ps.points_scored), 0) AS total_goals
FROM Department d
JOIN Team t ON d.dept_id = t.dept_id
LEFT JOIN Player p ON t.team_id = p.team_id
LEFT JOIN PlayerMatchStat ps ON p.player_id = ps.player_id AND ps.played = 1
GROUP BY d.dept_name, t.team_name
ORDER BY total_goals DESC;
