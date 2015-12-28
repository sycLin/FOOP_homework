package foop;
import java.lang.*;
import java.util.*;

public class POOCasino {
	/**
	 * This is the main function
	 * @param argv the arguments, array of string.
	 */
	public static void main(String argv[]) {
		
		/* testing command-line arguments */
		
		System.out.println("===== print out all arguments =====");
		for(String s: argv) {
			System.out.print(s + " ");
		}
		System.out.println();
		
		/* testing Card class */

		System.out.println("===== testing Card class =====");
		Card myCard = new Card(Card.CLUB, Card.VALUE_LOWER);
		System.out.println("a new card is created");
		System.out.println("Its suit: " + myCard.getSuit());
		System.out.println("Its value: " + myCard.getValue());

		/* testing Player class */
		
		System.out.println("===== testing Player class =====");
		Player myPlayer = new PlayerB01902044(5566);
		System.out.println("a new player is created with 5566 chips");
		System.out.println("he/she has: " + myPlayer.get_chips() + " chips.");
		try {
			myPlayer.increase_chips(100);
		} catch(Player.NegativeException e) {
			System.out.println("QAQ... illegal increase_chips() called with negative value.");
		}
		System.out.println("now we tried to increase the number of chips he/she has by 100.");
		System.out.println("he/she now has: " + myPlayer.get_chips() + " chips.");

	}

}

