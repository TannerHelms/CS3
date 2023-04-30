
import java.io.*;
import java.util.*;

public class GameStarter {
    public GameStarter() {
        H = new HashTable<WordInfo>();
    }
  
    public int computeScore(WordInfo word) {
        return letterScore(word.toString()) * lengthScore(word.toString()) * bonus(word.getTimesUsed());
    }
    public final int[] letterScore = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
    public int bonus(int timesUsed){
        if (timesUsed == 0) { return 5;};
        if (timesUsed > 0 && timesUsed < 6) {return 4;};
        if (timesUsed > 5 && timesUsed < 11) {return 3;};
        if (timesUsed > 10 && timesUsed < 16) {return 2;};
        return 1;
    }

    public int letterScore(String word) {
        int score = 0;
        for (int i = 0; i < word.length(); i++) {
            int sub = word.charAt(i) - 'a';
            score += letterScore[sub];
        }
        return score;
    }

    public int lengthScore(String word){
        int wordLen = word.length();
        if (wordLen < 3){
            return 0;
        } else if (wordLen == 3){
            return 1;
        } else if (wordLen == 4) {
            return 2;
        } else if (wordLen == 5) {
            return 3;
        } else if (wordLen == 6) {
            return 4;
        } else if (wordLen == 7) {
            return 5;
        } else {
            return 6;
        }
    }

    public ArrayList<String> playGame(String filename) {
        ArrayList<String> invalidWords = new ArrayList<String>();
        fileName = filename;
        score = 0;
        insertions = 0;
        finds = 0;
        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {
                WordInfo wi = new WordInfo(sc.next());
                if (this.Dictionary != null) {
                    if (!this.Dictionary.contains(wi.toString().toLowerCase())) {
                        invalidWords.add(wi.toString());
                        continue;
                    }
                }
                WordInfo w = H.find(wi);
                finds++;
                if (w != null) {
                    w.incTimesUsed();
                    score += computeScore(w);
                } else {
                    score += computeScore(wi);
                    H.insert(wi);
                    insertions++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invalidWords;
    }


    public void Print(ArrayList<String> invalidWords) {
        System.out.printf("File: %s\n", this.fileName);
        System.out.printf("Game Score: %d\n", score);
        System.out.printf("     Finds: %d\n", finds);
        System.out.printf("     Probes: %d\n", H.getProbesRequired());
        System.out.printf("     Items in the table: %d\n", insertions);
        System.out.printf("     Length of the table: %d\n", H.capacity());
        if (!invalidWords.isEmpty()) {
            System.out.println("Illegal Words");
        }
        for (String w:invalidWords) {
            System.out.printf("     %s\n", w);
        }
        System.out.println("First 20 non-null elements: (index: word occurrence)");
        System.out.println(H.toString(20));
        System.out.println();
    }

    private int score;
    private int finds;
    private int insertions;
    private String fileName;
    private HashTable<WordInfo> H;
    private HashTable<String> Dictionary;

    public static void main(String[] args) {
        HashTable<String> dictionary = new HashTable<>();
        try {
            Scanner sc = new Scanner(new File("dictionary.txt"));
            while (sc.hasNext()) {
                dictionary.insert(sc.next().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] games = {"game0.txt", "game1.txt", "game2.txt", "game3.txt", "game4.txt"};
         for (String filename : games) {
            GameStarter g = new GameStarter();
            g.Dictionary = dictionary;
            ArrayList<String> invalidWords = g.playGame(filename);
            g.Print(invalidWords);
        }
    }

}
