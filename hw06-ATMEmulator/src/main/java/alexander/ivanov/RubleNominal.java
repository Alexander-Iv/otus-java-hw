package alexander.ivanov;

public enum RubleNominal implements Nominal {

    R100(100),
    R200(200),
    R500(500),
    R1000(1000),
    R2000(2000),
    R5000(5000);

    private Integer amount;

    RubleNominal(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
            return amount;
}

    @Override
    public String toString() {
        return "RubleNominal{" +
                "amount=" + amount +
                '}';
    }
}
