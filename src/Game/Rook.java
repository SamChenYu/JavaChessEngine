package Game;

import Move.*;

import java.util.ArrayList;

public class Rook extends Piece {


    private static final int[][] whiteRookPST = {
            {-19, -13,   1,  17, 16,  7, -37, -26},
            {-44, -16, -20,  -9, -1, 11,  -6, -71},
            {-45, -25, -16, -17,  3,  0,  -5, -33},
            {-36, -26, -12,  -1,  9, -7,   6, -23},
            {-24, -11,   7,  26, 24, 35,  -8, -20},
            {-5,  19,  26,  36, 17, 45,  61,  16},
            {27,  32,  58,  62, 80, 67,  26,  44},
            {32,  42,  32,  51, 63,  9,  31,  43},
    };
    private static final int[][] blackRookPST = {
            {32,  42,  32,  51, 63,  9,  31,  43},
            {27,  32,  58,  62, 80, 67,  26,  44},
            {-5,  19,  26,  36, 17, 45,  61,  16},
            {-24, -11,   7,  26, 24, 35,  -8, -20},
            {-36, -26, -12,  -1,  9, -7,   6, -23},
            {-45, -25, -16, -17,  3,  0,  -5, -33},
            {-44, -16, -20,  -9, -1, 11,  -6, -71},
            {-19, -13,   1,  17, 16,  7, -37, -26}
    };

    public Rook(MovesGenerator mg, char c) {
        super("rook", c);
        this.mg = mg;
    }

    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char color = game.getActiveColor();


            // 4 DIRECTIONS:
            // POSITIVE X
            int x = i;
            int y = j;
            while (x < 7) {
                x++;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks rook's way
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
            // NEGATIVE X
            x = i;
            y = j;
            while (x > 0) {
                x--;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }

                } else {
                    // Piece blocks rook's way
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

            // POSITIVE Y
            x = i;
            y = j;
            while (y < 7) {
                y++;
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                } else {
                    // Piece blocks rook's way
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
            // NEGATIVE Y
            x = i;
            y = j;
            while (y > 0) {
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



        return moves;
    }

    @Override
    public Rook clone() {
        return (Rook) super.clone();
    }

    @Override
    public int getPSTValue(int i, int j) {
        if (this.getColor() == 'w') {
            return whiteRookPST[i][j];
        } else {
            return blackRookPST[i][j];
        }
    }
}
