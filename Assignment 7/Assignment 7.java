import java.util.*;
import java.io.*;

class Asg7 {
    static int data = 0;
    static Object lock = new Object();

    /**
     * The Producer class will generate an instance of a thread that can be run by other processes.
     * In the class, it will loop 5 times and on each loop, it will wait a random amount of time between 1 and 3 seconds.
     * After waiting, it will add the loop count to a shared variable.
     */
    static class Producer implements Runnable {
        Random rand = new Random();
        int counter = 0;

        @Override
        public void run() {
            while(counter != 8) {
                // wait x amount of seconds
                synchronized (lock) {
                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                counter++;
                // add loop count to shared variable
                data += counter;
            }
        }
    }

    /**
     * The Consumer class will generate an instance of a thread that can be run by other processes.
     * In the class, it will loop 5 times and on each loop, it will wait a random amount of time between 1 and 3 seconds.
     * After waiting, it will add the current shared variable value to a sum. Once it finishes
     * looping, it will output the sum into a file.
     */
    static class Consumer implements Runnable {
        Random rand = new Random();
        int sum = 0;

        @Override
        public void run() {
            while (data>=0) {
                // wait x amount of seconds
                synchronized (lock) {

                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add current shared variable to sum
                sum += data;
            }
            // output the sum into a file
            FileWriter output = null;
            try {
                output = new FileWriter("output.txt",true);
                output.append("\nThe sum is " + sum);
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Main will create both threads and run them simultaneously.
     */
    public static void main(String[] args) {
        Thread prod = new Thread(new Producer());
        Thread con = new Thread(new Consumer());

        prod.start();
        con.start();
    }
}
