// tak nie dziala
// public class Counter {
//     private int count = 0;
//     public int getAndIncrement() {
//         return count++;
//     }
// }


//tak juz ok
// import java.util.concurrent.locks.ReentrantLock;

// public class Counter {
//     ReentrantLock lock=new ReentrantLock();
//     private int count = 0;
//     public int getAndIncrement() {
//         lock.lock();//wszystko w srodku lock jest wykonywane nie rownolegle
//             int tmp=count++;
//         lock.unlock();
//         return tmp;
//     }
// }

//tu tez git
// public class Counter {
//     private int count = 0;
//     public synchronized int getAndIncrement() {
//         return count++;
//     }
// }

import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    ReentrantLock lock=new ReentrantLock();
    public synchronized int getAndIncrement() {
        try{
            lock.lock();
            return count++;
        } finally {
            lock.unlock();
        }

    }
}