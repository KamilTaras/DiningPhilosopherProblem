import java.sql.SQLOutput;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        int n = 50;

        Philosopher[] philosophers = new Philosopher[n];
        Fork[] forks = new Fork[n];
        for (int i = 0; i < n; i++) {
            forks[i] = new Fork(false, i);
        }

        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher(i, PhilosopherState.THINKING, forks[i], forks[indexOfSecond(i + 1, n)]);
        }
        for (int i = 0; i < n; i++) {
            philosophers[i].start();
        }
    }

    static int indexOfSecond(int currentIndex, int lastIndex) {
        return currentIndex % lastIndex;
    }
}

class Philosopher extends Thread {

    public Philosopher(int ID, PhilosopherState philosopherState, Fork leftFork, Fork rightFork) {
        this.ID = ID;
        this.philosopherState = philosopherState;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.isLeftForkBeingHold = false;
        this.isRightForkBeingHold = false;
        this.eatenDishes = 0;
    }

    private final int ID;
    private PhilosopherState philosopherState;
    private final Fork leftFork;
    private boolean isLeftForkBeingHold;
    private final Fork rightFork;
    private boolean isRightForkBeingHold;
    private int eatenDishes;


    @Override
    public void run() {
        while (true) {
            tryTakeLowerPriorityFork();
            tryTakeHigherPriorityFork();
            eat();
            putLowerPriorityForkDown();
            putHigherPriorityForkDown();
        }
    }

    boolean hasLeftForkLowerID(){
        return leftFork.getID() < rightFork.getID();
    }
    boolean hasRightForkLowerID(){
        return leftFork.getID() > rightFork.getID();
    }

    public boolean isLeftForkBeingHold() {
        return isLeftForkBeingHold;
    }

    public void setLeftForkBeingHold(boolean leftForkBeingHold) {
        isLeftForkBeingHold = leftForkBeingHold;
    }

    public boolean isRightForkBeingHold() {
        return isRightForkBeingHold;
    }

    public void setRightForkBeingHold(boolean rightForkBeingHold) {
        isRightForkBeingHold = rightForkBeingHold;
    }

    void tryTakeLowerPriorityFork() {
        synchronized (this) {
            if (hasLeftForkLowerID() && !leftFork.isTaken()) {
                leftFork.setIsTaken(true);
                isLeftForkBeingHold = true;
//                System.out.println("Philosopher " +ID + " takes left fork with ID "+leftFork.getID());
            }
        }
        synchronized (this) {
            if (hasRightForkLowerID() && !rightFork.isTaken()) {
                rightFork.setIsTaken(true);
                isRightForkBeingHold = true;
//                System.out.println("Philosopher " +ID + " takes right fork with ID "+leftFork.getID());
            }
        }
    }

    void tryTakeHigherPriorityFork() {
        synchronized (this) {
            if (!hasLeftForkLowerID() && !leftFork.isTaken()) {
                leftFork.setIsTaken(true);
                isLeftForkBeingHold = true;
            }
        }
        synchronized (this) {
            if (!hasRightForkLowerID() && !rightFork.isTaken()) {
                rightFork.setIsTaken(true);
                isRightForkBeingHold = true;
            }
        }

    }

    void eat() {

        if (isLeftForkBeingHold && isRightForkBeingHold){
            philosopherState = PhilosopherState.EATING;
            System.out.println("Philosopher "+ ID + " is " +philosopherState);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            eatenDishes++;
            System.out.println("Philosopher " + ID + " has EATEN " +eatenDishes + "(yummy)");
        }


    }


    void putLowerPriorityForkDown() {
        synchronized (this) {
            if (hasLeftForkLowerID() && isLeftForkBeingHold) {
                leftFork.setIsTaken(false);
                isLeftForkBeingHold = false;
//                System.out.println("Philosopher " +ID + " put left fork (with ID "+leftFork.getID()+") down");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        synchronized (this) {
            if(hasRightForkLowerID() && isRightForkBeingHold){
                rightFork.setIsTaken(false);
                isRightForkBeingHold = false;
//                System.out.println("Philosopher " +ID + " put right fork (with ID "+ rightFork.getID() + ") down");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        isPhilosopherThinking();
    }

    void putHigherPriorityForkDown() {
        synchronized (this) {
            if (!hasLeftForkLowerID() && isLeftForkBeingHold) {
                leftFork.setIsTaken(false);
                isLeftForkBeingHold = false;
//                System.out.println("Philosopher " +ID + " put left fork (with ID "+leftFork.getID()+") put down");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        synchronized (this) {
            if(!hasRightForkLowerID() && isRightForkBeingHold){
                rightFork.setIsTaken(false);
                isRightForkBeingHold = false;
//                System.out.println("Philosopher " +ID + " put right fork (with ID "+ rightFork.getID() + ") put down");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        isPhilosopherThinking();
    }
    boolean isPhilosopherThinking(){
        if(!isRightForkBeingHold && !isLeftForkBeingHold){
            philosopherState = PhilosopherState.THINKING;
            return true;
        }
        return false;
    }
}

enum PhilosopherState {
    EATING,
    THINKING,
    STARVING
}

enum Priority{
    LOWER,
    HIGHER
}


class Fork {
    private boolean isTaken;
    private int ID;

    public Fork(boolean isTaken, int ID) {
        this.isTaken = isTaken;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setIsTaken(boolean taken) {
        isTaken = taken;
    }

}