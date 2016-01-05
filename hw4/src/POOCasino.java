package foop;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;

public class POOCasino {

	/**
	 * the list of players participating in this BlackJack game
	 */
	private static ArrayList<Player> players;

	/**
	 * the total number of players
	 */
	public static int player_count;

	/**
	 * the indicator of where we are currently, starting from 0, ends at (total_round - 1)
	 */
	public static int current_round;

	/**
	 * total number of rounds, determined by command-line argument.
	 */
	private static int total_round;

	/**
	 * initial number of chips of each player, determined by command-line argument.
	 */
	private static int initial_chips;

	/**
	 * the deck owned by the casino
	 */
	private static Deck officialDeck;

	/**
	 * recording all the cards on the table of last round
	 */
	public static ArrayList<Hand> last_table;

	/**
	 * store all the hands on the table
	 */
	private static ArrayList<CompositeHand> players_hands;

	/**
	 * store the hand of dealer
	 */
	private static CompositeHand dealers_hand;

	/**
	 * form an ArrayList of Hands to represent the current table
	 * @param perspective the player index where the viewpoint is
	 */
	private static ArrayList<Hand> getCurrentTable(int perspective) {
		ArrayList<Hand> ret = new ArrayList<Hand>();
		for(int i=0; i<players_hands.size(); i++) {
			if(players_hands.get(i).playerIndex != perspective) {
				// add the opened hand to _ret_
				ret.add(players_hands.get(i).face_up_hand);
			}
		}
		return ret;
	}

	/**
	 * print the status of all the players to stdout
	 */
	private static void printAllPlayerStatus() {
		String s = "";
		for(int i=0; i<player_count; i++) {
			s += "Player " + i + ": " + players.get(i).toString() + " (" + players.get(i).get_chips() + ")\n";
		}
		System.out.println(s);
	}

	/**
	 * print the hands of all the players to stdout
	 */
	private static void printAllPlayerHands() {
		String s = "";
		for(int i=0; i<players_hands.size(); i++) {
			int pIndex = players_hands.get(i).playerIndex;
			s += "Player " + pIndex + "'s hand: " + players_hands.get(i) + "\n";
		}
		System.out.println(s);
	}

	/**
	 * print the hand of the dealer to stdout
	 */
	private static void printDealerHand() {
		String s = "";
		s += "Dealer's hand: " + dealers_hand + "\n";
		System.out.println(s);
	}

	/**
	 * This is the main function
	 * @param argv the command-line arguments, array of string.
	 */
	public static void main(String argv[]) {

		/* step 0: initialization of some instance variables */
		players = new ArrayList<Player>();
		current_round = 0;
		last_table = new ArrayList<Hand>();

		/* step 1: initializing the players from arguments */
		// exec cmd: $java POOCasino nRound nChip Player1 Player2 Player3 Player4
		total_round = Integer.parseInt(argv[0]);
		initial_chips = Integer.parseInt(argv[1]);
		player_count = argv.length - 2; // the first 2 arg are nRound and nChip
		for(int i=2; i<argv.length; i++) {
			try {
				// get the class
				Class c = Class.forName(argv[i]);
				// get the constructor
				Class[] params = new Class[1];
				params[0] = Integer.TYPE;
				Constructor cc = c.getConstructor(params);
				// create an instance of the class
				Object[] paramObj = new Object[1];
				paramObj[0] = new Integer(initial_chips);
				players.add((Player) cc.newInstance(paramObj));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		/* step 2: open the casino's official deck and shuffle it */
		officialDeck = new Deck();
		officialDeck.open();
		officialDeck.shuffle();


		// output the status of all players before game started
		System.out.println("/---------- Before Game Started ----------/");
		printAllPlayerStatus();


		/* step 3: main while-loop for rounds */
		while(current_round < total_round) {
			// initialize some variables
			players_hands = new ArrayList<CompositeHand>();
			int[] bets = new int[player_count];
			boolean[] buy_insurance = new boolean[player_count];
			boolean[] surrender_or_not = new boolean[player_count];
			// 4-1: ask every player to make a bet
			for(int i=0; i<player_count; i++)
				bets[i] = players.get(i).make_bet(last_table, player_count, i);
			// 4-2: assign the initial 2 cards to each player and the dealer
			// 4-2-1: dealer
			dealers_hand = new CompositeHand(-1, officialDeck.getNextCard(), officialDeck.getNextCard());
			// 4-2-2: players
			for(int i=0; i<player_count; i++) {
				ArrayList<Card> up = new ArrayList<Card>();
				ArrayList<Card> down = new ArrayList<Card>();
				up.add(officialDeck.getNextCard());
				down.add(officialDeck.getNextCard());
				Hand upp = new Hand(up);
				Hand downn = new Hand(down);
				CompositeHand a_composite_hand = new CompositeHand(i, downn, upp);
				players_hands.add(a_composite_hand);
			}
			// 4-3: if dealer's face-up card is ACE, ask player buy_insurance() or not
			for(int i=0; i<player_count; i++) {
				if(dealers_hand.face_up_hand.getCards().get(0).getValue() == 1) {
					Card my_open = players_hands.get(i).face_up_hand.getCards().get(0);
					Card dealer_open = dealers_hand.face_up_hand.getCards().get(0);
					ArrayList<Hand> current_table = getCurrentTable(i); // perspective == i
					buy_insurance[i] = players.get(i).buy_insurance(my_open, dealer_open, current_table);
				} else {
					buy_insurance[i] = false;
				}
			}
			// 4-4: if dealer not blackjack, ask player do_surrender() or not
			dealers_hand.sneakPeakEvaluate();
			boolean dealer_is_blackjack = dealers_hand.isBlackJack;
			for(int i=0; i<player_count; i++) {
				if(!dealer_is_blackjack) {
					// dealer doesn't get blackjack, prepare the parameters
					Card my_open = players_hands.get(i).face_up_hand.getCards().get(0);
					Card dealer_open = dealers_hand.face_up_hand.getCards().get(0);
					ArrayList<Hand> current_table = getCurrentTable(i); // perspective == i
					// ask the player
					surrender_or_not[i] = players.get(i).do_surrender(my_open, dealer_open, current_table);
				} else {
					// dealer gets blackjack
					surrender_or_not[i] = false;
				}
			}
			// 4-5: for each player didn't surrender
			for(int i=0; i<players_hands.size(); i++) {
				int pIndex = players_hands.get(i).playerIndex;
				if(surrender_or_not[pIndex] == false) {
					// flip up the face-down card
					players_hands.get(i).flipUp();
					// determine whether to split
					if(players_hands.get(i).canSplit()) {
						// prepare the parameter for asking player whether to split or not
						ArrayList<Card> my_open = players_hands.get(i).face_up_hand.getCards();
						Card dealer_open = dealers_hand.face_up_hand.getCards().get(0);
						ArrayList<Hand> current_table = getCurrentTable(pIndex); // perspective == pIndex
						if(players.get(pIndex).do_split(my_open, dealer_open, current_table)) {
							// SPLIT!!
							CompositeHand new_hand = players_hands.get(i).splitSelf();
							// add to players_hands at the next index
							players_hands.add(i+1, new_hand);
						}
					}
					// determine whether to double down
					if(players_hands.get(i).canDoubleDown()) {
						// prepare the parameter for asking player whether to double down or not
						Hand my_open = players_hands.get(i).face_up_hand;
						Card dealer_open = dealers_hand.face_up_hand.getCards().get(0);
						ArrayList<Hand> current_table = getCurrentTable(pIndex); // perspective == pIndex
						if(players.get(pIndex).do_double(my_open, dealer_open, current_table)) {
							// DOUBLE DOWN!
							// double the bet
							bets[pIndex] = bets[pIndex] * 2;
							// stand after receiving exactly 1 card
							players_hands.get(i).addCard(officialDeck.getNextCard());
							continue;
						}
					}
					// determine whether to hit / stand
					Hand my_open = players_hands.get(i).face_up_hand;
					Card dealer_open = dealers_hand.face_up_hand.getCards().get(0);
					ArrayList<Hand> current_table = getCurrentTable(pIndex);
					while(players_hands.get(i).evaluate() <= 21 && players.get(pIndex).hit_me(my_open, dealer_open, current_table)) {
						// give him/her a new card
						players_hands.get(i).addCard(officialDeck.getNextCard());
						// update
						my_open = players_hands.get(i).face_up_hand;
						current_table = getCurrentTable(pIndex);
					}
				}
			}

			// 4-6: dealer action
			dealers_hand.flipUp();
			int value = dealers_hand.evaluate();
			while(value <= 16 || (value == 17 && dealers_hand.isSoft)) {
				// draw
				dealers_hand.addCard(officialDeck.getNextCard());
				value = dealers_hand.evaluate();
			}

			// 4-7: comparing the results
			for(int i=0; i<players_hands.size(); i++) {
				value = players_hands.get(i).evaluate();
				int pIndex = players_hands.get(i).playerIndex;
				if(surrender_or_not[pIndex]) {
					// player surrenders
					try {
						players.get(pIndex).decrease_chips((1/2) * bets[pIndex]);
					} catch(Player.NegativeException e) {
						System.out.println("negative exception caught when decreasing chips for player: " + pIndex);
					} catch(Player.BrokeException e) {
						System.out.println("broke exception caught when decreasing chips for player: " + pIndex);
					}
				} else if(players_hands.get(i).evaluate() > 21) {
					// player busted
					try {
						players.get(pIndex).decrease_chips(bets[pIndex]);
					} catch(Player.NegativeException e) {
						System.out.println("negative exception caught when decreasing chips for player: " + pIndex);
					} catch(Player.BrokeException e) {
						System.out.println("broke exception caught when decreasing chips for player: " + pIndex);
					}
				} else if(dealers_hand.isBlackJack && players_hands.get(i).isBlackJack) {
					// both dealer and player got BlackJack
					// "push": player gets no more chips
				} else if(players_hands.get(i).isBlackJack) {
					// player got BlackJack, dealer not.
					// player gets (3/2)*B more chips
					try {
						players.get(pIndex).increase_chips((3/2) * bets[pIndex]);
					} catch(Player.NegativeException e) {
						System.out.println("negative exception caught when increasing chips for player: " + pIndex);
					}
				} else if(dealers_hand.evaluate() > 21) {
					// dealer busted
					try {
						players.get(pIndex).increase_chips(bets[pIndex]);
					} catch(Player.NegativeException e) {
						System.out.println("negative exception caught when increaseing chips for player: " + pIndex);
					}
				} else if(dealers_hand.isBlackJack) {
					// dealer BlackJack, player didn't
					if(buy_insurance[pIndex]) {
						// player bought an insurance
						// making it even
					} else {
						// player didn't buy insurance
						try {
							players.get(pIndex).decrease_chips(bets[pIndex]);
						} catch(Player.NegativeException e) {
							System.out.println("negatice exception caught when decreasing chips for player: " + pIndex);
						} catch(Player.BrokeException e) {
							System.out.println("broke exception caught when decreasing chips for player: " + pIndex);
						}
					}
				} else {
					// neither of them got busted or BlackJack
					int dealer_value = dealers_hand.evaluate();
					if(value > dealer_value) {
						// player gets more
						// player gets B more chips
						try {
							players.get(pIndex).increase_chips(bets[pIndex]);
						} catch(Player.NegativeException e) {
							System.out.println("negatice exception caught when increasing chips for player: " + pIndex);
						}
					} else if(value < dealer_value) {
						// dealer gets more value
						// bet goes to casino
						try {
							players.get(pIndex).decrease_chips(bets[pIndex]);
						} catch(Player.NegativeException e) {
							System.out.println("negative exception caught when decreasing chips for player: " + pIndex);
						} catch(Player.BrokeException e) {
							System.out.println("broke exception caught when decreasing chips for player: " + pIndex);
						}
					} else {
						// a push (same value)
						// player gets no more chips
					}
				}
				// subtract the insurance amount
				if(buy_insurance[pIndex])
					try {
						players.get(pIndex).decrease_chips((1/2) * bets[pIndex]);
					} catch(Player.NegativeException e) {
						System.out.println("negative exception caught when decreasing chips for player: " + pIndex);
					} catch(Player.BrokeException e) {
						System.out.println("broke exception caught when decreasing chips for player: " + pIndex);
					}
			}

			// last thing: record all the cards on the table of this round
			// set perpective to -100, so dealer's hand and all players' hands will be recorded
			// (-1 for dealer, 0 ~ player_count-1 for players)
			last_table = getCurrentTable(-100);
			current_round += 1;

			// output the status of all players after each round
			System.out.println("/---------- After Round" + current_round + " ----------/");
			// print the dealer's hand
			printDealerHand();
			// print all players' hands
			printAllPlayerHands();
			// print the bets
			String bets_string = "Players' bets are: ";
			for(int i=0; i<player_count; i++)
				bets_string += (bets[i] + " ");
			System.out.println(bets_string + "\n");
			// print the players' status
			printAllPlayerStatus();
		}
		
	}

	private static class CompositeHand {

		/**
		 * storing face-up cards in a Hand instance
		 */
		public Hand face_up_hand;

		/**
		 * storing face-down cards in a Hand instance
		 */
		public Hand face_down_hand;

		/**
		 * indicating which player owns this CompositeHand instance
		 * -1 means not set or dealer.
		 */
		public int playerIndex;

		/**
		 * whether the result of evaluate() is a soft value.
		 */
		public boolean isSoft;

		/**
		 * whether the result of evaluate() is a BlackJack
		 */
		public boolean isBlackJack;

		/**
		 * Constructor
		 */
		public CompositeHand() {
			playerIndex = -1;
			isSoft = false;
			isBlackJack = false;
		}

		/**
		 * constructor
		 * @param pIndex indicating which player owns this CompositeHand
		 * @param face_down a Hand instance for face-down cards for initialization
		 * @param face_up a Hand instance for face-up cards for initialization
		 */
		public CompositeHand(int pIndex, Hand face_down, Hand face_up) {
			face_down_hand = face_down;
			face_up_hand = face_up;
			playerIndex = pIndex;
			isSoft = false;
			isBlackJack = false;
		}

		/**
		 * constructor
		 * @param pIndex indicating which player owns this CompositeHand
		 * @param face_down a card to initialize the face_down_hand
		 * @param face_up a card to initialize the face_up_hand
		 */
		public CompositeHand(int pIndex, Card face_down, Card face_up) {
			ArrayList<Card> tmp = new ArrayList<Card>();
			tmp.add(face_down);
			face_down_hand = new Hand(tmp);
			tmp.clear();
			tmp.add(face_up);
			face_up_hand = new Hand(tmp);
			playerIndex = pIndex;
			isSoft = false;
			isBlackJack = false;
		}

		/**
		 * to flip up all the face-down cards
		 */
		public void flipUp() {
			ArrayList<Card> to_move = face_down_hand.getCards();
			ArrayList<Card> to_add_to = face_up_hand.getCards();
			for(int i=0; i<to_move.size(); i++) {
				to_add_to.add(to_move.get(i));
			}
			face_up_hand = new Hand(to_add_to);
			to_move.clear();
			face_down_hand = new Hand(to_move);
		}

		/**
		 * to check whether this CompositeHand can be splitted
		 * @return true if can be splitted; otherwise, false.
		 */
		public boolean canSplit() {
			// check the number of cards
			if(face_up_hand.getCards().size() != 2 || face_down_hand.getCards().size() != 0)
				return false;
			// check the value
			int v1 = -1, v2 = -1;
			try {
				v1 = face_up_hand.getCards().get(0).getValue();
			} catch(Exception e) {
				System.out.println("v1 error.");
				return false;
			}
			try {
				v2 = face_up_hand.getCards().get(1).getValue();
			} catch(Exception e) {
				System.out.println("v2 error.");
				return false;
			}
			if(v1 == v2)
				return true;
			return false;
		}

		/**
		 * to split a CompositeHand into 2
		 * @return a new CompositeHand instance resulted from splitting
		 */
		public CompositeHand splitSelf() {
			// just a normal checking
			if(!canSplit())
				return null;
			/* split the hand */
			// 1) handle the face-up cards splitting
			ArrayList<Card> o_up_cards = face_up_hand.getCards(); // original face-up cards
			ArrayList<Card> n_up_cards = new ArrayList<Card>();
			n_up_cards.add(o_up_cards.get(1)); // new face-up cards
			o_up_cards.remove(1);
			// 2) update the original face-up hand
			face_up_hand = new Hand(o_up_cards);
			// 3) create a new CompositeHand instance for splitted hand
			ArrayList<Card> dCards = new ArrayList<Card>(); // for dummy face-down hand
			Hand dHand = new Hand(dCards);
			Hand uHand = new Hand(n_up_cards); // create face-up hand from new face-up cards
			CompositeHand new_composite_hand = new CompositeHand(playerIndex, dHand, uHand);
			return new_composite_hand;
		}

		/**
		 * to check whether this CompositeHand can be double-downed
		 * @return true if positive; otherwise, false.
		 */
		public boolean canDoubleDown() {
			// if the player didn't split
			int count = 0;
			for(int i=0; i<players_hands.size(); i++) {
				if(players_hands.get(i).playerIndex == playerIndex)
					count += 1;
			}
			if(count == 1)
				return true;
			return false;
		}

		/**
		 * to add a card to face_up_hand
		 * @param c the card to add to face_up_hand
		 */
		public void addCard(Card c) {
			ArrayList<Card> cards = face_up_hand.getCards();
			cards.add(c);
			face_up_hand = new Hand(cards);
		}

		/**
		 * to evaluate the total points of face_up_hand
		 * @return an integer, the total value of face_up_hand
		 */
		public int evaluate() {
			int ret = 0;
			int ace_as_one = 0;
			int ace_as_eleven = 0;
			ArrayList<Card> cards = face_up_hand.getCards();
			for(int i=0; i<cards.size(); i++) {
				int tmp = cards.get(i).getValue();
				// face cards are all counted as 10
				if(tmp >= 10 && tmp <= 13)
					tmp = 10;
				// record if ACE is encountered
				if(tmp == 1)
					ace_as_one += 1;
				// add up "ret"
				ret += tmp;
			}
			while(ace_as_one >= 1 && ret + 10 <= 21) {
				// enter loop if:
				// 1) still some ACE's counted as 1, and
				// 2) won't be busted after adding 10 points
				ace_as_one -= 1;
				ace_as_eleven += 1;
				ret += 10;
			}
			// fill in the "isSoft" instance variable
			if(ace_as_eleven >= 1) {
				// there's still one or more ACE's counted as 1
				isSoft = true;
			}
			// fill in the "isBlackJack" instance variable
			if(cards.size() == 2 && ret == 21 && ace_as_eleven == 1) {
				isBlackJack = true;
			}
			return ret;
		}

		/**
		 * to evaluate the total points of both face_up_hand and face_down_hand
		 * @return an integer, the total value
		 */
		public int sneakPeakEvaluate() {
			int ret = 0;
			int ace_as_one = 0;
			int ace_as_eleven = 0;
			ArrayList<Card> up_cards = face_up_hand.getCards();
			ArrayList<Card> down_cards = face_down_hand.getCards();
			for(int i=0; i<up_cards.size(); i++) {
				int tmp = up_cards.get(i).getValue();
				if(tmp >= 10 && tmp <= 13)
					tmp = 10;
				if(tmp == 1)
					ace_as_one += 1;
				ret += tmp;
			}
			for(int i=0; i<down_cards.size(); i++) {
				int tmp = down_cards.get(i).getValue();
				if(tmp >= 10 && tmp <= 13)
					tmp = 10;
				if(tmp == 1)
					ace_as_one += 1;
				ret += tmp;
			}
			while(ace_as_one >= 1 && ret + 10 <= 21) {
				ace_as_one -= 1;
				ace_as_eleven += 1;
				ret += 10;
			}
			if(ace_as_eleven >= 1)
				isSoft = true;
			if((up_cards.size() + down_cards.size()) == 2 && ret == 21 && ace_as_eleven == 1)
				isBlackJack = true;
			return ret;
		}

		/**
		 * to show the hand
		 * @return a string representing the hand
		 */
		@Override
		public String toString() {
			String ret = "";
			// 1) the face_down_hand
			ArrayList<Card> d_cards = face_down_hand.getCards();
			for(int i=0; i<d_cards.size(); i++) {
				ret += "[**] ";
			}
			// 2) the face_up_hand
			ArrayList<Card> u_cards = face_up_hand.getCards();
			for(int i=0; i<u_cards.size(); i++) {
				ret += "[";
				// dealing with suit
				switch(u_cards.get(i).getSuit()) {
					case 4:
						ret += "C"; // club
						break;
					case 3:
						ret += "D"; // diamond
						break;
					case 2:
						ret += "H"; // heart
						break;
					case 1:
						ret += "S"; // spade
						break;
					default:
						ret += "."; // unknown
				}
				// dealing with value (rank)
				switch(u_cards.get(i).getValue()) {
					case 13:
						ret += "K"; // king
						break;
					case 12:
						ret += "Q"; // queen
						break;
					case 11:
						ret += "J"; // jack
						break;
					case 10:
						ret += "T"; // ten
						break;
					case 1:
						ret += "A"; // ace
						break;
					default:
						ret += ("" + u_cards.get(i).getValue());
				}
				ret += "] ";
			}
			return ret;
		}
	}

}












