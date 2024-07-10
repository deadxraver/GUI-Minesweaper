package classes;

import enums.Size;
import exceptions.InputException;

import java.util.Scanner;

public class Game {

    public void startConsole() {
        Scanner scanner = new Scanner(System.in);
        boolean correctInput = false;
        int dif = 0;
        while(!correctInput) {

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("Choose your difficulty:");
            System.out.println("1 - easy");
            System.out.println("2 - medium");
            System.out.println("3 - hard");
            dif = scanner.nextInt();
            if (dif > 3 || dif < 1) continue;
            else correctInput = true;
        }
        Size size = dif == 1 ? Size.SMALL : dif == 2 ? Size.MEDIUM : Size.LARGE;
        GameField gameField = new GameField(size);
        boolean wrongInput = false;
        while (gameField.checkCondition()) {
            gameField.buildField(false, wrongInput);
            wrongInput = false;
            System.out.print("Your move (e.g. a1 / a3f): ");
            try {
                int[] cells = WorkWithInput.convert(scanner.next());
                gameField.checkIfOutOfBounds(cells);
                gameField.move(cells);
                gameField.removeZeros();
            }
            catch (InputException e) {
                wrongInput = true;
            }
        }
        gameField.buildField(true, false);
        if (gameField.getCellsLeft() == 0) System.out.println("You win!");
        else System.out.println("Game over...");
    }
}
