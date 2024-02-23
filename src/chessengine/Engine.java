
package chessengine;

import GUI.EnginePanel;
import Game.*;
import Move.Move;
import Move.MovesGenerator;

import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Engine {

    // Objects
    private final Game game;
    private final EnginePanel ep;
    MovesGenerator mg;

    double timeToEval = 0;



    // Minimax
    private int maxDepth = 2;
    private int bestMoveIndex = -1, secondBestMoveIndex= -1, thirdBestMoveIndex = -1;
    private Move move1, move2, move3;
    private Integer gamesSearched = 0;


    // Multi threading
    private final ExecutorService executorService;

    public Engine(String input, EnginePanel ep) {
        // Create a thread pool with a fixed number of threads
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);
        this.ep = ep;



        mg = new MovesGenerator(this);
        game = new Game(mg, input);
        mg.initGame(game);
        evaluate(game);
        printBoardState(game);

    }


    public void startSearch() {
        maxDepth = ep.getDepth();
        timeToEval = 0;
        gamesSearched = 0;
        game.moves.clear();

         //finding legal moves
//        double timeStart = System.currentTimeMillis();
//        game.moves = mg.updateMoves(game);
//        double timeEnd = System.currentTimeMillis();
//        double totalTime = (timeEnd - timeStart) /1000.0;
//        System.out.println("Total Time: " + totalTime);
//        printMoves(game);
//        printBoardState(game);


        // Activates Minimax
        double timeStart = System.currentTimeMillis();
        game.moves = mg.updateMoves(game);
        printMoves(game);
        double lowestValue = -100000.0;
        double highestValue = 100000.0;

        findBestMove(game,0,lowestValue,highestValue, game.getActiveColor() == 'w');


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
        double timeStart = System.currentTimeMillis();
        Piece[][] board = game.getBoard();

        int whitePosition = 0;
        int blackPosition = 0;

        int whiteMaterial = 0;
        int blackMaterial = 0;
        int totalMaterial = 0;

        for(int i=0; i<8;i++) {
            for(int j=0; j<8;j++){
                Piece piece = board[j][i];

                if(piece.getColor() == 'w') {
                    whiteMaterial += piece.getValue();
                    whitePosition += piece.getPSTValue(i,j);
                } else {
                    blackMaterial += piece.getValue();
                    blackPosition += piece.getPSTValue(i,j);
                }
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
        double timeEnd = System.currentTimeMillis();
        timeToEval += (timeEnd - timeStart);
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

        // applying the move to the board
        // the move will be able to make the move itself
        game = move.makeMove(game);

        game.incrementHalfMoveClock();
        game.incrementFullMoveClock();
        return game;
    } // end makeMove
    public void revertMove(Game game, Move move) {
        //Exact opposite of the makeMove function
        char color = game.getActiveColor();


        // Dichotmy of moves
        game = move.revertMove(game);
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


        if(game.getHalfMoveClock() > 0) {
            game.revertHalfMoveClock();
        }
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

                Move a = moves.get(i);makeMove(game,moves.get(i));
                game.flipColor();
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

                System.out.println(moves.get(i).printMove(game));

            } // End loop
        }
    } // End printMoves













    // Methods used to connect to EnginePanel
    public String sendMoveToPanel(Move move) {
        // this method returns the move in a string format for the GUI
        return move.printMove(game);
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

    public void moveFromPanel(int startX, int startY, int endX, int endY) {

        // uhhh so somewhere the representation of the board is weird
        // so the Y values are upside down so i'm just manually replacing it lol
        switch(startY) {
            case 0 -> startY = 7;
            case 1 -> startY = 6;
            case 2 -> startY = 5;
            case 3 -> startY = 4;
            case 4 -> startY = 3;
            case 5 -> startY = 2;
            case 6 -> startY = 1;
            case 7 -> startY = 0;
        }
        switch(endY) {
            case 0 -> endY = 7;
            case 1 -> endY = 6;
            case 2 -> endY = 5;
            case 3-> endY = 4;
            case 4 -> endY = 3;
            case 5 -> endY = 2;
            case 6-> endY = 1;
            case 7-> endY = 0;
        }

        Move move = new Move(startX, startY, endX, endY);
        makeMove(game,move);
        game.flipColor();
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