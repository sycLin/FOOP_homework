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
	private static ArrayList<Card> dealers_cards;

	/**
	 * form an ArrayList of Hands to represent the current table
	 * @param perspective the player index where the viewpoint is
	 */
	private static ArrayList<Hand> getCurrentTable(int perspective) {
		ArrayList<Hand> ret = new ArrayList<Hand>();
		for(int i=0; i<players_hands.size(); i++) {
			if(players_hands.get(i).playerIndex != perspective) {
				// add the opened hand to _ret_
				ret.add(players_hands.get(i).face_up_cards);
			}
		}
		return ret;
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


		/* step 3: main while-loop for rounds */
		while(current_round < total_round) {
			// initialize some variables
			dealers_cards = new ArrayList<Card>();
			players_hands = new ArrayList<CompositeHand>();
			int[] bets = new int[player_count];
			boolean[] buy_insurance = new boolean[player_count];
			boolean[] surrender_or_not = new boolean[player_count];
			// 4-1: ask every player to make a bet
			for(int i=0; i<player_count; i++)
				bets[i] = players.get(i).make_bet(last_table, player_count, i);
			// 4-2: assign the initial 2 cards to each player and the dealer
			dealers_cards.add(officialDeck.getNextCard()); // the face-down one
			dealers_cards.add(officialDeck.getNextCard()); // the face-up one
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
				if(dealers_cards.get(1).getValue() == 1) {
					Card my_open = players_hands.get(i).face_up_cards;
					Card dealer_open = dealers_cards.get(1);
					ArrayList<Hand> current_table = getCurrentTable(i); // perspective == i
					buy_insurance[i] = players.get(i).buy_insurance(my_open, dealer_open, current_table);
				} else {
					buy_insurance[i] = false;
				}
			}
			// 4-4: if dealer not blackjack, ask player do_surrender() or not
			boolean dealer_is_blackjack = false;
			if(dealers_cards.get(0).getValue() == 1 && dealers_cards.get(1).getValue() >= 10 && dealers_cards.get(1).getValue() <= 13)
				dealer_is_blackjack = true;
			if(dealers_cards.get(0).getValue() >= 10 && dealers_cards.get(0).getValue() <= 13 && dealers_cards.get(1).getValue() == 1)
				dealer_is_blackjack = true;
			for(int i=0; i<player_count; i++) {
				if(!dealer_is_blackjack) {
					Card my_open = players_hands.get(i).face_up_cards;
					Card dealer_open = dealers_cards.get(1);
					ArrayList<Hand> current_table = getCurrentTable(i); // perspective == i
					surrender_or_not[i] = players.get(i).do_surrender(/* ... */);
				} else {
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
					// determine whether to double down
					// determine whether to hit / stand
				}
			}
			for(int i=0; i<player_count; i++) {
				if(surrender_or_not[i] == false) {
					// flip up the face-down card
					// determine whether to split
					boolean toSplit = false;
					if(players_card.get(i).get(0).get(0).getValue() == players_card.get(i).get(0).get(1).getValue()) {
						// do_split(ArrayList<Card> my_open, Card dealer_open, ArrayList<Hand> current_table)
						toSplit = players.get(i).do_split(players_card.get(i).get(0), dealers_cards.get(1), current_table);
					}
					// determine whether to double down
					// determine whether to hit / stand
				}
			}
			// 4-6: dealer action
			// 4-7: comparing the results

			// last thing: record all the cards on the table of this round
			// ...
			currnet_round += 1;
		}
		
	}

	private static class CompositeHand {
		public Hand face_up_cards;
		public Hand face_down_cards;
		public int playerIndex;

		public CompositeHand() {
			playerIndex = -1;
		}

		public CompositeHand(int pIndex, Hand face_down, Hand face_up) {
			face_down_cards = face_down;
			face_up_cards = face_up;
			playerIndex = pIndex;
		}

		public void flipUp() {
			ArrayList<Card> to_move = face_down_cards.getCards();
			ArrayList<Card> to_add_to = face_up_cards.getCards();
			for(int i=0; i<to_move.size(); i++) {
				to_add_to.add(to_move.get(i));
			}
			face_up_cards = new Hand(to_add_to);
			face_down_cards.clear();
		}
	}

}












