package com.submission.game;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * This class sole purpose is to help in managing the consistency of data across whole session.
 * It stores high score, score and holds appId for facebook and its secret to login in facebook
 */
public class DataManager {

    static final String HIGH_SCORE = "com.submission.game.HIGH_SCORE";

    // TODO: Please update mAppId and mAppSecret with your FB AppId and AppSecret
    private String mAppId = "YOUR_FB_APP_ID";
    private String mAppSecret = "YOUR_FB_APP_SECRET";
    private Preferences mPreferences = null;
    private String mAccessToken = null;
    private static DataManager sDataManager = null;
    private int mScore;
    private int mHighScore;
    private MainClass mMainClass = null;
    private boolean mIsSharing = false;

    private DataManager() {
        //getData();
    }

    public static DataManager getDataManager() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
            sDataManager.mHighScore = 0;
            sDataManager.mScore = 0;
            sDataManager.initiateEventListenersList();
        }

        return sDataManager;
    }

    public MainClass getMainClass() {
        if (mMainClass == null) {
            mMainClass = new MainClass();
        }

        return mMainClass;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getHighScore() {
        return mHighScore;
    }

    public void setHighScore(int highScore) {
        mHighScore = highScore;
    }

    public String getAppId() {
        return mAppId;
    }

    public String getAppSecret() {
        return mAppSecret;
    }

    public boolean isSharing() {
        return mIsSharing;
    }

    public void setSharing(boolean isSharing) {
        mIsSharing = isSharing;
    }

    /**
     * Saving the data using preferences in java
     */
    public void saveData() {
        if (mPreferences == null)
            mPreferences = Preferences.systemNodeForPackage(DataManager.class);

        int score = mScore >= mHighScore ? mScore : mHighScore;
        mPreferences.putInt(HIGH_SCORE, score);

    }

    /**
     * getting data from stored preferences in java
     */
    private void getData() {
        if (mPreferences == null)
            mPreferences = Preferences.systemNodeForPackage(DataManager.class);

        mHighScore = mPreferences.getInt(HIGH_SCORE, 0);
    }

    private void loginUser() {

    }

    // Events list to notify facebook post
    private List<FacebookPostListener> mEventListenerList = null;

    // Code for event listeners
    private void initiateEventListenersList() {

        String message = null;

        try{
            mEventListenerList = new ArrayList<FacebookPostListener>();
        } catch (Exception ex) {
            message = ex.getMessage();

        }
    }

    // adding new listener for facebook post
    public void addFacebookPostEvent(FacebookPostListener listener) {
        if (mEventListenerList == null)
            return;

        mEventListenerList.add(listener);
    }

    // removing listener for not getting facebook post notifications
    public void removeFacebookPostEvent(FacebookPostListener listener) {
        mEventListenerList.remove(listener);
    }

    // firing the event to notify all listeners about facebook post
    private void fireGameOverEvent(FacebookEvent event) {
        Object[] listeners = mEventListenerList.toArray();
        for (FacebookPostListener listener : mEventListenerList) {
//            if (listeners[i] == FacebookPostListener.class) {
//                ((FacebookPostListener) listeners[i]).gameOver(event);
//            }
            listener.gameOver(event);
        }
    }

    // raising the event and populating the data
    public void raiseGameOverEvent(int score) {
        if (mEventListenerList == null)
            return;

        FacebookEvent event = new FacebookEvent(this);
        event.setScore(score);
        fireGameOverEvent(event);
    }

    // Event object to be passed as extra data
    class FacebookEvent extends EventObject {

        public int getScore() {
            return mScore;
        }

        public void setScore(int score) {
            mScore = score;
        }

        private int mScore;

        public FacebookEvent(Object o) {
            super(o);
        }
    }

    interface FacebookPostListener extends EventListener {
        void gameOver(FacebookEvent event);
    }
}
