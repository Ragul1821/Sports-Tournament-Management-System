# Software Requirements Specification (SRS)
## Sports Tournament Management and Team Performance Analysis System

**Velalar College of Engineering and Technology, Erode-12**  
**Department of Information Technology**  
**Academic Year: 2026-27 (ODD) | Class: 25IT3A&B**

---

## 1. Introduction

### 1.1 Purpose
This document specifies the software requirements for the Sports Tournament Management and Team Performance Analysis System. The system manages interdepartmental sports tournaments including team registrations, match scheduling, score recording, and performance analysis.

### 1.2 Scope
The system will:
- Manage team and player details
- Handle tournament registrations and match scheduling
- Record match results and player statistics
- Generate tournament standings and reports
- Provide data visualizations for performance analysis

### 1.3 Definitions and Acronyms
| Term | Definition |
|------|-----------|
| SRS | Software Requirements Specification |
| DBMS | Database Management System |
| ER | Entity-Relationship |
| DFD | Data Flow Diagram |
| CRUD | Create, Read, Update, Delete |
| GUI | Graphical User Interface |

### 1.4 Technologies Used
| Component | Technology |
|-----------|-----------|
| Application Logic | Java (Console Application) |
| Database | MySQL 8 / SQLite |
| Data Analysis | Python (Matplotlib, NumPy) |
| Version Control | Git / GitHub |

---

## 2. Overall Description

### 2.1 Product Perspective
The system is a standalone desktop application with three integrated components:
1. **Java Application** - Core business logic and user interface
2. **Database (SQL)** - Persistent data storage
3. **Python Analytics** - Data visualization and analysis

### 2.2 Product Functions
1. Team management (CRUD operations)
2. Player registration and management
3. Tournament creation and team registration
4. Match scheduling with conflict detection
5. Score recording and result tracking
6. Standings calculation using scoring rules
7. Report generation (match results, standings, player stats)
8. Data visualization and performance analysis

### 2.3 User Classes
| User | Role |
|------|------|
| Sports Administrator | Manages teams, schedules matches, records results |
| Analyst | Views reports, generates charts and visualizations |

### 2.4 Operating Environment
- **OS:** Windows 10/11
- **Java:** JDK 17+
- **Python:** 3.8+
- **Database:** MySQL 8.0 / SQLite 3

### 2.5 Design Constraints
- Console-based user interface (no web/GUI)
- Single-user system (no concurrent access)
- Local database storage

---

## 3. Functional Requirements

### FR-01: Manage Team Details
- **Description:** Maintain team information including team ID, name, department, and sport
- **Inputs:** Team ID, team name, department ID, sport ID
- **Outputs:** Confirmation of team creation/update
- **Priority:** High

### FR-02: Manage Player Details
- **Description:** Maintain player information including player ID, name, team assignment, and jersey number
- **Inputs:** Player ID, name, team ID, jersey number
- **Outputs:** Confirmation of player registration
- **Priority:** High

### FR-03: Register Teams for Tournaments
- **Description:** Register teams for specific tournaments with validation
- **Inputs:** Team ID, tournament ID
- **Outputs:** Registration confirmation or error (sport mismatch, duplicate)
- **Priority:** High

### FR-04: Schedule Matches
- **Description:** Schedule matches between registered teams with conflict detection
- **Inputs:** Tournament ID, team1 ID, team2 ID, match date/time
- **Outputs:** Match ID or schedule conflict error
- **Priority:** High

### FR-05: Record Match Results
- **Description:** Record scores, determine winners, update match status
- **Inputs:** Match ID, team1 score, team2 score
- **Outputs:** Updated match record with winner derived
- **Priority:** High

### FR-06: Calculate Tournament Standings
- **Description:** Compute team points based on scoring rules (win/draw/loss points)
- **Inputs:** Tournament ID
- **Outputs:** Sorted standings table with MP, W, D, L, GF, GA, GD, PTS
- **Priority:** High

### FR-07: Generate Reports
- **Description:** Generate formatted reports for match results, standings, and player participation
- **Inputs:** Report type, tournament ID
- **Outputs:** Formatted text reports
- **Priority:** Medium

### FR-08: Data Visualization
- **Description:** Retrieve data, preprocess it, and generate charts using Python
- **Inputs:** CSV data exported from database
- **Outputs:** PNG chart files (bar charts, pie charts, line charts)
- **Priority:** Medium

---

## 4. Non-Functional Requirements

### NFR-01: Performance
- System shall process and display results within 2 seconds

### NFR-02: Reliability
- Data integrity maintained through foreign key constraints
- Input validation on all user entries

### NFR-03: Usability
- Menu-driven console interface
- Clear error messages for invalid inputs

### NFR-04: Maintainability
- Modular code structure with separation of concerns
- Well-documented source code

---

## 5. System Design

### 5.1 Architecture
The system follows a **3-tier layered architecture**:
- **Presentation Layer:** Console menu (Main.java)
- **Business Logic Layer:** TournamentService with validation and scheduling
- **Data Layer:** MySQL/SQLite database with 9 tables and 2 views

### 5.2 Design Diagrams
The following diagrams are included in the `diagrams/` folder:
1. **Use Case Diagram** - Shows system actors and use cases
2. **Class Diagram** - Shows entity classes and relationships
3. **DFD Level 0 (Context Diagram)** - System boundary and external entities
4. **DFD Level 1** - Detailed process decomposition
5. **Activity Diagram** - Match scheduling workflow
6. **Sequence Diagram** - Result recording interaction flow
7. **Architecture Diagram** - System layer structure

---

## 6. Data Dictionary

| Table | Field | Type | Constraint |
|-------|-------|------|-----------|
| Department | dept_id | INT | PRIMARY KEY |
| Department | dept_name | VARCHAR(100) | NOT NULL, UNIQUE |
| Sport | sport_id | INT | PRIMARY KEY |
| Sport | sport_name | VARCHAR(50) | NOT NULL, UNIQUE |
| ScoringRule | sport_id | INT | PK, FK→Sport |
| ScoringRule | win_pts, draw_pts, loss_pts | INT | NOT NULL |
| Team | team_id | INT | PRIMARY KEY |
| Team | team_name | VARCHAR(100) | NOT NULL |
| Team | dept_id | INT | FK→Department |
| Team | sport_id | INT | FK→Sport |
| Player | player_id | INT | PRIMARY KEY |
| Player | player_name | VARCHAR(100) | NOT NULL |
| Player | team_id | INT | FK→Team |
| Player | jersey_no | INT | NOT NULL |
| Tournament | tournament_id | INT | PRIMARY KEY |
| Tournament | tournament_name | VARCHAR(200) | NOT NULL |
| Tournament | sport_id | INT | FK→Sport |
| Tournament | start_date | DATE | NOT NULL |
| MatchSchedule | match_id | INT | PRIMARY KEY |
| MatchSchedule | tournament_id | INT | FK→Tournament |
| MatchSchedule | team1_id, team2_id | INT | FK→Team |
| MatchSchedule | match_time | DATETIME | NOT NULL |
| MatchSchedule | team1_score, team2_score | INT | NULLABLE |
| MatchSchedule | status | VARCHAR(20) | CHECK constraint |
| PlayerMatchStat | match_id | INT | PK, FK→MatchSchedule |
| PlayerMatchStat | player_id | INT | PK, FK→Player |
| PlayerMatchStat | points_scored | INT | DEFAULT 0 |
| PlayerMatchStat | played | BOOLEAN | DEFAULT 1 |
