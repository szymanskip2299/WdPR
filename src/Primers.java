public class Primers {



    public static class Worker extends Thread {
        private Counter counter;
        public Worker(Counter counter) {
            this.counter = counter;
        }
        @Override
        public void run() {
            int number;
            int count = 0;
            while ((number = counter.getAndIncrement()) < 1e6) {
                if(Helpers.isPrime(number)) {
                    //System.out.println(number);
                }
                ++count;
            }
            System.out.println(count);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        long start = System.nanoTime();
        Counter counter = new Counter();
        Worker[] workers = new Worker[16];
        for(int i = 0; i < workers.length; ++i)
            workers[i] = new Worker(counter);
        for(int i = 0; i < workers.length; ++i)
            workers[i].start();
        for(int i = 0; i < workers.length; ++i)
            workers[i].join();
        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) / 1e9);
    }
}


