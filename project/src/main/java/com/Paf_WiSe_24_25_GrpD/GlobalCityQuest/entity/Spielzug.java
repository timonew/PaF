package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
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

    // Getter und Setter für id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter und Setter für guessSpieler1
    public String getGuessSpieler1() {
        return guessSpieler1;
    }

    public void setGuessSpieler1(String guessSpieler1) {
        this.guessSpieler1 = guessSpieler1;
    }

    // Getter und Setter für guessSpieler2
    public String getGuessSpieler2() {
        return guessSpieler2;
    }

    public void setGuessSpieler2(String guessSpieler2) {
        this.guessSpieler2 = guessSpieler2;
    }

    // Getter und Setter für scoreSpieler1
    public long getScoreSpieler1() {
        return scoreSpieler1;
    }

    public void setScoreSpieler1(long scoreSpieler1) {
        this.scoreSpieler1 = scoreSpieler1;
    }

    // Getter und Setter für scoreSpieler2
    public long getScoreSpieler2() {
        return scoreSpieler2;
    }

    public void setScoreSpieler2(long scoreSpieler2) {
        this.scoreSpieler2 = scoreSpieler2;
    }

    // Getter und Setter für spiel
    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    // Getter und Setter für stadt
    public Stadt getStadt() {
        return stadt;
    }

    public void setStadt(Stadt stadt) {
        this.stadt = stadt;
    }
}
