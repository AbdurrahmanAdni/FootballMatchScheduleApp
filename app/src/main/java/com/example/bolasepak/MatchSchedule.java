package com.example.bolasepak;

public class MatchSchedule {

    private String homeTeamId;
    private String homeTeamName;
    private String homeScore;
    private String homeImageURL;
    private String homeShot;
    private String homeScorer;
    private String date;
    private String awayTeamId;
    private String awayTeamName;
    private String awayScore;
    private String awayImageURL;
    private String awayShot;
    private String awayScorer;

    public MatchSchedule(String homeTeamId, String homeTeamName, String homeScore, String homeImageURL,
                         String homeShot, String homeScorer, String date, String awayTeamId,
                         String awayTeamName, String awayScore, String awayImageURL, String awayShot,
                         String awayScorer) {
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.homeScore = homeScore;
        this.homeImageURL = homeImageURL;
        this.homeShot = homeShot;
        this.homeScorer = homeScorer;
        this.date = date;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.awayScore = awayScore;
        this.awayImageURL = awayImageURL;
        this.awayShot = awayShot;
        this.awayScorer = awayScorer;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public String getHomeImageURL() {
        return homeImageURL;
    }

    public String getHomeShot() {
        return homeShot;
    }

    public String getHomeScorer() {
        return homeScorer;
    }

    public String getDate() {
        return date;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public String getAwayImageURL() {
        return awayImageURL;
    }

    public String getAwayShot() {
        return awayShot;
    }

    public String getAwayScorer() {
        return awayScorer;
    }
}
