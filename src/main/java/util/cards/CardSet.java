package util.cards;

/**
 * Specifies which assortment of Ranks to use.
 * Useful if expanding to different games.
 */
public enum CardSet {
    MAU_MAU  (new CardRank[] {CardRank.SEVEN, CardRank.EIGHT, CardRank.NINE, CardRank.TEN, CardRank.JACK, CardRank.QUEEN, CardRank.KING, CardRank.ACE}),
    /*POKER   (CardRank.values())*/;

    /**
     * Ranks for the type of game
     */
    public final CardRank[] ranks;

    CardSet(CardRank[] values) {
        ranks = values;
    }
}