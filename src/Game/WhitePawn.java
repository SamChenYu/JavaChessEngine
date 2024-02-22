package Game;
import Move.*;

import java.util.ArrayList;

public class WhitePawn extends Piece{
    
    public WhitePawn(MovesGenerator mg) {
        super("pawn", 'w');
        this.mg = mg;
    }


    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();

            Piece[][] board = game.getBoard();
            char color = game.getActiveColor();
            int enPassantX = game.getEnPassantX();
            int enPassantY = game.getEnPassantY();

            // Forward one
            if (j < 6 /*promote is a different check*/) {
                int x = i, y = j + 1; // coords interested in
                if (board[x][y].isEmpty()) {
                    // Can move forward one
                    Move move = new Move(i, j, x, y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }
            // Forward two if on the starting position
            if (j < 6 && j == 1 /*promote is a different check*/) {
                int x = i, y = j + 2; // coords interested in
                if (board[i][j + 1].isEmpty() && board[x][y].isEmpty()) {
                    // Can move forward two
                    Move move = new MovedTwice(i,j,x,y, i, (j + y) / 2);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
            }

            // Attacking to the left (A pawn cannot attack left)
            // Includes if it can attack the en passant square
            if (i > 0 && j < 6 /*Promote is a different case*/) {
                int x = i - 1, y = j + 1; // coords interested in
                if (board[x][y].isEnemy(color)) {
                    // Can attack left
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i,j,x,y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }

                if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {
                    Move move = new EnPassantCapture(i, j, x, y, x, y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);

                    }
                }

            }

            //Attacking to the right (H pawn cannot attack right)
            // Includes if it can attack the en passant
            if (i < 7 && j < 6) {
                int x = i + 1, y = j + 1; // coords interested in
                if (board[x][y].isEnemy(color)) {
                    String enemyName = board[x][y].getName();
                    Move move = new Capture(i, j, x, y, board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    }
                }
                if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {

                    Move move = new EnPassantCapture(i, j, x, y,  x, y);
                    if (mg.checkIfMoveIsValid(game, move)) {
                        moves.add(move);
                    } else {
                    }
                }
            }

            // Promote via moving
            if (j == 6) {
                int x = i, y = j + 1; // coords interested in
                if (board[x][y].isEmpty()) {
                    // Can promote to queen, rook, bishop, knight
                    Move move1 = new Promote(i, j, x, y, new Queen(mg, 'w'));
                    Move move2 = new Promote(i, j, x, y, new Rook(mg, 'w'));
                    Move move3 = new Promote(i, j, x, y, new Bishop(mg, 'w'));
                    Move move4 = new Promote(i, j, x, y, new Knight(mg, 'w'));

                    if (mg.checkIfMoveIsValid(game, move1)) {
                        moves.add(move1);
                    }
                    if (mg.checkIfMoveIsValid(game, move2)) {
                        moves.add(move2);
                    }
                    if (mg.checkIfMoveIsValid(game, move3)) {
                        moves.add(move3);
                    }
                    if (mg.checkIfMoveIsValid(game, move4)) {
                        moves.add(move4);
                    }

                }
            }

            // Promote via capturing to the left (A pawn cannot attack left)
            if (i > 0 && j == 6) {
                int x = i - 1, y = j + 1; // coords interested in
                if (board[x][y].isEnemy(color)) {
                    // Can attack left
                    Move move1 = new PromoteCapture(i, j, x, y, new Queen(mg, color), board[x][y].clone());
                    Move move2 = new PromoteCapture(i, j, x, y, new Rook(mg, color), board[x][y].clone());
                    Move move3 = new PromoteCapture(i, j, x, y, new Bishop(mg, color), board[x][y].clone());
                    Move move4 = new PromoteCapture(i, j, x, y, new Knight(mg, color), board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move1)) {
                        moves.add(move1);
                    }
                    if (mg.checkIfMoveIsValid(game, move2)) {
                        moves.add(move2);
                    }
                    if (mg.checkIfMoveIsValid(game, move3)) {
                        moves.add(move3);
                    }
                    if (mg.checkIfMoveIsValid(game, move4)) {
                        moves.add(move4);
                    }
                }
            }

            // Promote via capturing to the right (H pawn cannot attack right)
            //Attacking to the right (H pawn cannot attack right)
            if (i < 7 && j == 6) {
                int x = i + 1, y = j + 1; // coords interested in
                if (board[x][y].isEnemy(color)) {
                    // Can attack right
                    Move move1 = new PromoteCapture(i, j, x, y, new Queen(mg, color), board[x][y].clone());
                    Move move2 = new PromoteCapture(i, j, x, y, new Rook(mg, color), board[x][y].clone());
                    Move move3 = new PromoteCapture(i, j, x, y, new Bishop(mg, color), board[x][y].clone());
                    Move move4 = new PromoteCapture(i, j, x, y, new Knight(mg, color), board[x][y].clone());
                    if (mg.checkIfMoveIsValid(game, move1)) {
                        moves.add(move1);
                    }
                    if (mg.checkIfMoveIsValid(game, move2)) {
                        moves.add(move2);
                    }
                    if (mg.checkIfMoveIsValid(game, move3)) {
                        moves.add(move3);
                    }
                    if (mg.checkIfMoveIsValid(game, move4)) {
                        moves.add(move4);
                    }
                }
            }

            // END White Pawn's code



        return moves;
    }

    @Override
    public WhitePawn clone() {
        return (WhitePawn) super.clone();
    }


}
