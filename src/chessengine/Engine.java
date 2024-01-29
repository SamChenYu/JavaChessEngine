
package chessengine;

import Game.Piece;
import Game.Game;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;




public final class Engine {
    
    private final Game game;
    
    private int movesIndexed = 0;
    private int gamesSearched = 0;
    private int bestMoveIndex = 0;
    
    // Multi threading
    private final ExecutorService executorService;
    
    // Minimax
    private final int maxDepth = 10;
    
    public Engine(String input) {
        
        // Create a thread pool with a fixed number of threads
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);
//
//        
        game = new Game(input);
        evaluate(game);
        System.out.println(isInCheck(game));
        double timeStart = System.currentTimeMillis();
//        startSearch(game);
        for(int i=0; i<8; i++) {
            updateMoves(game,i);
        }
        double timeEnd = System.currentTimeMillis();
        double totalTime = (timeEnd - timeStart) / 1000;
        System.out.println("Total Time: " + totalTime + " seconds for " + movesIndexed + " games");
        printMoves(game);

        
        
        
        
        
        
//          game = new Game(input);
//          evaluate(game);
//          Move move = new Move('e',2,'e',4,"Moved_Twice","",'e',-1,"");
//          makeMove(game, move);
//          printBoardState(game);
//          revertMove(game,move);
//          printBoardState(game);
//          
        
//        game = new Game(input);
//        double timeStart = System.currentTimeMillis();
//        findBestMove(game,0);
//        double timeEnd = System.currentTimeMillis();
//        double totalTime = timeEnd - timeStart;
//        System.out.println("Total Time: " + totalTime + " seconds for " + gamesSearched + " games");
//
//        
//        Move bestMove = game.moves.get(bestMoveIndex);
//        Piece[][] board = game.getBoard();
//        String name = board[bestMove.getStartX()][bestMove.getStartY()].getName();
//        System.out.println("Best Move: " + name + " from " + bestMove.getStartX() + bestMove.getStartY() + " moves to " + bestMove.getEndX() + bestMove.getEndY());
//        shutdown();


    }
    
    
    public void startSearch( Game game) {
        // Submit tasks for parallel execution

        
        for(int i=0; i<8;i++) {
            updateMoves(game,i);
        }
        /*
        Future<?> future1 = executorService.submit(() -> updateMoves(game, 0));
        Future<?> future2 = executorService.submit(() -> updateMoves(game, 1));
        Future<?> future3 = executorService.submit(() -> updateMoves(game, 2));
        Future<?> future4 = executorService.submit(() -> updateMoves(game, 3));
        Future<?> future5 = executorService.submit(() -> updateMoves(game, 4));
        Future<?> future6 = executorService.submit(() -> updateMoves(game, 5));
        Future<?> future7 = executorService.submit(() -> updateMoves(game, 6));
        Future<?> future8 = executorService.submit(() -> updateMoves(game, 7));
        

        // Wait for tasks to complete
        try {
            future1.get();
            future2.get();
            future3.get();
            future4.get();
            future5.get();
            future6.get();
            future7.get();
            future8.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        */
    }

    public void shutdown() {
        // Shutdown the executor service when you're done
        executorService.shutdown();
    }


    // Algorithmic Functions:
    public double evaluate(Game game) {
        
        Piece[][] board = game.getBoard();
        
        int whiteMaterial = 0;
        int blackMaterial = 0;
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8;j++) {
                movesIndexed++;
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
        //System.out.println("white: " + whiteMaterial);
        //System.out.println("black: " + blackMaterial);
        // Value is between 1 and -1
        double whiteCalc = (double) whiteMaterial  / 39.0;
        double blackCalc = (double) blackMaterial  / 39.0;
        game.setEvaluation(whiteCalc - blackCalc);
        return game.getEvaluation();
    } // end evaluate

    // multithreading function
    public void updateMoves(Game game, int row) {
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
        ArrayList<Move> moves = game.getMoveList();
        
        int i = row;
        for(int j=0; j<8; j++) {
            movesIndexed++;
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
                                        Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }
                                // Forward two if on the starting position
                                if( j<6 && j == 1 /*promote is a different check*/) {
                                    int x = i, y = j+2; // coords interested in
                                    if(board[i][j+1].isEmpty() && board[x][y].isEmpty()) {
                                        // Can move forward two
                                        Move move = new Move(i,j,x,y, "Moved_Twice", "", i,(j+y)/2, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Attacking to the left (A pawn cannot attack left)
                                // Includes if it can attack the en passant square
                                if( i>0 && j<6 /*Promote is a different case*/ ) {
                                    int x = i-1, y = j+1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // Can attack left
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                    if(enPassantX == (x) && enPassantY == (y)) {
                                        Move move = new Move(i,j,x,y, "En_Passant_Capture", "pawn", x,y, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }

                                }

                                //Attacking to the right (H pawn cannot attack right)
                                // Includes if it can attack the en passant
                                if( i<7 && j<6) {
                                    int x = i+1, y = j+1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                    if(enPassantX == (x) && enPassantY == (y)) {
                                        Move move = new Move(i,j,x,y, "En_Passant_Capture", "pawn", x,y, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Promote via moving
                                if(j == 6) {
                                    int x = i, y = j+1; // coords interested in
                                    if(board[x][y].isEmpty()) {
                                        // Can promote to queen, rook, bishop, knight
                                        Move move1 = new Move(i,j,x,y, "Promote", "", x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote", "", x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote", "", x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote", "", x,y, "knight");

                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                        }
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }

                                    }   
                                }

                                // Promote via capturing to the left (A pawn cannot attack left)
                                if( i>0 && j == 6) {
                                    int x = i-1, y = j+1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // Can attack left
                                        String enemyName = board[x][y].getName();
                                        Move move1 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "knight");
                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                        }
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }
                                    }
                                }

                                // Promote via capturing to the right (H pawn cannot attack right)
                                //Attacking to the right (H pawn cannot attack right)
                                if( i<7 && j==6) {
                                    int x = i+1, y = j+1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // Can attack right
                                        String enemyName = board[x][y].getName();
                                        Move move1 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "knight");
                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                        }
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }
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
                                        Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Forward two if on the starting position
                                if(j>1 && j == 6) {
                                    int x = i, y = j-2; // coords interested in
                                    if(board[i][j-1].isEmpty() && board[x][y].isEmpty()) {
                                        // can move forward two
                                        Move move = new Move(i,j,x,y, "Moved_Twice", "", i,(j+y)/2, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                // Includes if it can attack the en passant square
                                if(i > 0 && j>1) {
                                    int x = i-1, y = j-1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // can attack left
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }

                                    if(enPassantX == (x) && enPassantY == (y)) {
                                        Move move = new Move(i,j,x,y, "En_Passant_Capture", "pawn", x,y, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                // Includes it if can attack the en passant square

                                if(i < 7 && j>1) {
                                    int x = i+1, y = j-1;
                                    if(board[x][y].isEnemy(color)) {
                                        // can attack right
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }

                                    if(enPassantX == (x) && enPassantY == (y)) {
                                        Move move = new Move(i,j,x,y, "En_Passant_Capture", "pawn", x,y, "");
                                        if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                        }
                                    }
                                }

                                // Promote via moving

                                if(j == 1) {
                                    int x = i, y = j-1;
                                    if(board[x][y].isEmpty()) {
                                        // Can promote to queen ,rock, bishop knight
                                        Move move1 = new Move(i,j,x,y, "Promote", "", x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote", "", x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote", "", x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote", "", x,y, "knight");
                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                        }
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }
                                    }
                                }

                                // Promote via capturing to the left (A pawn cannot attack to the left)
                                if(i > 0 && j==1) {
                                    int x = i-1, y = j-1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // can attack left to promote
                                        String enemyName = board[x][y].getName();
                                        Move move1 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "knight");
                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                        }
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }
                                    }
                                }


                                // Promote via capturing to the right (H pawn cannot attack to the right) (White's perspective)
                                if(i > 0 && j==1) {
                                    int x = i+1, y = j-1; // coords interested in
                                    if(board[x][y].isEnemy(color)) {
                                        // can attack right to promote
                                        String enemyName = board[x][y].getName();
                                        Move move1 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "queen");
                                        Move move2 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "rook");
                                        Move move3 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "bishop");
                                        Move move4 = new Move(i,j,x,y, "Promote_Capture", enemyName, x,y, "knight");
                                        if(checkIfMoveIsValid(game,move1)) {
                                            moves.add(move1);
                                            
                                        }
                                        
                                        if(checkIfMoveIsValid(game,move2)) {
                                            moves.add(move2);
                                        }
                                        if(checkIfMoveIsValid(game,move3)) {
                                            moves.add(move3);
                                        }
                                        if(checkIfMoveIsValid(game,move4)) {
                                            moves.add(move4);
                                        }
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
                                 Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                    }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            }
                        }


                        // Left
                        x = i;
                        y = j;
                        x--;
                        y+=2;
                        if( x >= 0 && y<= 7) {

                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            }
                        }

                        // Left
                        x = i;
                        y = j;
                        x--;
                        y-=2;
                        if( x >= 0 && y >= 0) {

                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            }
                        }

                        // down
                        x = i;
                        y = j;
                        x+=2;
                        y--;
                        if( x <=7 && y >= 0) {

                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                 if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            }
                        }

                        // down
                        x = i;
                        y = j;
                        x-=2;
                        y--;
                        if( x >= 0 && y >= 0) {

                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }

                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                   if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                               Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }

                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                   if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks rook's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
                            } else {
                                // Piece blocks bishop's way
                                if(board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                    if(checkIfMoveIsValid(game,move)) {
                                    moves.add(move);
                                }
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
                        if( x <= 7 ) {
                            
                            
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            

                        }

                        x = i;
                        y = j;
                        x--;
                        if(x >= 0 && (board[x][y].isEmpty() || board[x][y].isEnemy(color)) ) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }

                        x = i;
                        y = j;
                        y++;
                        if( y <= 7) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }

                        x = i;
                        y = j;
                        y--;
                        if( y>=0   ) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }

                        // Diagonals

                        x = i;
                        y = j;

                        // TOP RIGHT
                        x++;
                        y++;
                        if(x <= 7 && y <= 7) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }

                        // TOP LEFT
                        x = i;
                        y = j;
                        x--;
                        y++;
                        if(x >= 0 && y <=7) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }

                        // Bottom RIGHT
                        x = i;
                        y = j;
                        x++;
                        y--;
                        if( (x<=7 && y >= 0)) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }
                        // Bottom LEFT
                        x = i;
                        y = j;
                        x--;
                        y--;
                        if(x>= 0 && y>=0) {
                            if(board[x][y].isEmpty()) {
                                Move move = new Move(i,j,x,y, "Move", "", -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                        moves.add(move);
                                }
                            }
                            
                            if(board[x][y].isEnemy(color)) {
                                String enemyName = board[x][y].getName();
                                Move move = new Move(i,j,x,y, "Capture", enemyName, -1,-1, "");
                                if(checkIfMoveIsValid(game,move)) {
                                            moves.add(move);
                                }
                            }
                            
                        }
                        // CASTLING RIGHTS
                        if(color == 'w') {

                            if(whiteCastleKingSide) {
                                if(board[5][0].isEmpty() && board[6][0].isEmpty()) {

                                    if(!isSquareInCheck(game,4,0) && !isSquareInCheck(game,5,0) &&
                                    !isSquareInCheck(game,6,0) && !isSquareInCheck(game,7,0)) {
                                        Move move = new Move(4,0,6,0,"Castle_KingSide", "", -1, -1, "");
                                        moves.add(move);
                                    }

                                }
                            }   
                            if(whiteCastleQueenSide) {
                                if(board[3][0].isEmpty() && board[2][0].isEmpty() && board[1][0].isEmpty()) {
                                    if(!isSquareInCheck(game,0,0) && !isSquareInCheck(game,1,0) &&
                                    !isSquareInCheck(game,2,0) && !isSquareInCheck(game,3,0) && !isSquareInCheck(game,4,0)) {
                                        Move move = new Move(4,0,2,0,"Castle_QueenSide", "", -1, -1, "");
                                        moves.add(move);
                                    }
                                }
                            }
                        } else {

                            if(blackCastleKingSide) {
                                if(board[5][7].isEmpty() && board[6][7].isEmpty()) {
                                    if(!isSquareInCheck(game,4,7) && !isSquareInCheck(game,5,7) &&
                                        !isSquareInCheck(game,6,7) && !isSquareInCheck(game,7,7)) {
                                            Move move = new Move(4,7,6,7,"Castle_KingSide", "", -1, -1, "");
                                            moves.add(move);
                                        }
                                }
                            }
                            if(blackCastleQueenSide) {
                                if(board[3][7].isEmpty() && board[2][7].isEmpty() && board[1][7].isEmpty()) {
                                    if(!isSquareInCheck(game,0,7) && !isSquareInCheck(game,1,7) &&
                                    !isSquareInCheck(game,2,7) && !isSquareInCheck(game,3,7) && !isSquareInCheck(game,4,7)) {
                                        Move move = new Move(4,7,2,7,"Castle_QueenSide", "", -1, -1, "");
                                        moves.add(move);
                                    }
                            }
                        }
                        }

                    } // End case kING

                } // end switch
            } // end if(activeColor)
        } // END INNERLOOP
        checkGameState(game);
    } // END UPDATEMOVES multithreading
    
    
    public boolean checkIfMoveIsValid(Game game, Move move) {
        boolean isInCheck = isInCheck(makeMove(game,move));
        revertMove(game,move);
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
                movesIndexed++;
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
        /********************
         * 
         * This method checks the entire opposite color
         * to see if there is a check on your king
         * returns true if the king is under check
         * returns false if the king isn't under check
         * 
         * very similar to the updatemoves function
        ********************/
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
                movesIndexed++;
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
    
    public Game makeMove(Game game, Move move) {
        
        Piece[][] newBoard = game.getBoard();
        char color = game.getActiveColor();
        

        String moveType = move.getMoveType();
        
        // Saving some states of the game before executing them
        move.setPreviousKingCastleState(game.getWhiteCastleKingSide());
        move.setPreviousQueenCastleState(game.getWhiteCastleQueenSide());
        
        move.setPreviousKingCastleState(game.getBlackCastleKingSide());
        move.setPreviousQueenCastleState(game.getBlackCastleQueenSide());
        
        move.setPreviousEnPassantX(game.getEnPassantX());
        move.setPreviousEnPassantY(game.getEnPassantY());
        
        // Dichotmy of moves
        
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        
                switch(moveType) {

                    case "Move" -> {
                        newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                        newBoard[startX][startY] = new Piece();
                        //Extra special rules:
                        
                        // If the king moves, then it can no longer castle
                        if(newBoard[startX][startY].getName().equals("king")) {
                            if(newBoard[startX][startY].getColor() == 'w') {
                                // save the states in the move object first

                                
                                game.setWhiteCastleKingSide(false);
                                game.setWhiteCastleQueenSide(false);
                            } else {

                                
                                game.setBlackCastleKingSide(false);
                                game.setBlackCastleQueenSide(false);
                            }
                        }

                         // if rooks move from their starting square then they cannot castle   
                        if(newBoard[startX][startY].getName().equals("rook")) {
                                // WHITE ROOKS
                                if (startX == 7 && startY == 0) {
                                    game.setWhiteCastleKingSide(false);
                                } else if(startX == 0 && startY == 0) {
                                    game.setWhiteCastleQueenSide(false);
                                }

                            // BLACK ROOKS
                                if(startX == 7 && startY == 7) {

                                    game.setBlackCastleKingSide(false);
                                } else if (startX == 0 && startY == 7) {
                                    game.setBlackCastleQueenSide(false);
                                }
                            }

                        
                        break;
                    }

                    case "Moved_Twice" -> {
                        newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                        newBoard[startX][startY] = new Piece();
                        game.setEnPassantXY(move.getEnPassantX(), move.getEnPassantY());
                        break;
                    }

                    case "Capture" -> {
                        newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                        newBoard[startX][startY] = new Piece();
                        break;
                    }

                    case "En_Passant_Capture" -> {
                        newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                        newBoard[startX][startY] = new Piece();
                        newBoard[move.getEnPassantX()][move.getEnPassantY()] = new Piece();
                        game.setEnPassant("-");
                        game.setHalfMoveClock(-1);
                        break;
                    }

                    case "Promote" -> {
                        newBoard[endX][endY] = new Piece(move.getPromotePieceTo(), color);
                        newBoard[startX][startY] = new Piece();
                        break;
                    }

                    case "Promote_Capture" -> {
                        newBoard[endX][endY] = new Piece(move.getPromotePieceTo(), color);
                        newBoard[startX][startY] = new Piece();
                        break;
                    }

                    case "Castle_KingSide" -> {
                        if(color == 'w') {
                            newBoard[4][0] = new Piece();
                            newBoard[7][0] = new Piece();
                            newBoard[6][0] = new Piece("king",'w');
                            newBoard[5][0] = new Piece("rook", 'w');
                            game.setWhiteCastleKingSide(false);
                            game.setWhiteCastleQueenSide(false);
                        } else {
                            newBoard[4][7] = new Piece();
                            newBoard[7][7] = new Piece();
                            newBoard[6][7] = new Piece("king",'b');
                            newBoard[5][7] = new Piece("rook", 'b');
                            game.setBlackCastleKingSide(false);
                            game.setBlackCastleQueenSide(false);
                        }
                        break;
                    }

                    case "Castle_QueenSide" -> {
                        if(color == 'w') {
                            newBoard[4][0] = new Piece();
                            newBoard[0][0] = new Piece();
                            newBoard[2][0] = new Piece("king", 'w');
                            newBoard[3][0] = new Piece("rook", 'w');
                            game.setWhiteCastleQueenSide(false);
                            game.setWhiteCastleKingSide(false);
                        } else {
                            newBoard[4][7] = new Piece();
                            newBoard[0][7] = new Piece();
                            newBoard[2][7] = new Piece("king", 'b');
                            newBoard[3][7] = new Piece("rook", 'b');
                            game.setBlackCastleKingSide(false);
                            game.setBlackCastleQueenSide(false);
                        }
                        break;
                    }

                    default-> {
                        System.out.println("Error unrecognized move");
                        break;
                    }
                } // End switch


        //game.setBoard(newBoard);
        // should be irrelevant because newBoard is apointer to the board anyways
        
        // swap the turns

        game.revertHalfMoveClock();
        game.revertFullMoveClock();
        return game;
    } // end makeMove
    public Game revertMove(Game game, Move move) {
        //Exact opposite of the makeMove function
        Piece[][] newBoard = game.getBoard();
        char color = game.getActiveColor();
        char enemyColor;
        if(color == 'w') {
            enemyColor = 'b';
        } else {
            enemyColor = 'w';
        }
        
        String moveType = move.getMoveType();
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        // Dichotmy of moves
        
        
                switch(moveType) {

                    case "Move" -> {
                        newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                        newBoard[endX][endY] = new Piece();
                        //Extra special rules:
                        
                        //Castling rights for reverting moves?
                        // If the king moves, then it can no longer castle
                        if(newBoard[startX][startY].getName().equals("king")) {
                            if(newBoard[startX][startY].getColor() == 'w') {
                                game.setWhiteCastleKingSide(move.getPreviousKingCastleState());
                                game.setWhiteCastleQueenSide(move.getPreviousQueenCastleState());
                            } else {
                                game.setBlackCastleKingSide(move.getPreviousKingCastleState());
                                game.setBlackCastleQueenSide(move.getPreviousQueenCastleState());
                            }
                        }

                         // if rooks move from their starting square then they cannot castle   
                        if(newBoard[startX][startY].getName().equals("rook")) {
                                // WHITE ROOKS
                                if (startX == 7 && startY == 0) {
                                    game.setWhiteCastleKingSide(move.getPreviousKingCastleState());
                                } else if(startX == 0 && startY == 0) {
                                    game.setWhiteCastleQueenSide(move.getPreviousQueenCastleState());
                                }

                            // BLACK ROOKS
                                if(startX == 7 && startY == 7) {

                                    game.setBlackCastleKingSide(move.getPreviousKingCastleState());
                                } else if (startX == 0 && startY == 7) {
                                    game.setBlackCastleQueenSide(move.getPreviousQueenCastleState());
                                }
                            }

                        
                        break;
                    }

                    case "Moved_Twice" -> {
                        newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                        newBoard[endX][endY] = new Piece();
                        game.setEnPassantXY(move.getPreviousEnPassantX(), move.getPreviousEnPassantY());
                        break;
                    }

                    case "Capture" -> {
                        newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                        newBoard[endX][endY] = new Piece(move.getCapturedPiece(),enemyColor);
                        break;
                    }

                    case "En_Passant_Capture" -> {
                        newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                        newBoard[endX][endY] = new Piece();
                        newBoard[move.getEnPassantX()][move.getEnPassantY()] = new Piece("pawn", enemyColor);
                        game.setEnPassant(""+move.getPreviousEnPassantX() + move.getPreviousEnPassantY());
                        game.setEnPassantX(move.getPreviousEnPassantX());
                        game.setEnPassantY(move.getPreviousEnPassantY());
                        game.setHalfMoveClock(-1);
                        break;
                    }

                    case "Promote" -> {
                        newBoard[endX][endY] = new Piece();
                        newBoard[startX][startY] = new Piece("pawn", color);
                        break;
                    }

                    case "Promote_Capture" -> {
                        newBoard[endX][endY] = new Piece(move.getCapturedPiece(),enemyColor);
                        newBoard[startX][startY] = new Piece("pawn", color);
                        break;
                    }

                    case "Castle_KingSide" -> {
                        if(color == 'w') {
                            newBoard[4][0] = new Piece("king",'w');
                            newBoard[7][0] = new Piece("rook", 'w');
                            newBoard[6][0] = new Piece();
                            newBoard[5][0] = new Piece();
                            game.setWhiteCastleKingSide(move.getPreviousKingCastleState());
                            game.setWhiteCastleQueenSide(move.getPreviousQueenCastleState());
                        } else {
                            newBoard[4][7] = new Piece("king",'b');
                            newBoard[7][7] = new Piece("rook", 'b');
                            newBoard[6][7] = new Piece();
                            newBoard[5][7] = new Piece();
                            game.setBlackCastleKingSide(move.getPreviousKingCastleState());
                            game.setBlackCastleQueenSide(move.getPreviousQueenCastleState());
                        }
                        break;
                    }

                    case "Castle_QueenSide" -> {
                        if(color == 'w') {
                            newBoard[4][0] = new Piece("king", 'w');
                            newBoard[0][0] = new Piece("rook", 'w');
                            newBoard[2][0] = new Piece();
                            newBoard[3][0] = new Piece();
                            game.setWhiteCastleQueenSide(move.getPreviousKingCastleState());
                            game.setWhiteCastleKingSide(move.getPreviousQueenCastleState());
                        } else {
                            newBoard[4][7] = new Piece("king", 'b');
                            newBoard[0][7] = new Piece("rook", 'b');
                            newBoard[2][7] = new Piece();
                            newBoard[3][7] = new Piece();
                            game.setBlackCastleKingSide(move.getPreviousKingCastleState());
                            game.setBlackCastleQueenSide(move.getPreviousQueenCastleState());
                        }
                        break;
                    }

                    default-> {
                        System.out.println("Error unrecognized move");
                        break;
                    }
                } // End switch


        
        // swap the turns

        game.incrementHalfMoveClock();
        game.incrementFullMoveClock();
        return game;
    } // End revertMove
    
    
    public Game copyGame(Game game, Game copy) {
        copy = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[][] board = game.getBoard();
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                copy.setBoard(game.getBoard()); // copy over the contents 
            }
        }
        copy.initFlippedBoard();
        copy.setActiveColor(game.getActiveColor());
        copy.setWhiteCastleKingSide(game.getWhiteCastleKingSide());
        copy.setWhiteCastleQueenSide(game.getWhiteCastleQueenSide());
        copy.setBlackCastleKingSide(game.getBlackCastleKingSide());
        copy.setBlackCastleQueenSide(game.getBlackCastleQueenSide());
        copy.setEnPassant(game.getEnPassant());
        copy.setEnPassantX(game.getEnPassantX());
        copy.setEnPassantY(game.getEnPassantY());
        copy.setHalfMoveClock(game.getHalfMoveClock());
        copy.setFullMoveClock(game.getFullMoveClock());
        
        return copy;
    }
    
    
    public void checkGameState(Game game) {
        
        
        if(isInCheck(game) && game.moves.isEmpty()) {
            System.out.println("Checkmate!");
            if(game.getActiveColor() == 'w') {
                game.setEvaluation(-1);
            } else {
                game.setEvaluation(1);
            }
        } else if(!isInCheck(game) && game.moves.isEmpty()) {
            System.out.println("Stalemate!");
            game.setEvaluation(0);
        }
    } // end checkGame
    
    
    public double findBestMove(Game game, int depth) {
        gamesSearched++;
        depth++;
        System.out.println(gamesSearched);
        if(depth == maxDepth) {
            evaluate(game);
            return game.getEvaluation();
        }

        ArrayList<Move> moves = game.moves;
        startSearch(game);
        System.out.println(moves.size() + "*********");
        if(game.getActiveColor() == 'w') {

            double bestValue = Double.NEGATIVE_INFINITY;
            for(int i=0; i<moves.size(); i++) {
                Game leaf = new Game();
                leaf = copyGame(game, leaf);
                makeMove(game,moves.get(i));
                double tempValue  = findBestMove(leaf,depth+1);
                System.out.println(tempValue + " " + bestValue);
                System.out.println(tempValue >= bestValue);
                if(tempValue >= bestValue) {
                    System.out.println("new move called");
                    bestValue = tempValue;
                    bestMoveIndex = i;
                }

            }
            return bestValue;
        } else {
            double leastValue = Double.POSITIVE_INFINITY;
            for (int i = 0; i < moves.size(); i++) {
                Game leaf = new Game();
                leaf = copyGame(game, leaf);
                makeMove(game, moves.get(i));
                double tempValue = findBestMove(leaf, depth + 1);
                leastValue = Math.min(leastValue, tempValue);
            }
            return leastValue;
        }
    } // end findBestMove
    
    
    
    
    // Testing Functions:
    public void printBoardState(Game game) {
        
        
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
                System.out.print("\t " + board[j][i].getName().charAt(0) + " \t");
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
        evaluate(game);
        System.out.println("\n ---------------------\nEvaluation (Only material wise): " + game.getEvaluation());
    }
    public void printMoves(Game game) {
        Piece[][] board = game.getBoard();
        ArrayList<Move> moves = game.getMoveList();
        
        System.out.println("VALID MOVES: " + moves.size());
        
        if(moves.isEmpty()) { 
            System.out.println("Game has ended");
        } else {
            for(int i=0; i<moves.size(); i++) {

                String startFile ="";
                int startX = moves.get(i).getStartX();
                int startY = moves.get(i).getStartY();

                String endFile ="";
                int endX = moves.get(i).getEndX();
                int endY = moves.get(i).getEndY();

                String pieceName = board[startX][startY].getName();
                String promotePieceTo = moves.get(i).getPromotePieceTo();

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

                switch(moves.get(i).getMoveType()) {

                    case "Move" -> {
                        System.out.println(pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY);
                        break;
                    }

                    case "Moved_Twice" -> {
                        System.out.println(pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY);
                        break;
                    }

                    case "Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY);
                        break;
                    }

                    case "En_Passant_Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " (En Passant)");
                        break;
                    }

                    case "Promote" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "promotes to " + promotePieceTo + " " + endFile + endY);
                        break;
                    }

                    case "Promote_Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " and promotes to " + promotePieceTo);
                        break;
                    }

                    case "Castle_KingSide" -> {
                        System.out.println("Castle King Side");
                        break;
                    }

                    case "Castle_QueenSide" -> {
                        System.out.println("Castle Queen Side");
                        break;
                    }

                    default-> {
                        System.out.println("Unrecognized Move");
                        break;
                    }
                } // End switch
            } // End loop
        }
    } // End printMoves
    // Getters and Setters
    public double getEvaluation() { return game.getEvaluation(); }
    public double getTruncatedEvaluation() {
        String truncatedNumberString = String.format("%.5f", game.getEvaluation());
        // Parse the formatted string back to a double if needed
        double truncatedNumber = Double.parseDouble(truncatedNumberString);
        return truncatedNumber;
    }
    // Some getters and setters for the game to pump back to the EnginePanel to user interface
    public Piece[][] getFlippedBoard() { return game.getFlippedBoard(); }
    public char getActiveColor() { return game.getActiveColor(); }
    public int getHalfMoveClock() { return game.getHalfMoveClock(); }
    public int getFullMoveClock() { return game.getFullMoveClock(); }
    
    
} // END ENGINE
