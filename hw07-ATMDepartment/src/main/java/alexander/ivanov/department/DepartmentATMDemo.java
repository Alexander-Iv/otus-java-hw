package alexander.ivanov.department;

import alexander.ivanov.department.atm.ATM;
import alexander.ivanov.department.atm.impl.ATMImpl;
import alexander.ivanov.department.impl.DepartmentATMImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class DepartmentATMDemo {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentATMDemo.class);

    public static void main(String[] args) {
        DepartmentATM department = new DepartmentATMImpl();

        ATM atm1 = new ATMImpl();
        atm1.putCash(100, 500);
        atm1.putCash(200, 250);
        atm1.putCash(500, 125);
        atm1.putCash(1000, 60);
        atm1.putCash(2000, 30);
        atm1.putCash(5000, 15);
        //atm1.printInfo();

        ATM atm2 = new ATMImpl();
        atm2.putCash(100, 1000);
        atm2.putCash(500, 500);
        atm2.putCash(1000, 100);
        //atm2.printInfo();

        ATM atm3 = new ATMImpl();
        atm3.putCash(2000, 100);
        atm3.putCash(5000, 100);
        //atm3.printInfo();

        department.add(Arrays.asList(atm1,atm2,atm3));
        logger.info("************************************************************************************************");
        logger.info("**************************************** BEFORE CHANGES ****************************************");
        department.calcBalanceATMs();
        department.printInfo();

        atm1.getCash(5000, 10);
        atm2.getCash(5000, 10);
        atm3.getCash(5000, 10);

        logger.info("************************************************************************************************");
        logger.info("**************************************** AFTER CHANGES *****************************************");
        department.calcBalanceATMs();
        department.printInfo();

        department.resetATMs();
        logger.info("************************************************************************************************");
        logger.info("**************************************** AFTER RESTORES ****************************************");
        department.calcBalanceATMs();
        department.printInfo();
    }
}