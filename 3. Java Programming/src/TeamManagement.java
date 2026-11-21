/*
 * TeamManagement – CMD-style interactive menu
 * Uses TournamentService from SportsTournament.java
 */
import java.util.*;
import java.util.stream.*;

public class TeamManagement {

    private static TournamentService service = new TournamentService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        service.loadDemoData();

        System.out.println("C:\\SportsArena> java TeamManagement");
        System.out.println();
        System.out.println("========================================");
        System.out.println("       SPORTSARENA - TEAM MANAGEMENT");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Add Team");
            System.out.println("2. View All Teams");
            System.out.println("3. Search Team");
            System.out.println("4. Update Team");
            System.out.println("5. Delete Team");
            System.out.println("6. Exit");
            System.out.println();

            int choice = readInt("Enter your choice (1-6) (e.g. 2): ");

            switch (choice) {
                case 1: addTeam(); break;
                case 2: viewAllTeams(); break;
                case 3: searchTeam(); break;
                case 4: updateTeam(); break;
                case 5: deleteTeam(); break;
                case 6:
                    System.out.println("Exiting Team Management...");
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please enter 1-6.");
            }
        }
    }

    private static void viewAllTeams() {
        System.out.println();
        System.out.println("------------ TEAM DETAILS --------------");
        System.out.printf("%-10s %-18s %-25s%n", "Team ID", "Team Name", "Department");
        System.out.println("----------------------------------------");

        Collection<Team> allTeams = service.getAllTeams();
        for (Team t : allTeams) {
            Department d = service.getDepartmentById(t.getDeptId());
            String deptName = (d != null) ? d.getName() : "Unknown";
            System.out.printf("%-10s %-18s %-25s%n", "T" + t.getId(), t.getName(), deptName);
        }

        System.out.println("----------------------------------------");
        System.out.println("Total Teams Registered: " + allTeams.size());
        System.out.println("----------------------------------------");
    }

    private static void addTeam() {
        System.out.println();
        System.out.println("------------ ADD NEW TEAM ---------------");
        int id = readInt("Enter Team ID (e.g. 6): ");

        // Duplicate ID check
        if (service.getTeamById(id) != null) {
            Team existing = service.getTeamById(id);
            System.out.println("[ALREADY REGISTERED] Team ID " + id + " is already registered as '"
                    + existing.getName() + "'. Please use a different ID.");
            return;
        }

        System.out.print("Enter Team Name (e.g. AIDS Hawks): ");
        String name = scanner.nextLine().trim();

        // Duplicate name check
        for (Team t : service.getAllTeams()) {
            if (t.getName().equalsIgnoreCase(name)) {
                System.out.println("[ALREADY REGISTERED] Team '" + name + "' is already registered with Team ID "
                        + t.getId() + ". Please use a different name.");
                return;
            }
        }

        // Show available departments
        System.out.println();
        System.out.println("  Available Departments:");
        for (Department d : service.getDepartments().values()) {
            System.out.println("    " + d.getId() + ". " + d.getName());
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
            System.out.println("[ERROR] Department '" + deptName + "' not found. Please try again.");
            return;
        }

        int sportId = readInt("Enter Sport ID (1-Cricket, 2-Football, 3-Basketball) (e.g. 2): ");

        service.addTeam(new Team(id, name, matchedDept.getId(), sportId));
        System.out.println("[SUCCESS] Team '" + name + "' added successfully under department '"
                + matchedDept.getName() + "'.");
    }

    private static void searchTeam() {
        System.out.println();
        System.out.println("------------ SEARCH TEAM ----------------");
        int id = readInt("Enter Team ID to search (e.g. 1): ");
        Team t = service.getTeamById(id);
        if (t == null) {
            System.out.println("[ERROR] Team with ID " + id + " not found.");
            return;
        }
        Department d = service.getDepartmentById(t.getDeptId());
        String deptName = (d != null) ? d.getName() : "Unknown";
        System.out.println();
        System.out.printf("%-10s %-18s %-25s%n", "Team ID", "Team Name", "Department");
        System.out.println("----------------------------------------");
        System.out.printf("%-10s %-18s %-25s%n", "T" + t.getId(), t.getName(), deptName);
        System.out.println("----------------------------------------");
        System.out.println("Players in team: " + t.getPlayers().size());
    }

    private static void updateTeam() {
        System.out.println();
        System.out.println("------------ UPDATE TEAM ----------------");
        int id = readInt("Enter Team ID to update (e.g. 1): ");
        Team t = service.getTeamById(id);
        if (t == null) {
            System.out.println("[ERROR] Team with ID " + id + " not found.");
            return;
        }
        System.out.println("Current Name: " + t.getName());
        System.out.print("Enter new Team Name (e.g. IT Warriors Pro) (or press ENTER to keep): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            // Remove old and add with new name
            service.addTeam(new Team(id, newName, t.getDeptId(), t.getSportId()));
            System.out.println("[SUCCESS] Team updated to '" + newName + "'.");
        } else {
            System.out.println("[INFO] No changes made.");
        }
    }

    private static void deleteTeam() {
        System.out.println();
        System.out.println("------------ DELETE TEAM ----------------");
        int id = readInt("Enter Team ID to delete (e.g. 5): ");
        Team t = service.getTeamById(id);
        if (t == null) {
            System.out.println("[ERROR] Team with ID " + id + " not found.");
            return;
        }
        // Remove from the internal map
        service.getTeams().remove(id);
        System.out.println("[SUCCESS] Team '" + t.getName() + "' deleted.");
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
