import java.util.Set;
import java.util.HashSet;

public class Stadt {
   private char name;
   
   public void setName(char value) {
      this.name = value;
   }
   
   public char getName() {
      return this.name;
   }
   
   private char koordinaten;
   
   public void setKoordinaten(char value) {
      this.koordinaten = value;
   }
   
   public char getKoordinaten() {
      return this.koordinaten;
   }
   
   private int difficultyLevel;
   
   public void setDifficultyLevel(int value) {
      this.difficultyLevel = value;
   }
   
   public int getDifficultyLevel() {
      return this.difficultyLevel;
   }
   
   private char continent;
   
   public void setContinent(char value) {
      this.continent = value;
   }
   
   public char getContinent() {
      return this.continent;
   }
   
   /**
    * <pre>
    *           1..1     0..*
    * Stadt ------------------------- Spielzug
    *           stadt        &lt;       spielzug
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
