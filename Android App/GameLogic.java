package com.babila.tic_tac_toeapp;

public class GameLogic {

    private int[][] gameBoard;
    private boolean gameFlag;

    GameLogic(){
        gameBoard = new int[3][3];
        for(int r=0; r<3; r++){
            for(int c=0; c<3; c++){
                gameBoard[r][c] = -1;
            }
        }
    }

    public void setGameFlag(boolean flag){
        gameFlag = flag;
    }

    public boolean updateGameBoard(int row, int col, int turn){
        if(gameBoard[row][col] == -1){
            gameBoard[row][col] = turn;

            if(gameFlag){
                for(int r=0; r<3; r++){
                    for(int c=0; c<3; c++){
                        if(gameBoard[r][c] == turn-6)
                            gameBoard[r][c] = -1;
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public int checkWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i][0] != -1 && gameBoard[i][0]%2 == gameBoard[i][1]%2 && gameBoard[i][1]%2 == gameBoard[i][2]%2) {
                return gameBoard[i][0];  // Return winner (even for X, odd for O)
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (gameBoard[0][j] != -1 && gameBoard[0][j]%2 == gameBoard[1][j]%2 && gameBoard[1][j]%2 == gameBoard[2][j]%2) {
                return gameBoard[0][j];  // Return winner
            }
        }

        // Check diagonals
        if (gameBoard[0][0] != -1 && gameBoard[0][0]%2 == gameBoard[1][1]%2 && gameBoard[1][1]%2 == gameBoard[2][2]%2) {
            return gameBoard[0][0];  // Return winner
        }
        if (gameBoard[0][2] != -1 && gameBoard[0][2]%2 == gameBoard[1][1]%2 && gameBoard[1][1]%2 == gameBoard[2][0]%2) {
            return gameBoard[0][2];  // Return winner
        }

        // No winner found
        return -1;
    }

    public int[][] getGameBoard(){
        return gameBoard;
    }

    public void resetBoard(){
        for(int r=0; r<3; r++){
            for(int c=0; c<3; c++){
                gameBoard[r][c] = -1;
            }
        }
    }

    public boolean boardIsFull(){
        for(int row=0; row<3; row++){
            for(int col=0; col<3; col++){
                if(gameBoard[row][col] == -1)
                    return false;
            }
        }
        return true;
    }

}
