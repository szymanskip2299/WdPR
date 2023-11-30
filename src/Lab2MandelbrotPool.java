import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.commons.numbers.complex.Complex;

public class Lab2MandelbrotPool {
    static int chunkSize;
    

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
        int color = Color.HSBtoRGB(  (float)m / maxIters, (float)1, m==maxIters ? 0 : 1 );
        return color;
    }
    public static void calculation(int iiStart, int iiEnd, double[] extent,int[] res,BufferedImage image,int maxIters) {
        double xStart = extent[0];
        double xEnd = extent[1];
        double yStart = extent[2];
        double yEnd = extent[3];
        int xres = res[0];
        int yres = res[1];
        double dx=(xEnd-xStart)/xres;
        double dy=(yEnd-yStart)/yres;


        for(int ii=iiStart;ii<=iiEnd;ii++){
            int xi = ii % xres;
            int yi = (int)(ii/xres); 

            image.setRGB(xi,yi,manToCol(mandelbrot(Complex.ofCartesian((xi+0.5)*dx+xStart, (yi+0.5)*dy+yStart),maxIters),maxIters));
        }


    }



    public static BufferedImage generateImage(int[] res,
                                              double[] extent, int maxIters,double[] time) throws InterruptedException{
        //extent in order xmin,xmax, ymin,ymax
        BufferedImage image = new BufferedImage(res[0], res[1], BufferedImage.TYPE_INT_RGB);

        
        ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        int chunkCount = (int) Math.ceil((double)res[0]*res[1]/chunkSize);

        long start = System.nanoTime();
        for(int i=0;i<chunkCount;i++){
            int j=i;
            ex.execute(() -> calculation(j*chunkSize, Math.min((j+1)*chunkSize-1,res[0]*res[1]-1), extent, res,image, maxIters));
        }

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.DAYS);
        long end = System.nanoTime();

        // image.setRGB(0, 0, res[0], res[1], imageData, 0, 0);
        time[0]+=(end-start)/1e9;
        return image;
    }


    
    public static double[] timeGeneration(int[] resolutions, int[] amounts) throws InterruptedException{
        double[] times = new double[resolutions.length];
        double[] time = new double[1];
        for(int i=0;i<resolutions.length;i++){
            // double times[i]=0;
            for(int j=0;j<amounts[i];j++){
                
                int[] res = {resolutions[i],resolutions[i]};
                double[] extent = {-2.1,0.6,-1.2,1.2};
                BufferedImage image = generateImage(res,extent,200,time);
                times[i]=time[0];

                File ImageFile = new File("mandelbrot.png");
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
        int[] chunkSizes = {4*4,8*8,16*16,32*32,64*64,128*128,256*256,512*512};
        for(int i=0;i<chunkSizes.length;i++){
            chunkSize=chunkSizes[i];
            System.out.println("");
            System.out.println("Pool dla bloków skladajacych sie z "+Integer.toString(chunkSize)+" pikseli");
            int[] resolutions={4096};
            int[] amounts={10,10,10,10,10,10,10,10,10};
            double[] avgTimes = timeGeneration(resolutions, amounts);
        }
    







        }
        
}
