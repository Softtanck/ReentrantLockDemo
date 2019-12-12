import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class NumberPrint {
    public static ReentrantLock sharedReentrantLock = new ReentrantLock(true);
    public static final Condition even = sharedReentrantLock.newCondition();
    public static final Condition odd = sharedReentrantLock.newCondition();
    public static final Condition zero = sharedReentrantLock.newCondition();

    public static void main(String[] args) {
        int n = 20;
        new Thread(new ZeroEvenOdd(n, 1)).start();
        new Thread(new ZeroEvenOdd(n, 2)).start();
        new Thread(new ZeroEvenOdd(n, 3)).start();
    }
}


class ZeroEvenOdd implements Runnable {
    private int n;
    private int caseNum;

    public ZeroEvenOdd(int n, int caseNum) {
        this.n = n;
        this.caseNum = caseNum;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero() throws InterruptedException {
        NumberPrint.sharedReentrantLock.lock();
        boolean revert = false;
        for (int i = 0; i < n; i++) {
            System.out.println(0);
            if (!revert) {
                revert = true;
                while (!NumberPrint.sharedReentrantLock.hasWaiters(NumberPrint.odd)) {
//                    System.out.println("Hold:" + NumberPrint.sharedReentrantLock.hasWaiters(NumberPrint.odd));
                    NumberPrint.zero.await();
//                    Thread.sleep(1000);
                }
                System.out.println("zero唤醒打印奇数");
                NumberPrint.odd.signal();
            } else {
                revert = false;
                while (!NumberPrint.sharedReentrantLock.hasWaiters(NumberPrint.even)) {
//                    System.out.println("Hold:" + NumberPrint.sharedReentrantLock.hasWaiters(NumberPrint.odd));
                    NumberPrint.zero.await();
//                    Thread.sleep(1000);
                }
                System.out.println("zero唤醒打印偶数");
                NumberPrint.even.signal();
            }
            System.out.println("zero进入等待状态");
            NumberPrint.zero.await();
        }
        System.out.println("zero全部执行完毕");
        NumberPrint.sharedReentrantLock.unlock();
    }

    // 偶数
    public void even() throws InterruptedException {
        NumberPrint.sharedReentrantLock.lock();
        int temp = n / 2;
        NumberPrint.zero.signal();
        for (int i = 1; i <= temp; i++) {
            System.out.println("偶数进入睡眠");
            NumberPrint.even.await();
            System.out.println("偶数唤醒");
            System.out.println(i * 2);
            System.out.println("Zero唤醒");
            NumberPrint.zero.signal();
        }
        System.out.println("偶数执行完毕");
        NumberPrint.sharedReentrantLock.unlock();
    }

    // 奇数
    public void odd() throws InterruptedException {
        NumberPrint.sharedReentrantLock.lock();
        int temp = n % 2 != 0 ? n / 2 + n % 2 : n / 2;
        NumberPrint.zero.signal();
        for (int i = 0; i < temp; i++) {
            System.out.println("奇数进入睡眠");
            NumberPrint.odd.await();
            System.out.println("奇数唤醒");
            System.out.println(i * 2 + 1);
            System.out.println("Zero唤醒");
            NumberPrint.zero.signal();
        }
        System.out.println("奇数执行完毕");
        NumberPrint.sharedReentrantLock.unlock();
    }

    @Override
    public void run() {
        System.out.println("线程开始:" + caseNum);
        try {
            switch (caseNum) {
                case 1:
                    odd();
                    break;
                case 2:
                    even();
                    break;
                case 3:
                    zero();
                    break;
            }
        } catch (Exception e) {
        }
    }
}