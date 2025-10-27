// Alec Li - ICS4U - Blackjack Project - 2025-10-24 - Main game loop and console I/O

import java.util.ArrayList;
import java.util.Scanner;

public class GameController {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Initialize players and dealer
    System.out.print("How many players are playing? ");
    int playerCount = Math.max(1, scanner.nextInt());
    ArrayList<Player> players = new ArrayList<>();
    for (int i = 0; i < playerCount; i++) {
      System.out.print("Player " + (i + 1) + ", how much money are you starting with? ");
      double startingMoney = scanner.nextDouble();
      players.add(new Player(startingMoney));
    }
    Dealer dealer = new Dealer();

    // Initialize deck
    Deck playingDeck = new Deck();
    playingDeck.createFullDeck();

    // 1) Print a deck of cards at the beginning of the game (for Ms. Strelkovska).
    scanner.nextLine();
    System.out.print("\nPress Enter to view deck...");
    scanner.nextLine();
    System.out.println("Here is the deck:");
    System.out.println(playingDeck);

    // 2) Shuffle and print the deck again (for Ms. Strelkovska).
    playingDeck.shuffle();
    System.out.println("\nDeck has been shuffled.");
    System.out.print("Press Enter to view shuffled deck...");
    scanner.nextLine();
    System.out.println(playingDeck);

    // Keep starting rounds as long as at least one player has money left
    while (true) {
      boolean anyHasMoney = false;
      for (Player p : players) {
        if (p.getMoney() > 0) {
          anyHasMoney = true;
          break;
        }
      }
      if (!anyHasMoney) {
        break;
      }

      // 11) Ask players how much money they are willing to risk and update the amount
      // according to winning or losing (optional).
      double[] roundBets = new double[players.size()];
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if (p.getMoney() <= 0) {
          roundBets[i] = 0;
          continue;
        }
        System.out.println("\nPlayer " + (i + 1) + ", you have $" + p.getMoney() + ", how much would you like to bet?");
        double playerBet = scanner.nextDouble();
        while (playerBet > p.getMoney() || playerBet <= 0) {
          System.out.println("Invalid bet. Enter an amount between 0 and " + p.getMoney() + ":");
          playerBet = scanner.nextDouble();
        }
        roundBets[i] = playerBet;
      }

      // 8) Continue playing with the same deck. Introduce a new deck if less than 10
      // cards are left in a deck of cards.
      if (playingDeck.cardsLeft() < 10) {
        System.out.println("New deck is being used.");
        playingDeck.createFullDeck();
        playingDeck.shuffle();
      }

      // 17) Don’t give a dealer responsibility for distributing cards and calling who
      // won. The deck provides cards to the player(s) and the dealer.
      // Reset hands and stands
      dealer.clearHand();
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        p.clearHand();
        p.resetStand();
        p.setBet(roundBets[i]);
      }

      // Initial deal: dealer gets two, each active player gets two
      dealer.addCard(playingDeck.dealCard());
      dealer.addCard(playingDeck.dealCard());
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if (p.getBet() > 0) {
          p.addCard(playingDeck.dealCard());
          p.addCard(playingDeck.dealCard());
        }
      }
      // 16) After distributing 2 cards - ask the players if they want a card. After
      // everyone who wants one gets a card, ask again.
      boolean[] stillContending = new boolean[players.size()]; // players who need dealer to play
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if (p.getBet() <= 0) {
          stillContending[i] = false;
          continue; // skipped this round
        }

        System.out.println("\n--- Player " + (i + 1) + "'s turn ---");
        while (true) {
          // 3) Show players’ cards and total all the time.
          System.out.println("Your hand: " + p.getHand(true) + " (" + p.getHandTotal() + ")");

          // 4) Show the dealer's cards face down. ( or one card and other face down)
          System.out.println("Dealer's hand: " + dealer.getHand(false));

          // Natural blackjack check
          if (p.getHandTotal() == 21 && p.getHand().size() == 2) {
            if (dealer.getHandTotal() == 21 && dealer.getHand().size() == 2) {
              System.out.println("Both you and the dealer have blackjack. Tie.");
            } else {
              System.out.println("Blackjack! You win!");
              p.setMoney(p.getMoney() + p.getBet());
              // 10) Every time a player or dealer wins – display a joke from the Joke class.
              System.out.println(Jokes.nextJoke());
            }
            stillContending[i] = false; // resolved immediately
            break;
          }

          if (p.isBusted()) {
            System.out.println("You busted! Your hand total is " + p.getHandTotal());
            stillContending[i] = false; // resolved (lost)
            break;
          }

          // If player reaches 21 via hits, stop asking and let dealer play
          if (p.getHandTotal() == 21 && p.getHand().size() > 2) {
            System.out.println("You have 21.");
            p.stand();
            stillContending[i] = true;
            break;
          }

          System.out.println("Would you like to (1)Hit or (2)Stand?");
          int response = scanner.nextInt();

          // Hit
          if (response == 1) {
            p.addCard(playingDeck.dealCard());
            if (p.isBusted()) {
              System.out.println("Your hand: " + p.getHand(true) + " (" + p.getHandTotal() + ")");
              System.out.println("You busted!");
              stillContending[i] = false;
              break;
            }
          }

          // Stand
          if (response == 2) {
            // 15) If the player picked a "stand" - don’t ask (hit or stand?) again - the
            // player cannot hit after standing.
            p.stand();
            stillContending[i] = true;
            break;
          }
        }
      }

      // Determine if dealer needs to play (at least one player is still in contention)
      boolean dealerMustPlay = false;
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if (p.getBet() > 0 && !p.isBusted() && stillContending[i]) {
          dealerMustPlay = true;
          break;
        }
      }

      // 5) Show dealer cards and cards’ total when the game is over (or before dealer plays).
      System.out.println("\nDealer's cards: " + dealer.getHand(true) + " (" + dealer.getHandTotal() + ")");

      // 14) Don’t ask the dealer to hit or stand; use casino rules (hit 16 or below,
      // stand at 17+).
      while (dealerMustPlay && dealer.getHandTotal() < 17) {
        dealer.addCard(playingDeck.dealCard());
        System.out.println("Dealer hits and gets: " + dealer.getHand().get(dealer.getHand().size() - 1));
        System.out.println("Dealer's total is now: " + dealer.getHandTotal());
      }

      // For players that are still contending, settle the results versus dealer
      for (int i = 0; i < players.size(); i++) {
        Player p = players.get(i);
        if (p.getBet() <= 0) continue;

        if (p.isBusted()) {
          System.out.println("Player " + (i + 1) + ": Dealer wins.");
          p.setMoney(p.getMoney() - p.getBet());
          System.out.println(Jokes.nextJoke());
        } else if (!stillContending[i]) {
          // already resolved (blackjack or bust handled earlier)
        } else if (dealer.isBusted()) {
          System.out.println("Player " + (i + 1) + ": Dealer busts! You win!");
          p.setMoney(p.getMoney() + p.getBet());
          System.out.println(Jokes.nextJoke());
        } else {
          if (p.getHandTotal() > dealer.getHandTotal()) {
            System.out.println("Player " + (i + 1) + ": You win the hand!");
            p.setMoney(p.getMoney() + p.getBet());
            System.out.println(Jokes.nextJoke());
          } else if (p.getHandTotal() == dealer.getHandTotal()) {
            System.out.println("Player " + (i + 1) + ": Tie.");
          } else {
            System.out.println("Player " + (i + 1) + ": You lose the hand.");
            p.setMoney(p.getMoney() - p.getBet());
            System.out.println(Jokes.nextJoke());
          }
        }
      }

      // Clear hands and reset
      dealer.clearHand();
      for (Player p : players) {
        p.clearHand();
        p.resetStand();
      }

      // Balances
      for (int i = 0; i < players.size(); i++) {
        System.out.println("Player " + (i + 1) + " now has $" + players.get(i).getMoney());
      }

      // 6) Display the deck after every round (for Ms. Strelkovska).
      scanner.nextLine();
      System.out.print("\nPress Enter to view end of round deck...");
      scanner.nextLine();
      System.out.println("End of round deck:");
      System.out.println(playingDeck);

      // 7) Ask if the user would like to play again.
      System.out.println("Play again? (y/n)");
      String playAgain = scanner.next();
      if (!playAgain.equalsIgnoreCase("y")) {
        break;
      }
    }

    // Game over balances
    for (int i = 0; i < players.size(); i++) {
      System.out.println("Player " + (i + 1) + ", you are leaving with $" + players.get(i).getMoney());
    }
    scanner.close();
  }
}
