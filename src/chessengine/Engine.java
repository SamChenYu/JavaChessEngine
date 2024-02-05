
package chessengine;

import Game.Piece;
import Game.Game;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public final class Engine {

    private final Game game;

    private final int maxDepth = 3;
    private int movesIndexed = 0;
    private int gamesSearched = 0;
    private int bestMoveIndex = 0;

    // Multi threading
    private final ExecutorService executorService;


    //Piece-Square Tables from
    // https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function
    // http://www.talkchess.com/forum3/viewtopic.php?f=2&t=68311&start=19#

    // The Piece-Square Tables have been flipped to be consistent with the board
    private final int[][] whitePawnPST = {
            { 0,   0,   0,   0,   0,   0,  0,   0},
            {-35,  -1, -20, -23, -15,  24, 38, -22},
            {-26,  -4,  -4, -10,   3,   3, 33, -12},
            {-27,  -2,  -5,  12,  17,   6, 10, -25},
            {-14,  13,   6,  21,  23,  12, 17, -23},
            {-6,   7,  26,  31,  65,  56, 25, -20},
            {98, 134,  61,  95,  68, 126, 34, -11},
            { 0,   0,   0,   0,   0,   0,  0,   0},
    };
    private final int[][] blackPawnPST = {
            { 0,   0,   0,   0,   0,   0,  0,   0},
            {98, 134,  61,  95,  68, 126, 34, -11},
            {-6,   7,  26,  31,  65,  56, 25, -20},
            {-14,  13,   6,  21,  23,  12, 17, -23},
            {-27,  -2,  -5,  12,  17,   6, 10, -25},
            {-26,  -4,  -4, -10,   3,   3, 33, -12},
            {-35,  -1, -20, -23, -15,  24, 38, -22},
            { 0,   0,   0,   0,   0,   0,  0,   0},
    };
    private final int[][] whiteKnightPST = {
            {-105, -21, -58, -33, -17, -28, -19,  -23},
            {-29, -53, -12,  -3,  -1,  18, -14,  -19},
            {-23,  -9,  12,  10,  19,  17,  25,  -16},
            {-13,   4,  16,  13,  28,  19,  21,   -8},
            {-9,  17,  19,  53,  37,  69,  18,   22},
            {-47,  60,  37,  65,  84, 129,  73,   44},
            {-73, -41,  72,  36,  23,  62,   7,  -17},
            {-167, -89, -34, -49,  61, -97, -15, -107},
    };
    private final int[][] blackKnightPST = {
            {-167, -89, -34, -49,  61, -97, -15, -107},
            {-73, -41,  72,  36,  23,  62,   7,  -17},
            {-47,  60,  37,  65,  84, 129,  73,   44},
            {-9,  17,  19,  53,  37,  69,  18,   22},
            {-13,   4,  16,  13,  28,  19,  21,   -8},
            {-23,  -9,  12,  10,  19,  17,  25,  -16},
            {-29, -53, -12,  -3,  -1,  18, -14,  -19},
            {-105, -21, -58, -33, -17, -28, -19,  -23},
    };
    private final int[][] whiteBishopPST = {
            {-33,  -3, -14, -21, -13, -12, -39, -21},
            {4,  15,  16,   0,   7,  21,  33,   1},
            {0,  15,  15,  15,  14,  27,  18,  10},
            {-6,  13,  13,  26,  34,  12,  10,   4},
            {-4,   5,  19,  50,  37,  37,   7,  -2},
            {-16,  37,  43,  40,  35,  50,  37,  -2},
            {-26,  16, -18, -13,  30,  59,  18, -47},
            {-29,   4, -82, -37, -25, -42,   7,  -8},
    };
    private final int[][] blackBishopPST = {
            {-29,   4, -82, -37, -25, -42,   7,  -8},
            {-26,  16, -18, -13,  30,  59,  18, -47},
            {-16,  37,  43,  40,  35,  50,  37,  -2},
            {-4,   5,  19,  50,  37,  37,   7,  -2},
            {-6,  13,  13,  26,  34,  12,  10,   4},
            {0,  15,  15,  15,  14,  27,  18,  10},
            {4,  15,  16,   0,   7,  21,  33,   1},
            {-33,  -3, -14, -21, -13, -12, -39, -21},
    };
    private final int[][] whiteRookPST = {
            {-19, -13,   1,  17, 16,  7, -37, -26},
            {-44, -16, -20,  -9, -1, 11,  -6, -71},
            {-45, -25, -16, -17,  3,  0,  -5, -33},
            {-36, -26, -12,  -1,  9, -7,   6, -23},
            {-24, -11,   7,  26, 24, 35,  -8, -20},
            {-5,  19,  26,  36, 17, 45,  61,  16},
            {27,  32,  58,  62, 80, 67,  26,  44},
            {32,  42,  32,  51, 63,  9,  31,  43},
    };
    private final int[][] blackRookPST = {
            {32,  42,  32,  51, 63,  9,  31,  43},
            {27,  32,  58,  62, 80, 67,  26,  44},
            {-5,  19,  26,  36, 17, 45,  61,  16},
            {-24, -11,   7,  26, 24, 35,  -8, -20},
            {-36, -26, -12,  -1,  9, -7,   6, -23},
            {-45, -25, -16, -17,  3,  0,  -5, -33},
            {-44, -16, -20,  -9, -1, 11,  -6, -71},
            {-19, -13,   1,  17, 16,  7, -37, -26}
    };
    private final int[][]  whiteQueenPST= {
            {-1, -18,  -9,  10, -15, -25, -31, -50},
            {-35,  -8,  11,   2,   8,  15,  -3,   1},
            {-14,   2, -11,  -2,  -5,   2,  14,   5},
            {-9, -26,  -9, -10,  -2,  -4,   3,  -3},
            {-27, -27, -16, -16,  -1,  17,  -2,   1},
            {-13, -17,   7,   8,  29,  56,  47,  57},
            {-24, -39,  -5,   1, -16,  57,  28,  54},
            {-28,   0,  29,  12,  59,  44,  43,  45},
    };
    private final int[][]  blackQueenPST= {
            {-28,   0,  29,  12,  59,  44,  43,  45},
            {-24, -39,  -5,   1, -16,  57,  28,  54},
            {-13, -17,   7,   8,  29,  56,  47,  57},
            {-27, -27, -16, -16,  -1,  17,  -2,   1},
            {-9, -26,  -9, -10,  -2,  -4,   3,  -3},
            {-14,   2, -11,  -2,  -5,   2,  14,   5},
            {-35,  -8,  11,   2,   8,  15,  -3,   1},
            {-1, -18,  -9,  10, -15, -25, -31, -50},
    };
    private final int[][] whiteKingPST = {
            {-15,  36,  12, -54,   8, -28,  24,  14},
            {1,   7,  -8, -64, -43, -16,   9,   8},
            {-14, -14, -22, -46, -44, -30, -15, -27},
            {-49,  -1, -27, -39, -46, -44, -33, -51},
            {-17, -20, -12, -27, -30, -25, -14, -36},
            {-9,  24,   2, -16, -20,   6,  22, -22},
            {29,  -1, -20,  -7,  -8,  -4, -38, -29},
            {-65,  23,  16, -15, -56, -34,   2,  13},
    };
    private final int[][] blackKingPST = {
            {-65,  23,  16, -15, -56, -34,   2,  13},
            {29,  -1, -20,  -7,  -8,  -4, -38, -29},
            {-9,  24,   2, -16, -20,   6,  22, -22},
            {-17, -20, -12, -27, -30, -25, -14, -36},
            {-49,  -1, -27, -39, -46, -44, -33, -51},
            {-14, -14, -22, -46, -44, -30, -15, -27},
            {1,   7,  -8, -64, -43, -16,   9,   8},
            {-15,  36,  12, -54,   8, -28,  24,  14},
    };
    public Engine(String input) {

        // Create a thread pool with a fixed number of threads
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);

        game = new Game(input);
        evaluate(game);
        printBoardState(game);

    }

    public void startSearch() {
        // finding legal moves
//        double timeStart = System.currentTimeMillis();
//        game.moves = updateMoves(game):
//        for(int i=0; i<8; i++) {
//            updateMoves(game,i);
//        }
//        double timeEnd = System.currentTimeMillis();
//        double totalTime = (timeEnd - timeStart) /1000.0;
//        System.out.println("Total Time: " + totalTime + " seconds for " + movesIndexed + " moves");
//        printMoves(game);
//        printBoardState(game);


        // Activates Minimax
        double timeStart = System.currentTimeMillis();
        game.moves = updateMoves(game);
        printMoves(game);
        double lowestValue = -100000.0;
        double highestValue = 100000.0;
        findBestMove(game,0,lowestValue,highestValue, true);
        System.out.println("Games searched: " + gamesSearched );
        double timeEnd = System.currentTimeMillis();
        double totalTime = (timeEnd - timeStart) / 1000;
        System.out.println("Total Time: " + totalTime + " seconds for " + gamesSearched + " games");

        // Prints out best move
        Move bestMove = game.moves.get(bestMoveIndex);
        Piece[][] board = game.getBoard();
        String name = board[bestMove.getStartX()][bestMove.getStartY()].getName();
        System.out.println("Best Move: " + name + " from " + bestMove.toChar(bestMove.getStartX()) + (bestMove.getStartY()+1) + " moves to " + bestMove.toChar(bestMove.getEndX()) + (bestMove.getEndY()+1));
        shutdown();

        evaluate(game);
    }

    public void parallel( Game game) {
        // Submit tasks for parallel execution

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

        int whitePosition = 0;
        int blackPosition = 0;

        int whiteMaterial = 0;
        int blackMaterial = 0;
        int totalMaterial = 0;

        for(int i=0; i<8;i++) {
            for(int j=0; j<8;j++){
                movesIndexed++;
                Piece piece = board[j][i];
                switch(piece.getName()) {

                    case "pawn" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whitePawnPST[i][j];
                            whiteMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        } else {
                            blackPosition += blackPawnPST[i][j];
                            blackMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        }
                    } // End case pawn

                    case "knight" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whiteKnightPST[i][j];
                            whiteMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        } else {
                            blackPosition += blackKnightPST[i][j];
                            blackMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        }
                    } // End case knight

                    case "bishop" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whiteBishopPST[i][j];
                            whiteMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        } else {
                            blackPosition += blackBishopPST[i][j];
                            blackMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        }
                    } // End case bishop

                    case "rook" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whiteRookPST[i][j];
                            whiteMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        } else {
                            blackPosition += blackRookPST[i][j];
                            blackMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        }
                    } // End case rook

                    case "queen" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whiteQueenPST[i][j];
                            whiteMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        } else {
                            blackPosition += blackQueenPST[i][j];
                            blackMaterial += piece.getValue();
                            totalMaterial += piece.getValue();
                        }
                    } // End case queen

                    case "king" -> {
                        if(piece.getColor() == 'w') {
                            whitePosition += whiteKingPST[i][j];

                        } else {
                            blackPosition += blackKingPST[i][j];
                        }
                    } // End case king


                } // End switch
            } // End innerloop
        } // End outerloop


        // Evaluation

        double materialWeight = 0.4;
        double positionWeight = 0.6;

        int materialDifference = whiteMaterial - blackMaterial;
        // Dynamic weighting based on material imbalance
        // Calculating the weightage
        // The bigger the material difference, the less the position matters
        // The smaller the material difference, the more position matters
        if (materialDifference > 8 || materialDifference < -8) { // material difference for > 8 points difference
            materialWeight = 0.9;
            positionWeight = 0.1;
        } else if (materialDifference > 6 || materialDifference < -6) { // material difference for > 6 points difference
            materialWeight = 0.7;
            positionWeight = 0.3;
        } else if (materialDifference > 4 || materialDifference < -4) { // material difference for > 4 points difference
            materialWeight = 0.5;
            positionWeight = 0.5;
        } else if (materialDifference > 1 || materialDifference < -1) { // material difference for > 1 points difference
            materialWeight = 0.3;
            positionWeight = 0.7;
        }


        // Calculating material difference values
        // Sigmoid function:
        // y = 1/(1.1 + e^(-x+4)) { 40 > x > 0 }
        double materialValue;
        if(materialDifference > 0) {                                           // POSITIVE Material Diff
            materialValue = 1.0 / (1.1 + Math.exp(-(materialDifference) + 4));
        } else if(materialDifference < 0) {
            materialDifference *= -1;                                          // NEGATIVE Material Diff
            materialValue = 1.0 / (1.1 + Math.exp(-(materialDifference) + 4));
            materialValue *= -1.0;
        } else {                                                               // EQUAL Material Diff
            materialValue = 0.0;
        }
        materialValue *= materialWeight;

        // Calculating the positional difference values
        final double positionScalar = 1169.0; // this is the highest amount of points possible given a full board
        double whitePositionValue = (double) (whitePosition / positionScalar) * positionWeight;
        double blackPositionValue = (double) (blackPosition / positionScalar) * positionWeight;
        double evaluation = (whitePositionValue - blackPositionValue + materialValue);

        game.setEvaluation(evaluation);
        return game.getEvaluation();
    } // end evaluate

    // multithreading function
    public ArrayList<Move> updateMoves(Game game) {
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
        ArrayList<Move> moves = new ArrayList<>();

        for(int i=0; i<8 ;i++) {
            for (int j = 0; j < 8; j++) {
                movesIndexed++;
                Piece piece = board[i][j];
                String pieceName = piece.getName();
                char color = piece.getColor();

                if (activeColor == color) {

                    switch (pieceName) {

                        case "pawn" -> {
                            if (activeColor == color) {

                            /*
                            Pawns are the only uni-directional piece depending on the
                            Player color, so this is way you have to split it between
                            White and black

                            */
                                // ***************************** White Pawn moving *****************************

                                if (color == 'w') {

                                    // Forward one
                                    if (j < 6 /*promote is a different check*/) {
                                        int x = i, y = j + 1; // coords interested in
                                        if (board[x][y].isEmpty()) {
                                            // Can move forward one
                                            Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }
                                    // Forward two if on the starting position
                                    if (j < 6 && j == 1 /*promote is a different check*/) {
                                        int x = i, y = j + 2; // coords interested in
                                        if (board[i][j + 1].isEmpty() && board[x][y].isEmpty()) {
                                            // Can move forward two
                                            Move move = new Move(i, j, x, y, "Moved_Twice", "", i, (j + y) / 2, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }

                                    // Attacking to the left (A pawn cannot attack left)
                                    // Includes if it can attack the en passant square
                                    if (i > 0 && j < 6 /*Promote is a different case*/) {
                                        int x = i - 1, y = j + 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            // Can attack left
                                            String enemyName = board[x][y].getName();
                                            Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }

                                        if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {
                                            Move move = new Move(i, j, x, y, "En_Passant_Capture", "pawn", x, y, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);

                                            }
                                        }

                                    }

                                    //Attacking to the right (H pawn cannot attack right)
                                    // Includes if it can attack the en passant
                                    if (i < 7 && j < 6) {
                                        int x = i + 1, y = j + 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            String enemyName = board[x][y].getName();
                                            Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                        if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {

                                            Move move = new Move(i, j, x, y, "En_Passant_Capture", "pawn", x, y, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            } else {
                                            }
                                        }
                                    }

                                    // Promote via moving
                                    if (j == 6) {
                                        int x = i, y = j + 1; // coords interested in
                                        if (board[x][y].isEmpty()) {
                                            // Can promote to queen, rook, bishop, knight
                                            Move move1 = new Move(i, j, x, y, "Promote", "", x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote", "", x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote", "", x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote", "", x, y, "knight");

                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);
                                            }
                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
                                                moves.add(move4);
                                            }

                                        }
                                    }

                                    // Promote via capturing to the left (A pawn cannot attack left)
                                    if (i > 0 && j == 6) {
                                        int x = i - 1, y = j + 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            // Can attack left
                                            String enemyName = board[x][y].getName();
                                            Move move1 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "knight");
                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);
                                            }
                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
                                                moves.add(move4);
                                            }
                                        }
                                    }

                                    // Promote via capturing to the right (H pawn cannot attack right)
                                    //Attacking to the right (H pawn cannot attack right)
                                    if (i < 7 && j == 6) {
                                        int x = i + 1, y = j + 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            // Can attack right
                                            String enemyName = board[x][y].getName();
                                            Move move1 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "knight");
                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);
                                            }
                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
                                                moves.add(move4);
                                            }
                                        }
                                    }

                                    // END White Pawn's code
                                } else {

                                    // ***************************** Black Pawn moving *****************************

                                    // Forward one
                                    if (j > 1 /*promote is a different check */) {
                                        int x = i, y = j - 1; // coords interested in
                                        if (board[x][y].isEmpty()) {
                                            // can move forward one
                                            Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }

                                    // Forward two if on the starting position
                                    if (j > 1 && j == 6) {
                                        int x = i, y = j - 2; // coords interested in
                                        if (board[i][j - 1].isEmpty() && board[x][y].isEmpty()) {
                                            // can move forward two
                                            Move move = new Move(i, j, x, y, "Moved_Twice", "", i, (j + y) / 2, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }

                                    // Attacking to the left (A pawn cannot attack to the left) (from white perspective)
                                    // Includes if it can attack the en passant square
                                    if (i > 0 && j > 1) {
                                        int x = i - 1, y = j - 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            // can attack left
                                            String enemyName = board[x][y].getName();
                                            Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }

                                        if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {
                                            Move move = new Move(i, j, x, y, "En_Passant_Capture", "pawn", x, y, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }

                                    // Attacking to the right (H pawn cannot attack to the right) (from white perspective)
                                    // Includes it if can attack the en passant square

                                    if (i < 7 && j > 1) {
                                        int x = i + 1, y = j - 1;
                                        if (board[x][y].isEnemy(color)) {
                                            // can attack right
                                            String enemyName = board[x][y].getName();
                                            Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }

                                        if (enPassantX == (x) && enPassantY == (y) && board[x][y].isEnemy(color)) {
                                            Move move = new Move(i, j, x, y, "En_Passant_Capture", "pawn", x, y, "");
                                            if (checkIfMoveIsValid(game, move)) {
                                                moves.add(move);
                                            }
                                        }
                                    }

                                    // Promote via moving

                                    if (j == 1) {
                                        int x = i, y = j - 1;
                                        if (board[x][y].isEmpty()) {
                                            // Can promote to queen ,rock, bishop knight
                                            Move move1 = new Move(i, j, x, y, "Promote", "", x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote", "", x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote", "", x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote", "", x, y, "knight");
                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);
                                            }
                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
                                                moves.add(move4);
                                            }
                                        }
                                    }

                                    // Promote via capturing to the left (A pawn cannot attack to the left)
                                    if (i > 0 && j == 1) {
                                        int x = i - 1, y = j - 1; // coords interested in
                                        if (board[x][y].isEnemy(color)) {
                                            // can attack left to promote
                                            String enemyName = board[x][y].getName();
                                            Move move1 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "knight");
                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);
                                            }
                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
                                                moves.add(move4);
                                            }
                                        }
                                    }


                                    // Promote via capturing to the right (H pawn cannot attack to the right) (White's perspective)
                                    if (i > 0 && j == 1) {
                                        int x = i + 1, y = j - 1; // coords interested in
                                        if ( x >= 0 && x <=7 && y >=0 && y <= 7 && board[x][y].isEnemy(color)) {
                                            // can attack right to promote
                                            String enemyName = board[x][y].getName();
                                            Move move1 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "queen");
                                            Move move2 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "rook");
                                            Move move3 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "bishop");
                                            Move move4 = new Move(i, j, x, y, "Promote_Capture", enemyName, x, y, "knight");
                                            if (checkIfMoveIsValid(game, move1)) {
                                                moves.add(move1);

                                            }

                                            if (checkIfMoveIsValid(game, move2)) {
                                                moves.add(move2);
                                            }
                                            if (checkIfMoveIsValid(game, move3)) {
                                                moves.add(move3);
                                            }
                                            if (checkIfMoveIsValid(game, move4)) {
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
                            while (x < 7 && y < 7) {
                                x++;
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0 && y < 7) {
                                x--;
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x < 7 && y > 0) {
                                x++;
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0 && y > 0) {
                                x--;
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            y += 2;
                            if (x <= 7 && y <= 7) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }


                            // Left
                            x = i;
                            y = j;
                            x--;
                            y += 2;
                            if (x >= 0 && y <= 7) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }


                            // TWO DOWN (LEFT / RIGHT)
                            x = i;
                            y = j;
                            // Right
                            x++;
                            y -= 2;
                            if (x <= 7 && y >= 0) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }

                            // Left
                            x = i;
                            y = j;
                            x--;
                            y -= 2;
                            if (x >= 0 && y >= 0) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }

                            // TWO RIGHT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x += 2;
                            y++;
                            if (x <= 7 && y <= 7) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x += 2;
                            y--;
                            if (x <= 7 && y >= 0) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }
                            // TWO LEFT (UP/DOWN)
                            x = i;
                            y = j;
                            // up
                            x -= 2;
                            y++;
                            if (x >= 0 && y <= 7) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }
                            }

                            // down
                            x = i;
                            y = j;
                            x -= 2;
                            y--;
                            if (x >= 0 && y >= 0) {

                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
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
                            while (x < 7) {
                                x++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0) {
                                x--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }

                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (y < 7) {
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (y > 0) {
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x < 7) {
                                x++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0) {
                                x--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }

                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (y < 7) {
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks rook's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (y > 0) {
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x < 7 && y < 7) {
                                x++;
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0 && y < 7) {
                                x--;
                                y++;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x < 7 && y > 0) {
                                x++;
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            while (x > 0 && y > 0) {
                                x--;
                                y--;
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                } else {
                                    // Piece blocks bishop's way
                                    if (board[x][y].isEnemy(color)) {
                                        String enemyName = board[x][y].getName();
                                        Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                        if (checkIfMoveIsValid(game, move)) {
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
                            if (x <= 7) {


                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }


                            }

                            x = i;
                            y = j;
                            x--;
                            if (x >= 0 && (board[x][y].isEmpty() || board[x][y].isEnemy(color))) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }

                            x = i;
                            y = j;
                            y++;
                            if (y <= 7) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }

                            x = i;
                            y = j;
                            y--;
                            if (y >= 0) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
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
                            if (x <= 7 && y <= 7) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }

                            // TOP LEFT
                            x = i;
                            y = j;
                            x--;
                            y++;
                            if (x >= 0 && y <= 7) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }

                            // Bottom RIGHT
                            x = i;
                            y = j;
                            x++;
                            y--;
                            if ((x <= 7 && y >= 0)) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }
                            // Bottom LEFT
                            x = i;
                            y = j;
                            x--;
                            y--;
                            if (x >= 0 && y >= 0) {
                                if (board[x][y].isEmpty()) {
                                    Move move = new Move(i, j, x, y, "Move", "", -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                                if (board[x][y].isEnemy(color)) {
                                    String enemyName = board[x][y].getName();
                                    Move move = new Move(i, j, x, y, "Capture", enemyName, -1, -1, "");
                                    if (checkIfMoveIsValid(game, move)) {
                                        moves.add(move);
                                    }
                                }

                            }
                            // CASTLING RIGHTS
                            if (color == 'w') {

                                if (whiteCastleKingSide) {
                                    if (board[5][0].isEmpty() && board[6][0].isEmpty()) {

                                        if (!isSquareInCheck(game, 4, 0) && !isSquareInCheck(game, 5, 0) &&
                                                !isSquareInCheck(game, 6, 0)) {
                                            Move move = new Move(4, 0, 6, 0, "Castle_KingSide", "", -1, -1, "");
                                            moves.add(move);
                                        }

                                    }
                                }
                                if (whiteCastleQueenSide) {
                                    if (board[3][0].isEmpty() && board[2][0].isEmpty() && board[1][0].isEmpty()) {
                                        if (!isSquareInCheck(game, 1, 0) &&
                                                !isSquareInCheck(game, 2, 0) && !isSquareInCheck(game, 3, 0) && !isSquareInCheck(game, 4, 0)) {
                                            Move move = new Move(4, 0, 2, 0, "Castle_QueenSide", "", -1, -1, "");
                                            moves.add(move);
                                        }
                                    }
                                }
                            } else {

                                if (blackCastleKingSide) {
                                    if (board[5][7].isEmpty() && board[6][7].isEmpty()) {
                                        if (!isSquareInCheck(game, 4, 7) && !isSquareInCheck(game, 5, 7) &&
                                                !isSquareInCheck(game, 6, 7)) {
                                            Move move = new Move(4, 7, 6, 7, "Castle_KingSide", "", -1, -1, "");
                                            moves.add(move);
                                        }
                                    }
                                }
                                if (blackCastleQueenSide) {
                                    if (board[3][7].isEmpty() && board[2][7].isEmpty() && board[1][7].isEmpty()) {
                                        if (!isSquareInCheck(game, 1, 7) &&
                                                !isSquareInCheck(game, 2, 7) && !isSquareInCheck(game, 3, 7) && !isSquareInCheck(game, 4, 7)) {
                                            Move move = new Move(4, 7, 2, 7, "Castle_QueenSide", "", -1, -1, "");
                                            moves.add(move);
                                        }
                                    }
                                }
                            }

                        } // End case kING

                    } // end switch
                } // end if(activeColor)
            } // END INNERLOOP
        } // END OUTERLOOP
        return moves;
    } // END UPDATEMOVES


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

        if(color == 'w') {
            move.setPreviousKingCastleState(game.getWhiteCastleKingSide());
            move.setPreviousQueenCastleState(game.getWhiteCastleQueenSide());
        } else {
            move.setPreviousKingCastleState(game.getBlackCastleKingSide());
            move.setPreviousQueenCastleState(game.getBlackCastleQueenSide());
        }

        move.setPreviousEnPassantX(game.getEnPassantX());
        move.setPreviousEnPassantY(game.getEnPassantY());

        // Set the en passant to nothing by default, and will be updated if needed below
        game.setEnPassant("-");
        game.setEnPassantX(-1);
        game.setEnPassantX(-1);


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
            }

            case "Moved_Twice" -> {
                newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                newBoard[startX][startY] = new Piece();
                game.setEnPassantXY(move.getEnPassantX(), move.getEnPassantY());
                game.setEnPassantX(move.getEnPassantX());
                game.setEnPassantY(move.getEnPassantY());
            }

            case "Capture" -> {
                newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                newBoard[startX][startY] = new Piece();
            }

            case "En_Passant_Capture" -> {
                newBoard[endX][endY].copyPiece(newBoard[startX][startY]);
                newBoard[startX][startY] = new Piece();

                // information about the enemy pawn is not stored

                if(color == 'w') {
                    newBoard[endX][endY-1] = new Piece();
                } else {
                    newBoard[endX][endY+1] = new Piece();
                }

                game.setHalfMoveClock(-1);
            }

            case "Promote" -> {
                newBoard[endX][endY] = new Piece(move.getPromotePieceTo(), color);
                newBoard[startX][startY] = new Piece();
            }

            case "Promote_Capture" -> {
                newBoard[endX][endY] = new Piece(move.getPromotePieceTo(), color);
                newBoard[startX][startY] = new Piece();
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
            }

            default-> {
                System.out.println("Error unrecognized move");
            }
        } // End switch



        game.incrementHalfMoveClock();
        game.incrementFullMoveClock();
        return game;
    } // end makeMove
    public Game revertMove(Game game, Move move) {
        //Exact opposite of the makeMove function
        Piece[][] newBoard = game.getBoard();
        char color = game.getActiveColor();
        char enemyColor;
        // we assume that the correct color as already been flipped
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

            }

            case "Moved_Twice" -> {
                newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                newBoard[endX][endY] = new Piece();

            }

            case "Capture" -> {
                newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                newBoard[endX][endY] = new Piece(move.getCapturedPiece(),enemyColor);

            }

            case "En_Passant_Capture" -> {
                newBoard[startX][startY].copyPiece(newBoard[endX][endY]);
                newBoard[endX][endY] = new Piece();

                // information about the enemy pawn is not stored

                if(color == 'w') {
                    newBoard[endX][endY-1] = new Piece("pawn",'b');
                } else {
                    newBoard[endX][endY+1] = new Piece("pawn",'w');
                }

                game.setHalfMoveClock(-1);
            }

            case "Promote" -> {
                newBoard[endX][endY] = new Piece();
                newBoard[startX][startY] = new Piece("pawn", color);
            }

            case "Promote_Capture" -> {
                newBoard[endX][endY] = new Piece(move.getCapturedPiece(),enemyColor);
                newBoard[startX][startY] = new Piece("pawn", color);
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
            }

            case "Castle_QueenSide" -> {
                if(color == 'w') {
                    newBoard[4][0] = new Piece("king", 'w');
                    newBoard[0][0] = new Piece("rook", 'w');
                    newBoard[2][0] = new Piece();
                    newBoard[3][0] = new Piece();

                } else {
                    newBoard[4][7] = new Piece("king", 'b');
                    newBoard[0][7] = new Piece("rook", 'b');
                    newBoard[2][7] = new Piece();
                    newBoard[3][7] = new Piece();

                }
            }

            default-> {
                System.out.println("Error unrecognized move");
            }
        } // End switch

        // Restore any states from the move being executed

        if(color == 'w') {
            game.setWhiteCastleQueenSide(move.getPreviousKingCastleState());
            game.setWhiteCastleKingSide(move.getPreviousQueenCastleState());
        } else {

            game.setBlackCastleKingSide(move.getPreviousKingCastleState());
            game.setBlackCastleQueenSide(move.getPreviousQueenCastleState());
        }

        game.setEnPassantXY(move.getPreviousEnPassantX() ,move.getPreviousEnPassantY());
        game.setEnPassantX(move.getPreviousEnPassantX());
        game.setEnPassantY(move.getPreviousEnPassantY());

        game.revertHalfMoveClock();
        game.revertFullMoveClock();
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


    public double findBestMove(Game game, int depth, double alpha, double beta, boolean isMaximising) {

        gamesSearched++;


        if(depth == maxDepth) {
            return evaluate(game);
        }

        ArrayList<Move> moves;
        if(depth == 0) {
            moves = game.moves;
            // game.moves are the moves for the initial board state ONLY
        } else {
            moves = updateMoves(game);
        }

        // Checking if the leaf is a terminal node
        if(moves.isEmpty()) {
            int terminate = isGameOver(game, moves);
            if (terminate != 403) {  // added a nested loop for added efficiency so there isn't always checks for the other 3 conditions
                if (terminate == -1 || terminate == 0 || terminate == 1) {
                    return terminate;
                }
            }
        }
        // Minimax algorithm

        if(isMaximising) {

            double bestValue = Double.NEGATIVE_INFINITY;
            for(int i=0; i<moves.size(); i++) {

                Move a = moves.get(i);
                //System.out.println("Best Move: " + game.getBoard()[a.getStartX()][a.getStartY()].getName() + " from " + a.toChar(a.getStartX()) + (a.getStartY()+1) + " moves to " + a.toChar(a.getEndX()) + (a.getEndY()+1));
                makeMove(game,moves.get(i));
                game.flipColor();
//                printBoardState(game);
                double tempValue  = findBestMove(game,depth+1, alpha, beta, false);
                game.flipColor();
                revertMove(game,moves.get(i));
                if(tempValue >= bestValue) {
                    bestValue = tempValue;
                    if(depth == 0) {
                        bestMoveIndex = i;
                    }
                }
                if(alpha >= bestValue) {
                    alpha = bestValue;
                }
                // Pruning
                if(beta <= alpha) {
                    break;
                }
            }
            return bestValue;




        } else {
            double leastValue = Double.POSITIVE_INFINITY;
            for (int i = 0; i < moves.size(); i++) {

                Move a = moves.get(i);
                //System.out.println("Best Move: " + game.getBoard()[a.getStartX()][a.getStartY()].getName() + " from " + a.toChar(a.getStartX()) + (a.getStartY()+1) + " moves to " + a.toChar(a.getEndX()) + (a.getEndY()+1));
                makeMove(game, moves.get(i));
                game.flipColor();
//                printBoardState(game);
                double tempValue = findBestMove(game, depth + 1, alpha, beta, true);
                game.flipColor();
                revertMove(game,moves.get(i));
                leastValue = Math.min(leastValue, tempValue);
                beta = Math.min(beta, leastValue);

                if(beta <= alpha) {
                    break;
                }
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
                char c = board[j][i].getName().charAt(0);
                if(board[j][i].getColor() == 'w') {
                    c = Character.toUpperCase(c);
                }
                System.out.print("\t " +  c + " \t");
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
                    case 0 ->   startFile = "a";
                    case 1 ->   startFile = "b";
                    case 2 ->   startFile = "c";
                    case 3 ->   startFile = "d";
                    case 4 ->   startFile = "e";
                    case 5 ->   startFile = "f";
                    case 6 ->   startFile = "g";
                    case 7 ->   startFile = "h";
                }
                switch(endX) {
                    case 0 ->  endFile = "a";
                    case 1 ->  endFile = "b";
                    case 2 ->  endFile = "c";
                    case 3 ->  endFile = "d";
                    case 4 ->  endFile = "e";
                    case 5 ->  endFile = "f";
                    case 6 ->  endFile = "g";
                    case 7 ->  endFile = "h";
                }

                switch(moves.get(i).getMoveType()) {

                    case "Move" -> {
                        System.out.println(pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY);
                    }

                    case "Moved_Twice" -> {
                        System.out.println(pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY);
                    }

                    case "Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY);
                    }

                    case "En_Passant_Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " (En Passant)");
                    }

                    case "Promote" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "promotes to " + promotePieceTo + " " + endFile + endY);
                    }

                    case "Promote_Capture" -> {
                        System.out.println(pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " and promotes to " + promotePieceTo);
                    }

                    case "Castle_KingSide" -> {
                        System.out.println("Castle King Side");
                    }

                    case "Castle_QueenSide" -> {
                        System.out.println("Castle Queen Side");
                    }

                    default-> {
                        System.out.println("Unrecognized Move");
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
    public String getGamesSearched() { return String.valueOf(gamesSearched); }


} // END ENGINE