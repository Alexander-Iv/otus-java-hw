package alexander.ivanov.department.atm;

import java.util.Map;

public interface Cell {
    void put(Money amount);
    void get(Money amount);
    Number total();
    Number total(Nominal nominal);
    Map getCells();
    Map<Nominal, Integer> copy(Map<Nominal, Integer> cells);
}
