package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "spiel") // Optional: explizite Tabellenbezeichnung
public class Spiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "continent", nullable = false)
    private char continent;

    @Column(name = "difficulty_level", nullable = false)
    private int difficultyLevel;

    // Status des Spiels, z. B. "waiting", "in_progress", "finished"
    @Column(name = "status", nullable = false)
    private String status;

    // Beziehungen zu anderen Entitäten
    @ManyToOne
    @JoinColumn(name = "spieler1_id", nullable = false)
    private Spieler spieler1;

    @ManyToOne
    @JoinColumn(name = "spieler2_id", nullable = true) // Spieler2 ist optional (z. B. bei "waiting")
    private Spieler spieler2;

    @OneToMany(mappedBy = "spiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spielzug> spielzug;

    @OneToMany
    @JoinColumn(name = "map_layer_id") // Fremdschlüsselbeziehung zu MapLayer
    private Set<MapLayer> mapLayer;

    // Zusatzmethoden für Status
    public boolean isWaiting() {
        return "waiting".equalsIgnoreCase(this.status);
    }

    public boolean isInProgress() {
        return "in_progress".equalsIgnoreCase(this.status);
    }

    public boolean isFinished() {
        return "finished".equalsIgnoreCase(this.status);
    }

    // Weitere Setter und Getter
    public void setRounds(int rounds) {
        // Logik für Runden festlegen (optional)
    }

    public void setDifficulty(String difficulty) {
        // Logik für Schwierigkeit festlegen (optional)
    }

	public void setSpieler1(Spieler spieler) {
		// TODO Auto-generated method stub
		
	}

	public void setContinent(String continent2) {
		// TODO Auto-generated method stub
		
	}

	public void setStatus(String string) {
		// TODO Auto-generated method stub
		
	}

	public String getDifficulty() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getContinent() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpieler2(Spieler spieler22) {
		// TODO Auto-generated method stub
		
	}
}
