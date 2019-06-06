package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RubleATM implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(RubleATM.class);

    private Balance balance = new RubleBalance();
    private Cell cells = new RubleCell();
    private Integer tmpAmount = 0;
    List<Money> moneyBuf;

    @Override
    public boolean putCash(Number amount) {
        try {
            Money money = new Ruble(amount);
            cells.put(money);
            balance.addition(money);
        } catch (UnsupportedOperationException e) {
            logger.info("Inputs " + amount + " rubles is not available!");
            return false;
        }
        logger.debug("cells.total() = {}", cells.total());

        return true;
    }

    @Override
    public boolean getCash(Number amount) {
        moneyBuf = new ArrayList<>();

        if(!checkCells(amount)) {
            logger.info("Not enough money");
            return false;
        }

        moneyBuf.forEach(money -> {
            cells.get(money);
            balance.spend(money);
        });
        logger.debug("RESULT cells.total() = {}", cells.total());

        return false;
    }

    public boolean checkCells(Number amount) {
        if (cells.total().intValue() < amount.intValue()) {
            logger.info("checkCells() = " + false);
            return false;
        }
        logger.info("need to give amount = " + amount);

        Map<RubleNominal, Integer> buf = cells.getCells();
        logger.debug("cells.getCells().values() = {}", buf.keySet());

        tmpAmount = amount.intValue();
        buf.forEach((rubleNominal, integer) -> {
            int count;
            while((count = buf.getOrDefault(rubleNominal, 0)) > 0) {
                logger.info("1 Amount = " + rubleNominal.getAmount());
                logger.info("2 {} / {} = {}", tmpAmount, rubleNominal.getAmount(), tmpAmount / rubleNominal.getAmount());
                if (tmpAmount / rubleNominal.getAmount() > 0) {
                    tmpAmount -= rubleNominal.getAmount();
                    logger.info("3 tmpAmount = " + tmpAmount);
                    if (tmpAmount >= 0) {
                        logger.info("4 add = " + rubleNominal.getAmount());
                        moneyBuf.add(new Ruble(rubleNominal.getAmount()));
                    }
                }
                buf.put(rubleNominal, --count);
            }

        });

        int calcSum = moneyBuf.stream()
                .map(money -> money.getNominal().getAmount().intValue()).mapToInt(value -> value).sum();
        logger.info("calcSum = " + calcSum);

        return amount.equals(calcSum);
    }

    @Override
    public void printBalance() {
        logger.debug("balance = {}", balance);
    }

}
