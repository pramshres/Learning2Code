import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.Semaphore;

class ParaFactorizer {
    
    int[] primes;
    long[] M_liste = new long[100]; 
    int n; 
    int k;
    CyclicBarrier cb;
    Semaphore guard = new Semaphore(1);
    
    ArrayList<ArrayList<Long>> globalFactors = new ArrayList<ArrayList<Long>>();

    ParaFactorizer(int[] primes, int n, int k) {
        this.primes = primes;
        this.n = n;
        this.k = k;
        this.cb = new CyclicBarrier(k);
    }

    public ArrayList<ArrayList<Long>> paraWork() {
        
        long base = (long) n*n;      
        for (int i = 0; i < 100; i++) {
            M_liste[i] = base - (i + 1);
            globalFactors.add(new ArrayList<Long>());
        }

        int numberOfM = globalFactors.size()/k;

        Thread[] threads = new Thread[k];

        for (int i = 0; i < k; i++) {
            int start = i * numberOfM;
            int end = (i+1) * numberOfM;

            if (i == k-1) {
                end = globalFactors.size();
            }

            threads[i] = new Thread(new Worker(i, start, end));
        }

        for (Thread t : threads) t.start();
    
        try {
            for (Thread t : threads) t.join();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return globalFactors;
      }

      class Worker implements Runnable {

        int ind;
        long base = (long) n * n;
        int start;
        int end;
     
        Worker(int ind, int start, int end) {
          this.ind = ind;
          this.start = start;
          this.end = end;
        }
        
        public void run() {

            for (int i = 0; i < 100; i++) {
                int index = ind; 

                while (index < primes.length) {
                    long M = M_liste[i];

                    if (M < (long) Math.pow(primes[index], 2)) {
                        break;
                    }
                  
                    if (M % primes[index] == 0) { 
                        try {
                            guard.acquire();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        M = M_liste[i];
                        if (M % primes[index] != 0) break; 
                        M_liste[i] = M/primes[index];
                        globalFactors.get(i).add((long) primes[index]);

                        guard.release();

                    } else {
                        index+= k;
                    }
                }
            }

            try {
                cb.await();
            } catch(Exception e) {
                e.printStackTrace();
            }

            //Finding the rest
            long base = (long) n * n;
            for (int i = start; i < end; i++) {
                long localBase = base - (i + 1);
                long product = 1;
    
                for (long factor : globalFactors.get(i)) {
                    product *= factor;
                }
    
                if (product != localBase) {
                    globalFactors.get(i).add(localBase/product);                
                }
            }


      }
    }
}