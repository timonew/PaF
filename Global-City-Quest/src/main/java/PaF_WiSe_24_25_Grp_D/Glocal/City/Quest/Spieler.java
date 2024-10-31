import java.util.Set;
import java.util.HashSet;

public class Spieler {
   private char name;
   
   public void setName(char value) {
      this.name = value;
   }
   
   public char getName() {
      return this.name;
   }
   
   private char userName;
   
   public void setUserName(char value) {
      this.userName = value;
   }
   
   public char getUserName() {
      return this.userName;
   }
   
   private char password;
   
   public void setPassword(char value) {
      this.password = value;
   }
   
   public char getPassword() {
      return this.password;
   }
   
   private long currentscore;
   
   public void setCurrentscore(long value) {
      this.currentscore = value;
   }
   
   public long getCurrentscore() {
      return this.currentscore;
   }
   
   /**
    * <pre>
    *           1..1     0..1
    * Spieler ------------------------- Spiel
    *           spieler1        &lt;       
    * </pre>
    */
   private Spiel ;
   
   public void set(Spiel value) {
      this. = value;
   }
   
   public Spiel get() {
      return this.;
   }
   
   /**
    * <pre>
    *           1..1     0..1
    * Spieler ------------------------- Spiel
    *           spieler2        &lt;       
    * </pre>
    */
   private Spiel ;
   
   public void set(Spiel value) {
      this. = value;
   }
   
   public Spiel get() {
      return this.;
   }
   
   /**
    * <pre>
    *           1..1     0..*
    * Spieler ------------------------> Highscore
    *           &gt;       highscore
    * </pre>
    */
   private Set<Highscore> highscore;
   
   public Set<Highscore> getHighscore() {
      if (this.highscore == null) {
         this.highscore = new HashSet<Highscore>();
      }
      return this.highscore;
   }
   
   }
