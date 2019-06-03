package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ruble implements Money {
    private static final Logger logger = LoggerFactory.getLogger(Ruble.class);

    private NOMINAL nominal;

    public enum NOMINAL {
        R100(100),
        R200(200),
        R500(500),
        R1000(1000),
        R2000(2000),
        R5000(5000);

        private Integer amount;

        NOMINAL(Integer amount) {
            this.amount = amount;
        }

        public Integer getAmount() {
            return amount;
        }
    }

    @Override
    public NOMINAL value(Number amount) throws UnsupportedOperationException {
        for (NOMINAL nominal : NOMINAL.values()) {
            if (nominal.getAmount().equals(amount)) {
                this.nominal = nominal;
                break;
            }
        }

        //logger.info("nominal = " + nominal);

        if (nominal == null) {
            throw new UnsupportedOperationException("Amount not supported");
        }

        return nominal;
    }
}