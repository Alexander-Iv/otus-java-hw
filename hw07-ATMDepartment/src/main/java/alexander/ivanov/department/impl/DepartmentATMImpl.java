package alexander.ivanov.department.impl;

import alexander.ivanov.department.DepartmentATM;
import alexander.ivanov.department.atm.ATM;
import alexander.ivanov.department.command.Command;
import alexander.ivanov.department.command.Invoker;
import alexander.ivanov.department.command.Receiver;
import alexander.ivanov.department.command.impl.command.CalculateBalanceAllATMCommand;
import alexander.ivanov.department.command.impl.command.ResetToDefaultAllATMCommand;
import alexander.ivanov.department.command.impl.invoker.InvokerImpl;
import alexander.ivanov.department.command.impl.receiver.CalculateBalanceAllATMReceiver;
import alexander.ivanov.department.command.impl.receiver.ResetToDefaultAllATMReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DepartmentATMImpl implements DepartmentATM {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentATMImpl.class);
    private List<ATM> atms = new ArrayList<>();
    private Number balance = 0;
    private Receiver receiver;
    private Command command;
    private Invoker invoker = new InvokerImpl();
    private Boolean resetResult;

    @Override
    public void add(List<ATM> atms) {
        this.atms.addAll(atms);
    }

    @Override
    public void remove(List<ATM> atms) {
        this.atms.removeAll(atms);
    }

    @Override
    public void calcBalanceATMs() {
        receiver = new CalculateBalanceAllATMReceiver(atms);
        command = new CalculateBalanceAllATMCommand(receiver);
        balance = invoker.execute(command);
    }

    @Override
    public void resetATMs() {
        receiver = new ResetToDefaultAllATMReceiver(atms);
        command = new ResetToDefaultAllATMCommand(receiver);
        resetResult = invoker.execute(command);
    }

    @Override
    public void printInfo() {
        logger.info("DepartmentATMImpl.printInfo");
        logger.info("**************************");
        logger.info("allBalance = " + balance);
        logger.info("resetResult = " + resetResult);
        //atms.forEach(atm -> atm.printInfo());
        logger.info("**************************");
        logger.info("");
    }
}
