package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "spieler") // Optional: Tabellennamen angeben
public class Spieler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "current_score", nullable = false)
    private long currentscore;

    // Ein Spieler kann mehrere Spiele als Spieler1 haben
    @OneToMany(mappedBy = "spieler1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spiel> spiele1;

    // Ein Spieler kann mehrere Spiele als Spieler2 haben
    @OneToMany(mappedBy = "spieler2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spiel> spiele2;

    // Ein Spieler kann mehrere Highscores haben
    @OneToMany(mappedBy = "spieler", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Highscore> highscores;

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPassword(String encode) {
		// TODO Auto-generated method stub
		
	}


}
