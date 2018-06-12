package dreckssau.consoleUI;

import dreckssau.game.Card;
import dreckssau.game.CardRank;
import dreckssau.game.CardSuit;

import java.util.ArrayList;

class Tools {

    String convertToCardString(ArrayList<String> list) {
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
                    sbArr.get(i).append(token[i]).append(" ");
                }
            }
            if (token.length == 1) {
                sbArr.get(3).append(token[0]).append(" ");
                String fill = "";
                fill = String.format("%" + token[0].length() + "s", fill);
                sbArr.get(0).append(fill).append(" ");
                sbArr.get(1).append(fill).append(" ");
                sbArr.get(2).append(fill).append(" ");
                sbArr.get(4).append(fill).append(" ");
            }
        }
        sb.append(sbArr.get(0).toString()).append("\n");
        sb.append(sbArr.get(1).toString()).append("\n");
        sb.append(sbArr.get(2).toString()).append("\n");
        sb.append(sbArr.get(3).toString()).append("\n");
        sb.append(sbArr.get(4).toString());

        return sb.toString();
    }


    String getCardString(String c) {

        String[] splitCard = c.split(" of ");

        String out = "";
        out += "╔═════╗\n";
        out += "║" + String.format("%-5s", CardRank.valueOf(splitCard[0]).getRank()) + "║\n";
        out += "║  " + CardSuit.valueOf(splitCard[1]).getSuit() + "  ║\n";
        out += "║" + String.format("%5s", CardRank.valueOf(splitCard[0]).getRank()) + "║\n";
        out += "╚═════╝";

        return out;
    }

    Card convertStringToCard(String c) {
        String[] s = c.split(" of ");
        CardRank rank = CardRank.valueOf(s[0]);
        CardSuit suit = CardSuit.valueOf(s[1]);
        return new Card(rank, suit);
    }
}
