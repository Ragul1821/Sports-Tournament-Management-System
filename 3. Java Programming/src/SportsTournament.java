/*
 * Sports Tournament Management and Team Performance Analysis System
 * Velalar College of Engineering and Technology, Erode
 * Department of Information Technology - 25IT3A&B
 * 
 * This program demonstrates Java concepts:
 * - Classes, Objects, Constructors (Unit 1)
 * - Inheritance, Abstract Classes, Interfaces (Unit 2)
 * - Exception Handling with custom exceptions (Unit 3)
 * - String Handling, StringBuilder (Unit 4)
 * - Collections, Generics, Streams, Lambdas (Unit 5)
 */

import java.util.*;
import java.util.stream.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ======================== CUSTOM EXCEPTION (Unit 3) ========================
class TournamentException extends Exception {
    private String errorCode;
    public TournamentException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public String getErrorCode() { return errorCode; }
}

class ScheduleConflictException extends TournamentException {
    public ScheduleConflictException(String message) {
        super(message, "SCHEDULE_CONFLICT");
    }
}

class InvalidRegistrationException extends TournamentException {
    public InvalidRegistrationException(String message) {
        super(message, "INVALID_REGISTRATION");
    }
}

// ======================== INTERFACE (Unit 2) ========================
interface Displayable {
    String toDisplayString();
}

interface Scoreable {
    int getPoints();
    String getResult();
}

// ======================== ABSTRACT CLASS (Unit 2) ========================
abstract class Entity implements Displayable {
    protected int id;
    protected String name;

    public Entity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

    // Abstract method
    public abstract String getType();
}

// ======================== DEPARTMENT (Unit 1) ========================
class Department extends Entity {
    public Department(int id, String name) {
        super(id, name);
    }

    @Override
    public String getType() { return "Department"; }

    @Override
    public String toDisplayString() {
        return String.format("| %-4d | %-30s |", id, name);
    }
}

// ======================== SPORT (Unit 1) ========================
class Sport extends Entity {
    public Sport(int id, String name) {
        super(id, name);
    }

    @Override
    public String getType() { return "Sport"; }

    @Override
    public String toDisplayString() {
        return String.format("| %-4d | %-20s |", id, name);
    }
}

// ======================== SCORING RULE ========================
class ScoringRule {
    private int sportId;
    private int winPoints;
    private int drawPoints;
    private int lossPoints;

    public ScoringRule(int sportId, int winPts, int drawPts, int lossPts) {
        this.sportId = sportId;
        this.winPoints = winPts;
        this.drawPoints = drawPts;
        this.lossPoints = lossPts;
    }

    public int getSportId() { return sportId; }
    public int getWinPoints() { return winPoints; }
    public int getDrawPoints() { return drawPoints; }
    public int getLossPoints() { return lossPoints; }

    public String toDisplayString() {
        return String.format("Win=%d, Draw=%d, Loss=%d", winPoints, drawPoints, lossPoints);
    }
}

// ======================== TEAM (Unit 1 & 2) ========================
class Team extends Entity {
    private int deptId;
    private int sportId;
    private List<Player> players;

    public Team(int id, String name, int deptId, int sportId) {
        super(id, name);
        this.deptId = deptId;
        this.sportId = sportId;
        this.players = new ArrayList<>();
    }

    public int getDeptId() { return deptId; }
    public int getSportId() { return sportId; }
    public List<Player> getPlayers() { return players; }

    public void addPlayer(Player p) {
        players.add(p);
    }

    @Override
    public String getType() { return "Team"; }

    @Override
    public String toDisplayString() {
        return String.format("| %-4d | %-25s | Dept:%-3d | Sport:%-3d | Players:%-2d |",
                id, name, deptId, sportId, players.size());
    }
}

// ======================== PLAYER (Unit 1 & 2) ========================
class Player extends Entity {
    private int teamId;
    private int jerseyNo;
    private int totalPointsScored;
    private int matchesPlayed;

    public Player(int id, String name, int teamId, int jerseyNo) {
        super(id, name);
        this.teamId = teamId;
        this.jerseyNo = jerseyNo;
        this.totalPointsScored = 0;
        this.matchesPlayed = 0;
    }

    public int getTeamId() { return teamId; }
    public int getJerseyNo() { return jerseyNo; }
    public int getTotalPointsScored() { return totalPointsScored; }
    public int getMatchesPlayed() { return matchesPlayed; }

    public void addMatchStat(int pointsScored) {
        this.totalPointsScored += pointsScored;
        this.matchesPlayed++;
    }

    @Override
    public String getType() { return "Player"; }

    @Override
    public String toDisplayString() {
        return String.format("| %-4d | %-20s | Team:%-3d | Jersey:%-3d | Pts:%-4d | Matches:%-2d |",
                id, name, teamId, jerseyNo, totalPointsScored, matchesPlayed);
    }
}

// ======================== TOURNAMENT ========================
class Tournament extends Entity {
    private int sportId;
    private LocalDate startDate;
    private List<Integer> registeredTeamIds;

    public Tournament(int id, String name, int sportId, LocalDate startDate) {
        super(id, name);
        this.sportId = sportId;
        this.startDate = startDate;
        this.registeredTeamIds = new ArrayList<>();
    }

    public int getSportId() { return sportId; }
    public LocalDate getStartDate() { return startDate; }
    public List<Integer> getRegisteredTeamIds() { return registeredTeamIds; }

    public void registerTeam(int teamId) {
        registeredTeamIds.add(teamId);
    }

    public boolean isTeamRegistered(int teamId) {
        return registeredTeamIds.contains(teamId);
    }

    @Override
    public String getType() { return "Tournament"; }

    @Override
    public String toDisplayString() {
        return String.format("| %-4d | %-30s | Sport:%-3d | Start:%-12s | Teams:%-2d |",
                id, name, sportId, startDate, registeredTeamIds.size());
    }
}

// ======================== MATCH (implements Scoreable) ========================
class Match implements Displayable, Scoreable {
    private int matchId;
    private int tournamentId;
    private int team1Id;
    private int team2Id;
    private LocalDateTime matchTime;
    private int team1Score;
    private int team2Score;
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public Match(int matchId, int tournamentId, int team1Id, int team2Id, LocalDateTime matchTime) {
        this.matchId = matchId;
        this.tournamentId = tournamentId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.matchTime = matchTime;
        this.team1Score = 0;
        this.team2Score = 0;
        this.status = "SCHEDULED";
    }

    public int getMatchId() { return matchId; }
    public int getTournamentId() { return tournamentId; }
    public int getTeam1Id() { return team1Id; }
    public int getTeam2Id() { return team2Id; }
    public LocalDateTime getMatchTime() { return matchTime; }
    public int getTeam1Score() { return team1Score; }
    public int getTeam2Score() { return team2Score; }
    public String getStatus() { return status; }

    public void recordResult(int score1, int score2) {
        this.team1Score = score1;
        this.team2Score = score2;
        this.status = "COMPLETED";
    }

    public int getWinnerId() {
        if (!status.equals("COMPLETED")) return -1;
        if (team1Score > team2Score) return team1Id;
        if (team2Score > team1Score) return team2Id;
        return 0; // draw
    }

    @Override
    public int getPoints() { return team1Score + team2Score; }

    @Override
    public String getResult() {
        if (status.equals("COMPLETED"))
            return team1Score + " - " + team2Score;
        return "Not played";
    }

    @Override
    public String toDisplayString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("| M%-3d | T%-3d vs T%-3d | %-16s | Score: %-7s | %-10s |",
                matchId, team1Id, team2Id, matchTime.format(fmt), getResult(), status);
    }
}

// ======================== PLAYER MATCH STAT ========================
class PlayerMatchStat {
    private int matchId;
    private int playerId;
    private int pointsScored;
    private boolean played;

    public PlayerMatchStat(int matchId, int playerId, int pointsScored, boolean played) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.pointsScored = pointsScored;
        this.played = played;
    }

    public int getMatchId() { return matchId; }
    public int getPlayerId() { return playerId; }
    public int getPointsScored() { return pointsScored; }
    public boolean isPlayed() { return played; }
}

// ======================== TEAM STANDING ========================
class TeamStanding implements Comparable<TeamStanding> {
    private int teamId;
    private String teamName;
    private int wins;
    private int draws;
    private int losses;
    private int totalPoints;
    private int goalsFor;
    private int goalsAgainst;

    public TeamStanding(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public void addWin(int pts, int gf, int ga) { wins++; totalPoints += pts; goalsFor += gf; goalsAgainst += ga; }
    public void addDraw(int pts, int gf, int ga) { draws++; totalPoints += pts; goalsFor += gf; goalsAgainst += ga; }
    public void addLoss(int pts, int gf, int ga) { losses++; totalPoints += pts; goalsFor += gf; goalsAgainst += ga; }

    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public int getTotalPoints() { return totalPoints; }
    public int getGoalDifference() { return goalsFor - goalsAgainst; }
    public int getMatchesPlayed() { return wins + draws + losses; }

    @Override
    public int compareTo(TeamStanding other) {
        if (this.totalPoints != other.totalPoints)
            return other.totalPoints - this.totalPoints;
        return other.getGoalDifference() - this.getGoalDifference();
    }

    public String toDisplayString() {
        return String.format("| %-25s | %2d | %2d | %2d | %2d | %3d | %3d | %+3d | %3d |",
                teamName, getMatchesPlayed(), wins, draws, losses,
                goalsFor, goalsAgainst, getGoalDifference(), totalPoints);
    }
}

// ======================== TOURNAMENT SERVICE (Unit 2, 3, 5) ========================
class TournamentService {
    private Map<Integer, Department> departments = new LinkedHashMap<>();
    private Map<Integer, Sport> sports = new LinkedHashMap<>();
    private Map<Integer, ScoringRule> scoringRules = new LinkedHashMap<>();
    private Map<Integer, Team> teams = new LinkedHashMap<>();
    private Map<Integer, Player> players = new LinkedHashMap<>();
    private Map<Integer, Tournament> tournaments = new LinkedHashMap<>();
    private Map<Integer, Match> matches = new LinkedHashMap<>();
    private List<PlayerMatchStat> playerStats = new ArrayList<>();

    // ---- Add Methods ----
    public void addDepartment(Department d) { departments.put(d.getId(), d); }
    public void addSport(Sport s) { sports.put(s.getId(), s); }
    public void addScoringRule(ScoringRule sr) { scoringRules.put(sr.getSportId(), sr); }
    public void addTeam(Team t) { teams.put(t.getId(), t); }
    public void addPlayer(Player p) {
        players.put(p.getId(), p);
        Team t = teams.get(p.getTeamId());
        if (t != null) t.addPlayer(p);
    }
    public void addTournament(Tournament t) { tournaments.put(t.getId(), t); }

    // ---- Registration ----
    public void registerTeamForTournament(int teamId, int tournamentId)
            throws InvalidRegistrationException {
        Tournament t = tournaments.get(tournamentId);
        Team team = teams.get(teamId);
        if (t == null) throw new InvalidRegistrationException("Tournament ID " + tournamentId + " not found.");
        if (team == null) throw new InvalidRegistrationException("Team ID " + teamId + " not found.");
        if (team.getSportId() != t.getSportId())
            throw new InvalidRegistrationException("Team sport does not match tournament sport.");
        if (t.isTeamRegistered(teamId))
            throw new InvalidRegistrationException("Team already registered for this tournament.");
        t.registerTeam(teamId);
    }

    // ---- Match Scheduling ----
    public Match scheduleMatch(int tournamentId, int team1Id, int team2Id, LocalDateTime matchTime)
            throws TournamentException {
        Tournament t = tournaments.get(tournamentId);
        if (t == null) throw new TournamentException("Tournament not found.", "NOT_FOUND");
        if (!t.isTeamRegistered(team1Id) || !t.isTeamRegistered(team2Id))
            throw new InvalidRegistrationException("Both teams must be registered for the tournament.");

        // Check schedule conflict (Unit 3: Exception Handling)
        for (Match m : matches.values()) {
            if (m.getTournamentId() == tournamentId && m.getMatchTime().equals(matchTime)) {
                if (m.getTeam1Id() == team1Id || m.getTeam2Id() == team1Id ||
                    m.getTeam1Id() == team2Id || m.getTeam2Id() == team2Id) {
                    throw new ScheduleConflictException(
                        "Team already has a match at " + matchTime);
                }
            }
        }

        int matchId = matches.size() + 1;
        Match match = new Match(matchId, tournamentId, team1Id, team2Id, matchTime);
        matches.put(matchId, match);
        return match;
    }

    // ---- Record Result ----
    public void recordMatchResult(int matchId, int score1, int score2)
            throws TournamentException {
        Match match = matches.get(matchId);
        if (match == null) throw new TournamentException("Match not found.", "NOT_FOUND");
        if (match.getStatus().equals("COMPLETED"))
            throw new TournamentException("Match result already recorded.", "ALREADY_RECORDED");
        match.recordResult(score1, score2);
    }

    // ---- Record Player Stats ----
    public void recordPlayerStat(int matchId, int playerId, int pointsScored, boolean played) {
        PlayerMatchStat stat = new PlayerMatchStat(matchId, playerId, pointsScored, played);
        playerStats.add(stat);
        Player p = players.get(playerId);
        if (p != null && played) p.addMatchStat(pointsScored);
    }

    // ---- Generate Standings (Unit 5: Streams, Lambdas, Collections) ----
    public List<TeamStanding> getStandings(int tournamentId) {
        Tournament t = tournaments.get(tournamentId);
        if (t == null) return Collections.emptyList();

        ScoringRule rule = scoringRules.get(t.getSportId());
        int winPts = (rule != null) ? rule.getWinPoints() : 3;
        int drawPts = (rule != null) ? rule.getDrawPoints() : 1;
        int lossPts = (rule != null) ? rule.getLossPoints() : 0;

        // Build standings map using streams
        Map<Integer, TeamStanding> standingsMap = t.getRegisteredTeamIds().stream()
                .collect(Collectors.toMap(
                    tid -> tid,
                    tid -> new TeamStanding(tid, teams.get(tid).getName()),
                    (a, b) -> a,
                    LinkedHashMap::new
                ));

        // Process completed matches
        matches.values().stream()
                .filter(m -> m.getTournamentId() == tournamentId && m.getStatus().equals("COMPLETED"))
                .forEach(m -> {
                    TeamStanding ts1 = standingsMap.get(m.getTeam1Id());
                    TeamStanding ts2 = standingsMap.get(m.getTeam2Id());
                    if (ts1 == null || ts2 == null) return;

                    int winner = m.getWinnerId();
                    if (winner == m.getTeam1Id()) {
                        ts1.addWin(winPts, m.getTeam1Score(), m.getTeam2Score());
                        ts2.addLoss(lossPts, m.getTeam2Score(), m.getTeam1Score());
                    } else if (winner == m.getTeam2Id()) {
                        ts2.addWin(winPts, m.getTeam2Score(), m.getTeam1Score());
                        ts1.addLoss(lossPts, m.getTeam1Score(), m.getTeam2Score());
                    } else {
                        ts1.addDraw(drawPts, m.getTeam1Score(), m.getTeam2Score());
                        ts2.addDraw(drawPts, m.getTeam2Score(), m.getTeam1Score());
                    }
                });

        // Sort by points then goal difference (Unit 5: Comparable)
        return standingsMap.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    // ---- Report: Match Results (Unit 4: String Handling) ----
    public String generateMatchReport(int tournamentId) {
        StringBuilder sb = new StringBuilder();
        Tournament t = tournaments.get(tournamentId);
        if (t == null) return "Tournament not found.";

        sb.append("\n========================================================\n");
        sb.append("       MATCH RESULTS REPORT\n");
        sb.append("       Tournament: ").append(t.getName()).append("\n");
        sb.append("========================================================\n");
        sb.append(String.format("| %-5s | %-15s | %-16s | %-13s | %-10s |\n",
                "Match", "Teams", "Date/Time", "Score", "Status"));
        sb.append("|-------|-----------------|------------------|---------------|------------|\n");

        matches.values().stream()
                .filter(m -> m.getTournamentId() == tournamentId)
                .forEach(m -> sb.append(m.toDisplayString()).append("\n"));

        return sb.toString();
    }

    // ---- Report: Standings ----
    public String generateStandingsReport(int tournamentId) {
        StringBuilder sb = new StringBuilder();
        Tournament t = tournaments.get(tournamentId);
        if (t == null) return "Tournament not found.";

        sb.append("\n===================================================================================\n");
        sb.append("       TOURNAMENT STANDINGS\n");
        sb.append("       ").append(t.getName()).append("\n");
        sb.append("===================================================================================\n");
        sb.append(String.format("| %-25s | %2s | %2s | %2s | %2s | %3s | %3s | %3s | %3s |\n",
                "Team", "MP", "W", "D", "L", "GF", "GA", "GD", "PTS"));
        sb.append("|---------------------------|----|----|----|----|-----|-----|-----|-----|\n");

        List<TeamStanding> standings = getStandings(tournamentId);
        standings.forEach(s -> sb.append(s.toDisplayString()).append("\n"));

        return sb.toString();
    }

    // ---- Report: Player Participation ----
    public String generatePlayerReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========================================================================\n");
        sb.append("       PLAYER PARTICIPATION REPORT\n");
        sb.append("========================================================================\n");
        sb.append(String.format("| %-4s | %-20s | %-6s | %-8s | %-6s | %-9s |\n",
                "ID", "Name", "Team", "Jersey", "Pts", "Matches"));
        sb.append("|------|----------------------|--------|----------|--------|-----------|");

        // Using streams to sort players by total points (Unit 5)
        players.values().stream()
                .sorted((p1, p2) -> p2.getTotalPointsScored() - p1.getTotalPointsScored())
                .forEach(p -> sb.append("\n").append(p.toDisplayString()));

        sb.append("\n");

        // Summary using streams (Unit 5: Lambdas, Stream API)
        long activePlayers = players.values().stream()
                .filter(p -> p.getMatchesPlayed() > 0)
                .count();
        int totalPoints = players.values().stream()
                .mapToInt(Player::getTotalPointsScored)
                .sum();
        OptionalDouble avgPoints = players.values().stream()
                .filter(p -> p.getMatchesPlayed() > 0)
                .mapToInt(Player::getTotalPointsScored)
                .average();

        sb.append("\n--- Summary ---\n");
        sb.append("Total Players: ").append(players.size()).append("\n");
        sb.append("Active Players (played >= 1 match): ").append(activePlayers).append("\n");
        sb.append("Total Points Scored: ").append(totalPoints).append("\n");
        sb.append(String.format("Average Points per Active Player: %.2f\n",
                avgPoints.orElse(0.0)));

        // Top scorer using streams
        players.values().stream()
                .filter(p -> p.getMatchesPlayed() > 0)
                .max(Comparator.comparingInt(Player::getTotalPointsScored))
                .ifPresent(p -> sb.append("Top Scorer: ").append(p.getName())
                        .append(" (").append(p.getTotalPointsScored()).append(" pts)\n"));

        return sb.toString();
    }

    // ---- Report: All Teams ----
    public String generateTeamReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=====================================================================\n");
        sb.append("       TEAM DETAILS REPORT\n");
        sb.append("=====================================================================\n");
        sb.append(String.format("| %-4s | %-25s | %-8s | %-9s | %-10s |\n",
                "ID", "Team Name", "Dept", "Sport", "Players"));
        sb.append("|------|---------------------------|----------|-----------|------------|");

        teams.values().forEach(t -> sb.append("\n").append(t.toDisplayString()));
        sb.append("\n");
        return sb.toString();
    }

    // Getters for collections
    public Map<Integer, Department> getDepartments() { return departments; }
    public Map<Integer, Sport> getSports() { return sports; }
    public Map<Integer, Team> getTeams() { return teams; }
    public Map<Integer, Player> getPlayers() { return players; }
    public Map<Integer, Tournament> getTournaments() { return tournaments; }
    public Map<Integer, Match> getMatches() { return matches; }
    public Map<Integer, ScoringRule> getScoringRules() { return scoringRules; }

    // ---- Convenience helpers for CMD‑style entry points ----
    public Collection<Team> getAllTeams() { return teams.values(); }
    public Collection<Player> getAllPlayers() { return players.values(); }
    public Collection<Match> getAllMatches() { return matches.values(); }
    public Department getDepartmentById(int id) { return departments.get(id); }
    public Sport getSportById(int id) { return sports.get(id); }
    public Team getTeamById(int id) { return teams.get(id); }
    public Player getPlayerById(int id) { return players.get(id); }
    public Tournament getTournamentById(int id) { return tournaments.get(id); }

    /** Populates default demo data so the CMD entry points have something to show. */
    public void loadDemoData() {
        // Departments
        addDepartment(new Department(1, "Information Technology"));
        addDepartment(new Department(2, "Computer Science"));
        addDepartment(new Department(3, "Electronics"));
        addDepartment(new Department(4, "Electrical"));
        addDepartment(new Department(5, "Mechanical"));

        // Sports & Scoring Rules
        addSport(new Sport(1, "Cricket"));
        addSport(new Sport(2, "Football"));
        addSport(new Sport(3, "Volleyball"));
        addSport(new Sport(4, "Basketball"));
        addScoringRule(new ScoringRule(1, 3, 1, 0));
        addScoringRule(new ScoringRule(2, 3, 1, 0));
        addScoringRule(new ScoringRule(3, 2, 0, 0));
        addScoringRule(new ScoringRule(4, 2, 0, 0));

        // Teams
        addTeam(new Team(1, "IT Warriors", 1, 2));
        addTeam(new Team(2, "CSE Titans", 2, 2));
        addTeam(new Team(3, "ECE Strikers", 3, 2));
        addTeam(new Team(4, "EEE Challengers", 4, 2));
        addTeam(new Team(5, "MECH Lions", 5, 2));

        // Players
        addPlayer(new Player(1, "Rahul", 1, 10));
        addPlayer(new Player(2, "Arjun", 2, 7));
        addPlayer(new Player(3, "Karthik", 3, 9));
        addPlayer(new Player(4, "Sanjay", 4, 11));
        addPlayer(new Player(5, "Naveen", 5, 8));
        addPlayer(new Player(6, "Arun", 1, 5));
        addPlayer(new Player(7, "Bharath", 2, 3));
        addPlayer(new Player(8, "Chandru", 3, 6));
        addPlayer(new Player(9, "Deepak", 4, 1));
        addPlayer(new Player(10, "Ezhil", 5, 4));
        addPlayer(new Player(11, "Ganesh", 1, 12));
        addPlayer(new Player(12, "Hari", 2, 14));
        addPlayer(new Player(13, "Inba", 3, 15));
        addPlayer(new Player(14, "Jagan", 4, 2));
        addPlayer(new Player(15, "Lokesh", 5, 13));

        // Tournament
        Tournament t = new Tournament(1, "Inter-Dept Football Championship 2026",
                2, LocalDate.of(2026, 11, 20));
        addTournament(t);

        // Register teams
        for (int tid = 1; tid <= 5; tid++) {
            try { registerTeamForTournament(tid, 1); } catch (Exception e) {}
        }

        // Schedule matches
        LocalDateTime base = LocalDateTime.of(2026, 11, 20, 9, 0);
        int[][] matchups = {{1,2},{3,4},{1,3},{2,5},{4,5},{1,4},{2,3},{3,5},{1,5},{2,4}};
        for (int i = 0; i < matchups.length; i++) {
            try { scheduleMatch(1, matchups[i][0], matchups[i][1], base.plusHours(i*3)); }
            catch (Exception e) {}
        }

        // Record results
        int[][] results = {{1,3,1},{2,2,2},{3,1,0},{4,4,2},{5,0,1},{6,2,3},{7,1,1},{8,3,0},{9,2,2},{10,1,4}};
        for (int[] r : results) {
            try { recordMatchResult(r[0], r[1], r[2]); } catch (Exception e) {}
        }

        // Record player stats
        recordPlayerStat(1, 1, 2, true);
        recordPlayerStat(1, 2, 1, true);
        recordPlayerStat(1, 3, 0, true);
        recordPlayerStat(1, 4, 1, true);
        recordPlayerStat(3, 1, 1, true);
        recordPlayerStat(3, 7, 0, true);
        recordPlayerStat(4, 4, 3, true);
        recordPlayerStat(4, 5, 1, true);
        recordPlayerStat(6, 1, 1, true);
        recordPlayerStat(6, 10, 2, true);
        recordPlayerStat(8, 7, 2, true);
        recordPlayerStat(8, 8, 1, true);
        recordPlayerStat(10, 4, 1, true);
        recordPlayerStat(10, 10, 2, true);
    }

    /** Compute tournament standings across all completed matches. */
    public List<TeamStanding> computeStandings() {
        Map<Integer, TeamStanding> map = new LinkedHashMap<>();
        for (Team t : teams.values()) {
            map.put(t.getId(), new TeamStanding(t.getId(), t.getName()));
        }
        for (Match m : matches.values()) {
            if (!"COMPLETED".equals(m.getStatus())) continue;
            Tournament tour = tournaments.get(m.getTournamentId());
            if (tour == null) continue;
            ScoringRule rule = scoringRules.get(tour.getSportId());
            int winPts  = (rule != null) ? rule.getWinPoints()  : 3;
            int drawPts = (rule != null) ? rule.getDrawPoints() : 1;
            int lossPts = (rule != null) ? rule.getLossPoints() : 0;
            int t1 = m.getTeam1Id(), t2 = m.getTeam2Id();
            int s1 = m.getTeam1Score(), s2 = m.getTeam2Score();
            if (s1 > s2) {
                map.get(t1).addWin(winPts, s1, s2);
                map.get(t2).addLoss(lossPts, s2, s1);
            } else if (s2 > s1) {
                map.get(t2).addWin(winPts, s2, s1);
                map.get(t1).addLoss(lossPts, s1, s2);
            } else {
                map.get(t1).addDraw(drawPts, s1, s2);
                map.get(t2).addDraw(drawPts, s2, s1);
            }
        }
        List<TeamStanding> list = new ArrayList<>(map.values());
        Collections.sort(list);
        return list;
    }
}

// ======================== MAIN CLASS ========================
public class SportsTournament {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  SPORTS TOURNAMENT MANAGEMENT & PERFORMANCE ANALYSIS SYSTEM");
        System.out.println("  Velalar College of Engineering and Technology, Erode");
        System.out.println("============================================================\n");

        TournamentService service = new TournamentService();

        // ========== 1. SETUP: Add Departments ==========
        System.out.println(">>> [1] SETTING UP DEPARTMENTS...");
        service.addDepartment(new Department(1, "Computer Science & Engineering"));
        service.addDepartment(new Department(2, "Information Technology"));
        service.addDepartment(new Department(3, "Electronics & Communication"));
        service.addDepartment(new Department(4, "Mechanical Engineering"));
        service.addDepartment(new Department(5, "Civil Engineering"));
        System.out.println("    5 departments added successfully.\n");

        // ========== 2. SETUP: Add Sports & Scoring Rules ==========
        System.out.println(">>> [2] SETTING UP SPORTS AND SCORING RULES...");
        service.addSport(new Sport(1, "Cricket"));
        service.addSport(new Sport(2, "Football"));
        service.addSport(new Sport(3, "Basketball"));

        service.addScoringRule(new ScoringRule(1, 3, 1, 0)); // Cricket
        service.addScoringRule(new ScoringRule(2, 3, 1, 0)); // Football
        service.addScoringRule(new ScoringRule(3, 2, 0, 0)); // Basketball (no draws)
        System.out.println("    3 sports with scoring rules added.\n");

        // ========== 3. SETUP: Create Teams ==========
        System.out.println(">>> [3] CREATING TEAMS...");
        service.addTeam(new Team(1, "CSE Warriors", 1, 2));
        service.addTeam(new Team(2, "IT Strikers", 2, 2));
        service.addTeam(new Team(3, "ECE Thunders", 3, 2));
        service.addTeam(new Team(4, "MECH Titans", 4, 2));
        service.addTeam(new Team(5, "CIVIL Spartans", 5, 2));
        System.out.println("    5 football teams created.\n");

        // ========== 4. SETUP: Register Players ==========
        System.out.println(">>> [4] REGISTERING PLAYERS...");
        String[][] playerData = {
            {"1", "Arun Kumar", "1", "10"}, {"2", "Bharath S", "1", "7"},
            {"3", "Chandru R", "1", "9"}, {"4", "Deepak M", "2", "11"},
            {"5", "Ezhil V", "2", "8"}, {"6", "Ganesh P", "2", "5"},
            {"7", "Hari K", "3", "12"}, {"8", "Inba S", "3", "3"},
            {"9", "Jagan R", "3", "6"}, {"10", "Karthik N", "4", "1"},
            {"11", "Lokesh T", "4", "4"}, {"12", "Mani V", "4", "2"},
            {"13", "Naveen S", "5", "14"}, {"14", "Oviya R", "5", "15"},
            {"15", "Priya K", "5", "13"}
        };
        for (String[] pd : playerData) {
            service.addPlayer(new Player(
                Integer.parseInt(pd[0]), pd[1],
                Integer.parseInt(pd[2]), Integer.parseInt(pd[3])
            ));
        }
        System.out.println("    15 players registered across 5 teams.\n");

        // ========== 5. CREATE TOURNAMENT ==========
        System.out.println(">>> [5] CREATING TOURNAMENT...");
        Tournament footTourney = new Tournament(1, "Inter-Dept Football Championship 2026",
                2, LocalDate.of(2026, 11, 20));
        service.addTournament(footTourney);
        System.out.println("    Tournament: " + footTourney.getName());
        System.out.println("    Start Date: " + footTourney.getStartDate() + "\n");

        // ========== 6. REGISTER TEAMS (with Exception Handling) ==========
        System.out.println(">>> [6] REGISTERING TEAMS FOR TOURNAMENT...");
        int[] teamIds = {1, 2, 3, 4, 5};
        for (int tid : teamIds) {
            try {
                service.registerTeamForTournament(tid, 1);
                System.out.println("    Team " + tid + " registered successfully.");
            } catch (InvalidRegistrationException e) {
                System.out.println("    ERROR [" + e.getErrorCode() + "]: " + e.getMessage());
            }
        }

        // Test duplicate registration exception
        try {
            service.registerTeamForTournament(1, 1);
        } catch (InvalidRegistrationException e) {
            System.out.println("    CAUGHT EXCEPTION: " + e.getMessage());
        }
        System.out.println();

        // ========== 7. SCHEDULE MATCHES (with Conflict Detection) ==========
        System.out.println(">>> [7] SCHEDULING MATCHES...");
        LocalDateTime baseTime = LocalDateTime.of(2026, 11, 20, 9, 0);
        int[][] matchups = {
            {1, 2}, {3, 4}, {1, 3}, {2, 5}, {4, 5},
            {1, 4}, {2, 3}, {3, 5}, {1, 5}, {2, 4}
        };

        for (int i = 0; i < matchups.length; i++) {
            try {
                LocalDateTime mt = baseTime.plusHours(i * 3);
                Match m = service.scheduleMatch(1, matchups[i][0], matchups[i][1], mt);
                System.out.println("    Match " + m.getMatchId() + " scheduled: Team "
                        + matchups[i][0] + " vs Team " + matchups[i][1]
                        + " at " + mt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            } catch (TournamentException e) {
                System.out.println("    SCHEDULE ERROR: " + e.getMessage());
            }
        }

        // Test schedule conflict
        try {
            service.scheduleMatch(1, 1, 2, baseTime);
        } catch (TournamentException e) {
            System.out.println("    CAUGHT CONFLICT: " + e.getMessage());
        }
        System.out.println();

        // ========== 8. RECORD MATCH RESULTS ==========
        System.out.println(">>> [8] RECORDING MATCH RESULTS...");
        int[][] results = {
            {1, 3, 1}, {2, 2, 2}, {3, 1, 0}, {4, 4, 2}, {5, 0, 1},
            {6, 2, 3}, {7, 1, 1}, {8, 3, 0}, {9, 2, 2}, {10, 1, 4}
        };

        for (int[] r : results) {
            try {
                service.recordMatchResult(r[0], r[1], r[2]);
                System.out.println("    Match " + r[0] + " result recorded: " + r[1] + " - " + r[2]);
            } catch (TournamentException e) {
                System.out.println("    ERROR: " + e.getMessage());
            }
        }

        // Test duplicate result recording
        try {
            service.recordMatchResult(1, 5, 5);
        } catch (TournamentException e) {
            System.out.println("    CAUGHT: " + e.getMessage());
        }
        System.out.println();

        // ========== 9. RECORD PLAYER STATS ==========
        System.out.println(">>> [9] RECORDING PLAYER STATISTICS...");
        // Match 1: Team 1 (3) vs Team 2 (1)
        service.recordPlayerStat(1, 1, 2, true);  // Arun scored 2
        service.recordPlayerStat(1, 2, 1, true);  // Bharath scored 1
        service.recordPlayerStat(1, 3, 0, true);  // Chandru played
        service.recordPlayerStat(1, 4, 1, true);  // Deepak scored 1
        service.recordPlayerStat(1, 5, 0, true);  // Ezhil played
        // Match 3: Team 1 (1) vs Team 3 (0)
        service.recordPlayerStat(3, 1, 1, true);
        service.recordPlayerStat(3, 7, 0, true);
        // Match 4: Team 2 (4) vs Team 5 (2)
        service.recordPlayerStat(4, 4, 3, true);
        service.recordPlayerStat(4, 5, 1, true);
        service.recordPlayerStat(4, 13, 1, true);
        service.recordPlayerStat(4, 14, 1, true);
        // Match 6: Team 1 (2) vs Team 4 (3)
        service.recordPlayerStat(6, 1, 1, true);
        service.recordPlayerStat(6, 10, 2, true);
        service.recordPlayerStat(6, 11, 1, true);
        // Match 8: Team 3 (3) vs Team 5 (0)
        service.recordPlayerStat(8, 7, 2, true);
        service.recordPlayerStat(8, 8, 1, true);
        // Match 10: Team 2 (1) vs Team 4 (4)
        service.recordPlayerStat(10, 4, 1, true);
        service.recordPlayerStat(10, 10, 2, true);
        service.recordPlayerStat(10, 11, 2, true);
        System.out.println("    Player statistics recorded for key matches.\n");

        // ========== 10. GENERATE REPORTS ==========
        System.out.println(">>> [10] GENERATING REPORTS...\n");

        // Team Details
        System.out.println(service.generateTeamReport());

        // Match Results
        System.out.println(service.generateMatchReport(1));

        // Tournament Standings
        System.out.println(service.generateStandingsReport(1));

        // Player Participation
        System.out.println(service.generatePlayerReport());

        // ========== 11. STREAM ANALYTICS (Unit 5) ==========
        System.out.println("\n>>> [11] STREAM-BASED ANALYTICS...\n");

        // Highest scoring match
        service.getMatches().values().stream()
                .filter(m -> m.getStatus().equals("COMPLETED"))
                .max(Comparator.comparingInt(Match::getPoints))
                .ifPresent(m -> System.out.println("    Highest Scoring Match: Match "
                        + m.getMatchId() + " (Total: " + m.getPoints() + " goals)"));

        // Count matches by result type
        long homeWins = service.getMatches().values().stream()
                .filter(m -> m.getStatus().equals("COMPLETED"))
                .filter(m -> m.getTeam1Score() > m.getTeam2Score())
                .count();
        long awayWins = service.getMatches().values().stream()
                .filter(m -> m.getStatus().equals("COMPLETED"))
                .filter(m -> m.getTeam2Score() > m.getTeam1Score())
                .count();
        long draws = service.getMatches().values().stream()
                .filter(m -> m.getStatus().equals("COMPLETED"))
                .filter(m -> m.getTeam1Score() == m.getTeam2Score())
                .count();
        System.out.println("    Home Wins: " + homeWins + " | Away Wins: " + awayWins + " | Draws: " + draws);

        // Average goals per match using streams
        double avgGoals = service.getMatches().values().stream()
                .filter(m -> m.getStatus().equals("COMPLETED"))
                .mapToInt(m -> m.getTeam1Score() + m.getTeam2Score())
                .average().orElse(0.0);
        System.out.printf("    Average Goals per Match: %.2f%n", avgGoals);

        // Teams grouped by department using streams and collectors
        System.out.println("\n    Teams grouped by Department:");
        service.getTeams().values().stream()
                .collect(Collectors.groupingBy(Team::getDeptId,
                        Collectors.mapping(Team::getName, Collectors.toList())))
                .forEach((deptId, teamNames) ->
                        System.out.println("      Dept " + deptId + ": " + String.join(", ", teamNames)));

        System.out.println("\n============================================================");
        System.out.println("  PROGRAM EXECUTION COMPLETED SUCCESSFULLY");
        System.out.println("============================================================");
    }
}
