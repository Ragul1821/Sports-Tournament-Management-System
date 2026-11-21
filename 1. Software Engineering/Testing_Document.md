# Software Testing Document
## Sports Tournament Management and Team Performance Analysis System

**Velalar College of Engineering and Technology, Erode-12**  
**Department of Information Technology**

---

## 1. Testing Strategy

### 1.1 Testing Levels
| Level | Description | Applied To |
|-------|-------------|-----------|
| Unit Testing | Test individual classes and methods | Java classes, SQL queries |
| Integration Testing | Test interaction between components | Java ↔ Database, DB ↔ Python |
| System Testing | Test complete end-to-end workflow | Full tournament lifecycle |

### 1.2 Testing Techniques
- **Black Box Testing** - Test based on requirements without code knowledge
- **White Box Testing** - Test based on internal code structure and logic
- **Boundary Value Analysis** - Test edge cases (zero scores, max players)
- **Equivalence Partitioning** - Group inputs into valid/invalid classes

---

## 2. Test Cases

### 2.1 Team Management Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-01 | Add valid team | ID=1, Name="CSE Warriors", Dept=1, Sport=2 | Team created successfully | Team created successfully | PASS |
| TC-02 | Add team with invalid dept | ID=6, Name="Test", Dept=99, Sport=2 | Error: Department not found | Error: Department not found | PASS |
| TC-03 | List all teams | - | Display 5 teams | 5 teams displayed | PASS |

### 2.2 Player Registration Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-04 | Register valid player | ID=1, Name="Arun", Team=1, Jersey=10 | Player registered | Player registered | PASS |
| TC-05 | Register player to team | Player→Team mapping | Player appears in team roster | Player in team.players list | PASS |
| TC-06 | List players with stats | - | 15 players with details | 15 players shown | PASS |

### 2.3 Tournament Registration Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-07 | Register team for tournament | Team=1, Tournament=1 | Registration successful | "Team 1 registered successfully" | PASS |
| TC-08 | Duplicate registration | Team=1, Tournament=1 (again) | Error: Already registered | "CAUGHT EXCEPTION: Team already registered" | PASS |
| TC-09 | Sport mismatch | Cricket team for Football tournament | Error: Sport mismatch | Error: Sport mismatch | PASS |
| TC-10 | Invalid tournament ID | Team=1, Tournament=99 | Error: Tournament not found | Error: Tournament not found | PASS |

### 2.4 Match Scheduling Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-11 | Schedule valid match | T1=1, T2=2, Time=09:00 | Match scheduled with ID | "Match 1 scheduled" | PASS |
| TC-12 | Schedule 10 matches | 10 unique matchups | All 10 scheduled | 10 matches created | PASS |
| TC-13 | Schedule conflict | Same team, same time | Error: Conflict | "CAUGHT CONFLICT" | PASS |
| TC-14 | Unregistered team | Team not in tournament | Error: Not registered | Error thrown | PASS |

### 2.5 Match Result Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-15 | Record valid result | Match=1, Score: 3-1 | Result recorded, status=COMPLETED | "Match 1 result recorded: 3 - 1" | PASS |
| TC-16 | Record all 10 results | 10 match scores | All 10 completed | 10 results recorded | PASS |
| TC-17 | Duplicate result | Match=1, Score: 5-5 | Error: Already recorded | "CAUGHT: Match result already recorded" | PASS |
| TC-18 | Draw result | Score: 2-2 | Winner = DRAW (0) | Draw correctly identified | PASS |

### 2.6 Standings Calculation Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-19 | Calculate standings | Tournament=1 | 5 teams sorted by points | Standings generated correctly | PASS |
| TC-20 | Win = 3 points | Team wins match | +3 points added | Points calculated correctly | PASS |
| TC-21 | Draw = 1 point | Match is draw | +1 point each team | Draw points correct | PASS |
| TC-22 | Goal difference tiebreak | Teams with same points | Sorted by GD | GD tiebreak applied | PASS |

### 2.7 Report Generation Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-23 | Match results report | Tournament=1 | Formatted table with all matches | Report generated | PASS |
| TC-24 | Standings report | Tournament=1 | Table with MP, W, D, L, GF, GA, GD, PTS | Report generated | PASS |
| TC-25 | Player report | - | Player stats with top scorer | Report with analytics | PASS |

### 2.8 Database Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-26 | Schema creation | Execute 01_schema.sql | 9 tables, 2 views created | Tables and views created | PASS |
| TC-27 | Data insertion | Execute 02_sample_data.sql | All records inserted | Records inserted successfully | PASS |
| TC-28 | JOIN query | Query 1 (teams + dept + sport) | 5 rows with all fields | 5 rows returned | PASS |
| TC-29 | Aggregate query | Query 7 (match stats) | Correct totals and averages | Statistics correct | PASS |
| TC-30 | Subquery | Query 6 (most wins) | Team with highest wins | Correct team returned | PASS |

### 2.9 Python Data Analysis Tests

| TC-ID | Description | Input | Expected Output | Actual Output | Status |
|-------|-------------|-------|-----------------|---------------|--------|
| TC-31 | CSV file loading | match_results.csv | 10 records loaded | "10 records" loaded | PASS |
| TC-32 | Data cleaning | Raw data | Missing values filled, duplicates removed | Clean data report | PASS |
| TC-33 | Descriptive stats | Goals data | Mean, median, std dev computed | Statistics computed | PASS |
| TC-34 | Chart generation | Standings data | 6 PNG charts saved | 6 charts generated | PASS |
| TC-35 | Correlation analysis | Goals vs Points | Pearson coefficient | Correlation computed | PASS |

---

## 3. Test Summary

| Category | Total Tests | Passed | Failed | Pass Rate |
|----------|-----------|--------|--------|-----------|
| Team Management | 3 | 3 | 0 | 100% |
| Player Registration | 3 | 3 | 0 | 100% |
| Tournament Registration | 4 | 4 | 0 | 100% |
| Match Scheduling | 4 | 4 | 0 | 100% |
| Match Results | 4 | 4 | 0 | 100% |
| Standings | 4 | 4 | 0 | 100% |
| Reports | 3 | 3 | 0 | 100% |
| Database | 5 | 5 | 0 | 100% |
| Python Analysis | 5 | 5 | 0 | 100% |
| **TOTAL** | **35** | **35** | **0** | **100%** |
