package com.yueshuya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class GameBoard {
    private int[][] board;
    private int numBombs;
    private int numFlags;
    public static final int BOMB = -1;
    private GameplayScreen gameplayScreen;
    private MusicManager musicManager;
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
    private static final int TILE_SIZE = 25;
    private static final int XOFFSET = 265;
    private static final int YOFFSET = 560;

    public GameBoard(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        musicManager = new MusicManager(gameplayScreen);
        board = new int[16][30];
        numBombs = 50;
        this.numFlags = numBombs;
        init();
    }

    private void init() {
        loadGraphics();
        placeBomb();
        numberBoard();
    }

    public int getNumBombs() {
        return numBombs;
    }

    public int getNumFlags() {
        return numFlags;
    }

    private void loadGraphics() {
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

    private void placeBomb() {
        int placed = 0;
        while (placed < numBombs) {
            int rowRandom = (int) (Math.random() * board.length);
            int colRandom = (int) (Math.random() * board[0].length);
            if (board[rowRandom][colRandom] != BOMB) {
                board[rowRandom][colRandom] = BOMB;
                bombSite.add(new Location(rowRandom, colRandom));
                placed++;
            }
        }
    }
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

    private void numberBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != BOMB) {
                    board[i][j] = countBomb(getNeighbors(new Location(i, j)));
                }
            }
        }
    }

    public Location getTileAt(int mouseX, int mouseY) {
        int col = (mouseX - XOFFSET) / TILE_SIZE;
        int row = (YOFFSET - mouseY) / TILE_SIZE;
        Location location = new Location(row, col);
        if (isValid(location)) {
            return location;
        } else {
            return null;
        }
    }

    private boolean isValid(Location loc) {
        return loc.getRow() >= 0 && loc.getRow() < board.length && loc.getCol() >= 0 && loc.getCol() < board[0].length;
    }

    private ArrayList<Location> getNeighbors(Location loc) {
        ArrayList<Location> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    Location neighbor = new Location(loc.getRow() + i, loc.getCol() + j);
                    if (isValid(neighbor)) {
                        neighbors.add(neighbor);
                    }
                }
            }
        }
        return neighbors;
    }

    private int countBomb(ArrayList<Location> neighbors) {
        int count = 0;
        for (Location l : neighbors) {
            if (board[l.getRow()][l.getCol()] == BOMB) {
                count++;
            }
        }
        return count;
    }

    public void leftClick(int x, int y) {
        Location clickLoc = getTileAt(x, y);
        if (clickLoc == null) {
            return;
        }
        int tileValue = board[clickLoc.getRow()][clickLoc.getCol()];
        if (tileValue == BOMB) {
            gameOver();
        } else if (tileValue == 0) {
            musicManager.playclickSound();
            uncoverArea(clickLoc);
        } else if (tileValue < 9) {
            musicManager.playclickSound();
            board[clickLoc.getRow()][clickLoc.getCol()] += 10;  // Mark the tile as uncovered
        }
    }

    public void rightClick(int x, int y) {
        Location target = getTileAt(x, y);
        if (target == null) {
            return;
        }
        int tileValue = board[target.getRow()][target.getCol()];
        if (tileValue < 9) {
            musicManager.playFlagOn(true);
            board[target.getRow()][target.getCol()] += 20;  // Flag the tile
            numFlags--;
        } else if (tileValue >18) {
            musicManager.playFlagOn(false);
            board[target.getRow()][target.getCol()] -= 20;  // Unflag the tile
            numFlags++;
        }
    }

    private void uncoverArea(Location loc) {
        if (!isValid(loc) || board[loc.getRow()][loc.getCol()] >= 10) {
            return;
        }
        if (board[loc.getRow()][loc.getCol()] < 9 && board[loc.getRow()][loc.getCol()] != BOMB) {
            board[loc.getRow()][loc.getCol()] += 10;  // Mark the tile as uncovered
        }
        if (countBomb(getNeighbors(loc)) > 0) {
            return;
        }
        for (Location neighbor : getNeighbors(loc)) {
            uncoverArea(neighbor);
        }
    }

    public void showAllBombs() {
        for (Location loc : bombSite) {
            board[loc.getRow()][loc.getCol()] = 9;  // Reveal all bombs
        }
    }

    public void gameOver() {
        musicManager.playGameOver();
        gameplayScreen.setGameOver(true);
        showAllBombs();
        bombSite.clear();
    }

    public void draw(SpriteBatch spriteBatch) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Texture tile = getTileTexture(board[i][j]);
                spriteBatch.draw(tile, XOFFSET + TILE_SIZE * j, YOFFSET - TILE_SIZE * (i + 1));
            }
        }
    }

    private Texture getTileTexture(int value) {
        switch (value) {
            case 11: return oneTile;
            case 12: return twoTile;
            case 13: return threeTile;
            case 14: return fourTile;
            case 15: return fiveTile;
            case 16: return sixTile;
            case 17: return sevenTile;
            case 18: return eightTile;
            case 9: return bombTile;
            case 10: return emptyFloorTile;
            default:
                if (value > 18) {
                    return flagTile;
                } else {
                    return emptyTile;
                }
        }
    }

    public void winSound() {
        musicManager.playWin();
    }

    public void dispose() {
        musicManager.dispose();
        emptyTile.dispose();
        bombTile.dispose();
        emptyFloorTile.dispose();
        flagTile.dispose();
        oneTile.dispose();
        twoTile.dispose();
        threeTile.dispose();
        fourTile.dispose();
        fiveTile.dispose();
        sixTile.dispose();
        sevenTile.dispose();
        eightTile.dispose();
    }
}

