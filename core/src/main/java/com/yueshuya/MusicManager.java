package com.yueshuya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    private Sound gameOver;
    private Sound playerWin;
    private Sound clickOnBomb;
    private Sound clickOnTile;
    private Sound bombReveal;
    private Sound optionSelect;

    public MusicManager(GameplayScreen gameplayScreen){
        loadSound();
    }

    public void playGameOver(){
        gameOver.play();
    }

    public void playBomb(){
        bombReveal.play();
    }


    private void loadSound(){
        gameOver = Gdx.audio.newSound(Gdx.files.internal("gameOver.wav"));
        playerWin = Gdx.audio.newSound(Gdx.files.internal("playerWin.wav"));
        clickOnBomb = Gdx.audio.newSound(Gdx.files.internal("bombTile.wav"));
        clickOnTile = Gdx.audio.newSound(Gdx.files.internal("safeTile.wav"));
        bombReveal = Gdx.audio.newSound(Gdx.files.internal("bombReveal.wav"));
        optionSelect = Gdx.audio.newSound(Gdx.files.internal("gameOver.wav"));
    }
}
