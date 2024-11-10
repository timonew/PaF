package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

@Entity
public class Spieler {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;
   private String userName;
   private String password;
   private long currentscore;

   @OneToMany(mappedBy = "spieler1")
   private Set<Spiel> spiele1;

   @OneToMany(mappedBy = "spieler2")
   private Set<Spiel> spiele2;

   @OneToMany(mappedBy = "spieler")
   private Set<Highscore> highscore;

   // Getter und Setter
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public long getCurrentscore() {
      return currentscore;
   }

   public void setCurrentscore(long currentscore) {
      this.currentscore = currentscore;
   }

   public Set<Spiel> getSpiele1() {
      return spiele1;
   }

   public void setSpiele1(Set<Spiel> spiele1) {
      this.spiele1 = spiele1;
   }

   public Set<Spiel> getSpiele2() {
      return spiele2;
   }

   public void setSpiele2(Set<Spiel> spiele2) {
      this.spiele2 = spiele2;
   }

   public Set<Highscore> getHighscore() {
      return highscore;
   }

   public void setHighscore(Set<Highscore> highscore) {
      this.highscore = highscore;
   }

	public void setUsername(String username2) {
		// TODO Auto-generated method stub
		
	}
	
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		
	}
}
