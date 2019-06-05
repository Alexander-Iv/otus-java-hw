package alexander.ivanov;

public interface Balance {
    void addition(Money amount);
    void spend(Money amount);
    Number getBalance();
}
