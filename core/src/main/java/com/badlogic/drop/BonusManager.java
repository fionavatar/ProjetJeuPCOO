package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BonusManager {
    private Texture bonusTexture;
    private Sound bonusSound;
    private Viewport viewport;
    private Array<InteractiveObject> bonuses; // Liste des bonus
    private Player player;
    private float bonusTimer;
    private CollisionStrategy bonusStrategy;

    public BonusManager(String texturePath, String soundPath, Viewport viewport, Player player, ScoreManager scoreManager) {
        this.bonusTexture = new Texture(texturePath);
        this.bonusSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
        this.viewport = viewport;
        this.player = player;
        this.bonuses = new Array<>();
        this.bonusTimer = 0;

        // Initialiser la stratégie des bonus
        this.bonusStrategy = new BonusCollisionStrategy(scoreManager);
    }

    public void update(float deltaTime) {
        bonusTimer += deltaTime;

        if (bonusTimer > 3f) { // Crée un bonus toutes les 3 secondes
            bonusTimer = 0;
            createBonus();
        }

        for (int i = bonuses.size - 1; i >= 0; i--) {
            Bonus bonus = (Bonus) bonuses.get(i);
            bonus.update(deltaTime); // Met à jour la position du bonus

            if (bonus.isOutOfBounds(viewport.getWorldHeight())) {
                bonuses.removeIndex(i); // Supprime si hors écran
            } else if (bonus.checkCollision(player)) {
                bonuses.removeIndex(i); // Supprime si collision avec le seau
                bonusSound.play();      // Joue le son
            }
        }
    }

    private void createBonus() {
        Sprite bonusSprite = new Sprite(bonusTexture);
        bonusSprite.setSize(0.6f, 0.6f);
        bonusSprite.setPosition(
            MathUtils.random(0, viewport.getWorldWidth() - bonusSprite.getWidth()),
            viewport.getWorldHeight()
        );

        // Associez une stratégie de collision
        Bonus bonus = new Bonus(bonusSprite, bonusStrategy);
        bonuses.add(bonus);
    }

    public void draw(SpriteBatch batch) {
        for (InteractiveObject bonus : bonuses) {
            bonus.draw(batch);
        }
    }

    public void dispose() {
        bonusTexture.dispose();
        bonusSound.dispose();
    }
}
