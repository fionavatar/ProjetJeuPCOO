package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LaserManager {
    private Texture ennemyTexture;
    private Viewport viewport;
    private Array<InteractiveObject> ennemies; // Utilisez InteractiveObject
    private Player player;
    private float ennemyTimer;
    private CollisionStrategy ennemyStrategy;

    public LaserManager(String texturePath, Viewport viewport, Player player, ScoreManager scoreManager) {
        this.ennemyTexture = new Texture(texturePath);
        this.viewport = viewport;
        this.player = player;
        this.ennemies = new Array<>();
        this.ennemyTimer = 0;

        // Initialiser la stratégie des gouttes
        this.ennemyStrategy = new LaserCollisionStrategy(scoreManager);
    }

    public void update(float deltaTime) {
        ennemyTimer += deltaTime;


        if (ennemyTimer > 1f) {
            ennemyTimer = 0;
            createEnnemy();
        }

        for (int i = ennemies.size - 1; i >= 0; i--) {
            Laser laser = (Laser) ennemies.get(i);
            laser.update(deltaTime); // Met à jour la position

            if (laser.isOutOfBounds(viewport.getWorldWidth())) {
                ennemies.removeIndex(i); // Supprime si hors écran
            } else if (laser.checkCollision(player)) {
                ennemies.removeIndex(i); // Supprime si collision avec le seau

            }
        }

    }

    private void createEnnemy() {
        Sprite ennemySprite = new Sprite(ennemyTexture);
        ennemySprite.setSize(0.6f, 0.6f);
        ennemySprite.setPosition(
            0,MathUtils.random(0, viewport.getWorldHeight() - ennemySprite.getHeight())
        );

        // Associez une stratégie de collision
        Laser laser = new Laser(ennemySprite, ennemyStrategy);
        ennemies.add(laser);
    }


    public void draw(SpriteBatch batch) {
        for (InteractiveObject ennemy : ennemies) {
            ennemy.draw(batch);
        }
    }

    public void dispose() {
        ennemyTexture.dispose();

    }
}

