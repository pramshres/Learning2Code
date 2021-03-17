import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

class ParaSieveOfEratosthenes {

    int n, root, numOfPrimes, k;
    byte[] oddNumbers;
    int sqrtOfRoot; 
    int[][] primes2D;

    CyclicBarrier cbMain;
    ReentrantLock laas = new ReentrantLock();

    ArrayList<Integer> newPrimes = new ArrayList<Integer>();

    ParaSieveOfEratosthenes(int n, int k) {
        this.n = n;
        this.k = k;
        root = (int) Math.sqrt(n);
        sqrtOfRoot = (int) Math.sqrt(root);

        oddNumbers = new byte[(n / 16) + 1];
        this.cbMain = new CyclicBarrier(k+1);
      }


    int[] getPrimes() {
        if (n <= 1) return new int[0];
        
        findFirst();
        paraSieve();
        

        return collectPrimes();
    }

    void findFirst() {
        numOfPrimes = 1;
        int prime = 3;

        //Sekvensiell del: tråd 0 -> sjekk alle primtall < sqrt(sqrt(n))
        while (prime != -1 && prime <= sqrtOfRoot) {
            traverse(prime);
            prime = nextPrime(prime);
        }

    }
     //Sekvensiell del: så finn alle primtall < sqrt(n)
    private void traverse(int prime) {
        for (int i = prime*prime; i <= root; i += prime * 2) {
            //System.out.println("Marking " + i);
            mark(i);
        }
    }

    private void mark(int num) {
        if (num % 2 == 0) {
            return;
        }

        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;
        oddNumbers[byteIndex] |= (1 << bitIndex);
    }

    private int nextPrime(int prev) {
        for (int i = prev + 2; i < root; i += 2)
          if (isPrime(i))
            return i;
    
        return -1;
      }
    
    private boolean isPrime(int num) {
        int bitIndex = (num % 16) / 2;
        int byteIndex = num / 16;
        
        boolean bool = (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
        //System.out.println("Is " + num + " a prime number? " + bool);

        return (oddNumbers[byteIndex] & (1 << bitIndex)) == 0;
    }

    private void paraSieve() {
        int start = 0;
        int prime = 3;
        int segment = n/k;
        int remainder = n % k; 
        Thread[] threads = new Thread[k];

        for (int i = 0; i < k; i++) {
            
            int end = start + segment;
            if (i == 0) {
                start = root;
            }         
            if (i == k-1) {
                end = n;
            }
            
            //System.out.println("Thread: " + i + " start: " + start + " -> " + "end: " + end);
            threads[i] = new Thread(new Worker(i, start, end));
            start = end + 1;
        }

        for (Thread t : threads) { t.start();
        }

        try {
            cbMain.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class Worker implements Runnable {
        int ind, start, end;

        Worker(int ind, int start, int end) {
            this.ind = ind;
            this.start = start;
            this.end = end;
        }
        @Override
        public void run() {
            int prime = 3;            

            while (prime != -1 && prime * prime < end) {

                int startPoint = (start + prime - 1) / prime;
                if ((startPoint & 1) == 0) {
                    startPoint++;
                }

                //System.out.println("thread " + ind + ": starting: " + startPoint);
                traversePara(prime, startPoint, end);
                prime = nextPrime(prime); 
                //System.out.println("New prime found: " + prime);  
                laas.lock();
                try {
                    if (!newPrimes.contains(prime)) {
                        newPrimes.add(prime);
                        numOfPrimes++;
                    }
                } finally {
                    laas.unlock();
                }            
   
            }

            try {
                cbMain.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
    
        }
        
    }

    void traversePara(int prime, int start, int end) {
        // System.out.println("Prime: " + prime);
        for (int i = start * prime; i <= end; i += prime * 2) {
            //System.out.println("Marking: " + i);
            mark(i);
        }
    }

    private int[] collectPrimes() {

        int start = (root % 2 == 0) ? root + 1 : root + 2;
    
        for (int i = start; i <= n; i += 2)
          if (isPrime(i))
            numOfPrimes++;

        //System.out.println("NumberOfPrimes: " + numOfPrimes);
    
        int[] primes = new int[numOfPrimes];
    
        primes[0] = 2;
    
        int j = 1;
    
        for (int i = 3; i <= n; i += 2)
          if (isPrime(i))
            primes[j++] = i;
    
        return primes;
      }

       
}