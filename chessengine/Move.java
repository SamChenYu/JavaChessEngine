
package chessengine;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
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

    @Override
    public String toString() {
        char startFile = (char) ('a' + startX);
        char endFile = (char) ('a' + endX);
        int startRank = 8 - startY;
        int endRank = 8 - endY;
        return String.format("%c%d to %c%d", startFile, startRank, endFile, endRank);
    }
}
