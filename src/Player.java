import java.util.*;

//class to represent a player in the game
public class Player {
    //the players hand is an ArrayList, to facilitate adding and removing Cards
    private final ArrayList<Card> hand = new ArrayList<>();
    //books are stored in an Array, with the length being 13, as that is the max books possible
    private final int[] books = new int[13];
    //the number of books taken, to facilitate methods that need to iterate over the array of books
    private int numBooks = 0;
    //the player number
    private final int playerNumber;
    //comparator object for sorting by rank
    private final Comparator<Card> by_rank = Comparator.comparing(Card::getRank);

    //default constructor, taking the player number as an argument
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    //getter for the player number
    public int getPlayerNumber() {
        return playerNumber;
    }

    //getter to return the number of Cards in the Player's hand
    public int getSizeOfHand() {
        return hand.size();
    }

    //getter to return the number of books a Player has taken
    public int getNumberOfBooks() {
        return numBooks;
    }

    //getter to return a String representation of the Player's current hand, formed from the String representation of the Cards
    public String getPlayerHand() {
        String[] handArray = new String[hand.size()];
        for (int i = 0; i < hand.size(); i++)
            handArray[i] = hand.get(i).toString();
        return String.join(" ", handArray);
    }

    public String intRankToStrRank(int rank) {
        return switch(rank) {
            case 0 -> "2";
            case 1 -> "3";
            case 2 -> "4";
            case 3 -> "5";
            case 4 -> "6";
            case 5 -> "7";
            case 6 -> "8";
            case 7 -> "9";
            case 8 -> "10";
            case 9 -> "J";
            case 10 -> "Q";
            case 11 -> "K";
            default -> "A";
        };
    }

    //getter to return a String representation of the books the Player has taken
    public String showBooks() {
        String[] booksArray = new String[numBooks];
        for (int i = 0; i < getNumberOfBooks(); i++) {
            booksArray[i] = intRankToStrRank(books[i]);
        }
        if (booksArray.length > 0)
            return String.join(" ", booksArray);
        else
            return "None";
    }

    //getter to calculate how many Cards of a given rank are in the Player's hand
    public int cardsOfRankInHand(int rank) {
        int numCards = 0;
        for (Card card: hand) {
            if (card.getRank() == rank) {
                numCards++;
            }
        }
        return numCards;
    }

    //method to add Cards to the Player's hand, from an Array of Cards
    public void addCardsToHand(Card[] addedCards) {
        Collections.addAll(hand, addedCards);
    }

    //method to remove Cards from the Player's hand, returned as an Array of Cards
    public Card[] removeCardsFromHand(int rank) {
        int numCards = cardsOfRankInHand(rank);
        Card[] removedCards = new Card[numCards];
        int removedCardsIdx = 0;
        for (int i = hand.size() - 1; i >= 0; i--) {
            if (hand.get(i).getRank() == rank) {
                removedCards[removedCardsIdx] = hand.remove(i);
                removedCardsIdx++;
            }
        }
        return removedCards;
    }

    //method to inspect the Player's hand for books. If found, they are removed from the Player's hand, and that rank is added to their Array of books
    public void checkForBooks() {
        HashMap<Integer, Integer> rankCounts = new HashMap<>();
        for (Card card: hand) {
            if (rankCounts.containsKey(card.getRank())) {
                int count = rankCounts.get(card.getRank());
                rankCounts.put(card.getRank(), count + 1);
            }
            else {
                rankCounts.put(card.getRank(), 1);
            }
        }

        for (Map.Entry<Integer, Integer> count: rankCounts.entrySet()) {
            if (count.getValue() == 4) {
                System.out.println("Book of rank " + intRankToStrRank(count.getKey()) + " found.\n");
                addBook(count.getKey());
                removeCardsFromHand(count.getKey());
            }
        }
    }

    //method to add a book of a specified rank to the Player's Array of books
    public void addBook(int rank) {
        books[numBooks] = rank;
        numBooks++;
        sortBooks(books, 0);
    }

    //method to sort the Array of books to make it easier to parse by a person
    public void sortBooks(int[] toSort, int low) {
        if (low < numBooks - 1) {
            int indexOfMin = low;
            int min = toSort[low];
            for (int i = low; i < numBooks; i++) {
                if (toSort[i] < min) {
                    min = toSort[i];
                    indexOfMin = i;
                }
            }
            toSort[indexOfMin] = toSort[low];
            toSort[low] = min;
            sortBooks(toSort, low + 1);
        }
    }

    public void sortHand() {
        hand.sort(by_rank);
    }

    public String fullHand() {
        return "Books: " + showBooks() + "\nHand: " + getPlayerHand();
    }

    public String abbreviatedHand() {
        return String.format("Player %d: %10d | %s", playerNumber, getSizeOfHand(), showBooks());
    }

    //getter for a String representation of the Player's current hand.
    public String toString() {
        return "Player " + playerNumber + "\n" + fullHand();
    }
}
