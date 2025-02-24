import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*        __          ______
     ____/ /__  _  __/ ____/_  _____  _____________  _____
    / __  / _ \| |/_/ / __/ / / / _ \/ ___/ ___/ _ \/ ___/
   / /_/ /  __/>  </ /_/ / /_/ /  __(__  |__  )  __/ /
   \__,_/\___/_/|_|\____/\__,_/\___/____/____/\___/_/

*/
/**
 * dexGuesser – Command Line Application
 * Randomly selects number between 1 and 1025, Guess the Pokémon that shares that National Pokédex number.
 *
 * @author twizzy-dev
 * @version 0.2.0
 */
public class dexGuesserCMD {

    /**
     * Generates random integer between 1 and 1025, prompts user for input.
     * Compares input against Pokédex and shows success message if correct, or how far away if wrong.
     */
    public static void main(String[] args) {
        ArrayList<String> pokedex = getPokedex(); // List of all 1025 Pokémon

        if (pokedex == null || pokedex.isEmpty()) {
            System.err.println("Pokédex Error: Pokédex not found");
            System.exit(0);
        }

        SaveHandler save = new SaveHandler("saves/stats.csv");
        Runtime.getRuntime().addShutdownHook(new Thread(save::exitHandler)); // ensures data is saved if terminal is closed by means other than user typing "exit"

        try (Scanner cmdInput = new Scanner(System.in)) {
            System.out.println("Type \"exit\" to terminate program.");

            Random RNG = new Random();

            while (true) {
                int dexNo = RNG.nextInt(pokedex.size());
                String pokemonName = pokedex.get(dexNo);

                System.out.print("Pokédex No. " + dexNo + ": \n> "); // user prompt

                String userGuess = cmdInput.nextLine().trim();

                // guess amount is not incremented if user types "exit" or "stats"
                if (userGuess.toLowerCase().matches("exit")) {
                    save.exitHandler();
                    break;
                }
                else if (userGuess.toLowerCase().matches("stats")) {
                    System.out.printf("""
                            User Statistics:
                              Total amount of guesses: %d
                              Total amount correct: %d
                              Avg difference off Correct Pokémon: %d
                            %n""", save.getTotalGuessAmount(), save.getTotalCorrectAmount(), save.getAverageDifference());
                }
                // TODO: move game logic to new class
                else if (userGuess.toLowerCase().replaceAll("[^A-Za-z2]", "").matches(pokemonName.toLowerCase().replaceAll("[^A-Za-z2]", ""))) {
                    // for input comparison, remove all spaces, hyphens, etc. to reduce user error (e.g. "TAPUKOKO" and "tapu koko" are both acceptable answers for "Tapu Koko")
                    // Pokémon relevant to this: Nidoran, Farfetch'd(+), Mr. Mime(+), Porygon2(+), Ho-oh, Flabébé, Type: Null, Jangmo-o(+), Tapus, Paradox Pokémon, Treasures of Ruin
                    // TODO: above pokémon only work when their dex number was chose or are spelt correctly, otherwise their dex entry can't be found. Add extra field for alternate spellings of name?

                    System.out.println("Correct, the Pokémon was " + pokemonName + ".");
                    save.incrementGuessAmount();
                    save.incrementCorrectAmount();
                }
                else {
                    save.incrementGuessAmount();
                    int dexNoOfInput = pokedex.indexOf(toTitleCase(userGuess.replaceAll("[^A-Za-z2;\\-.']", ""))); // removes any extra characters from input when searching dex (e.g. pikachu! will still find #25)
                    if (dexNoOfInput == -1) {
                        System.out.println("That is not a Pokémon, the correct answer is " + pokemonName + ".");
                        save.incrementWrongAmount();
                    }
                    else {
                        int difference = Math.abs(dexNo - dexNoOfInput); // difference should always be a positive int
                        System.out.println("The correct Pokémon is " + pokemonName + ", you were " + difference + " off.");
                        save.increaseTotalDifference(difference);
                    }
                }
            }
        }
    }

    /**
     * Reads in Pokémon data from text file and stores in an ArrayList of Strings.
     * Will most likely be refactored later; replaced with Pokémon and Pokédex classes.
     * @return ArrayList of all 1025 Pokémon names
     */
    private static ArrayList<String> getPokedex() {
        ArrayList<String> pokedex = new ArrayList<>();

        try (Scanner fileReader = new Scanner(new File("resources/pokedex.csv"))) {
            while (fileReader.hasNext()) {
                String[] pokemonInfo = fileReader.nextLine().split(",");
                pokedex.add(pokemonInfo[1]); // ignores all other Pokémon information
            }
            return pokedex;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Takes a string and converts it to Title Case.
     * Source: https://stackoverflow.com/a/1086134
     *
     * @param input to be converted
     * @return input in Title Case
     */
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            }
            else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}