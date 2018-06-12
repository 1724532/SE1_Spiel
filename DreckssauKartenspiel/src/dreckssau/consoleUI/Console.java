package dreckssau.consoleUI;

import dreckssau.game.IDreckssauHandler;

import java.util.*;

/**
 * The class is used for handling the UI experience and player input and
 * action handling within the game.
 *
 * @author Lukas Theisen <1724673@stud.hs-mannheim.de>
 */
public class Console {

    // fields with Scanner, the interface reference and the card tools
    private Scanner scan;
    private IDreckssauHandler game;
    private Tools tools;
    private Map<String, String> scoreBoard;


    // constructor
    public Console(IDreckssauHandler game) {
        this.scan = new Scanner(System.in);
        this.game = game;
        this.tools = new Tools();
        this.scoreBoard = new HashMap<>();
    }

    /**
     * This method gets the player name, the amount of enemy players and their respective skill level. After setup it
     * starts the game and calls the logic handler methods. Works as starting point for the game.
     */
    public void getGameConfiguration() {

        // get player name first
        String playerName = this.getPlayerName();


        // add human player to the game
        this.game.addHumanPlayer(playerName);


        // get the desired amount of computer enemies
        int botAmount = this.getBotAmount();


        // add all to the game and ask for skill level in the process
        for (int i = 0; i < botAmount; i++) {
            int skillLevel = this.getBotSkillLevel(i);
            this.game.addAIPlayer(skillLevel);
        }


        // wait for user input to start game
        System.out.println(">> Spielvorbereitung abgeschlossen! Beginnen Sie das Spiel mit Enter...");

        scan.nextLine();

        this.startGame();
    }

    /**
     * This method terates over the 14 game rounds and calls the according round logic handlers based on round number.
     */
    private void startGame() {

        this.game.startGame();


        // hard coded 14 rounds
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

    /**
     * This method handles the logic for a standard round without extra actions which are handled separately.
     */
    private void playStandardRound() {
        int round = Integer.parseInt(game.getRound().split(";")[1]);
        System.out.println("\n- - - - [Runde " + round + "] - - - ");
        System.out.println("- - - - Stichphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Wie viele Stiche werden Sie machen?");
        String[] playerHand = this.game.getHand().split(";");
        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));
        do {
            if (game.getGamePhase().split(";")[1].equals("BidPhase")) {


                // get player's bid for specific bid phase
                int playerBid = this.getPlayerBid();

                String[] bidPhaseCompleteFull = this.game.doNextStep(playerBid).split("\n");
                String[] bidPhaseComplete = bidPhaseCompleteFull[0].split(";");

                System.out.println(">>>> Es wurde wie folgt getippt: ");

                for (int i = 1; i < bidPhaseComplete.length; i++) {
                    String[] individualBid = bidPhaseComplete[i].split(",");
                    System.out.println(individualBid[0] + "  ->  " + individualBid[1]);
                }
            }
        } while (!handleTricksPhase());
    }

    /**
     * This method handles the tricks phase and asks the user repeatedly for their intended card to play.
     *
     * @return boolean whether the tricks phase is finished being handled
     */
    private boolean handleTricksPhase() {
        if (game.getGamePhase().split(";")[1].equals("TricksPhase")) {
            System.out.println("\n- - - - Trumpfphase - - - -");
            System.out.println("Welche Karte wollen Sie spielen?");

            int chosenCard = getPlayerCardToPlay();

            ArrayList<String> trickPhaseCompleteFull = new ArrayList<>(Arrays.asList(game.doNextStep
                    (chosenCard).split("\n")));

            String[] trickPhaseCards = trickPhaseCompleteFull.get(0).split(";");

            ArrayList<String> formattedTricks = new ArrayList<>();

            for (String trickPhaseCard : trickPhaseCards) {
                String[] individualTrick = trickPhaseCard.split(",");
                formattedTricks.add(individualTrick[0]);
                formattedTricks.add(tools.getCardString(individualTrick[1]));
            }

            System.out.println(tools.convertToCardString(formattedTricks));

            System.out.println(">>>> Gewinner des Trumpfes: " + trickPhaseCompleteFull.get(1).split(";")
                    [1] + "\n");


            if (trickPhaseCompleteFull.contains("RoundOver")) {

                ArrayList<String> formattedTricksOver = new ArrayList<>();

                for (String trickPhaseCard : trickPhaseCards) {
                    String[] individualTrick = trickPhaseCard.split(",");
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
                    this.scoreBoard.put(individualScore[0], individualScore[1]);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This method reformats the players / computers hand cards into a single line output format and displays them to
     * the user after being coupled with a sysout.
     *
     * @param playerHand contains the current player's hand or the played cards by all players after tricks being
     *                   placed.
     * @return ArrayList of type String which holds all the formatted names and their attached cards
     */
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

    /**
     * This method asks the user which card he wants to play and checks for its validity in the current game and phase.
     *
     * @return int with the number of the proven card
     */
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
                + ") | '9' fuer Punktetafel");

        String input = scan.nextLine();

        if (input != null && !("").equals(input) && possibleActions.contains(input) && !input.equals("9")) {
            return Integer.parseInt(input);
        } else if (("9").equals(input)) {
            this.printScoreBoard();
            return this.getPlayerCardToPlay();
        } else {
            System.out.println(">>>> Sie haben eine nicht gueltige Karte eingegeben! Bitte erneut eingeben!");
            return this.getPlayerBid();
        }
    }

    /**
     * This method handles the logic for the eigth round of the game Drecksau, in which there is no bid phase as the
     * user has to trick instantly to get the lowest possible wins.
     */
    private void playEighthRound() {

        System.out.println("\n- - - - [Runde 8] - - - -");
        System.out.println("- - - -Trumpfphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Machen Sie so wenige Stiche wie moeglich!");

        String[] playerHand = this.game.getHand().split(";");

        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));


        while (true) {

            if (handleTricksPhase()) break;


        }

    }

    /**
     * This method handles the logic for the seventh round of the game Drecksau, in which there is no bid phase as
     * the user to trick instantly to get the highest possible wins.
     */
    private void playSeventhRound() {

        System.out.println("\n- - - - [Runde 7] - - - -");
        System.out.println("- - - -Trumpfphase - - - -");
        System.out.println(">> Sie sehen Ihre Karten! Machen Sie so viele Stiche wie moeglich!");

        String[] playerHand = this.game.getHand().split(";");

        System.out.println(tools.convertToCardString(displayFormattedHand(playerHand)));

        while (true) {
            if (handleTricksPhase()) break;
        }

    }

    /**
     * This round handles the first and last round of the game Drecksau, in which the user has no access to its own
     * single card but sees the computer players' cards. He then has to decide how many of those he will beat with
     * his card and has to place his bid. If it is the last round, the final score is being printed after the bid and
     * the results have been processed.
     */
    private void playFirstOrLastRound() {
        System.out.println("- - - - [Runde " + ((game.getRound().split(";")[1].equals("1")) ? "1]" : "14]") + " - - -" +
                " -");
        System.out.println("- - - - Stichphase - - - -");
        System.out.println(">> Sie sehen die Gegnerkarten! Wie viele Stiche werden Sie machen?");

        String[] playerHand = this.game.getHand().split(";");

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

        for (String trickPhaseCard : trickPhaseCards) {
            String[] individualTrick = trickPhaseCard.split(",");
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
            this.scoreBoard.put(individualScore[0], individualScore[1]);
        }

        if (this.game.getRound().split(";")[1].equals("14")) {
            System.out.println("\n\n>>>>>> ENDPUNKTESTAND <<<<<<<");

            String[] endOfRoundScoreFinal = trickPhaseCompleteFull[3].split(";");
            for (int i = 1; i < endOfRoundScoreFinal.length; i++) {
                String[] individualScore = endOfRoundScoreFinal[i].split(",");
                System.out.println(individualScore[0] + "  ->  " + individualScore[1]);
                this.scoreBoard.put(individualScore[0], individualScore[1]);
            }

            System.out.println("\n ...Spiel beendet!");
            System.exit(0);

        }

        System.out.println("\n> Erste Runde beendet! Weiter mit Enter...");
        this.scan.nextLine();

    }

    // Input methods follow here

    /**
     * This method captures the user's bid and checks if it is in line with the game's possible actions for this
     * phase of the game.
     *
     * @return int with the proven intended number of bids by the user
     */
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
                + ") | '9' fuer Punktetafel");

        String input = scan.nextLine();

        // input check
        if (input != null && !("").equals(input) && possibleActions.contains(input) && !input.equals("S")) {
            return Integer.parseInt(input);
        } else if (("9").equals(input)) {
            this.printScoreBoard();
            return this.getPlayerBid();

        } else {

            // bad input handling
            System.out.println("Sie haben einen nicht moeglichen Stich angegeben! Bitte erneut eingeben!");
            return this.getPlayerBid();
        }


    }

    /**
     * This method asks the user for the specific skill level (1-3) of the current computer enemy being added to the
     * game.
     *
     * @param i int value with the index of the current computer enemy for output and information purposes
     * @return int value with the intended skill level and proven value by the user for the specific computer enemy
     */
    private int getBotSkillLevel(int i) {

        System.out.println(">> Bitte geben Sie den gewuenschten Schwierigkeitsgrad des " + (i + 1) + ". " +
                "Computergegners" +
                " ein!");

        String input = scan.nextLine();

        // input check
        if (input != null && !("").equals(input) && input.matches("[1-3]")) {
            int inputInt = Integer.parseInt(input);
            if (inputInt <= 3 && inputInt >= 1) {
                return inputInt;
            } else {
                System.out.println("Eingegebener Schwierigkeitsgrad ist ungueltig! Bitte erneut eingeben!");
                return this.getBotAmount();
            }
        } else {
            // bad input handling
            System.out.println("Eingegebener Schwierigkeitsgrad ist ungueltig! Bitte erneut eingeben!");
            return this.getBotAmount();
        }
    }

    /**
     * This method asks the user for the amount of bots he wants to play against (2-4).
     *
     * @return int value with the amount of bots being added to the game
     */
    private int getBotAmount() {

        System.out.println(">> Bitte geben Sie gewuenschte Anzahl an Gegnern ein! [2-4]");

        String input = scan.nextLine();

        // input check
        if (input != null && !("").equals(input) && input.matches("[2-4]")) {
            int inputInt = Integer.parseInt(input);
            if (inputInt <= 4 && inputInt >= 2) {
                return inputInt;
            } else {
                System.out.println("Eingegebene Anzahl ist ungueltig! Bitte erneut eingeben!");
                return this.getBotAmount();
            }
        } else {
            // bad input handling
            System.out.println("Eingegebene Anzahl ist ungueltig! Bitte erneut eingeben!");
            return this.getBotAmount();
        }
    }

    /**
     * This method asks the user to input his name and checks for invalid characters and emptiness.
     *
     * @return String with the player's input name
     */
    private String getPlayerName() {

        System.out.println(">> Bitte geben Sie Ihren Spielernamen ein!");

        String input = scan.nextLine();

        // input check
        if (input != null && !("").equals(input) && !input.contains(";") && !input.contains(",")) {
            return input;
        } else {
            // bad input handling
            System.out.println(">> Ungueltiger Spielername eingegeben! Bitte neu eingeben!");
            return this.getPlayerName();
        }

    }

    /**
     * The method shows a Welcome Message to the player and informs about the
     * creators and their origin.
     */
    public void welcomeMessage() {
        System.out.println("/ - - - - - - - - - - - - \\");
        System.out.println("| >> D R E C K S S A U << |");
        System.out.println("\\ - - - - - - - - - - - - /");
        System.out.println("----------------------------");
        System.out.println("> programmed by Team 1 | SE1\n> Hochschule " + "Mannheim | 2UIB");
        System.out.println("----------------------------");
        System.out.println("Sie spielen gegen 2 - 4 Computergegner mit individueller Stärke!\n");
    }

    /**
     * This method prints out the current score to the user
     */
    private void printScoreBoard() {
        if (this.scoreBoard.isEmpty()) {
            System.out.println("Noch keine Runde gespielt! Kein Punktestand vorhanden!");
            return;
        }


        System.out.println("\n>>> PUNKTESTAND <<<");
        for (String s : this.scoreBoard.keySet()) {
            System.out.println(s + " -> " + this.scoreBoard.get(s));
        }
        System.out.println("\n");
    }

}