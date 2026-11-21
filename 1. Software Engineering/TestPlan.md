# Test Plan for Sports Tournament Management System

## 1. Introduction
This test plan describes the testing strategy for the **Sports Tournament Management and Team Performance Analysis System**. The goal is to verify that all functional and non‑functional requirements are satisfied.

## 2. Test Levels
| Level | Scope | Types of Tests |
|-------|-------|----------------|
| Unit | Individual classes/methods in the Java code (e.g., `Team`, `Match`, `TournamentService`). | JUnit style unit tests (not provided here, but can be generated). |
| Integration | Interaction between Java services and the database. | CRUD operations, foreign‑key enforcement, transaction boundaries. |
| System | End‑to‑end execution of the compiled Java program, DBMS script, and Python analytics. | Full scenario from data loading to report generation. |
| Acceptance | Manual verification of reports and visualizations against the specification. | Review of generated text files and charts. |

## 3. Test Cases (selected)

### TC‑01: Add Department
- **Preconditions:** Database empty.
- **Steps:** Execute `INSERT INTO Department …`.
- **Expected Result:** Row count for `Department` = 1; foreign‑key constraints remain satisfied.

### TC‑02: Register Team for Tournament (valid)
- **Preconditions:** Department, Sport, Team, and Tournament exist.
- **Steps:** Call `service.registerTeamForTournament(teamId, tournamentId)`.
- **Expected Result:** Team appears in `TeamRegistration` and no exception is thrown.

### TC‑03: Register Team for Tournament (invalid sport)
- **Preconditions:** Team sport differs from tournament sport.
- **Steps:** Invoke registration.
- **Expected Result:** `InvalidRegistrationException` with code `INVALID_REGISTRATION`.

### TC‑04: Schedule Match (no conflict)
- **Preconditions:** Two teams registered for the same tournament.
- **Steps:** Call `service.scheduleMatch` with a unique datetime.
- **Expected Result:** New `Match` row created, returned match ID.

### TC‑05: Schedule Match (conflict)
- **Preconditions:** Same team already has a match at the given datetime.
- **Steps:** Attempt to schedule another match for that team at the same time.
- **Expected Result:** `ScheduleConflictException` with code `SCHEDULE_CONFLICT`.

### TC‑06: Record Match Result (once)
- **Preconditions:** Match status = `SCHEDULED`.
- **Steps:** Call `recordMatchResult` with scores.
- **Expected Result:** `Match` status becomes `COMPLETED`; winner computed correctly.

### TC‑07: Record Match Result (duplicate)
- **Preconditions:** Match already `COMPLETED`.
- **Steps:** Call `recordMatchResult` again.
- **Expected Result:** `TournamentException` with code `ALREADY_RECORDED`.

### TC‑08: Generate Standings
- **Preconditions:** Several completed matches with various outcomes.
- **Steps:** Call `generateStandingsReport`.
- **Expected Result:** Teams sorted by total points, then goal difference. Points reflect scoring rules.

### TC‑09: Python Export Verification
- **Preconditions:** DBMS script has exported CSV files to `4. Foundation of Data Science/data`.
- **Steps:** Run `analysis.py`.
- **Expected Result:** PNG charts saved, no runtime errors.

## 4. Test Data
- Sample data is provided in `sql_scripts/02_sample_data.sql`. It populates all tables with realistic departmental teams, players, a tournament, matches, and player statistics.

## 5. Execution Procedure
1. **Database Setup** – Run `run_dbms.py` (or execute `01_schema.sql` then `02_sample_data.sql`).
2. **Java Build** – Use the provided `run_java.bat` to compile and execute the program; output is written to `output.txt`.
3. **Python Analysis** – Run `run_analysis.bat` which calls `analysis.py`. Charts are stored in `visualizations/`.
4. Verify the generated text reports (`output.txt`) and PNG files against the expected values described in the SRS.

## 6. Acceptance Criteria
- All functional requirements (FR‑01 … FR‑08) produce the expected results without unhandled exceptions.
- Performance: each phase (compile, DB load, analysis) completes within 5 seconds on a typical Windows laptop.
- Reports are human‑readable and match the format shown in the SRS.
- Visualizations accurately reflect the exported CSV data.

---
*Prepared by the development team – 2026‑11‑17*