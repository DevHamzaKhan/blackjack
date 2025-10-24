import java.util.Scanner;

public class GameController {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Initialize characters
    System.out.print("How much money are you starting with? ");
    double startingMoney = scanner.nextDouble();
    Player player = new Player(startingMoney);
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

    while (player.getMoney() > 0) {
      // 11) Ask players how much money they are willing to risk and update the amount
      // according to winning or losing (optional).
      System.out.println("\nYou have $" + player.getMoney() + ", how much would you like to bet?");
      double playerBet = scanner.nextDouble();
      if (playerBet > player.getMoney()) {
        System.out.println("You cannot bet more than you have. Please leave.");
        break;
      }

      boolean endRound = false;

      // 8) Continue playing with the same deck. Introduce a new deck if less than 10
      // cards are left in a deck of cards.
      if (playingDeck.cardsLeft() < 10) {
        System.out.println("New deck is being used.");
        playingDeck.createFullDeck();
        playingDeck.shuffle();
      }

      // 17) Don’t give a dealer responsibility for distributing cards and calling who
      // won. The deck provides cards to the player(s) and the dealer.
      player.setBet(playerBet);
      player.addCard(playingDeck.dealCard());
      dealer.addCard(playingDeck.dealCard());
      player.addCard(playingDeck.dealCard());
      dealer.addCard(playingDeck.dealCard());

      // 16) After distributing 2 cards - ask the players if they want a card. After
      // everyone who wants one gets a card, ask again.
      while (true) {
        // 3) Show players’ cards and total all the time.
        System.out.println("Your hand: " + player.getHandAsString(true) + " (" + player.getHandTotal() + ")");

        // 4) Show the dealer's cards face down. ( or one card and other face down).p
        System.out.println("Dealer's hand: " + dealer.getHandAsString(false));

        if (player.isBusted()) {
          System.out.println("You busted! Your hand total is " + player.getHandTotal());
          endRound = true;
          break;
        }

        if (player.getHandTotal() == 21) {
          System.out.println("Blackjack! You win!");
          player.setMoney(player.getMoney() + player.getBet());
          // 10) Every time a player or dealer wins – display a joke from the Joke class.
          System.out.println(Jokes.nextJoke());
          endRound = true;
          break;
        }

        System.out.println("Would you like to (1)Hit or (2)Stand?");
        int response = scanner.nextInt();

        if (response == 1) {
          player.addCard(playingDeck.dealCard());
          if (player.isBusted()) {
            System.out.println("Your hand: " + player.getHandAsString(true) + " (" + player.getHandTotal() + ")");
            System.out.println("You busted!");
            endRound = true;
            break;
          }
        }

        if (response == 2) {
          // 15) If the player picked a "stand" - don’t ask (hit or stand?) again - the
          // player cannot hit after standing.
          player.stand();
          break;
        }
      }

      // 5) Show dealer cards and cards’ total when the game is over.
      System.out.println("Dealer's cards: " + dealer.getHandAsString(true) + " (" + dealer.getHandTotal() + ")");

      // 14) Don’t ask the dealer to hit or stand; use casino rules (hit 16 or below,
      // stand at 17+).
      while (dealer.getHandTotal() < 17 && !endRound) {
        dealer.addCard(playingDeck.dealCard());
        System.out.println("Dealer hits and gets: " + dealer.getHand().get(dealer.getHand().size() - 1));
        System.out.println("Dealer's total is now: " + dealer.getHandTotal());
      }

      if (dealer.isBusted() && !endRound) {
        System.out.println("Dealer busts! You win!");
        player.setMoney(player.getMoney() + player.getBet());
        // 10) Every time a player or dealer wins – display a joke from the Joke class.
        System.out.println(Jokes.nextJoke());
        endRound = true;
      }

      // 12) In case both players busted, the dealer wins even if the dealer busted as
      // well.
      if (player.isBusted()) {
        System.out.println("Dealer wins.");
        player.setMoney(player.getMoney() - player.getBet());
        // 10) Every time a player or dealer wins – display a joke from the Joke class.
        System.out.println(Jokes.nextJoke());
        endRound = true;
      } else if (!endRound) {
        if (player.getHandTotal() > dealer.getHandTotal()) {
          System.out.println("You win the hand!");
          player.setMoney(player.getMoney() + player.getBet());
          // 10) Every time a player or dealer wins – display a joke from the Joke class.
          System.out.println(Jokes.nextJoke());
        } else if (player.getHandTotal() == dealer.getHandTotal()) {
          System.out.println("Push.");
        } else {
          System.out.println("You lose the hand.");
          player.setMoney(player.getMoney() - player.getBet());
          // 10) Every time a player or dealer wins – display a joke from the Joke class.
          System.out.println(Jokes.nextJoke());
        }
      }

      player.clearHand();
      dealer.clearHand();
      player.resetStand();

      // 6) Display the deck after every round (for Ms. Strelkovska).
      scanner.nextLine();
      System.out.print("\nPress Enter to view end of round deck...");
      scanner.nextLine();
      System.out.println("End of round deck:");
      System.out.println(playingDeck);

      System.out.println("You now have $" + player.getMoney());

      // 7) Ask if the user would like to play again.
      System.out.println("Play again? (y/n)");
      String playAgain = scanner.next();
      if (!playAgain.equalsIgnoreCase("y")) {
        break;
      }
    }

    System.out.println("Game over! You are leaving with $" + player.getMoney());
    scanner.close();
  }
}
