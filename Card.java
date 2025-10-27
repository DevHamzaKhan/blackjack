// File Name: Card.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Playing card model

public class Card {
  private String suit;
  private String rank;
  private int value;

  public Card(String suit, String rank, int value) {
    this.suit = suit;
    this.rank = rank;
    this.value = value;
  }

  public String getSuit() {
    return suit;
  }

  public String getRank() {
    return rank;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String toString() {
    return rank + suit;
  }
}
