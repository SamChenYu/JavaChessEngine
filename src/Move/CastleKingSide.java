package Move;

import Game.*;

public class CastleKingSide extends Move {

    public CastleKingSide(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
    }


    @Override
    public Game makeMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        if(game.getActiveColor() == 'w') {
            newBoard[4][0] = new Piece();
            newBoard[7][0] = new Piece();
            newBoard[6][0] = new King(game.mg,'w');
            newBoard[5][0] = new Rook(game.mg, 'w');
            game.setWhiteCastleKingSide(false);
            game.setWhiteCastleQueenSide(false);
        } else {
            newBoard[4][7] = new Piece();
            newBoard[7][7] = new Piece();
            newBoard[6][7] = new King(game.mg,'b');
            newBoard[5][7] = new Rook(game.mg, 'b');
            game.setBlackCastleKingSide(false);
            game.setBlackCastleQueenSide(false);
        }

        return game;
    }


    @Override
    public Game revertMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        if(game.getActiveColor() == 'w') {
            newBoard[4][0] = new King(game.mg,'w');
            newBoard[7][0] = new Rook(game.mg, 'w');
            newBoard[6][0] = new Piece();
            newBoard[5][0] = new Piece();
            game.setWhiteCastleKingSide(getPreviousKingCastleState());
            game.setWhiteCastleQueenSide(getPreviousQueenCastleState());
        } else {
            newBoard[4][7] = new King(game.mg,'b');
            newBoard[7][7] = new Rook(game.mg, 'b');
            newBoard[6][7] = new Piece();
            newBoard[5][7] = new Piece();
            game.setBlackCastleKingSide(getPreviousKingCastleState());
            game.setBlackCastleQueenSide(getPreviousQueenCastleState());
        }
        return game;
    }

    @Override
    public String printMove(Game game) {
        return ("Castle King Side");
    }
}
