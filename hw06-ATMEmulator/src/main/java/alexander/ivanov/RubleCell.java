package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class RubleCell implements Cell {
    private static final Logger logger = LoggerFactory.getLogger(RubleCell.class);
    private Map<RubleNominal, Integer> cells = new TreeMap<>();
    private Integer total = 0;

    @Override
    public void put(Money amount) {
        //logger.debug("RubleCell.put");
        Ruble rubles = ((Ruble)amount);
        cells.put(rubles.getNominal(), value(amount)+1);
    }

    @Override
    public void get(Money amount) {
        //logger.debug("RubleCell.get");
        Ruble rubles = ((Ruble)amount);
        if (total().intValue() > rubles.getNominal().getAmount()) {
            cells.put(rubles.getNominal(), value(amount)-1);
        }
        //cells.getOrDefault(amount.getNominal(), 0);
    }

    private Integer value(Money amount) {
        return cells.getOrDefault(amount.getNominal(), 0);
    }

    @Override
    public Number total() {
        total = 0;
        cells.forEach((rubleNominal, integer) -> {
            //logger.info("Amount = {}, Count = {}", rubleNominal.getAmount(), integer);
            //total += (rubleNominal.getAmount() * integer);
            total += total(rubleNominal).intValue();
        });
        logger.debug("total = {}", total);
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
