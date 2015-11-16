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
		initPlayers();
	}

	/**
	 * Constructor 2: given number of players
	 * @param playerNum how many players
	 */
	public Var1(int playerNum) {
		this.playerCount = playerNum;
		initDeck();
		initPlayers();
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
