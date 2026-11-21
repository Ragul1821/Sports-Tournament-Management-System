/*
 * PlayerManagement – CMD-style interactive menu
 * Uses TournamentService from SportsTournament.java
 */
import java.util.*;
import java.util.stream.*;

public class PlayerManagement {

    private static TournamentService service = new TournamentService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        service.loadDemoData();

        System.out.println("C:\\SportsArena> java PlayerManagement");
        System.out.println();
        System.out.println("========================================");
        System.out.println("       SPORTSARENA - PLAYER MANAGEMENT");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Register Player");
            System.out.println("2. View All Players");
            System.out.println("3. Search Player");
            System.out.println("4. View Participation");
            System.out.println("5. Exit");
            System.out.println();

            int choice = readInt("Enter your choice (1-5) (e.g. 2): ");

            switch (choice) {
                case 1: registerPlayer(); break;
                case 2: viewAllPlayers(); break;
                case 3: searchPlayer(); break;
                case 4: viewParticipation(); break;
                case 5:
                    System.out.println("Exiting Player Management...");
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please enter 1-5.");
            }
        }
    }

    private static void viewAllPlayers() {
        System.out.println();
        System.out.println("------------- PLAYER DETAILS ------------");
        System.out.printf("%-10s %-15s %-20s %-25s %-12s%n", "Player ID", "Player Name", "Team Name", "Department", "Sport");
        System.out.println("--------------------------------------------------------------------------");

        Collection<Player> allPlayers = service.getAllPlayers();
        for (Player p : allPlayers) {
            Team t = service.getTeamById(p.getTeamId());
            String teamName = (t != null) ? t.getName() : "Unknown";
            String sportName = "Unknown";
            String deptName = "Unknown";
            if (t != null) {
                Sport s = service.getSportById(t.getSportId());
                if (s != null) sportName = s.getName();
                Department d = service.getDepartmentById(t.getDeptId());
                if (d != null) deptName = d.getName();
            }
            System.out.printf("%-10s %-15s %-20s %-25s %-12s%n",
                    "P" + p.getId(), p.getName(), teamName, deptName, sportName);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Registered Players: " + allPlayers.size());
        System.out.println("--------------------------------------------------------------------------");
    }

    private static void registerPlayer() {
        System.out.println();
        System.out.println("---------- REGISTER NEW PLAYER ----------");
        int id = readInt("Enter Player ID (e.g. 16): ");

        // Duplicate ID check
        if (service.getPlayerById(id) != null) {
            Player existing = service.getPlayerById(id);
            System.out.println("[ALREADY REGISTERED] Player ID " + id + " is already registered as '"
                    + existing.getName() + "'. Please use a different ID.");
            return;
        }

        System.out.print("Enter Player Name (e.g. Suresh Raina): ");
        String name = scanner.nextLine().trim();

        // Duplicate name check across all players
        for (Player p : service.getAllPlayers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                Team existingTeam = service.getTeamById(p.getTeamId());
                String tName = (existingTeam != null) ? existingTeam.getName() : "Unknown";
                System.out.println("[ALREADY REGISTERED] Player '" + name + "' is already registered in team '"
                        + tName + "' (Player ID: " + p.getId() + ").");
                return;
            }
        }

        // Show available departments
        System.out.println();
        System.out.println("  Available Departments:");
        for (Department d : service.getDepartments().values()) {
            Team matchedTeam = null;
            for (Team t : service.getAllTeams()) {
                if (t.getDeptId() == d.getId()) { matchedTeam = t; break; }
            }
            String teamInfo = (matchedTeam != null) ? " --> Team: " + matchedTeam.getName() : "";
            System.out.println("    " + d.getId() + ". " + d.getName() + teamInfo);
        }
        System.out.println();
        System.out.print("Enter Department Name (e.g. Information Technology): ");
        String deptName = scanner.nextLine().trim();

        // Find department by name (case-insensitive)
        Department matchedDept = null;
        for (Department d : service.getDepartments().values()) {
            if (d.getName().equalsIgnoreCase(deptName)) { matchedDept = d; break; }
        }
        if (matchedDept == null) {
            System.out.println("[ERROR] Department '" + deptName + "' not found. Registration failed.");
            return;
        }

        // Find the team belonging to that department
        Team matchedTeam = null;
        for (Team t : service.getAllTeams()) {
            if (t.getDeptId() == matchedDept.getId()) { matchedTeam = t; break; }
        }
        if (matchedTeam == null) {
            System.out.println("[ERROR] No team found for department '" + deptName + "'. Registration failed.");
            return;
        }

        int jerseyNo = readInt("Enter Jersey Number (e.g. 18): ");

        service.addPlayer(new Player(id, name, matchedTeam.getId(), jerseyNo));
        System.out.println("[SUCCESS] Player '" + name + "' registered to team '" + matchedTeam.getName()
                + "' (Department: " + matchedDept.getName() + ").");
    }

    private static void searchPlayer() {
        System.out.println();
        System.out.println("------------- SEARCH PLAYER -------------");
        int id = readInt("Enter Player ID to search (e.g. 1): ");
        Player p = service.getPlayerById(id);
        if (p == null) {
            System.out.println("[ERROR] Player with ID " + id + " not found.");
            return;
        }
        Team t = service.getTeamById(p.getTeamId());
        String teamName = (t != null) ? t.getName() : "Unknown";
        String sportName = "Unknown";
        if (t != null) {
            Sport s = service.getSportById(t.getSportId());
            if (s != null) sportName = s.getName();
        }
        System.out.println();
        System.out.printf("%-10s %-15s %-10s %-12s %-10s%n",
                "Player ID", "Player Name", "Team", "Sport", "Matches");
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-10s %-15s %-10s %-12s %-10d%n",
                "P" + p.getId(), p.getName(), teamName, sportName, p.getMatchesPlayed());
        System.out.println("-----------------------------------------------------------");
    }

    private static void viewParticipation() {
        System.out.println();
        System.out.println("-------- PLAYER PARTICIPATION -----------");
        System.out.printf("%-10s %-15s %-10s %-8s %-8s%n",
                "Player ID", "Player Name", "Team", "Matches", "Points");
        System.out.println("----------------------------------------------------------");

        List<Player> sorted = new ArrayList<>(service.getAllPlayers());
        sorted.sort((a, b) -> b.getTotalPointsScored() - a.getTotalPointsScored());

        for (Player p : sorted) {
            if (p.getMatchesPlayed() == 0) continue;
            Team t = service.getTeamById(p.getTeamId());
            String teamName = (t != null) ? t.getName() : "Unknown";
            System.out.printf("%-10s %-15s %-10s %-8d %-8d%n",
                    "P" + p.getId(), p.getName(), teamName,
                    p.getMatchesPlayed(), p.getTotalPointsScored());
        }

        long activePlayers = service.getAllPlayers().stream()
                .filter(p -> p.getMatchesPlayed() > 0).count();

        System.out.println("----------------------------------------------------------");
        System.out.println("Active Players (played >= 1 match): " + activePlayers);
        System.out.println("----------------------------------------------------------");
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
