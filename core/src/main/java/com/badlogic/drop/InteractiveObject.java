package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class InteractiveObject {
    protected Sprite sprite;
    private CollisionStrategy collisionStrategy;

    public InteractiveObject(Sprite sprite, CollisionStrategy collisionStrategy) {
        this.sprite = sprite;
        this.collisionStrategy = collisionStrategy;
    }

    // Méthode pour mettre à jour la position
    public abstract void update(float deltaTime);

    public boolean isOutOfBounds(float worldHeight) {
        return sprite.getY() < -sprite.getHeight();
    }

    public boolean checkCollision(Player player) {
        if (sprite.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
            collisionStrategy.onCollision(player); // Exécuter la stratégie
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
