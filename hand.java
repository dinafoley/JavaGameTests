package kz.edu.nu.cs.se.hw;

import java.util.ArrayList;

public class hand {
	private String name;
	private ArrayList<String> cards;
	
	public hand(String n) {
		name = n;
		 cards = new ArrayList();
	}
	
	public String getCard(int i) {
		return cards.get(i);
	}
	
	public void addToCards(String c) {
		cards.add(c);
	}
	public void deleteCard(String c) {
		int d = cards.indexOf(c);
		cards.remove(d);
	}
	public int positionCard(String c) {
		return cards.indexOf(c);
	}
	public String[] arrayCard() {
		String[] stringArray = cards.toArray(new String[0]);
		return stringArray;
	}
	public int numOfCards() {
		return cards.size();
	}
	

}
