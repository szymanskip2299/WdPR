import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarier001 {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[6];

        CyclicBarrier cyclicBarrier=new CyclicBarrier(threads.length,() -> System.out.println("Section finished"));

        for(int i=0;i<threads.length;i++){
            threads[i] = new Thread(){
                @Override
                public void run() {
                    Random random = new Random();
                    try {
                        while(true){
                            sleep(random.nextLong(2000));
                            System.out.println("Job A finished - " + getId());
                            
                            cyclicBarrier.await();

                            sleep(random.nextLong(2000));
                            System.out.println("Job B finished - " + getId());

                            cyclicBarrier.await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    
                    super.run();
                }
            };
        }
        for(int i=0;i<threads.length;i++)
            threads[i].start();
        
        for(int i=0;i<threads.length;i++)
            threads[i].join();
        

    }
}
