package ma.ensa.ebanking.enums;

public enum AccountLimit {

    ACC_200(200, 1),
    ACC_5K(5_000, 2),
    ACC_20K(20_000, 3);

    private final int limit, level;

    AccountLimit(int limit, int level) {

        this.limit = limit;
        this.level = level;
    }

    public int getLimit(){
        return limit;
    }

    public AccountLimit upgrade(int level){
        int newLevel = this.level + level;
        return switch (newLevel){
            case 1 -> ACC_200;
            case 2 -> ACC_5K;
            default -> ACC_20K;
        };
    }
}
