package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ruble implements Money {
    private static final Logger logger = LoggerFactory.getLogger(Ruble.class);
    private RubleNominal nominal;

    public Ruble(Number amount) {
        this.nominal = value(amount);
    }

    @Override
    public RubleNominal value(Number amount) {
        for (RubleNominal nominal : RubleNominal.values()) {
            if (nominal.getAmount().equals(amount.intValue())) {
                this.nominal = nominal;
                break;
            }
        }
        return this.nominal;
    }

    public RubleNominal getNominal() throws UnsupportedOperationException {
        if (nominal == null) {
            throw new UnsupportedOperationException("Amount not supported");
        }
        return nominal;
    }
}