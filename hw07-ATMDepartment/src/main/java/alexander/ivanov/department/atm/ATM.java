package alexander.ivanov.department.atm;

import alexander.ivanov.department.atm.memento.Caretaker;
import alexander.ivanov.department.atm.memento.Memento;

public interface ATM {
    Memento saveCellState();
    void restoreCellState(Memento memento);
    Caretaker getCaretaker();

    boolean putCash(Number amount);
    boolean putCash(Number amount, int count);
    boolean getCash(Number amount);
    boolean getCash(Number amount, int count);
    //State getState();
    Number getBalance();
    void printBalance();
    void printInfo();
}
