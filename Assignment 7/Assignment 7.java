
/**
 * Matthew Thao
 * Assignment #7
 * 4/30/2024
 * 
 * The program is similar to assignment 2, where there is a producer and a consumer. Both will share an integer variable data and a lock. 
 * The producer will do its work when there the lock is open. Once it finishes, it will wait until the consumer has accessed the data section.
 * Both producer and consumer will wait a random amount of time from 1 to 2 seconds. When either enters the data section, they will lock
 * the data and reopen once it completes. The program will end once the producer has reached a counter of 8.
 * 
 */

import java.util.*;
import java.io.*;

class Asg7 extends Thread {
    static int data = 0; // shared data between producer and consumer
    static Object lock; // shared lock

    /**
     * The Producer class will generate an instance of a thread that can be run by
     * other processes. In the class, it will keep looping until it finishes doing
     * work once it reaches a counter of 8.
     * On each loop, it will wait a random amount of time between 1 and 2 seconds.
     * After waiting, it will check if the critical section has been locked.
     * If it is not locked, it will check if the data section has been accessed by
     * the consumer.
     * If it has not been accessed by the consumer (data is not 0), then it resets
     * the lock.
     * Otherwise, it will increment the counter and set the shared data variable to
     * the counter, then it will reset the lock.
     * Once the counter has gone up to 8, it will set the data to -1 and signal to
     * the consumer that the producer has finished.
     */

    static class Producer implements Runnable {
        Random rand = new Random();
        int counter = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    // wait x amount of seconds
                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (lock == null) { // if critical section is open
                        lock = new Object();
                        if (data == 0) { // data has been accessed by consumer or thread just started
                            counter++;
                            if (counter == 8) { // producer finishes running
                                data = -1; // signal to consumer that producer is finished
                                lock = null; // reset lock
                                break;
                            } else {
                                data = counter;
                                lock = null;
                            }
                        } else { // data has not been accessed by consumer
                            lock = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * The Consumer class will generate an instance of a thread that can be run by
     * other processes.
     * In the class, it will keep looping until the Producer is finished. On every
     * iteration,
     * it will wait a random amount of time between 1 and 2 seconds.
     * After waiting, it will check if the critical section has been locked.
     * If it is not locked, it will add whatever is in the shared data variable to a
     * sum.
     * Otherwise, it will keep waiting until the critical section is open.
     * Once it finishes looping, it will output the sum into a file.
     */
    static class Consumer implements Runnable {
        Random rand = new Random();
        int sum = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    // wait x amount of seconds
                    try {
                        wait((rand.nextInt(2) + 1) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (lock == null) { // if critical section is open
                        // lock the critical section
                        lock = new Object();
                        if (data == 0) { // data has not been accessed by the producer
                            lock = null; // reset lock
                        } else if (data > 0) { // data has been accessed by the producer
                            sum += data;
                            data = 0;
                            lock = null;
                        } else if (data < 0) { // producer has finished work
                            lock = null;
                            break;
                        }
                    }
                }
            }
            // output the sum into a file
            FileWriter output = null;
            try {
                output = new FileWriter("output.txt", true);
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
        prod.start(); // start producer thread
        con.start(); // start consumer thread
    }
}