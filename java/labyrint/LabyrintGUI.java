import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane; //DEL A
import javafx.stage.FileChooser; //DEL A
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.layout.Border;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import java.awt.event.MouseListener;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabyrintGUI extends Application {

  Button button;
  Stage teater;
  BorderPane rootPane;
  GridPane pane;
  Labyrint labyrint;
  Rute[][] r;
  int stoerrelse = 0;
  Label tekst = null;

  public void start(Stage teater) { //primaryStage
  this.teater = teater;

	// Setter opp scene og kulisser
  teater.setTitle("Oblig 7: Labyrint");
  //button = new Button("Velg en labyrintfil");
  //button.setOnAction(this); //metode inni i samme klassen (handle)

  BorderPane rootPane = new BorderPane();

  FileChooser filVelger = new FileChooser();
  filVelger.setTitle("Velg en labyrintfil (X.in)");
  File file = filVelger.showOpenDialog(teater);

  tekst = new Label("Antall utvei(er): ");
  tekst.setFont(new Font(15));
  rootPane.setAlignment(tekst, Pos.CENTER);

  rootPane.setTop(tekst);

  try {
    //File file = new File("2.in");
    labyrint = Labyrint.lesFraFil(file);
    pane = lagRutenett(labyrint);
    rootPane.setCenter(pane);
    //teater.sizeToScene();

  } catch (FileNotFoundException e) {
      System.out.printf("FEIL: Kunne ikke lese fra '%s'\n");
      System.exit(1);
  }

  //rootPane.setPrefSize(400, 400);
       // Set the Style-properties of the BorderPane
       /*rootPane.setStyle("-fx-padding: 10;" +
               "-fx-border-style: solid inside;" +
               "-fx-border-width: 10;" +
               "-fx-border-insets: 5;" +
               "-fx-border-radius: 5;" +
               "-fx-border-color: BLUE;");*/

  Scene scene = new Scene(rootPane);
  teater.setScene(scene);
  teater.show();
    }

    public static void main(String[] args) {
	launch(args);
    }

  public GridPane lagRutenett(Labyrint labyrint) {
    GridPane grid = new GridPane();
    Rute[][] r = labyrint.maze;


    for (int rad  = 0; rad < labyrint.rader; rad++){
      for (int kol = 0; kol < labyrint.kolonner; kol++){

        int row = rad;
        int col = kol;

        Rectangle rekt;

        //justerer stoerrelsen til de ulike rutene ut i fra stoerrelsen til labyrinten
        if (labyrint.rader < 20) {
          rekt = new Rectangle(50,50);
          stoerrelse = 50;
        }
        else if (labyrint.rader < 30) {
          rekt = new Rectangle(20,20);
          stoerrelse = 20;
        }
        else if (labyrint.rader < 50){
          rekt = new Rectangle(15,15);
          stoerrelse = 15;
        }
        else {
          rekt = new Rectangle(8,8);
          stoerrelse = 8;
        }

        if (r[rad][kol] instanceof Aapning) {
          //System.out.println("Aapning");
          rekt.setFill(Color.WHITE);
          rekt.setStroke(Color.GREY);
          rekt.setOnMouseClicked(e -> klikk(row, col));

        }
        else if(r[rad][kol] instanceof HvitRute) {
          //System.out.println("Hvitrute");
          rekt.setFill(Color.WHITE);
          rekt.setStroke(Color.GREY);
          rekt.setOnMouseClicked(e -> klikk(row, col));
          //System.out.println(r[ra][k].toString());

        }
        else if (r[rad][kol] instanceof SortRute) {
          //System.out.println("SortRute");
          rekt.setFill(Color.BLACK);
          rekt.setStroke(Color.BLACK);

        }

        grid.add(rekt, kol, rad);
        }
      }

    return grid;
  }

  public void klikk(int rad, int kolonne) {
    restart();

    System.out.println("Du trykket paa ruten (" + rad + ", " + kolonne + ")");

    Liste<String> utveier = labyrint.finnUtveiFra(rad, kolonne);

    if (utveier.stoerrelse() == 0) {
      System.out.println("Ingen utvei\n");
      tekst.setText("Antall utvei(er): " + utveier.stoerrelse());
      restart();
      return;
    }
    String kortesteveien = utveier.hent(0);
    for (String utvei : utveier) {
      if(utvei.length() < kortesteveien.length()) {
        kortesteveien = utvei;
      }
    }
    System.out.println("Kortesteveien er: " + kortesteveien + "\n");
    System.out.println("Det finnes: " + utveier.stoerrelse() + " utveier\n");

    tekst.setText("Antall utvei(er): " + utveier.stoerrelse()); //bytter label

    boolean[][] bool = losningStringTilTabell(kortesteveien, labyrint.kolonner, labyrint.rader);
    for (int x = 0; x < labyrint.rader; x++) {
      for (int y = 0; y < labyrint.kolonner; y++) {
        int row = x;
        int col = y;
         if (bool[x][y]) {
           Rectangle rect = new Rectangle(stoerrelse,stoerrelse, Color.RED);
           rect.setOnMouseClicked(e -> klikk(row, col));
           pane.add(rect, y, x);

        }
      }
    }
  }

  static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
    boolean[][] losning = new boolean[hoyde][bredde];
    java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
    java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
    while (m.find()) {
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        losning[y][x] = true;
    }
    return losning;
  }

  public void restart() {
    for (Node node : pane.getChildren()) {
      Rectangle rektangel = (Rectangle) node;
      if (rektangel.getFill().equals(Color.RED)) {
        rektangel.setFill(Color.WHITE);
        //int kol = pane.getColumnIndex(rektangel);
        //int rad = pane.getRowIndex(rektangel);
        //ruteobjekt.setOnMouseClicked(e -> klikk(rad, kol));
        //pane.add(ruteobjekt, kol, rad);
      }
    }

  }

  /*@Override
  public void handle(ActionEvent event) {
    if(event.getSource() == button) {
      System.out.println("Starter FileChooser");

      FileChooser filVelger = new FileChooser();
      filVelger.setTitle("Velg en labyrintfil (X.in)");
      File fil = filVelger.showOpenDialog(teater);

      if(fil != null) {
        System.out.println(fil.getPath()); //printer ut filens plassering i mappene.
      }
      try {
        labyrint = Labyrint.lesFraFil(fil);
        pane = lagRutenett(labyrint);
        //rootPane.setCenter(pane);
        //teater.sizeToScene();
        //root.setCenter(rutenett);

      } catch (FileNotFoundException e) {
          System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", fil);
          System.exit(1);
      }
    } else {
      System.out.println("Ingen fil ble valgt");
    }
    if(event.getSource() == button2) {
      System.out.println("dsds")
    }

  }*/


}
