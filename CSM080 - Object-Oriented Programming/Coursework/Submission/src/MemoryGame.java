import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class MemoryGame implements Runnable {
    int timer = 2;  // default 2 seconds
    int colBoard = 0;
    int rowBoard = 0;
    Cards cards = new Cards(2, 2);
    int colChoice1 = 0, rowChoice1 = 0, colChoice2 = 0, rowChoice2 = 0;
    int colGuess1 = 0, rowGuess1 = 0, colGuess2 = 0, rowGuess2 = 0;
    boolean replay = true;
    Session session;
    int[] scoresList = new int[5];
    String[] scoresBoards = new String[5];
    int numOfGuesses = 0;


    public MemoryGame(int neworno) {
        try (FileWriter fw = new FileWriter("session.txt")) {
            fw.write("");
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }

        // 1 equals new game
        if (neworno == 1) {
            // this line should only be here for unit testing, comment out to play game
            // System.out.println(" ");

            // comment out everything below for the unit test
            boolean incorrectBoard = true;
            // ask for size from user
            while (incorrectBoard) {
                Scanner userInput = new Scanner(System.in);
                String userInputString;

                // ask for the board size
                System.out.println("\nWhat size board would you like to play?");
                System.out.print("Enter in the following format: ROWxCOLUMN -> ");
                userInputString = userInput.next().toLowerCase();
                incorrectBoard = checkSize(userInputString);
            }

            // let's get started
            System.out.println("Timer is set to " + timer + " seconds.");
            cards = new Cards(colBoard, rowBoard);
            // display backs
            cards.displayCards(cards.backs);
        }
        // 2 equals open from file
        else {
            // this line should only be here for unit testing, comment out to play game
            // System.out.println(" ");

            // comment out everything below for the unit test
            boolean notOpened = true;
            // check if file is correct
            while (notOpened) {
                Scanner userInput = new Scanner(System.in);
                String fname;

                // loop if filename is incorrect
                System.out.println("\nWhat File would you like to play?");
                System.out.print("Enter the full file path -> ");
                fname = userInput.nextLine();
                notOpened = openFile(fname);
            }
        }
    }


    // Function to open a game from a file
    public boolean openFile(String fname) {
        // Try to open the file provided
        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            String s;
            String guess;
            String[] guesses;

            // read file line by line
            while ((s = br.readLine()) != null) {
                // check for the board size
                if (Pattern.compile("^[0-9]+x[0-9]+").matcher(s).find()) {
                    String[] size = s.split("x");
                    colBoard = Integer.parseInt(size[1]);
                    rowBoard = Integer.parseInt(size[0]);

                    cards = new Cards(colBoard, rowBoard);
                }

                // check for the pattern of the front of the cards
                else if (Pattern.compile("^\\[+.+\\]+").matcher(s).find()) {
                    int row = 0;
                    int col = 0;
                    String[] sSplit = s.split("]");

                    // 2D array split properly -- an array = [,,,,] , [,,,,] , [,,,,]
                    for (int i = 0; i < sSplit.length; i++) {
                        // get rid of extra brackets and spaces
                        sSplit[i] = sSplit[i].replace("[", "");
                        sSplit[i] = sSplit[i].replace(" ", "");

                        // split the inside array Strings into their own array
                        String[] doubleSplit = sSplit[i].split(",");
                        // only first array is proper, the others have an extra ',' in front
                        String[] smallDoubleSplit = new String[doubleSplit.length - 1];

                        // proper array gets added to fronts
                        if (doubleSplit.length == colBoard) {
                            for (String dub : doubleSplit) {
                                cards.fronts[row][col] = dub;
                                col++;
                            }
                        } else {  // improper arrays get fixed
                            for (int j = 0; j < doubleSplit.length; j++) {
                                // extra ',' in front creates and empty string at first position
                                if (Objects.equals(doubleSplit[j], "")) continue;
                                smallDoubleSplit[col] = doubleSplit[j];
                                col++;
                            }
                            col = 0;

                            // now improper arrays should be proper and gets added to fronts
                            for (int k = 0; k < smallDoubleSplit.length; k++) {
                                cards.fronts[row][col] = smallDoubleSplit[k];
                                col++;
                            }
                        }
                        col = 0;
                        row++;
                    }
                }

                // time to go through the guesses
                else if (Pattern.compile("^\\([0-9]+,[0-9]+\\)").matcher(s).find()) {
                    String[] moves = s.split(" ");

                    // reset choices
                    colChoice1 = 0;
                    colChoice2 = 0;
                    rowChoice1 = 0;
                    rowChoice2 = 0;

                    // parse out choices
                    for (int i = 0; i < moves.length; i++) {
                        guess = moves[i].replace("(", "");
                        guess = guess.replace(")", "");
                        guesses = guess.split(",");
                        if (colChoice1 == 0) colChoice1 = Integer.parseInt(guesses[1]) - 1;
                        else colChoice2 = Integer.parseInt(guesses[1]) - 1;
                        if (rowChoice1 == 0) rowChoice1 = Integer.parseInt(guesses[0]) - 1;
                        else rowChoice2 = Integer.parseInt(guesses[0]) - 1;
                    }

                    // check if they are the same then change backs
                    cards.checkSameCards(rowChoice1, rowChoice2, colChoice1, colChoice2);
                    numOfGuesses++;
                }

                else {
                    System.out.println("Incorrect file format");
                    return true;
                }

            }
            // everything should have worked if this point was reached
            cards.displayCards(cards.backs);
            return false;
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
            return true;
        }
    }


    // Function to check user's input for the board size
    public boolean checkSize(String input) {
        if (!Pattern.compile("^[0-9]+x[0-9]+").matcher(input).find()) {
            System.out.println("Incorrect input.");
            return true;
        }

        // split for column and row
        String[] size = input.split("x");
        try {
            colBoard = Integer.parseInt(size[1]);
            rowBoard = Integer.parseInt(size[0]);
        } catch (Exception e) {
            System.out.println("Incorrect input.");
            return true;
        }

        // make sure column and row are not zero
        if (colBoard == 0 || rowBoard == 0) {
            System.out.println("Board size cannot be 0.");
            return true;
        }

        int total = colBoard * rowBoard;
        // check if board is even
        if (total % 2 != 0) {
            System.out.println("Board size is not even.");
            return true;
        }

        // check if board is smaller than front choices
        if (total >= 50) {
            System.out.println("Board size is too big.\nRow x Column must be less than 50.");
            return true;
        }

        // if all is good, print out size
        System.out.println("\nBoard size is now " + rowBoard + "x" + colBoard);
        return false;
    }


    // Function to check user's input for the game menu choice
    public int checkMenuChoice() {
        Scanner userInput = new Scanner(System.in);
        int menuChoice;
        List<Integer> menuOptions = Arrays.asList(1, 2, 3, 4);
        while (true) {
            System.out.println("\nCHOOSE AN OPTION (enter number within brackets):");
            System.out.println("[1] FLIP CARDS");
            System.out.println("[2] REPLAY GUESSES");
            System.out.println("[3] CHANGE TIMER");
            System.out.println("[4] QUIT AND SAVE PROGRESS");

            if (!userInput.hasNextInt()) {
                System.out.println("Incorrect input.");
                continue;
            }

            menuChoice = userInput.nextInt();

            if (!menuOptions.contains(menuChoice)) {
                System.out.println("Incorrect input.");
            }
            else break;
        }
        return menuChoice;
    }


    // Function to check user's input for flip choices
    public void checkFlip() {
        Scanner userInput = new Scanner(System.in);
        String userInputString;
        String flipChoice;
        String[] flipOptions1;
        String[] flipOptions2;
        String[] userInputSplit;
        boolean incorrect = true;

        do {
            System.out.println("\nEnter pair to reveal.");
            System.out.print("Enter in the following format: (row1,column1) (row2,column2) -> ");
            userInputString = userInput.nextLine();
            if (!userInputString.contains(" ")) {
                System.out.println("Incorrect input.");
                continue;
            }

            userInputSplit = userInputString.split(" ");

            if (userInputSplit.length > 2) {
                System.out.println("Incorrect input.");
                continue;
            }

            flipChoice = userInputSplit[0].replace("(", "");
            flipChoice = flipChoice.replace(")", "");
            flipOptions1 = flipChoice.split(",");

            flipChoice = userInputSplit[1].replace("(", "");
            flipChoice = flipChoice.replace(")", "");
            flipOptions2 = flipChoice.split(",");


            try {
                rowGuess1 = Integer.parseInt(flipOptions1[0]);
                colGuess1 = Integer.parseInt(flipOptions1[1]);
                rowGuess2 = Integer.parseInt(flipOptions2[0]);
                colGuess2 = Integer.parseInt(flipOptions2[1]);
            } catch (Exception e) {
                System.out.println("Incorrect input.");
                continue;
            }

            if (colGuess1 == 0 || colGuess2 == 0 || rowGuess1 == 0 || rowGuess2 == 0) {
                System.out.println("Incorrect input.");
            }
            else if (colGuess1 > colBoard || colGuess2 > colBoard || rowGuess1 > rowBoard || rowGuess2 > rowBoard) {
                System.out.println("Incorrect input.");
            }
            else if (colGuess1 == colGuess2 && rowGuess1 == rowGuess2) {
                System.out.println("Incorrect input.");
            } else {
                colChoice1 = colGuess1 - 1;
                rowChoice1 = rowGuess1 - 1;
                colChoice2 = colGuess2 - 1;
                rowChoice2 = rowGuess2 - 1;
                incorrect = false;
            }
        } while (incorrect);
        numOfGuesses++;
    }


    // Function to display flipped cards
    public void displayChoices() {
        System.out.println();
        for (int i = 0; i < rowBoard; i++){
            for (int j = 0; j < colBoard; j++) {
                if (i == rowChoice1 && j == colChoice1) {
                    System.out.print(cards.fronts[i][j] + "\t");
                }
                else if (i == rowChoice2 && j == colChoice2) {
                    System.out.print(cards.fronts[i][j] + "\t");
                }
                else {
                    System.out.print(cards.backs[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }


    // Function to replay current game's guesses
    public void replayGuesses() {
        if (numOfGuesses == 0) {
            System.out.println("\nNo guesses to replay");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("session.txt")))
        {
            String s;
            String guess;
            String[] guesses;
            while ((s = br.readLine()) != null) {
                colChoice1 = 0;
                colChoice2 = 0;
                rowChoice1 = 0;
                rowChoice2 = 0;
                if (Pattern.compile("[0-9]+x[0-9]+").matcher(s).find()) continue;
                if (s.contains("[")) continue;
                String[] moves = s.split(" ");
                for (int i = 0; i < moves.length; i++) {
                    guess = moves[i].replace("(", "");
                    guess = guess.replace(")", "");
                    guesses = guess.split(",");
                    if (colChoice1 == 0) colChoice1 = Integer.parseInt(guesses[1]) - 1;
                    else colChoice2 = Integer.parseInt(guesses[1]) - 1;
                    if (rowChoice1 == 0) rowChoice1 = Integer.parseInt(guesses[0]) - 1;
                    else rowChoice2 = Integer.parseInt(guesses[0]) - 1;
                }
                displayChoices();
                System.out.println();
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }
    }


    // Function to check user's timer change
    public void checkTimer() {
        Scanner userInput = new Scanner(System.in);
        int timerChoice;
        while (true) {
            System.out.print("\nHow many seconds do you want the timer to be? -> ");

            if (!userInput.hasNextInt()) {
                System.out.println("Incorrect input.");
                continue;
            }

            timerChoice = userInput.nextInt();
            break;
        };
        timer = timerChoice;
        System.out.println("Timer is set to " + timer + " seconds.\n");
        cards.displayCards(cards.backs);
    }


    // Function to display top 5 scores
    public void displayScores() {
        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt")))
        {
            int order = 1;
            String s;

            System.out.println("Top-5 High Scores:");
            while ((s = br.readLine()) != null) {
                String[] scores = s.split(",");
                System.out.println(order + ". " + scores[0] + " flips on " + scores[1] + " board");
                order++;
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }
    }


    // Function to set the new score in the file
    public void setNewScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt")))
        {
            int order = 0;
            String s;

            while ((s = br.readLine()) != null) {
                String[] score = s.split(",");
                int scoreInt = Integer.parseInt(score[0]);
                scoresList[order] = scoreInt;
                scoresBoards[order] = score[1];
                order++;
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }

        for (int i = 0; i < scoresList.length; i++) {
            if (numOfGuesses < scoresList[i]) {
                for (int j = scoresList.length-1; j > 0; j--) {
                    if (scoresList[j-1] < numOfGuesses) break;
                    scoresList[j] = scoresList[j-1];
                    scoresBoards[j] = scoresBoards[j-1];
                }
                scoresList[i] = numOfGuesses;
                scoresBoards[i] = colBoard + "x" + rowBoard;
                break;
            }
        }
    }


    // Function to save scores to a file
    public void saveScores() {
        try (FileWriter fw = new FileWriter("scores.txt")) {
            for (int i = 0; i < scoresList.length; i++) {
                String str = scoresList[i] + "," + scoresBoards[i] + "\n";
                fw.write(str);
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }
    }


    // Function to save session to a file after quitting
    public void saveAndQuit() {
        LocalDateTime currDate = LocalDateTime.now();
        DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyMMdd_HH-mm");
        String currDateFormatted = currDate.format(dateFormatted);
        String saveFileName = "session__" + currDateFormatted + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader("session.txt")))
        {
            String s = br.readLine();
            if (s == null) {
                return;
            }
            try (FileWriter fw = new FileWriter(saveFileName)) {
                do {
                    fw.write(s + "\n");
                } while ((s = br.readLine()) != null);
            } catch (IOException ex) {
                System.out.println("I/O Error: " + ex);
            }
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex);
        }

        System.out.println("\nFile saved as " + saveFileName);
    }


    // Function to attempt to clear the screen
    public void clearScreen() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("\n");
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    // Function to run the thread
    public void run() {
        int choice;
        boolean play = true;
        // loop here
        do {
            // get menu choice
            choice = checkMenuChoice();

            switch (choice) {
                // Flip cards
                case 1:
                    // get user choice
                    checkFlip();

                    // save choices to file
                    String bSize = colBoard + "x" + rowBoard;
                    session = new Session(colChoice1, colChoice2, rowChoice1, rowChoice2, bSize, cards.fronts);
                    Thread sess = new Thread(session);
                    sess.start();

                    // show fronts
                    displayChoices();

                    // stay for designated seconds
                    try {
                        int slp = timer*1000;
                        Thread.sleep(slp);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // if right, stay on screen, else flip back
                    if (!cards.checkSameCards(rowChoice1, rowChoice2, colChoice1, colChoice2)) {
                        clearScreen();
                        cards.displayCards(cards.backs);
                    }

                    // check for winning condition
                    if(cards.checkWin()) {
                        System.out.println("\nYou win!");
                        System.out.println("Final amount of guesses: " + numOfGuesses + "\n");
                        // when finished, show scores
                        setNewScore();
                        saveScores();
                        displayScores();
                        play = false;
                    }
                    break;

                // Change Timer
                case 2:
                    replayGuesses();

                    // stay for designated seconds
                    try {
                        int slp = timer*1000;
                        Thread.sleep(slp);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    clearScreen();
                    cards.displayCards(cards.backs);
                    break;


                // Change Timer
                case 3:
                    checkTimer();
                    break;

                // Quit and save
                default:
                    saveAndQuit();
                    play = false;
                    replay = false;
                    break;
            }
        } while (play);

        Scanner userReplay = new Scanner(System.in);
        // Ask to play again
        if (replay) {
            System.out.println("\nDo you wish to play again (y/n)? ");
            String again = userReplay.next().toLowerCase();
            if (again.contains("y")) {
                // Initialize game
                MemoryGame game = new MemoryGame(1);
                Thread threadGame = new Thread(game);

                // Play game
                threadGame.start();
            }
        }
    }
}
