import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    static ReentrantLock sharedReentrantLock = new ReentrantLock(false);
    private final Condition even  = sharedReentrantLock.newCondition();
    private final Condition odd = sharedReentrantLock.newCondition();

    public static void main(String[] args) {
        Thread firstThread = new Thread(new OurReentrantThread(), "FirstThread");
        Thread secondThread = new Thread(new OurReentrantThread(), "SecondThread");
        firstThread.start();
        secondThread.start();
    }

}


class OurReentrantThread implements Runnable {

    @Override
    public void run() {
//        while (true) {
        try {
//            ReentrantLockDemo.sharedReentrantLock.lock();
//            ReentrantLockDemo.sharedReentrantLock.newCondition().signal();
//            ReentrantLockDemo.sharedReentrantLock.hasWaiters(condition);
            System.out.println("Locked :" + Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println("Exception :" + Thread.currentThread().getName() + e.getMessage());
        } finally {
//            ReentrantLockDemo.sharedReentrantLock.unlock();
            System.out.println("UnLocked :" + Thread.currentThread().getName());
        }
//        }
    }
}