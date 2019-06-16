package alexander.ivanov.department.atm.memento;

import alexander.ivanov.department.atm.ATM;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class Caretaker {
    private Deque<Memento> history = new LinkedList<>();

    public void saveMemento(Memento memento) {
        this.history.add(memento);
    }

    public void restoreFirst(ATM atm) {
        if (!history.isEmpty()) {
            atm.restoreCellState(history.getFirst());
        }
    }

    @Override
    public String toString() {
        return "Caretaker{" +
                "history=" + Arrays.asList(history.toArray()) +
                '}';
    }
}