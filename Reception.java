/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues by Pao Yu
 */

import java.util.Comparator;
import java.util.ArrayList;
/**
 * Represents a bank receptionist which can receive customers and place them in multiple queues.
 * Extends the thread class so it is able to operate and run its tasks independently from other objects.
 */
public class Reception extends Thread {
    
    private static int timeAcceleration = 1;    /* accelerates the time using an integer factor */
    private Queue<Customer> queue1;             /* the 1st queue the receptionist is in charge of */
    private Queue<Customer> queue2;             /* the 2nd queue the receptionist is in charge of */
    private Queue<Customer> queue3;             /* the 3rd queue the receptionist is in charge of */
    ArrayList<Queue<Customer>> queueList;       /* the 1st, 2nd and 3rd queues as an array list */
    private static int totalCustomersRemaining = 0; /* the remaining customers the receptionist must serve */
    private static int totalCustomersArrived = 0;   /* the total customers placed in the queue */
    private int[] randomEnqueueTime;            /* a data set containing random integers */
    private boolean multiQueueEnabled;          /* enables/disables multi-queue capability */
    private boolean debugMode = true;           /* enables/disables console output display */

    /**
     * Constructs a reception object in charge of 1 queue.
     * @param queue the queue that the reception will funnel customers to
     * @param totalMaxCustomers the target customer number reception will serve
     * @param timeAccelerationFactor the speed of time the reception will operate
     * @param randomEnqueueTime a random data set of integers to control enqueue times
     */
    public Reception (Queue<Customer> queue1, int totalMaxCustomers, int timeAccelerationFactor, int[] randomEnqueueTime) { 
        this.queue1 = queue1;
        this.randomEnqueueTime = randomEnqueueTime;
        totalCustomersRemaining = totalMaxCustomers;
        if (timeAccelerationFactor <= 0) {
            timeAcceleration = 1;
        } else {
            timeAcceleration = timeAccelerationFactor;
        }
    }

    /**
     * Constructs a reception object in charge of 3 queues.
     * @param queue1 a queue that the reception will funnel customers to
     * @param queue2 a queue that the reception will funnel customers to
     * @param queue3 a queue that the reception will funnel customers to
     * @param totalMaxCustomers the target customer number reception will serve
     * @param timeAccelerationFactor the speed of time the reception will operate
     * @param randomEnqueueTime a random data set of integers to control enqueue times
     * @param enableMultiQueue switches the operation of the reception to handle multiple queues
     */
    public Reception (Queue<Customer> queue1, Queue<Customer> queue2, Queue<Customer> queue3, int totalMaxCustomers, int timeAccelerationFactor, int[] randomEnqueueTime, boolean enableMultiQueue) { 
        this.multiQueueEnabled = enableMultiQueue;
        this.queue1 = queue1;
        this.queue2 = queue2;
        this.queue3 = queue3;
        this.randomEnqueueTime = randomEnqueueTime;
        totalCustomersRemaining = totalMaxCustomers;
        if (timeAccelerationFactor <= 0) {
            timeAcceleration = 1;
        } else {
            timeAcceleration = timeAccelerationFactor;
        }
        queueList = new ArrayList<Queue<Customer>>();
        queueList.add(queue1);
        queueList.add(queue2);
        queueList.add(queue3);
    }

    /**
     * The main Thread task that the reception object will run upon starting.
     * Will run until the target customer level is reached. A time delay will
     * activate depending on the current customer count. Will add the customer
     * the shortest queue if multiQueue is enabled.
     */
    public void run() {
        do {
            controlledRandomTimePasses(totalCustomersArrived);
            System.out.flush();
            if (multiQueueEnabled) {
                addCustomerToShortestQueue();
            } else {
                addCustomerToQueue();
            }
            totalCustomersArrived += 1;
        } while (totalCustomersRemaining > 0);
    }

    /**
     * Adds a customer to the receptionist's queue if there are only 1.
     */
    public void addCustomerToQueue() {
        if (!queue1.isFull()) {
            Customer anonymousCustomer = new Customer();
            queue1.enqueue(anonymousCustomer);
            if (debugMode)
                System.out.println("Customer " + (anonymousCustomer.getCustomerID() + 1) + " added to Queue " + queue1.getQueueID() + ".");
            totalCustomersRemaining -= 1;
        }
    }

    /**
     * Adds a customer to the shortest queue by sorting the queues in the array list according to size, and placing the customer in the queue with the smallest size
     */
    public void addCustomerToShortestQueue() {
        synchronized (queueList) {
        queueList.sort(new QueueSizeComparator());
            if (!queueList.get(0).isFull()) {
                Customer anonymousCustomer = new Customer();
                queueList.get(0).enqueue(anonymousCustomer);
                if (debugMode)
                    System.out.println("Customer " + (anonymousCustomer.getCustomerID() + 1) + " added to Queue " + queueList.get(0).getQueueID() + ".");
                totalCustomersRemaining -= 1;
            }
        }
    }

    /**
     * Generate a time delay based on a random integer data set provided.
     * @param randomDataIndex the current item in the data set to be used for the time delay.
     */
    public void controlledRandomTimePasses(int randomDataIndex) {
        try {
            int randomCustomerArrivalDelay = randomEnqueueTime[randomDataIndex];
            sleep((1000 / timeAcceleration) * randomCustomerArrivalDelay);
        } catch (Exception e) {
            System.out.println("Error: Unable to delay time!");
        }
    }

    /**
     * A comparator object that enables comparison between queues and their sizes
     */
    public class QueueSizeComparator implements Comparator<Queue<Customer>> {
        @Override
        public int compare(Queue<Customer> q1, Queue<Customer> q2) {
        return Integer.compare(q1.size(), q2.size());
        }
    }

    /**
     * Gets the ID of the queue instances
     * @return the IDs of all the queue objects in a multiqueue simulation
     */
    public String[] getQueueIDs() {
        String[] queueIDs = new String[3];
        queueIDs[0] = queue1.toString();
        queueIDs[1] = queue2.toString();
        queueIDs[2] = queue3.toString();
        return queueIDs;
    }

    /**
     * Resets the class for use in another simulation.
     */
    public static void resetClass() {
        timeAcceleration = 1;
        totalCustomersRemaining = 0;
        totalCustomersArrived = 0;
    }
}
