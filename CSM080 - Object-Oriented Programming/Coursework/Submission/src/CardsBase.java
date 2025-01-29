import java.util.*;

public class CardsBase {
    Random rnd = new Random();
    int cols;
    int rows;
    String[][] backs;
    String[][] fronts;
    String choices = "abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";

    public void displayCards(String[][] cards){
        System.out.println("\nBoard layout:");
        for (String[] card : cards) {
            for (String s : card) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }


    // Function to check user's flip choices are the same card
    public boolean checkSameCards(int rowChoice1, int colChoice1, int rowChoice2, int colChoice2) {
        if (Objects.equals(fronts[rowChoice1][colChoice1], fronts[rowChoice2][colChoice2])) {
            backs[rowChoice1][colChoice1] = fronts[rowChoice1][colChoice1];
            backs[rowChoice2][colChoice2] = fronts[rowChoice2][colChoice2];
            return true;
        }
        return false;
    }


    // Function to check if the game is finished
    public boolean checkWin() {
        return Arrays.deepEquals(fronts, backs);
    }
}
