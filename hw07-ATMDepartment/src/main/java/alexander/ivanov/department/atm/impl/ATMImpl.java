package alexander.ivanov.department.atm.impl;

import alexander.ivanov.department.atm.ATM;
import alexander.ivanov.department.atm.Cell;
import alexander.ivanov.department.atm.Money;
import alexander.ivanov.department.atm.Nominal;
import alexander.ivanov.department.atm.memento.Caretaker;
import alexander.ivanov.department.atm.memento.Memento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(ATMImpl.class);
    private Cell cells;
    private Integer tmpAmount = 0;
    private List<Money> moneyBuf;
    private Caretaker caretaker;

    public ATMImpl() {
        this(new CellImpl());
    }

    public ATMImpl(Cell cells) {
        this.cells = cells;
        caretaker = new Caretaker();
    }

    public Memento saveCellState() {
        //logger.info("ATMImpl.saveCellState");
        //logger.info("caretaker = " + caretaker);
        return new Memento(cells);
    }

    public void restoreCellState(Memento memento) {
        this.cells = memento.getCell();
    }

    @Override
    public Caretaker getCaretaker() {
        return caretaker;
    }

    @Override
    public boolean putCash(Number amount, int count) {
        for (int i = 1; i <= count; i++) {
            putCash(amount);
        }
        return true;
    }

    @Override
    public boolean putCash(Number amount) {
        try {
            Money money = new MoneyImpl(amount);
            cells.put(money);
        } catch (UnsupportedOperationException e) {
            logger.info("Inputs " + amount + " is not available!");
            return false;
        }
        //logger.debug("cells.total() = {}, cells = {}", cells.total(), cells);
        return true;
    }

    @Override
    public boolean getCash(Number amount, int count) {
        for (int i = 1; i <= count; i++) {
            getCash(amount);
        }
        return false;
    }

    @Override
    public boolean getCash(Number amount) {
        moneyBuf = new ArrayList<>();

        if (!checkCells(amount)) {
            logger.info("Not enough money");
            return false;
        }

        //logger.info("cells.total() = " + cells.total());

        moneyBuf.forEach(money -> {
            caretaker.saveMemento(saveCellState());
            cells.get(money);
            //balance.spend(money);
        });
        //logger.debug("{} successfully written off.", amount);

        return false;
    }

    public boolean checkCells(Number amount) {
        if (cells.total().intValue() < amount.intValue()) {
            //logger.info("total = {}, request = {}, isAllow = {}", cells.total().intValue(), amount, false);
            return false;
        }
        //logger.info("need to give amount = " + amount);
        int tmpTotal = cells.total().intValue();

        Map<Nominal, Integer> buf = cells.getCells();
        //logger.debug("cells.getCells().values() = {}", buf.keySet());

        tmpAmount = amount.intValue();
        buf.forEach((nominal, integer) -> {
            int count;
            while ((count = buf.getOrDefault(nominal, 0)) > 0 && tmpAmount != 0) {
                //logger.info("1 Amount = {}, count = {}", nominal.getAmount(), count);
                //logger.info("2 {} / {} = {}", tmpAmount, nominal.getAmount(), (tmpAmount / nominal.getAmount().intValue()));
                if ((tmpAmount / nominal.getAmount().intValue()) > 0) {
                    tmpAmount -= nominal.getAmount().intValue();
                    //logger.info("3 tmpAmount = " + tmpAmount);
                    if (tmpAmount >= 0) {
                        //logger.info("4 add = " + nominal.getAmount());
                        moneyBuf.add(new MoneyImpl(nominal.getAmount()));
                    }
                }
            }

        });

        int calcSum = moneyBuf.stream()
                .map(money -> money.getNominal().getAmount().intValue())
                .mapToInt(value -> value)
                .sum();

        boolean res = amount.equals(calcSum);
        //logger.info("total = {}, request = {}, response = {}, isAllow = {}", tmpTotal, amount, calcSum, res);

        return amount.equals(calcSum);
    }

    @Override
    public void printInfo() {
        logger.info("ATMImpl.printInfo");
        logger.info("**************************");
        logger.info("balance = " + getBalance());
        logger.info("cells = " + cells);
        logger.info("caretaker = " + caretaker);
        logger.info("**************************");
        logger.info("");
    }

    @Override
    public Number getBalance() {
        return cells.total();
    }

    @Override
    public void printBalance() {
        logger.debug("balance = {}", getBalance());
    }

}