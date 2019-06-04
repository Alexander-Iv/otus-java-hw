package alexander.ivanov;

public interface Balance {
    //void setAmount(Number amount);
    //Number getAmount();
    //Number amount();
    void addition(Money amount);
    void spend(Money amount);
    Number getBalance();
}
