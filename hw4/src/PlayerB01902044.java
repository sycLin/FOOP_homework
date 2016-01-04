package foop;
import java.lang.*;
import java.util.*;

class PlayerB01902044 extends Player {

	/* there are some methods defined in foop.Player that should remain unchanged */

	// void decrease_chips(double diff);
	// protected double get_chips();
	// void increase_chips(double diff);

	/* my own constants */

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_SPLIT = 1;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_DOUBLE_OR_HIT = 2;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_DOUBLE_OR_STAND = 3;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_HIT = 4;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_STAND = 5;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_SURRENDER_OR_HIT = 6;

	/**
	 * for myStrategy() function to return
	 */
	private final byte ACTION_SURRENDER_OR_STAND = 7;

	/* my own instance variables */

	/**
	 * this flag will be filled after selfEvaluate() function
	 */
	private boolean isSoft;
	
	/**
	 * this flag will be filled after selfEvaluate() function
	 */
	private boolean canSplit;

	/**
	 * this flag will be filled after selfEvaluate() function
	 */
	private boolean isBlackJack;

	/* there are some abstract methods defined in foop.Player for us to override */

	public PlayerB01902044(int chips) {
		super(chips);
		isSoft = false;
		canSplit = false;
	}
	
	@Override
	public boolean buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		// i never bought insurance XD
		return false;
	}

	@Override
	public boolean do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		byte ret = myStrategy(my_open.getCards(), dealer_open);
		switch(ret) {
			case ACTION_DOUBLE_OR_HIT:
			case ACTION_DOUBLE_OR_STAND:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		byte ret = myStrategy(my_open, dealer_open);
		switch(ret) {
			case ACTION_SPLIT:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		ArrayList<Card> dummy = new ArrayList<Card>();
		dummy.add(my_open);
		byte ret = myStrategy(dummy, dealer_open);
		switch(ret) {
			case ACTION_SURRENDER_OR_HIT:
			case ACTION_SURRENDER_OR_STAND:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		byte ret = myStrategy(my_open.getCards(), dealer_open);
		switch(ret) {
			case ACTION_HIT:
			case ACTION_SURRENDER_OR_HIT:
			case ACTION_DOUBLE_OR_HIT:
				return true;
			default:
				return false;
		}
	}

	@Override
	public int make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position) {
		// simple strategy
		return 1;
	}

	@Override
	public java.lang.String toString() {
		// System.err.println("warning: PlayerB01902044.toString() is not yet implemented.");
		return "having " + get_chips() + " chips.";
	}

	/**
	 * all my core strategy
	 * @param my_open an ArrayList of my open cards
	 * @param dealer_open the dealer's open card
	 * @return one of the 7 constants: ACTION_SPLIT, ACTION_DOUBLE_OR_HIT, ACTION_DOUBLE_OR_STAND, ACTION_HIT, ACTION_STAND, ACTION_SURRENDER_OR_HIT, ACTION_SURRENDER_OR_STAND
	 */
	private byte myStrategy(ArrayList<Card> my_open, Card dealer_open) {

		// 1) evaluate the hand
		// this function also sets the 3 boolean flags: isSoft, canSplit, isBlackJack
		int total_value = selfEvaluate(my_open);
		int d_v = (dealer_open.getValue() > 10) ? 10 : dealer_open.getValue(); // store the value of the dealer's opened card
		// 2) splittable strategy
		if(canSplit) {
			int v = (my_open.get(0).getValue() > 10) ? 10 : my_open.get(0).getValue(); // my value, processed
			switch(v) {
				case 2:
					if(d_v == 2) {
						// hit
						return ACTION_HIT;
					} else if(d_v >= 3 && d_v <= 7) {
						// split
						return ACTION_SPLIT;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 3:
					if(d_v >= 2 && d_v <= 3) {
						// hit
						return ACTION_HIT;
					} else if(d_v >= 4 && d_v <= 7) {
						// split
						return ACTION_SPLIT;
					} else if(d_v == 8) {
						// hit
						return ACTION_HIT;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 4:
					if(d_v == 5 && d_v == 6) {
						// double down
						return ACTION_DOUBLE_OR_HIT;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 6:
					if(d_v >= 2 && d_v <= 6) {
						// split
						return ACTION_SPLIT;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 7:
					if(d_v >= 2 && d_v <=7 ) {
						// split
						return ACTION_SPLIT;
					} else if(d_v == 8 || d_v == 9) {
						// hit
						return ACTION_HIT;
					} else if(d_v == 10) {
						// surrender or stand
						return ACTION_SURRENDER_OR_STAND;
					} else { // 1 (Ace)
						// surrender or hit
						return ACTION_SURRENDER_OR_HIT;
					}
				case 8:
					// split
					return ACTION_SPLIT;
				case 9:
					if(d_v == 7 || d_v == 10 || d_v == 1) {
						// stand
						return ACTION_STAND;
					}
					else {
						// split
						return ACTION_SPLIT;
					}
				case 1:
					// split
					return ACTION_SPLIT;
				default:
					// doesn't apply any of the rules above, i.e., 5-5 or 10-10
					// then will go to hard hand strategy ultimately
					break;
			}
		}
		// 3) soft hand strategy
		if(isSoft) {
			switch(total_value) {
				case 13:
				case 14:
				case 15:
				case 16:
					if(d_v >= 4 && d_v <= 6) {
						// double
						return ACTION_DOUBLE_OR_HIT;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 17:
					if(d_v >= 2 && d_v <= 6) {
						// double
						return ACTION_DOUBLE_OR_HIT;
					}
					else {
						// hit
						return ACTION_HIT;
					}
				case 18:
					if(d_v >= 3 && d_v <= 6) {
						// double
						return ACTION_DOUBLE_OR_STAND;
					} else if(d_v == 2 || d_v == 7 || d_v == 8) {
						// stand
						return ACTION_STAND;
					} else {
						// hit
						return ACTION_HIT;
					}
				case 19:
					if(d_v == 6) {
						// double
						return ACTION_DOUBLE_OR_STAND;
					} else {
						// stand
						return ACTION_STAND;
					}
				default: // 20+ of total value
					return ACTION_STAND;
			}
		}
		// 3) hard hand strategy
		if(!isSoft) {
			switch(total_value) {
				case 4:
				case 5:
				case 6:
				case 7:
					return ACTION_HIT;	
				case 8:
					if(d_v == 5 || d_v == 6)
						return ACTION_DOUBLE_OR_HIT;
				case 9:
					if(d_v >= 2 && d_v <= 6)
						return ACTION_DOUBLE_OR_HIT;
					else
						return ACTION_HIT;
				case 10:
					if(d_v >= 2 && d_v <= 9)
						return ACTION_DOUBLE_OR_HIT;
					else
						return ACTION_HIT;
				case 11:
					return ACTION_DOUBLE_OR_HIT;
				case 12:
					if(d_v >= 4 && d_v <= 6)
						return ACTION_STAND;
					else
						return ACTION_HIT;
				case 13:
				case 14:
					if(d_v >= 2 && d_v <= 6)
						return ACTION_STAND;
					else
						return ACTION_HIT;
				case 15:
					if(d_v >= 2 && d_v <= 6)
						return ACTION_STAND;
					else if(d_v >= 7 && d_v <= 10)
						return ACTION_HIT;
					else
						return ACTION_SURRENDER_OR_HIT;
				case 16:
					if(d_v >= 2 && d_v <= 6)
						return ACTION_STAND;
					else if(d_v >= 7 && d_v <= 9)
						return ACTION_HIT;
					else
						return ACTION_SURRENDER_OR_HIT;
				case 17:
					if(d_v >= 2 && d_v <= 10)
						return ACTION_STAND;
					else
						return ACTION_SURRENDER_OR_STAND;
				default: // 18+ of total value
					return ACTION_STAND;
			}
		}
		return ACTION_STAND; // default, but shouldn't be here, actually.
	}

	/**
	 * to evaluate my own hand and to set the flags: isSoft, canSplit, isBlackJack.
	 * @param my_open an ArrayList of my open cards
	 * @return an integer, the value of my hand
	 */
	private int selfEvaluate(ArrayList<Card> my_open) {
		// need to fill in the flags: isSoft, canSplit.

		// 1) deal with the canSplit boolean flag
		if(my_open.size() == 2 && (my_open.get(0).getValue() == my_open.get(1).getValue()))
			canSplit = true;
		// 2) deal with evaluation, and also set the isSoft flag
		int ret = 0; // the ultimate value evaluated
		int ace_as_one = 0; // recording how many ace's are counted as one
		int ace_as_eleven = 0; // recording how many ace's are counted as eleven
		for(int i=0; i<my_open.size(); i++) {
			int tmp = my_open.get(i).getValue();
			if(tmp >= 10 && tmp <= 13) // deal with face cards
				tmp = 10;
			if(tmp == 1) // encounter ACE
				ace_as_one += 1;
			ret += tmp; // accumulate the values
		}
		while(ace_as_one >= 1 && ret + 10 <= 21) {
			// while:
			// 1) there's one or more ACE's counted as one
			// 2) won't get busted if change the value of an ACE from one to eleven
			ace_as_one -= 1;
			ace_as_eleven += 1;
			ret += 10;
		}
		// 3) fill the flag isSoft
		if(ace_as_eleven >= 1) // still one or more ACE's counted as one
			isSoft = true;
		// 4) fill the flag isBlackJack
		if(my_open.size() == 2 && ret == 21 && ace_as_eleven == 1)
			isBlackJack = true;
		return ret;
	}
}












