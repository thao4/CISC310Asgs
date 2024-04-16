
/**
 * Matthew Thao
 * Assignment #6
 * 4/16/2024
 * 
 * The program will use six different disk scheduling algorithms to service all the requests in a disk. 
 * A data file will be read from and used to calculate the total head movement of each algorithm. The 
 * data file will provide a starting position of the head, the number of cylinders in the disk, and 
 * the list of requests in the disk. The algorithms used are FCFS, SSTF, SCAN, CSCAN, LOOK, and CLOOK.
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

class Asg6 {
    public static FileWriter output;

    public static void main(String[] args) {
        // first data set
        int cylinders1;
        int startPos1;
        String[] strData1;
        ArrayList<Integer> data1 = new ArrayList<>();
        // second data set
        int cylinders2;
        int startPos2;
        String[] strData2;
        ArrayList<Integer> data2 = new ArrayList<>();

        try {
            // create file to write to
            output = new FileWriter("output.txt", false);
            output.append("Matthew Thao, 4.16.2024, Assignment 6.\n");

            // read Asg6Data.txt and collect data values to be used
            File file = new File("Asg6Data.txt");
            Scanner reader = new Scanner(file);

            // first set of data
            cylinders1 = Integer.valueOf(reader.nextLine());
            startPos1 = Integer.valueOf(reader.nextLine());
            strData1 = reader.nextLine().split(" ");
            for (int i = 0; i < strData1.length; i++) {
                data1.add(Integer.valueOf(strData1[i]));
            }

            // second set of data
            cylinders2 = Integer.valueOf(reader.nextLine());
            startPos2 = Integer.valueOf(reader.nextLine());
            strData2 = reader.nextLine().split(" ");
            for (int i = 0; i < strData2.length; i++) {
                data2.add(Integer.valueOf(strData2[i]));
            }
            reader.close();

            // print results of first data set using disk scheduling algorithms
            output.append(
                    "\nFor FCFS, the total head movement was " + FCFS(cylinders1, startPos1, data1) + " cylinders.");
            output.append(
                    "\nFor SSTF, the total head movement was " + SSTF(cylinders1, startPos1, data1) + " cylinders.");
            output.append(
                    "\nFor SCAN, the total head movement was " + SCAN(cylinders1, startPos1, data1) + " cylinders.");
            output.append(
                    "\nFor CSCAN, the total head movement was " + CSCAN(cylinders1, startPos1, data1) + " cylinders.");
            output.append(
                    "\nFor LOOK, the total head movement was " + LOOK(cylinders1, startPos1, data1) + " cylinders.");
            output.append(
                    "\nFor CLOOK, the total head movement was " + CLOOK(cylinders1, startPos1, data1) + " cylinders.");

            // print results of second data set using disk scheduling algorithms
            output.append("\n\n\nFor FCFS, the total head movement was " + FCFS(cylinders2, startPos2, data2)
                    + " cylinders.");
            output.append(
                    "\nFor SSTF, the total head movement was " + SSTF(cylinders2, startPos2, data2) + " cylinders.");
            output.append(
                    "\nFor SCAN, the total head movement was " + SCAN(cylinders2, startPos2, data2) + " cylinders.");
            output.append(
                    "\nFor CSCAN, the total head movement was " + CSCAN(cylinders2, startPos2, data2) + " cylinders.");
            output.append(
                    "\nFor LOOK, the total head movement was " + LOOK(cylinders2, startPos2, data2) + " cylinders.");
            output.append(
                    "\nFor CLOOK, the total head movement was " + CLOOK(cylinders2, startPos2, data2) + " cylinders.");

            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process service requests from the order they are given. Operates on a
     * first-come first-serve basis.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int FCFS(int cylinders, Integer startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        for (int i = 0; i < data.size(); i++) {
            seek_count += calcDistance(data.get(i), startPos);
            // update head
            startPos = data.get(i);
        }
        return seek_count;
    }

    /**
     * Copies the elements from data into a new ArrayList. (done to prevent changing
     * original data)
     * 
     * @param data - list of service requests in the disk
     * @return a copy of data
     */
    public static ArrayList<Integer> copyData(ArrayList<Integer> data) {

        ArrayList<Integer> data2 = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            data2.add(data.get(i));
        }
        return data2;
    }

    /**
     * Process requests that are closest to the current head position.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int SSTF(int cylinders, Integer startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        ArrayList<Integer> data2 = copyData(data); // duplicate data for modification purposes
        Integer minPos;
        // keep going until all requests have been serviced
        while (data2.size() > 0) {
            // find the closest request to the current head
            minPos = findMinDistance(data2, startPos);
            seek_count += calcDistance(minPos, startPos);
            // remove request from list of requests
            if (data2.contains(startPos)) {
                data2.remove(startPos);
            }
            // update head
            startPos = minPos;
        }
        return seek_count;
    }

    /**
     * SSTF helper method to find the closest request to the current head
     * 
     * @param data - list of service requests in disk
     * @param pos  - head position
     * @return
     */

    private static int findMinDistance(ArrayList<Integer> data, int pos) {
        int minDistance = Integer.MAX_VALUE; // set minDistance to invalid value
        int distance;
        int minDistanceIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            distance = calcDistance(data.get(i), pos);
            // update shortest position if distance between head and current position is the
            // lowest
            if (distance < minDistance) {
                minDistance = distance;
                minDistanceIndex = i;
            }
        }
        return data.get(minDistanceIndex);
    }

    /**
     * Process requests by scanning requests to the right of the head, then checks
     * left
     * until all requests are done.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int SCAN(int cylinders, int startPos, ArrayList<Integer> data) {
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();
        right.add(cylinders - 1); // check all the way right
        int seek_count = 0;
        // create ordering to process requests
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < startPos) {
                left.add(data.get(i));
            }
            if (data.get(i) > startPos) {
                right.add(data.get(i));
            }
        }
        Collections.sort(right);
        Collections.sort(left);

        // explore all the way right first
        for (int i = 0; i < right.size(); i++) {
            seek_count += calcDistance(startPos, right.get(i));
            startPos = right.get(i);
        }

        // start exploring left until all requests are serviced
        for (int i = left.size() - 1; i >= 0; i--) {
            seek_count += calcDistance(startPos, left.get(i));
            startPos = left.get(i);
        }
        return seek_count;
    }

    /**
     * Process requests by making a cycle starting at head and scanning all requests
     * as it goes in a cycle. It starts scanning the right side of the head first.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int CSCAN(int cylinders, int startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();

        // create ordering to process requests
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < startPos) {
                left.add(data.get(i));
            }
            if (data.get(i) > startPos) {
                right.add(data.get(i));
            }
        }
        left.add(0); // left side of cylinder
        right.add(cylinders - 1); // right side of cylinder
        Collections.sort(left);
        Collections.sort(right);

        // scan all the way right of head first
        for (int i = 0; i < right.size(); i++) {
            seek_count += calcDistance(right.get(i), startPos);
            startPos = right.get(i);
        }

        // come back to start and start scanning until all requests are completed
        for (int i = 0; i < left.size(); i++) {
            seek_count += calcDistance(left.get(i), startPos);
            startPos = left.get(i);
        }
        return seek_count;
    }

    /**
     * Process requests by looking for all requests in one direction, then goes in
     * the
     * opposite direction to finish all requests in disk.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int LOOK(int cylinders, int startPos, ArrayList<Integer> data) {
        int seek_count = 0;

        // create ordering to process requests
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < startPos) {
                left.add(data.get(i));
            }
            if (data.get(i) > startPos) {
                right.add(data.get(i));
            }
        }
        Collections.sort(left);
        Collections.sort(right);

        // process all requests on the right of the head
        for (int i = 0; i < right.size(); i++) {
            seek_count += calcDistance(right.get(i), startPos);
            startPos = right.get(i);
        }

        // once all requests on the right of the head are done,
        // come back and finish all requests on the left side
        for (int i = left.size() - 1; i >= 0; i--) {
            seek_count += calcDistance(left.get(i), startPos);
            startPos = left.get(i);
        }
        return seek_count;
    }

    /**
     * Process requests by looking for all requests in one direction of the head,
     * then cycle to the farthest request in the opposite direction and continue
     * processing requests in the same direction until all requests are serviced.
     * 
     * @param cylinders - number of cylinders
     * @param startPos  - starting position / head
     * @param data      - list of service requests in the disk
     * @return - total seek count travelled to service the requests in the disk
     */

    public static int CLOOK(int cylinders, int startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        // create ordering to process requests
        ArrayList<Integer> left = new ArrayList<>();
        ArrayList<Integer> right = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < startPos) {
                left.add(data.get(i));
            }
            if (data.get(i) > startPos) {
                right.add(data.get(i));
            }
        }
        Collections.sort(left);
        Collections.sort(right);

        // process all requests on the right of the head
        for (int i = 0; i < right.size(); i++) {
            seek_count += calcDistance(right.get(i), startPos);
            startPos = right.get(i);
        }

        // cycle back to the farthest request on the left side and
        // continue processing in the same direction until all
        // requests are serviced
        for (int i = 0; i < left.size(); i++) {
            seek_count += calcDistance(left.get(i), startPos);
            startPos = left.get(i);
        }
        return seek_count;
    }

    /**
     * Helper method for the disk algorithms to calculate
     * the distance between two requests in the disk.
     * 
     * @param pos1 - first position
     * @param pos2 - second position
     * @return
     */
    public static int calcDistance(int pos1, int pos2) {
        return Math.abs(pos1 - pos2);
    }
}
