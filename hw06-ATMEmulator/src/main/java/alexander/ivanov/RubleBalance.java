package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleBalance implements Balance {
    //private static final Logger logger = LoggerFactory.getLogger(RubleBalance.class);

    private Integer amount;
    private Ruble.NOMINAL nominal;
    private Money money = new Ruble();

    @Override
    public void setAmount(Number amount) {
        //logger.info("nominal = " + nominal);
        nominal = (Ruble.NOMINAL)money.value(amount);
        //logger.info("nominal = " + nominal);
    }

    @Override
    public Integer getAmount() {
        //logger.info("RubleBalance.getAmount");
        //logger.info("nominal = " + nominal.getAmount());
        return nominal.getAmount();
    }
}
