package com.submission.game.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.submission.game.DataManager;
import com.submission.game.MainClass;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter implements Screen{

    int level = 1;
    public static int charac = 1;

    // Properties of the game where batch performs bulk of data processing of images
    // Textures to be drawn on the screen, tube rectangles to draw hurdle tubes in rectangular shapes
    // BitmapFonts to show score and high score
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bTube;
	Texture tTube;
	Texture gameOver;
	Circle birdCircle;
	Texture enemy;
	Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    BitmapFont font;
    private BitmapFont mHighScoreFont;

    private MainClass mainClass;

    // Vertical position of character during game in birY, gameState to tell what is the state of game
    // either running or exiting or pause. maxTubeOffset to tell the distance between tubes.
    // score, and scoring tube to help in counting score whenever character passes a tube.
    // Random to help out in randomly selecting the distance of enemies and tubes.
	float birdY;
	int gameState;
	float velocity;
	float gravity;
	float gap;
	float maxTubeOffset;
	float tubeVelocity;
	float enemyVelocity;
	int numberOfTubes = 4;
	int numberofEnemies = 2;
    Circle[] enemyCircle;
	float tubeOffset[] = new float[numberOfTubes];
    float tubeX[] = new float[numberOfTubes];
    float enemyX[] = new float[numberofEnemies];
    float enemyY[] = new float[numberofEnemies];
    float distanceBetweenTubes;
    int score = 0;
    int scoringTube;
	Random randGen;


    /**
     * Initiate the class state and start game
     * @param mc MainClass to handle listener events and raise them
     */
    public FlappyBird(MainClass mc)
    {
        mainClass = mc;
        batch = new SpriteBatch();
        birdCircle = new Circle();
        font = new BitmapFont(Gdx.files.internal("multipagefont.fnt"), false);
        font.setColor(Color.WHITE);
        font.getData().scale(1);
        mHighScoreFont = new BitmapFont(Gdx.files.internal("multipagefont.fnt"), false);
        mHighScoreFont.setColor(Color.WHITE);
        mHighScoreFont.getData().scale(1);
        startGame();
    }

    /**
     * drawing enemies in the game and selecting textures for characters
     */
	private void startGame(){
	    // instantiating circle to hold enemy and rectangles size from top and bottom
        enemyCircle = new Circle[numberofEnemies];
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        // Below we are selecting how a character looks like..
        // Help out in drawing rocket and boy on choose screen.
        if (charac == 1) {
            bird = new Texture("char1.png");
        }else{
            bird = new Texture("char2.png");
        }

        // Tube designs for top and bottom
        bTube = new Texture("bottomtube.png");
        tTube = new Texture("toptube.png");

        // game over texture to be displayed at the bottom
        gameOver = new Texture("download.png");
        // enemy characters, if collide will cancel the game
        enemy = new Texture("enemy.png");

        // Setting background images for levels
        if (level == 1){
            background = new Texture("back1.jpg");
        }else if (level == 2){
            background = new Texture("back2.jpg");
        }else if (level == 4){
            background = new Texture("back3.jpg");
        }else{
            background = new Texture("back4.jpg");
        }

        // setting the gravity pull in each level to pull character most quickly
        if (level == 1) {
            gravity = 1f;
        }else if (level == 2){
            gravity = 1.25f;
        }else if (level == 3){
            gravity = 1.5f;
        }else{
            gravity = 1.75f;
        }

        // setting the gap between next tubes to make character it harder to score more runs
        if (level > 2){
            gap = 200;
        }
        else {
            gap = 400;
        }

        // velocity at which tubes are made at distances. and how quickly they move towards character
        if (level == 1) {
            tubeVelocity = 3.5f;
        }else if (level == 2){
            tubeVelocity = 4;
        }else if (level == 3){
            tubeVelocity = 4.5f;
        }else{
            tubeVelocity = 5;
        }

        // rate at which enemy is marching
        if (level == 1) {
            enemyVelocity = 3;
        }else if (level == 2){
            enemyVelocity = 3.5f;
        }else if (level == 3){
            enemyVelocity = 4;
        }else{
            enemyVelocity = 4.5f;
        }

        // setting the character position in the middle of the game at start
        birdY = Gdx.graphics.getHeight() / 2 - bird.getHeight() / 4;
        scoringTube = 0;
        gameState = 0;
        velocity = 0;
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 50;
        randGen = new Random();

        // distance between top and bottom tubes
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
        enemyY[0] = (randGen.nextFloat()) * (Gdx.graphics.getHeight() - gap - 100);
        enemyX[0] = Gdx.graphics.getWidth();
        enemyCircle[0] = new Circle();
        enemyY[1] = enemyY[0] - gap * 2;
        enemyX[1] = Gdx.graphics.getWidth() + gap * 2;
        enemyCircle[1] = new Circle();
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - bTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void show() {

    }

    /**
     * rendering the data at runtime to judge game state
     * @param delta
     */
    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // is game currently playing
        if (gameState == 1)
        {
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
                score++;
                if (scoringTube < numberOfTubes - 1){
                    scoringTube++;
                }else{
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched())
            {
                velocity = -15f;
            }

            for (int i = 0; i < numberofEnemies; i++){
                if (enemyX[i] < - enemy.getWidth()){
                    enemyX[i] = Gdx.graphics.getWidth();
                    enemyY[i] = randGen.nextFloat() * Gdx.graphics.getHeight() + i * gap;
                    enemyCircle[i] = new Circle(enemyX[i] + enemy.getWidth() / 2, enemyY[i] + enemy.getHeight() / 2, enemy.getHeight() / 2);
                }
                else{
                    enemyX[i] -= enemyVelocity;
                    enemyCircle[i] = new Circle(enemyX[i] + enemy.getWidth() / 2, enemyY[i] + enemy.getHeight() / 2, enemy.getHeight() / 2);
                }
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < - tTube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
                }
                else {
                    tubeX[i] -= tubeVelocity;

                }
                batch.draw(bTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bTube.getHeight() + tubeOffset[i]);
                batch.draw(tTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tTube.getWidth(), tTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bTube.getHeight() + tubeOffset[i], bTube.getWidth(), bTube.getHeight());
            }
            if (birdY > 0 || birdY > Gdx.graphics.getHeight()) {
                velocity += gravity;
                birdY -= velocity;
            }else{
                gameState = 2;
            }
        } else if (gameState == 0){
            if (Gdx.input.justTouched())
            {
                gameState = 4;
            }
        }else if (gameState == 4){
            if (Gdx.input.justTouched())
            {
                gameState = 1;
            }
        }

        // game over and sending high score to save in the game
        // drawing end scene view and sending those to GPU.
        // also setting screen to display game over menu to either continue or share to facebook
        else if (gameState == 2){
            DataManager.getDataManager().saveData();
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            //mainClass.raiseGameOverEvent(score);
            if (Gdx.input.justTouched()){
                //mainClass.setScreen(new MainMenu(mainClass));
                mainClass.setScreen(new GamOverMenu(mainClass));
            }
        }


        // drawing the positon of character
        batch.draw(bird, Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);
        for (int i = 0; i < numberofEnemies; i++) {
            batch.draw(enemy, enemyX[i], enemyY[i]);
        }

        // drawing score if score is bigger than previous high score then changing the high score as well
        if (score > DataManager.getDataManager().getHighScore())
            DataManager.getDataManager().setHighScore(score);

        font.draw(batch, "Score :" + String.valueOf(score), Gdx.graphics.getWidth() / 8 - 5,
                Gdx.graphics.getHeight() - 60);
        mHighScoreFont.draw(batch, "High Score :" + String.valueOf(DataManager.getDataManager().getHighScore()),
                Gdx.graphics.getWidth() / 8 - 25, Gdx.graphics.getHeight() - 10);
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + bird.getHeight() / 2, bird.getWidth() / 2);
        for (int i = 0; i < numberofEnemies; i++){
            if (Intersector.overlaps(birdCircle, enemyCircle[i])){
                gameState = 2;
            }
        }
        for (int i = 0; i < numberOfTubes; i++) {
            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                gameState = 2;
            }
        }

        // changing the background if score reaching certain point
        if (score > 9 && level == 1){
            level = 2;
            gameState = 4;
            startGame();
        }
        if (score > 29 && level == 2){
            level = 3;
            gameState = 4;
            startGame();
        }
        if (score > 49 && level == 3){
            level = 4;
            gameState = 4;
            startGame();
        }
    }

    @Override
    public void hide() {

    }

    @Override
	public void dispose () {
        if (DataManager.getDataManager().isSharing()) {
            return;
        }
		batch.dispose();
		background.dispose();
	}
}
