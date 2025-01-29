import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MemoryGameTest {
    @Test
    public void openFileTest1() throws Exception {
        MemoryGame memoryGame = new MemoryGame(2);
        assertTrue(memoryGame.openFile("hello.txt"));
    }

    @Test
    public void openFileTest2() throws Exception {
        MemoryGame memoryGame = new MemoryGame(2);
        assertFalse(memoryGame.openFile("C:\\Users\\Andy\\Documents\\GitHub\\Java\\CSM 80 - Object-Oriented Programming\\session__230331_18-03.txt"));
    }

    @Test
    public void checkSizeTest1() throws Exception {
        MemoryGame memoryGame = new MemoryGame(1);
        assertTrue(memoryGame.checkSize("1g2"));
    }

    @Test
    public void checkSizeTest2() throws Exception {
        MemoryGame memoryGame = new MemoryGame(1);
        assertFalse(memoryGame.checkSize("4x4"));
    }

    @Test
    public void checkSizeTest3() throws Exception {
        MemoryGame memoryGame = new MemoryGame(1);
        assertTrue(memoryGame.checkSize("5x3"));
    }

    @Test
    public void checkSizeTest4() throws Exception {
        MemoryGame memoryGame = new MemoryGame(1);
        assertTrue(memoryGame.checkSize("50x2"));
    }

    @Test
    public void checkSameCards1() throws Exception {
        Cards cards = new Cards(4, 4);
        assertFalse(cards.checkSameCards(1, 1, 1, 2));
    }

    @Test
    public void checkSameCards2() throws Exception {
        Cards cards = new Cards(4, 4);
        cards.fronts = new String[][] {{"n", "I", "l", "H"}, {"w", "W", "w", "z"}, {"I", "W", "l", "H"}, {"n", "P", "P", "z"}};
        assertTrue(cards.checkSameCards(0, 0, 3, 0));
    }

    @Test
    public void checkWinTest1() throws Exception {
        Cards cards = new Cards(4, 4);
        assertFalse(cards.checkWin());
    }

    @Test
    public void checkWinTest2() throws Exception {
        Cards cards = new Cards(4, 4);
        cards.backs = new String[][] {{"n", "I", "l", "H"}, {"w", "W", "w", "z"}, {"I", "W", "l", "H"}, {"n", "P", "P", "z"}};
        cards.fronts = new String[][] {{"n", "I", "l", "H"}, {"w", "W", "w", "z"}, {"I", "W", "l", "H"}, {"n", "P", "P", "z"}};
        assertTrue(cards.checkWin());
    }
}
