package Move;
import Main.Engine;
import Main.Game;

import java.util.ArrayList;

public class MovesGenerator {

    Engine engine;
    Game game;
    ArrayList<Move> moves;
    public MovesGenerator(Engine engine) {
        this.engine = engine;
        game = engine.game;
        moves = game.moves;
    }




    public ArrayList<Move> updateMoves(Game game) {

        char activeColor = game.getActiveColor();



        ArrayList<Move> moves = new ArrayList<>();


        if(activeColor == 'w') {
            updateWhite(moves);
        } else {
            updateBlack(moves);
        }

        return moves;
    }


    public void updateWhite(ArrayList<Move> moves) {
        game.updateOccupiedSquares();
        long[] board = game.getBoard();
        long occupiedSquares = game.getOccupiedSquares();
        long whiteOccupiedSquares = game.getWhiteOccupiedSquares();
        long blackOccupiedSquares = game.getBlackOccupiedSquares();

        // update white pawns
        long whitePawns = board[Game.W_PAWN];

        // Forward one step
        long forwardOne = (whitePawns << 8) & ~occupiedSquares;
        // shifts the bits up a row, then checks it with !occupiedSquares to see if it is empty

        // Forward two steps
//        long forwardTwo = ((forwardOne & (1L << 16)) << 8) & ~occupiedSquares;
//
//        // Left side capture
//        long leftCapture = (whitePawns & ~Game.FILE_A) << 7 & blackOccupiedSquares;
//
//        // Right side capture
//        long rightCapture = (whitePawns & ~Game.FILE_H) << 9 & blackOccupiedSquares;
//
//        // Promote
//        long promote = forwardOne & Game.RANK_8;
//
//        // En Passant Capture
//        long enPassantCapture = game.getEnPassantSquare() & forwardOne & (leftCapture | rightCapture);
//
//        // Add moves to the list
//        long allMoves = forwardOne | forwardTwo | leftCapture | rightCapture | promote | enPassantCapture;


    }

    public void updateBlack(ArrayList<Move> moves) {

    }



}