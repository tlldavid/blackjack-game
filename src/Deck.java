import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {

    private final List<Card> deckOfCards;

    public Deck() {
        this.deckOfCards = new ArrayList<>();
    }
    public void makeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deckOfCards.add(new Card(suit, rank));
            }
        }
    }
    public void shuffleDeck() {
        Collections.shuffle(deckOfCards);
    }
    public Card getCard() {
        return deckOfCards.remove(0);
    }

    public void clearDeck() {
        this.deckOfCards.clear();
    }

}
