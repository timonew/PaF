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

}
