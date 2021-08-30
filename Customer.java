/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues by Pao Yu
 */

/**
 * Represents a single anonymous customer identified by an ID.
 * The customer tracks it's own waiting time in a queue.
 */
public class Customer {

    private static int totalID = 0; /* class-level starting ID */
    private int customerID;         /* instance-level object ID */
    private long queueStartTime;    /* the time customers enter a queue */
    private long queueEndTime;      /* the time customers leave a queue */

    /**
     * Constructs a customer, tracking its instantiation time and its current ID
     */
    public Customer() {
        queueStartTime = System.nanoTime();
        this.customerID = totalID;
        totalID += 1;
    }

    /**
     * Retrieves a customer total wait time from entering a queue.
     * A Teller object must call this method to signal the customer has left the queue.
     * @return the total wait time the customer was in the queue.
     */
    public double getWaitTime() {
        queueEndTime = System.nanoTime();
        double waitTime = ((double) queueEndTime - (double) queueStartTime) / 1000000000.00;
        return waitTime;
    }

    /**
     * Retrieves the customer's ID
     * @return the customer's ID
     */
    public int getCustomerID() {
        return this.customerID;
    }

    /**
     * Resets the class for re-use in another simulation
     */
    public static void resetClass() {
        totalID = 0;
    }
}
