// File Name: Deck.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Manages deck creation, shuffling, and card dealing operations

import java.util.*;

public class Deck {
  private ArrayList<Card> cards;

  public Deck() {
    this.cards = new ArrayList<Card>();
  }

  public int cardsLeft() {
    return cards.size();
  }

  public Card dealCard() {
    return cards.remove(0);
  }

  public void shuffle() {
    Collections.shuffle(this.cards);
  }

  public void createFullDeck() {
    this.cards.clear();
    String[] suits = { "♥", "♦", "♣", "♠" };
    String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

    for (int i = 0; i < suits.length; i++) {
      String currentSuit = suits[i];
      addCardsForSuit(currentSuit, ranks);
    }
  }

  private void addCardsForSuit(String suit, String[] ranks) {
    for (int i = 0; i < ranks.length; i++) {
      this.cards.add(new Card(suit, ranks[i]));
    }
  }

  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.cards.size(); i++) {
      result.append(this.cards.get(i).toString());
      if (i < this.cards.size() - 1) {
        result.append(" | ");
      }
    }
    result.append("\nNumber of cards in Deck: ").append(this.cards.size());
    return result.toString();
  }
}
