import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Jokes {
  private static ArrayList<String> jokes = new ArrayList<String>();

  public static void loadJokes() {
    jokes.clear();
    try {
      File jokesFile = new File("jokes.txt");
      Scanner fileReader = new Scanner(jokesFile);
      while (fileReader.hasNextLine()) {
        String data = fileReader.nextLine();
        jokes.add(data);
      }
      fileReader.close();
      Collections.shuffle(jokes);
    } catch (FileNotFoundException e) {
      jokes.add("Why did the chicken cross the road? To get to the other side!");
    }
  }

  public static String nextJoke() {
    if (jokes.isEmpty()) {
      loadJokes();
    }
    if (jokes.isEmpty()) {
      return "No jokes available.";
    }
    return jokes.remove(0);
  }
}
