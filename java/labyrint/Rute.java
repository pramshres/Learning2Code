import java.util.ArrayList;

public abstract class Rute {
  protected int rad;
  protected int kolonne;

  //Deklarerer naboruter som akkurat nå er tomme.
  protected Rute nord;
  protected Rute vest;
  protected Rute syd;
  protected Rute ost;

  protected Labyrint laby;
  //protected boolean harBesokt = false;
  protected ArrayList<Rute> naboliste = new ArrayList<Rute>();  //Alle rute objekter har en naboliste


  public Rute(int rad, int kolonne) { //Rute konstruktør som tar inn x og y koordinat
    this.rad = rad;
    this.kolonne = kolonne;
  }
  public int hentRad() {
    return rad;
  }
  public int hentKol() {
    return kolonne;
  }

  public void settNabo(Rute nord, Rute ost, Rute syd , Rute vest){  //Vi setter naboer inni i instansvariabeler vi deklarerte tidligere
    this.nord = nord;
    this.ost = ost;
    this.syd = syd;
    this.vest = vest;

    if (nord != null) {
      naboliste.add(nord);
    }
    if (ost != null) {
      naboliste.add(ost);
    }
    if (syd != null) {
      naboliste.add(syd);
    }
    if (vest != null) {
      naboliste.add(vest);
    }


  }
  public String toString() {
    return "(" + kolonne + ", " + rad + ")";
  }

  /*public void harBesokt() {
    harBesokt = true;
  }
  public boolean finnOmBesokt() {
    return harBesokt;
  }*/

  public boolean sjekkAapning() {
    return false;
  }

  abstract char tilTegn();

  public void gaa(String rute) { //metoden gaa

    //base-casen vi går mot

    if (this.tilTegn() == '#') {  //den utvalgte ruten kan ikke være en sort rute
      return;
    }
    if (this.sjekkAapning() == true && this.tilTegn() != '#') { //hvis utvalgte ruten er en åpning
      laby.utveier.leggTil(rute + " er en utvei");  //legges åpningen i labyrint sin utvei liste
      return;
    }

    laby.besokteRuter.add(this);  //legger ruten vi er på inne i listen besokteRuter i klassen labyrint

    if (this.tilTegn() == '.'){  //hvis tegnet vi er på er en hvit rute
      for (Rute r : naboliste) {  //sjekker vi alle naboene til denne hvite ruten
        if (r.tilTegn() != '#' && !laby.besokteRuter.contains(r)){ //hvis naboen ikke er en sort rute og naboen ikke har blitt besøkt
          r.gaa(rute + " --> (" + r.kolonne + ", " + r.rad + ")"); //så kaller vi metoden gaa fra naboen
        }
      }
    }
  }

  public void finnUtvei() { //Metoden finnUtvei
    Rute start = this;  //Variabelen start er ruten vi starter på
    laby.besokteRuter.add(start); //Legger start ruten inni i besokteRuter
    start.gaa("(" + start.kolonne + ", " + start.rad + ")"); //
    laby.besokteRuter.clear(); //sletter alle besokte ruter i listen besokteRuter
  }


}
