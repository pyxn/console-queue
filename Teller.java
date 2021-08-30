/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues by Pao Yu
 */

/**
 * A Teller represents a bank teller that can receive and dequeue customers from a queue.
 * Extends Thread to be able to operate independently from the main thread and other objects.
 */
public class Teller extends Thread {

    private static int timeAcceleration = 1;    /* accelerates time based on a factor */
    private static int totalCustomersProcessed = 0; /* tracks customers processed by all tellers */
    private static int totalCustomersProcessedFinal = 0;    /* tracks final customers processed */
    private static int totalCustomersTarget = 0;    /* the target customers to be processed */
    private static int nextTellerID = 1;    /* class-level id tracker */
    private static int lastID = -1; /* ID of the last customer served */
    private static double customerWaitTimeTotal = 0;    /* aggregated customer wait time */
    private static double customerWaitTimeAverage = 0;  /* aggregated customer avg wait time */
    private static boolean bankIsOpen = true;   /* tracks whether Tellers should stop operations */

    private boolean debugMode = true;   /* enables consolue output display */
    private int tellerID;   /* ID of the current teller object instance */
    private int[] randomDequeueTime;    /* a dataset of random integers */
    private Queue<Customer> queue;  /* the queue in which a teller is in charge of */

    /**
     * Constructs a teller which can serve customers from a queue.
     * @param queue the queue in which the teller operates
     * @param totalMaxCustomers the total number of customers all tellers will serve collectively
     * @param timeAccelerationFactor the speed of time tellers will operate
     * @param randomDequeueTime a dataset of random integers for controlled dequeueing
     */
    public Teller (Queue<Customer> queue, int totalMaxCustomers, int timeAccelerationFactor, int[] randomDequeueTime) { 
        this.randomDequeueTime = randomDequeueTime;
        this.queue = queue;
        bankIsOpen = true;
        totalCustomersTarget = totalMaxCustomers;
        customerWaitTimeTotal = 0;
        if (timeAccelerationFactor <= 0) {
            timeAcceleration = 1;
        } else {
            timeAcceleration = timeAccelerationFactor;
        }
        this.tellerID = nextTellerID;
        nextTellerID++;
    }

    /**
     * The main task a teller runs when it operates. Serves and dequeues customers
     * based on a controlled time delay from a provided data set.
     */
    public void run() {
        do {
            controlledRandomTimePasses(totalCustomersProcessed);
            System.out.flush();
            if (totalCustomersProcessed >= totalCustomersTarget || (lastID + 1) >= totalCustomersTarget) {
                totalCustomersProcessedFinal = totalCustomersProcessed;
                bankIsOpen = false;
                break;
            }
            processCustomer();
        } while (bankIsOpen);
    }

    /**
     * Dequeues a customer and reveals their wait times.
     * Notifies all Tellers of the numbers and aggregates them.
     * Synchronized to prevent multiple tellers accessing the same queue at once.
     */
    public void processCustomer() {
        synchronized (queue) {
            if (queue.front() != null) {
                Customer processedCustomer = queue.dequeue();
                if (processedCustomer != null) {
                    Teller.totalCustomersProcessed += 1;
                    lastID = processedCustomer.getCustomerID();
                    double waitTime = processedCustomer.getWaitTime();
                    customerWaitTimeTotal += waitTime;
                    customerWaitTimeAverage = customerWaitTimeTotal / totalCustomersProcessed;
                    if (debugMode)
                        System.out.printf("Teller %d processed Customer %d from Queue %d, waited %.3f seconds. (%.3f total)\n", this.tellerID, (processedCustomer.getCustomerID() + 1), queue.getQueueID(), waitTime, Teller.customerWaitTimeTotal);
                }
            }
        }
    }

    /**
     * Generates a time delay based on a provided dataset of random integers.
     * @param randomDataIndex the data position of the random integer to be used
     */
    public void controlledRandomTimePasses(int randomDataIndex) {
        try {
            int randomCustomerArrivalDelay = randomDequeueTime[randomDataIndex];
            sleep((1000 / timeAcceleration) * randomCustomerArrivalDelay);
        } catch (Exception e) {
            System.out.println("Reached end of test data.");
        }
    }

    /**
     * Retrieves the total wait time from all tellers.
     * @return the total time customers waited to get off the queue
     */
    public static double getCustomerWaitTimeTotal() {
        return customerWaitTimeTotal;
    }

    /**
     * Retrieves the average wait time from all tellers.
     * @return the average wait time for each customer tellers served
     */
    public static double getCustomerWaitTimeAverage() {
        return customerWaitTimeAverage;
    }

    /**
     * Retrieves the total number of customers tellers have served collectively.
     * @return
     */
    public static int getTotalCustomersServed() {
        return totalCustomersProcessedFinal;
    }

    /**
     * Resets the Teller class for re-use in another simulation.
     */
    public static void resetClass() {
        timeAcceleration = 1;
        totalCustomersProcessed = 0;
        totalCustomersProcessedFinal = 0;
        totalCustomersTarget = 0;
        customerWaitTimeTotal = 0;
        customerWaitTimeAverage = 0;
        bankIsOpen = true;
        lastID = -1;
        nextTellerID = 1;
    }
}