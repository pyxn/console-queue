/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues by Pao Yu
 */

@SuppressWarnings("unchecked")  /* suppress safe type cast checks */
/**
 * A Queue represents an ADT (renamed from CircularArrayQueue),
 * more specifically, a Queue data structure which is implemented
 * using a circular array. Can accept varying data types.
 */
public class Queue<E> {

    private E[] queueCircularArray; /* array used for the queue */
    private int queueFrontPosition; /* the tracked front element position */
    private int queueBackPosition;  /* the tracked back element position */
    private int queueMaxCapacity;   /* total number of elements that can be queued */
    private int queueCurrentSize;   /* current number of elements in the queue */
    private int queueID;            /* queueID to identify multiple queues */
    private static int nextQueueID = 1; /* Class-level id-tracker */

    /**
     * Constructs a queue object using a circular array
     * @param maxCapacity the total amount of elements the queue can hold
     */
    public Queue(int maxCapacity) {
        this.queueCurrentSize = 0;
        this.queueFrontPosition = 0;
        this.queueBackPosition = 0;
        this.queueMaxCapacity = maxCapacity;   
        this.queueCircularArray = (E[]) new Object[queueMaxCapacity];
        this.queueID = nextQueueID;
        nextQueueID++;
    }

    /**
     * Adds an element to the back of the queue.
     * @param newElement the newElement to be added to the queue.
     */
    public void enqueue(E newElement) {
        if (isFull()) {
        } else {
            queueCircularArray[queueBackPosition] = newElement;
            queueBackPosition = (queueBackPosition + 1) % queueCircularArray.length; 
            queueCurrentSize += 1;
        }
    }

    /**
     * Removes and returns an element from the front of the queue.
     * @return the dequeuedElement
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            E dequeuedElement = queueCircularArray[queueFrontPosition];
            queueCircularArray[queueFrontPosition] = null;
            queueFrontPosition = (queueFrontPosition + 1) % queueCircularArray.length;
            queueCurrentSize -= 1;
            return dequeuedElement;
        }
    }

    /**
     * Returns the front element of the queue without removing it
     * @return the queue's current front element
     */
    public E front() {
        if (isEmpty()) {
            return null;
        } else {
            return queueCircularArray[queueFrontPosition];
        }
    }

    /**
     * Returns the current size (number of elements) in the queue.
     * @return the current number of elements inside the queue
     */
    public int size() {
        return queueCurrentSize;
    }

    /**
     * Checks if the queue is empty or not
     * @return true if the queue is empty, false if it has more than 0 elements
     */
    public boolean isEmpty() {
        if (queueCurrentSize == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the queue is full or not
     * @return true if the queue has reached max capacity, false if not
     */
    public boolean isFull() {
        if (queueCurrentSize == queueMaxCapacity) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get's the current queue's instance queueID
     * @return the current instance's queueID
     */
    public int getQueueID() {
        return this.queueID;
    }

    /**
     * Resets the Class-level id-tracker for re-use in another simulation
     */
    public static void resetClass() {
        nextQueueID = 1;
    }
}