import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

class Asg6 {
    public static FileWriter output;

    public static void main(String[] args) {
        int cylinders1;
        int startPos1;
        int cylinders2;
        int startPos2;
        String[] strData1;
        String[] strData2;
        ArrayList<Integer> data1 = new ArrayList<>();
        ArrayList<Integer> data2 = new ArrayList<>();
        try {
            output = new FileWriter("output2.txt", false);
            output.append("Matthew Thao, 4.2.2024, Assignment 6.\n");
            File file = new File("Asg6Data.txt");
            Scanner reader = new Scanner(file);
            cylinders1 = Integer.valueOf(reader.nextLine());
            startPos1 = Integer.valueOf(reader.nextLine());
            strData1 = reader.nextLine().split(" ");
            for (int i = 0; i < strData1.length; i++) {
                data1.add(Integer.valueOf(strData1[i]));
            }
            cylinders2 = Integer.valueOf(reader.nextLine());
            startPos2 = Integer.valueOf(reader.nextLine());
            strData2 = reader.nextLine().split(" ");
            for (int i = 0; i < strData2.length; i++) {
                data2.add(Integer.valueOf(strData2[i]));
            }
            reader.close();
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

    public static int FCFS(int cylinders, Integer startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        for (int i = 0; i < data.size(); i++) {
            seek_count += calcDistance(data.get(i), startPos);
            startPos = data.get(i);
        }
        return seek_count;
    }

    public static int SSTF(int cylinders, Integer startPos, ArrayList<Integer> data) {
        int seek_count = 0;
        Integer minPos;
        while (data.size() > 0) {
            minPos = findMinDistance(data, startPos);
            seek_count += calcDistance(minPos, startPos);
            if (data.contains(startPos)) {
                data.remove(startPos);
            }
            startPos = minPos;
        }
        return seek_count;
    }

    private static int findMinDistance(ArrayList<Integer> data, int pos) {
        int minDistance = Integer.MAX_VALUE;
        int distance;
        int minDistanceIndex = 0;
        for (int i = 0; i < data.size(); i++) {
            distance = calcDistance(data.get(i), pos);
            if (distance < minDistance) {
                minDistance = distance;
                minDistanceIndex = i;
            }
        }
        return data.get(minDistanceIndex);
    }

    public static int SCAN(int cylinders, int startPos, ArrayList<Integer> data) {
        return -1;
    }

    public static int CSCAN(int cylinders, int startPos, ArrayList<Integer> data) {
        return -1;
    }

    public static int LOOK(int cylinders, int startPos, ArrayList<Integer> data) {
        return -1;
    }

    public static int CLOOK(int cylinders, int startPos, ArrayList<Integer> data) {
        return -1;
    }

    public static int calcDistance(int pos1, int pos2) {
        return Math.abs(pos1 - pos2);
    }
}