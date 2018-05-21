package dreckssau.consoleUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import dreckssau.game.Game;
import dreckssau.game.IDreckssauHandler;

/**
 * The class is used for handling the UI experience and player input and
 * action handling within the game.
 *
 * @author Lukas Theisen <1724673@stud.hs-mannheim.de>
 */
public class Console {

    private Scanner scan;
    private IDreckssauHandler game;
    private Tools tools;

    public Console(IDreckssauHandler game) {
        this.scan = new Scanner(System.in);
        this.game = game;
        this.tools = new Tools();
        this.welcomeMessage();
        this.getGameConfiguration();
    }

    private void getGameConfiguration() {

        String playerName = this.getPlayerName();

        this.game.addHumanPlayer(playerName);

        int botAmount = this.getBotAmount();


        for (int i = 0; i < botAmount; i++) {
            int skillLevel = this.getBotSkillLevel(i);
            this.game.addAIPlayer(skillLevel);
        }

        System.out.println(">> Spielvorbereitung abgeschlossen! Beginnen Sie das Spiel mit Enter...");

        scan.nextLine();

        this.startGame();

    }

    private void startGame() {

        this.game.startGame();

        for (int i = 1; i <= 14; i++) {
            String[] gameRound = game.getRound().split(";");
            System.out.println(gameRound[0] + " " + gameRound[1]);
            System.out.println(gameRound[0].equals("Round") + " " + gameRound[1].equals("1\n"));
            if (gameRound[0].equals("Round") && gameRound[1].equals("1")) {
                this.playFirstRound();
            }
        }

    }

    private void playFirstRound() {

        System.out.println(">> Sie sehen die Gegnerkarten! Wie viele Stiche werden Sie machen?");


    }

    private int getBotSkillLevel(int i) {

        System.out.println(">> Bitte geben Sie den gewuenschten Schwierigkeitsgrad des " + (i + 1) + ". " +
                "Computergegners" +
                " ein!");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && input.matches("[1-3]")) {
            int inputInt = Integer.parseInt(input);
            if (inputInt <= 3 && inputInt >= 1) {
                return inputInt;
            } else {
                System.out.println("Eingegebener Schwierigkeitsgrad ist ungueltig! Bitte erneut eingeben!");
                return this.getBotAmount();
            }
        } else {
            System.out.println("Eingegebener Schwierigkeitsgrad ist ungueltig! Bitte erneut eingeben!");
            return this.getBotAmount();
        }
    }

    private int getBotAmount() {

        System.out.println(">> Bitte geben Sie gewuenschte Anzahl an Gegnern ein! [2-4]");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && input.matches("[2-4]")) {
            int inputInt = Integer.parseInt(input);
            if (inputInt <= 4 && inputInt >= 2) {
                return inputInt;
            } else {
                System.out.println("Eingegebene Anzahl ist ungueltig! Bitte erneut eingeben!");
                return this.getBotAmount();
            }
        } else {
            System.out.println("Eingegebene Anzahl ist ungueltig! Bitte erneut eingeben!");
            return this.getBotAmount();
        }
    }

    private String getPlayerName() {

        System.out.println(">> Bitte geben Sie Ihren Spielernamen ein!");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && !input.contains(";") && !input.contains(",")) {
            return input;
        } else {
            System.out.println(">> Ungueltiger Spielername eingegeben! Bitte neu eingeben!");
            return this.getPlayerName();
        }

    }


    /**
     * The method shows a Welcome Message to the player and informs about the
     * creators and their origin.
     */
    private void welcomeMessage() {
        System.out.println("/ - - - - - - - - - - - - \\");
        System.out.println("| >> D R E C K S S A U << |");
        System.out.println("\\ - - - - - - - - - - - - /");
        System.out.println("----------------------------");
        System.out.println("> programmed by Team 1 | SE1\n> Hochschule " + "Mannheim | 2UIB");
        System.out.println("----------------------------");
        System.out.println("Sie spielen gegen 2 - 4 Computergegner mit individueller Stärke!\n");

    }

}