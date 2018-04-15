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

/**
 * Just a menu to be shown when character collides with enemy or tube
 */
public class GamOverMenu  implements Screen {

    private final MainClass mainClass;

    // batch to send data to GPU and textures are the ones that need to be drawn on screen
    SpriteBatch batch;
    Texture background;
    Texture exit_Button;
    Texture play_Button;
    TextureRegion textureRegion1;
    TextureRegion textureRegion2;
    TextureRegionDrawable textureRegionDrawable1;
    TextureRegionDrawable textureRegionDrawable2;
    ImageButton playButton;
    ImageButton exitButton;
    private Stage stage;

    /**
     * Setting the initial state of the game object
     * @param mc a listener class to help out in sending events to change state of the game
     */
    public GamOverMenu (MainClass mc)
    {
        mainClass = mc;
        batch = new SpriteBatch();
        background = new Texture("back1.jpg");
        setFbButton();
        play_Button = new Texture("play_button.png");
        textureRegion1 = new TextureRegion(play_Button);
        textureRegion2 = new TextureRegion(exit_Button);
        textureRegionDrawable1 = new TextureRegionDrawable(textureRegion1);
        textureRegionDrawable2 = new TextureRegionDrawable(textureRegion2);
        playButton = new ImageButton(textureRegionDrawable1);
        exitButton = new ImageButton(textureRegionDrawable2);
        stage = new Stage(new ScreenViewport());
        stage.addActor(playButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
        // setting button positions and setting event listeners
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - play_Button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - play_Button.getHeight() / 2 + 100);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2 - exit_Button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - exit_Button.getHeight() / 2 - 100);
        playButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                mainClass.setScreen(new MainMenu(mainClass));
                return false;
            }
        });
        exitButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                //Gdx.app.exit();
                shareToFacebook();
                return false;
            }
        });
    }

    private void shareToFacebook() {
        DataManager.getDataManager().setSharing(true);
        DataManager.getDataManager().raiseGameOverEvent(0);
        mainClass.setScreen(new MainMenu(mainClass));
        //mainClass.setScreen(new MainMenu(mainClass));
    }

    private void setFbButton() {
        String error = null;
        try {
            exit_Button = new Texture("facebook_logo_version_2.png");
        } catch (Exception ex) {
            error = ex.getMessage();
        }
    }

    @Override
    public void show() {

    }

    // render the UI and screen textures
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
