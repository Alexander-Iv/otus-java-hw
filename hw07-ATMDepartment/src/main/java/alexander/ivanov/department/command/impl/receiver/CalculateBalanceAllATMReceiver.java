package alexander.ivanov.department.command.impl.receiver;

import alexander.ivanov.department.atm.ATM;
import alexander.ivanov.department.command.Receiver;

import java.util.List;

public class CalculateBalanceAllATMReceiver implements Receiver {
    private List<ATM> atms;
    private Long amount;

    public CalculateBalanceAllATMReceiver(List<ATM> atms) {
        this.atms = atms;
        this.amount = 0L;
    }

    public void calcSum() {
        amount = atms.stream()
                .mapToLong(atm -> atm.getBalance().intValue())
                .sum();
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public Number execute() {
        calcSum();
        return getAmount();
    }
}
