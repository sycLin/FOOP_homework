import java.lang.*;
import java.util.*;

public class PlayGame {
	/**
	 * This is the main function
	 * @param argv the arguments, array of string.
	 */
	public static void main(String argv[]) {
		// choose what game to play
		System.out.println("Which game to play?");
		System.out.println("[0] Classic OldMaidGame");
		System.out.println("[1] Variant #1");
		System.out.println("[2] Variant #2");
		System.out.print("Your choice > ");
		Scanner scanner = new Scanner(System.in);
		int whichGame = Integer.parseInt(scanner.nextLine());
		// create the game
		switch(whichGame) {
			case 0:
				OldMaidGame letsPlay = new OldMaidGame();
				letsPlay.start();
				break;
			case 1:
				Var1 letsPlayVar1 = new Var1();
				letsPlayVar1.start();
				break;
			case 2:
				Var2 letsPlayVar2 = new Var2();
				letsPlayVar2.start();
				break;
		}
	}



}

/**
 * classic game of Old Maid Card Game, 
 * a base class for variants to extend.
 */
class OldMaidGame {
	public Deck deck;
	public Player[] players;
	int playerCount; // how many player should participate in that game

	/**
	 * Basic Constructor: no arguments given, default playerCount = 4.
	 */
	public OldMaidGame() {
		this.playerCount = 4;
		initDeck();
		initPlayers();
	}

	/**
	 * Another constructor: given number of players
	 * @param playerNum how many player (integer)
	 */
	public OldMaidGame(int playerNum) {
		this.playerCount = playerNum;
		initDeck();
		initPlayers();
	}

	/**
	 * initialize the deck (standard 52 + 2 jokers), welcome to override.
	 */
	public void initDeck() {
		this.deck = new Deck();
		this.deck.addCard("R", "0");
		this.deck.addCard("B", "0");
	}

	/**
	 * initialize the players according to this.playerCount.
	 */
	public void initPlayers() {
		this.players = new Player[this.playerCount];
		for(int i=0; i<this.playerCount; i++) {
			this.players[i] = new Player();
		}
	}

	/**
	 * Start the game
	 */
	public void start() {
		// step1: shuffle the deck
		this.deck.shuffle();
		// step2: deal the cards
		System.out.println("Deal cards");
		// step2-1: determine how many cards to give each of the players
		int[] numOfCards = new int[this.playerCount]; // store the number of cards each player gets
		int cardsLeft = this.deck.getSize(); // for calculation
		for(int i=0; i<this.playerCount; i++) {
			if(i == this.playerCount-1) { // the last player to give, then give him all.
				numOfCards[i] = cardsLeft;
				break;
			}
			numOfCards[i] = cardsLeft/(this.playerCount-i); // how many to give to player i
			cardsLeft = cardsLeft - numOfCards[i]; // how many left after giving to the player
		}
		// step2-2: do the dealing (distribution of cards)
		for(int i=0; i<this.playerCount; i++) {
			for(int j=0; j<numOfCards[i]; j++) {
				Card c = this.deck.get(0);
				players[i].addCard(c);
				this.deck.remove(0);
			}
		}
		// step2-3: output their initial hands
		for(int i=0; i<this.playerCount; i++) {
			System.out.println("Player" + i + ": " + players[i].getHandString());
		}
		// step3: initial drops of all players
		System.out.println("Drop cards");
		for(int i=0; i<this.playerCount; i++)
			players[i].drop();
		for(int i=0; i<this.playerCount; i++) {
			System.out.println("Player" + i + ": " + players[i].getHandString());
		}
		// step4: main while-loop; break when 1 player left
		System.out.println("Game start");
		int playerTo = -1; // the player who draws
		int playerFrom = 0; // the player to draw from
		while(true) {
			// check if game over, using this.isOver()
			if(this.isOver()) {
				System.out.println("Game Over");
				break;
			}
			// determine the _playerTo_ and _playerFrom_
			playerTo = playerFrom;
			while(!players[playerTo].active)
				playerTo = (playerTo + 1) % this.playerCount;
			playerFrom = (playerTo + 1) % this.playerCount;
			while(!players[playerFrom].active)
				playerFrom = (playerFrom + 1) % this.playerCount;
			// do the drawing
			Card c = players[playerTo].draw(players[playerFrom]);
			// output
			System.out.println("Player" + playerTo + " draws a card from Player" + playerFrom + " " + c);
			System.out.println("Player" + playerTo + ": " + players[playerTo].getHandString());
			System.out.println("Player" + playerFrom + ": " + players[playerFrom].getHandString());
		}
	}

	/**
	 * check if the game is completely over
	 * @return true if game is completely over; false otherwise.
	 */
	public boolean isOver() {
		int how_many_winners = 0;
		String output = "";
		for(int i=0; i<this.playerCount; i++) {
			if(players[i].active) {
				players[i].updateStatus();
				if(!players[i].active) {
					// this player is a new winner!
					if(how_many_winners == 0) {
						output = output + "Player" + i;
					} else {
						output = output + " and Player" + i;
					}
					how_many_winners += 1;
				}
			}
		}
		// just for output
		if(how_many_winners == 1)
			System.out.println(output + " wins");
		else if(how_many_winners == 2)
			System.out.println(output + " win");
		// check how many active players, to determine if the game is over
		int activePlayerCount = 0;
		for(int i=0; i<this.playerCount; i++)
			if(players[i].active)
				activePlayerCount += 1;
		if(activePlayerCount == 1) return true; // the game is over.
		return false; // the game is not yet over.
	}
}

/**
 * Represent a card
 */
class Card {

	private String suit;
	private String rank;

	/**
	 * constructor of a card
	 * @param s a "suit string": R, B, C, D, H, or S.
	 * @param r a "rank string": A, K, Q, J, 10, ..., 2.
	 */
	public Card(String s, String r) {
		this.suit = s;
		this.rank = r;
	}

	/**
	 * Overrides Object.toString()
	 * @return a string representation of a card
	 */
	@Override
	public String toString() {
		return (this.suit + this.rank);
	}

	/**
	 * Determine if smaller than a given card.
	 * @param c another card.
	 * @return true if smaller than c; returns false otherwise.
	 */
	public boolean smallerThan(Card c) {
		// compare the ranks first
		int myRank = this.getIntRank();
		int yourRank = c.getIntRank();
		if(myRank < yourRank) return true;
		if(myRank > yourRank) return false;
		// ranks are identical, compare suit now
		int mySuit = this.getIntSuit();
		int yourSuit = c.getIntSuit();
		if(mySuit < yourSuit) return true;
		if(mySuit > yourSuit) return false;
		// the 2 cards are identical...
		return false;
	}

	/**
	 * Determine if bigger than a given card.
	 * @param c another card.
	 * @return true is bigger than c; otherwise, return false.
	 */
	public boolean biggerThan(Card c) {
		// utilize smallerThan()
		boolean isSmaller = this.smallerThan(c);
		if(isSmaller) return false;
		if(this.getIntSuit() == c.getIntSuit()
			&& this.getIntRank() == c.getIntRank())
			return false;
		return true;
	}

	/**
	 * get an integer value of this.rank
	 * @return 14 for A, 13 for K, 12 for Q, 11 for J.
	 */
	public int getIntRank() {
		if(this.rank.equals("A")) return 14;
		if(this.rank.equals("K")) return 13;
		if(this.rank.equals("Q")) return 12;
		if(this.rank.equals("J")) return 11;
		// default
		return Integer.parseInt(rank);
	}

	/**
	 * get an integer value of this.suit
	 * @return  4 for S(pade), 3 for H(eart), 2 for D(iamond), and 1 for C(lub).
	 */
	public int getIntSuit() {
		if(this.suit.equals("C")) return 1;
		if(this.suit.equals("D")) return 2;
		if(this.suit.equals("H")) return 3;
		if(this.suit.equals("S")) return 4;
		return 0;
	}
}

/**
 * A deck consists of cards
 */
class Deck {
	public ArrayList<Card> cards;

	/**
	 * Constructor: no arguments given, then standard deck with 52 cards
	 */
	public Deck() {
		cards = new ArrayList<Card>();
		for(int i=0; i<52; i++) {
			String s;
			String r;
			// deal with suit
			if(i < 13) {
				s = "C";
			} else if(i < 26) {
				s = "D";
			} else if(i < 39) {
				s = "H";
			} else {
				s = "S";
			}
			// deal with rank
			if((i+1)%13 == 0)
				r = "K";
			else if((i+1)%13 == 12)
				r = "Q";
			else if((i+1)%13 == 11)
				r = "J";
			else if((i+1)%13 == 1)
				r = "A";
			else
				r = Integer.toString((i+1)%13);
			// create the card
			Card newCard = new Card(s, r);
			cards.add(newCard);
		}
	}

	/**
	 * get the size of the deck
	 * @return the size of the deck
	 */
	public int getSize() {
		return this.cards.size();
	}

	/**
	 * add cards into the deck
	 * @param s the suit of the card we'd like to add
	 * @param r the rank of the card we'd like to add
	 * @return true if added successfully; false, otherwise.
	 */
	public boolean addCard(String s, String r) {
		Card newCard = new Card(s, r);
		cards.add(newCard);
		return true;
	}

	/**
	 * remove cards from the deck
	 * @param s the suit of the card we'd like to remove
	 * @param r the rank of the card we'd like to remove
	 * @return true if removed successfully; false, otherwise.
	 */
	public boolean removeCard(String s, String r) {
		Card tmpCard = new Card(s, r);
		for(int i=0; i<cards.size(); i++) {
			if(!cards.get(i).smallerThan(tmpCard) && !cards.get(i).biggerThan(tmpCard)) {
				cards.remove(cards.get(i));
				return true;
			}
		}
		return false;
	}

	/**
	 * add all the cards in the given ArrayList of Card class
	 * @param cards an ArrayList of cards to add
	 * @return true if succeeded; false if failed.
	 */
	public boolean addAll(ArrayList<Card> cards) {
		return this.cards.addAll(cards);
	}

	/**
	 * get the card at the given index
	 * @param index which card to get
	 * @return the card wanted
	 */
	public Card get(int index) {
		return this.cards.get(index);
	}

	/**
	 * remove the card at the given index
	 * @param index which card to remove
	 */
	public void remove(int index) {
		this.cards.remove(index);
	}

	/**
	 * Shuffle the deck
	 */
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
}

/**
 * Player can be AI or real-man
 */
class Player {
	public boolean active; // true if active; false otherwise.
	public ArrayList<Card> cards; // the player's hand

	/**
	 * constructor: initialize the instance variables
	 */
	public Player() {
		active = true;
		cards = new ArrayList<Card>();
	}

	/**
	 * add a card to the hand
	 * @param c the card to be added to the player's hand.
	 */
	public void addCard(Card c) {
		this.cards.add(c);
	}

	/**
	 * remove a card from a designated position
	 * @param pos position of the card to be removed
	 * @return the card removed
	 */
	public Card removeCard(int pos) {
		// check if _pos_ is out of bound
		if(pos >= this.cards.size()) return null;
		Card c = this.cards.get(pos);
		this.cards.remove(pos);
		return c;
	}

	/**
	 * do the drawing
	 * @param p the player to draw from
	 * @return the card that's been drawn
	 */
	public Card draw(Player p) {
		int how_many_cards = p.getHandCount();
		int which_do_you_want = this.drawingStrategy(how_many_cards);
		Card c = p.removeCard(which_do_you_want);
		this.addCard(c);
		this.drop();
		return c;
	}

	/**
	 * the default drawing strategy is pure random strategy.
	 * welcome to override this ^_^
	 * @param totalCount the total number of cards to choose from
	 * @return integer indicating the index of the card we want
	 */
	public int drawingStrategy(int totalCount) {
		// generate a random number
		Random ran = new Random();
		return ran.nextInt(totalCount);
	}

	/**
	 * check the hand, and drop cards if necessary
	 */
	public void drop() {
		this.sortHand(); // just to make sure the hand is sorted
		for(int i=0; (i+1)<this.cards.size(); i++) {
			int r1 = this.cards.get(i).getIntRank();
			int r2 = this.cards.get(i+1).getIntRank();
			if(r1 == r2 && r1 != 0) {
				this.cards.remove(i);
				this.cards.remove(i);
				i -= 1; // to neutralize the effect of: i++
			}
		}
	}

	/**
	 * upadte the _active_ boolean instance variable
	 */
	public void updateStatus() {
		// just in case
		this.sortHand();
		this.drop();
		// check hand
		if(this.cards.size() == 0)
			this.active = false; // win
	}

	/**
	 * sort the player's current hand
	 */
	public void sortHand() {
		// utilize bubble sort
		for(int i=0; i<this.cards.size(); i++) {
			for(int j=i+1; j<this.cards.size(); j++) {
				if(cards.get(i).biggerThan(cards.get(j))) {
					Collections.swap(cards, i, j);
				}
			}
		}
	}

	/**
	 * get a string representation of the player's hand
	 * @return a string representation of the hand
	 */
	public String getHandString() {
		// if no cards, return empty string
		if(this.cards.size() == 0) return "";
		// return the string of concatenating card strings
		this.sortHand();
		String buf = "";
		for(int i=0; i<this.cards.size()-1; i++) {
			buf += this.cards.get(i).toString() + " ";
		}
		buf += this.cards.get(this.cards.size()-1).toString();
		return buf;
	}

	/**
	 * get the number of cards the player has
	 * @return how many cards the player has
	 */
	public int getHandCount() {
		return this.cards.size();
	}
}
