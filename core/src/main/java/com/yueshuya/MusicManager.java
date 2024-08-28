package com.yueshuya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    private Sound gameOver;
    private Sound playerWin;
    private Sound clickOnTile;
    private Sound bombReveal;
    private Sound optionSelect;
    private Sound bgm;
    private Sound flagOn;
    private Sound flagOff;

    public MusicManager(GameplayScreen gameplayScreen){
        loadSound();
        init();
    }

    private void init(){
        bgm.loop(0.2f);
    }
    public void playGameOver(){
        bombReveal.play();
        bgm.stop();
    }
    public void playclickSound(){
        clickOnTile.play();
    }

    public void playWin(){
        playerWin.play();
    }
    public void dispose(){
        gameOver.dispose();
        playerWin.dispose();
        clickOnTile.dispose();
        bombReveal.dispose();
        optionSelect.dispose();
        bgm.dispose();
    }

    public void playFlagOn(boolean on){
        if (on){
            flagOn.play();
        }else
            flagOff.play();
    }
    private void loadSound(){
        gameOver = Gdx.audio.newSound(Gdx.files.internal("gameOver.wav"));
        playerWin = Gdx.audio.newSound(Gdx.files.internal("playerWin.wav"));
        clickOnTile = Gdx.audio.newSound(Gdx.files.internal("safeTile.wav"));
        bombReveal = Gdx.audio.newSound(Gdx.files.internal("bombReveal.wav"));
        flagOn = Gdx.audio.newSound(Gdx.files.internal("flag on.wav"));
        flagOff = Gdx.audio.newSound(Gdx.files.internal("flag off.wav"));
        bgm = Gdx.audio.newSound(Gdx.files.internal("Phonk bgm.mp3"));
    }
}
