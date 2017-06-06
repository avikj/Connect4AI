# Connect4AI 

Connect 4 with AI powered by Monte Carlo Tree Search.

## How to play

See the [Wikipedia entry on Connect 4](https://en.wikipedia.org/wiki/Connect_Four) for gameplay and rules.

### Two player

Play against another human locally.

```sh
javac Connect4TwoPlayer.java
java Connect4TwoPlayer
```

### Single player against AI

Play against the AI.

```sh
javac Connect4SinglePlayer.java
java Connect4SinglePlayer <seconds>
```

Optional argument for time given to AI to determine move; defaults to 2 seconds. Higher time values result in slower but stronger AI.

### AI against AI

Watch AI play against itself.

```sh
javac Connect4AIvAI.java
java Connect4AIvAI
```
