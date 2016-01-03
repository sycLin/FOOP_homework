package foop;
import java.lang.*;
import java.util.*;

class Deck {

	/**
	 * storing the content of the deck
	 */
	private ArrayList<Card> cards;

	/**
	 * which card index to return next
	 */
	private int current_index;

	/**
	 * Constructor initialized the instance variables
	 */
	public Deck() {
		current_index = 0;
		cards = new ArrayList<Card>();
	}
	
	/**
	 * open a new deck of 52 standard cards
	 */
	public void open() {
		// first, remove all cards in the current deck
		cards.clear();
		// set current_index to 0
		current_index = 0;
		// add 52 cards
		for(int i=0; i<52; i++) {
			// determine the suit
			byte suit = (byte)(i / 13 + 1); // S = 1, H = 2, D = 3, C = 4
			// determine the value
			byte value = (byte)(i % 13 + 1); // A = 1, ..., K = 13
			// create the card and add to cards
			Card new_card = new Card(suit, value);
			cards.add(new_card);
		}
	}

	/**
	 * shuffles the deck
	 */
	public void shuffle() {
		Collections.shuffle(cards);
		// should I set the current_index to 0...?
	}

	/**
	 * adds a card to the deck
	 * @param c the card we would like to add to deck.
	 */
	public void addCard(Card c) {
		// copy the card
		Card new_card = new Card(c.getSuit(), c.getValue());
		// add to cards
		cards.add(new_card);
	}

	/**
	 * returns the next card in deck, starting from the first card initially.
	 * @return the card we want
	 */
	public Card getNextCard() {
		// handling exception
		if(remainingCount() <= 0) {
			shuffle();
			current_index = 0;
		}
		// return the desired card increase the counter
		return cards.get(current_index++);
	}

	/**
	 * returns how many cards are left in the deck
	 * @return the number of cards remaining in the deck
	 */
	public int remainingCount() {
		// current_index is actually "next card index" semantically
		// please notice this.
		return (cards.size() - current_index);
	}
}