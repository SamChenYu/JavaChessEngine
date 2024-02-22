package Move;
import Game.*;

public class Promote extends Move{

    Piece promotePieceTo;

    public Promote(int startX, int starty, int endX, int endY, Piece promotePieceTo) {
        super(startX, starty, endX, endY);
        this.promotePieceTo = promotePieceTo;
    }


    @Override
    public Game makeMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        newBoard[endX][endY] = promotePieceTo;
        newBoard[startX][startY] = new Piece();

        return game;

    }

    @Override
    public Game revertMove(Game game) {
        Piece[][] newBoard = game.getBoard();

        newBoard[endX][endY] = new Piece();
        if(game.getActiveColor() == 'w') {
            newBoard[endX][endY-1] = new WhitePawn(game.mg);
        } else {
            newBoard[endX][endY+1] = new BlackPawn(game.mg);
        }

        return  game;
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

        return(pieceName + " " + startFile + tempStartY + " " + "promotes to " + promotePieceTo.getName() + " " + endFile + tempEndY);

    }

}
