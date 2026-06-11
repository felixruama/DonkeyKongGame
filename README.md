# Princess vs Donkey Kong - 2D Java Game (OOP)
### ISCTE-IUL | Object-Oriented Programming

This repository contains the source code for a 2D top-down Java game focused on dungeon exploration (Dungeon Crawler). The project places a strong emphasis on **software architecture** and **design patterns**, ensuring a highly modular, extensible, and maintainable codebase.

---

## Game Mechanics and Features

The player controls the "Princess" and must navigate through multiple rooms, defeat enemies, and reach the final flag (`Flag`), while overcoming obstacles and managing resources and time.

- **Combat and Swords:** The player has HP (health) and attack damage. Collecting swords (`Sword`) permanently increases attack damage, which is essential for defeating stronger enemies in fewer hits.
- **Durability System (Meat):** A global tick-based system is implemented. Meat heals the player but **spoils after a short period of time**. The player must plan routes carefully to consume it before it decays.
- **Bomb System (AoE):** The player can collect bombs scattered throughout the map and store them. By pressing the **'B' key**, a bomb is placed on the ground. After a few turns, it explodes in an area-of-effect radius, destroying obstacles, damaging enemies, and triggering chain reactions.
- **Dynamic Traps:** Both visible traps (`Trap`) and hidden traps (`HiddenTrap`) exist. Hidden traps activate when the player steps on them, requiring careful navigation.
- **Varied Enemy AI:**
  - **Donkey Kong (`Enemy`):** The boss that must be defeated to unlock room doors (`Door`).
  - **Bats (`Bat`):** Random and unpredictable movement across the grid.
  - **Bananas:** Falling obstacles that act as environmental hazards.

---

## Software Architecture and OOP Design

The main value of this project lies in its technical architecture. Object-Oriented Programming principles were applied to ensure extensibility and maintainability.

### Design Patterns

- **Singleton:** Used in `GameEngine` and `ImageGUI` to guarantee a single global point of control for game state and rendering.
- **Observer:** `GameEngine` acts as an observer of the graphical interface (`ImageGUI`), reacting asynchronously to keyboard input events.
- **Factory Method:** Dynamic instantiation of classes based on characters read from `.txt` map files (e.g., 'H' = Player, 'W' = Wall).

### Polymorphism and Interfaces

The codebase is structured around strict interface contracts:

- `Attackable`: Shared between player and enemies, defining damage and death behavior.
- `Interactable`: Defines interactions with the environment (picking items, triggering traps).
- `TicksChangeble`: Critical for time-based mechanics, applied to objects that change over time (such as meat spoiling).
- `FallableTile`: Defines behavior for objects affected by gravity or falling mechanics.

### Abstract Class Hierarchy

Base classes such as `MoveableObject` (handling vector logic and collision detection) and `InteractableObjects` were implemented to avoid code duplication and support extensibility (Open/Closed Principle from SOLID).

---

## Technologies Used

- **Language:** Java (JDK 8+)
- **Libraries:** GUI based on `javax.swing` and `java.awt` (provided by the academic framework `pt.iscte.poo`)
- **Data Structures:** Extensive use of Java Collections (Lists, ArrayLists) and functional iteration (`forEach`) for spatial object management.

---

## How to Run the Game

### Prerequisites

- Java Development Kit (JDK) installed
- An IDE such as Eclipse, IntelliJ IDEA, or VS Code with Java support

### Installation and Execution

1. Clone the repository
2. Open the project in your IDE
3. Ensure the `images` folder and map files (`room0.txt`, etc.) are located in the project root
4. Run the main entry file: `Main.java`

### Controls

- Arrow Keys: Move the character (combat and item pickup are automatic on collision)
- 'B' Key: Drop a bomb (if collected)

---

## Game Visual

![Game Screenshot](https://github.com/user-attachments/assets/2a0ccd7e-69c2-4e30-806a-66496b07549b)
