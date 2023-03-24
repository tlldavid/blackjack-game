import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Card> playerHand;
    private int handValue, numberOfAces, hitIteration;
    private boolean hasDoubledDown, hasSplit;

    public Player() {
        this.playerHand = new ArrayList<>();
        this.hasDoubledDown = false;
    }

    public void addCard(Card card) {
        this.playerHand.add(card);
        this.handValue += card.getRank().getValue();
        this.checkForAce(card);
    }

    // Counts aces in each hand so that soft hits could be calculated.
    public void checkForAce(Card card) {
        if (card.getRank() == Rank.ACE)
            numberOfAces++;
    }

    // Adjusts value of aces to allow soft hits.
    public void adjustForAces() {
        if (this.handValue > 21 && this.numberOfAces > 0)
            while (this.handValue > 21 && this.numberOfAces > 0) {
                this.handValue -= 10;
                this.numberOfAces -= 1;
            }
    }

    public int getHandValue() {
        return this.handValue;
    }

    public void clearHandValue() {
        this.handValue = 0;
    }

    public void addHandValue(int value) {
        this.handValue += value;
    }

    public List<Card> getHand() {
        return this.playerHand;
    }

    public void clearHand() {
        this.playerHand.clear();
    }

    public void doubledDown() {
        this.hasDoubledDown = true;
    }

    public void notDoubledDown() {
        this.hasDoubledDown = false;
    }

    public boolean hasDoubledDown() {
        return hasDoubledDown;
    }

    public void playerSplits() {
        this.hasSplit = true;
    }

    public boolean hasSplit() {
        return this.hasSplit;
    }

    public int getHitIteration() {
        return hitIteration;
    }

    public void plusHitIteration() {
        this.hitIteration++;
    }

    public void resetAll() {
        this.playerHand.clear();
        this.handValue = 0;
        this.numberOfAces = 0;
        this.hitIteration = 0;
        this.hasDoubledDown = false;
        this.hasSplit = false;
    }
}
