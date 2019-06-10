package alexander.ivanov.atm;

public interface Money {
    Nominal value(Number amount);
    Nominal getNominal();
}
