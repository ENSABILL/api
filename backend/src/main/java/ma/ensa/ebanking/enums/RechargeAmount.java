package ma.ensa.ebanking.enums;

public enum RechargeAmount {
    FIVE(5F),
    TEN(10F),
    TWENTY(20F),
    FIFTY(50F),
    ONE_HUNDRED(100F),
    TWO_HUNDRED(200F),
    FIVE_HUNDRED(500F);

    private final Float amount;

    RechargeAmount(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return this.amount;
    }
    static public boolean checkAmountIsValid(Float amount){
        for (RechargeAmount rechargeAmount: RechargeAmount.values()){
            if(rechargeAmount.getAmount().equals(amount)) return true;
        }
        return false;
    }
}
