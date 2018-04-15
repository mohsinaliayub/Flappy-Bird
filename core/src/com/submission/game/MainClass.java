package com.submission.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.submission.game.Screens.CharMenu;
import com.submission.game.Screens.FlappyBird;
import com.submission.game.Screens.MainMenu;

import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

/**
 * Perform game related events and help out to set the screen menus
 */
public class MainClass extends Game implements ApplicationListener {

    @Override
    public void create() {
        setMenuScreen();
    }

    // Set initial game play screen when user taps on Play button
    public void setGameScreen()
    {
        this.setScreen(new FlappyBird(this));
    }

    // showing the Main menu where we have only two buttons just to play or to exit
    public void setMenuScreen()
    {
        this.setScreen(new MainMenu(this));
    }

    // showing the char menu where game is actually played
    public void setCharScreen()
    {
        this.setScreen(new CharMenu(this));
    }



    @Override
    public void dispose() {
        if (DataManager.getDataManager().isSharing()) {
            return;
        }
        super.dispose();
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

//    private EventListenerList mEventListenerList = null;
//
//    // Code for event listeners
//    private void initiateEventListenersList() {
//        try{
//            mEventListenerList = new EventListenerList();
//        } catch (Exception ex) {
//
//
//
//        }
//    }
//
//    public void addGameOverEventListener(GameOverEventListener listener) {
//        if (mEventListenerList == null)
//            return;
//
//        mEventListenerList.add(GameOverEventListener.class, listener);
//    }
//
//    public void removeGameOverEventListener(GameOverEventListener listener) {
//        mEventListenerList.remove(GameOverEventListener.class, listener);
//    }
//
//    private void fireGameOverEvent(GameOverEvent event) {
//        Object[] listeners = mEventListenerList.getListenerList();
//        for (int i = 0; i < listeners.length; i = i + 2) {
//            if (listeners[i] == GameOverEventListener.class) {
//                ((GameOverEventListener) listeners[i + 1]).gameOver(event);
//            }
//        }
//    }
//
//    public void raiseGameOverEvent(int score) {
//        if (mEventListenerList == null)
//            return;
//
//        GameOverEvent event = new GameOverEvent(null);
//        event.setScore(score);
//        fireGameOverEvent(event);
//    }
}

