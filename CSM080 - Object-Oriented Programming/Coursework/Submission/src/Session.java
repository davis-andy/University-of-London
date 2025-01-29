import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Session implements Runnable {
    int cChoice1, cChoice2, rChoice1, rChoice2;
    String bSize;
    String[][] bLayout;
    List<String> currentSession = new ArrayList<>();

    public Session(int colChoice1, int colChoice2, int rowChoice1, int rowChoice2, String boardSize,
                   String[][] boardLayout) {
        cChoice1 = colChoice1 + 1;
        cChoice2 = colChoice2 + 1;
        rChoice1 = rowChoice1 + 1;
        rChoice2 = rowChoice2 + 1;
        bSize = boardSize;
        bLayout = boardLayout;
    }

    public void saveSession() {
        try (BufferedReader br = new BufferedReader(new FileReader("session.txt")))
        {
            String s;
            s = br.readLine();
            if (s == null) {
                currentSession.add(bSize);
                currentSession.add(Arrays.deepToString(bLayout));
            }
            else {
                currentSession.add(s);
                while ((s = br.readLine()) != null) {
                    currentSession.add(s);
                }
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }

        currentSession.add("(" + rChoice1 + "," + cChoice1 + ") (" + rChoice2 + "," + cChoice2 + ")");

        try (FileWriter fw = new FileWriter("session.txt")) {
            for (int i = 0; i < currentSession.size(); i++) {
                fw.write(currentSession.get(i) + "\n");
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }
    }

    public void run() {
        saveSession();
    }
}
