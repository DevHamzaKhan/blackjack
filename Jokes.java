// File Name: Jokes.java
// Author: Hamza Khan
// Date: 2025-10-23
// Description: Static joke provider class

import java.util.*;
import java.io.*;

public class Jokes {
  private static ArrayList<String> jokeList = new ArrayList<String>();

  public static String nextJoke() {
    if (isListEmpty()) {
      loadJokes();
    }
    return getNextJokeFromList();
  }

  private static boolean isListEmpty() {
    return jokeList.isEmpty();
  }

  private static String getNextJokeFromList() {
    if (jokeList.isEmpty()) {
      return "No jokes available.";
    }
    return jokeList.remove(0);
  }

  public static void loadJokes() {
    jokeList = new ArrayList<>();
    try {
      loadJokesFromFile();
      Collections.shuffle(jokeList);
    } catch (FileNotFoundException e) {
      addDefaultJoke();
    }
  }

  private static void loadJokesFromFile() throws FileNotFoundException {
    File jokesFile = new File("jokes.txt");
    Scanner scanner = new Scanner(jokesFile);
    while (scanner.hasNextLine()) {
      String jokeLine = scanner.nextLine();
      jokeList.add(jokeLine);
    }
    scanner.close();
  }

  private static void addDefaultJoke() {
    jokeList.add("Why did the chicken cross the road? To get to the other side!");
  }
}
