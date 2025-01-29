import java.io.*;
import java.util.*;

public interface Sequence
{
    void push( String st );
    ArrayList<String> getTopThree();
    void reset();
}