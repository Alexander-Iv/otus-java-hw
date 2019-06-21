package alexander.ivanov.department.atm.memento;

import alexander.ivanov.department.atm.Cell;
import alexander.ivanov.department.atm.impl.CellImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Memento {
    private static final Logger logger = LoggerFactory.getLogger(Memento.class);
    private Cell cell = new CellImpl();

    public Memento(Cell cell) {
        this.cell.copy(cell.getCells());
    }

    public Cell getCell() {
        return cell;
    }

    @Override
    public String toString() {
        return "Memento{" +
                "cell.total()=" + cell +
                "}";
    }
}