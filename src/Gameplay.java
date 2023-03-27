import java.lang.StringBuilder;

public class Gameplay {

    private final Deck deckOfCards;
    private final Player player;
    private final Player split;
    private final Dealer dealer;
    private final StringBuilder playerHandString;


    public Gameplay(Player player, Dealer dealer, Player split) {
        this.player = player;
        this.dealer = dealer;
        this.split = split;
        this.deckOfCards = new Deck();
        this.playerHandString = new StringBuilder();
    }


    // Creates deck and shuffles cards. Deals two cards each to player and dealer.
    public void dealInitialCards() {
        deckOfCards.makeDeck();
        deckOfCards.shuffleDeck();
        for (int i = 0; i < 2; i++) {
            Card playerCard = deckOfCards.getCard();
            player.addCard(playerCard);
        }
        for (int i = 0; i < 2; i++) {
            Card dealerCard = deckOfCards.getCard();
            dealer.addCard(dealerCard);
        }
        showPlayersCards();
    }

    /* Method displays player's cards. Uses timer to slow card presentation.
    StringBuilder playerHandString appends cards to a string after they are revealed.
    This allows subsequent showings of player's hand to bypass timer. */
    public void showPlayersCards() {
        player.adjustForAces();
        System.out.println("\nYOUR CARDS:");
        if (playerHandString.length() != 0) {
            System.out.println(playerHandString);
        }
        for (Card hand : player.getHand()) {
            delay(700);
            if (playerHandString.length() == 0) {
                System.out.println(hand);
                playerHandString.append(hand);
            } else {
                System.out.println(hand);
                playerHandString.append("\n").append(hand);
            }
        }
        delay(700);
        appendPlayerHand();
    }

    /* Method to add new cards to player's hand.
    Since previously shown cards are appended to a string, we
    clear the hand to ensure cards aren't displayed twice. */
    public void hitMe() {
        player.clearHand();
        Card newCard = deckOfCards.getCard();
        player.addCard(newCard);
        player.adjustForAces();
        showPlayersCards();
    }

    /* Checks if player's initial cards constitute blackjack.
    If player achieves blackjack, player's balance is updated,
    game is reset, and method returns true. */
    public boolean checkBlackjack() {
        if (player.getHandValue() == 21) {
            player.addBalance((int) (player.getWager() * 1.5));
            System.out.println("BLACKJACK! YOU WIN $" + ((int) (player.getWager() * 1.5)) + "!\n");
            resetGame();
            return true;
        }
        showDealersUpCard();
        return false;
    }

    /* Checks if player or dealer has busted.
    Parameter checks whether hand belong to player or dealer. */
    public boolean checkIfBusted(Hand hand) {
        if (hand instanceof Player) {
            if (player.getHandValue() > 21) {
                if (player.hasDoubledDown()) {
                    System.out.print("\n");
                }
                System.out.println("YOU BUSTED! PLAY AGAIN?\n");
                player.minusBalance(player.getWager());
                resetGame();
                return true;
            }
            return false;
        }
        if (hand instanceof Dealer) {
            if (player.hasSplit() && dealer.getHandValue() > 21) {
                System.out.printf("\nDealer busts with %d! \nYOU WIN $%d!\n\n",
                        dealer.getHandValue(), player.getSplitWager());
                player.addBalance(player.getSplitWager());
                dealer.clearHandValue();
                return true;
            } else if (dealer.getHandValue() > 21) {
                System.out.printf("\nDealer busts with %d! \nYOU WIN $%d!\n\n",
                        dealer.getHandValue(), player.getWager());
                player.addBalance(player.getWager());
                resetGame();
                return true;
            }
        }
        return false;
    }

    // Method display dealer's face-up card. Uses timer to slow card presentation.
    public void showDealersUpCard() {
        System.out.println("DEALER'S CARDS:");
        for (Card hand : dealer.getHand()) {
            delay(700);
            System.out.println(hand);
            delay(700);
            System.out.println("(X) of XXXXXX");
            delay(700);
            System.out.println("Card value: " + hand.getRank().getValue());
            delay(700);
            break;
        }
        System.out.println();
    }

    /* Method that reveals dealer's cards. dealerHitsOrBusts() is
    called at the end of method to check condition of dealer's hand. */
    public void dealerReveals() {
        dealer.adjustForAces();
        System.out.println("\nDEALER'S HAND:");
        for (Card card : dealer.getHand()) {
            delay(700);
            System.out.println(card);
        }
        delay(700);
        System.out.println("Hand value: " + dealer.getHandValue() + "\n");
        delay(700);
        if (!dealerHitsOrBusts() && !player.hasSplit())
            determineWinner();
    }

    /* Instructs dealer to draw cards until 17 is reached or dealer busts.
    Returns TRUE if dealer busts. */
    public boolean dealerHitsOrBusts() {
        while (dealer.getHandValue() < 17) {
            Card newCard = deckOfCards.getCard();
            dealer.addCard(newCard);
            dealer.adjustForAces();
            System.out.println("DEALER'S NEW CARD:");
            delay(700);
            System.out.println(newCard);
            delay(700);
            System.out.println("Hand value: " + dealer.getHandValue() + "\n");
            delay(700);
            if (checkIfBusted(dealer)) {
                dealer.clearHandValue();
                return true;
            }
        }
        return false;
    }

    // Checks if dealer's initial cards constitute blackjack. Ends game if true.
    public boolean dealerPeek() {
        if (dealer.getHand().get(0).getRank() == Rank.ACE ||
                dealer.getHand().get(0).getRank().getValue() == 10) {
            printPeekMessage();
            if (dealer.getHandValue() == 21) {
                System.out.println("DEALER'S HAND:");
                for (Card card : dealer.getHand()) {
                    delay(500);
                    System.out.println(card);
                }
                delay(500);
                System.out.println("Hand value: " + dealer.getHandValue() + "\n");
                delay(500);
                System.out.println("Sorry! Dealer has blackjack! Try again!\n");
                player.minusBalance(player.getWager());
                resetGame();
                return true;
            } else {
                System.out.println("Dealer does not have blackjack.\n");
            }
        }
        return false;
    }

    // If neither player nor dealer busts, this method evaluates winner based on value of hands.
    public void determineWinner() {
        if (player.getHandValue() > dealer.getHandValue()) {
            System.out.printf("\nYour %d beats dealer's %d\nYOU WIN $%d! PLAY AGAIN?\n\n",
                    player.getHandValue(), dealer.getHandValue(), player.getWager());
            player.addBalance(player.getWager());
            delay(1000);
            resetGame();
        } else if (dealer.getHandValue() > player.getHandValue()) {
            System.out.printf("\nDealer's %d beats your %d\nYOU LOSE $%d! PLAY AGAIN?\n\n",
                    dealer.getHandValue(), player.getHandValue(), player.getWager());
            delay(1000);
            player.minusBalance(player.getWager());
            resetGame();
        } else {
            System.out.printf("\nDealer's %d equals your %d\nIT'S A PUSH! PLAY AGAIN?\n\n",
                    dealer.getHandValue(), player.getHandValue());
            delay(1000);
            resetGame();
        }
    }

    // Following methods handle logic of splitting cards into two separate hands and bets.
    // splitHand returns 21 to UI if player achieves blackjack after each hand is completed.
    public int splitHand(int handNumber) {
        if (handNumber == 1) {
            player.playerSplits();
            player.doubleWager();
            player.setSplitWager(player.getWager());
            split.addCard((player.getHand().remove(1)));
            split.addCard(deckOfCards.getCard());
            player.addCard(deckOfCards.getCard());
            printDealing();
            printLoading();
            System.out.println();
            if (handleSplit(player) == 21) {
                return 21;
            }
        }
        if (handNumber == 2) {
            if (handleSplit(split) == 21) {
                return 21;
            }
        }
        if (handNumber == 3) {
            dealerReveals();
            // If dealer busted, their hand value would be reset.
            // If dealer hasn't busted, player hands will be evaluated.
            if (dealer.getHandValue() > 0) {
                evaluateSplit(player, "ONE");
                evaluateSplit(split, "TWO");
            }
            resetGame();
            delay(500);
        }
        return -1;
    }

    // Prints cards of each split hand. Returns 21 if blackjack is achieved.
    public int handleSplit(Player hand) {
        hand.clearHandValue();
        delay(500);
        if (hand == this.player) {
            System.out.println();
            System.out.println("HAND ONE: ");
        } else {
            System.out.println("HAND TWO: ");
        }
        for (Card card : hand.getHand()) {
            delay(500);
            hand.addHandValue(card.getRank().getValue());
            System.out.println(card);
        }
        delay(500);
        System.out.println("Hand value: " + hand.getHandValue() + "\n");
        if (hand.getHandValue() == 21) {
            System.out.printf("BLACKJACK! You win $%d! \n\n", (int) ((player.getWager() / 2) * 1.5));
            player.addSplitWager((int) ((player.getWager() / 2) * 1.5));
            return 21;
        }
        return -1;
    }

    /* Separate helper method for hitting on split hands.
    Takes the player's hand as parameter (player's default hand or split hand)
    Declares variable that subtracts value of player's original bet if hand busts.
    Method returns -1 if hand busts, or 1 if hand remains alive. */
    public int hitOnSplit(Player hand) {
        int subtractBust = (player.getWager() / 2);
        Card newCard = deckOfCards.getCard();
        hand.addCard(newCard);
        hand.adjustForAces();
        delay(300);
        System.out.println("NEW CARD:");
        delay(700);
        System.out.println(newCard);
        delay(700);
        System.out.println("New hand value: " + hand.getHandValue() + "\n");
        delay(700);
        if (hand.getHandValue() > 21) {
            System.out.println("BUST! This hand went over 21.\n");
            player.minusSplitWager(subtractBust);
            player.minusBalance(subtractBust);
            hand.clearHandValue();
            return -1;
        }
        return 1;
    }

    // Outcome of split hands are determined here.
    public void evaluateSplit(Player hand, String handCount) {
        delay(200);
        if (dealerHitsOrBusts()) {
            return;
        }
        if (hand.getHand().get(0).getRank().getValue() +
                hand.getHand().get(1).getRank().getValue() == 21) {
            System.out.printf("HAND %s: BLACKJACK! Your hand equals 21 \nYou win $%d!\n",
                    handCount, (int) (player.getWager() * 0.75));
            player.addBalance((int) (player.getWager() * 0.75));
        } else if (hand.getHandValue() == 0) {
            System.out.printf("HAND %s: BUST! This hand went over 21 \nYou lose $%d!\n",
                    handCount, player.getWager() / 2);
        } else if (hand.getHandValue() > dealer.getHandValue()) {
            System.out.printf("HAND %s: WIN! Your %d beats dealer's %d\nYou win $%d!\n",
                    handCount, hand.getHandValue(), dealer.getHandValue(), (player.getWager() / 2));
            player.addBalance(player.getWager() / 2);
        } else if (hand.getHandValue() < dealer.getHandValue()) {
            System.out.printf("HAND %s: LOSS! Dealer's %d beats your %d\nYou lose $%d!\n",
                    handCount, dealer.getHandValue(), hand.getHandValue(), (player.getWager() / 2));
            player.minusBalance(player.getWager() / 2);
        } else {
            System.out.printf("Hand %s: PUSH! Dealer's %d equals your %d\nIt's a push!\n",
                    handCount, dealer.getHandValue(), hand.getHandValue());
        }
        if (handCount.equals("TWO")) {
            System.out.println();
        }
        delay(400);
    }

    // Method to reset game after each hand is completed.
    public void resetGame() {
        player.resetAll();
        dealer.resetAll();
        split.resetAll();
        deckOfCards.clearDeck();
        playerHandString.setLength(0);
    }

    // Separating some formatting logic.
    public void appendPlayerHand() {
        if (player.hasDoubledDown() && player.getHitIteration() == 0) {
            System.out.println("Hand value: " + player.getHandValue());
            player.plusHitIteration();
        } else {
            System.out.println("Hand value: " + player.getHandValue() + "\n");
        }
    }

    public void printChecking() {
        String checking = "Checking";
        for (int i = 0; i < checking.length(); i++) {
            System.out.print(checking.charAt(i));
            delay(100);
        }
    }

    public void printDealing() {
        String checking = "Dealing";
        for (int i = 0; i < checking.length(); i++) {
            System.out.print(checking.charAt(i));
            delay(100);
        }
    }

    public void printLoading() {
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            delay(100);
        }
    }

    public void printPeekMessage() {
        delay(500);
        System.out.println("Dealer hand is showing an ace or ten-card.");
        delay(500);
        System.out.println("Dealer must peek at hidden card to check blackjack.");
        delay(500);
        printChecking();
        printLoading();
        System.out.println("\n");
    }

    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
