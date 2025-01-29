import java.io.*;
import java.util.*;

public class Top implements Sequence {
    ArrayList<String> arr;

    Top(){
        arr = new ArrayList<>();
    }

    public void push(String st){
        if (!arr.contains(st)){
            if (arr.size() < 3) {
                arr.add(st);
            }
            else {
                arr.sort(Comparator.naturalOrder());
                for (int i = 0; i < arr.size(); i++) {
                    int comp = st.compareTo(arr.get(i));
                    if (comp < 0) {
                        if (arr.size() == 3) {
                            arr.remove(arr.size() - 1);
                        }
                        arr.add(st);
                    }
                }
            }
        }
    }

    public void reset(){
        arr.clear();
    }

    public ArrayList<String> getTopThree(){
        arr.sort(Comparator.naturalOrder());
        return arr;
    }
}