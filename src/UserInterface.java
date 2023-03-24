import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final Gameplay gameplay;
    private boolean eligibleToDoubleDown;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.gameplay = new Gameplay();
    }

    public void startGame() {
        while (true) {
            try {
                eligibleToDoubleDown = true;
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

    // Takes bet as user input. Passes value to setWager() method in Gameplay class.
    public void placeBet() {
        while (true) {
            System.out.println("Place your bet:");
            try {
                int playerBet = Integer.parseInt(scanner.nextLine());
                if (playerBet == 0) {
                    startGame();
                } else if (playerBet > gameplay.getBalance()) {
                    System.out.println("This bet exceeds your bankroll. " +
                            "Bet lower or enter 0.");
                } else {
                    gameplay.setWager(playerBet);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid bet. Try again");
            }
        }
    }

    // Method takes user input whether to hit, stay, double down, or split.
    // Returns TRUE if new card busts hand.
    public boolean playerOptions() throws Exception {
        while (true) {
            System.out.println(appendOptions());
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Hit")) {
                gameplay.hitMe();
                eligibleToDoubleDown = false;
                if (gameplay.checkIfBusted(1)) {
                    return true;
                }
            }
            if (eligibleToDoubleDown) {
                if (input.equalsIgnoreCase("Double")) {
                    if (gameplay.getBalance() < (gameplay.getWager() * 2)) {
                        printNotEnough();
                        continue;
                    }
                    printDoubleInfo();
                    gameplay.doubleWager();
                    gameplay.hitMe();
                    return gameplay.checkIfBusted(1);
                }
                if (input.equalsIgnoreCase("Split")) {
                    if (gameplay.getBalance() < (gameplay.getWager() * 2)) {
                        printNotEnough();
                        continue;
                    }
                    printSplitInfo();
                    if (gameplay.splitHand(1) != 21) {
                        splitSubMenu(1);
                    }
                    if (gameplay.splitHand(2) != 21){
                        splitSubMenu(2);
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

    public void splitSubMenu(int hand) throws Exception {
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
        String line1 = "---21 BLACKJACK---\n  Balance: $" + gameplay.getBalance();
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
        if (eligibleToDoubleDown) {
            options.append(line1);
            options.append(line4);
        }
        return options.toString();
    }
    public String appendSplit() {
        StringBuilder options = new StringBuilder();
        String line1 = "Type HIT to draw another card.\n";
        String line2 = "Type STAY to stick with your hand.\n";
        options.append(line1).append(line2);
        return options.toString();
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
