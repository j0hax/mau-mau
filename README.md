# Cardgame Gruppenprojekt Prog II

Tools: Java8, JavaFx, Gradle

*By Anwar Ali, Cedric Fauth, Johannes Arnold*

## Programming Roadmap

* Expand README
* Design User Interface
    * Use proper graphics and animations for the game
    * Implement chat function
* Write basic server
* Implement game logic

The server should be multi-threaded, supporting not only multiple players but multiple games concurrently.
Clients connect to a lobby and wait for other players to join.

Proper object-oriented practices will be used, for example, `Player`, `Card`, and `GameLobby` classes will represent their real-life analogs.

The game should then be played according to the following rules according to [Wikipedia](https://en.wikipedia.org/wiki/Mau-Mau_%28card_game%29):


## Mau-Mau

* Players: 2-5

* Cards: 32

* Play: Clockwise

* Card Rank: A K Q J 10 9 8 7

### Rules

Before the start of the game, a player who is not the dealer cuts the deck 4 times. If they cut 1-3 significant cards, they are allowed to keep them if they want. However, if four cards where the cards are cut are found to be power cards, the deck needs to be reshuffled and the cut is repeated. The players are each dealt a hand of cards (usually 5 or 6). The rest are placed face down as the stock or stack. At the beginning of the game the topmost card is revealed and placed face up on the table then the players take it in turns to play their cards.

A card can only be played if it corresponds to the suit or value of the face-up card. E.g. if it is the 10 of spades, only another spade or another 10 can be played (but see below for Jacks). If a player is not able to do this, they draw one card from the stack; If they can play this card, they may do so, otherwise they keeps the drawn card and passes on their turn. When the drawing stack is empty, the playing stack (except for the topmost card) is shuffled and turned over to serve as new drawing stack.

The 7, 8, Jack and Ace of all suits are significant cards:

1. If a 7 is played, the next player has to draw two cards but may play. (A variant of the game allows the player facing the 7 to play another 7, in which case the player to his left must take 4 cards from the pack, unless he too has a 7, then 6, then 8.)

2. Any 8 forces the next player to miss his turn. (A variant of the game allows the player facing the 8 to play another 8, in which case the next player after them must play another 8 or miss a turn, etc.)

3. A Jack of any suit is the equivalent of a Joker and can be played on any card. The player who plays it then chooses a card suit. The next player then plays as if the Jack was of the chosen suit.

4. If an Ace is played, one other card **must** be played with it. If the player does not have another card, or cannot follow in suit or number, then the player must take a card from the pack. If your final card is an Ace, you cannot win on that turn.

5. When a player has only one card left, he must say “Mau” (even if it is an Ace); if that card is a Jack, he must say “Mau-Mau”. Failure means that the player must take a card.

6. If the game is scored, **and** the winning card is a Jack, then all points against the losers are doubled.
