import java.lang.*;
import java.util.*;

/**
 * Variance 2: different player number.
 */
public class Var2 extends OldMaidGame {
	/**
	 * Constructor
	 */
	public Var2() {
		this.playerCount = 6;
		initDeck();
		initPlayers(false);
	}

	/**
	 * Constructor 2: given number of players
	 * @param playerNum how many players
	 * @param human set to true if 1 human player; false if all AI.
	 */
	public Var2(int playerNum, boolean human) {
		if(playerNum <= 0) // using default
			this.playerCount = 6;
		else
			this.playerCount = playerNum;
		initDeck();
		initPlayers(human);
	}

	/**
	 * overriding because we need two decks
	 */
	@Override
	public void initDeck() {
		this.deck = new Deck();
		Deck anotherDeck = new Deck();
		this.deck.addAll(anotherDeck.cards);
		this.deck.removeCard("C", "Q"); // remove a Club Q
	}
}
