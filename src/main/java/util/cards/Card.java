package util.cards;

import javafx.scene.image.Image;

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
        return rank + " of " + suite;
    }

    /**
     * Compares two cards according to Mau-Mau rules.
     * IMPORTANT: Special cards such as Jack, 7 & 8 should be checked before externally.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Card c = (Card)o;

        if (c.getRank() == getRank()) {
            return 0;
        }

        if (c.getSuite() == getSuite()) {
            return 0;
        }

        return getRank().compareTo(c.getRank());
    }

    public Image getImage() {
        String rank;
        String suite;

        switch (getRank()) {
            case TWO:
                rank = "2";
                break;

            case THREE:
                rank = "3";
                break;

            case FOUR:
                rank = "4";
                break;

            case FIVE:
                rank = "5";
                break;

            case SIX:
                rank = "6";
                break;

            case SEVEN:
                rank = "7";
                break;

            case EIGHT:
                rank = "8";
                break;

            case NINE:
                rank = "9";
                break;

            case TEN:
                rank = "10";
                break;

            default:
                rank = getRank().name().toLowerCase();
        }

        suite = getSuite().name().toLowerCase();
        String fileName = "/client/cards/PNG-cards-1.3/" + rank + "_of_" + suite +
                ".png";
        return new Image(this.getClass().getResourceAsStream(fileName), 125, 181, true,
                true);
    }
}