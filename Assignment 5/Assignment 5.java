import java.util.*;

class Assignment {
    public static void main(String[] args){
        int[] nums = {4,1,8,7,3,6,6,2,7,6,7,8,8,1,1,7,4,8,8,8};
        int[] nums2 = {0,2,1,6,4,0,1,0,3,1,2,1};
        Paging p1 = new Paging(nums2);
        int frame = 4;
        p1.FIFO(frame);
        p1.LRU(frame);
        p1.optimal(frame);
    }

}

class Paging {
    int[] nums;
    Paging(int[] nums){
        this.nums = nums;
    }

    public void FIFO(int frame){
        Queue<Integer> fifo = new LinkedList<Integer>();
        int pageFault = 0;
        for(int i = 0; i < this.nums.length;i++){
            if(!fifo.contains(nums[i])){
                if(fifo.size()<frame){
                    fifo.offer(nums[i]);
                } else {
                    fifo.poll();
                    fifo.offer(nums[i]);
                }
                pageFault++;
            }
        }
        System.out.println("FIFO had "+pageFault+" page faults.");
    }
    
    public void LRU(int frame){
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
                    pointer = findMostRecent(recency);
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
        System.out.println("LRU had "+pageFault+" page faults.");
    }

    public int locate(int[] nums, int num){
        for(int i = 0; i<nums.length;i++){
            if(nums[i]==num){
                return i;
            }
        }
        return -1;
    }

    public int findMostRecent(int[] recent){
        int max = 0;
        int maxIndex = 0;
        for(int i = 0;i<recent.length;i++){
            if(recent[i]>max){
                max = recent[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void optimal(int frame){
        int[] opt = new int[frame];
        int pointer=0;
        int y=0;
        int pageFault=0;
        for(int i=0;i<nums.length;i++){
            if(locate(opt,nums[i])==-1){
                if(y<frame){
                    opt[pointer] = nums[i];
                    y++;
                    pointer++;
                } else {
                    pointer = findLeastUsed(opt);
                    opt[pointer] = nums[i];
                }
                pageFault++;
            }
        }
        System.out.println("Optimal had "+pageFault+" page faults.");
    }
    public int findLeastUsed(int[] opt){
        int frequency;
        int leastUsed=Integer.MAX_VALUE;
        int leastUsedIndex=0;
        for(int i = 0; i<opt.length;i++){
            frequency = 0;
            for(int j=0;j<nums.length;j++){
                if(opt[i]==nums[j]){
                    frequency++;
                }
                if(frequency < leastUsed){
                    frequency = leastUsed;
                    leastUsedIndex=i;
                }
            }
        }
        return leastUsedIndex;
    }
}
