
package chessengine;

import Game.Piece;
import Game.Game;
import java.util.ArrayList;

public final class Engine {
    
    private Game game;
    
    private double evaluation = 0;
    private final ArrayList<Move> moves = new ArrayList<>();
    
    public Engine(String input) {
        
        game = new Game(input);
        evaluate(game.getBoard());
        updateMoves(game);
        printMoves(game.getBoard(),moves);
    }


    // Algorithmic Functions:
    public void evaluate(Piece[][] board) {
        
        int whiteMaterial = 0;
        int blackMaterial = 0;
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8;j++) {
                
                switch(board[i][j].getColor()) {
                    
                    case 'w' -> {
                        if(!board[i][j].getName().equals("king")) {
                            whiteMaterial += board[i][j].getValue();
                        }
                        break;
                    }
                    
                    case 'b' -> {
                        if(!board[i][j].getName().equals("king")) {
                            blackMaterial += board[i][j].getValue();
                        }
                        break;
                    } 
                }
                
            }
        }
        System.out.println("white: " + whiteMaterial);
        System.out.println("black: " + blackMaterial);
        // Value is between 1 and -1
        double whiteCalc = (double) whiteMaterial  / 39.0;
        double blackCalc = (double) blackMaterial  / 39.0;
        evaluation = (whiteCalc - blackCalc);
    }
    public void updateMoves(Game game) {
        
       /*
        
            OKIE FLOW CHART FOR HOW MOVES ARE DETERMINED
        
        1) GIVEN THE ACTIVE PLAYER ARE YOU IN CHECK? (yes -> 2) (no -> )
        2) DO YOU HAVE ANY PIECES THAT CAN BLOCK AN ATTACKER (implies not a double attack) (yes -> 3) ( no -> 4)
        3) ADD PIECE BLOCKS (be careful of double attacks) GO TO 4
        4) CHECK AND ADD FOR KING MOVES (yes-> DONE) ( no-> 5 )
        5) CHECKMATE - EVALUATION IS 1.0 or -1.0
        
        6) CHECK FOR ANY PIECES THAT DOESN'T PUT KING IN CHECK:
        
        PAWNS: 
            MOVE FORWARD OR MOVE FORWARD x 2
            ATTACK DIAGONALLY
            PROMOTE (move AND attack)
            EN PASSANT
        
        ROOK / KNIGHT / BISHOP / QUEEN:
            MOVE / CAPTURE
        
        KING:
            MOVE
            ATTACK < Cannot attack into check >
        
        EVERY SINGLE PIECE WILL HAVE THEIR possibleMoves ArrayList Checked
        */
        


        
        // Firstly we update every single piece's possible moves
        // If any of the possible moves intersect the active player's king,
        // It is CHECK
        
        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        int enPassantX = game.getEnPassantX();
        int enPassantY = game.getEnPassantY();
        boolean whiteCastleKingSide = game.getWhiteCastleKingSide();
        boolean whiteCastleQueenSide = game.getWhiteCastleQueenSide();
        boolean blackCastleKingSide = game.getBlackCastleKingSide();
        boolean blackCastleQueenSide = game.getBlackCastleQueenSide();
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                
                Piece piece = board[i][j];
                String pieceName = piece.getName();
                char color = piece.getColor();
                
                if(activeColor == color) {
                
                switch(pieceName) {
                    
                        case "pawn" -> {
                            if(activeColor == color) {

                                /*
                                Pawns are the only uni-directional piece depending on the
                                Player color, so this is way you have to split it between
                                White and black

                                */
                                /**********************************************/
                                /************** WHITE PAWN MOVING**************/
                                /**********************************************/

                                if(color == 'w') {

                                    // Forward one
                                    if( j<6 /*promote is a different check*/) {
                                        int x = i, y = j+1; // coords interested in
                                        if(board[x][y].isEmpty()) {
                                            // Can move forward one
                                            Move move = new Move(i,j,x,y, false);
                                            moves.add(move);
                                        }
                                    }
                                    // Forward two if on the starting position
                                    if( j<6 && j == 1 /*promote is a different check*/) {
                                        int x = i, y = j+2; // coords interested in
                                        if(board[i][j+1].isEmpty() && board[x][y].isEmpty()) {
                                            // Can move forward two
                                            Move move = new Move(i,j,x,y,false);
                                            move.pawnMovedTwice();
                                            moves.add(move);
                                        }
                                    }

                                    // Attacking to the left (A pawn cannot attack left)
                                    // Includes if it can attack the en passant square
                                    if( i>0 && j<6 /*Promote is a different case*/ ) {
                                        int x = i-1, y = j+1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // Can attack left
                                            Move move = new Move(i,j,x,y,true);
                                            moves.add(move);
                                        }
                                        if(enPassantX == (x) && enPassantY == (y)) {
                                            Move move = new Move(i,j,x,y,x-1,y);
                                            moves.add(move);
                                        }

                                    }

                                    //Attacking to the right (H pawn cannot attack right)
                                    // Includes if it can attack the en passant
                                    if( i<7 && j<6) {
                                        int x = i+1, y = j+1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // Can right left
                                            Move move = new Move(i,j,x,y,true);
                                            moves.add(move);
                                        }
                                        if(enPassantX == (x) && enPassantY == (y)) {
                                            Move move = new Move(i,j,x,y,x+1,y);
                                            moves.add(move);
                                        }
                                    }

                                    // Promote via moving
                                    if(j == 6) {
                                        int x = i, y = j+1; // coords interested in
                                        if(board[x][y].isEmpty()) {
                                            // Can promote to queen, rook, bishop, knight
                                            Move move1 = new Move(i,j,x,y,false,true,"queen");
                                            Move move2 = new Move(i,j,x,y,false,true,"rook");
                                            Move move3 = new Move(i,j,x,y,false,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,false,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }   
                                    }

                                    // Promote via capturing to the left (A pawn cannot attack left)
                                    if( i>0 && j == 6) {
                                        int x = i-1, y = j+1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // Can attack left
                                            Move move1 = new Move(i,j,x,y,true,true,"queen");
                                            Move move2 = new Move(i,j,x,y,true,true,"rook");
                                            Move move3 = new Move(i,j,x,y,true,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,true,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }
                                    }

                                    // Promote via capturing to the right (H pawn cannot attack right)
                                    //Attacking to the right (H pawn cannot attack right)
                                    if( i<7 && j==6) {
                                        int x = i+1, y = j+1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // Can attack right
                                            Move move1 = new Move(i,j,x,y,true,true,"queen");
                                            Move move2 = new Move(i,j,x,y,true,true,"rook");
                                            Move move3 = new Move(i,j,x,y,true,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,true,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }
                                    }

                                    // END White Pawn's code
                                } else {
                                    /**********************************************/
                                    /************** BLACK PAWN MOVING**************/
                                    /**********************************************/

                                    // Forward one
                                    if(j>1 /*promote is a different check */) {
                                        int x = i, y = j-1; // coords interested in
                                        if(board[x][y].isEmpty()) {
                                            // can move forward one
                                            Move move = new Move(i,j,x,y,false);
                                            moves.add(move);
                                        }
                                    }

                                    // Forward two if on the starting position
                                    if(j>1 && j == 6) {
                                        int x = i, y = j-2; // coords interested in
                                        if(board[i][j-1].isEmpty() && board[x][y].isEmpty()) {
                                            // can move forward two
                                            Move move = new Move(i,j,x,y,false);
                                            moves.add(move);
                                        }
                                    }

                                    // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                    // Includes if it can attack the en passant square
                                    if(i > 0 && j>1) {
                                        int x = i-1, y = j-1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // can attack left
                                            Move move = new Move(i,j,x,y,true);
                                            moves.add(move);
                                        }

                                        if(enPassantX == (x) && enPassantY == (y)) {
                                            Move move = new Move(i,j,x,y,x-1,y);
                                            moves.add(move);
                                        }
                                    }

                                    // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                    // Includes it if can attack the en passant square

                                    if(i < 7 && j>1) {
                                        int x = i+1, y = j-1;
                                        if(board[x][y].isEnemy(color)) {
                                            // can attack right
                                            Move move = new Move(i,j,x,y,true);
                                            moves.add(move);
                                        }

                                        if(enPassantX == (x) && enPassantY == (y)) {
                                            Move move = new Move(i,j,x,y,x-1,y);
                                            moves.add(move);
                                        }
                                    }

                                    // Promote via moving

                                    if(j == 1) {
                                        int x = i, y = j-1;
                                        if(board[x][y].isEmpty()) {
                                            // Can promote to queen ,rock, bishop knight
                                            Move move1 = new Move(i,j,x,y,false,true,"queen");
                                            Move move2 = new Move(i,j,x,y,false,true,"rook");
                                            Move move3 = new Move(i,j,x,y,false,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,false,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }
                                    }

                                    // Promote via capturing to the left (A pawn cannot attack to the left)
                                    if(i > 0 && j==1) {
                                        int x = i-1, y = j-1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // can attack left to promote
                                            Move move1 = new Move(i,j,x,y,false,true,"queen");
                                            Move move2 = new Move(i,j,x,y,false,true,"rook");
                                            Move move3 = new Move(i,j,x,y,false,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,false,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }
                                    }


                                    // Promote via capturing to the right (H pawn cannot attack to the right) (White's perspective)
                                    if(i > 0 && j==1) {
                                        int x = i+1, y = j-1; // coords interested in
                                        if(board[x][y].isEnemy(color)) {
                                            // can attack right to promote
                                            Move move1 = new Move(i,j,x,y,false,true,"queen");
                                            Move move2 = new Move(i,j,x,y,false,true,"rook");
                                            Move move3 = new Move(i,j,x,y,false,true,"bishop");
                                            Move move4 = new Move(i,j,x,y,false,true,"knight");
                                            moves.add(move1); moves.add(move2); moves.add(move3); moves.add(move4);
                                        }
                                    }
                                } // End BLACK's pawn code
                            }
                            break;
                        } // End case pawn

                        case "bishop" -> {
                            // 4 Diagonals:

                            int x = i;
                            int y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            break;
                        } // End case bishop

                        case "knight" -> {

                            // Knight has 8 possible attacks



                            int x = i;
                            int y = j;

                            // TWO UP (LEFT / RIGHT)

                            // Right
                            x++;
                            y+=2;
                            if( x <= 7 && y<= 7) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }


                            // Left
                            x = i;
                            y = j;
                            x--;
                            y+=2;
                            if( x >= 0 && y<= 7) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }


                            // TWO DOWN (LEFT / RIGHT)
                            x = i;
                            y = j;
                            // Right
                            x++;
                            y-=2;
                            if( x <= 7  && y>=0) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }

                            // Left
                            x = i;
                            y = j;
                            x--;
                            y-=2;
                            if( x >= 0 && y >= 0) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }

                            // TWO RIGHT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x+=2;
                            y++;
                            if( x <= 7  && y <= 7) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x+=2;
                            y--;
                            if( x <=7 && y >= 0) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }
                            // TWO LEFT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x-=2;
                            y++;
                            if( x >= 0  && y <= 7) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x-=2;
                            y--;
                            if( x >= 0 && y >= 0) {

                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else if(board[x][y].isEnemy(color)) {
                                    Move move = new Move(i,j,x,y, true);
                                    moves.add(move);
                                }
                            }
                            break;
                        } // End case knight

                        case "rook" -> {

                            // 4 DIRECTIONS:
                            // POSITIVE X
                            int x = i;
                            int y = j;
                            while(x < 7) {
                                x++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);

                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            break;
                        } // END case rook

                        case "queen" -> {

                            // 4 AXIS:
                            // POSITIVE X
                            int x = i;
                            int y = j;
                            while(x < 7) {
                                x++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // NEGATIVE X
                            x = i;
                            y = j;
                            while(x > 0) {
                                x--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);

                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // POSITIVE Y
                            x = i;
                            y = j;
                            while(y < 7) {
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks rook's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // NEGATIVE Y
                            x = i;
                            y = j;
                            while(y > 0) {
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // 4 Diagonals:

                            x = i;
                            y = j;

                            // Top Right
                            while(x < 7 && y < 7) {
                                x++;
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // Top Left
                            x = i;
                            y = j;
                            while(x > 0 && y < 7) {
                                x--;
                                y++;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }
                            // Bottom Right
                            x = i;
                            y = j;
                            while(x < 7 && y > 0) {
                                x++;
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            // Bottom Left
                            x = i;
                            y = j;
                            while(x > 0 && y > 0) {
                                x--;
                                y--;
                                if(board[x][y].isEmpty()) {
                                    Move move = new Move(i,j,x,y, false);
                                    moves.add(move);
                                } else {
                                    // Piece blocks bishop's way
                                    if(board[x][y].isEnemy(color)) {
                                        Move move = new Move(i,j,x,y,true);
                                        moves.add(move);
                                        break; // break as there is a piece blocking
                                    } else {
                                        break; // block as there is a friendly piece blocking
                                    }
                                }
                            }

                            break;
                        } // End case queen

                        case "king" -> {

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
                            if( x <= 7 && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            x = i;
                            y = j;
                            x--;
                            if(x >= 0 && board[x][y].isEmpty() ) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            x = i;
                            y = j;
                            y++;
                            if( y <= 7 && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            x = i;
                            y = j;
                            y--;
                            if( y>=0 && board[x][y].isEmpty()  ) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            // Diagonals

                            x = i;
                            y = j;

                            // TOP RIGHT
                            x++;
                            y++;
                            if(x <= 7 && y <= 7  && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            // TOP LEFT
                            x = i;
                            y = j;
                            x--;
                            y++;
                            if(x >= 0 && y <=7 && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            // Bottom RIGHT
                            x = i;
                            y = j;
                            x++;
                            y--;
                            if( (x<=7 && y >= 0) && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }
                            // Bottom LEFT
                            x = i;
                            y = j;
                            x--;
                            y--;
                            if(x>= 0 && y>=0 && board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, false);
                                moves.add(move);
                            }

                            // castling moves
                            if(color == 'w') {

                                if(whiteCastleKingSide) {
                                    if(board[5][0].isEmpty() && board[6][0].isEmpty()) {
                                        Move move = new Move(true, true);
                                        moves.add(move);
                                    }
                                }

                                if(whiteCastleQueenSide) {
                                    if(board[3][0].isEmpty() && board[2][0].isEmpty() && board[1][0].isEmpty()) {
                                        Move move = new Move(true, false);
                                        moves.add(move);
                                    }
                                }
                            } else {
                                if(blackCastleKingSide) {
                                    if(board[5][7].isEmpty() && board[6][7].isEmpty()) {
                                        Move move = new Move(true, true);
                                        moves.add(move);
                                    }
                                }

                                if(blackCastleQueenSide) {
                                    if(board[3][7].isEmpty() && board[2][7].isEmpty() && board[1][7].isEmpty()) {
                                        Move move = new Move(true, false);
                                        moves.add(move);
                                    }
                                }
                            }


                        } // End case kING

                    } // end switch
                } // end if(activeColor)
            } // END INNERLOOP
        } // END OUTERLOOP
    } // END UPDATEMOVES
    
    public boolean isInCheck(Piece[][] board, char activeColor) {
                
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
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece piece = board[i][j];
                if(piece.getColor() == enemyColor) {
                    switch(piece.getName()) {

                        case("pawn") -> {
                            if(enemyColor == 'w') {
                                    // Attacking to the left (A pawn cannot attack left)
                                    // Includes if it can attack the en passant square
                                    if( i>0) {
                                        int x = i-1, y = j+1; // coords interested in
                                        if(x == kingX && y == kingY) {
                                            isKingInCheck = true;
                                            break;
                                        }
                                    }

                                    //Attacking to the right (H pawn cannot attack right)
                                    // Includes if it can attack the en passant
                                    if( i<7) {
                                        int x = i+1, y = j+1; // coords interested in
                                        if(x == kingX && y == kingY) {
                                            isKingInCheck = true;
                                            break;
                                        }
                                    }
                        }  else {
                                    // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                    // Includes if it can attack the en passant square
                                    if(i > 0) {
                                        int x = i-1, y = j-1; // coords interested in
                                        if(x == kingX && y == kingY) {
                                            isKingInCheck = true;
                                            break;
                                        }
                                    }

                                    // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                    // Includes it if can attack the en passant square
                                    if(i < 7) {
                                        int x = i+1, y = j-1;
                                        if(x == kingX && y == kingY) {
                                            isKingInCheck = true;
                                            break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                    break;
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
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
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
                                    }
                                    break; // break as piece blocks
                                } 
                            }
                            
                           // 4 DIRECTIONS:
                            // POSITIVE X
                            x = i;
                            y = j;
                            while(x < 7) {
                                if(!board[x][y].isEmpty()) {
                                    if(x == kingX && y == kingY) {
                                        isKingInCheck = true;
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
                                    }
                                    break; // break as piece blocks
                                } 
                            }
                            break;
                        } // end case queen
                        
                    } // End switch
                }
            } // End inner loop
        } // End outer loop
        
        
        if(isKingInCheck) {
            System.out.println("king in check");
        } else {
            System.out.println("not in check");
        }
        
        
        
        return isKingInCheck;
    } // end IsInCheck
    
    public Piece[][] makeMove(Piece[][] board, Move move) {
        
        // This will be the new board to return
        Piece[][] temp = new Piece[8][8];
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                temp[i][j] = board[i][j];
            }
        }
        
        // isCastle, isEnpassant, isPromote, isCapture
        
        boolean isCastle = move.isCastle();
        boolean isEnPassant = move.isCaptureEnPassant();
        boolean isPromote = move.isPromote();
        boolean isCapture = move.isCapture();
        
        // Dichotmy of moves
        
        if(isCastle) {
            
        } else if (isEnPassant && isCapture) { // this is if you are taking en passant
            
        } else if(isEnPassant) { // this is if the pawn moved twice
            
        } else if(isCapture) {
            
        }
        
        
        
        
        
        return null;
    }
    
    // Testing Functions:
    public void printBoardState() {
        
        Piece[][] board = game.getBoard();
        char activeColor = game.getActiveColor();
        int halfMoveClock = game.getHalfMoveClock();
        int fullMoveClock = game.getFullMoveClock();
        String enPassant = game.getEnPassant();
        
        boolean whiteCastleKingSide = game.getWhiteCastleKingSide();
        boolean whiteCastleQueenSide = game.getWhiteCastleQueenSide();
        boolean blackCastleKingSide = game.getBlackCastleKingSide();
        boolean blackCastleQueenSide = game.getBlackCastleQueenSide();
        
        
        System.out.println("Complete Board");
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                System.out.print(board[i][j].getName() + " ");
            }
            System.out.print("\n");
        }
        System.out.println("Active Color: " + activeColor);
        System.out.println("White Castle King Side " + whiteCastleKingSide);
        System.out.println("White Castle Queen Side " + whiteCastleQueenSide);
        System.out.println("Black Castle King Side " + blackCastleKingSide);
        System.out.println("Black Castle Queen Side " + blackCastleQueenSide);
        System.out.println("En Passant: " + enPassant);
        System.out.println("Half Move Clock " + halfMoveClock);
        System.out.println("Full Move Clock " + fullMoveClock);
        evaluate(board);
        System.out.println("\n ---------------------\nEvaluation (Only material wise): " + evaluation);
    }
    public void printMoves(Piece[][] board, ArrayList<Move> moves) {
        System.out.println("MOVES: " + moves.size());
        for(int i=0; i<moves.size(); i++) {
            
            String startFile ="";
            int startX = moves.get(i).getStartX();
            int startY = moves.get(i).getStartY();
            
            String endFile ="";
            int endX = moves.get(i).getEndX();
            int endY = moves.get(i).getEndY();
            
            String pieceName = board[startX][startY].getName();
            boolean isCapture = moves.get(i).isCapture();
            boolean isEnPassant = moves.get(i).isCaptureEnPassant();
            boolean isPromote = moves.get(i).isPromote();
            String newPiece = moves.get(i).promotePiece();
            boolean isCastle = moves.get(i).isCastle();
            boolean isKingSide = moves.get(i).isKingSide();
            String side ="";
            if(isKingSide) {
                side = "King side";
            } else {
                side = "Queen side";
            }
            startY++; // array starts from 0,0
            endY++; // chess board starts from 1,1
            switch(startX) {
                case 0 -> { startFile = "a"; break;}
                case 1 -> { startFile = "b"; break;}
                case 2 -> { startFile = "c"; break;}
                case 3 -> { startFile = "d"; break;}
                case 4 -> { startFile = "e"; break;}
                case 5 -> { startFile = "f"; break;}
                case 6 -> { startFile = "g"; break;}
                case 7 -> { startFile = "h"; break;}
            }
            switch(endX) {
                case 0 -> { endFile = "a"; break;}
                case 1 -> { endFile = "b"; break;}
                case 2 -> { endFile = "c"; break;}
                case 3 -> { endFile = "d"; break;}
                case 4 -> { endFile = "e"; break;}
                case 5 -> { endFile = "f"; break;}
                case 6 -> { endFile = "g"; break;}
                case 7 -> { endFile = "h"; break;}
            }
            if(isCapture && isPromote) {
                System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " and promotes to " + newPiece);
            }else if(isCastle) {
                System.out.println("Castle " + side);
            } else if(isEnPassant) { 
                System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " (En Passant)");
            }else if(isCapture) {
                System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY);
            } else if(isPromote) {
                System.out.println(pieceName + " " + startFile + startY + " " + "promotes to " + newPiece + " " + endFile + endY);
            } else {
                System.out.println(pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY);
            } 
        }
    } // End printMoves
    // Getters and Setters
    public double getEvaluation() { return evaluation; }
    public double getTruncatedEvaluation() {
        String truncatedNumberString = String.format("%.5f", evaluation);
        // Parse the formatted string back to a double if needed
        double truncatedNumber = Double.parseDouble(truncatedNumberString);
        return truncatedNumber;
    }
    // Some getters and setters for the original game to pump back to the EnginePanel
    public Piece[][] getFlippedBoard() { return game.getFlippedBoard(); }
    public char getActiveColor() { return game.getActiveColor(); }
    public int getHalfMoveClock() { return game.getHalfMoveClock(); }
    public int getFullMoveClock() { return game.getFullMoveClock(); }
    
    
} // END ENGINE
