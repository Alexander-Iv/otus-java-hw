package alexander.ivanov.department.atm.memento;

import alexander.ivanov.department.atm.ATM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Caretaker {
    private static final Logger logger = LoggerFactory.getLogger(Caretaker.class);
    private List<Memento> history = new ArrayList<>();

    public void saveMemento(Memento memento) {
        this.history.add(memento);
    }

    public void restoreFirst(ATM atm) {
        if (!history.isEmpty()) {
            if (history.size() > 1) {
                atm.restoreCellState(history.get(1)); //последнее значние
            } else {
                atm.restoreCellState(history.get(0)); //текущее значение
            }
        }
    }

    @Override
    public String toString() {
        return "Caretaker{" +
                "history=" + Arrays.asList(history.toArray()) +
                '}';
    }
}