# Sports Tournament Management and Team Performance Analysis System

### **Velalar College of Engineering and Technology (Autonomous), Erode – 638012**
**Department of Information Technology**  
**Academic Year:** 2026 – 2027 (ODD Semester) | **Class:** 25IT3A & 25IT3B  
**Activity:** Project-Based Assessment (Integrated 4-Subject Curriculum)

---

## 📋 Project Overview

The **Sports Tournament Management and Team Performance Analysis System** is an end-to-end, inter-disciplinary engineering project developed to fulfill the project-based assessment requirements across four fundamental computing subjects:

1. **Subject 1: Software Engineering (SE)** – SRS specification, 3-tier architecture, complete UML modeling suite, and software test plan with 100% pass verification.
2. **Subject 2: Database Management System (DBMS)** – Relational database model in Third Normal Form (3NF), Entity-Relationship (ER) diagram, automated DDL/DML schema, and 10 production SQL queries.
3. **Subject 3: Java Programming (OOP)** – Object-Oriented software architecture mapping all 5 university syllabus units, interactive management modules (`managements/`), custom exception handling, and Java 8 Stream analytics.
4. **Subject 4: Foundation of Data Science (FDS)** – Direct database retrieval using Python `sqlite3`, rigorous data preprocessing, descriptive statistical analysis (Mean, Median, Mode, Std Dev), Matplotlib charts, and a Jupyter Notebook.
5. **Interactive Deliverables** – A multi-subject visual HTML5 dashboard ([Tournament_Dashboard.html](Tournament_Dashboard.html)) and viva preparation guide ([VIVA_PREPARATION.md](VIVA_PREPARATION.md)).

---

## 🛠️ Technologies & Tools Used

| Domain | Technology / Tool | Version / Library |
| :--- | :--- | :--- |
| **Application Logic** | Java (JDK) | Java 17+ (SE) |
| **Object-Oriented Concepts** | Abstract Classes, Interfaces, Streams, Lambdas | Core Java Standard Library |
| **Database Engine** | SQLite 3 / MySQL 8 compatible | `sqlite3` self-contained database |
| **Database Design** | Third Normal Form (3NF), ER Modeling | Mermaid & Draw.io Diagramming |
| **Data Science & Analytics** | Python | Python 3.8+ (`sqlite3`, `matplotlib`, `numpy`, `pandas`) |
| **Interactive Dashboard** | HTML5, Modern CSS3, JavaScript | Chart.js 4+ |
| **Build & Execution** | Windows Command Scripts | Automated `.bat` Launchers |
| **Version Control** | Git | GitHub Submission Ready |

---

## 🗂️ Project Directory Structure

```text
E:\Sports Management System\
│
├── RUN_SUBJECTS.bat                    # Master Interactive Launcher for all 4 subjects
├── run_menu.bat                        # Quick shortcut launcher
├── Tournament_Dashboard.html           # Comprehensive 4-Subject Interactive Web Dashboard
├── README.md                           # Master Project Documentation & Execution Guide
├── VIVA_PREPARATION.md                 # Complete Viva Voce Q&A and Evaluation Guide
│
├── managements/                        # Modular Interactive Management CLI Launchers
│   ├── run_managements.bat             # Unified Management Menu (with suggested demo inputs)
│   ├── TeamManagement.bat              # Add, View, Search, Update, Delete Teams
│   ├── PlayerManagement.bat            # Register Players, View Roster, Participation
│   └── TournamentManagement.bat        # Schedule Matches, Enter Scores, View Standings
│
├── 1. Software Engineering/            # Subject 1 Deliverables
│   ├── SRS_Document.md                 # Software Requirements Specification (IEEE Std 830)
│   ├── Testing_Document.md             # Testing Techniques, 14 Test Cases (100% Pass) & Traceability
│   ├── TestPlan.md                     # Test Execution Strategy and Defect Reporting
│   ├── DBMS_Documentation.md           # Database Architecture in SE context
│   ├── software_engineering_output.txt # Automated verification output report
│   ├── run_software_engineering.bat    # Automated SE verification runner
│   └── diagrams/                       # 7 UML & Architecture System Diagrams (PNG)
│       ├── Architecture_Diagram.png    # 3-Tier System Architecture
│       ├── Use_Case_Diagram.png        # Actors & System Boundaries
│       ├── Class_Diagram.png           # OOP Class Relationships & Interfaces
│       ├── Sequence_Diagram.png        # Match Lifecycle & Score Recording
│       ├── Activity_Diagram.png        # Process Flow & Exception Decisions
│       ├── DFD_Level_0_Context_Diagram.png # Context Data Flow Diagram
│       └── DFD_Level_1.png             # Level 1 Decomposition
│
├── 2. Database Management System/      # Subject 2 Deliverables
│   ├── sports_tournament.db            # SQLite Database File (9 Tables + 2 Views in 3NF)
│   ├── Relational_Model.md             # Relational Schema, Keys & Constraints Documentation
│   ├── Normalization_Document.md       # 1NF, 2NF, 3NF & BCNF Proofs & Dependency Analysis
│   ├── run_dbms.py                     # Python DB script (Schema, Data, 10 Queries, CSV Export)
│   ├── run_dbms.bat                    # Automated DBMS Runner
│   ├── dbms_output.txt                 # Formatted SQL execution and query results log
│   ├── input.txt                       # Sample SQL input commands
│   ├── diagrams/
│   │   └── ER_Diagram.png              # Entity-Relationship (ER) Diagram
│   └── sql_scripts/
│       ├── 01_schema.sql               # DDL: 9 Base Tables + 2 Computed Views
│       ├── 02_sample_data.sql          # DML: Departments, Teams, Players, Fixtures, Stats
│       └── 03_queries.sql              # 10 Production Analytical SQL Queries
│
├── 3. Java Programming/                # Subject 3 Deliverables
│   ├── run_java.bat                    # Compiles all Java files & launches interactive menu
│   ├── input.txt                       # Documented sample inputs for CLI menus
│   ├── output.txt                      # 11-Phase Full System Simulation Execution Log
│   ├── TeamManagement_Output.txt       # Sample execution log for Team Management
│   ├── PlayerManagement_Output.txt     # Sample execution log for Player Management
│   ├── TournamentManagement_Output.txt # Sample execution log for Tournament Management
│   └── src/                            # Java Source Code (5 University Units)
│       ├── SportsTournament.java       # Core application, Domain Entities, Simulation & Streams
│       ├── TeamManagement.java         # Interactive CMD Menu: Team CRUD operations
│       ├── PlayerManagement.java       # Interactive CMD Menu: Player Registration & Stats
│       └── TournamentManagement.java   # Interactive CMD Menu: Match Scheduling & Reports
│
└── 4. Foundation of Data Science/       # Subject 4 Deliverables
    ├── analysis.py                     # DB Retrieval (sqlite3), Preprocessing & Matplotlib Visualizations
    ├── analysis_notebook.ipynb         # Interactive Jupyter Notebook for Data Analysis
    ├── run_analysis.bat                # Automated Data Science Runner
    ├── run_python.bat                  # Package install & execution helper
    ├── analysis_output.txt             # Statistical output log (Mean, Median, Mode, Std Dev)
    ├── output.txt                      # Execution status output
    ├── data/                           # Clean Preprocessed CSV Datasets
    │   ├── match_results.csv           # 10 Match fixtures and scores
    │   ├── standings.csv               # 5 Team standings and points
    │   ├── player_stats.csv            # 15 Player scoring statistics
    │   └── teams.csv                   # 5 Registered teams
    └── visualizations/                 # High-Resolution Matplotlib Charts (300 DPI)
        ├── goals_per_match.png         # Match-by-Match Goals Distribution Bar Chart
        ├── points_distribution.png     # Tournament Points Share Pie Chart
        └── top_scorers.png             # Golden Boot Top 5 Goal Scorers Horizontal Bar Chart
```

---

## 🚀 Execution Instructions

### Method 1: Master Launcher (Recommended)
Double-click **`RUN_SUBJECTS.bat`** (or run `run_menu.bat`) in the root directory.  
This provides a menu to run subjects individually or all in sequence:
* `[1]` Run Subject 1: Software Engineering (SRS, UML Diagrams, Test Plan)
* `[2]` Run Subject 2: Database Management System (SQLite Schema, Sample Data, 10 Queries)
* `[3]` Run Subject 3: Java Programming (Interactive Menus or Full Simulation)
* `[4]` Run Subject 4: Foundation of Data Science (Database Retrieval, Analytics, Visualizations)
* `[5]` Execute ALL Subjects in Sequence (1 to 4)
* `[6]` Open Visual Interactive HTML Dashboard in Web Browser
* `[7]` View Comprehensive Viva Preparation Guide
* `[8]` Exit

---

### Method 2: Individual Subject Execution

#### 1. Software Engineering
```cmd
cd "1. Software Engineering"
run_software_engineering.bat
```
* Generates `software_engineering_output.txt` verifying SRS requirements, UML diagrams, and all 14 test cases.

#### 2. Database Management System
```cmd
cd "2. Database Management System"
run_dbms.bat
```
* Creates/refreshes SQLite database `sports_tournament.db`.
* Creates 9 tables (in 3NF) and 2 SQL views (`v_match_results`, `v_standings`).
* Inserts sample data across all entities.
* Executes 10 analytical SQL queries and saves the formatted table output to `dbms_output.txt`.
* Exports cleaned CSV datasets to `4. Foundation of Data Science\data\`.

#### 3. Java Programming
```cmd
cd "3. Java Programming"
run_java.bat
```
* Compiles all Java files (`SportsTournament.java`, `TeamManagement.java`, `PlayerManagement.java`, `TournamentManagement.java`).
* Displays an interactive menu to choose between:
  * Team Management (CRUD)
  * Player Management (Registration & Roster)
  * Tournament Management (Scheduling & Results)
  * Full System Simulation (Runs all 11 phases and saves log to `output.txt`)

You can also navigate to `managements\` to run modular batch scripts:
```cmd
cd "managements"
run_managements.bat
```
*(All input prompts display suggested demo inputs in parentheses `( )` for seamless demonstration).*

#### 4. Foundation of Data Science
```cmd
cd "4. Foundation of Data Science"
run_analysis.bat
```
* Connects directly to SQLite database `sports_tournament.db` using `sqlite3`.
* Preprocesses and validates match scores, team points, and player stats.
* Computes descriptive statistics (Mean, Median, Mode, Variance, Standard Deviation).
* Generates 3 Matplotlib charts in `visualizations/`:
  1. `goals_per_match.png`
  2. `points_distribution.png`
  3. `top_scorers.png`
* An interactive Jupyter Notebook is also provided at `4. Foundation of Data Science\analysis_notebook.ipynb`.

---

## 📊 Summary of Tournament Results

* **Tournament:** Inter-Departmental Football Championship 2026
* **Teams Participating:** 5 (CSE Warriors, IT Strikers, ECE Thunders, MECH Titans, CIVIL Spartans)
* **Total Matches Played:** 10 (Round-Robin Fixtures)
* **Total Goals Scored:** 35 (Average 3.50 goals / match)
* **Tournament Champion:** **MECH Titans** (7 Points, +3 Goal Difference)
* **Tournament Runner-Up:** **CSE Warriors** (7 Points, +2 Goal Difference)
* **Golden Boot Top Scorer:** **Deepak M** (Information Technology) – 5 Goals
* **Software Verification:** 14 / 14 Test Cases Passed (100% Pass Rate)
* **Database Normalization:** Third Normal Form (3NF) with Zero Functional Redundancy

---

## ✅ Evaluation Deliverables Checklist

| Requirement | Description | Status | Deliverable File(s) |
| :--- | :--- | :---: | :--- |
| **Req 1 & 2** | Problem Analysis & SRS Document | ✅ Complete | `1. Software Engineering/SRS_Document.md` |
| **Req 3** | Use Case, Class & Architecture Diagrams | ✅ Complete | `1. Software Engineering/diagrams/*.png` (7 diagrams) |
| **Req 4** | Java Application (OOP, Input Handling, Logic) | ✅ Complete | `3. Java Programming/src/*.java`, `managements/` |
| **Req 5** | Database, ER Diagram, Relational Model, 10 Queries, Normalization | ✅ Complete | `2. Database Management System/` (Schema, DB, Docs, ERD) |
| **Req 6** | Python Database Retrieval, Preprocessing & Visualization | ✅ Complete | `4. Foundation of Data Science/analysis.py`, `analysis_notebook.ipynb` |
| **Req 7** | Testing Techniques, Test Cases (Expected vs Actual) | ✅ Complete | `1. Software Engineering/Testing_Document.md` (14/14 PASS) |
| **Req 8** | Upload Deliverables to Individual GitHub Repository | ✅ Ready | Complete organized repository structure with `.gitignore` |
| **Req 9** | Comprehensive `README.md` File | ✅ Complete | `README.md` |
| **Req 10** | Meaningful Python Data Visualizations | ✅ Complete | `visualizations/*.png` & `Tournament_Dashboard.html` |

---

## 🎓 Viva Voce Preparation
For a complete guide with 30+ expected viva voce questions, subject-wise answers, and presentation tips, review:
* [VIVA_PREPARATION.md](VIVA_PREPARATION.md)
* [Tournament_Dashboard.html](Tournament_Dashboard.html)
