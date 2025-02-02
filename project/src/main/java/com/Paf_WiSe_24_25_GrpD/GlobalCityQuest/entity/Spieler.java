package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "spieler") 
public class Spieler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    // Ein Spieler kann mehrere Spiele als Spieler1 haben
    @OneToMany(mappedBy = "spieler1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spiel> spiele1;

    // Ein Spieler kann mehrere Spiele als Spieler2 haben
    @OneToMany(mappedBy = "spieler2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Spiel> spiele2;

    // Ein Spieler kann mehrere Highscores haben
    @OneToMany(mappedBy = "spieler", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Highscore> highscores;

    // Getter und Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Highscore> getHighscores() {
        return highscores;
    }

    public void setHighscores(Set<Highscore> highscores) {
        this.highscores = highscores;
    }

}
