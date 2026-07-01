# ICoop

A 2-player cooperative action-adventure game built in Java on top of a custom 2D game engine, developed as the second mini-project (MP2) of the EPFL CS-107 *Introduction à la programmation* course.

The game is inspired by [Fireboy and Watergirl](https://en.wikipedia.org/wiki/Fireboy_and_Watergirl): two players — one aligned with **Fire**, one with **Water** — must cooperate, each using their element's strengths and avoiding the other's weaknesses, to progress through a series of areas, solve puzzles, defeat enemies, and finally beat the boss.

![Overview of several IC-CoOp areas][overview]

---

## Table of Contents

- [Overview](#overview)
- [Goal of the Game](#goal-of-the-game)
- [Gameplay & Areas](#gameplay--areas)
- [Controls](#controls)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Extensions](#extensions)
- [Building & Running](#building--running)
- [Documentation](#documentation)

---

## Overview

**ICoop** ("IC Cooperative") is a top-down, tile-based cooperative game. Both players share the same world and must reach and clear the final boss room while staying alive. Progress is gated behind cooperative *challenges* that require both the Fire and Water player to contribute — neither player can finish the game alone.

The two playable characters are **elemental entities**:

![The two elemental players][players]

- The **Fire** player takes damage from water-type sources and vice versa.
- Element-specific walls, keys, orbs and weapons force the players to split tasks and coordinate.

The project is built on a reusable **game engine** (the `game-engine` module) that provides the rendering loop, audio, actor/area abstractions, physics-lite grid movement, and input handling. All game-specific logic lives in the `iccoop` module.

---

## Goal of the Game

Reach the boss room (**Sanctum**) and clear it while surviving. To unlock the way to the boss (the Manor Door), the players must complete the cooperative challenges spread across the world:

- **OrbWay** — collect the Fire and Water **Orbs** (granting immunity and summoning each player's companion).
- **Maze** — collect the two elemental **Staffs** (Fire and Water).
- **Arena** — collect the Fire and Water **Keys** to activate the teleporter back to Spawn.

Once the orbs, staffs and keys have been collected, the **Manor Door** opens and gives access to the endgame areas (**SanctumEntrance** and **Sanctum**).

![The Manor Door stays locked until the challenges are complete][manor-door-locked]

> *"You cannot pass here without defeating the maze and the arena!"* — the Manor Door remains locked until every challenge is complete.

---

## Gameplay & Areas

The world is made up of six connected areas:

| Area | Description |
|------|-------------|
| **Spawn** | Starting area where both players appear. Contains doors to **OrbWay** and **Maze**, plus the **Manor Door** that leads to the endgame (locked until all challenges are complete). |
| **OrbWay** | Contains **ElementalWalls** that can only be safely crossed by an entity of the matching element. **PressurePlates** let players deactivate the walls so the other can pass without taking damage. Here players collect the Fire and Water **Orbs**, which grant immunity and summon each player's **Companion**. |
| **Maze** | A labyrinth of **ElementalWalls** and **PressurePlates** requiring both players to cooperate. Guarded by fire-throwing **HellSkulls** (water-type damage) and **BombFoes** (roam randomly and drop bombs when a player enters their vision, then enter a protected state). Completing the maze yields the two elemental **Staffs**. |
| **Arena** | An open area with obstacles and destructible **Rocks** (broken with the elemental Staffs). Collect the Fire and Water **Keys** to activate a **Teleporter** back to Spawn. |
| **SanctumEntrance** | Transitional area between Spawn and the Sanctum. Contains chests with **Bombs** (highly effective against the **ElementalFoe** guarding the entrance). Defeating that foe spawns **Evolution Potions** that upgrade both players' companions and make them hostile to enemies. |
| **Sanctum** | The final boss arena. The **DarkLord** (King of the Night) randomly spawns elemental energy balls and can turn temporarily invisible. Defeating him triggers the final dialogue and ends the game. |

**Spawn — the two players and their elemental weapons**

![Spawn area with both players][spawn-weapons]

**Elemental walls (fire/water) and pressure plates**

![Elemental walls and pressure plates][maze-elements]

**HellSkulls launching fire in the Maze**

![HellSkulls throwing fire][hellskulls]

**BombFoes — roam and drop bombs near players**

![BombFoes][bombfoes]

---

## Controls

The game supports two players on a single keyboard. On-screen dialogs guide the players as they progress.

![The game window with an in-game dialog][game-window]

**Menus**

- **Main menu** — press **←** then **ENTER** to *Start*, or **→** then **ENTER** to *Quit*.
- **Pause menu** — press **ESCAPE** to open, then **←** + **ENTER** to *Resume*, or **→** + **ENTER** to *Quit*.

**In-game**

| Action | Player 1 | Player 2 |
|--------|----------|----------|
| Move up | **Z** | **I** |
| Move left | **Q** | **J** |
| Move down | **S** | **K** |
| Move right | **D** | **L** |
| Attack / Interact | **E** | **E** |
| Switch item (inventory) | **A** | **U** |
| Use item | **E** | **O** |

**Global**

- **T** — reset the current area
- **R** — reset the whole game
- **ESCAPE** — open the pause menu

---

## Project Structure

This is a multi-module Maven project (Java 21):

```
MP2-2024-UPDATE/
├── pom.xml                 # Parent POM (aggregates the modules)
├── game-engine/            # Reusable 2D game engine (rendering, audio, actors, areas, input)
├── iccoop/                 # The IC-CoOp game itself (main module)
└── tutos/                  # Tutorial modules used to learn the engine
```

The main game module is organized as follows:

```
iccoop/src/main/
├── java/ch/epfl/cs107/
│   ├── Play.java                     # Program entry point
│   └── icoop/
│       ├── ICoop.java                # Game definition: registers areas, players, transitions
│       ├── KeyBindings.java          # Keyboard mapping for both players
│       ├── actor/
│       │   ├── collectables/         # Orb, Key, Staff, Heart, EvolutionPotion, ...
│       │   ├── decor/                # Altar, DeadTree, Grass, Rock, Obstacle
│       │   ├── elemental/            # ElementalEntity, DamageType
│       │   └── entity/
│       │       ├── foe/              # Foe, BombFoe, HellSkull, ElementalFoe, DarkLord
│       │       ├── player/           # ICoopPlayer, ICoopCompanion, CenterOfMass
│       │       ├── projectile/       # Projectile, Flame, ElementalBall
│       │       └── props/            # Door, ManorDoor, ElementalWall, PressurePlate,
│       │                             #   Lever, Chest, Teleporter, Bomb, Mage
│       ├── area/
│       │   ├── ICoopArea.java        # Base class for all areas
│       │   ├── ICoopBehavior.java    # Cell-type behavior parsed from behavior maps
│       │   └── maps/                 # Spawn, OrbWay, Maze, Arena, SanctumEntrance, Sanctum
│       └── handler/                  # Cross-cutting logic (see Architecture below)
└── resources/
    ├── images/                       # Sprites, backgrounds, foregrounds, behavior maps
    ├── dialogs/                      # XML dialog scripts
    ├── sounds/                       # Music & sound effects
    └── fonts/                        # UI fonts
```

---

## Architecture

Some noteworthy design elements (see `CONCEPTION.md` for the full French write-up):

- **`handler/` packages** group the cross-cutting abstractions:
  - **`ICoopInteractionVisitor`** — the visitor interface driving interactions between actors (attacks, pickups, doors, plates, etc.).
  - **`TargetFollower` / `TargetEntity`** — lets an entity (e.g. a `BombFoe`) compute distance to and track an `ICoopPlayer` without breaking encapsulation.
  - **`Timer`** — a small reusable countdown used throughout the codebase wherever timed behavior is needed.
  - **`Challenge`** — behaves as a `Logic` signal that becomes active only once its underlying conjunction (`MultipleAnd`) of conditions is satisfied; used to gate area progression.
  - **`AreaCellTypeHandler`** — lets `ICoopBehavior` auto-register actors without exposing the `ICoopArea` to the behavior layer, avoiding an encapsulation leak.
  - **`ICoopInventory` / `ICoopItem`** — the per-player item system.
  - **`DialogHandler`** — plays the XML dialog scripts in `resources/dialogs/`.

- **Elemental system** — `ElementalEntity` and `DamageType` model the Fire/Water duality that underpins damage rules, walls, keys, orbs and weapons.

- **Menu system** — `AbstractMenu` (implementing `Menu`) provides a three-state (default / left-highlight / right-highlight) menu, specialized by `StartMenu` and `PauseMenu`, with `GameState` tracking the overall game state.

---

## Extensions

Beyond the base assignment, the project adds several features (detailed in `CONCEPTION.md`):

- **Menus** — a full start menu and in-game pause menu (`handler/menu`).
- **Companions** — `ICoopCompanion`, a follower that can **evolve** (via `EvolutionPotion`) and fight enemies when its player is attacked.
- **Final boss** — the `DarkLord`, which can turn invisible and summon foes and `ElementalBall` projectiles.
- **Ranged enemy** — `ElementalFoe`, able to attack the player from a distance with its staff.
- **New props** — `Chest` (single-item container, optionally locked behind a challenge), `Lever` (a `Logic` signal, e.g. opening the door from Maze to Arena), and `Mage` (appears after the boss dies to deliver the ending dialogue).
- **Animated decor** — `Altar` (periodically spawns hearts in the Sanctum), `DeadTree` and animated `Grass` in Spawn.
- **New areas** — `SanctumEntrance` and `Sanctum` for the endgame.

---

## Building & Running

### Requirements

- **JDK 21** (the project targets Java 21)
- **Maven 3.6+**

### Run from the command line

```bash
# From the repository root
mvn -pl iccoop -am compile
mvn -pl iccoop exec:java -Dexec.mainClass=ch.epfl.cs107.Play
```

### Build a runnable jar

The `iccoop` module is configured with the Maven Assembly plugin to produce a self-contained jar (`IC-CoOp-2024-jar-with-dependencies.jar`) with `ch.epfl.cs107.Play` as its main class:

```bash
mvn -pl iccoop -am package
java -jar iccoop/target/IC-CoOp-2024-jar-with-dependencies.jar
```

### Run from an IDE

Import the project as a Maven project and run the main class:

```
ch.epfl.cs107.Play
```

---

## Documentation

- **[`iccoop/src/main/java/ch/epfl/cs107/HELP.md`](iccoop/src/main/java/ch/epfl/cs107/HELP.md)** — player-facing help: goal, controls and a full walkthrough (in French).
- **[`iccoop/src/main/java/ch/epfl/cs107/CONCEPTION.md`](iccoop/src/main/java/ch/epfl/cs107/CONCEPTION.md)** — design document detailing the class architecture and extensions (in French).

---

*Developed as part of the EPFL CS-107 course (Mini-Project 2, 2024).*

<!-- ============================================================= -->
<!-- Embedded screenshots (base64 data URIs).                       -->
<!-- Kept here so images render everywhere, including IntelliJ's    -->
<!-- Markdown preview. Original PNGs live in docs/screenshots/.     -->
<!-- ============================================================= -->

[players]: docs/screenshots/players.png
[overview]: docs/screenshots/overview-areas.png
[manor-door-locked]: docs/screenshots/manor-door-locked.png
[game-window]: docs/screenshots/game-window.png
[spawn-weapons]: docs/screenshots/spawn-weapons.png
[maze-elements]: docs/screenshots/maze-elements.png
[hellskulls]: docs/screenshots/hellskulls.png
[bombfoes]: docs/screenshots/bombfoes.png
