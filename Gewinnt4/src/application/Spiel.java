package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


// Konstanten im Spiel
// #########################
interface Konstanten {
	// Werte für eine Zelle
	final char SPIELER_R = 'R';
	final char SPIELER_B = 'B';
	final char FREI = ' ';
	final char ZUENDE = ' ';
	// Aussehen
	final Color FARBE_R = Color.RED;
	final Color FARBE_B = Color.BLUE;
	final double STRICHTSTAERKE = 5.0;
}


// Klasse zum Zeichnen einer Zelle im Spielfeldes
// ###############################################
class Zelle extends Pane {
	
  private char inhalt;   // Der Inhalt der Zelle: ' ', 'X', 'O'
  private Spiel spiel;   // Die Zelle ist Teil des Spieles
     // Erlaubt uns die Weiterleitung von Klick-Events an das Spiel zur Überprüfung
  
  public Zelle(Spiel spiel) {
	this.spiel = spiel;
  	inhalt = Konstanten.FREI; // Zu Beginn ist die Zelle leer
  	// Die Referenz für CSS findet ihr: 
  	// => https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
  	this.setStyle("-fx-border-color: black"); // Rahmen für das Feld
  	this.setPrefSize(2000, 2000); // Max Grösze
  	this.setOnMouseClicked(e -> handleMouseClick()); // Wenn man darauf klickt
  }
  
  public char getInhalt() {
  	return inhalt;
  } 
  
  /**
   * Ein Spielzug wird dargestellt
   * @param stein welcher Spieler hat gezogen 
   */
  public void zeichnen(char stein) {
	  // Wir merken uns den Spielstein
	  inhalt = stein;
		 if (inhalt == Konstanten.SPIELER_R) {
			 Ellipse ellipse1 = new Ellipse(
		  			  this.getWidth() / 2, 
		  			  this.getHeight() / 2, 
		  			  this.getWidth() / 2 - 10, 
		  			  this.getHeight() / 2 - 10);
				  
				  // Ursprung an die Mitte der Zelle binden
				  ellipse1.centerXProperty().bind(this.widthProperty().divide(2));
				  ellipse1.centerYProperty().bind(this.heightProperty().divide(2));
				  
				  // Größe an die Zellengröße binden
				  ellipse1.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));        
				  ellipse1.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10)); 
				  
				  // Rahmen und Farben setzen
				  ellipse1.setStroke(Konstanten.FARBE_R);
				  ellipse1.setStrokeWidth(Konstanten.STRICHTSTAERKE);
				  ellipse1.setFill(Konstanten.FARBE_R);
		      
				  getChildren().add(ellipse1); 
	  }
	  else if (inhalt == Konstanten.SPIELER_B) {
		  Ellipse ellipse = new Ellipse(
  			  this.getWidth() / 2, 
  			  this.getHeight() / 2, 
  			  this.getWidth() / 2 - 10, 
  			  this.getHeight() / 2 - 10);
		  
		  // Ursprung an die Mitte der Zelle binden
		  ellipse.centerXProperty().bind(this.widthProperty().divide(2));
		  ellipse.centerYProperty().bind(this.heightProperty().divide(2));
		  
		  // Größe an die Zellengröße binden
		  ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(10));        
		  ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(10)); 
		  
		  // Rahmen und Farben setzen
		  ellipse.setStroke(Konstanten.FARBE_B);
		  ellipse.setStrokeWidth(Konstanten.STRICHTSTAERKE);
		  ellipse.setFill(Konstanten.FARBE_B);
      
		  getChildren().add(ellipse); 
	  }
  	}

  	private void handleMouseClick() {
	  // Teile dem Spiel mit, dass auf diese Zelle geklickt wurde
	  spiel.spielzug(this);
  	}
}

// Das TicTacToe Spiel
// #####################
public class Spiel extends Application {

	private char werZieht = Konstanten.SPIELER_R;

	// Das Spielfeld ist ein 2 dimensionales Array
	private Zelle[][] spielfeld =  new Zelle[6][7];

	// Ausgabefeld für die Meldungen des Programmes
	private Label lblAnzeige = new Label("Rot ist am Zug");

   	@Override
	public void start(Stage primaryStage) {
   		// GridPane für die Anzeige
   		GridPane tabelle = new GridPane(); 
   		// Das Spielfeld aufbauen
		for (int zeile = 0; zeile < 6; zeile++) {
			for (int spalte = 0; spalte < 7; spalte++) {
				Zelle zelle = new Zelle(this);
				// Wir merken uns die Zelle im Spielfeld Modell
				spielfeld[zeile][spalte] = zelle;
				// und tragen die geleiche Zelle in der Darstellung ein
				// Vorsicht: die Spalte kommt zuerst !
				tabelle.add(zelle, spalte, zeile);
			}
		}
        
		// Den Label in einer HBox zentriert darstellen
		HBox box = new HBox(10);
		box.setAlignment(Pos.BASELINE_CENTER);
	    box.getChildren().add(lblAnzeige);
	    
	    // Das Spielfeld und die Anzeige untereinander darstellen
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(tabelle);
		borderPane.setBottom(box);
		
		// und alles anzeigen
		Scene scene = new Scene(borderPane, 450, 400);
		
		primaryStage.setTitle("4 Gewinnt");
		primaryStage.setMinHeight(300 + 50);
		primaryStage.setMinWidth(200);
		primaryStage.setScene(scene);
		primaryStage.show();   
   	}

	/**
	 * Überprüft ob das Spielfeld voll ist
	 * @return true wenn voll
	 */
	public boolean feldIstVoll() {
		for (int zeile = 0; zeile < 6; zeile++) {
			for (int spalte = 0; spalte < 5; spalte++) {
				if (spielfeld[zeile][spalte].getInhalt() == Konstanten.FREI) {
					return false;
				}
			}
		}
	    return true;
	}

	/**
	 * 
	 * @param spieler X oder O
	 * @return true ... der Spieler hat gewonnen
	 */
	public boolean hatGewonnen(char spieler) {
		 
		// 4 gleiche in einer Zeile
		for (int zeile = 0; zeile < 6; zeile++) {
		    for (int spalte = 0; spalte <= 3; spalte++) {
		        if (spielfeld[zeile][spalte].getInhalt() == spieler
		                && spielfeld[zeile][spalte + 1].getInhalt() == spieler
		                && spielfeld[zeile][spalte + 2].getInhalt() == spieler
		                && spielfeld[zeile][spalte + 3].getInhalt() == spieler) {
		            return true;
		        }
		    }
		}
		// 4 gleiche in einer Zeile
		for (int zeile = 0; zeile <= 2; zeile++) {
		    for (int spalte = 0; spalte < 7; spalte++) {
		        if (spielfeld[zeile][spalte].getInhalt() == spieler
		                && spielfeld[zeile+1][spalte].getInhalt() == spieler
		                && spielfeld[zeile+2][spalte].getInhalt() == spieler
		                && spielfeld[zeile+3][spalte].getInhalt() == spieler) {
		            return true;
		        }
		    }
		}
		
		// Diagonale1
		for (int zeile = 0; zeile <= 2; zeile++) {
		    for (int spalte = 0; spalte <= 3; spalte++) {
		        if (spielfeld[zeile][spalte].getInhalt() == spieler
		                && spielfeld[zeile+1][spalte+1].getInhalt() == spieler
		                && spielfeld[zeile+2][spalte+2].getInhalt() == spieler
		                && spielfeld[zeile+3][spalte+3].getInhalt() == spieler) {
		            return true;
		        }
		    }
		}

		// Diagonale2
		for (int zeile = 0; zeile <=  2; zeile++) {
		    for (int spalte = 6; spalte >= 3; spalte--) {
		        if (spielfeld[zeile][spalte].getInhalt() == spieler
		                && spielfeld[zeile+1][spalte-1].getInhalt() == spieler
		                && spielfeld[zeile+2][spalte-2].getInhalt() == spieler
		                && spielfeld[zeile+3][spalte-3].getInhalt() == spieler) {
		            return true;
		        }
		    }
		}



	    return false;
	  }
	
	  
	
	int getfreieZeile(int spalte) {
		for(int zeile = 5; zeile>=0; zeile --) {
			if(spielfeld[zeile][spalte].getInhalt() == Konstanten.FREI) {
				return zeile; 
			}
		}
		return -1;  	
	}
	
	
	int getSpalte(Zelle zelle) {
		for(int z = 0; z<6; z++) {
			for(int s = 0; s<7; s++) {
				if(spielfeld[z][s] == zelle) {
					return s; 
				}
				
			}
		}

		return -1; 

	}
	
	
	  // Spieler hat auf die Zelle geklickt
      void spielzug(Zelle feld) {
    	 int spalte = getSpalte(feld); 
	  	 int zeile = getfreieZeile(spalte); 
	  	 
	  	 
		// Nur wenn das Feld noch FREI ist und das Spiel nicht zu Ende ist
	  	if (werZieht != Konstanten.ZUENDE && spalte != -1 && zeile != -1) {
	  		Zelle Feld = spielfeld[zeile][spalte]; 
	  		Feld.zeichnen(werZieht);
	  		
	  		// wenn der Spieler gewonnen hat
	  		if (hatGewonnen(werZieht)) {
	  			lblAnzeige.setText(werZieht + " hat gewonnen: Das Spiel ist zu Ende");
	  			werZieht = Konstanten.ZUENDE; 
	  		}
	  		
	  		
	  		// Wenn das Spielfeld voll ist
	  		else if (feldIstVoll() || spalte == -1) {
	  			lblAnzeige.setText("Das Spiel ist vobei - Keiner hat gewonnen");
	  			werZieht = Konstanten.ZUENDE; 
	  		}
	  		// ansonsten ist der nächste Spieler am Zug
	  		else {
	  			// Spieler wechseln
	  			if (werZieht == Konstanten.SPIELER_R) {
	  				werZieht = Konstanten.SPIELER_B;
	  			} else {
	  				werZieht = Konstanten.SPIELER_R;
	  			}
	  			lblAnzeige.setText(werZieht + " ist am Zug");
	  		}
	  	}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}