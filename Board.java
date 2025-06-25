import java.util.*;

public class Board {
    public BoardItem[][] grid;
    private List<Ship> ships = new ArrayList<>();

    public Board() {
        grid = new BoardItem[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                grid[i][j] = new BoardItem();
    }

    public void placeShipsFromUser() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            System.out.println("Place ship of length " + (i+2));
            System.out.print("Row (0-9): ");
            int row = sc.nextInt();
            System.out.print("Col (0-9): ");
            int col = sc.nextInt();
            System.out.print("Horizontal? (true/false): ");
            boolean horiz = sc.nextBoolean();

            try {
                placeShip(new Ship(i + 2), row, col, horiz);
            } catch (InvalidPlacementException e) {
                System.out.println(e.getMessage());
                i--;
            }
        }
    }

    public void placeShipsRandomly() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(10);
                int col = rand.nextInt(10);
                boolean horiz = rand.nextBoolean();
                try {
                    placeShip(new Ship(i + 2), row, col, horiz);
                    placed = true;
                } catch (InvalidPlacementException ignored) {}
            }
        }
    }

    public void placeShip(Ship ship, int row, int col, boolean horiz) throws InvalidPlacementException {
        int len = ship.length;
        if ((horiz && col + len > 10) || (!horiz && row + len > 10)) {
            throw new InvalidPlacementException("Ship doesn't fit.");
        }

        for (int i = 0; i < len; i++) {
            int r = row + (horiz ? 0 : i);
            int c = col + (horiz ? i : 0);
            if (grid[r][c].hasShip()) {
                throw new InvalidPlacementException("Overlap with another ship.");
            }
        }

        for (int i = 0; i < len; i++) {
            int r = row + (horiz ? 0 : i);
            int c = col + (horiz ? i : 0);
            grid[r][c].placeShip(ship);
        }

        ships.add(ship);
    }

    public void print() {
        for (BoardItem[] row : grid) {
            for (BoardItem item : row) {
                System.out.print(item + " ");
            }
            System.out.println();
        }
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public void shoot(int row, int col) throws ShotException {
        if (row < 0 || row >= 10 || col < 0 || col >= 10) throw new ShotException("Invalid coordinates.");
        grid[row][col].shoot();
    }
}
