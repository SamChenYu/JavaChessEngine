package Game;

import Move.*;

import java.util.ArrayList;

public class Bishop extends Piece{

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
    public boolean isAttackingKing(Game game, int i, int j, int kingX, int kingY) {

        Piece[][] board = game.getBoard();
        // 4 Diagonals:

        int x = i;
        int y = j;

        // Top Right
        while(x < 7 && y < 7) {
            x++;
            y++;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks


            }
        }

        // Top Left
        x = i;
        y = j;
        while(x > 0 && y < 7) {
            x--;
            y++;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }
        // Bottom Right
        x = i;
        y = j;
        while(x < 7 && y > 0) {
            x++;
            y--;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }

        // Bottom Left
        x = i;
        y = j;
        while(x > 0 && y > 0) {
            x--;
            y--;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }
        return false;
    }


}