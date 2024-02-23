package Game;

import Move.*;

import java.util.ArrayList;

public class BlackPawn extends Piece {

    private static int[][] blackPawnPST = {
            { 0,   0,   0,   0,   0,   0,  0,   0},
            {98, 134,  61,  95,  68, 126, 34, -11},
            {-6,   7,  26,  31,  65,  56, 25, -20},
            {-14,  13,   6,  21,  23,  12, 17, -23},
            {-27,  -2,  -5,  12,  17,   6, 10, -25},
            {-26,  -4,  -4, -10,   3,   3, 33, -12},
            {-35,  -1, -20, -23, -15,  24, 38, -22},
            { 0,   0,   0,   0,   0,   0,  0,   0},
    };
    public BlackPawn(MovesGenerator mg) {
        super("pawn", 'b');
        this.mg = mg;
    }

    @Override
    public ArrayList<Move> update(Game game, int i, int j) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece[][] board = game.getBoard();
        char color = game.getActiveColor();
        int enPassantX = game.getEnPassantX();
        int enPassantY = game.getEnPassantY();
        // ***************************** Black Pawn moving *****************************

        // Forward one
        if (j > 1 /*promote is a different check */) {
            int x = i, y = j - 1; // coords interested in
            if (board[x][y].isEmpty()) {
                // can move forward one
                Move move = new Move(i, j, x, y);
                if (mg.checkIfMoveIsValid(game, move)) {
                    moves.add(move);
                }
            }
        }

        // Forward two if on the starting position
        if (j > 1 && j == 6) {
            int x = i, y = j - 2; // coords interested in
            if (board[i][j - 1].isEmpty() && board[x][y].isEmpty()) {
                // can move forward two
                Move move = new MovedTwice(i,j,x,y,i, (j + y) /2);
                if (mg.checkIfMoveIsValid(game, move)) {
                    moves.add(move);
                }
            }
        }

        // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
        // Includes if it can attack the en passant square
        if (i > 0 && j > 1) {
            int x = i - 1, y = j - 1; // coords interested in
            if (board[x][y].isEnemy(color)) {
                // can attack left
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

        // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
        // Includes it if can attack the en passant square

        if (i < 7 && j > 1) {
            int x = i + 1, y = j - 1;
            if (board[x][y].isEnemy(color)) {
                // can attack right
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
                }
            }
        }

        // Promote via moving

        if (j == 1) {
            int x = i, y = j - 1;
            if (board[x][y].isEmpty()) {
                // Can promote to queen ,rock, bishop knight
                Move move1 = new Promote(i, j, x, y, new Queen(mg, 'b'));
                Move move2 = new Promote(i, j, x, y, new Rook(mg, 'b'));
                Move move3 = new Promote(i, j, x, y, new Bishop(mg, 'b'));
                Move move4 = new Promote(i, j, x, y, new Knight(mg, 'b'));
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

        // Promote via capturing to the left (A pawn cannot attack to the left)
        if (i > 0 && j == 1) {
            int x = i - 1, y = j - 1; // coords interested in
            if (board[x][y].isEnemy(color)) {
                // can attack left to promote
                String enemyName = board[x][y].getName();
                Move move1 = new Promote(i, j, x, y, new Queen(mg, 'b'));
                Move move2 = new Promote(i, j, x, y, new Rook(mg, 'b'));
                Move move3 = new Promote(i, j, x, y, new Bishop(mg, 'b'));
                Move move4 = new Promote(i, j, x, y, new Knight(mg, 'b'));
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


        // Promote via capturing to the right (H pawn cannot attack to the right) (White's perspective)
        if (i > 0 && j == 1) {
            int x = i + 1, y = j - 1; // coords interested in
            if ( x >= 0 && x <=7 && y >=0 && y <= 7 && board[x][y].isEnemy(color)) {
                // can attack right to promote
                String enemyName = board[x][y].getName();
                Move move1 = new PromoteCapture(i, j, x, y, new Queen(mg, 'b'), board[x][y].clone());
                Move move2 = new PromoteCapture(i, j, x, y, new Rook(mg, 'b'), board[x][y].clone());
                Move move3 = new PromoteCapture(i, j, x, y, new Bishop(mg, 'b'), board[x][y].clone());
                Move move4 = new PromoteCapture(i, j, x, y, new Knight(mg, 'b'), board[x][y].clone());
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


        return moves;
    }

    @Override
    public BlackPawn clone() {
        return (BlackPawn) super.clone();
    }

    @Override
    public int getPSTValue(int i, int j) {
        return blackPawnPST[i][j];
    }

}
