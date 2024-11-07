import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public ArrayList<Player> players = new ArrayList<>();
    public Deck deck = new Deck();
    public Player activePlayer;

    Scanner input = new Scanner(System.in);

    public Game() {
        int numPlayers = initGame();

        for (int i = 1; i <= numPlayers; i++)
            players.add(new Player(i));

        activePlayer = players.get((int)(Math.random() * numPlayers - 1));
        System.out.println("\nPlayer " + activePlayer.getPlayerNumber() + " will go first.\n");

        deck.shuffleDeck();
        dealCards(numPlayers);
    }

    public int initGame() {
        int numPlayers = 0;

        System.out.println("\nLET'S PLAY GO FISH!\n");
        do {
            try {
                System.out.print("How many players in the game? (2-5): ");
                numPlayers = input.nextInt();

                if (numPlayers < 2 || numPlayers > 5)
                    System.out.println("Please follow instructions.\n");
            }
            catch (Exception ex) {
                System.out.println("Please use a number.\n");
                input.nextLine();
            }
        }
        while (numPlayers < 2 || numPlayers > 5);
        return numPlayers;
    }

    public void play() {
        do {
            playerTurn();
            nextPlayersTurn();
        }
        while (getTotalBooks() < 13 && getNumberOfPlayers() > 1);
        Player winner = getGameWinner();
        System.out.println("PLAYER " + winner.getPlayerNumber() + " WINS!");
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public Player getGameWinner() {
        Player winner = players.getFirst();
        if (getNumberOfPlayers() > 1) {
            int maxBooks = 0;
            for (Player player: players) {
                if (player.getNumberOfBooks() > maxBooks) {
                    winner = player;
                    maxBooks = player.getNumberOfBooks();
                }
            }
        }
        return winner;
    }

    public int strRankToIntRank(String rank) {
        return switch(rank) {
            case "2" -> 0;
            case "3" -> 1;
            case "4" -> 2;
            case "5" -> 3;
            case "6" -> 4;
            case "7" -> 5;
            case "8" -> 6;
            case "9" -> 7;
            case "10" -> 8;
            case "J" -> 9;
            case "Q" -> 10;
            case "K" -> 11;
            case "A" -> 12;
            default -> 13;
        };
    }

    public void nextPlayersTurn() {
        if (activePlayer.getPlayerNumber() >= players.size())
            activePlayer = players.getFirst();
        else
            activePlayer = players.get(activePlayer.getPlayerNumber());
    }

    public void removePlayer() {
        System.out.println("Player " + activePlayer.getPlayerNumber() + " can not draw, and is removed from the game.\n");
        players.remove(activePlayer);
    }

    public void dealCards(int numPlayers) {
        if (numPlayers < 3) {
            for (int i = 0; i < 7; i++) {
                for (Player player: players) {
                    player.addCardsToHand(deck.drawCards(1));
                }
            }
        }
        else {
            for (int i = 0; i < 5; i++) {
                for (Player player: players) {
                    player.addCardsToHand(deck.drawCards(1));
                }
            }
        }
    }

    public String getPlayerHandCounts() {
        StringBuilder playerHandCounts = new StringBuilder("          Card Count | Books\n");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (i != activePlayer.getPlayerNumber() - 1)
                playerHandCounts.append(player.abbreviatedHand()).append("\n");
        }
        return playerHandCounts.toString();
    }

    public int getTotalBooks() {
        int totalBooks = 0;
        for (Player player: players)
            totalBooks += player.getNumberOfBooks();
        return totalBooks;
    }

    public void playerTurn() {
        Player currentPlayer = activePlayer;
        boolean successfulFish;

        Player targetPlayer;
        int targetRank;

        currentPlayer.checkForBooks();
        currentPlayer.sortHand();
        do {
            successfulFish = false;
            System.out.println(playerStats(currentPlayer));
            targetPlayer = chooseTargetPlayer();
            targetRank = chooseTargetRank();
            if (checkTargetPlayerHand(targetPlayer, targetRank)) {
                moveCardsFromTargetToPlayer(currentPlayer, targetPlayer, targetRank);
                currentPlayer.checkForBooks();
                successfulFish = true;
            }
            else {
                System.out.println("\nGO FISH!\n");
                if (deck.getNumberOfCards() == 0) {
                    removePlayer();
                }
                else {
                    currentPlayer.addCardsToHand(deck.drawCards(1));
                }
            }
        }
        while (successfulFish && getTotalBooks() < 13);
    }

    public String playerStats(Player currentPlayer) {
        return currentPlayer + "\n" + "Cards in Deck: " +
                deck.getNumberOfCards() + "\n\n" + getPlayerHandCounts();
    }

    public Player chooseTargetPlayer() {
        int targetPlayer = 0;
        boolean validTarget = false;

        do {
            System.out.print("Which player will you try to take cards from?: ");
            try {
                targetPlayer = input.nextInt();
                for (Player player: players) {
                    if (player.getPlayerNumber() == targetPlayer) {
                        validTarget = true;
                        break;
                    }
                }
                if (activePlayer.getPlayerNumber() == targetPlayer) {
                    System.out.println("You can not target yourself.\n");
                    validTarget = false;
                }
                else if (!validTarget) {
                    System.out.println("That player number is not in the game.\n");
                }
            }
            catch (Exception ex) {
                System.out.println("Please use a number.\n");
                input.nextLine();
            }
        }
        while (!validTarget);
        return players.get(targetPlayer - 1);
    }

    public int chooseTargetRank() {
        int targetRank;
        boolean validTarget = false;

        do {
            System.out.print("Which rank will you ask for?: ");
            targetRank = strRankToIntRank(input.next());
            if (targetRank < 13)
                validTarget = true;
            else
                System.out.println("That is not a valid rank.\n");
        }
        while (!validTarget);
        return targetRank;
    }

    public boolean checkTargetPlayerHand(Player targetPlayer, int rank) {
        return targetPlayer.cardsOfRankInHand(rank) > 0;
    }

    public void moveCardsFromTargetToPlayer(Player currentPlayer, Player targetPlayer, int rank) {
        System.out.println("\nPlayer " + targetPlayer.getPlayerNumber() + " has " + targetPlayer.cardsOfRankInHand(rank) + " of those.\n");
        Card[] takenCards = targetPlayer.removeCardsFromHand(rank);
        currentPlayer.addCardsToHand(takenCards);
    }
}
