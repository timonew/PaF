package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stadt")
public class Stadt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stadt_name", nullable = false)
    private String stadtName;
    
    @Column(name = "koordinaten", nullable = false)
    private String koordinaten;
    
    @Column(name = "difficultyLevel", nullable = false)
    private int difficultyLevel;
    
    @Column(name = "continent", nullable = false)
    private String continent;
    
    // Eine Stadt kann in mehreren Spielzügen vorkommen (1:N Beziehung zu Spielzug)
    @OneToMany(mappedBy = "stadt")
    private Set<Spielzug> spielzug;
    
    // Getter für id
    public Long getId() {
        return id;
    }

    // Getter für Stadtname
    public String getStadtName() {
        return stadtName;
    }

    // Setter für Stadtname
    public void setStadtName(String stadtName) {
        this.stadtName = stadtName;
    }

    // Getter für Koordinaten
    public String getKoordinaten() {
        return koordinaten;
    }

    // Setter für Koordinaten
    public void setKoordinaten(String koordinaten) {
        this.koordinaten = koordinaten;
    }

    // Getter für Difficulty Level
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    // Setter für Difficulty Level
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    // Getter für Kontinent
    public String getContinent() {
        return continent;
    }

    // Setter für Kontinent
    public void setContinent(String continent) {
        this.continent = continent;
    }

    // Getter für Spielzüge
    public Set<Spielzug> getSpielzug() {
        return spielzug;
    }

    // Setter für Spielzüge
    public void setSpielzug(Set<Spielzug> spielzug) {
        this.spielzug = spielzug;
    }
}
