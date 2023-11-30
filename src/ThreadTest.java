public class ThreadTest {


    public static long factorial(int num){
        long fact=1;
        for(int i=0;i<num;i++){
            fact*=i+1;
        }
        return fact;
    }
    public static void main(String[] args){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<20;i++){
                    System.out.println(Long.toString(factorial(i))+" from thread 1");
                }
            }
        });
        

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run(){
                for(int i=0; i<20;i++){
                    System.out.println(factorial(i)+" from thread 2");
                }
            }
        });
        t1.start();
        t2.start();
        
        try {
            t2.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            t1.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
