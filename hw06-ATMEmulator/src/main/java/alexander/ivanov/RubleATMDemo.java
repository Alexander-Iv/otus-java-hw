package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RubleATMDemo {
    private static final Logger logger = LoggerFactory.getLogger(RubleATMDemo.class);

    public static void main(String[] args) {
        ATM atm = new RubleATM();

        atm.printBalance();

        atm.putCash(50); //указанный номинал не поддерживается банкоматом
        atm.putCash(100);
        atm.putCash(500);
        atm.putCash(500);
        /*
        atm.putCash(1000);
        atm.putCash(1000);
        atm.putCash(1000);
        atm.putCash(2000);
        atm.putCash(5000);
        */
        atm.printBalance();
        atm.getCash(100);
        //atm.getCash(15000); //не доступно
        atm.printBalance();
    }
}
