package Game;

public abstract class Piece {

    // Convert a square index to a bitmask
    public static long squareToBitmask(int square) {
        return 1L << square;
    }

}
