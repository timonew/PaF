import java.util.Set;
import java.util.HashSet;

public class Spiel {
   private char continent;
   
   public void setContinent(char value) {
      this.continent = value;
   }
   
   public char getContinent() {
      return this.continent;
   }
   
   private int difficultyLevel;
   
   public void setDifficultyLevel(int value) {
      this.difficultyLevel = value;
   }
   
   public int getDifficultyLevel() {
      return this.difficultyLevel;
   }
   
   /**
    * <pre>
    *           0..1     1..1
    * Spiel ------------------------- Spieler
    *           &gt;       spieler1
    * </pre>
    */
   private Spieler spieler1;
   
   public void setSpieler1(Spieler value) {
      this.spieler1 = value;
   }
   
   public Spieler getSpieler1() {
      return this.spieler1;
   }
   
   /**
    * <pre>
    *           0..1     1..1
    * Spiel ------------------------- Spieler
    *           &gt;       spieler2
    * </pre>
    */
   private Spieler spieler2;
   
   public void setSpieler2(Spieler value) {
      this.spieler2 = value;
   }
   
   public Spieler getSpieler2() {
      return this.spieler2;
   }
   
   /**
    * <pre>
    *           1..1     1..*
    * Spiel ------------------------> MapLayer
    *           spiel        &gt;       mapLayer
    * </pre>
    */
   private Set<MapLayer> mapLayer;
   
   public Set<MapLayer> getMapLayer() {
      if (this.mapLayer == null) {
         this.mapLayer = new HashSet<MapLayer>();
      }
      return this.mapLayer;
   }
   
   /**
    * <pre>
    *           1..1     10..10
    * Spiel ------------------------- Spielzug
    *           spiel        &gt;       spielzug
    * </pre>
    */
   private Set<Spielzug> spielzug;
   
   public Set<Spielzug> getSpielzug() {
      if (this.spielzug == null) {
         this.spielzug = new HashSet<Spielzug>();
      }
      return this.spielzug;
   }
   
   }
