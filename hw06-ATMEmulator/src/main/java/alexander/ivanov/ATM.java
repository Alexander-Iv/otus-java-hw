package alexander.ivanov;

public interface ATM {
    boolean put(Number amount);
    boolean get(Number amount);
    Number total();
    //Money cell();
}
