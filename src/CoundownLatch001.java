import java.util.LinkedList;
import java.util.concurrent.*;

public class CoundownLatch001 { // Zabawy z ciagiem F., pule watkow
    public static void main(String[] args) throws InterruptedException, ExecutionException {


        
        // Pula watkow
        

       ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // Nowa pula z liczba watkow rowna liczbie logicznych rdzeni w maszynie
       long start = System.nanoTime();

       CountDownLatch countDownLatch = new CountDownLatch(47);

       for (int i = 0; i < 47; ++i) {
            int j = i;
            ex.execute(() -> {
                System.out.println(j + " " + Helpers.fibon(j));
                countDownLatch.countDown();
            }); // dodajemy zadanie do puli - definiujemy je przy pomocy wyrazenia lambda
       }
       countDownLatch.await();

       long end = System.nanoTime();
       System.out.println("Time: " + (end - start) / 1000000000.0);

       

       ex.shutdown(); // wysyla polecenie do puli, zeby zakonczyla swoje zadanie. Nowe joby nie sa przyjmowane, te siedzace juz w puli zostana skonczone
       ex.awaitTermination(1, TimeUnit.DAYS); // metoda blokuje do czasu zamknieciu puli (po wykonaniu metody shutdown)


    }
}