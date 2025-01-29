import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cards extends CardsBase {
    public Cards(int col, int row) {
        // initialize the Card class
        cols = col;
        rows = row;
        backs = new String[rows][cols];
        fronts = new String[rows][cols];
        String filler = "0";
        List<Integer> prevChoices = new ArrayList<>();

        // fill the fronts array with a placeholder
        for (String[] card : fronts) {
            Arrays.fill(card, filler);
        }

        // randomly change the fronts until the placeholder is gone
        while (true) {
            int ch = rnd.nextInt(choices.length());
            // make sure the same character isn't used twice
            if (prevChoices.contains(ch)) {
                continue;
            }
            // break once there are enough unique characters
            if (prevChoices.size() == ((cols * rows) / 2)){
                break;
            }
            // make a list of which unique characters were used
            prevChoices.add(ch);

            String chose = choices.substring(ch, ch+1);
            int twoChoices = 0;
            // make two cards the same character
            while(twoChoices < 2) {
                int x = rnd.nextInt(rows);
                int y = rnd.nextInt(cols);

                if (Objects.equals(fronts[x][y], filler)) {
                    backs[x][y] = "\uD83C\uDCA0";
                    fronts[x][y] = chose;
                    twoChoices++;
                }
            }
        }
    }
}

