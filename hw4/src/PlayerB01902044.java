package foop;
import java.lang.*;
import java.util.*;

class PlayerB01902044 extends Player {

	/* there are some methods defined in foop.Player that should remain unchanged */

	// void decrease_chips(double diff);
	// protected double get_chips();
	// void increase_chips(double diff);

	/* there are some abstract methods defined in foop.Player for us to override */

	public PlayerB01902044(int chips) {
		super(chips);
	}
	
	@Override
	public boolean buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		System.err.println("warning: PlayerB01902044.buy_insurance() is not yet implemented.");
		return false;
	}

	@Override
	public boolean do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		System.err.println("warning: PlayerB01902044.do_double() is not yet implemented.");
		return false;
	}

	@Override
	public boolean do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		System.err.println("warning: PlayerB01902044.do_split() is not yet implemented.");
		return false;
	}

	@Override
	public boolean do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		System.err.println("warning: PlayerB01902044.do_split() is not yet implemented.");
		return false;
	}

	@Override
	public boolean hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table) {
		System.err.println("warning: PlayerB01902044.hit_me() is not yet implemented.");
		return false;
	}

	@Override
	public int make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position) {
		// System.err.println("warning: PlayerB01902044.make_bet() is not yet implemented.");
		return 1;
	}

	@Override
	public java.lang.String toString() {
		// System.err.println("warning: PlayerB01902044.toString() is not yet implemented.");
		return "having " + get_chips() + " chips.";
	}
}