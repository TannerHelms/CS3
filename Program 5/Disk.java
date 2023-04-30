public class Disk implements Comparable<Disk>{
    String contents;
    int remainingSpace;
    int id;

    Disk(int id, int maxSize){
       contents="";
       remainingSpace = maxSize;
       this.id = id;
     }

     public boolean add(int oneFileSize){
        if (remainingSpace<oneFileSize) return false;
        remainingSpace-=oneFileSize;
        contents+= " "  + oneFileSize;
        return true;
     }
     public String toString(){
        return id + "(" + remainingSpace+")  :"+ contents;
     }

    @Override
    public int compareTo(Disk o) {
        return Integer.compare(this.remainingSpace, o.remainingSpace);
    }
}
