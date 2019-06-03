package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleATM implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(RubleATM.class);

    private RubleBalance balance = new RubleBalance();
    private Integer total = 0;

    @Override
    public boolean put(Number amount) {
        try {
            balance.setAmount(amount);
        } catch (UnsupportedOperationException e) {
            logger.info("Inputs " + amount + " rubles is not available!");
            return false;
        }
        total += balance.getAmount();
        return true;
    }

    @Override
    public boolean get(Number amount) {
        if (total == null || total < amount.intValue()) {
            return false;
        }

        return false;
    }

    @Override
    public Integer total() {
        return total;
    }
}
