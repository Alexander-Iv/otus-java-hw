package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleBalance implements Balance {
    private static final Logger logger = LoggerFactory.getLogger(RubleBalance.class);

    private Integer balance = 0;

    @Override
    public void addition(Money amount) {
        balance += amount.getNominal().getAmount().intValue();
    }

    @Override
    public void spend(Money amount) throws UnsupportedOperationException {
        if (balance >= amount.getNominal().getAmount().intValue()) {
            balance -= amount.getNominal().getAmount().intValue();
        } else {
            throw new UnsupportedOperationException("Not enough money");
        }
    }

    @Override
    public Integer getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "RubleBalance{" +
                "balance=" + balance +
                '}';
    }
}