import java.util.*;

class Main {

  public static void main(String[] args) {

    // create Scanner
    Scanner scan = new Scanner(System.in);

    // prime the while loop
    boolean userquit = false;

    // loop to allow the user to play multiple games
    while (!userquit) {

      // determine if it is a 1-player or 2-player game
      System.out.print("How many players (1 or 2)? ");
      String tmp = scan.nextLine();
      System.out.println("");
      int numPlayers = 0;
      if (tmp.equals("1")) {
        numPlayers = 1;
      }
      if (tmp.equals("2")) {
        numPlayers = 2;
      }
      // if an invalid number of players was entered, treat that as if the player quit the game
      if (numPlayers != 1 && numPlayers != 2) {
        userquit = true;
      }

      // randomize the vowel rules
      char doubleVowel = 'a';
      char quadVowel = 'e';
      char halfVowel = 'i';
      char subtwoVowel = 'o';
      char addtwoVowel = 'u';
      char tmpchr;
      Random rand = new Random();
      for (int i=1; i<=25; i++) {
        int r = rand.nextInt(5);
        if (r==1) {
          tmpchr = quadVowel;
          quadVowel = doubleVowel;
          doubleVowel = tmpchr;
        }
        if (r==2) {
          tmpchr = halfVowel;
          halfVowel = quadVowel;
          quadVowel = tmpchr;
        }
        if (r==3) {
          tmpchr = subtwoVowel;
          subtwoVowel = halfVowel;
          halfVowel = tmpchr;
        }
        if (r==4) {
          tmpchr = addtwoVowel;
          addtwoVowel = subtwoVowel;
          subtwoVowel = tmpchr;
        }
      }
      
      // initialize game variables
      int totalScore = 0;
      int totalScore1 = 0;
      int totalScore2 = 0;

      // initialize loop variables
      int stroke = 0;
      int player = 0;

      // loop through each player and each stroke
      while (!userquit && totalScore != 100) {

        // select the next player and get their score
        player++;
        if (player > numPlayers) {
          player = 1;
        }
        if (player == 1) {
          totalScore = totalScore1;
        } else {
          totalScore = totalScore2;
        }
        // move to the next stroke if we're on the first player
        if (player == 1) {
          stroke += 1;
        }

        // ask for the next play
        if (numPlayers > 1) {
          System.out.println("Player " + player + "'s turn...");
        }
        System.out.print("Next sentence (q to quit): ");
        String nextTry = scan.nextLine();

        if (nextTry.equals("q")) {

          // user entered just q to quit, so don't do anything else
          userquit = true;

        } else {

          // calculate score
          int thisScore = 0;
          if (nextTry.length() > 0) {
            thisScore = parseSentence(nextTry, doubleVowel, quadVowel, halfVowel, subtwoVowel, addtwoVowel);
            // add or subtract from total score based on which direction we need to go to get to 100
            if (totalScore < 100) {
              totalScore += thisScore;
            } else {
              totalScore -= thisScore;
            }
            // save off new score in player's score variable
            if (player == 1) {
              totalScore1 = totalScore;
            } else {
              totalScore2 = totalScore;
            }
          }

          // print stroke results
          System.out.println("Stroke " + stroke + ": \"" + nextTry + "\" = " + thisScore + " points");
          if (totalScore == 100) {
            // player won!
            System.out.println("Congratulations, you won with only " + stroke + " strokes!");
          } else {
            // determine how far below (or above) 100 the player is
            int diff = 100 - totalScore;
            if (diff < 0) {
              // player is above 100
              diff *= -1;
              System.out.println("Your total is " + totalScore + " points.  You overshot your goal by " + diff + " points!");
            } else {
              // player is below 100
              System.out.println("Your total is " + totalScore + " points, " + diff + " from the goal!");
            }

          }

          System.out.println("");

        }

        // end of loop for each player/stroke
      }

      if (userquit) {

        // user decide to quite the game
        System.out.println("Giving up so soon?  I didn't take you for a quitter.");

      } else {

        // ask if user wants to play again
        System.out.print("Would you like to play again? ");
        userquit = true;
        String askQuit = scan.nextLine();
        if (askQuit.length() > 0) {
          if (askQuit.toLowerCase().charAt(0) == 'y') {
            userquit = false;
          }
        }

      }

    }

    scan.close();

  }

  public static int parseSentence(String s, char doubleVowel, char quadVowel, char halfVowel, char subtwoVowel, char addtwoVowel) {
 
    // initialize running total (v)
    int v = 0;
    String checkWord = "";
    String results = "";
 
    // remove spaces from beginning and end
    s = s.trim();

    // loop through sentence as long as there is something to process
    while (s.length() > 0) {

      // find first space
      int sp = s.indexOf(' ');

      if (sp > 0) {

        // next word is from the beginning of the string to just before the first space
        checkWord = s.substring(0,sp);
        // strip that first word off our string to be checked, and again remove any spaces from beginning and end
        s = s.substring(sp+1).trim();

      } else {

        // no spaces, so check the entire line
        checkWord = s;
        // clear our string to be checked so no further checking will be done
        s = "";

      }

      // if we have a word to score, get its score and add that score to our running total (v)
      if (checkWord.length() > 0) {
        int wordval = parseWord(checkWord, doubleVowel, quadVowel, halfVowel, subtwoVowel, addtwoVowel);
        v += wordval;
        results += "\"" + checkWord + "\": " + wordval+ " points, "; 
      }

    }

    results += "sentence value: " + v + " points";
    //System.out.println (results);
    return v;

  }

  public static int parseWord(String w, char doubleVowel, char quadVowel, char halfVowel, char subtwoVowel, char addtwoVowel) {

    // every word starts with a score of 1
    int v = 1;

    // loop through each character in the word
    for (int i=0; i<w.length(); i++) {
 
      // get the next character into c
      char c = w.charAt(i);

      // make it lowercase
      if (c>='A' && c<='Z') {
        c -= ('A' - 'a');
      }
      // if c is in the alphabet...
      if ((c>='a' && c<='z')) {
        // if c is a vowel
        if (c=='a' || c=='e' || c=='i' || c=='o' || c=='u') {
          // it's a vowel: apply the appropriate rule
          boolean useVowelRules = false;
          if (!useVowelRules) {
            v *= 2;
          } else {
            if (c == doubleVowel) {
              v *= 2;
            }
            if (c == quadVowel) {
              v *= 4;
            }
            if (c == halfVowel) {
              v /= 2;
            }
            if (c == subtwoVowel) {
              v -= 2;
            }
            if (c == addtwoVowel) {
              v += 2;
            }
          }
        } else {
          // it's a consonant: add one to the running word total
          v += 1;
        }
      }

    }

    return v;

  }

}