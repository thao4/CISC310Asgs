/**
 * 
 */


import java.io.FileWriter;
import java.io.IOException;

class Assignment {
    public static void main(String[] args){
        int[] nums = new int[20];
        Paging p1 = new Paging();
        int frame;
        try {
            FileWriter output = new FileWriter("output.txt",false);
            output.append("Matthew Thao, 4.2.2024, Assignment 5.\n");
            for(int x = 0; x < 3; x++){
                for(int i = 0; i < nums.length;i++){
                    nums[i] = (int)(Math.random()*10);
                }
                for(int i = 1; i <= 7;i++){
                    frame = i;
                    output.append("For "+frame+" page frames, and using string page reference string:\n");
                    for(int j = 0; j < nums.length-1;j++){
                        output.append(nums[j]+",");
                    }
                    output.append(nums[nums.length-1]+"\n");
                    output.append("    FIFO had "+p1.FIFO(frame,nums)+" page faults.\n");
                    output.append("    LRU had "+p1.LRU(frame,nums)+" page faults.\n");
                    output.append("    Optimal had "+p1.optimal(frame,nums)+" page faults.\n\n");
                }
                output.append("\n");
            }
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class Paging {
    Paging(){
    }

    public int FIFO(int frame, int[] nums){
        int[] fifo = new int[frame];
        for(int i = 0;i<fifo.length;i++){
            fifo[i] = Integer.MIN_VALUE;
        }
        int order = 0;
        int complete = 0;
        int pageFault = 0;
        for(int i = 0; i < nums.length;i++){
            if(locate(fifo,nums[i])==-1){
                if(complete<frame){
                    fifo[complete] = nums[i];
                    complete++;
                } else {
                    fifo[order] = nums[i];
                    order=(order+1)%frame;
                }
                pageFault++;
            }
        }
        return pageFault;
    }
    
    public int LRU(int frame, int[] nums){
        int[] lru = new int[frame];
        for(int i = 0;i<lru.length;i++){
            lru[i] = Integer.MIN_VALUE;
        }
        int[] recency = new int[frame];
        int pointer = 0;
        int x;
        int y=0;
        int pageFault = 0;
        for(int i = 0; i < nums.length;i++){
            x = locate(lru,nums[i]);
            if(x==-1){
                if(y<frame){
                    lru[pointer] = nums[i];
                    recency[pointer] = 0;
                    pointer++;
                    y++;
                } else {
                    pointer = findLeastRecent(recency);
                    lru[pointer] = nums[i];
                    recency[pointer]=0;
                }
                pageFault++;
            } else {
                recency[x]=0;
            }
            for(int j = 0;j<recency.length;j++){
                recency[j]++;
            }
        }
        return pageFault;
    }

    public int locate(int[] nums, int num){
        for(int i = 0; i<nums.length;i++){
            if(nums[i]==num){
                return i;
            }
        }
        return -1;
    }
    /**
     * Finds the least recent page in the frame
     * 
     * @param recent - recency list of the pages in the frame
     * @return integer - location of the least recently used page
     */
    public int findLeastRecent(int[] recent){
        int leastRecent = 0; // least recently used placeholder
        int leastRecentIndex = 0; // index of least recently used
        // check every element in the recency list to find the least recently used page
        for(int i = 0;i<recent.length;i++){
            if(recent[i]>leastRecent){
                leastRecent = recent[i];
                leastRecentIndex = i;
            }
        }
        return leastRecentIndex;
    }
    /**
     * Uses Optimal Page Algorithm for a given page-string reference and a frame size to calculate the number of page
     * faults generated.
     * 
     * @param frame - size of page frame
     * @param nums - page-reference string
     * @return integer - number of page faults
     */
    public int optimal(int frame, int[] nums){
        int[] opt = new int[frame]; // optimal page frame
        // set values of the frame to invalid entries
        for(int i = 0;i<opt.length;i++){ 
            opt[i] = Integer.MIN_VALUE;
        }
        int pointer=0; // points to the current position in the page frame
        int y=0; // counter to check if page frame is full
        int pageFault=0; 
        // check every element in the page-reference string
        for(int i=0;i<nums.length;i++){
            if(locate(opt,nums[i])==-1){ // check if the page is not in the frame
                if(y<frame){ // check if page frame is full
                    opt[pointer] = nums[i];
                    y++;
                    pointer++;
                } else {
                    pointer = findNotLongUsed(opt,i,nums); //finds page that will not be used for long
                    opt[pointer] = nums[i];
                }
                pageFault++;
            }
        }
        return pageFault;
    }

    /**
     * Finds the page that will not be used for the longest period of time. Uses frequency 
     * to find the least likely page to be used.
     * 
     * @param opt - optimal page frame
     * @param index - index of current page in the page-reference string
     * @param nums - page-reference string
     * @return integer - location of the page in optimal paging list to be replaced
     */
    public int findNotLongUsed(int[] opt,int index, int[] nums){
        int frequency; // frequency of the pages
        int notLongUsed = Integer.MAX_VALUE; // frequency of the not long used page
        int notLongUsedIndex = index; // index of the not long used page
        // check every element in the optimal page to see what element will not be used for a long time
        for(int i = 0; i < opt.length;i++){
            frequency = 0;
            for(int j = index; j< nums.length;j++){
                if(opt[i]==nums[j]){
                    frequency++;
                }
            }
            // check if frequency of current page is less than the current not long used page
            if(frequency < notLongUsed){
                notLongUsed = frequency;
                notLongUsedIndex = i;
            }
        }

        return notLongUsedIndex;
    }
}
