package alexander.ivanov.atm.impl;

import alexander.ivanov.atm.Money;
import alexander.ivanov.atm.Nominal;
import alexander.ivanov.atm.impl.nominal.RubleNominal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MoneyImpl implements Money {
    private static final Logger logger = LoggerFactory.getLogger(MoneyImpl.class);
    private Nominal nominal;
    private List<? extends Nominal> nominals;

    public MoneyImpl(Number amount) {
        this(amount, Arrays.asList(RubleNominal.values())); // default Rubles
    }

    public MoneyImpl(Number amount, List<? extends Nominal> nominals) {
        this.nominals = nominals;
        this.nominal = value(amount);
    }

    @Override
    public Nominal value(Number amount) {
        for (Nominal nominal : nominals) {
            if (nominal.getAmount().equals(amount.intValue())) {
                this.nominal = nominal;
                break;
            }
        }
        return this.nominal;
    }

    public Nominal getNominal() throws UnsupportedOperationException {
        if (nominal == null) {
            throw new UnsupportedOperationException("Amount not supported");
        }
        return nominal;
    }
}