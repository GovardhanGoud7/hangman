import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Hangman: guess the hidden word one letter at a time before you run out of lives.
 *
 * Run in VS Code: open this file and click the Run button above main.
 * The file name must stay Hangman.java.
 */
public class Hangman {

    private static final int MAX_LIVES = 6;

    private static final String[] WORDS = {
        "java", "python", "keyboard", "monitor", "variable", "function", "network",
        "database", "compiler", "program", "internet", "software", "algorithm",
        "browser", "laptop", "github", "student", "elephant", "mountain", "chocolate"
    };

    // The gallows picture for 0 to 6 wrong guesses.
    private static final String[] STAGES = {
        "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    private static final Scanner input = new Scanner(System.in);
    private static final Random random = new Random();

    /** Shows the word with _ for letters not guessed yet, e.g. j _ v _ */
    static String maskedWord(String word, Set<Character> guessed) {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            sb.append(guessed.contains(c) ? c : '_').append(' ');
        }
        return sb.toString().trim();
    }

    static boolean isSolved(String word, Set<Character> guessed) {
        for (char c : word.toCharArray()) {
            if (!guessed.contains(c)) return false;
        }
        return true;
    }

    /** Keeps asking until the player enters one new letter. */
    static char askLetter(Set<Character> guessed) {
        while (true) {
            System.out.print("Guess a letter: ");
            String text = input.nextLine().trim().toLowerCase();
            if (!text.matches("[a-z]")) {
                System.out.println("  Please type a single letter (a-z).");
                continue;
            }
            char letter = text.charAt(0);
            if (guessed.contains(letter)) {
                System.out.println("  You already guessed '" + letter + "'. Try another.");
                continue;
            }
            return letter;
        }
    }

    /** Plays one round. Returns true if the player wins. */
    static boolean playRound() {
        String word = WORDS[random.nextInt(WORDS.length)];
        Set<Character> guessed = new TreeSet<>();
        int wrong = 0;

        System.out.println("\nI'm thinking of a word with " + word.length() + " letters.");

        while (wrong < MAX_LIVES) {
            System.out.println();
            System.out.println(STAGES[wrong]);
            System.out.println("\nWord:  " + maskedWord(word, guessed));
            System.out.println("Lives: " + (MAX_LIVES - wrong)
                    + "   Guessed: " + (guessed.isEmpty() ? "-" : guessed.toString().replaceAll("[\\[\\],]", "")));

            char letter = askLetter(guessed);
            guessed.add(letter);

            if (word.indexOf(letter) >= 0) {
                System.out.println("  Yes! '" + letter + "' is in the word.");
                if (isSolved(word, guessed)) {
                    System.out.println("\nYou got it! The word was: " + word);
                    return true;
                }
            } else {
                wrong++;
                System.out.println("  No '" + letter + "' in the word.");
            }
        }

        System.out.println();
        System.out.println(STAGES[MAX_LIVES]);
        System.out.println("\nGame over! The word was: " + word);
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== Hangman ===");
        System.out.println("Guess the word one letter at a time. You have " + MAX_LIVES + " lives.");

        int wins = 0, losses = 0;
        boolean playing = true;
        while (playing) {
            if (playRound()) wins++; else losses++;
            System.out.println("Score  Wins: " + wins + "  Losses: " + losses);
            System.out.print("\nPlay again? (y/n): ");
            playing = input.nextLine().trim().equalsIgnoreCase("y");
        }
        System.out.println("Thanks for playing!");
    }
}