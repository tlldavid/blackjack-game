public class Dealer extends Hand {

    public Dealer() {
        super();
    }

    // Method display dealer's face-up card. Uses timer to slow card presentation.
    public void showUpCard() {
        System.out.println("DEALER'S CARDS:");
        for (Card hand : getHand()) {
            Gameplay.delay(700);
            System.out.println(hand);
            Gameplay.delay(700);
            System.out.println("(X) of XXXXXX");
            Gameplay.delay(700);
            System.out.println("Card value: " + hand.getRank().getValue());
            Gameplay.delay(700);
            break;
        }
        System.out.println();
    }


}
