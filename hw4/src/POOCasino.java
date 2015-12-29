package foop;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;

public class POOCasino {

	private static ArrayList<Player> players;
	public static int current_round;
	private static int total_round;
	private static int initial_chips;

	/**
	 * This is the main function
	 * @param argv the arguments, array of string.
	 */
	public static void main(String argv[]) {

		/* initialization of some instance variables */
		players = new ArrayList<Player>();

		/* step 1: initializing the players from arguments */
		// exec cmd: $java POOCasino nRound nChip Player1 Player2 Player3 Player4
		total_round = Integer.parseInt(argv[0]);
		initial_chips = Integer.parseInt(argv[1]);
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

		// test whether it succeeded
		for(int i=0; i<players.size(); i++) {
			System.out.println("Player " + i + " has: " + players.get(i).get_chips() + " chips.");
		}
		
		/* testing command-line arguments */
		
		// System.out.println("===== print out all arguments =====");
		// for(String s: argv) {
		// 	System.out.print(s + " ");
		// }
		// System.out.println();
		
		/* testing Card class */

		// System.out.println("===== testing Card class =====");
		// Card myCard = new Card(Card.CLUB, Card.VALUE_LOWER);
		// System.out.println("a new card is created");
		// System.out.println("Its suit: " + myCard.getSuit());
		// System.out.println("Its value: " + myCard.getValue());

		/* testing Player class */
		
		// System.out.println("===== testing Player class =====");
		// Player myPlayer = new PlayerB01902044(5566);
		// System.out.println("a new player is created with 5566 chips");
		// System.out.println("he/she has: " + myPlayer.get_chips() + " chips.");
		// try {
		// 	myPlayer.increase_chips(100);
		// } catch(Player.NegativeException e) {
		// 	System.out.println("QAQ... illegal increase_chips() called with negative value.");
		// }
		// System.out.println("now we tried to increase the number of chips he/she has by 100.");
		// System.out.println("he/she now has: " + myPlayer.get_chips() + " chips.");

	}

}

