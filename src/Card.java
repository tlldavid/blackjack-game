public class Card {

     private final Rank cardRank;
     private final Suit cardSuit;


     public Card(Suit cardSuit, Rank cardRank) {
         this.cardRank = cardRank;
         this.cardSuit = cardSuit;
     }

     public Rank getRank() {
         return this.cardRank;
     }

     public String toString() {
         return this.cardRank.getRank() + " of " + this.cardSuit.getSuit();
     }
}
