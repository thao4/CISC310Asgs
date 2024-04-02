
/**
 * Matthew Thao
 * Assignment #5
 * 4/2/2024
 * 
 * The program will use three different page replacement algorithms, FIFO, LRU, and Optimal, to calculate
 * the number of page faults incurred by each algorithm. It will generate a random page-reference string
 * of size 20 and run each of the algorithms using the page frame sizes from 1 to 7. It will then perform
 * the same operations for the given page-reference strings.
 * 
 */

import java.io.FileWriter;
import java.io.IOException;

class Assignment {
    public static FileWriter output;

    public static void main(String[] args) {
        int[] randomNums = new int[20]; // initialize page-reference string to hold 20 pages
        // create randomized page frame of length 20
        for (int i = 0; i < randomNums.length; i++) {
            randomNums[i] = (int) (Math.random() * 10);
        }
        int[] nums1 = { 0, 7, 0, 1, 2, 0, 8, 9, 0, 3, 0, 4, 5, 6, 7, 0, 8, 9, 1, 2 };
        int[] nums2 = { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1 };
        try {
            // outputting the results to a file
            output = new FileWriter("output.txt", false);
            output.append("Matthew Thao, 4.2.2024, Assignment 5.\n");
            run(randomNums); // run for a random page-reference string
            run(nums1); // run for the first given page-reference string
            run(nums2); // run for the second given page-reference string
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(int[] nums) {
        Paging p1 = new Paging();
        int frame;
        try {
            // calculate the page faults in the page-reference string using frame sizes 1-7
            for (int i = 1; i <= 7; i++) {
                frame = i;
                // output page-reference string
                output.append("For " + frame + " page frames, and using string page reference string:\n");
                for (int j = 0; j < nums.length - 1; j++) {
                    output.append(nums[j] + ",");
                }
                output.append(nums[nums.length - 1] + "\n");
                // output results
                output.append("    FIFO had " + p1.FIFO(frame, nums) + " page faults.\n");
                output.append("    LRU had " + p1.LRU(frame, nums) + " page faults.\n");
                output.append("    Optimal had " + p1.optimal(frame, nums) + " page faults.\n\n");
            }
            output.append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class Paging {
    Paging() {
    }

    /**
     * Uses First-In-First-Out Algorithm to calculate the page faults for a given
     * frame size
     * and page-reference string. If the frame is full, pops out the first entry in
     * the frame,
     * and appends the new page at the end of the frame.
     * 
     * @param size - size of the page frame
     * @param nums - page-reference string
     * @return
     */
    public int FIFO(int size, int[] nums) {
        int[] fifo = new int[size];
        // set the values of the frame to invalid entries
        for (int i = 0; i < fifo.length; i++) {
            fifo[i] = Integer.MIN_VALUE;
        }
        int order = 0; // keeps track of where to pop the page from the frame
        int complete = 0; // keeps track of whether the frame is full
        int pageFault = 0;
        // checks for every element in the page-reference string
        for (int i = 0; i < nums.length; i++) {
            // checks if the current page in the page-reference string is in the frame
            if (locate(fifo, nums[i]) == -1) {
                if (complete < size) { // if frame is not full
                    fifo[complete] = nums[i];
                    complete++;
                } else {
                    fifo[order] = nums[i]; // replace the first page in the frame with the new page in the
                                           // page-reference string
                    order = (order + 1) % size;
                }
                pageFault++;
            }
        }
        return pageFault;
    }

    /**
     * Uses Least Recently Used Algorithm for a given page-reference string and a
     * frame size to calculate
     * the page faults. Keeps track of the recencies of the pages in the frame and
     * pops out the least recently
     * used page from the frame.
     * 
     * @param size - size of the page frame
     * @param nums - page-reference string
     * @return
     */
    public int LRU(int size, int[] nums) {
        int[] lru = new int[size];
        // set values of the frame to invalid entries
        for (int i = 0; i < lru.length; i++) {
            lru[i] = Integer.MIN_VALUE;
        }
        int[] recency = new int[size];
        int pointer = 0; // points to the current position in the page frame
        int x; // placeholder for the value to be found in the frame
        int y = 0; // counter to check if the frame is full
        int pageFault = 0;
        // check every element in the page-reference string
        for (int i = 0; i < nums.length; i++) {
            x = locate(lru, nums[i]); // find if the page in the page-reference string is in the frame
            if (x == -1) { // if not found
                if (y < size) { // if frame is not full
                    lru[pointer] = nums[i];
                    recency[pointer] = 0;
                    pointer++;
                    y++;
                } else { // if frame is full
                    pointer = findLeastRecent(recency);
                    lru[pointer] = nums[i]; // replace least recently used page with the new page in the reference
                                            // string
                    recency[pointer] = 0;
                }
                pageFault++;
            } else { // if found, reset the recency of that page to 0
                recency[x] = 0;
            }
            // increase the recency of all the pages by 1
            for (int j = 0; j < recency.length; j++) {
                recency[j]++;
            }
        }
        return pageFault;
    }

    /**
     * Finds the page in the page frame
     * 
     * @param frame - page frame
     * @param page  - page to be found
     * @return
     */
    public int locate(int[] frame, int page) {
        for (int i = 0; i < frame.length; i++) {
            if (frame[i] == page) {
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
    public int findLeastRecent(int[] recent) {
        int leastRecent = 0; // least recently used placeholder
        int leastRecentIndex = 0; // index of least recently used
        // check every element in the recency list to find the least recently used page
        for (int i = 0; i < recent.length; i++) {
            if (recent[i] > leastRecent) {
                leastRecent = recent[i];
                leastRecentIndex = i;
            }
        }
        return leastRecentIndex;
    }

    /**
     * Uses Optimal Page Algorithm for a given page-string reference and a frame
     * size to calculate the number of page
     * faults generated.
     * 
     * @param frame - size of page frame
     * @param nums  - page-reference string
     * @return integer - number of page faults
     */
    public int optimal(int size, int[] nums) {
        int[] opt = new int[size]; // optimal page frame
        // set values of the frame to invalid entries
        for (int i = 0; i < opt.length; i++) {
            opt[i] = Integer.MIN_VALUE;
        }
        int pointer = 0; // points to the current position in the page frame
        int y = 0; // counter to check if page frame is full
        int pageFault = 0;
        // check every element in the page-reference string
        for (int i = 0; i < nums.length; i++) {
            if (locate(opt, nums[i]) == -1) { // check if the page is not in the frame
                if (y < size) { // check if page frame is full
                    opt[pointer] = nums[i];
                    y++;
                    pointer++;
                } else {
                    pointer = findNotLongUsed(opt, i, nums);
                    opt[pointer] = nums[i]; // replace the page that will not be used for long with the new page in the
                                            // reference string
                }
                pageFault++;
            }
        }
        return pageFault;
    }

    /**
     * Finds the page that will not be used for the longest period of time. Uses
     * frequency
     * to find the least likely page to be used.
     * 
     * @param opt   - optimal page frame
     * @param index - index of current page in the page-reference string
     * @param nums  - page-reference string
     * @return integer - location of the page in optimal paging list to be replaced
     */
    public int findNotLongUsed(int[] opt, int index, int[] nums) {
        int frequency; // frequency of the pages
        int notLongUsed = Integer.MAX_VALUE; // frequency of the not long used page
        int notLongUsedIndex = index; // index of the not long used page
        // check every element in the optimal page to see what element will not be used
        // for a long time
        for (int i = 0; i < opt.length; i++) {
            frequency = 0;
            for (int j = index; j < nums.length; j++) {
                if (opt[i] == nums[j]) {
                    frequency++;
                }
            }
            // check if frequency of current page is less than the current not long used
            // page
            if (frequency < notLongUsed) {
                notLongUsed = frequency;
                notLongUsedIndex = i;
            }
        }

        return notLongUsedIndex;
    }
}
