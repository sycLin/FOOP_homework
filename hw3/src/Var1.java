import java.lang.*;
import java.util.*;


/**
 * Variance 1: different deck content.
 */
public class Var1 extends OldMaidGame {
	/**
	 * Constructor
	 */
	public Var1() {
		this.playerCount = 4;
		initDeck();
		initPlayers(false);
	}

	/**
	 * Constructor 2: given number of players
	 * @param playerNum how many players
	 * @param human set to true if 1 human player; false if all AI.
	 */
	public Var1(int playerNum, boolean human) {
		if(playerNum <= 0) // using default
			this.playerCount = 4;
		else
			this.playerCount = playerNum;
		initDeck();
		initPlayers(human);
	}

	/**
	 * overriding because different content of deck
	 */
	@Override
	public void initDeck() {
		this.deck = new Deck();
		this.deck.removeCard("C", "Q"); // remove Club Q
	}
}
