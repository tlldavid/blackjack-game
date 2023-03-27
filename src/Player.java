public class Player extends Participant {

    private int balance, wager, splitWager;
    private boolean eligibleToDoubleDown, hasSplit, hasDoubledDown;

    public Player() {
        super();
        this.balance = 500;
        this.eligibleToDoubleDown = true;
        this.hasSplit = false;
        this.hasDoubledDown = false;
    }

    public void addBalance(int value) {
        this.balance += value;
    }

    public void minusBalance(int value) {
        this.balance -= value;
    }
    public int getBalance() {
        return this.balance;
    }
    public void setWager(int wager) {
        this.wager = wager;
    }
    public void doubleWager() {
        this.wager *= 2;
        this.hasDoubledDown = true;
    }

    public int getWager() {
        return this.wager;
    }

    public int getSplitWager() {
        return this.splitWager;
    }

    public void addSplitWager(int value) {
        this.splitWager += value;
    }

    public void minusSplitWager(int value) {
        this.splitWager -= value;
    }

    public void setSplitWager(int value) {
        this.splitWager = value;
    }

    public boolean hasDoubledDown() {
        return this.hasDoubledDown;
    }

    public boolean isEligibleToDoubleDown() {
        return this.eligibleToDoubleDown;
    }

    public void notEligibleToDoubleDown() {
        this.eligibleToDoubleDown = false;
    }

    public void playerSplits() {
        this.hasSplit = true;
    }

    public boolean hasSplit() {
        return this.hasSplit;
    }

    @Override
    public void resetAll() {
        super.resetAll();
        this.hasDoubledDown = false;
        this.hasSplit = false;
        this.eligibleToDoubleDown = true;
        this.wager = 0;
        this.splitWager = 0;
    }

}
