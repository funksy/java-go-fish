import java.util.ArrayList;

//class to represent a deck of cards
public class Deck {
    //deck is stored as an ArrayList of cards to enable adding/removing cards
    private final ArrayList<Card> deck = new ArrayList<>();

    //default constructor adds 52 Card objects, with the ranks and suits to represent a standard deck
    public Deck() {
        for (int i = 0; i < 4; i++) {
            char suit = switch(i) {
                case 0 -> 'C';
                case 1 -> 'D';
                case 2 -> 'H';
                default -> 'S';
            };
            for (int rank = 0; rank < 13; rank++) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    //getter for the number of cards currently in the deck
    public int getNumberOfCards() {
        return deck.size();
    }

    //method to shuffle the cards in the deck, using Fisher-Yates
    public void shuffleDeck() {
        for (int i = deck.size() - 1; i > 0; i--) {
            int index = (int)(Math.random() * i);
            Card temp = deck.get(index);
            deck.set(index, deck.get(i));
            deck.set(i, temp);
        }
    }

    //method to return an Array of Cards of the specified length, removing them from the beginning of the deck
    public Card[] drawCards(int num) {
        Card[] drawnCards = new Card[num];
        for (int i = 0; i < num; i++) {
            drawnCards[i] = deck.removeFirst();
        }
        return drawnCards;
    }

    //method for returning a string representation of the deck, formed from the string representation of the Cards
    public String toString() {
        String[] res = new String[deck.size()];
        for (int i = 0; i < deck.size(); i++)
            res[i] = deck.get(i).toString();
        return String.join(" ", res);
    }
}
