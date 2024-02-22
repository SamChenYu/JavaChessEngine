package Game;

import Move.*;
import java.util.ArrayList;

public class Knight extends Piece {

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

}
