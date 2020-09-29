import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class Labyrint {
  protected static Rute[][] maze;
  protected static int rader;
  protected static int kolonner;

  protected Lenkeliste<String> utveier = new Lenkeliste<String>();
  protected ArrayList<Rute> besokteRuter = new ArrayList<Rute>();


private Labyrint(Rute[][] m, int rader, int kolonner) {
  //maze = new Rute[rader][kolonner];
  maze = m;
  this.rader = rader;
  this.kolonner = kolonner;
}

public static Labyrint lesFraFil(File fil) throws FileNotFoundException {

  Scanner sc = new Scanner(fil);

  String[] str = sc.nextLine().split(" ");
  int antRader = Integer.parseInt(str[0]);
  int antKolonner = Integer.parseInt(str[1]);

  Rute[][] rutenett = new Rute[antRader][antKolonner];

  for (int rad = 0; rad < antRader; rad ++) {
    String radRuter = sc.nextLine();
    //String[] radTegn = linje.split("");

    for (int kol = 0; kol < antKolonner; kol++) {
      //Hvis ruten er av tegnet "#", så oppretter vi sort rute objekt i denne ruten
      if (radRuter.charAt(kol) == '#') {
        rutenett[rad][kol] = new SortRute(rad,kol);
        }
      else if (radRuter.charAt(kol) == '.') { //Hvis ruten er av tegnet "." og ligger i sidene, så oppretter vi aapningsruter
        if (rad == 0 || kol == 0 || rad == antRader-1 || kol == antKolonner-1) {
          rutenett[rad][kol] = new Aapning(rad,kol);
        }
        else {  //ellers så oppretter vi vanlige hvite ruter
          rutenett[rad][kol] = new HvitRute(rad,kol);
          }
        }
      }
    }

  sc.close();
  Labyrint laby = new Labyrint(rutenett, antRader, antKolonner); //Oppretter vi labyrint objekt
  skrivUt();
  settNabo(antRader,antKolonner);
  for (int x = 0; x < antRader; x++) {
    for (int y = 0; y < antKolonner; y++) {
      maze[x][y].laby = laby;
      //System.out.println(maze[x][y]);
    }
  }

  return laby;
}

private static void skrivUt() {
    for (int i = 0; i < rader; i++) {
      String plass = " ";
      for (int j = 0; j < kolonner; j++) {
        //System.out.print(j);
        System.out.print(maze[i][j].tilTegn());
      }
      System.out.println(plass);
    }
  } //Skriver ut labyrinten

//Metoden under setter naboer for alle ruter inni i labyrinten
private static void settNabo(int rader, int kolonner) { //x er antallrader og y er antallKolonner
  Rute tomNord = null;
  Rute tomOst = null;
  Rute tomSyd = null;
  Rute tomVest = null;
  for (int x= 0; x < rader; x++) {
    for (int y = 0; y < kolonner; y++) {
      if (x - 1 >= 0) {
        tomNord = maze[x-1][y];
      }
      if (y + 1 < kolonner) {
        tomOst = maze[x][y+1];
      }
      if (x + 1 < rader) {
        tomSyd = maze[x+1][y];
      }
      if (y - 1 >= 0) {
        tomVest = maze[x][y-1];
      }

      maze[x][y].settNabo(tomNord, tomOst, tomSyd, tomVest);
      //Rute nord = (x - 1 < 0) ? null : maze[x-1][y];
    }
  }
}

public Lenkeliste<String> finnUtveiFra(int kol, int rad) { //Metoden finnUtVeiFra finner utvei fra en gitt koordiant (kol, rad)
  for (String vei : utveier) {  //for alle utveier i listen utveier, fjerner vi de, slik at utveiene ikke blir lagret til neste gang vi bruker denne listen
    utveier.fjern();
  }
  maze[kol][rad].finnUtvei(); //finner utvei fra gitt rute
  return utveier;
  }
}
