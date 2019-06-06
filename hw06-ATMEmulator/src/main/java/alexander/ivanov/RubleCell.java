package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class RubleCell implements Cell {
    private static final Logger logger = LoggerFactory.getLogger(RubleCell.class);
    private Map<RubleNominal, Integer> cells = new TreeMap<>(Collections.reverseOrder());
    private Integer total = 0;

    @Override
    public void put(Money amount) {
        Ruble rubles = ((Ruble)amount);
        cells.put(rubles.getNominal(), value(amount)+1);
    }

    @Override
    public void get(Money amount) {
        Ruble rubles = ((Ruble)amount);
        if (total().intValue() > rubles.getNominal().getAmount()) {
            cells.put(rubles.getNominal(), value(amount)-1);
        }
    }

    private Integer value(Money amount) {
        return cells.getOrDefault(amount.getNominal(), 0);
    }

    @Override
    public Number total() {
        total = 0;
        cells.forEach((rubleNominal, integer) -> {
            total += total(rubleNominal).intValue();
        });
        return total;
    }

    @Override
    public Number total(Nominal nominal) {
        Integer total = (cells.get(nominal) * nominal.getAmount().intValue());
        return total;
    }

    @Override
    public String toString() {
        return "RubleCell{" +
                "cells=" + cells +
                '}';
    }

    @Override
    public Map getCells() {
        return cells;
    }
}
