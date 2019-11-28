import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    static ReentrantLock sharedReentrantLock = new ReentrantLock(false);

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
                ReentrantLockDemo.sharedReentrantLock.lock();
                System.out.println("Locked :" + Thread.currentThread().getName());
            } catch (Exception e) {
                System.out.println("Exception :" + Thread.currentThread().getName());
            } finally {
                ReentrantLockDemo.sharedReentrantLock.unlock();
                System.out.println("UnLocked :" + Thread.currentThread().getName());
            }
//        }
    }
}