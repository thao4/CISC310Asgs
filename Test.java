/**
 * 
 */

 
 class Test {
     public static void main(String[] args){
         int[] nums = {7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1
         };
         //int[] nums = new int[20];
         /*for(int i = 0; i < nums.length;i++){
             nums[i] = (int)(Math.random()*10);
         }*/
         //int[] nums2 = {0,2,1,6,4,0,1,0,3,1,2,1};
         Pagings p1 = new Pagings();
         int frame;
         for(int i = 1; i <= 7;i++){
            frame = i;
            System.out.println("For "+frame+" page frames, and using string page reference string:");
            for(int j = 0; j < nums.length-1;j++){
                System.out.print(nums[j]+",");
            }
            System.out.print(nums[nums.length-1]+"\n");
            System.out.println("    FIFO had "+p1.FIFO(frame,nums)+" page faults.");
            System.out.println("    LRU had "+p1.LRU(frame,nums)+" page faults.");
            System.out.println("    Optimal had "+p1.optimal(frame,nums)+" page faults.\n\n");
        }
     }
 
 }
 
 class Pagings {
     Pagings(){
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
 
     public int optimal(int frame, int[] nums){
         int[] opt = new int[frame];
         for(int i = 0;i<opt.length;i++){
             opt[i] = Integer.MIN_VALUE;
         }
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
                     pointer = findNotLongUsed(opt,i,nums);
                     opt[pointer] = nums[i];
                 }
                 pageFault++;
             }
         }
         return pageFault;
     }
     public int findNotLongUsed(int[] opt,int index, int[] nums){
         int frequency;
         int notLongUsed = Integer.MAX_VALUE;
         int notLongUsedIndex = index;
         for(int i = 0; i < opt.length;i++){
             frequency = 0;
             for(int j = index; j< nums.length;j++){
                 if(opt[i]==nums[j]){
                     frequency++;
                 }
             }
             if(frequency < notLongUsed){
                 notLongUsed = frequency;
                 notLongUsedIndex = i;
             }
         }
 
         return notLongUsedIndex;
     }
 }
 