package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "spiel")
public class Spiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "continent", nullable = false, length = 255)
    private String continent;

    @Column(name = "difficulty_level", nullable = false)
    private int difficultyLevel;

    @Column(name = "status", nullable = false)
    private String status = "waiting"; // Standardwert

    @ManyToOne
    @JoinColumn(name = "spieler1_id", nullable = false)
    private Spieler spieler1;

    @ManyToOne
    @JoinColumn(name = "spieler2_id", nullable = true)
    private Spieler spieler2;
    
    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = true)
    private Spieler winner;


	@OneToMany(mappedBy = "spiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spielzug> spielzug;

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Spieler getSpieler1() {
        return spieler1;
    }

    public void setSpieler1(Spieler spieler1) {
        this.spieler1 = spieler1;
    }

    public Spieler getSpieler2() {
        return spieler2;
    }

    public void setSpieler2(Spieler spieler2) {
        this.spieler2 = spieler2;
    }

    public Set<Spielzug> getSpielzug() {
        return spielzug;
    }

    public void setSpielzug(Set<Spielzug> spielzug) {
        this.spielzug = spielzug;
    }
    
    public Spieler getWinner() {
		return winner;
	}

	public void setWinner(Spieler winner) {
		this.winner = winner;
	}

    // Status-Hilfsmethoden
    public boolean isWaiting() {
        return "waiting".equalsIgnoreCase(this.status);
    }

    public boolean isInProgress() {
        return "in_progress".equalsIgnoreCase(this.status);
    }

    public boolean isFinished() {
        return "finished".equalsIgnoreCase(this.status);
    }

}
