import java.util.ArrayList;

public class Person {
  private ArrayList<Card> hand;

  public Person() {
    this.hand = new ArrayList<Card>();
  }

  public ArrayList<Card> getHand() {
    return hand;
  }

  public void addCard(Card card) {
    this.hand.add(card);
  }

  public int getHandTotal() {
    int total = 0;
    int aces = 0;
    for (Card card : hand) {
      total += card.getValue();
      if (card.getRank().equals("Ace")) {
        aces++;
      }
    }
    // 13) When totals are calculated consider that Ace values 1 or 11. Consider
    // more than one Ace.
    while (total > 21 && aces > 0) {
      total -= 10;
      aces--;
    }
    return total;
  }

  public void clearHand() {
    this.hand.clear();
  }

  public boolean isBusted() {
    return getHandTotal() > 21;
  }

  public String getHandAsString(boolean showAllCards) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hand.size(); i++) {
      if (i == 0 && !showAllCards) {
        sb.append("[Card is face down]");
      } else {
        sb.append(hand.get(i).toString());
      }
      if (i < hand.size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
}
