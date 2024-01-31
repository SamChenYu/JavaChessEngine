package chessengine;

public class Move {
    
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    
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
    

    public Move(int startX, int startY, int endX, int endY, String moveType,
        String capturedPiece, int enPassantX, int enPassantY, String promotePieceTo) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.moveType = moveType;
        this.capturedPiece = capturedPiece;
        this.enPassantX = enPassantX;
        this.enPassantY = enPassantY;
        this.promotePieceTo = promotePieceTo;
    }
    // This is a method for testing purposes
    public Move(char startXc, int startY, char endXc, int endY, String moveType,
        String capturedPiece, char enPassantXc, int enPassantY, String promotePieceTo) {
        this.startX = toInt(startXc);
        this.startY = startY-1;
        this.endX = toInt(endXc);
        this.endY = endY-1;
        this.moveType = moveType;
        this.capturedPiece = capturedPiece;
        this.enPassantX = toInt(enPassantXc);
        this.enPassantY = enPassantY-1;
        this.promotePieceTo = promotePieceTo;
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
    
    
    
    @Override
    public String toString() {
        char startFile = (char) ('a' + startX);
        char endFile = (char) ('a' + endX);
        int startRank = 8 - startY;
        int endRank = 8 - endY;
        return String.format("%c%d to %c%d", startFile, startRank, endFile, endRank);
    }
}