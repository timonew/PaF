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
@Table(name = "spielzug")
public class Spielzug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guessSpieler1", nullable = true)
    private String guessSpieler1;
    
    @Column(name = "guessSpieler2", nullable = true)
    private String guessSpieler2;
    
    @Column(name = "scoreSpieler1", nullable = true)
    private long scoreSpieler1;
    
    @Column(name = "scoreSpieler2", nullable = true)
    private long scoreSpieler2;
    

    // Spielzug gehört zu einem bestimmten Spiel (N:1 Beziehung zu Spiel)
    @ManyToOne
    @JoinColumn(name = "spiel_id", nullable = false)
    private Spiel spiel;

    // Spielzug bezieht sich auf eine Stadt (N:1 Beziehung zu Stadt)
    @ManyToOne
    @JoinColumn(name = "stadt_id", nullable = false)
    private Stadt stadt;

}
