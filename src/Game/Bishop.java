package Game;

import Move.*;

import java.util.ArrayList;

public class Bishop extends Piece{

    private static final int[][] whiteBishopPST = {
            {-33,  -3, -14, -21, -13, -12, -39, -21},
            {4,  15,  16,   0,   7,  21,  33,   1},
            {0,  15,  15,  15,  14,  27,  18,  10},
            {-6,  13,  13,  26,  34,  12,  10,   4},
            {-4,   5,  19,  50,  37,  37,   7,  -2},
            {-16,  37,  43,  40,  35,  50,  37,  -2},
            {-26,  16, -18, -13,  30,  59,  18, -47},
            {-29,   4, -82, -37, -25, -42,   7,  -8},
    };
    private static final int[][] blackBishopPST = {
            {-29,   4, -82, -37, -25, -42,   7,  -8},
            {-26,  16, -18, -13,  30,  59,  18, -47},
            {-16,  37,  43,  40,  35,  50,  37,  -2},
            {-4,   5,  19,  50,  37,  37,   7,  -2},
            {-6,  13,  13,  26,  34,  12,  10,   4},
            {0,  15,  15,  15,  14,  27,  18,  10},
            {4,  15,  16,   0,   7,  21,  33,   1},
            {-33,  -3, -14, -21, -13, -12, -39, -21},
    };





    public Bishop(MovesGenerator mg, char c) {
        super("bishop", c);
        this.mg = mg;
    }

    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char  color = game.getActiveColor();
            // 4 Diagonals:

            int x = i;
            int y = j;

            // Top Right
            while (x < 7 && y < 7) {
                x++;
                y++;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks bishop's way
                    if (board[x][y].isEnemy(color)) {
                        String enemyName = board[x][y].getName();
                        Move move = new Capture(i,j,x,y, board[x][y].clone());
                        if (mg.checkIfMoveIsValid(game, move)) {
                            moves.add(move);
                        }
                        break; // break as there is a piece blocking
                    } else {
                        break; // block as there is a friendly piece blocking
                    }
                }
            }

            // Top Left
            x = i;
            y = j;
            while (x > 0 && y < 7) {
                x--;
                y++;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks bishop's way
                    if (board[x][y].isEnemy(color)) {
                        String enemyName = board[x][y].getName();
                        Move move = new Capture(i,j,x,y, board[x][y].clone());
                        if (mg.checkIfMoveIsValid(game, move)) {
                            moves.add(move);
                        }
                        break; // break as there is a piece blocking
                    } else {
                        break; // block as there is a friendly piece blocking
                    }
                }
            }
            // Bottom Right
            x = i;
            y = j;
            while (x < 7 && y > 0) {
                x++;
                y--;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks bishop's way
                    if (board[x][y].isEnemy(color)) {
                        String enemyName = board[x][y].getName();
                        Move move = new Capture(i,j,x,y, board[x][y].clone());
                        if (mg.checkIfMoveIsValid(game, move)) {
                            moves.add(move);
                        }
                        break; // break as there is a piece blocking
                    } else {
                        break; // block as there is a friendly piece blocking
                    }
                }
            }

            // Bottom Left
            x = i;
            y = j;
            while (x > 0 && y > 0) {
                x--;
                y--;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks bishop's way
                    if (board[x][y].isEnemy(color)) {
                        String enemyName = board[x][y].getName();
                        Move move = new Capture(i,j,x,y, board[x][y].clone());
                        if (mg.checkIfMoveIsValid(game, move)) {
                            moves.add(move);
                        }
                        break; // break as there is a piece blocking
                    } else {
                        break; // block as there is a friendly piece blocking
                    }
                }
            }
         // End case bishop

        return moves;
    }

    @Override
    public Bishop clone() {
        return (Bishop) super.clone();
    }

    @Override
    public int getPSTValue(int i, int j) {
        if (this.getColor() == 'w') {
            return whiteBishopPST[i][j];
        } else {
            return blackBishopPST[i][j];
        }
    }


}
