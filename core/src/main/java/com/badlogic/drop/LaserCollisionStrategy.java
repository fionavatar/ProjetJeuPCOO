package com.badlogic.drop;

public class LaserCollisionStrategy implements CollisionStrategy {
    private ScoreManager scoreManager;

    public LaserCollisionStrategy(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void onCollision(Player player) {
        scoreManager.decrementScore();
        System.out.println("Ennemy got us ! Score: " + scoreManager.getScore());
    }
}

