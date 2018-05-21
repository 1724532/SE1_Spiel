package dreckssau.consoleUI;

import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;

import java.util.ArrayList;

class Tools {

    public String convertToCardString(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        ArrayList<StringBuilder> sbArr = new ArrayList<>();
        sbArr.add(new StringBuilder());
        sbArr.add(new StringBuilder());
        sbArr.add(new StringBuilder());
        sbArr.add(new StringBuilder());
        sbArr.add(new StringBuilder());
        for (String s : list) {
            String[] token = s.split("\n");
            if (token.length == 5) {
                for (int i = 0; i < token.length; i++) {
                    sbArr.get(i).append(token[i] + " ");
                }
            }
            if (token.length == 1) {
                sbArr.get(3).append(token[0] + " ");
                String fill = "";
                fill = fill.format("%" + token[0].length() + "s", fill);
                sbArr.get(0).append(fill + " ");
                sbArr.get(1).append(fill + " ");
                sbArr.get(2).append(fill + " ");
                sbArr.get(4).append(fill + " ");
            }
        }
        sb.append(sbArr.get(0).toString() + "\n");
        sb.append(sbArr.get(1).toString() + "\n");
        sb.append(sbArr.get(2).toString() + "\n");
        sb.append(sbArr.get(3).toString() + "\n");
        sb.append(sbArr.get(4).toString());

        return sb.toString();
    }


    public String getCardString(String c) {

        String[] splitCard = c.split(" of ");

        String out = "";
        out += "╔═════╗\n";
        out += "║" + String.format("%-5s", CardRank.valueOf(splitCard[0]).getRank()) + "║\n";
        out += "║  " + CardSuit.valueOf(splitCard[1]).getSuit() + "  ║\n";
        out += "║" + String.format("%5s", CardRank.valueOf(splitCard[0]).getRank()) + "║\n";
        out += "╚═════╝";

        return out;
    }

}
