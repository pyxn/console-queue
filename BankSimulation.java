/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues
 * by Pao Yu
 */

/**
 * Represents a bank which can operate with a reception, tellers, and customers in a queue.
 * @author Pao Yu
 */
public class BankSimulation<E> extends Thread {

    private Teller teller1;
    private Teller teller2;
    private Teller teller3;
    private Reception reception;
    private Queue<Customer> queue1;
    private Queue<Customer> queue2;
    private Queue<Customer> queue3;

    /**
     * Constructs a simulated bank with three tellers and one queue.
     * @param maxCustomers the total number of customers the bank will serve
     * @param timeAccelerationFactor the speed of time the bank's thread objects will run
     * @param randomEnqueueTime a random dataset of integers for controlled dequeue times
     * @param randomDequeueTime a random dataset of integers for controlled dequeue times
     */
    public BankSimulation(int maxCustomers, int timeAccelerationFactor, int[] randomEnqueueTime, int[] randomDequeueTime) {
        queue1 = new Queue<Customer>(maxCustomers);
        teller1 = new Teller(queue1, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        teller2 = new Teller(queue1, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        teller3 = new Teller(queue1, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        reception = new Reception(queue1, maxCustomers, timeAccelerationFactor, randomEnqueueTime);
    }

    /**
     * Constructs a simulated bank with three tellers and three queues.
     * @param maxCustomers the total number of customers the bank will serve
     * @param timeAccelerationFactor the speed of time the bank's thread objects will run
     * @param randomEnqueueTime a random dataset of integers for controlled dequeue times
     * @param randomDequeueTime a random dataset of integers for controlled dequeue times
     * @param enableMultiQueue enables the functionality of the multiple queue reception operations
     */
    public BankSimulation(int maxCustomers, int timeAccelerationFactor, int[] randomEnqueueTime, int[] randomDequeueTime, boolean enableMultiQueue) {
        queue1 = new Queue<Customer>(maxCustomers);
        queue2 = new Queue<Customer>(maxCustomers);
        queue3 = new Queue<Customer>(maxCustomers);
        teller1 = new Teller(queue1, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        teller2 = new Teller(queue2, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        teller3 = new Teller(queue3, maxCustomers, timeAccelerationFactor, randomDequeueTime);
        reception = new Reception(queue1, queue2, queue3, maxCustomers, timeAccelerationFactor, randomEnqueueTime, enableMultiQueue);
    }

    /**
     * Starts the operations of the reception and the tellers.
     * Waits for all of these objects to complete their tasks before 
     * "closing" the bank, which is dictated by teller operations.
     */
    public void run() {
        reception.start();
        teller1.start();
        teller2.start();
        teller3.start();
        try {
            reception.join();
            teller1.join();
            teller2.join();
            teller3.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            System.out.printf("%sSimulation complete!%s", "\033[1;32m", "\033[0m");
        }
    }
}