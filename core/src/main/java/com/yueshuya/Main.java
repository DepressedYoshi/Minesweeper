package com.yueshuya;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;


public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameplayScreen());
    }
}
