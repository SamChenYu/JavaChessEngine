package Game;

import Move.*;

import java.util.ArrayList;

public class King extends Piece {

    public King(MovesGenerator mg, char c) {
        super("king", c);
        this.mg = mg;
    }

    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char color = game.getActiveColor();
        boolean whiteCastleKingSide = game.getWhiteCastleKingSide();
        boolean whiteCastleQueenSide = game.getWhiteCastleQueenSide();
        boolean blackCastleKingSide = game.getBlackCastleKingSide();
        boolean blackCastleQueenSide = game.getBlackCastleQueenSide();


            int x = i;
            int y = j;

            // 4 horizontal axis
            x++;
            if (x <= 7) {


                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }


            }

            x = i;
            y = j;
            x--;
            if (x >= 0 && (board[x][y].isEmpty() || board[x][y].isEnemy(color))) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }

            x = i;
            y = j;
            y++;
            if (y <= 7) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }

            x = i;
            y = j;
            y--;
            if (y >= 0) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }

            // Diagonals

            x = i;
            y = j;

            // TOP RIGHT
            x++;
            y++;
            if (x <= 7 && y <= 7) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }

            // TOP LEFT
            x = i;
            y = j;
            x--;
            y++;
            if (x >= 0 && y <= 7) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }

            // Bottom RIGHT
            x = i;
            y = j;
            x++;
            y--;
            if ((x <= 7 && y >= 0)) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }
            // Bottom LEFT
            x = i;
            y = j;
            x--;
            y--;
            if (x >= 0 && y >= 0) {
                if (board[x][y].isEmpty()) {
                    Move move = new Move(i,j,x,y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

            }
            // CASTLING RIGHTS
            if (color == 'w') {

                if (whiteCastleKingSide) {
                    if (board[5][0].isEmpty() && board[6][0].isEmpty()) {

                        if (!mg.isSquareInCheck(game, 4, 0) && !mg.isSquareInCheck(game, 5, 0) &&
                                !mg.isSquareInCheck(game, 6, 0)) {
                            Move move = new CastleKingSide(4,0,6,0);
                            moves.add(move);
                        }

                    }
                }
                if (whiteCastleQueenSide) {
                    if (board[3][0].isEmpty() && board[2][0].isEmpty() && board[1][0].isEmpty()) {
                        if (!mg.isSquareInCheck(game, 1, 0) &&
                                !mg.isSquareInCheck(game, 2, 0) && !mg.isSquareInCheck(game, 3, 0) && !mg.isSquareInCheck(game, 4, 0)) {
                            Move move = new CastleQueenSide(4,0,2,0);
                            moves.add(move);
                        }
                    }
                }
            } else {

                if (blackCastleKingSide) {
                    if (board[5][7].isEmpty() && board[6][7].isEmpty()) {
                        if (!mg.isSquareInCheck(game, 4, 7) && !mg.isSquareInCheck(game, 5, 7) &&
                                !mg.isSquareInCheck(game, 6, 7)) {
                            Move move = new CastleKingSide(4,7,6, 7);
                            moves.add(move);
                        }
                    }
                }
                if (blackCastleQueenSide) {
                    if (board[3][7].isEmpty() && board[2][7].isEmpty() && board[1][7].isEmpty()) {
                        if (!mg.isSquareInCheck(game, 1, 7) &&
                                !mg.isSquareInCheck(game, 2, 7) && !mg.isSquareInCheck(game, 3, 7) && !mg.isSquareInCheck(game, 4, 7)) {
                            Move move = new CastleQueenSide(4,7,2,7);
                            moves.add(move);
                        }
                    }
                }
            }

        return moves;
    }

    @Override
    public King clone() {
        return (King) super.clone();
    }

}

