
package chessengine;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean isCapture;
    
    private boolean isEnPassant = false;
    private int enPassantX = -1;
    private int enPassantY = -1;
    
    private boolean isCaptureEnPassant = false;
    private int capturePassantX = -1;
    private int capturePassantY = -1;
    
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
    
    public Move(int startX, int startY, int endX, int endY, int capturePassantX, int capturePassantY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        isCapture = true;
        isCaptureEnPassant = true;
        this.capturePassantX = capturePassantX;
        this.capturePassantY = capturePassantY;
    }
    
    public void causesEnPassant() {
        isEnPassant = true;
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
    
    public String getPromotePiece() {
        return promotePiece;
    }
    
    public boolean isCaptureEnPassant() {
        return isCaptureEnPassant;
    }
    
    public boolean getIsCastle() {
        return isCastle;
    }
    
    public boolean getIsKingSide() {
        return isKingSide;
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
