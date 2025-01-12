// DropletManager.java
package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CoinManager {
    private Texture dropTexture;
    private Sound dropSound;
    private Viewport viewport;
    private Array<InteractiveObject> coins; // Utilisez InteractiveObject
    private Player player;
    private float dropTimer;
    private CollisionStrategy dropletStrategy;

    public CoinManager(String texturePath, String soundPath, Viewport viewport, Player player, ScoreManager scoreManager) {
        this.dropTexture = new Texture(texturePath);
        this.dropSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
        this.viewport = viewport;
        this.player = player;
        this.coins = new Array<>();
        this.dropTimer = 0;

        // Initialiser la stratégie des gouttes
        this.dropletStrategy = new CoinCollisionStrategy(scoreManager);
    }

    public void update(float deltaTime) {
        dropTimer += deltaTime;

        if (dropTimer > 1f) {
            dropTimer = 0;
            createCoins();
        }

        for (int i = coins.size - 1; i >= 0; i--) {
            Coin coin = (Coin) coins.get(i);
            coin.update(deltaTime); // Met à jour la position

            if (coin.isOutOfBounds(viewport.getWorldHeight())) {
                coins.removeIndex(i); // Supprime si hors écran
            } else if (coin.checkCollision(player)) {
                coins.removeIndex(i); // Supprime si collision avec le seau
                dropSound.play();        // Joue le son
            }
        }

    }

    private void createCoins() {
        Sprite dropletSprite = new Sprite(dropTexture);
        dropletSprite.setSize(0.6f, 0.6f);
        dropletSprite.setPosition(
            MathUtils.random(0, viewport.getWorldWidth() - dropletSprite.getWidth()),
            viewport.getWorldHeight()
        );

        // Associez une stratégie de collision
        Coin coin = new Coin(dropletSprite, dropletStrategy);
        coins.add(coin);
    }


    public void draw(SpriteBatch batch) {
        for (InteractiveObject droplet : coins) {
            droplet.draw(batch);
        }
    }

    public void dispose() {
        dropTexture.dispose();
        dropSound.dispose();
    }
}
