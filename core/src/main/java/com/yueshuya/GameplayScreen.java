package com.yueshuya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameplayScreen implements Screen {
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Camera camera;
    private Viewport viewport;
    private GameBoard gameBoard;
    private BitmapFont customFont;
    private boolean gameOver = false;
    private float gameTimer = 0; // Timer variable

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        gameBoard = new GameBoard(this);

        // Load custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("BebasNeue-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24; // Adjust font size
        parameter.color = Color.WHITE; // Font color
        customFont = generator.generateFont(parameter);
        generator.dispose(); // Don't forget to dispose generator after use
    }

    public void clearScreen() {
        Gdx.gl.glClearColor(0.5F, 0.5F, 0.5F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void handleMouseClick() {
        if (gameBoard.getNumFlags() == 0 && gameBoard.playerWon()) {
            gameOver = true;
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 worldCoordinates = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            gameBoard.leftClick((int) worldCoordinates.x, (int) worldCoordinates.y);
            System.out.println((int) worldCoordinates.x + "  "+(int) worldCoordinates.y);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector2 worldCoordinates = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            gameBoard.rightClick((int) worldCoordinates.x, (int) worldCoordinates.y);
        }
    }

    private void drawGUI() {
        if (!gameOver) {
            customFont.draw(spriteBatch, "Bombs left to flag: " + gameBoard.getNumFlags(), 100, 700);
            customFont.draw(spriteBatch, "Time: " + (int) gameTimer + " seconds", 100, 670); // Display timer
        } else {
            if (gameBoard.playerWon()) {
                gameBoard.winSound();
                gameBoard.gameOver();
                customFont.draw(spriteBatch, "GREAT JOB, YOU WIN - press spacebar to play again", 100, 700);
            } else {
                customFont.draw(spriteBatch, "YOU LOSE - press spacebar to play again", 100, 700);
            }
        }
    }

    private void handleKeyPresses() {
        if (gameOver && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gameBoard = new GameBoard(this);
            gameOver = false;
            gameTimer = 0; // Reset timer when a new game starts
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();
        if (!gameOver) {
            gameTimer += delta; // Increment timer if game is ongoing
            handleMouseClick();
        }
        handleKeyPresses();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.end();
        spriteBatch.begin();
        gameBoard.draw(spriteBatch);
        drawGUI();
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        gameBoard.dispose();
        customFont.dispose(); // Dispose font when no longer needed
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
