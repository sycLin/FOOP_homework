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
		initPlayers();
	}

	/**
	 * Constructor 2: given number of players
	 * @param playerNum how many players
	 */
	public Var2(int playerNum) {
		this.playerCount = playerNum;
		initDeck();
		initPlayers();
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
