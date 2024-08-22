
public class Orderlies {
    // total number of orderlies
    private final int totalOrderlies; 

    // number of available orderlies
    private int availableOrderlies; 

    // constructor
    public Orderlies() {
        this.totalOrderlies = Params.ORDERLIES;
        this.availableOrderlies = totalOrderlies; // 初始时，所有勤务员均可用
    }

    // request for orderlies
    public synchronized void requestOrderlies() throws InterruptedException {
        // if the rest orderlies are not enough, wait
        while (availableOrderlies < Params.TRANSFER_ORDERLIES) {
            wait();
        }

        // allocate 3 orderlies
        availableOrderlies -= Params.TRANSFER_ORDERLIES;
        //System.out.println(Params.TRANSFER_ORDERLIES + " orderlies have been assigned to transfer a patient. Here are "+ availableOrderlies+ "left");
        
    }

    // release oderlies
    public synchronized void releaseOrderlies() throws InterruptedException {
        // release 3 orderlies
        availableOrderlies += Params.TRANSFER_ORDERLIES;
        // System.out.println(Params.TRANSFER_ORDERLIES + " orderlies have been released after transferring a patient. Here are "+ availableOrderlies+ "left");
        // 
        notifyAll();
    }

    // check the number of orderlies
    public synchronized int getAvailableOrderlies() {
        return availableOrderlies;
    }



}
