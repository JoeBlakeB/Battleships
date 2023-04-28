# Logic Module

This is the module with the implementation of the game logic. It is independent of Android, can be tested
independently, and could potentially be used for other platforms.

## Features

- Functionality is split into three classes
  - GameBoard - Represents a single game grid
  - Opponent - Contains information about the opponent that the GameBoard is trying to shoot at
  - Battleship - Contains information about a single ship
- Different computer opponents
  - RandomPlayer - Easy computer which shoots randomly
  - ProbabilityPlayer - Hard computer which shoots intelligently by determining which locations are more likely to contain a ship.
  - OtherPlayer - Interface so that both opponents, and any potential new computer or multiplayer opponents, can all be accessed the same way by the UI logic.
- An opponent that can be manipulated
  - Opponent - An opponent that can not be changed, used in gameplay
  - MutableOpponent - An opponent which can have its ships moved, allowing the user to place their ships