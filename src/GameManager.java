import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
    private String word = "";
    private char[] wordArray;                                               // stores word in a char array
    private int guessesRemaining;
    private boolean isGameOver;
    private char guess;                                                     // users input guess
    private boolean[] wordGuessedArray;                                     // an array storing which positions have been corectly guessed

    public GameManager() {
        startGame();
    }

    /**
     * Obtains a random word from the "wordlist.txt" file if there is no file called wordlist.txt then exit with a message
     * 
     * @return word selected for this game. 
     */
    public String getNewWord() {
        ArrayList<String> targetWords = new ArrayList<>();
        try {
            Scanner in = new Scanner(new File("wordlist.txt"));
            while(in.hasNext()){
                targetWords.add(in.next());
            }
            Random r = new Random();
            String word = targetWords.get(r.nextInt(targetWords.size())).toUpperCase();
            System.out.println("DEBUG | The target word is: \""+ word +"\"");
            in.close();
            return word;
        } catch(FileNotFoundException e) {
            System.err.println("Could not find file wordlist.txt containing all possible words.\nPlease make sure there is a wordlist.txt file");
            if (getWord().isEmpty()) System.exit(1);
            return null;
        }
    }

    /**
     * This starts a new game.  This is used on first use of the gamne but also if the play again button is clicked
     */
    public void startGame() {
        this.word = getNewWord();                                     // gets the word for the game, from wordlist.txt
        this.wordArray = word.toCharArray();                          // convert word to char array
        this.guessesRemaining = 7;                                    // sets the number of guesses the player can have at the start
        this.isGameOver = false;
        this.wordGuessedArray = new boolean[word.length()];           // an array storing which positions have been corectly guessed
    }

    /**
     * this creates a string of the currently correctly guessed letters in the correct positions in the word, with __'s where the letter
     * has not been guessed yet, this is used to display the current word guessing progress
     * @return string
     */
    public String getGuessDisplayText() {
        String guessDisplayText = " ";
        for (int i=0; i<word.length(); i++) {
            if (wordGuessedArray[i] == true) {
                guessDisplayText += wordArray[i]+"  ";
            }
            else {
                guessDisplayText += "__  ";
            }
        }
        return guessDisplayText;
    }

    /**
     * checks the currently guessed letter, if input letter is in the word updates the array with the guesses and returns true
     * else returns false for an incorrect guess
     * @param input
     * @return
     */
    public boolean checkGuess(char input){
        boolean isLetterFound = false;
        // Checking if the guess is within the word
        for (int i=0; i<word.length(); i++) {
            if (input == (wordArray[i])) {
                wordGuessedArray[i] = true;
                isLetterFound = true;
            }
        }
        if (isLetterFound == true) {
            return true;
        }
        this.guessesRemaining--;
        return false;
    }

    /**
     * gets the current word being guessed
     * @return string 
     */ 
    public String getWord() {
        return word;
    }

    /**
     * checks to see if the whole word has been guessed
     * returns true if the word has been guessed and false if it has now
     * @return boolean 
     */
    public boolean getIsWordGuessed() {
        for (int i=0; i<getWordGuessedArray().length; i++) {
            if (getWordGuessedArray()[i]==false) {
                return false;
            }
        }    
        return true;
    }
    

    /**
     * get the word to be guessed as an array
     * @return
     */
    public char[] getWordArray() {
        return wordArray;
    }

    /**
     * get the number of guesses remaining
     * @return int
     */
    public int getGuessesRemaining() {
        return guessesRemaining;
    }

    /**
     * get game over (Which is word guessed or no more guesses left)
     * @return
     */
    public Boolean getIsGameOver() {
        return isGameOver;
    } 

    /**
     * get the current guessed letter
     * @return
     */
    public char getGuess() {
        return guess;
    }

    /**
     * get the array of boolean char representing which char has already been guessed
     * @return
     */
    public boolean[] getWordGuessedArray() {
        return wordGuessedArray;
    }

    // Setters
    /**
     * set the guesses remaining
     * @param guessesRemaining
     */
    public void setGuessesRemaining(int guessesRemaining) {
        this.guessesRemaining = guessesRemaining;
    }

    /**
     * set the game over flag
     * @param isGameOver
     */
    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    /**
     * set the current guess
     * @param guess
     */
    public void setGuess(char guess) {
        this.guess = guess;
    }

    /**
     * set the word guessed array
     * @param wordGuessedArray
     */
    public void setWordGuessedArray(boolean[] wordGuessedArray) {
        this.wordGuessedArray = wordGuessedArray;
    }
}