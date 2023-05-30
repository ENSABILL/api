package ma.ensa.ebanking.enums;

public enum RechargeAmount {
    FIVE(5F),
    TEN(10F),
    TWENTY(20F),
    FIFTY(50F);

    private final Float amount;

    RechargeAmount(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return this.amount;
    }
}
