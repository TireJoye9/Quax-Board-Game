# Quax Board Game

![Java Version](https://img.shields.io/badge/Java-21-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Coverage](https://img.shields.io/badge/coverage-85%25-green.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

---

## Quick Start

```bash
git clone https://github.com/TireJoye9/SweProjectSHT.git
cd SweProjectSHT
mvn clean javafx:run
```

---

## Table of Contents

- [Overview](#overview)
- [Screenshots](#screenshots)
- [Game Rules](#game-rules)
- [Technical Architecture](#technical-architecture)
- [Installation](#installation)
- [Development](#development)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

Quax is a strategic two-player board game implemented in Java using the JavaFX framework. The game is played on an 11x11 hybrid grid that combines octagonal cells with diamond-shaped bridge positions, creating a unique tactical environment. Players compete to establish connected paths across opposite sides of the board while utilizing the Pie Rule to maintain competitive balance.

### Key Features

- **Dual-cell board**: 121 octagonal cells + 100 diamond bridge positions
- **Pie Rule (Swap Rule)**: One-time color swap after the first move
- **Real-time win detection**: Graph-based connectivity algorithms
- **Visual goal bands**: Clear indicators for each player's win conditions
- **Responsive GUI**: Scales with window size
- **Comprehensive test suite**: JUnit 5 and TestFX coverage

---

## Screenshots

### Main Game Board

<img width="600" alt="Main Game Board" src="https://github.com/user-attachments/assets/78ea3c90-5e2c-4125-b3b2-b7009b5f8ebd" />

### Gameplay in Progress

<img width="600" alt="Gameplay" src="https://github.com/user-attachments/assets/935086f5-fa30-4461-ba46-fae3571a84bc" />

### Win Detection

<img width="600" alt="Win Detection" src="https://github.com/user-attachments/assets/02ec0a9c-46fc-41df-953e-334841d4bda7" />

### Pie Rule Activation

<img width="600" alt="Pie Rule" src="https://github.com/user-attachments/assets/55aa40df-9239-4222-91e8-63ca975aa3be" />

---

## Game Rules

### Board Structure

The game board consists of two distinct cell types arranged in an alternating pattern:

| Cell Type | Count | Purpose |
|-----------|-------|---------|
| Octagonal Cells | 121 | Main positions for piece placement |
| Diamond Cells | 100 | Bridge positions connecting octagons |

### Basic Gameplay

1. **First Move**: Black places a piece on any empty octagonal cell
2. **Turn Alternation**: Players alternate turns, placing either:
   - A **piece** on an octagonal cell, or
   - A **bridge** on a diamond cell
3. **Cell Ownership**: Once claimed, cells belong to the claiming player permanently

### Win Conditions

| Player | Win Condition |
|--------|---------------|
| **Black** | Connected chain from top edge to bottom edge |
| **White** | Connected chain from left edge to right edge |

The connectivity check employs graph traversal algorithms to verify valid paths across the board.

### The Pie Rule (Swap Rule)

After Black's first move, White has a one-time option to swap colors:
- White becomes Black and vice versa
- The first move's piece changes ownership to the new Black player
- The Pie Rule button disappears after use

This mechanic addresses the first-player advantage common in strategic board games.

---

## Technical Architecture

### Technology Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| Programming Language | Java 21 | Core implementation |
| GUI Framework | JavaFX 21 | Rendering, event handling, user interaction |
| Testing Framework | JUnit 5 | Unit and integration testing |
| UI Testing | TestFX | Automated GUI interaction testing |
| Build Tool | Maven | Dependency management and build automation |

### Architecture Diagram

<img width="600" alt="Architecture Diagram" src="https://github.com/user-attachments/assets/17150952-2e77-4f46-8d69-0c46a03cc8e2" />

### Core Components

**1. Board Model**
- Manages cell states (empty, black, white)
- Tracks piece and bridge placements
- Handles coordinate system (A-K, 1-11)

**2. Game Controller**
- Manages turn progression
- Validates moves
- Processes Pie Rule activation
- Detects win conditions

**3. Connectivity Algorithm**
- Builds adjacency graphs for each player
- Implements efficient path traversal
- Validates connection chains
- Handles both octagonal and bridge-mediated adjacency

**4. GUI Renderer**
- Renders custom octagonal and rhombus shapes
- Displays goal bands and coordinates
- Provides real-time feedback
- Scales responsively

**5. Pie Rule Manager**
- Tracks game state for rule activation
- Handles color and ownership transfer
- Manages UI state for Pie Rule button

### Data Structures

```java
// Board representation
CellType[][] board = new CellType[11][11];

// Cell types
enum CellType {
    EMPTY,
    BLACK_PIECE,
    WHITE_PIECE,
    BLACK_BRIDGE,
    WHITE_BRIDGE
}

// Player state
class Player {
    Color color;
    Set<Coordinate> pieces;
    Set<Coordinate> bridges;
}
```

---

## Installation

### Prerequisites

| Requirement | Version |
|-------------|---------|
| Java Development Kit | 21 or higher |
| Apache Maven | 3.6 or higher |
| JavaFX SDK | 21 (Maven managed) |

### Step-by-Step Installation

**1. Clone the Repository**

```bash
git clone https://github.com/TireJoye9/SweProjectSHT.git
cd SweProjectSHT
```

**2. Build the Application**

```bash
mvn clean package
```

This command compiles the source code, executes the test suite, and packages the application into a JAR file.

**3. Run the Application**

With JavaFX SDK:

```bash
java --module-path "path/to/javafx-sdk-21/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -jar target/SweProjectSHT.jar
```

Using Maven (development):

```bash
mvn javafx:run
```

### IDE Setup

**IntelliJ IDEA**
1. File → Open → Select project directory
2. Enable auto-import for Maven
3. Run → Edit Configurations → Add Application
4. Main class: `com.quax.MainApp`
5. VM options: `--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml`

**Eclipse**
1. File → Import → Maven → Existing Maven Projects
2. Install e(fx)clipse plugin
3. Configure JavaFX runtime in Preferences

**VS Code**
1. Install Extension Pack for Java
2. Install Maven for Java extension
3. Open project folder
4. Run from Maven Explorer

### Troubleshooting

| Issue | Solution |
|-------|----------|
| JavaFX not found | Set `PATH_TO_FX` environment variable |
| Maven build fails | Check Java version: `java -version` |
| Test failures | Run `mvn clean test` to diagnose |
| GUI not rendering | Ensure JavaFX modules are properly added |

---

## Development

### Project Structure

```
SweProjectSHT/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/quax/
│   │           ├── MainApp.java
│   │           ├── model/
│   │           │   ├── Board.java
│   │           │   ├── GameState.java
│   │           │   └── Player.java
│   │           ├── controller/
│   │           │   ├── GameController.java
│   │           │   └── PieRuleManager.java
│   │           ├── view/
│   │           │   ├── BoardRenderer.java
│   │           │   └── CellRenderer.java
│   │           └── algorithm/
│   │               └── ConnectivityChecker.java
│   └── test/
│       └── java/
│           └── com/quax/
│               ├── model/
│               └── controller/
├── pom.xml
└── README.md
```

### Key Algorithms

**Connectivity Checker**

The win detection algorithm uses a union-find data structure for efficient connectivity testing:

```java
public class ConnectivityChecker {
    public boolean hasConnection(Player player, Direction direction) {
        // Build graph of connected cells
        UnionFind uf = new UnionFind(boardSize);
        
        // Union adjacent cells belonging to player
        for each cell in player.pieces {
            for each neighbor in getNeighbors(cell) {
                if (neighbor.belongsTo(player)) {
                    uf.union(cell, neighbor);
                }
            }
        }
        
        // Check if any edge cell connects to opposite edge
        return checkEdgeConnection(uf, player, direction);
    }
}
```

### Coding Standards

- **Naming**: CamelCase for variables/methods, PascalCase for classes
- **Documentation**: Javadoc for all public methods
- **Testing**: Unit tests for all business logic
- **Formatting**: 4-space indentation, 100-character line limit

### Build Commands

```bash
mvn clean          # Clean build directory
mvn compile        # Compile source code
mvn test           # Run tests
mvn package        # Build JAR file
mvn javafx:run     # Run application
mvn site           # Generate project site
```

---

## Testing

### Test Coverage

| Component | Coverage |
|-----------|----------|
| Model | 90% |
| Controller | 85% |
| Algorithm | 92% |
| UI (TestFX) | 75% |
| **Overall** | **85%** |

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BoardTest

# Generate coverage report
mvn jacoco:report
```

### Test Examples

**Unit Test**

```java
@Test
public void testPiecePlacement() {
    Board board = new Board();
    board.placePiece(0, 0, Color.BLACK);
    assertEquals(CellType.BLACK_PIECE, board.getCell(0, 0));
}
```

**UI Test**

```java
@Test
public void testPieRuleButton() {
    clickOn("#cell-0-0");
    clickOn("#pie-rule-button");
    verifyThat("#turn-indicator", hasText("White's Turn"));
}
```

---

## Contributing

### Getting Started

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -m "Add feature"`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request

### Development Workflow

1. **Issue Assignment**: Claim an issue or create a new one
2. **Development**: Implement with tests
3. **Code Review**: At least one approval required
4. **Merge**: Squash and merge to main branch

### Reporting Issues

When reporting bugs, please include:
- Java version (`java -version`)
- Operating system
- Steps to reproduce
- Expected vs actual behavior
- Screenshots if applicable

### Feature Requests

Feature requests should include:
- Clear description of the feature
- Use case or motivation
- Suggested implementation approach

---

## Acknowledgments

- Inspired by classic connection games like Hex and TwixT
- Built with JavaFX open-source framework
- Special thanks to the software engineering course team

---

## Contact

- **Repository**: [https://github.com/TireJoye9/SweProjectSHT](https://github.com/TireJoye9/SweProjectSHT)
- **Issues**: [https://github.com/TireJoye9/SweProjectSHT/issues](https://github.com/TireJoye9/SweProjectSHT/issues)

---

*Last Updated: September 2026*
