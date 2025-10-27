// File Name: GameController.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Main game loop and console I/O

import java.util.ArrayList;
import java.util.Scanner;

public class GameController {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    // Setup game participants  
    System.out.print("How many players are playing? ");
    int numPlayers = Math.max(1, sc.nextInt());
    ArrayList<Player> playerList = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      System.out.print("Player " + (i + 1) + ", how much money are you starting with? ");
      double initialMoney = sc.nextDouble();
      playerList.add(new Player(initialMoney));
    }
    Dealer dealer = new Dealer();

    // Create and prepare deck
    Deck deck = new Deck();
    deck.createFullDeck();

    // 1) Print a deck of cards at the beginning of the game (for Ms. Strelkovska).
    sc.nextLine();
    System.out.print("\nPress Enter to view deck...");
    sc.nextLine();
    System.out.println("Here is the deck:");
    System.out.println(deck);

    // 2) Shuffle and print the deck again (for Ms. Strelkovska).
    deck.shuffle();
    System.out.println("\nDeck has been shuffled.");
    System.out.print("Press Enter to view shuffled deck...");
    sc.nextLine();
    System.out.println(deck);

    // Continue rounds while at least one player has money left
    while (true) {
      boolean atLeastOneRich = false;
      for (Player currentPlayer : playerList) {
        if (currentPlayer.getMoney() > 0) {
          atLeastOneRich = true;
          break;
        }
      }
      if (!atLeastOneRich) {
        break;
      }

      // 11) Ask players how much money they are willing to risk and update the amount
      // according to winning or losing (optional).
      double[] bets = new double[playerList.size()];
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        if (currentPlayer.getMoney() <= 0) {
          bets[i] = 0;
          continue;
        }
        System.out.println("\nPlayer " + (i + 1) + ", you have $" + String.format("%.2f", currentPlayer.getMoney()) + ", how much would you like to bet?");
        double betAmount = sc.nextDouble();
        while (betAmount > currentPlayer.getMoney() || betAmount <= 0) {
          System.out.println("Invalid bet. Enter an amount between 0 and " + String.format("%.2f", currentPlayer.getMoney()) + ":");
          betAmount = sc.nextDouble();
        }
        bets[i] = betAmount;
      }

      // 8) Continue playing with the same deck. Introduce a new deck if less than 10
      // cards are left in a deck of cards.
      if (deck.cardsLeft() < 10) {
        System.out.println("New deck is being used.");
        deck.createFullDeck();
        deck.shuffle();
      }

      // 17) Don't give a dealer responsibility for distributing cards and calling who
      // won. The deck provides cards to the player(s) and the dealer.
      dealer.clearHand();
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        currentPlayer.clearHand();
        currentPlayer.resetStand();
        currentPlayer.setBet(bets[i]);
      }

      // Deal initial cards: dealer and each active player get two
      dealer.addCard(deck.dealCard());
      dealer.addCard(deck.dealCard());
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        if (currentPlayer.getBet() > 0) {
          currentPlayer.addCard(deck.dealCard());
          currentPlayer.addCard(deck.dealCard());
        }
      }
      // 16) After distributing 2 cards - ask the players if they want a card. After
      // everyone who wants one gets a card, ask again.
      boolean[] inContention = new boolean[playerList.size()]; // track players still playing
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        if (currentPlayer.getBet() <= 0) {
          inContention[i] = false;
          continue; // skip players with no bet
        }

        System.out.println("\n--- Player " + (i + 1) + "'s turn ---");
        while (true) {
          // 3) Show players' cards and total all the time.
          System.out.println("Your hand: " + currentPlayer.getHand(true) + " (" + currentPlayer.getHandTotal() + ")");

          // 4) Show the dealer's cards face down
          System.out.println("Dealer's hand: " + dealer.getHand(false));

          // Check for natural blackjack
          if (currentPlayer.getHandTotal() == 21 && currentPlayer.getHand().size() == 2) {
            if (dealer.getHandTotal() == 21 && dealer.getHand().size() == 2) {
              System.out.println("Both you and the dealer have blackjack. Tie.");
            } else {
              System.out.println("Blackjack! You win!");
              currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
              // 10) Every time a player or dealer wins – display a joke from the Joke class.
              System.out.println(Jokes.nextJoke());
            }
            inContention[i] = false; // player's turn is over
            break;
          }

          if (currentPlayer.isBusted()) {
            System.out.println("You busted! Your hand total is " + currentPlayer.getHandTotal());
            inContention[i] = false; // player is out
            break;
          }

          // Player hits 21, let dealer play
          if (currentPlayer.getHandTotal() == 21 && currentPlayer.getHand().size() > 2) {
            System.out.println("You have 21.");
            currentPlayer.stand();
            inContention[i] = true;
            break;
          }

          System.out.println("Would you like to (1)Hit or (2)Stand?");
          int playerChoice = sc.nextInt();

          // Player hits
          if (playerChoice == 1) {
            currentPlayer.addCard(deck.dealCard());
            if (currentPlayer.isBusted()) {
              System.out.println("Your hand: " + currentPlayer.getHand(true) + " (" + currentPlayer.getHandTotal() + ")");
              System.out.println("You busted!");
              inContention[i] = false;
              break;
            }
          }

          // Player stands
          if (playerChoice == 2) {
            // 15) If the player picked a "stand" - don't ask (hit or stand?) again - the
            // player cannot hit after standing.
            currentPlayer.stand();
            inContention[i] = true;
            break;
          }
        }
      }

      // Check if dealer needs to play
      boolean dealerActive = false;
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        if (currentPlayer.getBet() > 0 && !currentPlayer.isBusted() && inContention[i]) {
          dealerActive = true;
          break;
        }
      }

      // 5) Show dealer cards and cards' total
      System.out.println("\nDealer's cards: " + dealer.getHand(true) + " (" + dealer.getHandTotal() + ")");

      // 14) Don't ask the dealer to hit or stand; use casino rules (hit 16 or below,
      // stand at 17+).
      while (dealerActive && dealer.getHandTotal() < 17) {
        dealer.addCard(deck.dealCard());
        System.out.println("Dealer hits and gets: " + dealer.getHand().get(dealer.getHand().size() - 1));
        System.out.println("Dealer's total is now: " + dealer.getHandTotal());
      }

      // Determine results for each player vs dealer
      for (int i = 0; i < playerList.size(); i++) {
        Player currentPlayer = playerList.get(i);
        if (currentPlayer.getBet() <= 0) continue;

        if (currentPlayer.isBusted()) {
          System.out.println("Player " + (i + 1) + ": Dealer wins.");
          currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
          System.out.println(Jokes.nextJoke());
        } else if (!inContention[i]) {
          // already resolved (blackjack or bust)
        } else if (dealer.isBusted()) {
          System.out.println("Player " + (i + 1) + ": Dealer busts! You win!");
          currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
          System.out.println(Jokes.nextJoke());
        } else {
          if (currentPlayer.getHandTotal() > dealer.getHandTotal()) {
            System.out.println("Player " + (i + 1) + ": You win the hand!");
            currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
            System.out.println(Jokes.nextJoke());
          } else if (currentPlayer.getHandTotal() == dealer.getHandTotal()) {
            System.out.println("Player " + (i + 1) + ": Tie.");
          } else {
            System.out.println("Player " + (i + 1) + ": You lose the hand.");
            currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
            System.out.println(Jokes.nextJoke());
          }
        }
      }

      // Reset all hands
      dealer.clearHand();
      for (Player p : playerList) {
        p.clearHand();
        p.resetStand();
      }

      // Show current balances
      for (int i = 0; i < playerList.size(); i++) {
        System.out.println("Player " + (i + 1) + " now has $" + String.format("%.2f", playerList.get(i).getMoney()));
      }

      // 6) Display the deck after every round (for Ms. Strelkovska).
      sc.nextLine();
      System.out.print("\nPress Enter to view end of round deck...");
      sc.nextLine();
      System.out.println("End of round deck:");
      System.out.println(deck);

      // 7) Ask if the user would like to play again.
      System.out.println("Play again? (y/n)");
      String keepPlaying = sc.next();
      if (!keepPlaying.equalsIgnoreCase("y")) {
        break;
      }
    }

    // Final balances
    for (int i = 0; i < playerList.size(); i++) {
      System.out.println("Player " + (i + 1) + ", you are leaving with $" + String.format("%.2f", playerList.get(i).getMoney()));
    }
    sc.close();
  }
}
