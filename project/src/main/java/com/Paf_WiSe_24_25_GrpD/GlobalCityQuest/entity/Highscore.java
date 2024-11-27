package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "highscore",
    uniqueConstraints = @UniqueConstraint(columnNames = {"score_difficulty_level", "score_continent", "spieler_id"})
)
public class Highscore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score_difficulty_level", nullable = false)
    private int scoreDifficultyLevel;

    @Column(name = "score_continent", nullable = false)
    private String scoreContinent;

    @Column(name = "score", nullable = false)
    private long score;

    @ManyToOne
    @JoinColumn(name = "spieler_id", nullable = false)
    private Spieler spieler;

    // Getter und Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScoreDifficultyLevel() {
        return scoreDifficultyLevel;
    }

    public void setScoreDifficultyLevel(int scoreDifficultyLevel) {
        this.scoreDifficultyLevel = scoreDifficultyLevel;
    }

    public String getScoreContinent() {
        return scoreContinent;
    }

    public void setScoreContinent(String scoreContinent) {
        this.scoreContinent = scoreContinent;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Spieler getSpieler() {
        return spieler;
    }

    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }
}
