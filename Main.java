/**
 * Queueing Theory - Bank Queue Simulation
 * Single VS Multi-Line Queues by Pao Yu
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
/**
 * Testing ground for the bank simulation.
 */
public class Main {

    public static void main(String[] args) {

        /* variables to control the simulation customers wait times, and operation speed */
        final int TOTAL_CUSTOMERS = 100;
        final int MIN_WAIT_TIME = 1;
        final int MAX_WAIT_TIME = 5;
        int timeAccelerationFactor = 1;
        Scanner in = new Scanner(System.in);
        System.out.printf("%sENTER TIME ACCELERATION FACTOR: %s", "\033[1;35m", "\033[0m");
        if (in.hasNextInt()) {
            timeAccelerationFactor = in.nextInt();
        }
        in.close();

        /* generate test data and prepare containers for result storage */
        int[] randomEnqueueTime = new int[TOTAL_CUSTOMERS];
        int[] randomDequeueTime = new int[TOTAL_CUSTOMERS];
        double[] resultSingleQueue = new double[2];
        double[] resultMultiQueue = new double[2];
        for (int i = 0; i < TOTAL_CUSTOMERS; i++) {
            randomEnqueueTime[i] = ThreadLocalRandom.current().nextInt(MIN_WAIT_TIME, MAX_WAIT_TIME + 1);
        }
        for (int i = 0; i < TOTAL_CUSTOMERS; i++) {
            randomDequeueTime[i] = ThreadLocalRandom.current().nextInt(MIN_WAIT_TIME, MAX_WAIT_TIME + 1);
        }

        /* start the bank simulation for a single queue bank operation */
        BankSimulation<Customer> bankSimulationSingleQueue = new BankSimulation<Customer>(TOTAL_CUSTOMERS, timeAccelerationFactor, randomEnqueueTime, randomDequeueTime);
        bankSimulationSingleQueue.start();
        try {
            bankSimulationSingleQueue.join();
        } catch (InterruptedException e){
            System.out.println(e);
        } finally {
            resultSingleQueue[0] = Teller.getCustomerWaitTimeTotal();
            resultSingleQueue[1] = Teller.getCustomerWaitTimeAverage();
            Reception.resetClass();
            Teller.resetClass();
            Customer.resetClass();
            Queue.resetClass();
        }

        /* start the bank simulation for a multi-queue bank operation */
        BankSimulation<Customer> bankSimulationMultiQueue = new BankSimulation<Customer>(TOTAL_CUSTOMERS, timeAccelerationFactor, randomEnqueueTime, randomDequeueTime, true);
        bankSimulationMultiQueue.start();
        try {
            bankSimulationMultiQueue.join();
        } catch (InterruptedException e){
            System.out.println(e);
        } finally {
            resultMultiQueue[0] = Teller.getCustomerWaitTimeTotal();
            resultMultiQueue[1] = Teller.getCustomerWaitTimeAverage();
            Reception.resetClass();
            Teller.resetClass();
            Customer.resetClass();
            Queue.resetClass();
        }

        /* display the final results of both simulations */
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println("Bank Simulation Single-Queue Results:");
        System.out.println("----------------------------------------");
        System.out.printf("Total customers served  : %d\n", TOTAL_CUSTOMERS);
        System.out.printf("Total customer wait time: %.5f\n", resultSingleQueue[0]);
        System.out.printf("Average queue wait time : %.5f\n", resultSingleQueue[1]);
        System.out.println("----------------------------------------");
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println("Bank Simulation Multi-Queue Results:");
        System.out.println("----------------------------------------");
        System.out.printf("Total customers served  : %d\n", TOTAL_CUSTOMERS);
        System.out.printf("Total customer wait time: %.5f\n", resultMultiQueue[0]);
        System.out.printf("Average queue wait time : %.5f\n", resultMultiQueue[1]);
        System.out.println("----------------------------------------");
        System.out.println("");
    }
}