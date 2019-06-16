package alexander.ivanov.department.atm;

import alexander.ivanov.department.atm.impl.ATMImpl;
import alexander.ivanov.department.atm.impl.nominal.RubleNominal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ATMDemo {
    private static final Logger logger = LoggerFactory.getLogger(ATMDemo.class);

    public static void main(String[] args) {
        ATM atm = new ATMImpl();

        atm.printInfo();
        atm.putCash(50, 1); //указанный номинал не поддерживается банкоматом
        atm.putCash(100, 2);
        atm.putCash(500, 2);
        logger.info("");
        /*
        atm.putCash(1000);
        atm.putCash(1000);
        atm.putCash(1000);
        atm.putCash(2000);
        atm.putCash(5000);
        */
        atm.printInfo();
        logger.info("");
        //atm.getCash(100);
        //atm.getCash(100);
        atm.getCash(1200);
        logger.info("");
        atm.getCash(100); //не доступно - Not enough money, т.к. уже списали 1200
        logger.info("");
        atm.printInfo();
    }
}
