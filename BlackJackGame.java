// File Name: BlackJackGame.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Main game loop and console I/O

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackGame {
  public static void main(String[] args) {
    System.out.println("WELCOME TO THE BLACKJACK JAVA GAME");
    System.out.println("===================================\n");
    
    Scanner sc = new Scanner(System.in);
    
    // Setup game
    ArrayList<Player> playerList = setupPlayers(sc);
    Dealer dealer = new Dealer();
    Deck deck = initializeDeck();
    
    // Display initial deck state
    displayInitialDeck(sc, deck);
    
    // Main game loop
    while (true) {
      if (!hasAnyPlayerWithMoney(playerList)) {
        break;
      }
      
      double[] bets = collectBetsFromPlayers(sc, playerList);
      if (shouldRefreshDeck(deck)) {
        resetDeck(deck);
      }
      
      dealCards(dealer, playerList, deck, bets);
      boolean[] inContention = playPlayerTurns(sc, dealer, playerList, deck);
      dealerPlays(dealer, playerList, deck, inContention);
      determineRoundResults(dealer, playerList, inContention);
      
      cleanupRound(dealer, playerList);
      displayBalances(playerList);
      displayDeckAfterRound(sc, deck);
      
      if (!promptPlayAgain(sc)) {
        break;
      }
    }
    
    displayFinalBalances(playerList);
    sc.close();
  }
  
  private static ArrayList<Player> setupPlayers(Scanner sc) {
      System.out.print("How many players are playing? ");
      int numPlayers = Math.max(1, sc.nextInt());
    ArrayList<Player> playerList = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      System.out.print("Player " + (i + 1) + ", how much money are you playing with? ");
      double initialMoney = sc.nextDouble();
      playerList.add(new Player(initialMoney));
    }
    return playerList;
  }
  
  private static Deck initializeDeck() {
    Deck deck = new Deck();
    deck.createFullDeck();
    return deck;
  }
  
  private static void displayInitialDeck(Scanner sc, Deck deck) {
    sc.nextLine();
    System.out.println("\nHere is the deck:");
    System.out.println(deck);
    
    // Shuffle before playing
    deck.shuffle();
    System.out.println("\nDeck has been shuffled.");
    System.out.println(deck);
  }
  
  private static boolean hasAnyPlayerWithMoney(ArrayList<Player> playerList) {
    for (Player currentPlayer : playerList) {
      if (currentPlayer.getMoney() > 0) {
        return true;
      }
    }
    return false;
  }
  
  private static double[] collectBetsFromPlayers(Scanner sc, ArrayList<Player> playerList) {
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
        System.out.println("Invalid bet. Please enter an amount between 0 and " + String.format("%.2f", currentPlayer.getMoney()) + ":");
        betAmount = sc.nextDouble();
      }
      bets[i] = betAmount;
    }
    return bets;
  }
  
  private static boolean shouldRefreshDeck(Deck deck) {
    return deck.cardsLeft() < 10;
  }
  
  private static void resetDeck(Deck deck) {
    System.out.println("New deck is being used.");
    deck.createFullDeck();
    deck.shuffle();
  }
  
  private static void dealCards(Dealer dealer, ArrayList<Player> playerList, Deck deck, double[] bets) {
    dealer.clearHand();
    for (int i = 0; i < playerList.size(); i++) {
      Player currentPlayer = playerList.get(i);
      currentPlayer.clearHand();
      currentPlayer.resetStand();
      currentPlayer.setBet(bets[i]);
    }
    
    dealer.addCard(deck.dealCard());
    dealer.addCard(deck.dealCard());
    for (int i = 0; i < playerList.size(); i++) {
      Player currentPlayer = playerList.get(i);
      if (currentPlayer.getBet() > 0) {
        currentPlayer.addCard(deck.dealCard());
        currentPlayer.addCard(deck.dealCard());
      }
    }
  }
  
  private static boolean[] playPlayerTurns(Scanner sc, Dealer dealer, ArrayList<Player> playerList, Deck deck) {
    boolean[] inContention = new boolean[playerList.size()];
    
    for (int i = 0; i < playerList.size(); i++) {
      Player currentPlayer = playerList.get(i);
      if (currentPlayer.getBet() <= 0) {
        inContention[i] = false;
        continue;
      }
      
      System.out.println("\n--- Player " + (i + 1) + "'s turn ---");
      
      while (true) {
        System.out.println("Your hand: " + currentPlayer.getHand(true) + " (" + currentPlayer.getHandTotal() + ")");
        System.out.println("Dealer's hand: " + dealer.getHand(false));
        
        // Check for natural blackjack
        if (currentPlayer.getHandTotal() == 21 && currentPlayer.getHand().size() == 2) {
          if (dealer.getHandTotal() == 21 && dealer.getHand().size() == 2) {
            System.out.println("Both you and the dealer have blackjack. Tie.");
          } else {
            System.out.println("Blackjack! You win!");
            currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
            System.out.println(Jokes.nextJoke());
          }
          inContention[i] = false;
          break;
        }
        
        if (currentPlayer.isBusted()) {
          System.out.println("You busted! Your hand total is " + currentPlayer.getHandTotal());
          inContention[i] = false;
          break;
        }
        
        if (currentPlayer.getHandTotal() == 21 && currentPlayer.getHand().size() > 2) {
          System.out.println("You have 21.");
          currentPlayer.stand();
          inContention[i] = true;
          break;
        }
        
        System.out.println("Choose (1)Hit or (2)Stand?");
        int playerChoice = sc.nextInt();
        
        // Handle hit or stand decision
        if (playerChoice == 1) {
          currentPlayer.addCard(deck.dealCard());
          if (currentPlayer.isBusted()) {
            System.out.println("Your hand: " + currentPlayer.getHand(true) + " (" + currentPlayer.getHandTotal() + ")");
            System.out.println("You busted!");
            inContention[i] = false;
            break;
          }
        }
        
        if (playerChoice == 2) {
          currentPlayer.stand();
          inContention[i] = true;
          break;
        }
      }
    }
    
    return inContention;
  }
  
  private static void dealerPlays(Dealer dealer, ArrayList<Player> playerList, Deck deck, boolean[] inContention) {
    boolean dealerActive = false;
    for (int i = 0; i < playerList.size(); i++) {
      Player currentPlayer = playerList.get(i);
      if (currentPlayer.getBet() > 0 && !currentPlayer.isBusted() && inContention[i]) {
        dealerActive = true;
        break;
      }
    }
    
    System.out.println("\nDealer's cards: " + dealer.getHand(true) + " (" + dealer.getHandTotal() + ")");
    
    // Dealer plays according to casino rules
    while (dealerActive && dealer.getHandTotal() < 17) {
      dealer.addCard(deck.dealCard());
      System.out.println("Dealer hits and gets: " + dealer.getHand().get(dealer.getHand().size() - 1));
      System.out.println("Dealer's total is now: " + dealer.getHandTotal());
    }
  }
  
  private static void determineRoundResults(Dealer dealer, ArrayList<Player> playerList, boolean[] inContention) {
    for (int i = 0; i < playerList.size(); i++) {
      Player currentPlayer = playerList.get(i);
      if (currentPlayer.getBet() <= 0) continue;
      
      if (currentPlayer.isBusted()) {
        System.out.println("Player " + (i + 1) + ": The dealer wins.");
        currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
        System.out.println(Jokes.nextJoke());
      } else if (!inContention[i]) {
      } else if (dealer.isBusted()) {
        System.out.println("Player " + (i + 1) + ": Dealer busts! You win!");
        currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
        System.out.println(Jokes.nextJoke());
      } else {
        if (currentPlayer.getHandTotal() > dealer.getHandTotal()) {
            System.out.println("Player " + (i + 1) + ": You win!");
          currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
          System.out.println(Jokes.nextJoke());
        } else if (currentPlayer.getHandTotal() == dealer.getHandTotal()) {
          System.out.println("Player " + (i + 1) + ": Tie.");
        } else {
            System.out.println("Player " + (i + 1) + ": You lose.");
          currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
          System.out.println(Jokes.nextJoke());
        }
      }
    }
  }
  
  private static void cleanupRound(Dealer dealer, ArrayList<Player> playerList) {
    dealer.clearHand();
    for (Player p : playerList) {
      p.clearHand();
      p.resetStand();
    }
  }
  
  private static void displayBalances(ArrayList<Player> playerList) {
    for (int i = 0; i < playerList.size(); i++) {
      System.out.println("Player " + (i + 1) + " now has $" + String.format("%.2f", playerList.get(i).getMoney()));
    }
  }
  
  private static void displayDeckAfterRound(Scanner sc, Deck deck) {
    sc.nextLine();
    System.out.println("\nEnd of round deck:");
    System.out.println(deck);
  }
  
  private static boolean promptPlayAgain(Scanner sc) {
    System.out.println("Play again? (y/n)");
    String keepPlaying = sc.next();
    return keepPlaying.equalsIgnoreCase("y");
  }
  
  private static void displayFinalBalances(ArrayList<Player> playerList) {
    for (int i = 0; i < playerList.size(); i++) {
      System.out.println("Player " + (i + 1) + ", you are leaving with $" + String.format("%.2f", playerList.get(i).getMoney()));
    }
  }
}
