package Move;

import Game.*;

public class CastleQueenSide extends Move {

    public CastleQueenSide(int startX, int startY, int endX, int endY) {

        super(startX, startY, endX, endY);
    }

    @Override
    public Game makeMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        if(game.getActiveColor() == 'w') {
            newBoard[4][0] = new Piece();
            newBoard[0][0] = new Piece();
            newBoard[2][0] = new King(game.mg, 'w');
            newBoard[3][0] = new Rook(game.mg, 'w');
            game.setWhiteCastleQueenSide(false);
            game.setWhiteCastleKingSide(false);
        } else {
            newBoard[4][7] = new Piece();
            newBoard[0][7] = new Piece();
            newBoard[2][7] = new King(game.mg, 'b');
            newBoard[3][7] = new Rook(game.mg, 'b');
            game.setBlackCastleKingSide(false);
            game.setBlackCastleQueenSide(false);
        }

        return game;
    }

    @Override
    public Game revertMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        if(game.getActiveColor() == 'w') {
            newBoard[4][0] = new King(game.mg, 'w');
            newBoard[0][0] = new Rook(game.mg, 'w');
            newBoard[2][0] = new Piece();
            newBoard[3][0] = new Piece();

        } else {
            newBoard[4][7] = new King(game.mg, 'b');
            newBoard[0][7] = new Rook(game.mg, 'b');
            newBoard[2][7] = new Piece();
            newBoard[3][7] = new Piece();

        }

        return game;
    }

    @Override
    public String printMove(Game game) {
        return("Castle Queen Side");
    }

}
