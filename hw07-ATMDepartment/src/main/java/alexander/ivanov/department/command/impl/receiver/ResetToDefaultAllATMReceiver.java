package alexander.ivanov.department.command.impl.receiver;

import alexander.ivanov.department.atm.ATM;
import alexander.ivanov.department.command.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResetToDefaultAllATMReceiver implements Receiver {
    private static final Logger logger = LoggerFactory.getLogger(ResetToDefaultAllATMReceiver.class);
    private List<ATM> atms;

    public ResetToDefaultAllATMReceiver(List<ATM> atms) {
        this.atms = atms;
    }

    @Override
    public Boolean execute() {
        atms.forEach(atm -> {
            atm.getCaretaker().restoreFirst(atm);
        });
        return true;
    }
}
