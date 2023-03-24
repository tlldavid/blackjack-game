public enum Suit {

    HEARTS("Hearts"), SPADES("Spades"), DIAMONDS("Diamonds"), CLUBS("Clubs");

    private final String suit;

    Suit(String suit) {
        this.suit = suit;
    }
    public String getSuit() {
        return this.suit;
    }

}
