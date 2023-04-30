import java.util.Objects;

public class WordInfo {
    private int hashCode;
    private final String word;

    private int timesUsed;

    public void incTimesUsed(){
        this.timesUsed++;
    }
    public int getTimesUsed(){
        return this.timesUsed;
    }

    public WordInfo(String word){
        this.hashCode = 0;
        this.word = word;
        this.timesUsed = 0;
        this.hashCode = word.hashCode();
    }
    @Override
    public String toString(){
        return this.word;
    }

    @Override
    public int hashCode(){
        return this.hashCode;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WordInfo)){return false;};
        WordInfo w = (WordInfo) o;
        if (this.word.equals(w.word)){return true;}
        return false;
    }
}
