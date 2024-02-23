package Game;

import Move.*;
import java.util.ArrayList;

public class Knight extends Piece {

    private static final int[][] whiteKnightPST = {
            {-105, -21, -58, -33, -17, -28, -19,  -23},
            {-29, -53, -12,  -3,  -1,  18, -14,  -19},
            {-23,  -9,  12,  10,  19,  17,  25,  -16},
            {-13,   4,  16,  13,  28,  19,  21,   -8},
            {-9,  17,  19,  53,  37,  69,  18,   22},
            {-47,  60,  37,  65,  84, 129,  73,   44},
            {-73, -41,  72,  36,  23,  62,   7,  -17},
            {-167, -89, -34, -49,  61, -97, -15, -107},
    };
    private static final int[][] blackKnightPST = {
            {-167, -89, -34, -49,  61, -97, -15, -107},
            {-73, -41,  72,  36,  23,  62,   7,  -17},
            {-47,  60,  37,  65,  84, 129,  73,   44},
            {-9,  17,  19,  53,  37,  69,  18,   22},
            {-13,   4,  16,  13,  28,  19,  21,   -8},
            {-23,  -9,  12,  10,  19,  17,  25,  -16},
            {-29, -53, -12,  -3,  -1,  18, -14,  -19},
            {-105, -21, -58, -33, -17, -28, -19,  -23},
    };


    private static final int[][] blackAltPST = {
            {-10, -10, -10, -10, -10, -10, -10, -10},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10, -30, -10, -10, -10, -10, -30, -10}
    };

    private static final int[][] whiteAltPST = {
            {-10, -30, -10, -10, -10, -10, -30, -10},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10, -10, -10, -10, -10, -10, -10, -10}
    };




    public Knight(MovesGenerator mg, char c) {
        super("knight", c);
        this.mg = mg;
    }
    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char color = game.getActiveColor();

            // Knight has 8 possible attacks


            int x = i;
            int y = j;

            // TWO UP (LEFT / RIGHT)

            // Right
            x++;
            y += 2;
            if (x <= 7 && y <= 7) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());

                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }


            // Left
            x = i;
            y = j;
            x--;
            y += 2;
            if (x >= 0 && y <= 7) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }


            // TWO DOWN (LEFT / RIGHT)
            x = i;
            y = j;
            // Right
            x++;
            y -= 2;
            if (x <= 7 && y >= 0) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }

            // Left
            x = i;
            y = j;
            x--;
            y -= 2;
            if (x >= 0 && y >= 0) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }

            // TWO RIGHT (UP/DOWN)
            x = i;
            y = j;
            // up
            x += 2;
            y++;
            if (x <= 7 && y <= 7) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }

            // down
            x = i;
            y = j;
            x += 2;
            y--;
            if (x <= 7 && y >= 0) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }
            // TWO LEFT (UP/DOWN)
            x = i;
            y = j;
            // up
            x -= 2;
            y++;
            if (x >= 0 && y <= 7) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }

            // down
            x = i;
            y = j;
            x -= 2;
            y--;
            if (x >= 0 && y >= 0) {

                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }
        return moves;
    }

    @Override
    public Knight clone() {
        return (Knight) super.clone();
    }


    @Override
    public int getPSTValue(int i, int j) {
        if (this.getColor() == 'w') {
            return whiteKnightPST[i][j];
        } else {
            return blackKnightPST[i][j];
        }
    }

}
