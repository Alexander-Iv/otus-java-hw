package alexander.ivanov.department.atm;

public interface Money {
    Nominal value(Number amount);
    Nominal getNominal();
}
