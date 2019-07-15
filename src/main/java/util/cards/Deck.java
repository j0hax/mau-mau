package util.cards;

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
     * Instantiate an ordered deck of Mau-Mau cards
     */
    public Deck() {
        this(CardSet.MAU_MAU);
    }

    /**
     * Instantiate an ordered deck with a specified set of ranks
     * @param type the collection of cards to be used
     */
    public Deck(CardSet type) {
        for (CardSuite s : CardSuite.values()) {
            for (CardRank r : type.ranks) {
                cards.add(new Card(s, r));
            }
        }
    }

    /**
     * Deals a random card.
     * The card will be removed from the deck.
     * @return the dealt card.
     */
    public Card deal() {
        Random randGen = new Random();
        int randIndex = randGen.nextInt(getSize());
        return cards.remove(randIndex);

    }

    /**
     * Deals a number of random cards
     * which will be removed from the deck
     * @param count the number of cards
     * @return array containing the dealt cards
     */
    public Card[] deal(int count) throws Exception {
        if (getSize() <= 0) throw new Exception("Deck empty");
        Card[] dealt = new Card[count];

        for (int i = 0; i < count; i++) {
            dealt[i] = deal();
        }

        return dealt;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Card c : cards) {
            result.append(c.toString()).append("\n");
        }

        return result.toString();
    }
}
