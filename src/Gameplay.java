import java.lang.StringBuilder;
public class Gameplay {

    private final Deck deckOfCards;
    private final Player player;
    private final Player dealer;
    private final Player split;
    private final StringBuilder playerHandString;
    private int playerBalance, playerWager, updatedWager;


    public Gameplay() {
        this.player = new Player();
        this.dealer = new Player();
        this.split = new Player();
        this.deckOfCards = new Deck();
        this.playerBalance = 500;
        this.playerHandString = new StringBuilder();
    }

    public void setWager(int wager) {
        this.playerWager = wager;
    }

    public int getWager() {
        return this.playerWager;
    }

    public void doubleWager() {
        this.playerWager = this.playerWager * 2;
        player.doubledDown();
    }

    public int getBalance() {
        return this.playerBalance;
    }

    // Creates deck and shuffles cards. Deals two cards each to player and dealer.
    public void dealInitialCards() throws Exception {
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

    // Method displays player's cards. Uses timer to slow card presentation.
    public void showPlayersCards() throws Exception {
        player.adjustForAces();
        System.out.println("\nYOUR CARDS:");
        if (playerHandString.length() != 0) {
            System.out.println(playerHandString);
        }
        for (Card hand : player.getHand()) {
            Thread.sleep(700);
            if (playerHandString.length() == 0) {
                System.out.println(hand);
                playerHandString.append(hand);
            } else {
                System.out.println(hand);
                playerHandString.append("\n").append(hand);
            }
        }
        Thread.sleep(700);
        appendPlayerHand();
    }

    // Adds new card to player's hand.
    public void hitMe() throws Exception {
        player.clearHand();
        Card newCard = deckOfCards.getCard();
        player.addCard(newCard);
        player.adjustForAces();
        showPlayersCards();
    }

    // Checks if player's initial cards constitute blackjack.
    public boolean checkBlackjack() throws Exception {
        if (player.getHandValue() == 21) {
            playerBalance += (int) (playerWager * 1.5);
            System.out.println("BLACKJACK! YOU WIN $" + ((int) (playerWager * 1.5)) + "!\n");
            resetGame();
            return true;
        }
        showDealersUpCard();
        return false;
    }

    // Checks if player or dealer has busted.
    // int parameter represents different hands (1 for player, 2 for dealer)
    public boolean checkIfBusted(int player) {
        if (player == 1) {
            if (this.player.getHandValue() > 21) {
                if (this.player.hasDoubledDown()) {
                    System.out.print("\n");
                }
                System.out.println("YOU BUSTED! PLAY AGAIN?\n");
                playerBalance -= playerWager;
                resetGame();
                return true;
            }
            return false;
        }
        if (player == 2) {
            if (this.player.hasSplit() && dealer.getHandValue() > 21) {
                System.out.printf("\nDealer busts with %d! \nYOU WIN $%d!\n\n",
                        dealer.getHandValue(), updatedWager);
                playerBalance += updatedWager;
                dealer.clearHandValue();
                return true;
            } else if (dealer.getHandValue() > 21) {
                System.out.printf("\nDealer busts with %d! \nYOU WIN $%d!\n\n",
                        dealer.getHandValue(), playerWager);
                playerBalance += playerWager;
                resetGame();
                return true;
            }
        }
        return false;
    }

    // Method display dealer's face-up card. Uses timer to slow card presentation.
    public void showDealersUpCard() throws Exception {
        System.out.println("DEALER'S CARDS:");
        for (Card hand : dealer.getHand()) {
            Thread.sleep(700);
            System.out.println(hand);
            Thread.sleep(700);
            System.out.println("(X) of XXXXXX");
            Thread.sleep(700);
            System.out.println("Card value: " + hand.getRank().getValue());
            Thread.sleep(700);
            break;
        }
        System.out.println();
    }

    // Method that begins dealer's final act of revealing cards.
    public void dealerReveals() throws Exception {
            dealer.adjustForAces();
            System.out.println("\nDEALER'S HAND:");
            for (Card card : dealer.getHand()) {
                Thread.sleep(700);
                System.out.println(card);
            }
            Thread.sleep(700);
            System.out.println("Hand value: " + dealer.getHandValue() + "\n");
            Thread.sleep(700);
            if (!dealerHitsOrBusts() && !player.hasSplit())
                determineWinner();
    }

    // Instructs dealer to draw cards until 17 is reached or dealer busts.
    // Returns TRUE if dealer busts.
    public boolean dealerHitsOrBusts() throws Exception {
        while (dealer.getHandValue() < 17) {
            Card newCard = deckOfCards.getCard();
            dealer.addCard(newCard);
            dealer.adjustForAces();
            System.out.println("DEALER'S NEW CARD:");
            Thread.sleep(700);
            System.out.println(newCard);
            Thread.sleep(700);
            System.out.println("Hand value: " + dealer.getHandValue() + "\n");
            Thread.sleep(700);
            if (checkIfBusted(2)) {
                dealer.clearHandValue();
                return true;
            }
        }
        return false;
    }

    // Checks if dealer's initial cards constitute blackjack. Ends game if true.
    public boolean dealerPeek() throws Exception {
        if (dealer.getHand().get(0).getRank() == Rank.ACE ||
                dealer.getHand().get(0).getRank().getValue() == 10) {
            printPeekMessage();
            if (dealer.getHandValue() == 21) {
                System.out.println("DEALER'S HAND:");
                for (Card card : dealer.getHand()) {
                    Thread.sleep(500);
                    System.out.println(card);
                }
                Thread.sleep(500);
                System.out.println("Hand value: " + dealer.getHandValue() + "\n");
                Thread.sleep(500);
                System.out.println("Sorry! Dealer has blackjack! Try again!\n");
                playerBalance -= playerWager;
                resetGame();
                return true;
            } else {
                System.out.println("Dealer does not have blackjack.\n");
            }
        }
        return false;
    }

    // If neither player nor dealer busts, this method evaluates winner based on value of hands.
    public void determineWinner() throws Exception {
        if (player.getHandValue() > dealer.getHandValue()) {
            System.out.printf("\nYour %d beats dealer's %d\nYOU WIN $%d! PLAY AGAIN?\n\n",
                    player.getHandValue(), dealer.getHandValue(), playerWager);
            playerBalance += playerWager;
            Thread.sleep(1000);
            resetGame();
        } else if (dealer.getHandValue() > player.getHandValue()) {
            System.out.printf("\nDealer's %d beats your %d\nYOU LOSE $%d! PLAY AGAIN?\n\n",
                    dealer.getHandValue(), player.getHandValue(), playerWager);
            Thread.sleep(1000);
            playerBalance -= playerWager;
            resetGame();
        } else {
            System.out.printf("\nDealer's %d equals your %d\nIT'S A PUSH! PLAY AGAIN?\n\n",
                    dealer.getHandValue(), player.getHandValue());
            Thread.sleep(1000);
            playerWager = 0;
            resetGame();
        }
    }

    // Following methods handle logic of splitting cards into two separate hands and bets.
    // splitHand returns 21 to UI if player achieves blackjack after hand is completed.
    public int splitHand(int hand) throws Exception {
        if (hand == 1) {
            player.playerSplits();
            doubleWager();
            updatedWager = playerWager;
            split.addCard((player.getHand().remove(1)));
            split.addCard(deckOfCards.getCard());
            player.addCard(deckOfCards.getCard());
            printDealing();
            printLoading();
            System.out.println();
            if (handleSplit(player, "ONE") == 21) {
                return 21;
            }
        }
        if (hand == 2) {
            if (handleSplit(split, "TWO") == 21) {
                return 21;
            }
        }
        if (hand == 3) {
            dealerReveals();
            // If dealer busted, their hand value would be reset.
            // If dealer hasn't busted, player hands will be evaluated.
            if (dealer.getHandValue() > 0) {
                evaluateSplit(player, "ONE");
                evaluateSplit(split, "TWO");
            }
            resetGame();
            Thread.sleep(500);
        }
        return -1;
    }

    // Prints cards of each split hand. Returns 21 if blackjack is achieved.
    public int handleSplit(Player hand, String count) throws Exception {
        hand.clearHandValue();
        Thread.sleep(500);
        if (count.equals("ONE")) {
            System.out.println();
        }
        System.out.println("HAND " + count + ":");
        for (Card card : hand.getHand()) {
            Thread.sleep(500);
            hand.addHandValue(card.getRank().getValue());
            System.out.println(card);
        }
        Thread.sleep(500);
        System.out.println("Hand value: " + hand.getHandValue() + "\n");
        if (hand.getHandValue() == 21) {
            System.out.printf("BLACKJACK! You win $%d! \n\n", (int) ((playerWager / 2) * 1.5));
            updatedWager += (int) ((playerWager / 2) * 1.5);
            return 21;
        }
        return -1;
    }

    // Separate helper method for hitting on split hands.
    // Takes the hand number as a parameter (1 for first hand, 2 for second hand)
    // Declares subtractBust variable that subtracts value of player's original bet if hand busts.
    public int hitOnSplit(int hand) throws Exception {
        int subtractBust = (playerWager / 2);
        if (hand == 1) {
            Card newCard = deckOfCards.getCard();
            player.addCard(newCard);
            player.adjustForAces();
            Thread.sleep(300);
            System.out.println("NEW CARD:");
            Thread.sleep(700);
            System.out.println(newCard);
            Thread.sleep(700);
            System.out.println("New hand value: " + player.getHandValue() + "\n");
            Thread.sleep(700);
            if (player.getHandValue() > 21) {
                System.out.println("BUST! This hand went over 21.\n");
                updatedWager = updatedWager - subtractBust;
                playerBalance -= subtractBust;
                player.clearHandValue();
                return -1;
            }
        }
        if (hand == 2) {
            Card newCard = deckOfCards.getCard();
            split.addCard(newCard);
            split.adjustForAces();
            Thread.sleep(300);
            System.out.println("NEW CARD:");
            Thread.sleep(700);
            System.out.println(newCard);
            Thread.sleep(700);
            System.out.println("New hand value: " + split.getHandValue() + "\n");
            Thread.sleep(700);
            if (split.getHandValue() > 21) {
                System.out.println("BUST! This hand went over 21.");
                updatedWager = updatedWager - subtractBust;
                playerBalance -= subtractBust;
                split.clearHandValue();
                return -1;
            }
        }
        return 1;
    }

    // Outcome of split hands are determined here.
    public void evaluateSplit(Player hand, String handCount) throws Exception {
        Thread.sleep(200);
        if (dealerHitsOrBusts()) {
            return;
        }
        if (hand.getHand().get(0).getRank().getValue() +
                hand.getHand().get(1).getRank().getValue() == 21) {
            System.out.printf("HAND %s: BLACKJACK! Your hand equals 21 \nYou win $%d!\n",
                    handCount, (int) (playerWager * 0.75));
            playerBalance += (int) (playerWager * 0.75);
        } else if (hand.getHandValue() == 0) {
            System.out.printf("HAND %s: BUST! This hand went over 21 \nYou lose $%d!\n",
                    handCount, playerWager / 2);
        } else if (hand.getHandValue() > dealer.getHandValue()) {
            System.out.printf("HAND %s: WIN! Your %d beats dealer's %d\nYou win $%d!\n",
                    handCount, hand.getHandValue(), dealer.getHandValue(), (playerWager / 2));
            playerBalance += playerWager / 2;
        } else if (hand.getHandValue() < dealer.getHandValue()) {
            System.out.printf("HAND %s: LOSS! Dealer's %d beats your %d\nYou lose $%d!\n",
                    handCount, dealer.getHandValue(), hand.getHandValue(), (playerWager / 2));
            playerBalance -= playerWager / 2;
        } else {
            System.out.printf("Hand %s: PUSH! Dealer's %d equals your %d\nIt's a push!\n",
                    handCount, dealer.getHandValue(), hand.getHandValue());
        }
        if (handCount.equals("TWO")) {
            System.out.println();
        }
        Thread.sleep(400);
    }

    // Method to reset game after each hand is completed.
    public void resetGame() {
        player.resetAll();
        dealer.resetAll();
        split.resetAll();
        player.notDoubledDown();
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

    public void printChecking() throws Exception {
        String checking = "Checking";
        for (int i = 0; i < checking.length(); i++) {
            System.out.print(checking.charAt(i));
            Thread.sleep(100);
        }
    }

    public void printDealing() throws Exception {
        String checking = "Dealing";
        for (int i = 0; i < checking.length(); i++) {
            System.out.print(checking.charAt(i));
            Thread.sleep(100);
        }
    }

    public void printLoading() throws Exception {
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            Thread.sleep(300);
        }
    }

    public void printPeekMessage() throws Exception {
        Thread.sleep(500);
        System.out.println("Dealer hand is showing an ace or ten-card.");
        Thread.sleep(500);
        System.out.println("Dealer must peek at hidden card to check blackjack.");
        Thread.sleep(500);
        printChecking();
        printLoading();
        System.out.println("\n");
    }

}
