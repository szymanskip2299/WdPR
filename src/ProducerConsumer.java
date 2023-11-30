import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {
    



    public static class Producer extends Thread{
        private BlockingQueue<String> blockingQueue;
        public Producer(BlockingQueue<String> blockingQueue){this.blockingQueue = blockingQueue;}
        @Override
        public void run() {
            Random random = new Random();
            for(int count= 0;true;count++){
                try {
                    sleep(random.nextInt(4000));
                    System.out.println("Produced: "+getId()+ " "+ count);
                    blockingQueue.put(getId()+" "+count);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }


            }
        }
    }
    public static class Consumer extends Thread{
    private BlockingQueue<String> blockingQueue;
    public Consumer(BlockingQueue<String> blockingQueue){this.blockingQueue = blockingQueue;}
    @Override
    public void run() {
        Random random = new Random();
        while(true){
            try {
                String result = blockingQueue.take();
                sleep(random.nextInt(4000));
                System.out.println("Consumed: "+result+"; by "+getId());
            } catch (InterruptedException e) {

                e.printStackTrace();
            }


        }
    }
    }
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(10);
        Producer[] producers = new Producer[6];
        
        for(int i =0; i<6 ;i++){
            producers[i]=new Producer(blockingQueue);
            producers[i].start();
        }
        Consumer[] consumers = new Consumer[3];
        for(int i =0; i<3 ;i++){
            consumers[i]=new Consumer(blockingQueue);
            consumers[i].start();
        }
     }

    
}
