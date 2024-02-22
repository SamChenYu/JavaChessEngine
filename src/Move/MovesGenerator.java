package Move;


import Game.Game;
import Game.Piece;
import Move.Move;
import chessengine.Engine;


import java.util.ArrayList;

public class MovesGenerator {

    Game game;
    Engine engine;

    public double movesGenerationTime = 0;

    public MovesGenerator(Engine engine) {
        this.engine = engine;
    }

    public void initGame(Game game) {
        this.game = game;
    }


    public ArrayList<Move> updateMoves(Game game) {
        Piece[][] board = game.getBoard();
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




    public boolean checkIfMoveIsValid(Game game, Move move) {

        boolean isInCheck = isInCheck(engine.makeMove(game,move));
        engine.revertMove(game,move);
        return !isInCheck; // valid if not in check
    } // checkIfMoveIsValid
    public boolean isInCheck(Game game) {

        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        /********************
         *
         * This method checks the entire opposite color
         * to see if there is a check on your king
         * returns true if the king is under check
         * returns false if the king isn't under check
         *
         * very similar to the updatemoves function
         ********************/
        boolean isKingInCheck = false;
        int kingX = -1, kingY = -1;

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(board[i][j].getName().equals("king") && board[i][j].getColor() == activeColor) {
                    kingX = i; kingY = j;
                    break;
                }
            } // End inner loop
        }// End Outerloop
        // Wanting to see the enemy attacks
        char enemyColor;
        if(activeColor == 'w') {
            enemyColor = 'b';
        } else {
            enemyColor = 'w';
        }

        outerLoop:
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(piece.getColor() == enemyColor) {
                    switch(piece.getName()) {

                        case "-" -> { continue; }

                        case("pawn") -> {
                            if(enemyColor == 'w') {
                                // Attacking to the left (A pawn cannot attack left)
                                // Includes if it can attack the en passant square
                                if( i>0) {
                                    int x = i-1, y = j+1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                }

                                //Attacking to the right (H pawn cannot attack right)
                                // Includes if it can attack the en passant
                                if( i<7) {
                                    int x = i+1, y = j+1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                }
                            }  else {
                                // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                // Includes if it can attack the en passant square
                                if(i > 0) {
                                    int x = i-1, y = j-1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                }

                                // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                // Includes it if can attack the en passant square
                                if(i < 7) {
                                    int x = i+1, y = j-1;
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }

                                }
                            }
                            break;
                        } // end Case pawn

                        case("bishop") -> {
                            // 4 Diagonals:

                            int x = i;
                            int y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks


                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case bishop

                        case("knight") -> {

                            // Knight has 8 possible attacks



                            int x = i;
                            int y = j;

                            // TWO UP (LEFT / RIGHT)

                            // Right
                            x++;
                            y+=2;
                            if( x <= 7 && y<= 7) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }


                            // Left
                            x = i;
                            y = j;
                            x--;
                            y+=2;
                            if( x >= 0 && y<= 7) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }


                            // TWO DOWN (LEFT / RIGHT)
                            x = i;
                            y = j;
                            // Right
                            x++;
                            y-=2;
                            if( x <= 7  && y>=0) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // Left
                            x = i;
                            y = j;
                            x--;
                            y-=2;
                            if( x >= 0 && y >= 0) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // TWO RIGHT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x+=2;
                            y++;
                            if( x <= 7  && y <= 7) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x+=2;
                            y--;
                            if( x <=7 && y >= 0) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }
                            // TWO LEFT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x-=2;
                            y++;
                            if( x >= 0  && y <= 7) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x-=2;
                            y--;
                            if( x >= 0 && y >= 0) {

                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }
                            break;
                        } // end case knight

                        case("rook") -> {
                            // 4 DIRECTIONS:
                            // POSITIVE X
                            int x = i;
                            int y = j;
                            while(x < 7) {
                                x++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case rook

                        case("queen") -> {
                            // 4 Diagonals:

                            int x = i;
                            int y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // 4 DIRECTIONS:
                            // POSITIVE X
                            x = i;
                            y = j;
                            while(x < 7) {
                                x++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;

                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
                                        break outerLoop;
                                    }
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case queen

                        case ("king") -> {

                            /****************************************
                             * Special Notes:
                             * The king cannot take a piece under check
                             * However, I think due to the minimax algorithm, it should
                             * be able to avoid that unless it is at the terminated depth
                             *
                             * King also has the ability to castle
                             ****************************************/

                            int x = i;
                            int y = j;

                            // 4 horizontal axis
                            x++;
                            if( x <= 7) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            x = i;
                            y = j;
                            x--;
                            if(x >= 0 ) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            x = i;
                            y = j;
                            y++;
                            if( y <= 7) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            x = i;
                            y = j;
                            y--;
                            if( y>=0   ) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // Diagonals

                            x = i;
                            y = j;

                            // TOP RIGHT
                            x++;
                            y++;
                            if(x <= 7 && y <= 7) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // TOP LEFT
                            x = i;
                            y = j;
                            x--;
                            y++;
                            if(x >= 0 && y <=7) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                            // Bottom RIGHT
                            x = i;
                            y = j;
                            x++;
                            y--;
                            if( (x<=7 && y >= 0)) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }
                            // Bottom LEFT
                            x = i;
                            y = j;
                            x--;
                            y--;
                            if(x>= 0 && y>=0) {
                                if(x == kingX && y == kingY) {
                                    isKingInCheck = true;
                                    break outerLoop;
                                }
                            }

                        } // End case kING
                    } // End switch
                }
            } // End inner loop
        } // End outer loop





        return isKingInCheck;
    } // end IsInCheck


    public boolean isSquareInCheck(Game game, int targetX, int targetY) {

        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        /*
         *
         * This method checks the entire opposite color
         * to see if there is a check on your king
         * returns true if the king is under check
         * returns false if the king isn't under check
         *
         * very similar to the updatemoves function
         */
        int kingX = targetX, kingY = targetY;

        // Wanting to see the enemy attacks
        char enemyColor;
        if(activeColor == 'w') {
            enemyColor = 'b';
        } else {
            enemyColor = 'w';
        }

        outerLoop:
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(piece.getColor() == enemyColor) {
                    switch(piece.getName()) {

                        case "-" -> { continue; }

                        case("pawn") -> {
                            if(enemyColor == 'w') {
                                // Attacking to the left (A pawn cannot attack left)
                                // Includes if it can attack the en passant square
                                if( i>0) {
                                    int x = i-1, y = j+1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        return true;
                                    }
                                }

                                //Attacking to the right (H pawn cannot attack right)
                                // Includes if it can attack the en passant
                                if( i<7) {
                                    int x = i+1, y = j+1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        return true;
                                    }
                                }
                            }  else {
                                // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                // Includes if it can attack the en passant square
                                if(i > 0) {
                                    int x = i-1, y = j-1; // coords interested in
                                    if(x == kingX && y == kingY) {
                                        return true;
                                    }
                                }

                                // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                // Includes it if can attack the en passant square
                                if(i < 7) {
                                    int x = i+1, y = j-1;
                                    if(x == kingX && y == kingY) {
                                        return true;
                                    }

                                }
                            }
                            break;
                        } // end Case pawn

                        case("bishop") -> {
                            // 4 Diagonals:

                            int x = i;
                            int y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case bishop

                        case("knight") -> {

                            // Knight has 8 possible attacks



                            int x = i;
                            int y = j;

                            // TWO UP (LEFT / RIGHT)

                            // Right
                            x++;
                            y+=2;
                            if( x <= 7 && y<= 7) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }


                            // Left
                            x = i;
                            y = j;
                            x--;
                            y+=2;
                            if( x >= 0 && y<= 7) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }


                            // TWO DOWN (LEFT / RIGHT)
                            x = i;
                            y = j;
                            // Right
                            x++;
                            y-=2;
                            if( x <= 7  && y>=0) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }

                            // Left
                            x = i;
                            y = j;
                            x--;
                            y-=2;
                            if( x >= 0 && y >= 0) {
                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }

                            // TWO RIGHT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x+=2;
                            y++;
                            if( x <= 7  && y <= 7) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x+=2;
                            y--;
                            if( x <=7 && y >= 0) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }
                            // TWO LEFT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x-=2;
                            y++;
                            if( x >= 0  && y <= 7) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x-=2;
                            y--;
                            if( x >= 0 && y >= 0) {

                                if(x == kingX && y == kingY) {
                                    return true;
                                }
                            }
                            break;
                        } // end case knight

                        case("rook") -> {
                            // 4 DIRECTIONS:
                            // POSITIVE X
                            int x = i;
                            int y = j;
                            while(x < 7) {
                                x++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case rook

                        case("queen") -> {
                            // 4 Diagonals:

                            int x = i;
                            int y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // 4 DIRECTIONS:
                            // POSITIVE X
                            x = i;
                            y = j;
                            while(x < 7) {
                                x++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;
                                if(x == kingX && y == kingY) {
                                    return true;
                                }

                                if(!board[x][y].isEmpty()) {
                                    break; // break as piece blocks
                                }
                            }
                            break;
                        } // end case queen

                    } // End switch
                }
            } // End inner loop
        } // End outer loop





        return false;
    }



    public int isGameOver(Game game, ArrayList<Move> moves) {
        // This function checks if the game is over
        // It is built for the minimax algorithm
        boolean isInCheck = isInCheck(game);
        if(isInCheck && moves.isEmpty()) {
            if(game.getActiveColor() == 'w') {
                return -1;
            } else {
                return 1;
            }
        } else if(!isInCheck && moves.isEmpty()) {
            System.out.println("Stalemate!");
            return 0;
        } else {
            return 403; // terminatino code
        }
    } // end checkGame

}
