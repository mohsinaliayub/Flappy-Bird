package com.submission.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.submission.game.Screens.FlappyBird;


public class AndroidLauncher extends AndroidApplication implements DataManager.FacebookPostListener {

	final String SHARED_PREF = "com.submission.game.SHARED_PREF";
	final String HIGH_SCORE = "com.submission.game.HIGH_SCORE";

	MainClass mMainClass;

	/**
	 * When app launches this method is instantiated
	 * @param savedInstanceState previous stored app state
	 */
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Getting the configuration for application to render
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// setting the main class
		mMainClass = new MainClass();

		// shared preferences to retrieve previous high score
		SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
		DataManager.getDataManager().setHighScore(prefs.getInt(HIGH_SCORE, 0));

		// initializing the app
		initialize(mMainClass, config);
		DataManager.getDataManager().addFacebookPostEvent(this);
	}

	/**
	 * At the end of game we save the session state which is game highest score in this session
	 * to the shared preferences
	 */
	@Override
	protected void onDestroy() {
		saveHighScore();

		if (DataManager.getDataManager().isSharing()) {
			sharingToSocialMedia("com.facebook.katana");
		} else
			super.onDestroy();
	}

	private void saveHighScore() {
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
		editor.putInt(HIGH_SCORE, DataManager.getDataManager().getHighScore());
		editor.apply();
	}

	// creating an intent to start facebook post activity
	public void sharingToSocialMedia(String application) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");
		//intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		intent.putExtra(Intent.EXTRA_TITLE, "Dodger");
//		intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout my score on dodger "
//				+ DataManager.getDataManager().getHighScore());

		boolean installed = checkAppInstall(application);
		String message = null;
		if (installed) {
			intent.setPackage(application);
			try {
                startActivity(intent);
            } catch (Exception e) {
			    message = e.getMessage();
			    System.out.print(message);
            }

            DataManager.getDataManager().setSharing(false);
		}

	}


	// checking whether facebook is installed on the system
	private boolean checkAppInstall(String uri) {
		PackageManager pm = getPackageManager();
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return false;
	}

	// raised when event is launched and do work in response by
	// calling facebook intent
	@Override
	public void gameOver(DataManager.FacebookEvent event) {
		int score = event.getScore();
		saveHighScore();
		if (DataManager.getDataManager().isSharing())
			sharingToSocialMedia("com.facebook.katana");
	}
}
