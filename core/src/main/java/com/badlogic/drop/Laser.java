package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Laser extends InteractiveObject {
    public Laser(Sprite sprite, CollisionStrategy collisionStrategy) {
        super(sprite, collisionStrategy);
    }

    @Override
    public void update(float deltaTime) {
        sprite.translateX(1.5f * deltaTime); // Gauche à droite
    }
}


