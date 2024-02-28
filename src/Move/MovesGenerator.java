package Move;
import Main.Engine;
import Main.Game;

import java.util.ArrayList;

public class MovesGenerator {

    Engine engine;
    Game game;
    ArrayList<Move> moves;
    public MovesGenerator(Engine engine) {
        this.engine = engine;
        game = engine.game;
        moves = game.moves;
    }




    public ArrayList<Move> updateMoves(Game game) {
        long[] board = game.getBoard();
        ArrayList<Move> moves = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(game.getActiveColor() == piece.getColor()) {
                    ArrayList<Move> temp = piece.update(game,i,j);
                    if(temp!=null) {
                        moves.addAll(temp);
                    }
                }
            }
        }

        return moves;
    }



}