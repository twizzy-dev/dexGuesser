import java.io.*;
import java.util.Scanner;

/**
 * dexGuesser – Save/Stats File Handler
 * Manages all read, update, and write operations on user's stat file.
 * stats.txt stores data as integers in the format: 19,9,0,105
 *
 * @author twizzy-dev
 * @version 0.2.0
 */

public class SaveHandler {

    private int totalGuessAmount = 0; // total attempts altogether
    private int totalCorrectAmount = 0; // exact match
    private int totalWrongAmount = 0; // non-Pokémon entered (required for average difference)
    private int totalDifference = 0; // difference between guessed Pokémon's Pokédex number and given Pokédex number (total value of all guesses)

    private final String statsFilePath;

    /**
     * Object for storing and saving user's stats.
     * @param filePath of stats.csv
     */
    public SaveHandler(String filePath) {
        statsFilePath = filePath;
        readFromFile();
    }

    /**
     * If file is found, reads in data and stores it as user's stats.
     * If file can't be found, keeps stats at 0 (default value).
     */
    public void readFromFile() {
        try (Scanner fileReader = new Scanner(new File(statsFilePath))) {
            while (fileReader.hasNext()) {
                String[] stats = fileReader.nextLine().split(",");

                if (stats.length == 4) { // ensure stats.csv is formatted correctly (e.g. 0,0,0,0)
                    try {
                        totalGuessAmount = Integer.parseInt(stats[0]);
                        totalCorrectAmount = Integer.parseInt(stats[1]);
                        totalCorrectAmount = Integer.parseInt(stats[2]);
                        totalDifference = Integer.parseInt(stats[3]);

                        return;
                    }
                    catch (NumberFormatException _) {}
                }
                System.err.println("Save File Error: Incorrect stats.csv format");
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("Save File Error: stats.csv not found");
        }
    }

    /**
     * Save user's current statistics to text file.
     * If error occurs, stats.csv will remain unchanged.
     */
    public void saveToFile() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(statsFilePath))) {
            fileWriter.write(totalGuessAmount + "," + totalCorrectAmount + "," + totalWrongAmount + "," + totalDifference);
        }
        catch (IOException e) {
            System.err.println("Save File Error: Could not save to stats.csv");
        }
    }

    /**
     * Used when program terminates, either by user typing user "exit" in program, or CTRL + C, terminal being closed, etc.
     */
    public void exitHandler() {
        saveToFile();
    }

    /**
     * Calculates average distance off of correct guess across all incorrect but valid guesses.
     * @return average distance off of correct guesses
     */
    public int getAverageDifference() {
        return Math.round((float) totalDifference / (totalGuessAmount - totalCorrectAmount - totalWrongAmount));
    }

    /**
     * @param difference between guessed Pokémon's Pokédex number and given Pokédex number
     */
    public void increaseTotalDifference(int difference) {
        totalDifference += difference;
    }

    /**
     * @return total amount of guesses user has entered
     */
    public int getTotalGuessAmount() {
        return totalGuessAmount;
    }

    /**
     * @return total amount of user guesses that were correct
     */
    public int getTotalCorrectAmount() {
        return totalCorrectAmount;
    }

    public void incrementGuessAmount() {
        totalGuessAmount++;
    }

    public void incrementCorrectAmount() {
        totalCorrectAmount++;
    }

    public void incrementWrongAmount() {
        totalWrongAmount++;
    }
}