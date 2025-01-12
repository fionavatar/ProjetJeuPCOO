package com.badlogic.drop;

public class ScoreManager {
    private int score;

    public ScoreManager() {
        this.score = 6;
    }
    public void incrementScore() {
        if (score<6){
            score++;
        }
    }

    public void decrementScore() {
        if (score>0){
            score--;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return score <= 0;
    }
}
