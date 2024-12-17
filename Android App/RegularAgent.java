package com.babila.tic_tac_toeapp;

public class RegularAgent {
    private static final int EMPTY = -1;  // מייצג תא ריק
    private static final boolean IS_CIRCLE = true;  // הסוכן תמיד משחק כעיגול

    public int[] getNextMove(int[][] board) {
        int[] bestMove = {-1, -1};
        int bestScore = Integer.MIN_VALUE;

        // בדיקה על כל תא בלוח
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY) {
                    // ביצוע הצעד
                    board[row][col] = 1; // עיגול - מספר אי זוגי

                    // חישוב ניקוד באמצעות מינימקס
                    int score = minimax(board, 0, false, 8);

                    // ביטול הצעד
                    board[row][col] = EMPTY;

                    // עדכון הצעד הטוב ביותר
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing, int remainingDepth) {
        // בדיקה אם המשחק נגמר או אם העומק המקסימלי הושג
        Integer result = evaluate(board);
        if (result != null || depth == remainingDepth) {
            return result != null ? result : 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = 1; // עיגול - מספר אי זוגי
                        bestScore = Math.max(bestScore, minimax(board, depth + 1, false, remainingDepth));
                        board[row][col] = EMPTY; // ביטול הצעד
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = 2; // איקס - מספר זוגי
                        bestScore = Math.min(bestScore, minimax(board, depth + 1, true, remainingDepth));
                        board[row][col] = EMPTY; // ביטול הצעד
                    }
                }
            }
            return bestScore;
        }
    }

    private Integer evaluate(int[][] board) {
        // בדיקה לניצחון
        for (int i = 0; i < 3; i++) {
            // שורות
            if (board[i][0] != EMPTY && board[i][0] % 2 == board[i][1] % 2 && board[i][1] % 2 == board[i][2] % 2) {
                return (board[i][0] % 2 == 1) ? 10 : -10;
            }
            // עמודות
            if (board[0][i] != EMPTY && board[0][i] % 2 == board[1][i] % 2 && board[1][i] % 2 == board[2][i] % 2) {
                return (board[0][i] % 2 == 1) ? 10 : -10;
            }
        }
        // אלכסונים
        if (board[0][0] != EMPTY && board[0][0] % 2 == board[1][1] % 2 && board[1][1] % 2 == board[2][2] % 2) {
            return (board[0][0] % 2 == 1) ? 10 : -10;
        }
        if (board[0][2] != EMPTY && board[0][2] % 2 == board[1][1] % 2 && board[1][1] % 2 == board[2][0] % 2) {
            return (board[0][2] % 2 == 1) ? 10 : -10;
        }

        // בדיקה אם יש תאים ריקים
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == EMPTY) {
                    return null; // המשחק ממשיך
                }
            }
        }

        return 0; // תיקו
    }
}

