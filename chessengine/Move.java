
package chessengine;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    private boolean isCapture;
    
    // Pawn moved forward twice
    private boolean pawnMovedTwice = false;
    private int enPassantX = -1;
    private int enPassantY = -1;
    
    

    // Captures an en passant
    private boolean isCaptureEnPassant = false;
    private int enPassantPieceActualX = -1;
    private int enPassantPieceActualY = -1;
    

    
    private boolean isPromote = false;
    private String promotePiece = "";
    
    private boolean isCastle;
    private boolean isKingSide;
    
    // For capturing a piece
    public Move(int startX, int startY, int endX, int endY, boolean isCapture) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isCapture = isCapture;
    }
    
    // For promoting a piece (move / capture)
    public Move(int startX, int startY, int endX, int endY, boolean isCapture,
        boolean isPromote, String promotePiece) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.isCapture = isCapture;
        this.isPromote = isPromote;
        this.promotePiece = promotePiece;
    }
    
    public Move(boolean isCastle, boolean isKingSide) {
        this.isCastle = isCastle;
        this.isKingSide = isKingSide;
    }
    
    
    
    // For capturing en passant
    
    public Move(int startX, int startY, int endX, int endY, int enPassantPieceActualX, int enPassantPieceActualY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        isCapture = true;
        isCaptureEnPassant = true;
        this.enPassantPieceActualX = enPassantPieceActualX;
        this.enPassantPieceActualY = enPassantPieceActualY;
    }
    
    public void pawnMovedTwice() {
        pawnMovedTwice = true;
        enPassantX = startX;
        enPassantY = (startY + endY) /2;
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
    
    public boolean isCapture() {
        return isCapture;
    }
    
    public boolean isPromote() {
        return isPromote;
    }
    
    public String promotePiece() {
        return promotePiece;
    }
    
    public boolean isCaptureEnPassant() {
        return isCaptureEnPassant;
    }
    
    public boolean isCastle() {
        return isCastle;
    }
    
    public boolean isKingSide() {
        return isKingSide;
    }

    
    public int getEnPassantX() { return enPassantX; }
    public int getEnPassantY() { return enPassantY; }
    
    public int getEnPassantPieceActualX() { return enPassantPieceActualX; }
    public int getEnPassantPieceActualY() { return enPassantPieceActualY;}
    
    @Override
    public String toString() {
        char startFile = (char) ('a' + startX);
        char endFile = (char) ('a' + endX);
        int startRank = 8 - startY;
        int endRank = 8 - endY;
        return String.format("%c%d to %c%d", startFile, startRank, endFile, endRank);
    }
}
