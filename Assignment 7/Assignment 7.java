import java.util.*;
import java.io.*;

class Asg7 {
    static int num = 100;

    /**
     * The Producer class will generate an instance of a thread that can be run by other processes.
     * In the class, it will loop 5 times and on each loop, it will wait a random amount of time between 1 and 3 seconds.
     * After waiting, it will add the loop count to a shared variable.
     */
    static class Producer implements Runnable {
        Random rand = new Random();

        @Override
        public void run() {
            for (int x = 0; x <= 4; x++) {
                // wait x amount of seconds
                synchronized (this) {
                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add loop count to shared variable
                num += x;
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
            for (int x = 0; x <= 4; x++) {
                // wait x amount of seconds
                synchronized (this) {

                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add current shared variable to sum
                sum += num;
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
