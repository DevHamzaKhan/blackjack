import java.util.ArrayList;
import java.util.Random;

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
    ArrayList<Card> tempDeck = new ArrayList<Card>();
    Random random = new Random();
    int randomCardIndex = 0;
    int originalSize = this.cards.size();
    for (int i = 0; i < originalSize; i++) {
      randomCardIndex = random.nextInt(this.cards.size());
      tempDeck.add(this.cards.get(randomCardIndex));
      this.cards.remove(randomCardIndex);
    }
    this.cards = tempDeck;
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
