package Game;
import Main.Game;
import Move.*;
public class Knight extends Piece {

    public static void updateMoves(Game game) {
        game.updateOccupiedSquares();
        long[] board = game.getBoard();
        long occupiedSquares = game.getOccupiedSquares();
        long whiteOccupiedSquares = game.getWhiteOccupiedSquares();
        long blackOccupiedSquares = game.getBlackOccupiedSquares();


        long knights = board[Game.W_KNIGHT];


        while(knights != 0) {
            long attackBitmask = 0L;
            int square = Long.numberOfTrailingZeros(knights); // finds the least significant 1 bit in knights (from right to left)
            knights &= knights - 1; // Reset the least significant 1 bit in knights

            // basically what it does is it finds the least significant bit in knights, then sets it to 0
            // so that we can process it and find then next bit

            // creating the knight move bitmask
            /*
            bit positioning:
                top: -6, 10
                right: 17, 15
                bottom: 6, -10
                left: -17, -15
             */

            byte vectors[] = {-6, 10, 17, 15, 6, -10, -17, -15};
            for (int vector : vectors) {
                int destination = square + vector;
                if (destination >= 0 && destination < 64 && (1L << destination & whiteOccupiedSquares) == 0) {
                    attackBitmask |= squareToBitmask(destination);
                }
            }

            // now the moves must be extracted from the attack bitmask

            while(attackBitmask != 0) {
                int move = Long.numberOfTrailingZeros(attackBitmask);
                attackBitmask &= attackBitmask - 1;

                game.moves.add(new Move(square, move)) ;
            }
        }




    }

}