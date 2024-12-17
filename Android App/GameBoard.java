package com.babila.tic_tac_toeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class GameBoard extends View {

    private final int boardColor;
    private final int xColor;
    private final int oColor;
    private final int winningLineColor;
    private final int aboutToDeleteColor;

    private Random random = new Random();

    private RegularAgent regularAgent = new RegularAgent();

    public boolean isGameOver = false;

    private final GameLogic game;

    private int winner = -1;

    private final int fromEdgeSize = 50;
    private final int fromCellSize = 100;

    private int cellSize = getWidth() / 3;

    private final Paint paint = new Paint();

    private int turn = 0;
    private int[][] board = new int[3][3];
    private int xPoints = 0;
    private int oPoints = 0;

    private boolean threeStepsGameFlag;

    private TextView playerTurn;
    private gamePage gamePage;

    private boolean opponentFlag;
    private boolean easyLevle;

    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0);

        try {
            boardColor = a.getInteger(R.styleable.GameBoard_boardColor, 0);
            xColor = a.getInteger(R.styleable.GameBoard_xColor, 0);
            oColor = a.getInteger(R.styleable.GameBoard_oColor, 0);
            winningLineColor = a.getInteger(R.styleable.GameBoard_winningLineColor, 0);
            aboutToDeleteColor = a.getInteger(R.styleable.GameBoard_aboutToDeleteColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight()) - (fromEdgeSize * 2);
        cellSize = dimension / 3;
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawMarkers(canvas);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            int row = (int) Math.ceil(y / cellSize) - 1;
            int col = (int) Math.ceil(x / cellSize) - 1;
            if (game.updateGameBoard(row, col, turn)) {
                turn++;
                if (turn % 2 == 0) {
                    playerTurn.setText("X");
                } else {
                    playerTurn.setText("O");
                }
                if (opponentFlag && !threeStepsGameFlag && turn < 9) {
                    //delay
                    winner = game.checkWinner();
                    if(winner == -1) {
                        new Handler().postDelayed(() -> {
                            opponentTurn();
                        }, 150);
                    }
                }

            }
            invalidate();
            return true;
        }
        return false;
    }

    private void opponentTurn(){
        //get data
        int[] move = regularAgent.getNextMove(game.getGameBoard());
        if((turn == 1) && easyLevle){
            do {
                move[0] = random.nextInt(3);
                move[1] = (random.nextInt(9) + 6) % 3;

            }while(game.getGameBoard()[move[0]][move[1]] != -1);
        }

        game.updateGameBoard(move[0], move[1], turn);
        turn++;
        if (turn % 2 == 0) {
            playerTurn.setText("X");
        } else {
            playerTurn.setText("O");
        }
        invalidate();
    }


    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardColor);
        paint.setStrokeWidth(16);

        for (int c = 1; c < 3; c++) {
            canvas.drawLine(cellSize * c, 0, cellSize * c, canvas.getHeight(), paint);
        }

        for (int r = 1; r < 3; r++) {
            canvas.drawLine(0, cellSize * r, canvas.getWidth(), cellSize * r, paint);
        }

    }

    private void drawMarkers(Canvas canvas) {
        int[][] gameBoard = game.getGameBoard();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (gameBoard[r][c] != -1) {
                    if (gameBoard[r][c] % 2 == 0) {
                        if (threeStepsGameFlag && gameBoard[r][c] == turn - 6)
                            drawX(canvas, r, c, true);
                        else
                            drawX(canvas, r, c, false);
                    } else {
                        if (threeStepsGameFlag && gameBoard[r][c] == turn - 6)
                            drawO(canvas, r, c, true);
                        else
                            drawO(canvas, r, c, false);
                    }
                }

            }
        }
        winner = game.checkWinner();
        if (winner != -1 && isGameOver == false) {
            if (winner % 2 == 0)
                xPoints++;
            else
                oPoints++;
            isGameOver = true;
            gamePage.showWinnerDialog(winner, xPoints, oPoints);
            resetGame();
            invalidate();
            playerTurn.setText("X");
        } else if (isGameOver == false && game.boardIsFull()) {
            isGameOver = true;
            gamePage.showWinnerDialog(winner, xPoints, oPoints);
            resetGame();
            invalidate();
            playerTurn.setText("X");
        }
    }

    private void drawX(Canvas canvas, int row, int col, boolean isWhite) {
        if (isWhite)
            paint.setColor(aboutToDeleteColor);
        else
            paint.setColor(xColor);
        paint.setStrokeWidth(35);
        canvas.drawLine((col + 1) * cellSize - fromCellSize,
                fromCellSize + row * cellSize,
                col * cellSize + fromCellSize,
                (row + 1) * cellSize - fromCellSize,
                paint);
        canvas.drawLine(col * cellSize + fromCellSize,
                row * cellSize + fromCellSize,
                (col + 1) * cellSize - fromCellSize,
                (row + 1) * cellSize - fromCellSize,
                paint);
    }

    private void drawO(Canvas canvas, int row, int col, boolean isWhite) {
        if (isWhite)
            paint.setColor(aboutToDeleteColor);
        else
            paint.setColor(oColor);
        paint.setStrokeWidth(35);
        canvas.drawOval(col * cellSize + fromCellSize,
                row * cellSize + fromCellSize,
                col * cellSize + cellSize - fromCellSize,
                row * cellSize + cellSize - fromCellSize,
                paint);
    }

    public void setUp(TextView playerTurn, boolean flag, boolean opponentFlag, boolean easyLevel, gamePage gamePage) {
        this.playerTurn = playerTurn;
        this.threeStepsGameFlag = flag;
        this.gamePage = gamePage;
        game.setGameFlag(threeStepsGameFlag);
        this.opponentFlag = opponentFlag;
        this.easyLevle = easyLevel;
    }

    public void resetGame() {
        turn = 0;
        winner = -1;
        game.resetBoard();
        isGameOver = false;
    }

}
