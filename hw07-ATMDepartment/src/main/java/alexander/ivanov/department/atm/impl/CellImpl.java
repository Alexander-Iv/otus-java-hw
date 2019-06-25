package alexander.ivanov.department.atm.impl;

import alexander.ivanov.department.atm.Cell;
import alexander.ivanov.department.atm.Money;
import alexander.ivanov.department.atm.Nominal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class CellImpl implements Cell {
    private static final Logger logger = LoggerFactory.getLogger(CellImpl.class);
    private Map<Nominal, Integer> cells = new TreeMap<>(Collections.reverseOrder());

    @Override
    public void put(Money amount) {
        cells.put(amount.getNominal(), value(amount)+1);
    }

    @Override
    public void get(Money amount) {
        if (total().intValue() >= amount.getNominal().getAmount().intValue()) {
            cells.put(amount.getNominal(), value(amount)-1);
        }
    }

    private Integer value(Money amount) {
        return cells.getOrDefault(amount.getNominal(), 0);
    }

    @Override
    public Number total() {
        return cells.keySet().stream()
                .map(nominal -> total(nominal).intValue())
                .reduce(0, (integer, integer2) -> integer + integer2);
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

    @Override
    public Map<Nominal, Integer> copy(Map<Nominal, Integer> cells) {
        this.cells = new TreeMap<>(cells);
        return this.cells;
    }
}
