package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RubleATM implements ATM {
    private static final Logger logger = LoggerFactory.getLogger(RubleATM.class);

    private Balance balance = new RubleBalance();
    private Cell cells = new RubleCell();

    @Override
    public boolean putCash(Number amount) {
        try {
            Money money = new Ruble(amount);
            cells.put(money);
            balance.addition(money);
        } catch (UnsupportedOperationException e) {
            logger.info("Inputs " + amount + " rubles is not available!");
            return false;
        }
        logger.debug("cells.total() = {}", cells.total());

        return true;
    }

    @Override
    public boolean getCash(Number amount) {
        /*if (total == null || total < amount.intValue()) {
            return false;
        }*/

        checkCells(amount);

        Money money = new Ruble(amount);
        cells.get(money);
        balance.spend(money);
        logger.debug("cells.total() = {}", cells.total());

        return false;
    }

    public boolean checkCells(Number amount) {
        Map<RubleNominal, Integer> buf = cells.getCells();
        logger.debug("cells.getCells().values() = {}", buf.keySet());
        Integer tmpAmount = amount.intValue();

        logger.debug("buf.keySet().stream() = {}",
                buf.keySet()
                        .stream()
                        .filter(rubleNominal -> buf.getOrDefault(rubleNominal, 0) > 0 )
                        .filter(rubleNominal -> buf.getOrDefault(rubleNominal, 0) * rubleNominal.getAmount() <= amount.intValue() )
                        //.map(rubleNominal -> amount.intValue() % rubleNominal.getAmount())
                        .map(rubleNominal -> rubleNominal.getAmount() + " " + buf.getOrDefault(rubleNominal, 0))
                        .collect(Collectors.toList())
        );
        
        Arrays.stream(RubleNominal.values())
                .peek(integer -> logger.debug("before integer = {}", integer))
                .map(rubleNominal -> amount.intValue() % rubleNominal.getAmount())
                //.filter(rubleNominal -> amount.intValue() % rubleNominal.getAmount() == 0)
                .peek(integer -> logger.debug("after integer = {}", integer))
                .count();

        return true;
    }

    @Override
    public void printBalance() {
        logger.debug("balance = {}", balance);
    }

}
