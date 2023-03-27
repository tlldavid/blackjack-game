import java.util.ArrayList;
import java.util.List;

public abstract class Participant {
    protected List<Card> hand;
    protected int handValue, numberOfAces, hitIteration;
    protected boolean isBusted;

    public Participant() {
        this.hand = new ArrayList<>();
        this.handValue = 0;
        this.isBusted = false;
        this.numberOfAces = 0;
    }

    public List<Card> getHand() {
        return this.hand;
    }
    public void addCard(Card card) {
        this.hand.add(card);
        this.handValue += card.getRank().getValue();
        this.checkForAce(card);
    }

    // Counts aces in each hand so that soft hits could be calculated.
    public void checkForAce(Card card) {
        if (card.getRank() == Rank.ACE)
            this.numberOfAces++;
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
    public void clearHand() {
        this.hand.clear();
    }

    public int getHitIteration() {
        return hitIteration;
    }

    public void plusHitIteration() {
        this.hitIteration++;
    }

    public void resetAll() {
        this.hand.clear();
        this.handValue = 0;
        this.numberOfAces = 0;
        this.hitIteration = 0;
        this.isBusted = false;

    }

}
