package alexander.ivanov;

public interface Money {
    Nominal value(Number amount);
    Nominal getNominal() throws UnsupportedOperationException;
}
