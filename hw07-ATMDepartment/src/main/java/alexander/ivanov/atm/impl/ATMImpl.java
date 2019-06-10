package alexander.ivanov.atm.impl;

import alexander.ivanov.atm.ATM;
import alexander.ivanov.atm.Cell;
import alexander.ivanov.atm.Money;
import alexander.ivanov.atm.Nominal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(ATMImpl.class);

    private Cell cells = new CellImpl();
    private Integer tmpAmount = 0;
    private List<Money> moneyBuf;

    @Override
    public boolean putCash(Number amount) {
        try {
            Money money = new MoneyImpl(amount);
            cells.put(money);
            //balance.addition(money);
        } catch (UnsupportedOperationException e) {
            logger.info("Inputs " + amount + " is not available!");
            return false;
        }
        logger.debug("cells.total() = {}, cells = {}", cells.total(), cells);

        return true;
    }

    @Override
    public boolean getCash(Number amount) {
        moneyBuf = new ArrayList<>();

        if (!checkCells(amount)) {
            logger.info("Not enough money");
            return false;
        }

        moneyBuf.forEach(money -> {
            cells.get(money);
            //balance.spend(money);
        });
        logger.debug("{} successfully written off.", amount);

        return false;
    }

    public boolean checkCells(Number amount) {
        if (cells.total().intValue() < amount.intValue()) {
            logger.info("total = {}, request = {}, isAllow = {}", cells.total().intValue(), amount, false);
            return false;
        }
        //logger.info("need to give amount = " + amount);
        int tmpTotal = cells.total().intValue();

        Map<Nominal, Integer> buf = cells.getCells();
        //logger.debug("cells.getCells().values() = {}", buf.keySet());

        tmpAmount = amount.intValue();
        buf.forEach((nominal, integer) -> {
            int count;
            while ((count = buf.getOrDefault(nominal, 0)) > 0) {
                //logger.info("1 Amount = " + nominal.getAmount());
                //logger.info("2 {} / {} = {}", tmpAmount, nominal.getAmount(), tmpAmount / nominal.getAmount().intValue());
                if (tmpAmount / nominal.getAmount().intValue() > 0) {
                    tmpAmount -= nominal.getAmount().intValue();
                    //logger.info("3 tmpAmount = " + tmpAmount);
                    if (tmpAmount >= 0) {
                        //logger.info("4 add = " + nominal.getAmount());
                        moneyBuf.add(new MoneyImpl(nominal.getAmount()));
                    }
                }
                buf.put(nominal, --count);
            }

        });

        int calcSum = moneyBuf.stream()
                .map(money -> money.getNominal().getAmount().intValue()).mapToInt(value -> value).sum();

        boolean res = amount.equals(calcSum);
        logger.info("total = {}, request = {}, response = {}, isAllow = {}", tmpTotal, amount, calcSum, res);

        return amount.equals(calcSum);
    }

    @Override
    public void printBalance() {
        logger.debug("balance = {}", cells.total());
    }

}
