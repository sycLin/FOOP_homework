package foop;
import java.lang.*;
import java.util.*;
import foop.*;

public class PlayerB01902078 extends Player {
	int lastBet;
	double lastChips;
	int[] cardCount = new int[13];
	boolean wash;
	// 
	// inherited abstract methods
	// 
	public PlayerB01902078(int chips) {
		super(chips);
		this.lastBet = 5;
		this.lastChips = chips;
		this.renewCardCount();
		this.wash = false;
	}

	private void renewCardCount() {
		for (int i=0; i<this.cardCount.length; i++) {
			this.cardCount[i] = 4;
		}
	}

	private void changeCardCount(ArrayList<Hand> table) {
		if (this.wash)
			this.wash = false;
		boolean bFlag = false;
		for (int i=0; i<table.size(); i++) {
			for (int j=0; j<table.get(i).getCards().size(); j++) {
				int value = table.get(i).getCards().get(j).getValue() - 1;
				this.cardCount[value] -= 1;
				if (this.cardCount[value] < 0) {
					this.renewCardCount();
					this.wash = true;
					bFlag = true;
					break;
				}
			}
			if (bFlag) {
				break;
			}
		}
	}

	private void returnCardCount(ArrayList<Hand> table) {
		for (int i=0; i<table.size(); i++) {
			for (int j=0; j<table.get(i).getCards().size(); j++) {
				int value = table.get(i).getCards().get(j).getValue() - 1;
				this.cardCount[value] += 1;
			}
		}
	}

	private double probabilityN2Bust(int current_point) {
		int limit = 21 - current_point;
		int remainder = 0;
		int accept = 0;
		double n2b;
		for (int i=0; i<13; i++) {
			remainder += this.cardCount[i];
		}
		for (int i=0; i<13; i++) {
			if (i+1 >= 10 && 10 <= limit) {
				accept += this.cardCount[i];
			} else if (i+1 < 10 && i+1 <= limit) {
				accept += this.cardCount[i];
			} else {
				accept += 0;
			}
		}
		n2b = (double)accept/(double)remainder;
		return n2b;
	} 

	public boolean buy_insurance(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		return false;
	}

	public boolean do_double(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		return false;
	} 

	public boolean do_split(ArrayList<Card> my_open, Card dealer_open, ArrayList<Hand> current_table) {
		int v = my_open.get(0).getValue();
		int dv = dealer_open.getValue();
		if (v == 1) {
			return true;
		} else if (v == 10) {
			return false;
		} else if (dv >= 2 && dv<=9) {
			return true;
		} else {
			return false;
		}
	}

	public boolean do_surrender(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		return false;
	}

	public boolean hit_me(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		boolean re;
		ArrayList<Hand> tmp = new ArrayList<Hand>();
		tmp.add(my_open);
		this.changeCardCount(current_table);
		if (this.maximumOfHand(my_open.getCards()) <= 16) {
			re = true;
		} else {
			if (this.probabilityN2Bust(this.maximumOfHand(my_open.getCards())) > 0.7) {
				re = true;
			} else {
				re = false;
			}
		}
		if (!this.wash) {
			this.returnCardCount(current_table);
		}
		return re;
	}

	public int make_bet(ArrayList<Hand> last_table, int total_player, int my_position) {

		this.changeCardCount(last_table);

		if (this.get_chips() > this.lastChips) {
			if (this.lastBet >= this.get_chips()/4.0) {
				this.lastBet = this.lastBet;
			} else {
				this.lastBet += 5; 
			}
		} else {
			this.lastBet = 5;
		}	
		this.lastChips = this.get_chips();
		return this.lastBet;
		
	}

	public String toString() {
		return "B01902078";
	}

	private int maximumOfHand(ArrayList<Card> cards) {
		// 
		int total = 0;
		int count = 0;
		for (int i=0; i<cards.size(); i++) {
			int v = (int)cards.get(i).getValue();
			if (v == 1) {
				count += 1;
			}
		}
		for (int i=0; i<cards.size(); i++) {
			int v = (int)cards.get(i).getValue();
			if (v >= 10) {
				total += 10;
			} else if (v == 1) {
				total += 0;
			} else {
				total += v;
			}
		}
		for (int i=0; i<count; i++) {
			if (total+11 <= 21) {
				total += 11;
			} else {
				total += 1;
			}
		}
		return total;
	}

	// 
	// inherited concrete method
	// 

	//protected double get_chips()

	//public void increase_chips(double diff)

	//public void decrease_chips(double diff)
}