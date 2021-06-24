package com.example.simonglish;

public class ScoreBoard {
    private String username;
    private String score;
    private String color;

    public ScoreBoard( String username,String score, String color) {
        this.score = score;
        this.username=username;
        this.color=color;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }






}
