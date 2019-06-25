package alexander.ivanov.department;

import alexander.ivanov.department.atm.ATM;

import java.util.List;

public interface DepartmentATM {
    void add(List<ATM> atms);
    void remove(List<ATM> atms);
    void calcBalanceATMs();
    void resetATMs();
    void printInfo();
}
