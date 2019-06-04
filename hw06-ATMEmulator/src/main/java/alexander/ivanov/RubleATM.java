package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleATM implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(RubleATM.class);

    private Balance balance = new RubleBalance();
    private Cell cells = new RubleCell();

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
        /*if (total == null || total < amount.intValue()) {
            return false;
        }*/

        Money money = new Ruble(amount);
        cells.get(money);
        balance.spend(money);
        logger.debug("cells.total() = {}", cells.total());

        return false;
    }

    @Override
    public void printBalance() {
        logger.debug("balance = {}", balance);
    }

}
