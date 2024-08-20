package com.yueshuya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;

public class GameBoard {
    private int[][] board; // data structure as described in the notes
    private int numBombs; // number of bombs in the grid
    private int numFlags; // the number of flags that sill have to be placed by the player
    public static final int BOMB = -1; // help with readability
    private GameplayScreen gameplayScreen;

    private Texture emptyTile;
    private Texture emptyFloorTile;
    private Texture bombTile;
    private Texture flagTile;
    private Texture oneTile;
    private Texture twoTile;
    private Texture threeTile;
    private Texture fourTile;
    private Texture fiveTile;
    private Texture sixTile;
    private Texture sevenTile;
    private Texture eightTile;


    public GameBoard(GameplayScreen gameplayScreen){
        this.gameplayScreen = gameplayScreen;
        board = new int[16][30];
        numBombs = 50;
        this.numFlags = numBombs;
        init();
    }

    public GameBoard(GameplayScreen gameplayScreen, int numRow, int numCols, int numBombs){
        this.gameplayScreen = gameplayScreen;
        board = new int[numRow][numCols];
        this.numBombs = numBombs;
        this.numFlags = numBombs;
        init();
    }

    private void init(){
        loadGraphics();
        placeBomb();
        numberBoard();
    }

    private void placeBomb(){
        int placed = 0;
        while (placed < numBombs){
            int rowRandom = (int) (Math.random()*board.length);
            int colRandom = (int) (Math.random()*board[0].length);
            if (board[rowRandom][colRandom] != BOMB){
                board[rowRandom][colRandom] = BOMB;
                placed++;
            }

        }
    }


    private void loadGraphics(){
        emptyTile = new Texture("emptyTile.jpg");
        bombTile = new Texture("bomb.jpg");
        emptyFloorTile = new Texture("empty floor.jpg");
        flagTile = new Texture("flagTile.jpg");
        oneTile = new Texture("oneTile.jpg");
        twoTile = new Texture("twoTile.jpg");
        threeTile = new Texture("threeTile.jpg");
        fourTile = new Texture("fourTile.jpg");
        fiveTile = new Texture("fiveTile.jpg");
        sixTile = new Texture("sixTile.jpg");
        sevenTile = new Texture("sevenTile.jpg");
        eightTile = new Texture("eightTile.jpg");

    }
    private boolean isValid(Location loc) {
        boolean notTooBig = loc.getRow() < board.length && loc.getCol() < board[0].length;
        boolean notTooSmall = loc.getRow() >=0 && loc.getCol() >=0;
        return notTooBig && notTooSmall;
    }
    private ArrayList<Location> getNeighbors(Location loc) {
        ArrayList<Location> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) { // Exclude the center location
                    Location neighbor = new Location(loc.getRow() + i, loc.getCol() + j);
                    if (isValid(neighbor)) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;

    }

    private int countBomb(ArrayList<Location> neibors){
        int count = 0;
        for(Location l : neibors){
            if (board[l.getRow()][l.getCol()] == BOMB){
                count+=1;
            }
        }
        return count;
    }
    private void numberBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != BOMB){
                    board [i][j] = countBomb(getNeighbors(new Location(i,j)));
                }
            }
        }
    }
    public void draw(SpriteBatch spriteBatch) {
        int xOffset = 300;
        int yOffset = 550;
        int size = 25;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Texture tile = getTileTexture(board[i][j]);
                spriteBatch.draw(tile, xOffset + size * j, yOffset - size * i);
            }
        }
    }

    private Texture getTileTexture(int value) {
        switch (value) {
            case 1:
                return oneTile;
            case 2:
                return twoTile;
            case 3:
                return threeTile;
            case 4:
                return fourTile;
            case 5:
                return fiveTile;
            case 6:
                return sixTile;
            case 7:
                return sevenTile;
            case 8:
                return eightTile;
            case BOMB:
                return bombTile;
            case 10:
                return emptyFloorTile;
            case 19:
                return flagTile;
            default:
                return emptyTile;
        }
    }

}
