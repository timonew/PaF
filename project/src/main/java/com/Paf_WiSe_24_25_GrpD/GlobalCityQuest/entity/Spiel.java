package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    // Beziehungen zu anderen Entitäten
    @ManyToOne
    @JoinColumn(name = "spieler1_id", nullable = false)
    private Spieler spieler1;

    @ManyToOne
    @JoinColumn(name = "spieler2_id", nullable = false)
    private Spieler spieler2;

    @OneToMany(mappedBy = "spiel")
    private Set<Spielzug> spielzug;

    @OneToMany
    @JoinColumn(name = "map_layer_id") // Fremdschlüsselbeziehung zu MapLayer
    private Set<MapLayer> mapLayer;


    // Zusatzmethoden
    public void setRounds(int rounds) {
        // Logik für Runden festlegen
    }

    public void setDifficulty(String difficulty) {
        // Logik für Schwierigkeit festlegen
    }
}
