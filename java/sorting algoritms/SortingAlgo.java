import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;

class SortingAlgo {

  public static void main(String[] args) {

    int[] liste = new int[] {4,5,12,87,43,1,6}; //random
    //int[] liste42 = new int[] {4,5,12,87,43,1,6}; //random nr2
    //int[] liste = new int[] {1,4,5,6,12,43,87}; //sortert stigende
    //int[] liste = new int[] {87,43,12,6,5,4,1}; //sortert

    int n = liste.length;

    System.out.println("Input: " + Arrays.toString(liste));
    System.out.println();


    long t = System.nanoTime(); //nanosekunder

    //CHOICES:

    //int[] liste2 = selectionSort(liste);
    //int[] liste2 = insertionSort(liste);
    int[] liste2 = heapSort(liste);
    //int[] liste2 = mergeSort(liste);
    //int[] liste2 = quickSort(liste, 0, liste.length-1); //siste som pivot
    //int[] liste2 = quickSort2(liste, 0, liste.length-1); //median av 3
    //int[] liste2 = bucketSort(liste);
    //int[] liste2 = radixSort(liste);

    double tid = (System.nanoTime() - t ) /  1000000.0;  //millisekunder

    System.out.println();
    System.out.println("Output: " + Arrays.toString(liste2));
    System.out.println();

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
