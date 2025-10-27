// File Name: Player.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Human player state and bets

public class Player extends Person {
  private boolean isStanding;
  private double bet;
  private double money;

  public Player(double money) {
    this.money = money;
    this.bet = 0;
    this.isStanding = false;
  }

  public void stand() {
    this.isStanding = true;
  }

  public boolean isStanding() {
    return isStanding;
  }

  public void resetStand() {
    this.isStanding = false;
  }

  public double getBet() {
    return bet;
  }

  public void setBet(double bet) {
    this.bet = bet;
  }

  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
    this.money = money;
  }
}
