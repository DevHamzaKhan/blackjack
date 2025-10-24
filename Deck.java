// Alec Li - ICS4U - Blackjack Project - 2025-10-24 - Standard 52-card deck

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  private ArrayList<Card> cards;

  public Deck() {
    this.cards = new ArrayList<Card>();
  }

  public void createFullDeck() {
    this.cards.clear();
    String[] suits = { "♥", "♦", "♣", "♠" };
    String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    int[] values = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11 };

    for (String suit : suits) {
      for (int i = 0; i < ranks.length; i++) {
        this.cards.add(new Card(suit, ranks[i], values[i]));
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(this.cards);
  }

  public Card dealCard() {
    return cards.remove(0);
  }

  public int cardsLeft() {
    return cards.size();
  }

  public String toString() {
    String cardListOutput = "";
    for (Card aCard : this.cards) {
      cardListOutput += "\n " + aCard.toString();
    }
    cardListOutput += "\nTotal cards: " + this.cards.size();
    return cardListOutput;
  }
}
