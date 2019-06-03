package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleATMDemo {
    private static final Logger logger = LoggerFactory.getLogger(RubleATMDemo.class);

    public static void main(String[] args) {
        ATM atm = new RubleATM();

        atm.put(50);
        atm.put(100);

        logger.info("atm.total() = " + atm.total());
    }
}
