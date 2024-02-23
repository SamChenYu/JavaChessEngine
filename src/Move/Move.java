package Move;

import Game.*;

public class Move {
    
    public final int startX;
    public final int startY;
    public final int endX;
    public final int endY;
    
    
    private String moveType;
    /**
     * Types:
     * Move 
     * Moved_Twice (For pawns moving twice to update en passant)
     * Capture (save piece type)
     * En_Passant_Capture 
     * Promote 
     * Promote_Capture
     * Castle_KingSide
     * Castle_QueenSide
     **/
    private String capturedPiece;
    private int enPassantX; // either holds the created enpassant square or the piece being captured
    private int enPassantY;
    private String promotePieceTo;
    
    // States that the move needs to store in the event that it changes the states in the game
    private boolean previousKingCastleState;
    private boolean previousQueenCastleState;
    private int previousEnPassantX;
    private int previousEnPassantY;
    

    // Constructor for ONLY move

    public Move(int starX, int startY, int endX, int endY) {
        this.startX = starX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
    
    public String getMoveType() {
        return moveType;
    }
    
    public String getCapturedPiece() {
        return capturedPiece;
    }
    
    public int getEnPassantX() {
        return enPassantX;
    }
    
    public int getEnPassantY() {
        return enPassantY;
    }
    
    public String getPromotePieceTo() {
        return promotePieceTo;
    }
    
    public boolean getPreviousKingCastleState() { return previousKingCastleState; }
    public boolean getPreviousQueenCastleState() { return previousQueenCastleState; }
    public int getPreviousEnPassantX() { return previousEnPassantX; }
    public int getPreviousEnPassantY() { return previousEnPassantY; }
    
    public void setPreviousKingCastleState(boolean b) { previousKingCastleState = b; }
    public void setPreviousQueenCastleState(boolean b) { previousQueenCastleState = b; }
    public void setPreviousEnPassantX(int i) { previousEnPassantX = i; }
    public void setPreviousEnPassantY(int i) { previousEnPassantY = i; }
    
    
    public int toInt(char c) {
        
        switch(c) {
            
            case 'a' -> {
                return 0;
            }
            
            case 'b' -> {
                return 1;
            }
            
            case 'c' -> {
                return 2;
            }
            
            case 'd' -> {
                return 3;
            }
            
            case 'e' -> {
                return 4;
            }
            
            case 'f' -> {
                return 5;
                
            }
            
            case 'g' -> {
                return 6;
            }
            
            case 'h' -> {
                return 7;
            }
            
            default -> {
                System.out.println("Unrecognized file for input move");
                return -1;
            }
            
        }
        
        
    }

    public char toChar(int i) {

        switch(i) {

            case 0 -> {
                return 'a';
            }

            case 1 -> {
                return 'b';
            }

            case 2 -> {
                return 'c';
            }

            case 3 -> {
                return 'd';
            }

            case 4 -> {
                return 'e';
            }

            case 5 -> {
                return 'f';

            }

            case 6 -> {
                return 'g';
            }

            case 7 -> {
                return 'h';
            }

            default -> {
                System.out.println("Unrecognized file for input move");
                return 'z';
            }

        }


    }
    
    
    
    @Override
    public String toString() {
        char startFile = (char) ('a' + startX);
        char endFile = (char) ('a' + endX);
        int startRank = 8 - startY;
        int endRank = 8 - endY;
        return String.format("%c%d to %c%d", startFile, startRank, endFile, endRank);
    }

    public Game makeMove(Game game) {

        Piece[][] newBoard = game.getBoard();
        newBoard[endX][endY] = newBoard[startX][startY].clone();
        newBoard[startX][startY] = new Piece();
        //Extra special rules:

        // If the king moves, then it can no longer castle
        if(newBoard[startX][startY] instanceof King) {
            if(newBoard[startX][startY].getColor() == 'w') {
                // save the states in the move object first


                game.setWhiteCastleKingSide(false);
                game.setWhiteCastleQueenSide(false);
            } else {
                game.setBlackCastleKingSide(false);
                game.setBlackCastleQueenSide(false);
            }
        }

        // if rooks move from their starting square then they cannot castle
        if(newBoard[startX][startY] instanceof Rook) {
            // WHITE ROOKS
            if (startX == 7 && startY == 0) {
                game.setWhiteCastleKingSide(false);
            } else if(startX == 0 && startY == 0) {
                game.setWhiteCastleQueenSide(false);
            }

            // BLACK ROOKS
            if(startX == 7 && startY == 7) {

                game.setBlackCastleKingSide(false);
            } else if (startX == 0 && startY == 7) {
                game.setBlackCastleQueenSide(false);
            }
        }
        return game;
    }


    public Game revertMove(Game game) {
        Piece[][] newBoard = game.getBoard();
        newBoard[startX][startY] = newBoard[endX][endY].clone();
        newBoard[endX][endY] = new Piece();
        //Extra special rules:

        //Castling rights for reverting moves?
        // If the king moves, then it can no longer castle
        if(newBoard[startX][startY] instanceof King) {
            if(newBoard[startX][startY].getColor() == 'w') {
                game.setWhiteCastleKingSide(getPreviousKingCastleState());
                game.setWhiteCastleQueenSide(getPreviousQueenCastleState());
            } else {
                game.setBlackCastleKingSide(getPreviousKingCastleState());
                game.setBlackCastleQueenSide(getPreviousQueenCastleState());
            }
        }

        // if rooks move from their starting square then they cannot castle
        if(newBoard[startX][startY] instanceof Rook) {
            // WHITE ROOKS
            if (startX == 7 && startY == 0) {
                game.setWhiteCastleKingSide(getPreviousKingCastleState());
            } else if(startX == 0 && startY == 0) {
                game.setWhiteCastleQueenSide(getPreviousQueenCastleState());
            }

            // BLACK ROOKS
            if(startX == 7 && startY == 7) {

                game.setBlackCastleKingSide(getPreviousKingCastleState());
            } else if (startX == 0 && startY == 7) {
                game.setBlackCastleQueenSide(getPreviousQueenCastleState());
            }
        }





        return game;
    }



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