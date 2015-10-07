import java.lang.*;
import java.util.Random; // for shuffling the deck

public class PlayGame {
	/** The main function
	 */
	public static void main(String argv[]) {
		// create a new game
		Game letsPlayGame = new Game();
		letsPlayGame.init();
		// let the game begin
		letsPlayGame.start();
	}

	public static class Game {
		public Player[] Players;
		public Deck Cards;
		public boolean basic_game_over_yet;

		/** initialize a game
		 */
		public void init() {
			// step 1: create four players
			Players = new Player[4];
			// step 2: create the deck
			Cards = new Deck();

			basic_game_over_yet = false;
		}

		/** start the game
		 */
		public void start() {
			// step 1: shuffle the deck
			Cards.shuffle();
			// step 2: deal the cards
			System.out.println("Deal cards");
			for(int i=0; i<4; i++) {
				if(i < 2) { // the first two persons should get 14 cards
					Card[] tmp_cards = new Card[14];
					// System.arraycopy(o_arr, startIndex, n_arr, startIndex, length)
					System.arraycopy(Cards.Cards, i*14, tmp_cards, 0, 14);
					Players[i] = new Player(14, tmp_cards);
				} else { // the latter two persons should get 13 cards
					Card[] tmp_cards = new Card[13];
					System.arraycopy(Cards.Cards, 28 + (i-2)*13, tmp_cards, 0, 13);
					Players[i] = new Player(13, tmp_cards);
				}
			}
			// output the default cards
			for(int i=0; i<4; i++) {
				System.out.print("Player"+i+": ");
				Players[i].printHand();
			}
			// drop for the first time
			System.out.println("Drop cards");
			for(int i=0; i<4; i++) {
				Players[i].drop();
				System.out.print("Player"+i+": ");
				Players[i].printHand();
			}
			System.out.println("Game start");
			// step 3: main while loop, break condition: one player left.
			int playerTo = -1; // will be determined later
			int playerFrom = 0; // will be determined later
			while(true) {
				// check the game status (if someone wins?)
				boolean status = this.check();
				if(!status) {
					System.out.println("Bonus game over");
					break;
				}
				// determine playerTo
				playerTo = playerFrom;
				while(Players[playerTo].active == 0)
					playerTo = (playerTo + 1) % 4;
				// determine playerFrom
				playerFrom = (playerTo + 1) % 4;
				while(Players[playerFrom].active == 0)
					playerFrom = (playerFrom + 1) % 4;
				// draw a card!
				Card tmp_card = Players[playerTo].draw(Players[playerFrom]);
				// output
				System.out.print("Player" + playerTo + " draws a card from Player" + playerFrom + " ");
				tmp_card.printCard();
				System.out.println(""); // just for new-line
				System.out.print("Player" + playerTo + ": ");
				Players[playerTo].printHand();
				System.out.print("Player" + playerFrom + ": ");
				Players[playerFrom].printHand();
			}
		}

		/** check if someone wins,
			return true if the game can continue;
			return false if game is completely over (only 1 player has cards).
		 */
		public boolean check() {
			int how_many_winners = 0;
			String output = "";
			for(int i=0; i<4; i++) {
				if(Players[i].active == 1) {
					Players[i].check();
					if(Players[i].active == 0) {
						// this player is a new winner
						if(how_many_winners == 0) {
							how_many_winners += 1;
							output = output + "Player" + i;
						} else {
							how_many_winners += 1;
							output = output + " and Player" + i;
						}
					}
				}
			}
			if(how_many_winners == 1) {
				System.out.println(output + " wins");
			} else if(how_many_winners == 2) {
				System.out.println(output + " win");
			}
			if(how_many_winners > 0 && !basic_game_over_yet) {
				basic_game_over_yet = true;
				System.out.println("Basic game over");
				System.out.println("Continue");
			}
			// check the game status
			int active_players = 0;
			for(int i=0; i<4; i++) {
				if(Players[i].active == 1)
					active_players += 1;
			}
			if(active_players == 1) return false;
			return true;
		}
	}

	public static class Player {
		public int active; // 0: inactive, 1: active
		public int cardCount;
		public Card[] Cards;

		/** constructor (overloaded)
		 */
		public Player(int cc, Card[] cs) { // param: card_count & cards
			active = 1;
			cardCount = cc;
			Cards = new Card[cardCount+5];
			for(int i=0; i<cardCount; i++) {
				Cards[i] = cs[i];
			}
			this.sortHand();
		}

		/** sort the player's current hand
		 */
		public void sortHand() {
			// let's implement bubble sort(?)
			for(int i=0; i<cardCount; i++) {
				for(int j=i+1; j<cardCount; j++) {
					if(Cards[i].biggerThan(Cards[j]) == 1) {
						// swap!
						Card tmp_card = Cards[i];
						Cards[i] = Cards[j];
						Cards[j] = tmp_card;
					}
				}
			}
		}

		/** draw a card from another Player
		 */
		public Card draw(Player playerToDraw) {
			// step 1: determine the card we want
			int how_many_cards = playerToDraw.cardCount;
			// generate a random number between 0 ~ how_many_cards-1
			Random ran = new Random();
			int cardToDraw = ran.nextInt(how_many_cards); // including itself, i.e., "i"
			// step 2: do DRAW!
			Card tmp_card = playerToDraw.deleteCard(cardToDraw);
			this.addCard(tmp_card);
			this.drop();
			return tmp_card;
		}

		/** check my own cards, and drop the pairs
		 */
		public void drop() {
			// just in case, let's sort the hand again.
			this.sortHand();
			for(int i=0; i+1<cardCount; i++) {
				int rank1 = Cards[i].getIntRank();
				int rank2 = Cards[i+1].getIntRank();
				if(rank1 != 0 && rank1 == rank2) {
					// pair found!! drop them!!
					for(int j=i+2; j<cardCount; j++) {
						Cards[j-2] = Cards[j];
					}
					cardCount -= 2;
					i--;
				}
			}
		}

		/** delete a card on the index given, and return that card.
		 */
		public Card deleteCard(int index) {
			if(index >= cardCount)
				System.out.println("ERROR: you're trying to delete a card that does not exist.");
			Card tmp_card = Cards[index];
			for(int i=index; i+1<cardCount; i++) {
				Cards[i] = Cards[i+1];
			}
			cardCount -= 1;
			return tmp_card;
		}

		/** add a card (to a proper location)
		 */
		public void addCard(Card cardToAdd) {
			Cards[cardCount++] = cardToAdd;
			this.sortHand();
		}

		/** print the cards of this player by ascending order
		 */
		public void printHand() {
			for(int i=0; i<cardCount; i++) {
				Cards[i].printCard();
				System.out.print(" ");
			}
			System.out.println(""); // just for new-line
		}

		/** update the Player's status (active):
			set active = 0 if won.
		 */
		public void check() {
			// just in case, sort again
			this.sortHand();
			// just in case, drop again
			this.drop();
			if(cardCount == 0)
				this.active = 0; // win!
		}
	}

	public static class Deck {
		public Card[] Cards;

		/** constructor (overloaded)
		 */
		public Deck() {
			Cards = new Card[54];
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
				if((i+1)%13 == 0) {
					r = "K";
				} else if((i+1)%13 == 12) {
					r = "Q";
				} else if((i+1)%13 == 11) {
					r = "J";
				} else if((i+1)%13 == 1) {
					r = "A";
				} else {
					int rr = (i+1)%13;
					r = Integer.toString(rr);
				}
				// create the card
				Cards[i] = new Card(s, r);
			}
			// creat two jokers
			Cards[52] = new Card("R", "0");
			Cards[53] = new Card("B", "0");
		}

		/** shuffle the deck
		 */
		public void shuffle() {
			for(int i=53; i>=0; i--) {
				// generate a random number between 0 ~ i
				Random ran = new Random();
				int toSwap = ran.nextInt(i+1); // including itself, i.e., "i"
				// swap the two cards
				Card temp = Cards[toSwap];
				Cards[toSwap] = Cards[i];
				Cards[i] = temp;
			}
		}
	}

	public static class Card {
		public String suit;
		public String rank;

		/** constructor (overloaded)
		 */
		public Card(String s, String r) {
			suit = s;
			rank = r;
		}

		public void printCard() {
			String output = suit+rank;
			System.out.print(output);
		}

		/** check if smaller than 'c':
		 *  If true, returns 1;
		 *  If false, returns 0;
		 */
		public int smallerThan(Card c) {
			int my_rank = this.getIntRank();
			int your_rank = c.getIntRank();
			if(my_rank < your_rank) return 1;
			if(my_rank > your_rank) return 0;
			// same rank, compare suit!
			int my_suit = this.getIntSuit();
			int your_suit = c.getIntSuit();
			if(my_suit < your_suit) return 1;
			if(my_suit > your_suit) return 0;
			System.out.println("ERROR: shouldn't be here! Two identical cards found when comparing cards");
			return 0;
		}

		/** check if bigger than 'c':
		 *  If true, returns 1;
		 *  If false, returns 0;
		 */
		public int biggerThan(Card c) {
			int smaller = this.smallerThan(c);
			if(smaller == 1) return 0;
			else if(smaller == 0) return 1;
			System.out.println("ERROR: shouldn't be here! someothing wrong returned from smallerThan() to biggerThan()");
			return 0;
		}

		public int getIntSuit() {
			if(suit.equals("R"))
				return 1;
			if(suit.equals("B"))
				return 2;
			if(suit.equals("C"))
				return 3;
			if(suit.equals("D"))
				return 4;
			if(suit.equals("H"))
				return 5;
			if(suit.equals("S"))
				return 6;
			return 0;
		}

		public int getIntRank() {
			// exception parsing
			if(rank.equals("A"))
				return 14;
			if(rank.equals("K"))
				return 13;
			if(rank.equals("Q"))
				return 12;
			if(rank.equals("J"))
				return 11;
			return Integer.parseInt(rank); // default parsing
		}
	}
}
