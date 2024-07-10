package classes;

import enums.Condition;
import enums.Contains;
import enums.Size;
import exceptions.InputException;

import static enums.Condition.*;
import static enums.Contains.MINE;
import static enums.Contains.VOID;

public class GameField {
    public GameField(Size s) {
        size = s;
        if (s == Size.SMALL) {
            width = 6;
            height = 13;
            numberOfMines = 10;
        }
        else if (s == Size.MEDIUM) {
            width = 9;
            height = 20;
            numberOfMines = 35;
        }
        else {
            width = 13;
            height = 27;
            numberOfMines = 75;
        }

        field = new Cell[height + 2][width + 2];
        gameOngoing = true;
        fillWith(0, CLOSED, VOID);
        cellsLeft = height * width - numberOfMines;
        for (int i = 0; i < numberOfMines; i++) {
            int lenPos = (int)(Math.random() * height + 1);
            int widPos = (int)(Math.random() * width + 1);
            if (field[lenPos][widPos].getContainment() == MINE) i--;
            else {
                field[lenPos][widPos].setContainment(MINE);
                for (int j = 0; j <= 7; j++) {
                    field[lenPos + circle1[j]][widPos + circle2[j]].addToNumberOfMines(1);
                }
            }
        }
    }

    public int getCellsLeft() {
        return cellsLeft;
    }

    private final char[] alphabet = "-abcdefghijklm".toUpperCase().toCharArray();
    private final int[] circle1 = {-1, -1, -1,  0, 0,  1, 1, 1};
    private final int[] circle2 = {-1,  0,  1, -1, 1, -1, 0, 1};

    private final int width;
    private final int height;
    private final int numberOfMines;

    public Size getSize() {
        return size;
    }

    private final Size size;

    private Cell[][] field;

    private boolean gameOngoing;
    public int getWidth() {
        return width;
    }

    public Cell[][] getField() {
        return field;
    }

    public int getHeight() {
        return height;
    }

    private int cellsLeft;

    private void fillWith(int numberOfMinesAround, Condition condition, Contains containment) {
        for (int i = 0; i < height + 2; i++) {
            for (int j = 0; j < width + 2; j++) {
                field[i][j] = new Cell(numberOfMinesAround, condition, containment);
            }
        }
    }

    public boolean checkCondition() {
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (field[i][j].getCondition() == OPEN && field[i][j].getContainment() == MINE) return false;
            }
        }
        int cnt = 0;
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (field[i][j].getCondition() == CLOSED && field[i][j].getContainment() != MINE) cnt++;
            }
        }
        cellsLeft = cnt;
        if (cnt == 0) return false;
        return (numberOfMines > 0 && gameOngoing && cellsLeft != 0);
    }

    public void buildField(boolean gameIsOver, boolean wrongInput) {

        System.out.print("\033[H\033[2J"); // UNIX
        System.out.flush();

        if (gameIsOver) {
            openAll();
        }

        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                if (i != 0 && j != 0) System.out.print(field[i][j]);
                else if (i != 0) System.out.print(i + (i < 10 ? "  " : " "));
                else if (j == 0) {
                    System.out.print("   ");
                }
                else {
                    System.out.print(alphabet[j] + " ");
                }
            }
            System.out.println();
        }
        if (wrongInput) System.out.println("Try again!");
    }

    private void openAll() {
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                field[i][j].setCondition(OPEN);
            }
        }
    }

    public void removeZeros() {
        int pre = -1;
        int now = 0;
        while (pre - now != 0) {
            for (int i = 1; i <= height; i++) {
                for (int j = 1; j <= width; j++) {
                    if (field[i][j].getNumberOfMinesAround() == 0 && field[i][j].getCondition() == OPEN) {
                        for (int d = 0; d < 8; d++) {
                            if (field[i + circle1[d]][j + circle2[d]].getCondition() == CLOSED) {
                                field[i + circle1[d]][j + circle2[d]].setCondition(OPEN);
                                now++;
                            }
                        }
                    }
                }
            }
            pre = now;
            now = 0;
        }
    }

    public void move(int[] moves) {
        int lenPos = moves[0];
        int widPos = moves[1];
        int flag = moves[2];
        if (flag == 1) {
            field[lenPos][widPos].setCondition(FLAG);
            return;
        }
        if (field[lenPos][widPos].getCondition() == CLOSED) cellsLeft++;
        else cellsLeft--;
        field[lenPos][widPos].setCondition(OPEN);
    }

    public void checkIfOutOfBounds(int[] moves) throws InputException {
        if (moves[0] > height || moves[0] < 1 || moves[1] > width || moves[1] < 1) throw new InputException();
    }

}
