package Game;

import Move.*;

import java.util.ArrayList;

public class Queen extends Piece{

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
    public boolean isAttackingKing(Game game, int i, int j, int kingX, int kingY) {
        // 4 Diagonals:
        Piece[][] board = game.getBoard();
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

        // 4 DIRECTIONS:
        // POSITIVE X
        x = i;
        y = j;
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
