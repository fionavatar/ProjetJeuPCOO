package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

public class Main implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Player player;
    private CoinManager coinManager;
    private LaserManager laserManager;
    private BonusManager bonusManager;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Liste des obstacles
    private Array<Rectangle> obstacles;
    // Gestionnaire de score
    private ScoreManager scoreManager;
    //Vies
    private Texture fullHeart;
    private Texture halfHeart;

    private Texture gameOver;

    @Override
    public void create() {

        spriteBatch = new SpriteBatch();
        //coeur vies
        fullHeart = new Texture("fullHeart.png");
        halfHeart = new Texture("halfHeart.png");

        // Charger la carte Tiled
        tiledMap = new TmxMapLoader().load("map.tmx");

        gameOver = new Texture("gameOver.png");

        // Récupérer les dimensions de la carte
        int mapWidth = tiledMap.getProperties().get("width", Integer.class);        // Nombre de tiles en largeur
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);      // Nombre de tiles en hauteur
        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);   // Largeur d'une tile en pixels
        int tileHeight = tiledMap.getProperties().get("tileheight", Integer.class); // Hauteur d'une tile en pixels

        // Calculer la taille du monde en unités (tiles transformées en unités)
        float worldWidth = mapWidth * (tileWidth / 32f);   // Échelle de 1 unité = 32 pixels
        float worldHeight = mapHeight * (tileHeight / 32f);

        // Configurer le viewport pour s'adapter à la taille de la carte
        viewport = new FitViewport(worldWidth, worldHeight);

        // Configurer le renderer pour les tiles
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 32f); // Facteur d'échelle pour correspondre à 1 unité = 32 pixels

        // Initialiser la liste des obstacles
        obstacles = new Array<>();
        for (MapObject object : tiledMap.getLayers().get("ObjectLayer").getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                obstacles.add(new Rectangle(
                    rectangle.x / 32f, // Conversion en unités du monde
                    rectangle.y / 32f,
                    rectangle.width / 32f,
                    rectangle.height / 32f
                ));
            }
        }
        scoreManager = new ScoreManager();
        // Passez cette liste au constructeur
        player = new Player("Fumiko.png", viewport, obstacles);

        coinManager = new CoinManager("01coin.png", "drop.mp3", viewport, player, scoreManager);
        laserManager = new LaserManager("beams.png", viewport, player, scoreManager);

        bonusManager = new BonusManager("saphir.png", "drop.mp3", viewport, player, scoreManager);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        // Si le joueur a perdu, affichez "Game Over" et arrêtez le jeu
        if (scoreManager.isGameOver()) {
            renderGameOver();
            return; // Stoppez le reste de la logique
        }

        player.handleInput();
        coinManager.update(Gdx.graphics.getDeltaTime());
        laserManager.update(Gdx.graphics.getDeltaTime());
        bonusManager.update(Gdx.graphics.getDeltaTime());

        viewport.apply();

        // Rendu de la carte Tiled
        mapRenderer.setView((OrthographicCamera) viewport.getCamera());
        mapRenderer.render();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        player.draw(spriteBatch);
        coinManager.draw(spriteBatch);
        laserManager.draw(spriteBatch);
        bonusManager.draw(spriteBatch);

        renderLives(spriteBatch, scoreManager.getScore(), 0, viewport.getWorldHeight() -1, 1.2f);

        spriteBatch.end();

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        player.dispose();
        coinManager.dispose();
        laserManager.dispose();
        bonusManager.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();

    }
    private void renderLives(SpriteBatch batch, int score, float x, float y, float spacing) {
        int nbCoeur = score/2;
        for (int i = 0; i < nbCoeur; i++) {
            batch.draw(fullHeart, x + i * spacing, y, 1, 1);
        }
        for (int i = nbCoeur; i < score - nbCoeur; i++) {
            batch.draw(halfHeart, x + i * spacing, y, 1, 1);
        }
    }

    private void renderGameOver() {
        spriteBatch.begin();

        // Définir les dimensions d'affichage
        float targetWidth = viewport.getWorldWidth() * 0.8f; // 80% de la largeur de l'écran
        float targetHeight = gameOver.getHeight() * (targetWidth / gameOver.getWidth()); // Maintenir le ratio

        // Calculer la position pour centrer l'image
        float x = (viewport.getWorldWidth() - targetWidth) / 2;
        float y = (viewport.getWorldHeight() - targetHeight) / 2;

        // Dessiner l'image avec les nouvelles dimensions
        spriteBatch.draw(gameOver, x, y, targetWidth, targetHeight);

        // Optionnel : Ajouter du texte ou des instructions
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1);
        font.draw(spriteBatch, "Press R to Restart or Q to Quit", viewport.getWorldWidth() / 2 - 100, y - 20);

        spriteBatch.end();

    }






}

