import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.commons.numbers.complex.Complex;

public class Lab1Mandelbrot {

    static int chunkCount = 16;
    static int threadCount = 16;
    

    public static double mandelbrot(Complex c,int maxIters){
        int n=0;
        Complex z = Complex.ofCartesian(0, 0);
        while(n<maxIters && z.abs()<2){
            n+=1;
            z=z.multiply(z).add(c);
        }

        return n;

    }
    public static int manToCol(double m,int maxIters){
        int color = Color.HSBtoRGB(  (float)m / maxIters, (float)1, m==maxIters+1 ? 0 : 1 );
        return color;
    }

    public static class Worker extends Thread {
        private Counter counter;
        private double xStart;
        private double xEnd;
        private double yStart;
        private double yEnd;
        private int xres;
        private int yres;
        private BufferedImage image;
        private int maxIters;

        public Worker(Counter counter, double[] extent,int[] res,BufferedImage image,int maxIters) {
            this.counter = counter;
            this.xStart = extent[0];
            this.xEnd = extent[1];
            this.yStart = extent[2];
            this.yEnd = extent[3];
            this.xres = res[0];
            this.yres = res[1];
            this.image = image;
            this.maxIters =maxIters;
        }
        @Override
        public void run() {
            //extent in order xmin,xmax, ymin,ymax



            double dx=(xEnd-xStart)/xres;
            double dy=(yEnd-yStart)/yres;
            int chunk;
            int chunkSize = (int) Math.ceil((double)xres*yres/chunkCount);
            int ii;

            while ((chunk = counter.getAndIncrement()) < Math.ceil(xres*yres/chunkSize)) {
                // System.out.println("Chunk:\t"+Integer.toString(chunk)+"\tout of\t"+Integer.toString((int)xres*yres/chunksize));
                ii=chunk*chunkSize;
                while(ii<xres*yres & ii<chunkSize*(chunk+1)){
                    int xi = ii % xres;
                    int yi = (int)(ii/xres); 
                    // img[ii] = manToCol(mandelbrot(Complex.ofCartesian((xi+0.5)*dx+xStart, (yi+0.5)*dy+yStart)));
                    image.setRGB(xi,yi,manToCol(mandelbrot(Complex.ofCartesian((xi+0.5)*dx+xStart, (yi+0.5)*dy+yStart),maxIters),maxIters));
                    ii++;
                }

            }
        }
    }

    public static BufferedImage generateImage(int[] res,
                                              double[] extent, int maxIters) throws InterruptedException{
        //extent in order xmin,xmax, ymin,ymax
        BufferedImage image = new BufferedImage(res[0], res[1], BufferedImage.TYPE_INT_RGB);
        Counter counter = new Counter();
        Worker[] workers = new Worker[threadCount];
        for(int i = 0; i < workers.length; ++i){
            workers[i] = new Worker(counter,extent, res,image,maxIters);
            workers[i].start();
        }
        for(int i = 0; i < workers.length; ++i)
            workers[i].join();

        return image;
    }


    
    public static double[] timeGeneration(int[] resolutions, int[] amounts) throws InterruptedException{
        double[] times = new double[resolutions.length];
        for(int i=0;i<resolutions.length;i++){
            // double times[i]=0;
            for(int j=0;j<amounts[i];j++){
                long start = System.nanoTime();
                int[] res = {resolutions[i],resolutions[i]};
                double[] extent = {-2.1,0.6,-1.2,1.2};
                BufferedImage image = generateImage(res,extent,200);
                long end = System.nanoTime();
                times[i]+=(end-start)/1e9;


                File ImageFile = new File("tmp.png");
                try {
                    ImageIO.write(image, "png", ImageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            times[i]/=amounts[i];
            System.out.println(Double.toString(times[i])+"\t"+Integer.toString(resolutions[i]));
        }
        return times;
    }

    public static void main(String[] args) throws InterruptedException{
        
        int[] resolutions={32,64,128,256,512,1024,2048,4096,8192};
        int[] amounts={10,10,10,10,10,10,10,10,10};
        double[] avgTimes = timeGeneration(resolutions, amounts);
    

        // int[] resolutions={997,998,999,1000,1022,1024,2048,4096,8192};
        // int[] amounts={5,5,5,5,5,5,5,5,5};
        // double[] avgTimes = timeGeneration(resolutions, amounts);

        // int[] res = {32,32};
        // double[] extent = {-2.1,0.6,-1.2,1.2};
        // BufferedImage image = generateImage(res, extent, 254);
        // File ImageFile = new File("test3.png");
        // try {
        //     ImageIO.write(image, "png", ImageFile);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }





        }
        
}
