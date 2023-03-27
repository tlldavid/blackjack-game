import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final Gameplay gameplay;
    private final Player player;
    private final Player split;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.player = new Player();
        this.split = new Player();
        Dealer dealer = new Dealer();
        this.gameplay = new Gameplay(player, dealer, split);
    }

    /* Start method displays initial menu, takes user input on whether
    to deal or exit game. If player chooses to deal, a bet is placed and a
    sequence of Gameplay methods are subsequently called. */
    public void startGame() {
        while (true) {
            try {
                System.out.println(appendHeader());
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                if (input.equalsIgnoreCase("deal")) {
                    placeBet();
                    gameplay.dealInitialCards();
                    if (gameplay.checkBlackjack() || gameplay.dealerPeek() ||
                            playerOptions()) {
                        continue;
                    }
                    gameplay.dealerReveals();
                }
            } catch (Exception e) {
                System.out.println("Game error: " + e);
            }
        }
    }

    // Takes bet as user input. Passes value to setWager() method in Player class.
    public void placeBet() {
        while (true) {
            System.out.println("Place your bet:");
            try {
                int playerBet = Integer.parseInt(scanner.nextLine());
                if (playerBet == 0) {
                    startGame();
                } else if (playerBet > player.getBalance()) {
                    System.out.println("This bet exceeds your bankroll. " +
                            "Bet lower or enter 0.");
                } else {
                    player.setWager(playerBet);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid bet. Try again");
            }
        }
    }

    /* After cards are revealed, this method takes user input on whether to
     hit, stay, double down, or split. Returns TRUE if new card busts hand. */
    public boolean playerOptions() throws Exception {
        while (true) {
            System.out.println(appendOptions());
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Hit")) {
                gameplay.hitMe();
                player.notEligibleToDoubleDown();
                if (gameplay.checkIfBusted(this.player)) {
                    return true;
                }
            }
            if (player.isEligibleToDoubleDown()) {
                if (input.equalsIgnoreCase("Double")) {
                    if (player.getBalance() < (player.getWager() * 2)) {
                        printNotEnough();
                        continue;
                    }
                    printDoubleInfo();
                    player.doubleWager();
                    gameplay.hitMe();
                    return gameplay.checkIfBusted(this.player);
                }
                if (input.equalsIgnoreCase("Split")) {
                    if (player.getBalance() < (player.getWager() * 2)) {
                        printNotEnough();
                        continue;
                    }
                    printSplitInfo();
                    if (gameplay.splitHand(1) != 21) {
                        splitSubMenu(player);
                    }
                    if (gameplay.splitHand(2) != 21){
                        splitSubMenu(split);
                    }
                    gameplay.splitHand(3);
                    return true;
                }
            }
            if (input.equalsIgnoreCase("Stay")) {
                return false;
            }
        }
    }

    public void splitSubMenu(Player hand)  {
        while (true) {
            System.out.println(appendSplit());
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("hit")) {
                if (gameplay.hitOnSplit(hand) == -1) {
                    break;
                }
            }
            if (input.equalsIgnoreCase("stay")) {
                return;
            }
        }
    }
    public String appendHeader() {
        StringBuilder header = new StringBuilder();
        String line1 = "---21 BLACKJACK---\n  Balance: $" + player.getBalance();
        String line2 = "\n\n1. Type DEAL to play.\n2. Type EXIT to quit.";
        header.append(line1).append(line2);
        return header.toString();
    }
    public String appendOptions() {
        StringBuilder options = new StringBuilder();
        String line1 = "Type DOUBLE to double down on your bet.\n";
        String line2 = "Type HIT to draw another card.\n";
        String line3 = "Type STAY to stick with your hand.\n";
        String line4 = "Type SPLIT to split your cards.\n";
        options.append(line2).append(line3);
        if (player.isEligibleToDoubleDown()) {
            options.append(line1);
            options.append(line4);
        }
        return options.toString();
    }
    public String appendSplit() {
        String line1 = "Type HIT to draw another card.\n";
        String line2 = "Type STAY to stick with your hand.\n";
        return line1 + line2;
    }
    public void printSplitInfo() throws Exception {
        System.out.println("\nYour cards will be split into two separate hands.");
        Thread.sleep(1000);
        System.out.println();
    }
    public void printDoubleInfo() throws Exception {
        System.out.println("\nYou have doubled down. You will receive one additional card.");
        Thread.sleep(1000);
    }
    public void printNotEnough(){
        System.out.println("You don't have enough money for this bet.\n");
    }
}
