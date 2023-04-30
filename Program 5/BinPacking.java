import java.util.Arrays;
import java.util.Random;

public class BinPacking {
    static int BINSIZE=100;
    int [] requests;

    int onlineBinCount;
    int offLineBinCount;
    LeftistHeap<Disk> queue;
    BinPacking(int size){
        Random rand  = new Random(size); //Seed will cause the same sequence of numbers to be generated each test
        requests = new  int[size];
        for (int i=0; i < size; i++){
            requests[i] =rand.nextInt(BINSIZE)+1;
        }
        System.out.print("Size " + size);
        if (size < 20) {
            System.out.print(" " +Arrays.toString(requests));
        }
        System.out.println();

    }

    public void scheduleWorstFit() {
        LeftistHeap<Disk> leftistHeap = new LeftistHeap<>();
        var maxDiskSize = 100;
        var i = 0;
        for (int request: requests) {
            if (leftistHeap.isEmpty()) {
                var newDisk = new Disk(i, maxDiskSize);
                i++;
                newDisk.add(request);
                leftistHeap.Insert(newDisk);
            } else if (leftistHeap.root.element.remainingSpace - request > 0) {
                var minElement = leftistHeap.deleteMin();
                minElement.remainingSpace -= request;
                minElement.contents += " " + request;
                leftistHeap.Insert(minElement);
            } else {
                var newDisk = new Disk(i, maxDiskSize);
                i++;
                newDisk.add(request);
                leftistHeap.Insert(newDisk);
            }
        }
        queue = leftistHeap;
    }

    public void PrintStats(boolean online) {
        var minbins = 0;
        for (var ct:requests) {
            minbins += ct;
        }
        minbins = Math.ceilDiv(minbins, 100);
        if (online) {
            System.out.printf("OnLine worst Fit Bin Packing Yields %d (requestCT=%d) Minimum number of bins %d", queue.Count(), requests.length, minbins);
        } else {
            System.out.printf("Decreasing Worst Fit Bin Packing Yields %d (requestCT=%d) Minimum number of bins %d", queue.Count(), requests.length, minbins);
        }
        if (requests.length < 20) {
            System.out.println();
            HeapSort<Disk> heapSort = new HeapSort<>();
            var leftistheap = queue.toArray().toArray(new Disk[queue.Count()]);
            heapSort.sort(leftistheap);
            for (var item:leftistheap) {
                System.out.println(item.toString());
            }
        }
        System.out.println();
    }

    public void scheduleOfflineWorstFit() {
        HeapSort<Integer> heapSort = new HeapSort<>();
        Integer[] newArr = new Integer[requests.length];
        int i = 0;
        for (int value:requests) {
            newArr[i++] = value;
        }
        heapSort.sort(newArr);
        requests = new int[requests.length];
        i = 0;
        for (Integer value:newArr) {
            requests[i++] = value;
        }
        if (requests.length < 20) {
            System.out.println("Size "+ requests.length + " " + Arrays.toString(requests));
        }
        scheduleWorstFit();
    }

    public static void main (String[] args)
    {
       int [] fileSizes = {10, 20, 100, 500,10000,100000};
        for (int size :fileSizes){
            BinPacking b = new BinPacking(size);
            b.scheduleWorstFit();
            int onlineBinCount = b.queue.Count();
            b.PrintStats(true);
            b.scheduleOfflineWorstFit();
            int offlineBinCount = b.queue.Count();
            b.PrintStats(false);
            if (onlineBinCount == offlineBinCount) {
                System.out.println("BOTH are the same");
            } else {
                System.out.println("They are different!");
                double percentage = (double)onlineBinCount / (double)offlineBinCount * 100;
                System.out.printf("Sorted is better by: %s%%", (int)percentage);
            }
            System.out.println();
            System.out.println("-----------------------------------------------");
        }

    }}
