//class to represent a playing card
public class Card {
    //rank is stored as an int
    private final int rank;
    //suit is stored as a char
    private final char suit;

    //constructor must receive arguments for rank and suit
    public Card(int rank, char suit) {
        this.rank = rank;
        this.suit = suit;
    }

    //getter for rank
    public int getRank() {
        return rank;
    }

    //getter for a string representation of the Card
    public String toString() {
        return switch(rank) {
            case 0 -> "2" + suit;
            case 1 -> "3" + suit;
            case 2 -> "4" + suit;
            case 3 -> "5" + suit;
            case 4 -> "6" + suit;
            case 5 -> "7" + suit;
            case 6 -> "8" + suit;
            case 7 -> "9" + suit;
            case 8 -> "10" + suit;
            case 9 -> "J" + suit;
            case 10 -> "Q" + suit;
            case 11 -> "K" + suit;
            default -> "A" + suit;
        };
    }
}
