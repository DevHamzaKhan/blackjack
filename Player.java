// Alec Li - ICS4U - Blackjack Project - 2025-10-24 - Human player state and bets

public class Player extends Person {
  private double money;
  private double bet;
  private boolean isStanding;

  public Player(double money) {
    this.money = money;
    this.bet = 0;
    this.isStanding = false;
  }

  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
    this.money = money;
  }

  public double getBet() {
    return bet;
  }

  public void setBet(double bet) {
    this.bet = bet;
  }

  public boolean isStanding() {
    return isStanding;
  }

  public void stand() {
    this.isStanding = true;
  }

  public void resetStand() {
    this.isStanding = false;
  }
}
