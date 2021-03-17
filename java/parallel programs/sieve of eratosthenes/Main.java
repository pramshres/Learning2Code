import java.util.Arrays;
import java.util.ArrayList;
import java.io.PrintWriter;

class Main {

    static int mode;
    static int runs;
    static int n;
    static int k; //number of threads
    static int cores = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        // k = Integer.parseInt(args[2]);
        try {
            n = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
            runs = Integer.parseInt(args[2]);
        } catch(Exception e) {
            instructions();
        }

        //if k is a negative number or zero, use amount of cores:
        k = (k <= 0) ? cores : k;

        if (n <= 16) {
            System.out.println("N must be greater than 16, Your N: " + n + "\n");
            instructions();
        }

        if (runs < 1 || runs > 10) {
          System.out.println("\nI wouldn't recommend running this program with more than 10 runs or without any runs");
          System.out.println("Please run the program again.\n");
          instructions();
        }

        System.out.println("\nFIND PRIMES AND FACTORS\n");

        System.out.println("N: " + n);
        System.out.println("Threads: " + k);
        System.out.println("Runs: " + runs);

        int[] primes = null;
        long[][] factors = null;
        double[] timeToSieve = new double[runs];
        double[] timeToFactor = new double[runs];
        ArrayList<ArrayList<Long>> factor_ArrayList = new ArrayList<ArrayList<Long>>();;

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            ParaSieveOfEratosthenes psoe = new ParaSieveOfEratosthenes(n, k);
            primes = psoe.getPrimes();
            double time = (System.nanoTime() - startTime) / 1000000.0;
            timeToSieve[i] = time;
        }

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            ParaFactorizer pf = new ParaFactorizer(primes, n, k);
            factor_ArrayList = pf.paraWork();
            double time = (System.nanoTime() - startTime) / 1000000.0;
            timeToFactor[i] = time;
        }

        System.out.println("\nRuntimes (Sieve): " + Arrays.toString(timeToSieve));
        System.out.println("Runtimes (Factorization): " + Arrays.toString(timeToFactor));
        System.out.println("Median (Sieve): " + getMedianRuntime(timeToSieve) + "ms");
        System.out.println("Median (Factorization): " + getMedianRuntime(timeToFactor) + "ms");

        writeToFilePrimes(primes);
        writeToFileFactors(n, factor_ArrayList, true);
        }

    static double getMedianRuntime(double[] a) {
        Arrays.sort(a);
        return a[a.length/2];
    }

    static void writeToFile(int n, long[][] factors) {
        Precode precode = new Precode(n);
        long base = (long) n*n;
        base--; //We want less than N*N

        for (long[] M : factors) {
            for (long factor : M) {
                precode.addFactor(base, factor);
            }
            base--;
        }

        precode.writeFactors();
    }

    static void writeToFilePrimes(int[] primes) {
      String filename = "Primes_" + n + ".txt";

      try (PrintWriter writer = new PrintWriter(filename)) {
          writer.printf("Primes for n=%d\n", n);

          for (int prime : primes) {
            writer.println(prime);
          }

          writer.flush();
          writer.close();

      } catch(Exception e) {
          System.out.printf("Got exception when trying to write file %s : ",filename, e.getMessage());
      }
    }

    static long[][] writeToFileFactors(int n, ArrayList<ArrayList<Long>> factor_ArrayList, boolean print) {

        long[][] factors = new long[100][];

        for (int i = 0; i < factor_ArrayList.size(); i++) {
            int factorSize = factor_ArrayList.get(i).size();

            long[] localFactors = new long[factorSize];
            for (int j = 0; j < factorSize; j++) {
                localFactors[j] = (long) factor_ArrayList.get(i).get(j);
            }
            Arrays.sort(localFactors);
            factors[i] = localFactors;
        }

        if (print) {
            writeToFile(n, factors);
            return null;
        }
        else {
            return factors;
        }
    }

    static void instructions() {
      System.out.println("How to run:");
      System.out.println("\tjava Main {N} {k} {runs}");
      System.exit(0);
    }

}
