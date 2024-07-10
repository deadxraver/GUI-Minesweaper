package classes;
import enums.Condition;
import enums.Contains;

public class Cell {
    public Cell(int numberOfMinesAround, Condition condition, Contains containment) {
        this.numberOfMinesAround = numberOfMinesAround;
        this.condition = condition;
        this.containment = containment;
    }

    public void addToNumberOfMines(int d) {
        numberOfMinesAround += d;
    }

    public Contains getContainment() {
        return containment;
    }

    public void setContainment(Contains containment) {
        this.containment = containment;
    }

    public int getNumberOfMinesAround() {
        return numberOfMinesAround;
    }

    private int numberOfMinesAround;
    private Condition condition;
    private Contains containment;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        if (condition == Condition.OPEN && numberOfMinesAround == 0) return "  ";
        else if (condition == Condition.OPEN && containment == Contains.VOID) return numberOfMinesAround + " ";
        else if (condition == Condition.CLOSED) return "? ";
        else if (condition == Condition.FLAG) return "F ";
        else return "X ";
    }
}
