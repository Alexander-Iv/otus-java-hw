package alexander.ivanov.atm.impl;

import alexander.ivanov.atm.Cell;
import alexander.ivanov.atm.Money;
import alexander.ivanov.atm.Nominal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class CellImpl implements Cell {
    private static final Logger logger = LoggerFactory.getLogger(CellImpl.class);
    private Map<Nominal, Integer> cells = new TreeMap<>(Collections.reverseOrder());
    private Integer total = 0;

    @Override
    public void put(Money amount) {
        cells.put(amount.getNominal(), value(amount)+1);
    }

    @Override
    public void get(Money amount) {
        if (total().intValue() > amount.getNominal().getAmount().intValue()) {
            cells.put(amount.getNominal(), value(amount)-1);
        }
    }

    private Integer value(Money amount) {
        return cells.getOrDefault(amount.getNominal(), 0);
    }

    @Override
    public Number total() {
        total = 0;
        cells.forEach((nominal, integer) -> {
            //logger.info("nominal = {}, count = {}", nominal.getAmount(), integer);
            total += total(nominal).intValue();
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
        return "Cell{" +
                "cells=" + Arrays.asList(cells) +
                '}';
    }

    @Override
    public Map getCells() {
        return cells;
    }
}
