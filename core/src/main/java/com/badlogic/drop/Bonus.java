package com.badlogic.drop;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bonus extends InteractiveObject {
    public Bonus(Sprite sprite, CollisionStrategy collisionStrategy) {
        super(sprite, collisionStrategy);
    }

    @Override
    public void update(float deltaTime) {
        sprite.translateY(-2f * deltaTime); // Le bonus descend verticalement
    }
}

