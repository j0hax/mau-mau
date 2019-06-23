package util;

import java.util.LinkedList;
import java.util.Random;

/**
 * Simulates a deck of cards.
 */
public class Deck {

    /**
     * The deck of cards.
     */
    private LinkedList<Card> cards = new LinkedList<>();

    /**
     * Instantiate an ordered deck of 52 Cards
     */
    public Deck() {
        for (CardSuite s : CardSuite.values()) {
            for (CardRank r : CardRank.values()) {
                cards.add(new Card(s, r));
            }
        }
    }

    // TODO: Mau-Mau only uses certain cards from the deck, not all 52. Implement a constructor to only use those.

    /**
     * Deals a card at random.
     * The card will be removed from the deck.
     * @return the dealt card.
     */
    public Card deal() {
        Random randGen = new Random();
        int randIndex = randGen.nextInt(getSize());
        return cards.remove(randIndex);
    }

    /**
     * Number of cards remaining in the deck
     * @return size of deck
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Add a card to the deck
     * @param c the card to be added.
     */
    public void add(Card c) {
        cards.add(c);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Card c : cards) {
            result.append(c.toString()).append("\n");
        }

        return result.toString();
    }
}
