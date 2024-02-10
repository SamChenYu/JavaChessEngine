
package chessengine;

import GUI.EnginePanel;
import Game.Piece;
import Game.Game;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class Engine {

    // Objects
    private final Game game;
    private final EnginePanel ep;
    MovesGenerator mg;


    // Minimax
    private final int maxDepth = 4;
    private int bestMoveIndex = -1, secondBestMoveIndex= -1, thirdBestMoveIndex = -1;
    private Move move1, move2, move3;
    private Integer gamesSearched = 0;


    // Multi threading
    private final ExecutorService executorService;



    //Piece-Square Tables from
    // https://www.chessprogramming.org/PeSTO%27s_Evaluation_Function
    // http://www.talkchess.com/forum3/viewtopic.php?f=2&t=68311&start=19#

    // The Piece-Square Tables have been flipped to be consistent with the board
    private final int[][] whitePawnPST = {
            // white side here
            { 0,   0,   0,   0,   0,   0,  0,   0},
            {-35,  -1, -20, -23, -15,  24, 38, -22},
            {-26,  -4,  -4, -10,   3,   3, 33, -12},
            {-27,  -2,  -5,  12,  17,   6, 10, -25},
            {-14,  13,   6,  21,  23,  12, 17, -23},
            {-6,   7,  26,  31,  65,  56, 25, -20},
            {98, 134,  61,  95,  68, 126, 34, -11},
            { 0,   0,   0,   0,   0,   0,  0,   0},
            // black side here
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
            {-15,  36,  12, -54, 8, -28,  24,  14},
    };

    public Engine(String input, EnginePanel ep) {
        // Create a thread pool with a fixed number of threads
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);
        this.ep = ep;

        game = new Game(input);
        mg = new MovesGenerator(game, this);
        evaluate(game);
        printBoardState(game);

    }

    public void startSearch() {
        gamesSearched = 0;
        game.moves.clear();

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
        game.moves = mg.updateMoves(game);
        printMoves(game);
        double lowestValue = -100000.0;
        double highestValue = 100000.0;

        if(game.getActiveColor() == 'w') {
            findBestMove(game,0,lowestValue,highestValue, true);
        } else {
            findBestMove(game,0,lowestValue,highestValue, false);
        }


        System.out.println("Games searched: " + gamesSearched );
        double timeEnd = System.currentTimeMillis();
        double totalTime = (timeEnd - timeStart) / 1000;
        System.out.println("Total Time: " + totalTime + " seconds for " + gamesSearched + " games");

        // Prints out best move
        move1 = game.moves.get(bestMoveIndex);
        if(secondBestMoveIndex == -1) {
            move2 = game.moves.get(bestMoveIndex);
        } else {
            move2 = game.moves.get(secondBestMoveIndex);
        }
        if(thirdBestMoveIndex == -1) {
            move3 = game.moves.get(bestMoveIndex);
        } else {
            move3 = game.moves.get(thirdBestMoveIndex);
        }

        System.out.println(sendMoveToPanel(move1));

        ep.updateMove1(sendMoveToPanel(move1));
        ep.updateMove2(sendMoveToPanel(move2));
        ep.updateMove3(sendMoveToPanel(move3));
        bestMoveIndex = -1; secondBestMoveIndex = -1; thirdBestMoveIndex = -1;
        //shutdown();


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
                Piece piece = board[j][i];
                switch(piece.getName()) {

                    case "-" -> { continue; }

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
    public void revertMove(Game game, Move move) {
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
    } // End revertMove

    public double findBestMove(Game game, int depth, double alpha, double beta, boolean isMaximising) {

        gamesSearched++;
        ep.updateGamesSearched(gamesSearched);

        if(depth == maxDepth) {
            return evaluate(game);
        }

        ArrayList<Move> moves;
        if(depth == 0) {
            moves = game.moves;
            // game.moves are the moves for the initial board state ONLY
        } else {
            moves = mg.updateMoves(game);
        }

        // Checking if the leaf is a terminal node
        if(moves.isEmpty()) {
            int terminate = mg.isGameOver(game, moves);
            if (terminate != 403) {  // added a nested loop for added efficiency so there isn't always checks for the other 3 conditions
                if (terminate == -1 || terminate == 0 || terminate == 1) {
                    return terminate;
                }
            }
        }
        // Minimax algorithm

        if(isMaximising) {

            double bestValue = Double.NEGATIVE_INFINITY;
            double secondBestValue = Double.NEGATIVE_INFINITY + 1.0;
            double thirdBestValue = Double.NEGATIVE_INFINITY + 2.0;
            for(int i=0; i<moves.size(); i++) {

                Move a = moves.get(i);
                //System.out.println("Best Move: " + game.getBoard()[a.getStartX()][a.getStartY()].getName() + " from " + a.toChar(a.getStartX()) + (a.getStartY()+1) + " moves to " + a.toChar(a.getEndX()) + (a.getEndY()+1));
                makeMove(game,moves.get(i));
                game.flipColor();
//               printBoardState(game);
                double tempValue  = findBestMove(game,depth+1, alpha, beta, false);
                game.flipColor();
                revertMove(game,moves.get(i));


                if(tempValue >= bestValue) {
                    thirdBestValue = secondBestValue;
                    secondBestValue = bestValue;
                    bestValue = tempValue;

                    if(depth == 0) {
                        thirdBestMoveIndex = secondBestMoveIndex;
                        secondBestMoveIndex = bestMoveIndex;
                        bestMoveIndex = i;
                    }
                } else if (tempValue >= secondBestValue) {
                    thirdBestValue = secondBestValue;
                    secondBestValue = tempValue;

                    if(depth == 0) {
                        thirdBestMoveIndex = secondBestMoveIndex;
                        secondBestMoveIndex = i;
                    }
                } else if (tempValue >= thirdBestValue) {
                    thirdBestValue = tempValue;
                    if(depth == 0) {
                        thirdBestMoveIndex = i;
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
            double secondLeastValue = Double.POSITIVE_INFINITY + 1.0;
            double thirdLeastValue = Double.POSITIVE_INFINITY + 2.0;
            for (int i = 0; i < moves.size(); i++) {

                Move a = moves.get(i);
                makeMove(game, moves.get(i));
                game.flipColor();
                double tempValue = findBestMove(game, depth + 1, alpha, beta, true);
                game.flipColor();
                revertMove(game,moves.get(i));

                if(tempValue <= leastValue) {
                    thirdLeastValue = secondLeastValue;
                    secondLeastValue = leastValue;
                    leastValue = tempValue;
                    if(depth == 0) {
                        thirdBestMoveIndex = secondBestMoveIndex;
                        secondBestMoveIndex = bestMoveIndex;
                        bestMoveIndex = i;
                    }
                } else if (tempValue <= secondLeastValue) {
                    thirdLeastValue = secondLeastValue;
                    secondLeastValue = i;
                    if(depth == 0) {
                        secondBestMoveIndex = i;
                    }
                } else if (tempValue <= thirdLeastValue) {
                    thirdLeastValue = tempValue;
                    if(depth == 0) {
                        thirdBestMoveIndex = i;
                    }
                }
                //alpha = Math.max(alpha, leastValue); // not sure if this is the correct once so imma leave it here lol
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

    // Methods used to connect to EnginePanel
    public String sendMoveToPanel(Move move) {
        // this method sends out to the enginepanel to update

        String startFile ="";
        int startX = move.getStartX();
        int startY = move.getStartY();

        String endFile ="";
        int endX = move.getEndX();
        int endY = move.getEndY();

        String pieceName = game.getBoard()[startX][startY].getName();
        String promotePieceTo = move.getPromotePieceTo();

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

        switch(move.getMoveType()) {

            case "Move" -> {
                return pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY;
            }

            case "Moved_Twice" -> {
                return pieceName + " " + startFile + startY  + " " + "moves to " + endFile + endY;
            }

            case "Capture" -> {
                return pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY;
            }

            case "En_Passant_Capture" -> {
                return pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " (En Passant)";
            }

            case "Promote" -> {
                return pieceName + " " + startFile + startY + " " + "promotes to " + promotePieceTo + " " + endFile + endY;
            }

            case "Promote_Capture" -> {
                return pieceName + " " + startFile + startY + " " + "captures at " + endFile + endY + " and promotes to " + promotePieceTo;
            }

            case "Castle_KingSide" -> {
                return "Castle King Side";
            }

            case "Castle_QueenSide" -> {
                return "Castle Queen Side";
            }

            default-> {
                return "Unrecognized Move";
            }
        } // End switch
    }

    public void makeMove1() {
        if(move1 != null) {
            makeMove(game,move1);
            game.setEvaluation(evaluate(game));
            game.flipColor();
        }
    }
    public void makeMove2() {
        if(move2 != null) {
            makeMove(game,move2);
            game.setEvaluation(evaluate(game));
            game.flipColor();
        }
    }
    public void makeMove3() {
        if(move3 != null) {
            makeMove(game,move3);
            game.setEvaluation(evaluate(game));
            game.flipColor();
        }
    }



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