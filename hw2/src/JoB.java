import java.lang.*;
import java.util.Scanner;

public class JoB {
	public static void main(String argv[]) {
		System.out.println("Testing");
		// try to create an instance of Random Index
		RandomIndex test = new RandomIndex();
		test.setSize(52);
		test.initializeIndex();
		System.out.println("Before permuteIndex()");
		for(int i=0; i<test.index.length; i++) {
			System.out.print(test.index[i] + " ");
		}
		System.out.print("\n");
		test.permuteIndex();
		System.out.println("After permuteIndex()");
		for(int i=0; i<test.index.length; i++) {
			System.out.print(test.index[i] + " ");
		}
		System.out.print("\n");
		Player p = new Player();
		Card c = new Card("S", "K");
		c.printCard(); // no new line in the end
	}

	/**
	 * this class demonstrates the game.
	 */
	public static class POOCasino {

		/**
		 * casinoComputer is the only computer in this casino
		 */
		public Computer casinoComputer;

		/**
		 * casinoPlayer is our beloved guest in this casino
		 */
		public Player casinoPlayer;

		/**
		 * Constructor: initialize the casinoComputer and casinoPlayer
		 */
		public POOCasino() {
			System.out.println("POOCasino Jacks or better, written by b01902044 Yu-Chih Lin");
			System.err.println("starting up casino computer...");
			casinoComputer = new Computer();
			System.err.println("player initializing...");
			casinoPlayer = new Player();
		}

		/**
		 * start operating the casino!
		 * there are 6 steps in the main while-loop, they are according to "hw2.pdf".
		 */
		public void operate() {
			while(true) {
				// step 1: computer opens a new deck of 52 cards, and shuffles it.
				casinoComputer.openDeck();
				// step 2: player betting
				if(!this.casinoPlayer.setCurrentBet())
					break;
				// step 3: computer distribute 5 cards to player
				casinoComputer.distributeCard(casinoPlayer);
				// step 4: player chooses which to keep; others are replaced.
				casinoComputer.redistributeCard(casinoPlayer, casinoPlayer.discardAnyCards());
				// step 5 & 6: computer determines the best hand, and computer pays the user.
				casinoComputer.payThePlayer(casinoPlayer);
				// one round done!
				casinoPlayer.roundCount += 1;
			}
		}
	}

	/**
	 * every instance of this class represents one of the 52 cards in a standard deck.
	 */
	public static class Card {

		/**
		 * the suit of a card: S, H, D, or C.
		 */
		public String suit;

		/**
		 * the rank of a card: 2, 3, ..., J, Q, K, or A.
		 */
		public String rank;

		/**
		 * Constructor: initialize a card with given suit and rank.
		 * @param s the suit for initialization.
		 * @param r the rank for initialization.
		 */
		public Card(String s, String r) {
			this.suit = s;
			this.rank = r;
		}

		/**
		 * form the string representation of this card.
		 * @return  return the string representation of this card.
		 */
		public String toString() {
			return this.suit + this.rank;
		}

		/**
		 * Output the card onto screen with no newline, nor space.
		 */
		public void printCard() {
			// form the output string
			String outputStr = this.toString();
			// output onto screen (stdout)
			System.out.print(outputStr);
		}

		/**
		 * Determine if smaller than a given card.
		 * @param c another card.
		 * @return  true if smaller than c; otherwise, false is return.
		 */
		public boolean smallerThan(Card c) {
			// compare the ranks first
			int my_rank = this.getIntRank();
			int your_rank = c.getIntRank();
			if(my_rank < your_rank) return true;
			if(my_rank > your_rank) return false;
			// the ranks are identical, let's compare the suits
			int my_suit = this.getIntSuit();
			int your_suit = c.getIntSuit();
			if(my_suit < your_suit) return true;
			if(my_suit > your_suit) return false;
			// shouldn't be here... you have two identical cards...
			System.err.println("comparing two identical cards! ...");
			return false;
		}

		/**
		 * Determine if bigger than a given card.
		 * (calling smallerThan() actually)
		 * @param c another card.
		 * @return  true if bigger than c; false otherwise.
		 */
		public boolean biggerThan(Card c) {
			// call smallerThan()
			boolean isSmaller = this.smallerThan(c);
			if(isSmaller)
				return false;
			return true;
		}

		/**
		 * more convenient for comparing if we have integral rank
		 * @return  14 for A, 13 for K, 12 for Q, 11 for J.
		 */
		public int getIntRank() {
			// exceptions
			if(this.rank.equals("A")) return 14;
			if(this.rank.equals("K")) return 13;
			if(this.rank.equals("Q")) return 12;
			if(this.rank.equals("J")) return 11;
			// default
			return Integer.parseInt(rank);
		}

		/**
		 * more convenient for comparing if we have integral suits
		 * @return  4 for S(pade), 3 for H(eart), 2 for D(iamond), and 1 for C(lub).
		 */
		public int getIntSuit() {
			if(this.suit.equals("C")) return 1;
			if(this.suit.equals("D")) return 2;
			if(this.suit.equals("H")) return 3;
			if(this.suit.equals("S")) return 4;
			// shouldn't be here
			System.err.println("couldn't recognize the suit of this card: " + this.suit);
			return 0;
		}
	}

	/**
	 * shuffling N decks as described in hw2.pdf.
	 */
	public static class Shuffler {
		;
	}

	/**
	 * implementation of actions of a computer.
	 */
	public static class Computer {

		/**
		 * store the payoff table ratio w.r.t. player's bet
		 */
		private int[] payoffRatio = new int[]{250, 50, 25, 9, 6, 4, 3, 2, 1, 0};

		/**
		 * store the types of hand
		 */
		private String[] handType = new String[] {
			"royal flush",
			"straight flush",
			"four of a kind",
			"full house",
			"flush",
			"straight",
			"three of a kind",
			"two pair",
			"Jacks or better",
			"others"
		};

		/**
		 * store the deck of cards
		 */
		private Card[] deck;


		/**
		 * Constructor: initialize a Computer instance
		 */
		public Computer() {
			// TODO: here?
			// ...
			;
		}

		/**
		 * initialize the deck of 52 cards, and shuffle it
		 */
		public void openDeck() {
			// initialize the deck with the proper 52 cards 
			this.deck = new Card[52];
			for(int i=0; i<52; i++) {
				String s;
				String r;
				// deal with suit
				if(i < 13) {
					s = "C"; // club
				} else if(i < 26) {
					s = "D"; // diamond
				} else if(i < 39) {
					s = "H"; // heart
				} else {
					s = "S"; // spade
				}
				// deal with rank
				if((i+1) % 13 == 0) {
					r = "K";
				} else if((i+1) % 13 == 12) {
					r = "Q";
				} else if((i+1) % 13 == 11) {
					r = "J";
				} else if((i+1) % 13 == 1) {
					r = "A";
				} else {
					r = Integer.toString((i+1) % 13);
				}
				// construct the card
				this.deck[i] = new Card(s, r);
			}
			// shuffle the deck
			// TODO: here ...
		}

		/**
		 * distribute 5 cards to player
		 * @param player the player to distribute to
		 */
		public void distributeCard(Player player) {
			// TODO: ...
			;
		}

		/**
		 * check if player wants to discard any cards; replace them.
		 * @param player the player to re-distribute cards.
		 * @param keepOrNot an array of 5 booleans to indicate if keeping the card.
		 */
		public void redistributeCard(Player player, boolean[] keepOrNot) {
			if(keepOrNot[0] && keepOrNot[1] && keepOrNot[2] && keepOrNot[3] && keepOrNot[4])
				return; // player keeps all 5 cards
			// TODO: ...
			;
		}

		/**
		 * determine the best hand of the player.
		 * @param hand the 5 cards to determine.
		 * @return  1, 2, ..., 10: representing "royal flush", "straight flush", ..., "other".
		 */
		private int determineBestHand(Card[] hand) {
			if(hand == null) return -1; // error
			return 0;
		}

		/**
		 * pay the player according to the player's bet, the hand, and the ratio.
		 * (will call determineBestHand() to determine the ratio)
		 * (will update the roundCount of the player too)
		 * @param player the player to pay
		 */
		public void payThePlayer(Player player) {
			int myHand = determineBestHand(player.hand);
			if(myHand == -1) {
				// error
				System.err.println("ERROR: cannot determine hand of player!");
				return;
			}
			int payoff = player.currentBet*this.payoffRatio[myHand];
			if(payoff == 1250)
				payoff = 4000; // the only exception in the payoff table
			System.out.println("You get a " + handType[myHand] + " hand. The payoff is " + payoff + ".");
			player.balance += payoff;
			System.out.println("Your have " + player.balance + " P-dollars now.");
		}

	}

	/**
	 * interacts with the decisions from human player.
	 */
	public static class Player {

		/**
		 * store the balance of the player's P-dollars.
		 */
		public int balance;

		/**
		 * store the name of the player
		 */
		public String name;

		/**
		 * store the current hand of the player (should be 5 cards).
		 */
		public Card[] hand;

		/**
		 * store the bet for this round.
		 */
		public int currentBet;

		/**
		 * how many round has this player played
		 */
		public int roundCount;

		/**
		 * Constructor: initialize a player instance with player's name from stdin and with default balance.
		 * (also initialize other instance variables: roundCount = 0 and currentBet = 0)
		 */
		public Player() {
			// setup player's name by reading from stdin
			Scanner scanner = new Scanner(System.in);
			System.out.print("Please enter your name: ");
			this.name = scanner.nextLine();
			// default balance is set to 1000 P-dollars
			this.balance = 1000;
			// some other instance variable initialization
			this.roundCount = 0;
			this.currentBet = 0;
			System.err.println("successfully initialized player: " + this.name);
			System.out.println("Welcome, " + this.name + ".");
			System.out.println("You have 1000 P-dollars now.");
		}

		/**
		 * ask the player how many P-dollars he/she would like to bet (i.e., read from stdin).
		 * (player can choose to bet 1~5 P-dollars)
		 * @return  true if betting 1~5; false when 0 is inputted indicating exiting this game.
		 */
		public boolean setCurrentBet() {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Please enter your P-dollar bet for round " + (roundCount + 1) + " (1-5 or 0 for quitting the game): ");
			this.currentBet = scanner.nextInt();
			if(this.currentBet >= 1 && this.currentBet <= 5) {
				this.balance = this.balance - this.currentBet;
				return true;
			}
			return false;
		}

		/**
		 * ask the player which cards out of the 5 to keep
		 * (print options (a), (b), (c), (d), and (e).)
		 * @return  array of booleans to indicate keeping or not
		 */
		public boolean[] discardAnyCards() {
			// output the current hand with options
			System.out.print("Your cards are");
			for(int i=0; i<5; i++) {
				System.out.print(" (" + Character.toString((char) (i+97)) + ") " + this.hand[i].toString());
			}
			System.out.println(""); // need a new line here
			// ask the player which to keep
			Scanner scanner = new Scanner(System.in);
			System.out.print("Which cards do you want to keep? ");
			String tmp = scanner.nextLine(); // player's input
			boolean[] keepOrNot = new boolean[]{false, false, false, false, false};
			if(!tmp.equals("none")) {
				for(int i=0; i<5; i++) {
					if(tmp.indexOf(((char)(i+97))) >= 0) {
						keepOrNot[i] = true;
					}
				}
			}
			// discard the cards
			if(keepOrNot[0] && keepOrNot[1] && keepOrNot[2] && keepOrNot[3] && keepOrNot[4]) {
				System.out.println("Okay. I will discard nothing.");
			} else {
				int count = 0;
				System.out.print("Okay. I will discard");
				for(int i=0; i<5; i++) {
					if(!keepOrNot[i]) {
						System.out.print(" (" + Character.toString((char) (i+97)) + ") " + this.hand[i].toString());
					}
					System.out.println(".");
				}
			}
			return keepOrNot;
		}

		/**
		 * sort the player's current hand by Bubble Sort.
		 */
		public void sortHand() {
			for(int i=0; i<5; i++) {
				for(int j=i+1; j<5; j++) {
					if(this.hand[i].biggerThan(this.hand[j])) {
						// swap
						Card tmp = this.hand[i];
						this.hand[i] = this.hand[j];
						this.hand[j] = tmp;
					}
				}
			}
		}
		
	}

}
