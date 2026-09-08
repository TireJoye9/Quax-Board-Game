# Quax Board Game

A strategic two-player board game built with JavaFX, featuring a unique hybrid grid of octagonal cells and diamond-shaped bridges. The game challenges players to connect opposite sides of the board while offering the Pie Rule for balanced gameplay.

## 🎮 Overview

Quax is a turn-based strategy game played on an 11x11 board. Unlike traditional grid games, Quax features two types of playable cells:
- **Octagonal Cells (121 total)** - Main positions where players place their pieces
- **Diamond/Rhombic Cells (100 total)** - Bridge positions between octagons that can also be claimed

The game combines elements of connection games like Hex with unique bridge mechanics, creating a deep strategic experience.

## 📜 Game Rules

### Basic Gameplay
1. **First Move**: BLACK places a piece on any empty octagonal cell
2. **Turn Alternation**: Players alternate turns, placing either a piece (octagon) or a bridge (diamond)
3. **Win Conditions**:
   - BLACK wins by creating a connected chain from **top edge to bottom edge**
   - WHITE wins by creating a connected chain from **left edge to right edge**

### The Pie Rule (Swap Rule)
- After BLACK makes the first move, WHITE has a one-time option to **swap colors**
- If activated, the White player becomes Black and vice versa
- The first move's piece changes ownership to the new Black player
- The Pie Rule button disappears after use (can only be used once per game)

### Move Types
| Move Type | Placement | Effect |
|-----------|-----------|--------|
| Piece Move | Octagonal Cell | Claims an octagon for current player |
| Bridge Move | Diamond Cell | Claims a bridge connecting adjacent octagons |

## ✨ Features

- **Interactive GUI** built with JavaFX
- **Real-time turn indicator** with custom octagon and rhombus graphics
- **Pie Rule (Swap Rule)** activation button after first move
- **Win detection** using graph connectivity algorithms
- **Visual goal bands** showing each player's connection targets
- **Board coordinate system** (A-K letters for columns, 1-11 for rows)
- **Comprehensive test suite** with JUnit and TestFX
- **Responsive layout** that scales with window size

## 🛠 Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 21 | Core programming language |
| JavaFX 21 | GUI framework for rendering |
| JUnit 5 | Unit and integration testing |
| TestFX | UI testing framework |
| Maven | Dependency management and build tool |


## 🔧 Installation & Setup

### Prerequisites
- **Java 21** or higher
- **Maven** 3.6+
- **JavaFX 21** (Maven will handle this automatically)

### Clone the Repository
```bash
git clone https://github.com/TireJoye9/SweProjectSHT.git
cd SweProjectSHT

# Build the JAR first
mvn clean package

# Run the JAR
java --module-path "path/to/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml -jar target/SweProjectSHT.jar

