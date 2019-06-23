package util;

public class Card implements Comparable {

    private final CardSuite suite;
    private final CardRank rank;

    /**
     * Instantiate a playing card
     * @param suite suite of the card
     * @param rank rank of the card
     */
    public Card(CardSuite suite, CardRank rank) {
        this.suite = suite;
        this.rank = rank;
    }

    /**
     * Returns the suite of card
     * @return enum suite
     */
    public CardSuite getSuite() {
        return suite;
    }

    /**
     * Returns the rank of the card
     * @return enum rank
     */
    public CardRank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suite + "S";
    }

    @Override
    public int compareTo(Object o) {
        Card c = (Card)o;
        // TODO: implement rules of Mau-Mau
        return 0;
    }
}