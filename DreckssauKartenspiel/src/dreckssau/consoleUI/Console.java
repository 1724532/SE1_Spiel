package dreckssau.consoleUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (gameRound[0].equals("Round") && gameRound[1].equals("1") || gameRound[1].equals("14")) {
                this.playFirstRound();
                System.out.println("Erste Runde fertig!");
            } else if (gameRound[0].equals("Round") && gameRound[1].equals("7")) {
                this.playSeventhRound();
                System.out.println("Siebte Runde fertig!");
            } else if (gameRound[0].equals("Round") && gameRound[1].equals("8")) {
                this.playEighthRound();
                System.out.println("Achte Runde fertig!");
            } else {
                playStandardRound();
            }
        }

    }

    private void playStandardRound() {


        System.out.println(">> Runde " + this.game.getRound().split(";")[1]);

        System.out.println("- - - - Stichphase - - - -");

        System.out.println(">> Sie sehen Ihre Karten!");

        System.out.println(game.getHand());
        String[] playerHand = this.game.getHand().split(";");

        for (int i = 1; i < playerHand.length; i++) {

        }


    }

    private void playEighthRound() {
    }

    private void playSeventhRound() {
    }

    private void playFirstRound() {
        System.out.println("- - - - Stichphase - - - -");
        System.out.println(">> Sie sehen die Gegnerkarten! Wie viele Stiche werden Sie machen?");

        String[] playerHand = this.game.getHand().split(";");
        String[] gamePlayers = this.game.getAllPlayers().split(";");

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < playerHand.length; i++) {
            String[] individualPlayer = playerHand[i].split(",");
            sb.append(">>Karte von").append(individualPlayer[0]).append(": \n").append(this.tools.getCardString
                    (individualPlayer[1])).append("\n");
        }

        System.out.println(sb.toString());

        int playerBid = this.getPlayerBid();

        String[] bidPhaseCompleteFull = this.game.doNextStep(playerBid).split("\n");
        String[] bidPhaseComplete = bidPhaseCompleteFull[0].split(";");

        System.out.println(">>>> Es wurde wie folgt getippt: ");

        for (int i = 1; i < bidPhaseComplete.length; i++) {
            String[] individualBid = bidPhaseComplete[i].split(",");
            System.out.println(individualBid[0] + "  ->  " + individualBid[1]);
        }


        System.out.println("\n- - - - Trumpfphase - - - -");
        String[] trickPhaseCompleteFull = game.doNextStep(0).split("\n");
        String[] trickPhaseCards = trickPhaseCompleteFull[0].split(";");

        for (String s : trickPhaseCompleteFull) {
            System.out.println(s);
        }

        for (int i = 0; i < trickPhaseCards.length; i++) {
            String[] individualTrick = trickPhaseCards[i].split(",");
            System.out.println(">> Karte von " + individualTrick[0] + ": \n" + tools.getCardString(individualTrick[1]));
        }

        System.out.println(">>>> Gewinner dieser Trumpfphase: " + trickPhaseCompleteFull[1].split(";")[1] + "\n");

        System.out.println(">> Punktestand nach der ersten Runde: ");
        String[] endOfRoundScore = trickPhaseCompleteFull[3].split(";");
        for (int i = 1; i < endOfRoundScore.length; i++) {
            String[] individualScore = endOfRoundScore[i].split(",");
            System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
        }
    }

    private int getPlayerBid() {

        ArrayList<String> possibleActions = new ArrayList<>(Arrays.asList(this.game.getPossibleActions().split(";")));

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < possibleActions.size(); i++) {
            if (i < possibleActions.size() - 1)
                sb.append(possibleActions.get(i)).append(",");
            else sb.append(possibleActions.get(i));
        }

        System.out.println("Bitte geben Sie ihre Anzahl an Stichen an! (Regelbedingt moeglich: " + sb.toString() + ")");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && possibleActions.contains(input)) {
            return Integer.parseInt(input);
        } else {
            System.out.println("Sie haben einen nicht moeglichen Stich angegeben! Bitte erneut eingeben!");
            return this.getPlayerBid();
        }
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