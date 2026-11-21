/*
 * TournamentManagement – CMD-style interactive menu
 * Uses TournamentService from SportsTournament.java
 */
import java.util.*;
import java.util.stream.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TournamentManagement {

    private static TournamentService service = new TournamentService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        service.loadDemoData();

        System.out.println("C:\\SportsArena> java TournamentManagement");
        System.out.println();
        System.out.println("========================================");
        System.out.println("    SPORTSARENA - TOURNAMENT MANAGEMENT");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Schedule Match");
            System.out.println("2. View Match Schedule");
            System.out.println("3. Record Match Results");
            System.out.println("4. Display Points Table");
            System.out.println("5. Generate Tournament Report");
            System.out.println("6. Exit");
            System.out.println();

            int choice = readInt("Enter your choice (1-6) (e.g. 2): ");

            switch (choice) {
                case 1: scheduleMatch(); break;
                case 2: viewMatchSchedule(); break;
                case 3: recordMatchResults(); break;
                case 4: displayPointsTable(); break;
                case 5: generateReport(); break;
                case 6:
                    System.out.println("Exiting Tournament Management...");
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please enter 1-6.");
            }
        }
    }

    private static void viewMatchSchedule() {
        System.out.println();
        System.out.println("------------- MATCH SCHEDULE ------------");
        System.out.printf("%-10s %-18s %-18s %-10s%n", "Match ID", "Team A", "Team B", "Status");
        System.out.println("----------------------------------------------------------");

        Collection<Match> allMatches = service.getAllMatches();
        for (Match m : allMatches) {
            Team tA = service.getTeamById(m.getTeam1Id());
            Team tB = service.getTeamById(m.getTeam2Id());
            String nameA = (tA != null) ? tA.getName() : "Team " + m.getTeam1Id();
            String nameB = (tB != null) ? tB.getName() : "Team " + m.getTeam2Id();
            System.out.printf("%-10s %-18s %-18s %-10s%n",
                    "M" + String.format("%03d", m.getMatchId()), nameA, nameB, m.getStatus());
        }

        System.out.println("----------------------------------------------------------");
        System.out.println("Total Matches Scheduled: " + allMatches.size());
        System.out.println("----------------------------------------------------------");
    }

    private static void displayPointsTable() {
        System.out.println();
        System.out.println("----------- TOURNAMENT STANDINGS --------");
        System.out.printf("%-6s %-18s %-8s %-6s %-6s %-6s %-8s%n",
                "Rank", "Team", "Played", "Won", "Draw", "Lost", "Points");
        System.out.println("----------------------------------------------------------");

        List<TeamStanding> standings = service.computeStandings();
        int rank = 1;
        for (TeamStanding ts : standings) {
            if (ts.getMatchesPlayed() == 0) continue;
            System.out.printf("%-6d %-18s %-8d %-6d %-6d %-6d %-8d%n",
                    rank++, ts.getTeamName(), ts.getMatchesPlayed(),
                    ts.getWins(), ts.getDraws(), ts.getLosses(), ts.getTotalPoints());
        }

        System.out.println("----------------------------------------------------------");
        if (!standings.isEmpty() && standings.get(0).getMatchesPlayed() > 0) {
            System.out.println("TOURNAMENT LEADER: " + standings.get(0).getTeamName().toUpperCase());
        }
        System.out.println("----------------------------------------------------------");
    }

    private static void scheduleMatch() {
        System.out.println();
        System.out.println("---------- SCHEDULE NEW MATCH -----------");
        int tournId = readInt("Enter Tournament ID (e.g. 1): ");
        int team1 = readInt("Enter Team A ID (1-5) (e.g. 1): ");
        int team2 = readInt("Enter Team B ID (1-5) (e.g. 2): ");

        // Same team check
        if (team1 == team2) {
            System.out.println("[ERROR] Team A and Team B cannot be the same team.");
            return;
        }

        // Duplicate match pair check (same two teams already scheduled)
        for (Match m : service.getAllMatches()) {
            if (m.getTournamentId() == tournId) {
                boolean sameMatch = (m.getTeam1Id() == team1 && m.getTeam2Id() == team2)
                        || (m.getTeam1Id() == team2 && m.getTeam2Id() == team1);
                if (sameMatch) {
                    Team tA = service.getTeamById(team1);
                    Team tB = service.getTeamById(team2);
                    String nameA = (tA != null) ? tA.getName() : "Team " + team1;
                    String nameB = (tB != null) ? tB.getName() : "Team " + team2;
                    System.out.println("[ALREADY SCHEDULED] Match between '" + nameA + "' and '" + nameB
                            + "' is already scheduled as Match M" + String.format("%03d", m.getMatchId()) + ".");
                    return;
                }
            }
        }

        System.out.print("Enter Match Date-Time (yyyy-MM-dd HH:mm) (e.g. 2026-11-25 15:30): ");
        String dtStr = scanner.nextLine().trim();
        try {
            // Accept both zero-padded (2026-11-05) and single-digit (2026-11-5) day/month
            DateTimeFormatter strictFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            DateTimeFormatter flexFmt  = new java.time.format.DateTimeFormatterBuilder()
                    .appendPattern("yyyy-M-d H:mm")
                    .toFormatter();
            DateTimeFormatter displayFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dt;
            try {
                dt = LocalDateTime.parse(dtStr, strictFmt);
            } catch (Exception ex) {
                dt = LocalDateTime.parse(dtStr, flexFmt);
            }
            Match m = service.scheduleMatch(tournId, team1, team2, dt);
            Team tA = service.getTeamById(team1);
            Team tB = service.getTeamById(team2);
            System.out.println("[SUCCESS] Match M" + String.format("%03d", m.getMatchId())
                    + " scheduled: " + tA.getName() + " vs " + tB.getName()
                    + " at " + dt.format(displayFmt));
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void recordMatchResults() {
        System.out.println();
        System.out.println("---------- RECORD MATCH RESULT ----------");
        int matchId = readInt("Enter Match ID (1-10) (e.g. 1): ");

        // Check if match exists
        Match existing = service.getMatches().get(matchId);
        if (existing == null) {
            System.out.println("[ERROR] Match ID " + matchId + " does not exist.");
            return;
        }

        // Duplicate result check
        if (existing.getStatus().equals("COMPLETED")) {
            System.out.println("[ALREADY RECORDED] Result for Match " + matchId + " is already recorded as "
                    + existing.getTeam1Score() + " - " + existing.getTeam2Score() + ". Cannot re-enter.");
            return;
        }

        int score1 = readInt("Enter Team A Score (e.g. 3): ");
        int score2 = readInt("Enter Team B Score (e.g. 1): ");
        try {
            service.recordMatchResult(matchId, score1, score2);
            System.out.println("[SUCCESS] Match " + matchId + " result recorded: " + score1 + " - " + score2);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void generateReport() {
        System.out.println();
        System.out.println("-------- TOURNAMENT REPORT --------------");
        int tournId = readInt("Enter Tournament ID (default 1) (e.g. 1): ");
        if (tournId <= 0) tournId = 1;

        // Match results
        System.out.println(service.generateMatchReport(tournId));

        // Standings
        System.out.println(service.generateStandingsReport(tournId));

        // Player participation
        System.out.println(service.generatePlayerReport());
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (Exception e) {
            return -1;
        }
    }
}
