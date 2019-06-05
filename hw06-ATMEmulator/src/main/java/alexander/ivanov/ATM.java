package alexander.ivanov;

public interface ATM {
    boolean putCash(Number amount);
    boolean getCash(Number amount);
    void printBalance();
}
