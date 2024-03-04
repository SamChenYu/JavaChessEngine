package Move;
import Main.*;
import Game.*;

import java.util.ArrayList;

public class MovesGenerator {

    Engine engine;
    Game game;
    ArrayList<Integer> moves;
    public MovesGenerator(Engine engine) {
        this.engine = engine;
        game = engine.game;
        moves = game.moves;
    }




    public ArrayList<Integer> updateMoves(Game game) {

        char activeColor = game.getActiveColor();



        ArrayList<Integer> moves = new ArrayList<>();


        if(activeColor == 'w') {
            moves = updateWhite();
        } else {
            moves = updateBlack();
        }

        return moves;
    }


    public ArrayList<Integer> updateWhite() {

        ArrayList<Integer> moves = new ArrayList<>();

        Knight.updateMoves(game);
        return moves;
    }

    private long squareToBitmask(int square) {
        return 1L << square;
    }
    public ArrayList<Integer> updateBlack() {
        return null;
    }



}