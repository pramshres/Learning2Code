import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Scanner;

class SortingAlgo {

  static int listChoice;
  static int algoChoice;

  public static void main(String[] args) {

    int[][] lists = new int[4][];

    lists[0] = new int[] {4,5,12,87,43,1,6}; //random
    lists[1] = new int[] {12,6,43,87,4,5,1}; //random nr2
    lists[2] = new int[] {1,4,5,6,12,43,87}; //sortert stigende
    lists[3] = new int[] {87,43,12,6,5,4,1}; //sortert

    System.out.println("****** Sorting Algorithms ******\n");

    System.out.println("Please choose one of these 4 list to sort (choose by ID): \n");
    System.out.format("%-12s %-12s", "ID", "List");
    System.out.println();

    for (int i = 0; i < lists.length; i++) {
      System.out.format("%-12d %-12s", i+1, Arrays.toString(lists[i]));
      System.out.println();
    }

    Scanner scanner = new Scanner(System.in);
    System.out.print("\nID > ");

    try {
      listChoice = scanner.nextInt();
      if (listChoice < 1 || listChoice > 4) {
        System.out.println("Pick between 1 and 4");
        System.exit(0);
      }
      else {
        listChoice = listChoice - 1;
      }
    } catch (Exception e) {
      System.out.println("Please enter a number");
    }

    System.out.println("You picked list nr " + (listChoice + 1) + ": " + Arrays.toString(lists[listChoice]));
    try {
      Thread.sleep(1000);  
    } catch (Exception e) {
      System.out.println("Main thread not working");
    }
    
    //-------------------------------------ALGORITHM CHOICE-----------------------------------------------------
    String[] algorithms = {"Selection Sort", "Insertion Sort", "Heap Sort", "Merge Sort", "Quick Sort with pivot = last index", "Quick Sort with pivot = median of 3", "Bucket Sort", "Radix Sort"};

    System.out.println("\nPlease choose one of these sorting algorithms (choose by ID): \n");
    System.out.format("%-12s %-12s", "ID", "Algorithms");
    System.out.println();

    for (int i = 0; i < algorithms.length; i++) {
      System.out.format("%-12d %-12s", i+1, algorithms[i]);
      System.out.println();
    }

    System.out.print("\nID > ");
    try {
      algoChoice = scanner.nextInt();
      if (algoChoice < 1 || algoChoice > 8) {
        System.out.println("Pick between 1 and 8");
        System.exit(0);
      }
      else {
        algoChoice = algoChoice - 1;
      }
    } catch (Exception e) {
      System.out.println("Please enter a number");
    }

    System.out.println("You picked list nr " + (algoChoice + 1) + ": " + (algorithms[algoChoice]));


    System.out.println("\n****** RUNNING ******\n");
    System.out.println("Sorting Algorithm: " + algorithms[algoChoice]);
    System.out.println("Input list: " + Arrays.toString(lists[listChoice]));

    int n = lists[listChoice].length;
    int[] sorted = null;
    
    long t = 0; 
    double tid = 0;

    switch (algoChoice) {
      case 0:
        t = System.nanoTime(); //nanosekunder
        sorted = selectionSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;
      case 1:
        t = System.nanoTime(); //nanosekunder
        sorted = insertionSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break; 
      case 2:
        t = System.nanoTime(); //nanosekunder
        sorted = heapSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;    
      case 3:
        t = System.nanoTime(); //nanosekunder
        sorted = mergeSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;    
      case 4:
        t = System.nanoTime(); //nanosekunder
        sorted = quickSort(lists[listChoice], 0, lists[listChoice].length-1);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;    
      case 5:
        t = System.nanoTime(); //nanosekunder
        sorted = quickSort2(lists[listChoice], 0, lists[listChoice].length-1);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;    
      case 6:
        t = System.nanoTime(); //nanosekunder
        sorted = bucketSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;            
      case 7:
        t = System.nanoTime(); //nanosekunder
        sorted = radixSort(lists[listChoice]);
        tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder
        break;    
    }
    
    System.out.println("\nOutput list: " + Arrays.toString(sorted));
    System.out.println("Runtime: " + tid + " ms\n");
    //2147483647

  }
  //-------------------- Test av hastighet --------------------
public static void runtime_test() {
  boolean bool = true;
  double temptid = 0;
  int size = 1000;
  int teller = 0;

  while (bool && temptid < 300) { //300s = 5 min
    try {

      int[] array = new int[size];
      System.out.println(array.length);

      for (int i = 0; i < array.length; i++) {
        Random r = new Random();
        int random = r.nextInt(101); //tallene i lista er mellom 0 til 100
        array[i] = random;
      }

      //Arrays.sort(array); //sorterer stigende
      //reverse(array); //snur lista til fallende

      long t3 = System.nanoTime();
      //array = insertionSort(array);
      //array = heapSort(array);
      //array = quickSort(array, 0, size-1);
      array = quickSort2(array, 0, size-1);
      //array = mergeSort(array);
      //Arrays.sort(array);
      //array = bucketSort(array);
      double kjoretid = (System.nanoTime() - t3 ) /  1000000.0;

      System.out.println("Tid: " + kjoretid + "\n");

      temptid = temptid + kjoretid/1000; //for å få sekunder

      if (teller % 2 == 0) {
        size = size * 5;
      } else {
        size = size * 2;
      }
      teller++;

    } catch (OutOfMemoryError e) {
      bool = false;
      System.out.println("Oh no, out of memory error");
    }
  }
}


  public static void reverse(int[] input) {
    int last = input.length - 1;
    int middle = input.length / 2;
    for (int i = 0; i <= middle; i++) {
      int temp = input[i];
      input[i] = input[last - i];
      input[last - i] = temp;
    }
  }

  //-----------------------Selectionsort -----------------------------
  public static int[] selectionSort(int[] liste) {
    for (int i = 0; i < liste.length; i++) {
      //find the index, s, of the smallest element in A[i..n]
      int s = i; //finding the minimum element in unsorted array
      for (int j = i + 1; j < liste.length; j++) {
        if (liste[j] < liste[s]) {
          s = j;
        }
      }
      if (i != s) {
        //Swap A[i] and A[s] (swap the found minimum element with the first element)
        int temp = liste[s];
        liste[s] = liste[i];
        liste[i] = temp;
      }
    }
    return liste;

  }

  //-----------------------Insertionsort -----------------------------
  public static int[] insertionSort(int[] liste) {

      for (int i = 1; i < liste.length; i++) { //looping through the list starting with index 1
        int x = liste[i]; //we save the value of index i
        //put x in the right place in A[0...i], moving larger elements up as needed
        int j = i - 1; // j starts as the left index (previous index)
        while (j >= 0 && x < liste[j]) {
          liste[j + 1] = liste[j];
          j = j-1;
          //System.out.println(Arrays.toString(liste));
        }
      liste[j+1] = x; //put x in the right place
      //System.out.println(Arrays.toString(liste));

      }
    return liste;
  }

  //-----------------------Heapsort ----------------------------------
  public static int[] heapSort(int[] liste) {
    int n = liste.length;
    settMaxHeap(liste, n); //finne største noden.
    for (int i = n-1; i >= 0; i--) {
      int temp = liste[0];
      liste[0] = liste[i];
      liste[i] = temp;
      //n--;

      heapify(liste, 0, i);
    }
    return liste;
  }

  public static void settMaxHeap(int[] liste, int n) {
    for (int i = n/2-1; i >= 0; i--) {
      heapify(liste, i, n);
    }
  }

  public static void heapify(int[] liste, int i, int n) {
    //System.out.println(Arrays.toString(liste));
    int venstre = 2 * i + 1; //+ 1 fordi vi har indeks 0 i lista vår
    int hoyre = 2 * i + 2;  //+ 2 fordi vi har indeks 0 i lista vår
    int maks = i;

    if (venstre < n && liste[venstre] > liste[maks]) {
      maks = venstre;
    }

    if (hoyre < n && liste[hoyre] > liste[maks]) {
      maks = hoyre;
    }

    if (maks != i) { //sjekker om forelderen er større enn barna
      //bytter noden i indeks 0 med noden med størst verdi
      int temp = liste[i];
      liste[i] = liste[maks];
      liste[maks] = temp;

      heapify(liste, maks, n);

    }
  }

  //-----------------------Quicksort med siste index som pivot -------
  public static int[] quickSort(int[] liste, int startindeks, int sluttindeks) { //best case O(n^2) når lista er sortert fra før

    while (startindeks < sluttindeks) {

      int pivot_indeks = partition(liste, startindeks, sluttindeks);
      if (pivot_indeks - startindeks < sluttindeks - pivot_indeks) {
          quickSort(liste, startindeks, pivot_indeks-1);
          startindeks = pivot_indeks + 1;
      }
      else {
          quickSort(liste, pivot_indeks + 1, sluttindeks);
          sluttindeks = pivot_indeks-1;
      }
    }
    return liste;
  }

  public static int partition(int[] liste, int a, int b) {
    int pivot = liste[b];

    int l = a; //will scan rightward
    int r = b - 1; //will scan leftward
    while (l <= r) { //find an element larger than the pivot
      while (l <= r && liste[l] <= pivot) { //finds numbers bigger than the pivot
        l = l + 1;
      }
      while (r >= l && liste[r] >= pivot) { //finds number smaller than the pivot
        r = r - 1;
      }
      if (l < r) {
        int temp = liste[l];
        liste[l] = liste[r];
        liste[r] = temp;
      }
    }
    int temp = liste[l];
    liste[l] = liste[b]; //put the pivot into its final place
    liste[b] = temp;

    return l;
  }

  //-----------------------Quicksort med median av 3 som pivot -------
  public static int[] quickSort2(int[] liste, int startindeks, int sluttindeks) { //best case O(n^2) når lista er sortert fra før

    while (startindeks < sluttindeks) {

      int median = medianOf3(liste, startindeks, sluttindeks);
      //System.out.println("M: " + Arrays.toString(liste));
      int pivot_indeks = partition2(liste, startindeks, sluttindeks, median);
      //System.out.println(Arrays.toString(liste));
      if (pivot_indeks - startindeks < sluttindeks - pivot_indeks) {
          quickSort2(liste, startindeks, pivot_indeks-1);
          startindeks = pivot_indeks + 1;
      }
      else {
          quickSort2(liste, pivot_indeks + 1, sluttindeks);
          sluttindeks = pivot_indeks-1;
      }
      /*quickSort(liste, startindeks, pivot_indeks-1);
      quickSort(liste, pivot_indeks + 1, sluttindeks);*/
    //}
    }
      return liste;
    }

  public static int partition2(int[] liste, int a, int b, int pivot) {
      //int pivot = liste[b];

      int l = a; //will scan rightward
      int r = b - 1; //will scan leftward
      while (l <= r) { //find an element larger than the pivot
        while (l <= r && liste[l] <= pivot) { //finds numbers bigger than the pivot
          l = l + 1;
        }
        while (r >= l && liste[r] >= pivot) { //finds number smaller than the pivot
          r = r - 1;
        }
        if (l < r) {
          int temp = liste[l];
          liste[l] = liste[r];
          liste[r] = temp;
        }
      }
      //swap(liste, l, b);
      int temp = liste[l];
      liste[l] = liste[b]; //put the pivot into its final place
      liste[b] = temp;

      //System.out.println("   " + Arrays.toString(liste));

      return l;
    }

  public static int medianOf3(int[] liste, int left, int right) {
    int center = (left + right)/2;

    if (liste[left] > liste[center]) {
      swap(liste, left, center);
    }
    if (liste[left] > liste[right]) {
      swap(liste, left, right);
    }
    if (liste[center] > liste[right]) {
      swap(liste, center, right);
    }

    swap(liste, center, right);
    return liste[right];
  }

  //-----------------------Mergesort --------------------------------
  public static int[] mergeSort(int[] liste) { //rekursiv
    int n = liste.length;
    if (n == 1) { //sjekker om vi har liste med bare et element
      //System.out.println("hei");
      return null;
       //return liste;

    }
    int[] listeA = new int[n/2];
    int[] listeB = new int[n - listeA.length];

    for (int i = 0; i < listeA.length; i++) {
      listeA[i] = liste[i];
    }
    for (int i = 0; i < listeB.length; i++) {
      listeB[i] = liste[n/2 + i];
    }

    //System.out.println(listeEN.length);
    //System.out.println(listeTO.length);

    /*System.out.println(Arrays.toString(listeA));
    System.out.println(Arrays.toString(listeB));
    System.out.println();*/

    mergeSort(listeA);
    mergeSort(listeB);

    merge(liste, listeA, listeB);

    return liste;
  }

  public static void merge(int[] listeC, int[] A, int[] B) {
    //int[] listeC = new int[A.length + B.length];

    int a = A.length; //left
    int b = B.length; //right

    int i = 0;
    int j = 0;
    int k = 0;

    while (i < a && j < b) {
      if (A[i] <= B[j]) {
        listeC[k++] = A[i++];
      }
      else {
        listeC[k++] = B[j++];
      }
    }

    while (i < a) {
      listeC[k++] = A[i++];
    }
    while (j < b) {
      listeC[k++] = B[j++];
    }

  }

  //-----------------------Bucketsort -------------------------------
  public static int[] bucketSort (int[] liste) {
    int maks = getMaks(liste);
    int[] bucket = new int[maks + 1];

    for (int i = 0; i < bucket.length; i++) {
      bucket[i] = 0;
    }

    for (int i = 0; i < liste.length; i++) {
      bucket[liste[i]]++;
    }

    int outPos = 0;
    for (int i = 0; i < bucket.length; i++) {
      for (int j = 0; j < bucket[i]; j++) {
        liste[outPos++] = i;
      }
    }
        return liste;
    }

  //-----------------------Radixsort (kopiert kode)----------------------------
  public static int[] radixSort(int[] liste) {
    int n = liste.length;
    int maks = getMaks(liste);

    for (int plass = 1; maks/plass > 0; plass*= 10) {
      countsort(liste, n, plass);
    }

    return liste;
  }

  public static void countsort(int[] liste, int n, int plass) {
    int[] output = new int[n];
    int maks = getMaks(liste);

    int[] teller = new int[10];
    Arrays.fill(teller, 0); //fyller lista med bare 0;

    for (int i = 0; i < n; i++) {
      int m = (liste[i]/plass)%10;
      teller[m]++;
    }

    for (int i = 1; i < 10; i++) {
      teller[i] += teller[i-1];
    }

    for (int i = n-1; i >= 0; i--) {
      int m = (liste[i]/plass)%10;
      output[teller[m] - 1] = liste[i];
      teller[m]--;
    }

    for (int i = 0; i < n; i++) {
      liste[i] = output[i];
    }


  }

  //-----------------------Hjelpemetoder ----------------------------
  public static void swap(int[] liste, int dex1, int dex2) {
    int temp = liste[dex1];
    liste[dex1] = liste[dex2];
    liste[dex2] = temp;
  }

  public static int getMaks(int[] liste) {
    int maks = liste[0];
    for (int i = 1; i < liste.length; i++) {
      if (liste[i] > maks) {
        maks = liste[i];
      }
    }
    return maks;
  }


}
