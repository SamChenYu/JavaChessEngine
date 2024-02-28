package Move;


import Game.Game;
import Game.Piece;
import Move.Move;
import chessengine.Engine;


import java.util.ArrayList;

public class MovesGenerator {

    Game game;
    Engine engine;

    public double movesGenerationTime = 0;

    public MovesGenerator(Engine engine) {
        this.engine = engine;
    }

    public void initGame(Game game) {
        this.game = game;
    }


    public ArrayList<Move> updateMoves(Game game) {
        Piece[][] board = game.getBoard();
        ArrayList<Move> moves = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(game.getActiveColor() == piece.getColor()) {
                    ArrayList<Move> temp = piece.update(game,i,j);
                    if(temp!=null) {
                        moves.addAll(temp);
                    }
                }
            }
        }

        return moves;
    }




    public boolean checkIfMoveIsValid(Game game, Move move) {

        boolean isInCheck = isInCheck(engine.makeMove(game,move));
        engine.revertMove(game,move);
        return !isInCheck; // valid if not in check
    } // checkIfMoveIsValid
    public boolean isInCheck(Game game) {

        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        /********************
         *
         * This method checks the entire opposite color
         * to see if there is a check on your king
         * returns true if the king is under check
         * returns false if the king isn't under check
         *
         * very similar to the updatemoves function
         ********************/
        int kingX = -1, kingY = -1;

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(board[i][j].getName().equals("king") && board[i][j].getColor() == activeColor) {
                    kingX = i; kingY = j;
                    break;
                }
            } // End inner loop
        }// End Outerloop




        // Wanting to see the enemy attacks
        char enemyColor;
        if(activeColor == 'w') {
            enemyColor = 'b';
        } else {
            enemyColor = 'w';
        }

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(piece.getName().equals("-")) {
                    continue;
                } else if(piece.getColor() == enemyColor) {
                    if(piece.isAttackingKing(game, i, j, kingX, kingY)) {
                        return true;
                    }
                }
            }
        }


        return false;
    } // end IsInCheck


    public boolean isSquareInCheck(Game game, int targetX, int targetY) {

        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        int kingX = targetX, kingY = targetY;


        // Wanting to see the enemy attacks
        char enemyColor;
        if(activeColor == 'w') {
            enemyColor = 'b';
        } else {
            enemyColor = 'w';
        }

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];

                if(piece.getColor() == enemyColor) {
                    if(piece.isAttackingKing(game, i, j, kingX, kingY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    public int isGameOver(Game game, ArrayList<Move> moves) {
        // This function checks if the game is over
        // It is built for the minimax algorithm
        boolean isInCheck = isInCheck(game);
        if(isInCheck && moves.isEmpty()) {
            if(game.getActiveColor() == 'w') {
                return -1;
            } else {
                return 1;
            }
        } else if(!isInCheck && moves.isEmpty()) {
            System.out.println("Stalemate!");
            return 0;
        } else {
            return 403; // terminatino code
        }
    } // end checkGame

}
