library(future.apply)
library(dplyr)
library(tictoc)
library(future)
library(microbenchmark)

mandelbrot <- function(c){
    n <- 0
    z <- complex(1,0,0)
    while(n<max_iters && abs(z)<2){
      z <- z*z+c
      n <- n+1
    }
    return(n)
}




res <- 256
max_iters <- 100
xmin <- -2.1
xmax <- 0.6
ymin <- -1.2
ymax <- 1.2

my_col <- rainbow(max_iters)
my_col[max_iters] <- "#000000"

plan(multisession)



for(size in c(4,8,16,32,64)){
  print(size)
  result <- microbenchmark({
    grid_vals <- expand.grid(seq(xmin,xmax,length.out=res),seq(ymin,ymax,length.out=res))
    output <- future_sapply(complex(length(grid_vals),grid_vals$Var1,grid_vals$Var2),mandelbrot,future.chunk.size=size*size)},times=10)
  image(matrix(output,res,res),col=my_col,useRaster = T)
  print(paste(size,mean(result$time)/1E9))
}

for(res in c(16,32,64,128,256,512,1024)){
  result <- microbenchmark({
    grid_vals <- expand.grid(seq(xmin,xmax,length.out=res),seq(ymin,ymax,length.out=res))
    output <- future_sapply(complex(length(grid_vals),grid_vals$Var1,grid_vals$Var2),mandelbrot,future.chunk.size=res*res/4)},times=10)
  image(matrix(output,res,res),col=my_col,useRaster = T)
  print(paste(res,mean(result$time)/1E9))
}


plan(sequential)#closes background workers














