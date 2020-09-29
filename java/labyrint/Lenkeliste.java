import java.util.Iterator;
import java.util.NoSuchElementException;

public class Lenkeliste<T> implements Liste<T> {

  protected Node forste;
  protected int storrelse;

  public Lenkeliste() {
    int storrelse = 0;

  }

  class Node {
    protected Node neste = null;
    protected T data;

    Node(T x) {
      data = x;
    }

  }

  @Override
  public int stoerrelse() {
    /*Node denne = forste;
    int teller = 0;

    while (forste != null) {
      teller++;
      forste = forste.neste;
      }
    return teller;*/
    return storrelse;
    }

  @Override
  public void leggTil(T x) {
    Node ny = new Node(x);
    storrelse++;

    if (forste == null) {
      forste = ny;
      return;

    } else {

    Node denne = forste;

    while (denne.neste != null) {
      denne = denne.neste;
    }
    denne.neste = ny;
    }
  }

  @Override
  public void leggTil(int pos, T x) throws UgyldigListeIndeks{

      Node ny = new Node(x);
      if (pos >= 0 && pos <= storrelse) {
        if (forste == null) {     //Når lista er tom
          forste = ny;
          storrelse++;
          return;
        }
        else if (pos == 0) {    //Når vi skal legge til ny node i indeks 0
          ny.neste = forste;
          forste = ny;
          storrelse++;
          return;
        }
        else if (pos == storrelse) {  //Når vi skal legge til en ny node etter den siste indeksen
          this.leggTil(x);
          return;
        }
        else {
        Node posisjon = forste;
        for (int i = 0; i <pos-1; i++) {
          posisjon = posisjon.neste;
        }
        ny.neste = posisjon.neste;
        posisjon.neste = ny;
        storrelse++;
      }
    }
      else {
        throw new UgyldigListeIndeks(pos);
      }
    }

  @Override
  public void sett(int pos, T x) throws UgyldigListeIndeks {
    if(pos < 0 || pos >= storrelse) {
      throw new UgyldigListeIndeks(pos);
    }
    else {
      Node ny = new Node(x);
      Node posisjon = forste;
      for (int i = 0; i <pos; i++) {
        posisjon = posisjon.neste;
      }
      posisjon.data = ny.data;
    }
  }

  @Override
  public T hent(int pos) throws UgyldigListeIndeks {

    if(pos < 0 || pos >= storrelse) {
      throw new UgyldigListeIndeks(pos);
    }
    else {
      Node posisjon = forste;
      for (int i = 0; i <pos; i++) {
        posisjon = posisjon.neste;
      }
      return posisjon.data;
    }
  }

  @Override
  public T fjern(int pos) throws UgyldigListeIndeks {
    Node posisjon = forste;
    if (pos >= 0 && pos < storrelse) {
        if (pos == 0 && forste == null) {   //hvis tom liste
          throw new UgyldigListeIndeks(-1);
        }
        else if (pos == 0) {  //&& storrelse > 1
          storrelse--;
          forste = forste.neste;
          return posisjon.data;
        }

        /*else if (pos == storrelse - 1) {
          for (int i = 0; i <pos-1; i++) {
            posisjon = posisjon.neste;
            }
          posisjon.neste = null;
          storrelse--;
          return posisjon.data;

          /*Node temp = posisjon.neste;
          posisjon.data = temp.data;
          posisjon.neste = temp.neste;
          storrelse--;
        return posisjon.data;
      }*/
        else {

          for (int i = 0; i <pos-1; i++) {
            posisjon = posisjon.neste;
          }
          Node temp = posisjon.neste;
          posisjon.neste = temp.neste;

          storrelse--;
        //posisjon.neste = posisjon.neste.neste;

        return temp.data;
      }

    } else {
        throw new UgyldigListeIndeks(pos);
      }
    }
    /*else if (pos == 0 && storrelse == 1){
      forste = null;
      storrelse--;
      return posisjon.data;
    }*/
    /*for (int i = 0; i <pos-1; i++) {
      posisjon = posisjon.neste;
      }
    posisjon.neste = posisjon.neste.neste;
    return posisjon.data;*/

  @Override
  public T fjern() throws UgyldigListeIndeks {
    if (storrelse == 0) {
      throw new UgyldigListeIndeks(-1);
    }
    else if (storrelse == 1) {
      Node denne = forste;
      forste = null;
      storrelse--;
      return denne.data;
    }
    else {
      Node denne = forste;
      forste = denne.neste;
      storrelse--;
      return denne.data;
    }
  }

  public Iterator<T> iterator(){
    return new LenkelisteIterator();
  }

  private class LenkelisteIterator implements Iterator<T> {
    private Node denne;

    public LenkelisteIterator() {
      denne = forste;
    }

    @Override
    public boolean hasNext() {

      if (denne != null) {
        return true;
      }
      return false;
      //return denne != null;
    }

    @Override
    public T next() {
      if (denne == null) {
        throw new NoSuchElementException("next");
      }
      T temp = denne.data;
      denne = denne.neste;
      return temp;

    }

    @Override
    public void remove() {
      if (denne == null){
        denne = denne.neste;
      }
      else {
        denne = forste;
        forste = denne.neste;
      }
    }

  }

  public void skrivAlle() {
    Node denne = forste;
      if (denne == null) {
        System.out.println("Listen er tom\n");
      }

      while (denne != null){
          System.out.println(denne.data);
          denne = denne.neste;
      }
      System.out.println();
  }     //Test metode

}







  /*private Plass foran;

  public Lenkeliste() {
    foran = new Plass(null);

  }

  private class Plass {
    T data;
    Plass neste;
  }

  public Plass(T info) {
    data = info;

  }*/
