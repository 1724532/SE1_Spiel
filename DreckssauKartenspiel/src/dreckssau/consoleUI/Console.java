package dreckssau.consoleUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;

/**
 * The class is used for handling the UI experience and player input and action handling within the game.
 *
 * @author Lukas Theisen <1724673@stud.hs-mannheim.de>
 */
public class Console {

    private InputStreamReader systemIn = new InputStreamReader(System.in);
    private BufferedReader consoleIn = new BufferedReader(systemIn);


    /**
     * The method which handles the UI during gameplay and manages the user input and view presentation for the player.
     */
    private void startConsole() {
        this.welcomeMessage();
        this.getGameConfiguration();
    }

    private void getGameConfiguration() {

        System.out.println("> Please enter amount of computer players you want to compete against: ");
        int amountBots = 0;
        String inputNumber = null;
        try {
            inputNumber = consoleIn.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inputNumber != null && !inputNumber.equals("") && inputNumber.matches("[2-4]")) {
            amountBots = Integer.parseInt(inputNumber);
        } else {
            System.out.println(">> Input has to be a number between 2 - 4! Please enter a correct number!");
            getGameConfiguration();
        }

        System.out.println("> Now enter the skill levels of your computer enemies from 1 to 3:");
        int[] difficultiesBots = new int[amountBots];

        for (int i = 0; i < difficultiesBots.length; i++) {
            System.out.println("> Difficulty of computer player " + (i + 1) + ":");
            difficultiesBots[i] = this.getDifficultyBot();
        }

        System.out.println("\n> Before we start we need your player name!");
        String playerName = this.getName();

        System.out.println("\n>>> Thank you, " + playerName + "! You entered all skill levels! Game is ready to begin! <<<");
    }

    private int getDifficultyBot() {

        String inputNumber = null;
        try {
            inputNumber = consoleIn.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inputNumber != null && !inputNumber.equals("") && inputNumber.matches("[1-3]")) {
            return Integer.parseInt(inputNumber);
        } else {
            System.out.println("Invalid skill level input! Please reenter your desired skill level [1-3]:");
            return this.getDifficultyBot();
        }
    }

    /**
     * The method shows a Welcome Message to the player and informs about the creators and their origin.
     */
    private void welcomeMessage() {
        System.out.println("/ - - - - - - - - - - - - \\");
        System.out.println("| >> D R E C K S S A U << |");
        System.out.println("\\ - - - - - - - - - - - - /");
        System.out.println("----------------------------");
        System.out.println("> programmed by Team 1 | SE1\n> Hochschule Mannheim | 2UIB");
        System.out.println("----------------------------");
        System.out.println("You are able to play against 2 - 4 computer players for which you define the skill level!\n");
    }

    private String getName() {

        String inputName = null;

        System.out.println("> Please insert your player name!");

        try {
            inputName = consoleIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputName != null && !inputName.equals("") && inputName.length() <= 25)
            return inputName;

        else {
            System.out.println("Your name is invalid for playing, please reenter a valid name again!");
            return this.getName();
        }
    }


    public static void main(String[] args) {
        Console console = new Console();
        console.startConsole();
    }


}
