package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player {
    private Sprite playerSprite;
    private Viewport viewport;
    private Array<Rectangle> obstacles; // Liste des obstacles
    private Vector2 previousPosition;
    private float speed;// Position précédente du seau

    public Player(String texturePath, Viewport viewport, Array<Rectangle> obstacles) {
        Texture bucketTexture = new Texture(texturePath);
        this.playerSprite = new Sprite(bucketTexture);
        this.playerSprite.setSize(0.4f, 0.4f);
        this.viewport = viewport;
        this.obstacles = (obstacles != null) ? obstacles : new Array<>(); // Initialisation par défaut
        this.previousPosition = new Vector2(playerSprite.getX(), playerSprite.getY());
        this.speed = 5f;

    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void handleInput() {
        float delta = Gdx.graphics.getDeltaTime();

        // Sauvegarder la position précédente
        previousPosition.set(playerSprite.getX(), playerSprite.getY());
        //On gère les déplacements horizontaux avec le clavier
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
        }

        // Déplacements verticaux
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerSprite.translateY(-speed * delta);
        }
        // Déplacements avec le trackpad
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            Vector2 touchPos = new Vector2(touchX, touchY);
            viewport.unproject(touchPos); // Convertit les coordonnées de l'écran en coordonnées du monde
            playerSprite.setCenter(touchPos.x, touchPos.y); // Met à jour la position X du seau
        }

        //empêcher le joueur de sortir du cadre
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, viewport.getWorldWidth() - playerSprite.getWidth()));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, viewport.getWorldHeight() - playerSprite.getHeight()));
        // Vérifier les collisions avec les obstacles
        Rectangle bucketBounds = new Rectangle(
            playerSprite.getX(),
            playerSprite.getY(),
            playerSprite.getWidth(),
            playerSprite.getHeight()
        );

        for (Rectangle obstacle : obstacles) {
            if (bucketBounds.overlaps(obstacle)) {
                // Si collision, revenir à la position précédente
                playerSprite.setX(previousPosition.x);
                playerSprite.setY(previousPosition.y);
                break;
            }
        }

    }


    public void draw(SpriteBatch batch) {
        playerSprite.draw(batch);
    }

    

    public void dispose() {
        playerSprite.getTexture().dispose();
    }

    public Rectangle getBoundingRectangle() {
        return playerSprite.getBoundingRectangle();
    }



}

