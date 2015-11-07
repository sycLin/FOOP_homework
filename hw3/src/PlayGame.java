import java.lang.*;
import java.util.Random; // for shuffling the deck

public class PlayGame {
	/**
	 * This is the main function
	 */
	public static void main(String argv[]) {
		;
	}

	/**
	 * Represent a card
	 */
	public static class Card {

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
			if(this.rank.equals("K")) return 14;
			if(this.rank.equals("Q")) return 14;
			if(this.rank.equals("J")) return 14;
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
	public static class Deck {
		;
	}

	/**
	 * Player can be AI or real-man
	 */
	public static class Player {
		;
	}

}