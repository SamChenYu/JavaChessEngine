package Game;

import Move.*;

import java.util.ArrayList;

public class Queen extends Piece{


    private static final int[][]  whiteQueenPST= {
            {-1, -18,  -9,  10, -15, -25, -31, -50},
            {-35,  -8,  11,   2,   8,  15,  -3,   1},
            {-14,   2, -11,  -2,  -5,   2,  14,   5},
            {-9, -26,  -9, -10,  -2,  -4,   3,  -3},
            {-27, -27, -16, -16,  -1,  17,  -2,   1},
            {-13, -17,   7,   8,  29,  56,  47,  57},
            {-24, -39,  -5,   1, -16,  57,  28,  54},
            {-28,   0,  29,  12,  59,  44,  43,  45},
    };
    private static final int[][]  blackQueenPST= {
            {-28,   0,  29,  12,  59,  44,  43,  45},
            {-24, -39,  -5,   1, -16,  57,  28,  54},
            {-13, -17,   7,   8,  29,  56,  47,  57},
            {-27, -27, -16, -16,  -1,  17,  -2,   1},
            {-9, -26,  -9, -10,  -2,  -4,   3,  -3},
            {-14,   2, -11,  -2,  -5,   2,  14,   5},
            {-35,  -8,  11,   2,   8,  15,  -3,   1},
            {-1, -18,  -9,  10, -15, -25, -31, -50},
    };



    public Queen(MovesGenerator mg, char c) {
        super("queen", c);
        this.mg = mg;

    }


    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char color = game.getActiveColor();



            // 4 AXIS:
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

            // 4 Diagonals:

            x = i;
            y = j;

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

        return moves;
    }


    @Override
    public Queen clone() {
        return (Queen) super.clone();
    }

    @Override
    public int getPSTValue(int i, int j) {
        if (this.getColor() == 'w') {
            return whiteQueenPST[i][j];
        } else {
            return blackQueenPST[i][j];
        }
    }


}
