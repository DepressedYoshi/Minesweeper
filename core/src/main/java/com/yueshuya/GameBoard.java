package com.yueshuya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class GameBoard {
    private int[][] board; // data structure as described in the notes
    private int numBombs; // number of bombs in the grid
    private int numFlags; // the number of flags that sill have to be placed by the player
    public static final int BOMB = -1; // help with readability
    private GameplayScreen gameplayScreen;
    private ArrayList<Location> bombSite = new ArrayList<>();
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
    private final int XOFFSET = 300;
    private final int YOFFSET = 550;
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
    public int getNumBombs() {
        return numBombs;
    }
    public int getNumFlags() {
        return numFlags;
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
                bombSite.add(new Location(rowRandom,colRandom));
                placed++;
            }

        }
    }
    //pre-condition: numFlag is zero
    public boolean playerWon() {
        boolean ans = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] < 10){
                    ans = false;
                    break;
                }
            }
        }

        return ans;
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
    /*
     * Given a (mouse_x, mouse_y) position, return the Location on
     * the board (row, col).
     * Return null if Location is not valid
     */
    public Location getTileAt(int mouse_x, int mouse_y) {
        //todo fix size adjusting error
        int col = (mouse_x-XOFFSET)/25;
        int row =( board.length -1 ) - (YOFFSET - mouse_y) / 25;
        Location location = new Location(row,col);
        if (isValid(location)){
            return location;
        }else
            return null;
    }
    public void leftClick(int x, int y) {
        Location clickLoc = getTileAt(x, y);
        // If the click is outside the board, do nothing
        if (clickLoc == null) {return;}
        int tileValue = board[clickLoc.getRow()][clickLoc.getCol()];
        // Handle click on a bomb (game over condition can be handled here)
        if (tileValue == BOMB) {
            gameOver();
            return;
        }
        // If we uncover an empty floor, initiate the recursive uncover method
        if (tileValue == 0) {
            uncoverArea(clickLoc);
        } else if (tileValue < 9){
            // Otherwise, just uncover the clicked tile
            board[clickLoc.getRow()][clickLoc.getCol()] += 10; // Assuming 10 represents uncovered
        }
    }
    public void rigtClick(int x, int y) {
        Location target = getTileAt(x,y);
        if(board[target.getRow()][target.getCol()] < 9 ){
            board[target.getRow()][target.getCol()] += 20;
            numFlags --;
        } else if (board[target.getRow()][target.getCol()] > 18) {
            board[target.getRow()][target.getCol()] -= 20;
            numFlags++;
        }

    }
    //recursive method that "propogates" through the board uncovering an area of empty tiles, and
//the surrounding numbers
    private void uncoverArea(Location loc) {
        int row = loc.getRow();
        int col = loc.getCol();
        int tileVal = board[row][col];
        if (!isValid(loc) || tileVal >= 10) {
            return;
        }
        if (tileVal < 9 && tileVal != BOMB)
            board[row][col] += 10;
        if (countBomb(getNeighbors(loc)) > 0) {
            return;
        }
        // Recurse into all neighboring tiles
        for (Location neighbor : getNeighbors(loc)) {
            uncoverArea(neighbor);
        }
    }
    public void showAllBombs() {
        for (Location l : bombSite){
            board[l.getRow()][l.getCol()] = 9;
        }
    }
    public void gameOver(){
        gameplayScreen.setGameOver(true);
        bombSite.clear();
        showAllBombs();
    }
    public void draw(SpriteBatch spriteBatch) {

        int size = 25;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Texture tile = getTileTexture(board[i][j]);
                spriteBatch.draw(tile, XOFFSET + size * j, YOFFSET - size * i);
            }
        }
    }
    private Texture getTileTexture(int value) {
        switch (value) {
            case 11:
                return oneTile;
            case 12:
                return twoTile;
            case 13:
                return threeTile;
            case 14:
                return fourTile;
            case 15:
                return fiveTile;
            case 16:
                return sixTile;
            case 17:
                return sevenTile;
            case 18:
                return eightTile;
            case 9:
                return bombTile;
            case BOMB:
                return bombTile;
            case 10:
                return emptyFloorTile;
            default:
                if (value > 18)
                    return flagTile;
                else
                    return emptyTile;
        }
    }
}
