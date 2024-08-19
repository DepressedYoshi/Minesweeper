package com.yueshuya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameplayScreen implements Screen {
    //object atht draws all sprite graphics
    private SpriteBatch spriteBatch;
    //object that draw shapes
    private ShapeRenderer shapeRenderer;
    //need camera to view the 2D world
    private Camera camera;
    //controll how the camera vew the world
    private Viewport viewport;




    /*
    runs one time at the very begginging
    all setup should happend here
    ie: load textures, sounds, setup screen, etc
     */
    @Override
    public void show() {
        //2D camera
        camera = new OrthographicCamera();
        //set camera to the middle of the window
        camera.position.set(1280/2,720/2,0);
        //update the camera to the changes above
        camera.update();
        // freeze my view to 1280x720 not matter the rsolution of the windows
        viewport = new FitViewport(12080, 720, camera);
        //empty instantiation of all the sawing stuff
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        shapeRenderer.setAutoShapeType(true);
    }

    public void cleaScreen(){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    /*
    this method runs as fast as it can or to a set fps
    repeatly, constantly looped
    Things include:
        1. process user input
        2. A.I. or program logic
        3. Draw all graphics
     */
    @Override
    public void render(float v) {
        cleaScreen();

        // all drawing of shapes must go between begin/end
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(0,0,100,100);
        shapeRenderer.end();

        //all graphics mush be between the begin and end
        spriteBatch.begin();

        spriteBatch.end();
    }

    @Override
    public void resize(int i, int i1) {
        viewport.update(i, i1);
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
    }
}
