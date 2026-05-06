# Smart Energy Distribution System
## Java Swing Desktop Application

### Files (place all in the same folder):
- MainApp.java
- LandingPage.java
- LoginPage.java
- DashboardPage.java
- SmartEnergyDistributionPage.java
- GridAnalysisPage.java
- AlgorithmComparisonPage.java
- StatisticsPage.java
- AboutPage.java

---

### How to Compile & Run in VS Code

**Option 1 — Terminal (Recommended)**
```bash
# Navigate to your project folder
cd path/to/SmartEnergySystem

# Compile all Java files at once
javac *.java

# Run the application
java MainApp
```

**Option 2 — VS Code with Java Extension**
1. Install the "Extension Pack for Java" from Microsoft in VS Code
2. Open the folder containing all .java files
3. Right-click `MainApp.java` → "Run Java"

---

### Login Credentials
- Username: `admin`
- Password: `1234`

---

### Features
- **Landing Page** — Animated grid background, project intro, navigation
- **Login Page** — Split-screen auth with hardcoded credentials
- **Dashboard** — Summary cards, quick actions, system info
- **Energy Distribution** — Add power stations/lines, run Prim's or Kruskal's MST
- **Grid Analysis** — Full edge list, adjacency matrix, cost breakdown
- **Algorithm Comparison** — Execution time benchmarks, visual bar chart
- **Statistics** — KPI cards, savings analysis, progress bars
- **About** — Project overview, technologies, algorithm details

---

### Sample Workflow
1. Go to **Energy Distribution**
2. Click **"Load Sample Grid"** (6 nodes, 10 edges pre-loaded)
3. Click **"Run Prim's Algorithm"** or **"Run Kruskal's Algorithm"**
4. View results in the table and graph area
5. Navigate to **Grid Analysis** and **Algorithm Comparison** for detailed views

---

### Requirements
- Java 8 or higher (JDK)
- No external libraries needed — pure Java Swing
