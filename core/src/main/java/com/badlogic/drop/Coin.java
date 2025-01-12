package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Coin extends InteractiveObject {
    public Coin(Sprite sprite, CollisionStrategy collisionStrategy) {
        super(sprite, collisionStrategy);
    }

    @Override
    public void update(float deltaTime) {
        sprite.translateY(-2f * deltaTime); // Descend verticalement
    }


}

