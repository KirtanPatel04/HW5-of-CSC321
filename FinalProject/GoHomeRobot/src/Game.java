import java.util.*;

public class Game {
    private int gridSize;
    private char[][] grid;
    private int robotRow, robotCol;
    private int homeRow, homeCol;
    private int totalCost;
    private Stack<String> moveHistory;
    private HashMap<Integer, BadGuy> badGuys;
    private List<Obstacle> obstacles;
    private int level;

    public Game(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new char[gridSize][gridSize];
        this.totalCost = 0;
        this.moveHistory = new Stack<>();
        this.badGuys = new HashMap<>();
        this.obstacles = new ArrayList<>();
        this.level = 1;
        initializeGrid();
        spawnBadGuys();
        placeObstacles();
    }

    private void initializeGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = '.';
            }
        }

        Random random = new Random();
        robotRow = random.nextInt(gridSize);
        robotCol = random.nextInt(gridSize);
        homeRow = random.nextInt(gridSize);
        homeCol = random.nextInt(gridSize);

        grid[robotRow][robotCol] = 'R';
        grid[homeRow][homeCol] = 'H';
    }

    private void placeObstacles() {
        Random random = new Random();
        int obstacleCount = gridSize / 2;

        for (int i = 0; i < obstacleCount; i++) {
            int row, col;
            do {
                row = random.nextInt(gridSize);
                col = random.nextInt(gridSize);
            } while (grid[row][col] != '.');

            grid[row][col] = 'X';
            obstacles.add(new Obstacle(row, col));
        }
    }

    public boolean isObstacle(int row, int col) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getRow() == row && obstacle.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    public void play() {
        displayGameAim(); // Show the game's aim at the start.
        Scanner scanner = new Scanner(System.in);
        while (robotRow != homeRow || robotCol != homeCol) {
            printGrid();
            System.out.print("Enter move (i=up, j=left, l=right, m=down): ");
            char move = scanner.next().charAt(0);

            if (isValidMove(move)) {
                updateRobotPosition(move);
                totalCost++;
                moveBadGuys();
            } else {
                System.out.println("Invalid move or obstacle in the way!");
            }
        }

        System.out.println("Robot reached home! Total cost: $" + totalCost);
        scanner.close();
    }

    private void displayGameAim() {
        System.out.println("Welcome to the Robot Adventure Game!");
        System.out.println("Aim: Guide the robot (R) to its home (H) on the grid.");
        System.out.println("Avoid obstacles (X) and stay away from bad guys (B)!");
        System.out.println("Each move costs $1. Try to reach home with minimal cost.");
        System.out.println("Good luck!\n");
    }

    private boolean isValidMove(char move) {
        int newRow = robotRow, newCol = robotCol;
        switch (move) {
            case 'i': newRow--; break; // Up
            case 'j': newCol--; break; // Left
            case 'l': newCol++; break; // Right
            case 'm': newRow++; break; // Down
        }
        return newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize && !isObstacle(newRow, newCol);
    }

    private void updateRobotPosition(char move) {
        moveHistory.push(robotRow + "," + robotCol); // Save current position
        grid[robotRow][robotCol] = '.';
        switch (move) {
            case 'i': robotRow--; break;
            case 'j': robotCol--; break;
            case 'l': robotCol++; break;
            case 'm': robotRow++; break;
        }
        grid[robotRow][robotCol] = 'R';
    }

    private void printGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void spawnBadGuys() {
        Random random = new Random();
        int badGuyCount = level;

        for (int i = 0; i < badGuyCount; i++) {
            int row, col;
            do {
                row = random.nextInt(gridSize);
                col = random.nextInt(gridSize);
            } while (grid[row][col] != '.');

            BadGuy badGuy = new BadGuy(row, col);
            badGuys.put(i, badGuy);
            grid[row][col] = 'B';
        }
    }

    private void moveBadGuys() {
        for (Map.Entry<Integer, BadGuy> entry : badGuys.entrySet()) {
            BadGuy badGuy = entry.getValue();
            grid[badGuy.getRow()][badGuy.getCol()] = '.'; // Clear current position
            badGuy.moveTowards(robotRow, robotCol); // Move towards the robot

            if (isObstacle(badGuy.getRow(), badGuy.getCol())) {
                continue;
            }

            grid[badGuy.getRow()][badGuy.getCol()] = 'B'; // Update position
        }
    }

    public static void main(String[] args) {
        System.out.print("Enter grid size: ");
        Scanner scanner = new Scanner(System.in);
        int gridSize = scanner.nextInt();

        Game game = new Game(gridSize);
        game.play();
        scanner.close();
    }
}