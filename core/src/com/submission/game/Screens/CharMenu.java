package com.submission.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.submission.game.DataManager;
import com.submission.game.MainClass;

public class CharMenu implements Screen{

    private final MainClass mainClass;

    // Sprite batch sends all the textures to the GPU at once,
    // and helps in rendering the UI.
    SpriteBatch batch;

    // Decoded images from their .png file formats
    Texture background;
    Texture char1_Button;
    Texture char2_Button;
    TextureRegion textureRegion1;
    TextureRegion textureRegion2;
    TextureRegionDrawable textureRegionDrawable1;
    TextureRegionDrawable textureRegionDrawable2;

    // buttons to select characters -> rocket or boy
    ImageButton char1Button;
    ImageButton char2Button;
    private Stage stage;

    /**
     * Initialize all the textures, drawables and image buttons
     * also handling setting positions of all elements to be rendered
     * @param mc MainClass to help out in listening and raising events and special screens
     */
    public CharMenu(MainClass mc)
    {
        mainClass = mc;
        batch = new SpriteBatch();
        background = new Texture("back1.jpg");
        char2_Button = new Texture("char2.png");
        char1_Button = new Texture("char1.png");
        textureRegion1 = new TextureRegion(char1_Button);
        textureRegion2 = new TextureRegion(char2_Button);
        textureRegionDrawable1 = new TextureRegionDrawable(textureRegion1);
        textureRegionDrawable2 = new TextureRegionDrawable(textureRegion2);
        char1Button = new ImageButton(textureRegionDrawable1);
        char2Button = new ImageButton(textureRegionDrawable2);
        stage = new Stage(new ScreenViewport());
        stage.addActor(char1Button);
        stage.addActor(char2Button);
        Gdx.input.setInputProcessor(stage);
        char1Button.setPosition(Gdx.graphics.getWidth() / 2 - char1_Button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - char1_Button.getHeight() / 2 + 100);
        char2Button.setPosition(Gdx.graphics.getWidth() / 2 - char2_Button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - char2_Button.getHeight() / 2 - 100);

        // To select boy character as your game player
        char1Button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                FlappyBird.charac = 1;
                mainClass.setGameScreen();
                return false;
            }
        });

        // Select rocket as your player
        char2Button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                FlappyBird.charac = 2;
                mainClass.setGameScreen();
                return false;
            }
        });
    }

    @Override
    public void show() {

    }

    // Drawing the UI on screen using stage.draw and sending textures to gpu using batch
    // object
    @Override
    public void render(float delta) {
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw(); //Draw the ui
        batch.end();
    }

    @Override
    public void resize(int width, int height) {


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        stage.draw();
        batch.begin();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }

    @Override
    public void dispose() {
        if (DataManager.getDataManager().isSharing()) {
            return;
        }
        batch.dispose();
        stage.dispose();
    }
}
