// File Name: Dealer.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Manages card hand, calculates totals with ace adjustment, and checks bust status

import java.util.*;
 

public class Dealer {
  private ArrayList<Card> hand;

  public Dealer() {
    this.hand = new ArrayList<Card>();
  }

  public void addCard(Card card) {
    this.hand.add(card);
  }

  public void clearHand() {
    this.hand.clear();
  }

  public ArrayList<Card> getHand() {
    return hand;
  }

  public boolean isBusted() {
    return getHandTotal() > 21;
  }

  public int getHandTotal() {
    int sum = calculateRawTotal();
    return adjustForAces(sum);
  }

  private int calculateRawTotal() {
    int total = 0;
    for (Card card : hand) {
      total += card.getValue();
    }
    return total;
  }

  private int adjustForAces(int total) {
    int aces = countAces();
    while (total > 21 && aces > 0) {
      total -= 10;
      aces--;
    }
    return total;
  }

  private int countAces() {
    int count = 0;
    for (Card card : hand) {
      if (card.getRank().equals("A")) {
        count++;
      }
    }
    return count;
  }

  public String getHand(boolean showAllCards) {
    String output = "";
    for (int i = 0; i < hand.size(); i++) {
      output += formatCard(i, showAllCards);
      if (i < hand.size() - 1) {
        output += ", ";
      }
    }
    return output;
  }

  private String formatCard(int index, boolean showAll) {
    if (index == 0 && !showAll) {
      return "[Face down card]";
    }
    return hand.get(index).toString();
  }
}
