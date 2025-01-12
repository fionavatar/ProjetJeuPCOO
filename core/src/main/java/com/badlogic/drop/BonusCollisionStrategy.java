package com.badlogic.drop;

public class BonusCollisionStrategy implements CollisionStrategy {
    private ScoreManager scoreManager;

    public BonusCollisionStrategy(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void onCollision(Player player) {
        // Augmenter la vitesse du seau quand il touche un bonus
        player.setSpeed(player.getSpeed() * 1.2f); // Augmenter la vitesse de 20%
        System.out.println("Bonus attrapé ! Vitesse du seau augmentée à : " + player.getSpeed());

    }
}

