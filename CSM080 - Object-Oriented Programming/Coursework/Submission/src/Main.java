import java.util.Arrays;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Scanner for inputs
        Scanner gaming = new Scanner(System.in);
        int neworno;
        List<Integer> newornoOptions = Arrays.asList(1, 2);

        while (true) {
            System.out.println("CHOOSE AN OPTION (enter number within brackets):");
            System.out.println("[1] START A NEW GAME");
            System.out.println("[2] LOAD GAME FROM FILE");

            if (!gaming.hasNextInt()) {
                System.out.println("Incorrect input.\n");
                continue;
            }

            neworno = gaming.nextInt();

            if (!newornoOptions.contains(neworno)) {
                System.out.println("Incorrect input.\n");
            }
            else break;
        }

        // Initialize game
        MemoryGame game = new MemoryGame(neworno);
        Thread threadGame = new Thread(game);

        // Play game
        threadGame.start();
    }
}