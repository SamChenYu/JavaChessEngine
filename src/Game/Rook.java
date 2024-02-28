package Game;

import Move.*;

import java.util.ArrayList;

public class Rook extends Piece {

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
    public boolean isAttackingKing(Game game, int i, int j, int kingX, int kingY) {
        // 4 DIRECTIONS:
        // POSITIVE X
        Piece[][] board = game.getBoard();

        int x = i;
        int y = j;
        while(x < 7) {
            x++;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }
        // NEGATIVE X
        x = i;
        y = j;
        while(x > 0) {
            x--;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }

        // POSITIVE Y
        x = i;
        y = j;
        while(y < 7) {
            y++;
            if(!board[x][y].isEmpty()) {
                if(x == kingX && y == kingY) {
                    return true;
                }
                break; // break as piece blocks
            }
        }
        // NEGATIVE Y
        x = i;
        y = j;
        while(y > 0) {
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
