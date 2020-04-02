package com.example.bolasepak.database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;

@Entity(tableName = "matchpast")
public class Matchpast {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_match")
    private Integer id_match;

    @ColumnInfo(name = "idHomeTeam")
    private String idHomeTeam;

    @ColumnInfo(name = "homeTeamName")
    private String homeTeamName;

    @ColumnInfo(name = "homeTeamScore")
    private String homeTeamScore;

    @ColumnInfo(name = "homeImageURL")
    private String homeImageURL;

    @ColumnInfo(name = "homeShot")
    private String homeShot;

    @ColumnInfo(name = "homeScorer")
    private String homeScorer;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "idAwayTeam")
    private String idAwayTeam;

    @ColumnInfo(name = "awayTeamName")
    private String awayTeamName;

    @ColumnInfo(name = "awayTeamScore")
    private String awayTeamScore;

    @ColumnInfo(name = "awayImageURL")
    private String awayImageURL;

    @ColumnInfo(name = "awayShot")
    private String awayShot;

    @ColumnInfo(name = "awayScorer")
    private String awayScorer;

    public Matchpast(Integer id_match, String idHomeTeam, String homeTeamName, String homeTeamScore, String homeImageURL, String homeShot,
                     String homeScorer, String date, String idAwayTeam, String awayTeamName, String awayTeamScore, String awayImageURL,
                     String awayShot, String awayScorer) {
        this.id_match = id_match;
        this.idHomeTeam = idHomeTeam;
        this.homeTeamName = homeTeamName;
        this.homeTeamScore = homeTeamScore;
        this.homeImageURL = homeImageURL;
        this.homeShot = homeShot;
        this.homeScorer = homeScorer;
        this.date = date;
        this.idAwayTeam = idAwayTeam;
        this.awayTeamName = awayTeamName;
        this.awayTeamScore = awayTeamScore;
        this.awayImageURL = awayImageURL;
        this.awayShot = awayShot;
        this.awayScorer = awayScorer;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getHomeImageURL() {
        return homeImageURL;
    }

    public String getDate() {
        return date;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getAwayImageURL() {
        return awayImageURL;
    }

    @NonNull
    public Integer getId_match() {
        return id_match;
    }

    public String getAwayScorer() {
        return awayScorer;
    }

    public String getAwayShot() {
        return awayShot;
    }

    public String getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getHomeScorer() {
        return homeScorer;
    }

    public String getHomeShot() {
        return homeShot;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }

    public String getIdAwayTeam() {
        return idAwayTeam;
    }

    public String getIdHomeTeam() {
        return idHomeTeam;
    }
}

