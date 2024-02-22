package Move;

import Game.*;

public class MovedTwice extends Move {

    int enPassantX;
    int enPassantY;

    public MovedTwice(int startX, int startY, int endX, int endY, int enPassantX, int enPassantY) {
        super(startX, startY, endX, endY);
        this.enPassantX = enPassantX;
        this.enPassantY = enPassantY;
    }

    @Override
    public Game makeMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        newBoard[endX][endY] = newBoard[startX][startY].clone();
        newBoard[startX][startY] = new Piece();
        game.setEnPassantXY(enPassantX, enPassantY);
        game.setEnPassantX(enPassantX);
        game.setEnPassantY(enPassantY);

        return game;
    }

    @Override
    public Game revertMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        newBoard[startX][startY] = newBoard[endX][endY].clone();
        newBoard[endX][endY] = new Piece();
        return game;
    }


    @Override
    public String printMove(Game game) {
        String startFile ="";

        String endFile ="";
        String pieceName = game.getBoard()[startX][startY].getName();

        int tempStartY = startY + 1; // array starts from 0,0
        int tempEndY = endY + 1; // chess board starts from 1,1
        switch(startX) {
            case 0 ->   startFile = "a";
            case 1 ->   startFile = "b";
            case 2 ->   startFile = "c";
            case 3 ->   startFile = "d";
            case 4 ->   startFile = "e";
            case 5 ->   startFile = "f";
            case 6 ->   startFile = "g";
            case 7 ->   startFile = "h";
        }
        switch(endX) {
            case 0 ->  endFile = "a";
            case 1 ->  endFile = "b";
            case 2 ->  endFile = "c";
            case 3 ->  endFile = "d";
            case 4 ->  endFile = "e";
            case 5 ->  endFile = "f";
            case 6 ->  endFile = "g";
            case 7 ->  endFile = "h";
        }
        return(pieceName + " " + startFile + tempStartY  + " " + "moves to " + endFile + tempEndY);

    }

}
