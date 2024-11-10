package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "highscore") // Optional: Tabellennamen angeben
public class Highscore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primärschlüssel für eindeutige Identifikation

    @Column(name = "score_difficulty_level", nullable = false)
    private int scoreDifficultyLevel;

    @Column(name = "score_continent", nullable = false)
    private char scoreContinent;

    @Column(name = "score", nullable = false)
    private long score;

    // ManyToOne-Beziehung zu Spieler, ein Spieler kann mehrere Highscore-Einträge haben
    @ManyToOne
    @JoinColumn(name = "spieler_id", nullable = false) // FK in der Highscore-Tabelle
    private Spieler spieler;
}

