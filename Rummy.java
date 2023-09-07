package kz.edu.nu.cs.se.hw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
 * Starter code for a class that implements the <code>PlayableRummy</code>
 * interface. A constructor signature has been added, and method stubs have been
 * generated automatically in eclipse.
 * 
 * Before coding you should verify that you are able to run the accompanying
 * JUnit test suite <code>TestRummyCode</code>. Most of the unit tests will fail
 * initially.
 * 
 * @see PlayableRummy
 * @see TestRummyCode
 *
 */
public class Rummy implements PlayableRummy {

	private String[] playerss;
	private Steps state;
	private List<String> deck;
	private List<String> discard;
	private int currentPlayer;
	//private ArrayList<ArrayList<String>> hand;
	private ArrayList<hand> hand;
	private ArrayList<ArrayList<String>> meld;
	boolean Rummy;
	private String top;

	public Rummy(String... players)throws RummyException {
		playerss = players;
		hand = new ArrayList<>(getNumPlayers());
		meld = new ArrayList<>();
		deck = new ArrayList<String>();
		discard = new ArrayList<String>();
		Rummy = false;
		
		for (String p : players) {
				hand.add(new hand(p));
		}
		for (int i = 0; i < getNumMelds(); i++) {
			meld.add(new ArrayList());
		}
		if(playerss.length<2) {
			throw new RummyException("NOT_ENOUGH_PLAYERS", 2);
		}
		if(playerss.length>6) {
			throw new RummyException("EXPECTED_FEWER_PLAYERS",8 );
		}
		state = Steps.WAITING;
		final String[] suits = new String[] { "C", "D", "H", "S", "M" };
		final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

		for (String suit : suits) {
			for (String rank : ranks) {

				deck.add(rank + suit);
			}
		}

	}

	@Override
	public String[] getPlayers() {
		return playerss;
	}

	@Override
	public int getNumPlayers() {
		return playerss.length;
	}

	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public int getNumCardsInDeck() {
		return deck.size();
	}

	@Override
	public int getNumCardsInDiscardPile() {
		return discard.size();
	}

	@Override
	public String getTopCardOfDiscardPile() {
		top = discard.get(0);
		return top;
	}

	@Override
	public String[] getHandOfPlayer(int player) throws RummyException{
		if(player>=getNumPlayers()) {
			throw new RummyException("NOT_VALID_INDEX_OF_PLAYER", 10);
		}
		String[] a = hand.get(player).arrayCard();
		return 	a;	
	}

	@Override
	public int getNumMelds() {
		return meld.size();
	}

	@Override
	public String[] getMeld(int i) throws RummyException {
		if (i >= getNumMelds()) {
			throw new RummyException("MELD OUT OF BOUNDS", 11);
		}
		String[] stringArray = meld.get(i).toArray(new String[0]);
		return stringArray;

	}

	@Override
	public void rearrange(String card) throws RummyException {
		if (state == Steps.WAITING) {
			int a = deck.indexOf(card);
			deck.remove(a);
			deck.add(0, card);
		} else {
			throw new RummyException("EXPECTED_WAITING_STEP", 3);
		}

	}

	@Override
	public void shuffle(Long l) throws RummyException {
		if (state == Steps.WAITING) {
			Random r = new Random(l);
			Collections.shuffle(deck, r);
		} else {
			throw new RummyException("EXPECTED_WAITING_STEP", 3);
		}

	}

	@Override
	public Steps getCurrentStep() {
		return state;
	}

	@Override
	public int isFinished() {
		if (state == Steps.FINISHED) {
			return currentPlayer;
		}
		return -1;
	}

	@Override
	public void initialDeal() throws RummyException {
		if (state == Steps.WAITING) {
			if (getNumPlayers() == 2) {
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 2; j++) {
					hand.get(j).addToCards(deck.remove(0));
					}
				}
			}
			if (getNumPlayers() == 3) {
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 3; j++) {
						hand.get(j).addToCards(deck.remove(0));
					}
				}

			}
			if (getNumPlayers() == 4) {
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 4; j++) {
						hand.get(j).addToCards(deck.remove(0));
					}

				}
			}
			if (getNumPlayers() == 5) {
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 5; j++) {
						hand.get(j).addToCards(deck.remove(0));
					}
				}
			}
			if (getNumPlayers() == 6) {

				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 6; j++) {
						hand.get(j).addToCards(deck.remove(0));
					}

				}
			}

			discard.add(deck.remove(0));
			currentPlayer = 0;
			state = Steps.DRAW;

		} else {
			throw new RummyException("EXPECTED_WAITING_STEP", 3);
		}
	}

	@Override
	public void drawFromDiscard() throws RummyException {
		if (state == Steps.DRAW) {
			String c = discard.get(0);
			discard.remove(0);
			hand.get(currentPlayer).addToCards(c);
			state = Steps.MELD;
		}else {
			throw new RummyException("EXPECTED_DRAW_STEP", 4);
		}

	}

	@Override
	public void drawFromDeck() throws RummyException {
		if (state == Steps.DRAW) {
				String c = deck.get(0);
				deck.remove(0);
				hand.get(currentPlayer).addToCards(c);
			state = Steps.MELD;
		} else {
			throw new RummyException("EXPECTED_DRAW_STEP", 4);
		}

	}

	@Override
	public void meld(String... cards) throws RummyException {
		if (state == Steps.MELD || state == Steps.RUMMY) {
			int size = hand.get(currentPlayer).numOfCards();
			//int size = getHandOfPlayer(currentPlayer).length;
			int j = 0;
			int b = 0;
			int a = 0;
			int meldNum = cards.length;
			//String[] inHand = getHandOfPlayer(currentPlayer);
			for (String card : cards) {
				//for(String h : hand.get(currentPlayer)  ) {
				for (int i = 0; i < size; i++) {
					String card2 = hand.get(currentPlayer).getCard(i);
					if (card2 == card) {
						j++;
					}
				}
			}
			if (meldNum == j) {
				char suit = cards[0].charAt(cards[0].length() - 1);
				String string = cards[0].substring(0, (cards[0].length() - 1));
				for (String card : cards) {
					if (suit == card.charAt(card.length() - 1)) {
						a++;
					}
					String str = card.substring(0, (card.length() - 1));
					if (string.equals(str)) {
						b++;
					}
				}
				
				if (b == meldNum || a == meldNum) {
					meld.add(new ArrayList(Arrays.asList(cards)));
					for (String card : cards) {
						hand.get(currentPlayer).deleteCard(card);
					}
					if (getHandOfPlayer(currentPlayer).length == 0) {
						state = Steps.FINISHED;
					}
					if(Rummy) {
						if(getHandOfPlayer(currentPlayer).length == 1) {
							state = Steps.FINISHED;
						}
					}
				}else {
					throw new RummyException("EXPECTED_CARDS", 7);
				}	
			}else {
				char suit = cards[0].charAt(cards[0].length() - 1);
				String string = cards[0].substring(0, (cards[0].length() - 1));
				for (String card : cards) {
					if (suit == card.charAt(card.length() - 1)) {
						a++;
					}
					String str = card.substring(0, (card.length() - 1));
					if (string.equals(str)) {
						b++;
					}
				}
				if ((b ==meldNum ||  a == meldNum )) {
					throw new RummyException("NOT_VALID_MELD", 1);
				
				}else {
					throw new RummyException("EXPECTED_CARDS", 7);
				}
			}
		}else {
			throw new RummyException("EXPECTED_MELD_STEP_OR_RUMMY_STEP", 15);
		}

	}

		@Override
		public void addToMeld(int meldIndex, String... cards) {
			if (state == Steps.MELD || state == Steps.RUMMY) {
				String fromMeld = meld.get(meldIndex).get(0);

				char suit = fromMeld.charAt(fromMeld.length() - 1);
				String string = fromMeld.substring(0, (fromMeld.length() - 1));
				for (String card : cards) {
					String str = card.substring(0, (card.length() - 1));
					if (suit == card.charAt(card.length() - 1)) {
						meld.get(meldIndex).add(card);	
						hand.get(currentPlayer).deleteCard(card);	
					}
					if (str.equals(string)) {
						meld.get(meldIndex).add(card);
						hand.get(currentPlayer).deleteCard(card);
					}
				}
				if (getHandOfPlayer(currentPlayer).length == 0) {
					state = Steps.FINISHED;
				}
			}
		}

		@Override
		public void declareRummy() throws RummyException{
			if (state == Steps.MELD ) {
				state = Steps.RUMMY;
				Rummy = true;
			}else {
				throw new  RummyException("EXPECTED_MELD_STEP", 5 );
			}

		}

		@Override
		public void finishMeld() throws RummyException {
			if (state == Steps.MELD || state == Steps.RUMMY) {
				state = Steps.DISCARD;
				if(Rummy!=false) {
					throw new RummyException ("RUMMY_NOT_DEMONSTRATED",16 );
				}
			}

		}

		@Override
		public void discard(String card) throws RummyException {
			if (state == Steps.DISCARD) {
				if (card == top) {
					throw new RummyException("NOT_VALID_DISCARD", 13);
				}
				if (hand.get(currentPlayer).positionCard(card) == -1) {
					throw new RummyException("EXPECTED_CARDS", 7);
				} else {
					
					hand.get(currentPlayer).deleteCard(card);
					discard.add(0, card);
				if (getHandOfPlayer(currentPlayer).length == 0) {
						state = Steps.FINISHED;
					} else {					
						int num = getNumPlayers();
						int cur = getCurrentPlayer();
						if (cur + 1 >= num) {
							currentPlayer = 0;
						} else {
							currentPlayer = currentPlayer + 1;
						}
						state = Steps.DRAW;
						if (getNumCardsInDeck() == 0) {
								Collections.reverse(discard);
								deck.addAll(discard) ;
								discard.clear();
								discard.add(deck.get(0));
								deck.remove(0);
						}
						
					}
				}
			} else {
				throw new RummyException("NOT IN DISCARD STATE", 6);
			}

		}

	}
