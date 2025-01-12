package com.badlogic.drop;

public class CoinCollisionStrategy implements CollisionStrategy {
    private ScoreManager scoreManager;

    public CoinCollisionStrategy(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void onCollision(Player player) {
        scoreManager.incrementScore();
        System.out.println("Goutte attrapée ! Score: " + scoreManager.getScore());
    }
}

