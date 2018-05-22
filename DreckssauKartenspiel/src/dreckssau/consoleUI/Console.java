package dreckssau.consoleUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
                this.playFirstOrLastRound();
            } else if (gameRound[0].equals("Round") && gameRound[1].equals("7")) {
                this.playSeventhRound();

            } else if (gameRound[0].equals("Round") && gameRound[1].equals("8")) {
                this.playEighthRound();

            } else {
                playStandardRound();
            }
        }

    }

    private void playStandardRound() {

        int round = Integer.parseInt(game.getRound().split(";")[1]);
        System.out.println("\n- - - - [Runde " + round + "] - - - ");
        System.out.println("- - - - Stichphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Wie viele Stiche werden Sie machen?");

        String[] playerHand = this.game.getHand().split(";");

        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));

        while (true) {
            if (game.getGamePhase().split(";")[1].equals("BidPhase")) {

                int playerBid = this.getPlayerBid();

                String[] bidPhaseCompleteFull = this.game.doNextStep(playerBid).split("\n");
                String[] bidPhaseComplete = bidPhaseCompleteFull[0].split(";");

                System.out.println(">>>> Es wurde wie folgt getippt: ");

                for (int i = 1; i < bidPhaseComplete.length; i++) {
                    String[] individualBid = bidPhaseComplete[i].split(",");
                    System.out.println(individualBid[0] + "  ->  " + individualBid[1]);
                }


            }

            if (game.getGamePhase().split(";")[1].equals("TricksPhase")) {
                System.out.println("\n- - - - Trumpfphase - - - -");
                System.out.println("Welche Karte wollen Sie spielen?");

                int chosenCard = getPlayerCardToPlay();

                ArrayList<String> trickPhaseCompleteFull = new ArrayList<>(Arrays.asList(game.doNextStep
                        (chosenCard).split("\n")));

                String[] trickPhaseCards = trickPhaseCompleteFull.get(0).split(";");

                ArrayList<String> formattedTricks = new ArrayList<>();

                for (int i = 0; i < trickPhaseCards.length; i++) {
                    String[] individualTrick = trickPhaseCards[i].split(",");
                    formattedTricks.add(individualTrick[0]);
                    formattedTricks.add(tools.getCardString(individualTrick[1]));
                }

                System.out.println(tools.convertToCardString(formattedTricks));

                System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                        [1] + "\n");


                if (trickPhaseCompleteFull.contains("RoundOver")) {
                    String[] trickPhaseCardsOver = trickPhaseCompleteFull.get(0).split(";");

                    ArrayList<String> formattedTricksOver = new ArrayList<>();

                    for (int i = 0; i < trickPhaseCards.length; i++) {
                        String[] individualTrick = trickPhaseCards[i].split(",");
                        formattedTricksOver.add(individualTrick[0]);
                        formattedTricksOver.add(tools.getCardString(individualTrick[1]));
                    }

                    System.out.println(tools.convertToCardString(formattedTricksOver));

                    System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                            [1] + "\n");


                    System.out.println(">>> Punktestand nach Runde: ");
                    String[] endOfRoundScore = trickPhaseCompleteFull.get(3).split(";");
                    for (int i = 1; i < endOfRoundScore.length; i++) {
                        String[] individualScore = endOfRoundScore[i].split(",");
                        System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
                    }

                    break;

                }

            }
        }

    }

    private ArrayList<String> displayFormattedHand(String[] playerHand) {

        ArrayList<String> formatted = new ArrayList<>();

        for (int i = 1; i < playerHand.length; i++) {
            String[] individualSplit = playerHand[i].split(",");
            formatted.add(individualSplit[0]);
            for (int j = 1; j < individualSplit.length; j++) {
                formatted.add(tools.getCardString(individualSplit[j]));
            }
        }

        return formatted;

    }

    private int getPlayerCardToPlay() {

        ArrayList<String> possibleActions = new ArrayList<>(Arrays.asList(this.game.getPossibleActions().split(";" +
                "")));

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < possibleActions.size(); i++) {
            if (i < possibleActions.size() - 1)
                sb.append(possibleActions.get(i)).append(",");
            else sb.append(possibleActions.get(i));
        }

        System.out.println(">> Bitte geben Sie ihre gewuenschte Karte zum Spielen an: (" + sb.toString()
                + ")");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && possibleActions.contains(input)) {
            return Integer.parseInt(input);
        } else {
            System.out.println(">>>> Sie haben eine nicht gueltige Karte eingegeben! Bitte erneut eingeben!");
            return this.getPlayerBid();
        }
    }


    private void playEighthRound() {

        System.out.println("\n- - - - [Runde 8] - - - -");
        System.out.println("- - - -Trumpfphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Machen Sie so wenige Stiche wie moeglich!");

        String[] playerHand = this.game.getHand().split(";");

        ArrayList<String> formatted = new ArrayList<>();



        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));


        while (true) {

            if (game.getGamePhase().split(";")[1].equals("TricksPhase")) {
                System.out.println("\n- - - - Trumpfphase - - - -");
                System.out.println("Welche Karte wollen Sie spielen?");

                int chosenCard = getPlayerCardToPlay();

                ArrayList<String> trickPhaseCompleteFull = new ArrayList<>(Arrays.asList(game.doNextStep
                        (chosenCard).split("\n")));

                String[] trickPhaseCards = trickPhaseCompleteFull.get(0).split(";");

                ArrayList<String> formattedTricks = new ArrayList<>();

                for (int i = 0; i < trickPhaseCards.length; i++) {
                    String[] individualTrick = trickPhaseCards[i].split(",");
                    formattedTricks.add(individualTrick[0]);
                    formattedTricks.add(tools.getCardString(individualTrick[1]));
                }

                System.out.println(tools.convertToCardString(formattedTricks));

                System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                        [1] + "\n");


                if (trickPhaseCompleteFull.contains("RoundOver")) {
                    String[] trickPhaseCardsOver = trickPhaseCompleteFull.get(0).split(";");

                    ArrayList<String> formattedTricksOver = new ArrayList<>();

                    for (int i = 0; i < trickPhaseCards.length; i++) {
                        String[] individualTrick = trickPhaseCards[i].split(",");
                        formattedTricksOver.add(individualTrick[0]);
                        formattedTricksOver.add(tools.getCardString(individualTrick[1]));
                    }

                    System.out.println(tools.convertToCardString(formattedTricksOver));

                    System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                            [1] + "\n");


                    System.out.println(">>> Punktestand nach Runde: ");
                    String[] endOfRoundScore = trickPhaseCompleteFull.get(3).split(";");
                    for (int i = 1; i < endOfRoundScore.length; i++) {
                        String[] individualScore = endOfRoundScore[i].split(",");
                        System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
                    }

                    break;

                }

            }


        }


    }

    private void playSeventhRound() {

        System.out.println("\n- - - - [Runde 7] - - - -");
        System.out.println("- - - -Trumpfphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Machen Sie so viele Stiche wie moeglich!");

        String[] playerHand = this.game.getHand().split(";");


        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));


        while (true) {

            if (game.getGamePhase().split(";")[1].equals("TricksPhase")) {
                System.out.println("\n- - - - Trumpfphase - - - -");
                System.out.println("Welche Karte wollen Sie spielen?");

                int chosenCard = getPlayerCardToPlay();

                ArrayList<String> trickPhaseCompleteFull = new ArrayList<>(Arrays.asList(game.doNextStep
                        (chosenCard).split("\n")));

                String[] trickPhaseCards = trickPhaseCompleteFull.get(0).split(";");

                ArrayList<String> formattedTricks = new ArrayList<>();

                for (int i = 0; i < trickPhaseCards.length; i++) {
                    String[] individualTrick = trickPhaseCards[i].split(",");
                    formattedTricks.add(individualTrick[0]);
                    formattedTricks.add(tools.getCardString(individualTrick[1]));
                }

                System.out.println(tools.convertToCardString(formattedTricks));

                System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                        [1] + "\n");


                if (trickPhaseCompleteFull.contains("RoundOver")) {
                    String[] trickPhaseCardsOver = trickPhaseCompleteFull.get(0).split(";");

                    ArrayList<String> formattedTricksOver = new ArrayList<>();

                    for (int i = 0; i < trickPhaseCards.length; i++) {
                        String[] individualTrick = trickPhaseCards[i].split(",");
                        formattedTricksOver.add(individualTrick[0]);
                        formattedTricksOver.add(tools.getCardString(individualTrick[1]));
                    }

                    System.out.println(tools.convertToCardString(formattedTricksOver));

                    System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                            [1] + "\n");


                    System.out.println(">>> Punktestand nach Runde: ");
                    String[] endOfRoundScore = trickPhaseCompleteFull.get(3).split(";");
                    for (int i = 1; i < endOfRoundScore.length; i++) {
                        String[] individualScore = endOfRoundScore[i].split(",");
                        System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
                    }

                    break;

                }

            }


        }

    }

    private void playFirstOrLastRound() {
        System.out.println("- - - - [Runde " + ((game.getRound().split(";")[1].equals("1")) ? "1]" : "14]") + " - - -" +
                " -");
        System.out.println("- - - - Stichphase - - - -");
        System.out.println(">> Sie sehen die Gegnerkarten! Wie viele Stiche werden Sie machen?");

        String[] playerHand = this.game.getHand().split(";");

        ArrayList<String> formatted = new ArrayList<>();

        for (int i = 1; i < playerHand.length; i++) {
            String[] individualSplit = playerHand[i].split(",");
            formatted.add(individualSplit[0]);
            formatted.add(tools.getCardString(individualSplit[1]));
        }

        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));

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

        ArrayList<String> formattedTricks = new ArrayList<>();

        for (int i = 0; i < trickPhaseCards.length; i++) {
            String[] individualTrick = trickPhaseCards[i].split(",");
            formattedTricks.add(individualTrick[0]);
            formattedTricks.add(tools.getCardString(individualTrick[1]));
        }

        System.out.println(tools.convertToCardString(formattedTricks));

        System.out.println(">>>> Gewinner dieser Trumpfphase: " + trickPhaseCompleteFull[1].split(";")[1] + "\n");

        System.out.println(">> Punktestand nach der Runde: ");
        String[] endOfRoundScore = trickPhaseCompleteFull[3].split(";");
        for (int i = 1; i < endOfRoundScore.length; i++) {
            String[] individualScore = endOfRoundScore[i].split(",");
            System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
        }

        if (this.game.getRound().split(";")[1].equals("14")) {
            System.out.println("\n\n>>>>>> ENDPUNKTESTAND <<<<<<<");

            String[] endOfRoundScoreFinal = trickPhaseCompleteFull[3].split(";");
            for (int i = 1; i < endOfRoundScoreFinal.length; i++) {
                String[] individualScore = endOfRoundScoreFinal[i].split(",");
                System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
            }

            System.out.println("\n ...Spiel beendet!");

        }

    }

    private int getPlayerBid() {

        ArrayList<String> possibleActions = new ArrayList<>(Arrays.asList(this.game.getPossibleActions().split(";" +
                "")));

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < possibleActions.size(); i++) {
            if (i < possibleActions.size() - 1)
                sb.append(possibleActions.get(i)).append(",");
            else sb.append(possibleActions.get(i));
        }

        System.out.println("Bitte geben Sie ihre Anzahl an Stichen an! (Regelbedingt moeglich: " + sb.toString()
                + ")");

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