package ma.ensa.ebanking.enums;

public enum AccountLimit {

    ACC_200(200),
    ACC_5K(5000),
    ACC_20K(20000);

    private final int limit;

    AccountLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit(){
        return limit;
    }
}
