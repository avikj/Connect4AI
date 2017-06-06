# Connect4AI 

Connect 4 with AI powered by Monte Carlo Tree Search.

## How to play

See the [Wikipedia entry on Connect 4](https://en.wikipedia.org/wiki/Connect_Four) for gameplay and rules.

### Installation/Setup

Clone the repository and compile code.

```sh
git clone https://github.com/avikj/Connect4AI.git
cd Connect4AI
javac *.java
```

### Two player

Play against another human locally.

```sh
java Connect4TwoPlayer
```

### Single player against AI

Play against the AI.

```sh
java Connect4SinglePlayer <seconds>
```

Optional argument for time given to AI to determine move; defaults to 2 seconds. Higher time values result in stronger but slower AI.

### AI against AI

Watch AI play against itself.

```sh
java Connect4AIvAI <seconds>
```

Optional argument for time given to AI to determine move; defaults to 2 seconds. Higher time values result in stronger but slower AI.
