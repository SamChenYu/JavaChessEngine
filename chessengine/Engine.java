
package chessengine;

public final class Engine {
    
    private Piece[][] board = new Piece[8][8];
    private char activeColor = ' ';
    private boolean whiteCastleKingSide = false;
    private boolean whiteCastleQueenSide = false;
    private boolean blackCastleKingSide = false;
    private boolean blackCastleQueenSide = false;
    private String enPassant = "-";
    int halfMoveClock = 0; // turns since last capture for 50 move rule
    int fullMoveClock = 1; // full amount of turns taken
    
    private double evaluation = 0;
    
    public Engine(String input) {
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = new Piece();
            }
        }
        
        processString(input);
        evaluate(board);
        
    }
    public void processString(String input) {

        // Split the FEN string using space as the delimiter
        String[] fenParts = input.split(" ");
        
        activeColor = fenParts[1].charAt(0);
        
        for(int i=0; i<fenParts[2].length(); i++) {
            char currentChar = fenParts[2].charAt(i);
            switch(currentChar) {
                case 'K' -> { whiteCastleKingSide = true; break; }
                case 'Q' -> { whiteCastleQueenSide = true; break; }
                case 'k' -> { blackCastleKingSide = true; break; }
                case 'q' -> { blackCastleQueenSide = true; break; }
            }
        }
        enPassant = fenParts[3];
        halfMoveClock = Integer.parseInt(fenParts[4]);
        fullMoveClock = Integer.parseInt(fenParts[5]);
        
        String[] boardRows = fenParts[0].split("/");

        outerLoop:
        for(int i=0; i<8; i++) {
            //ITERATING FOR 8 ROWS
            int squareCounter = 0; // This keeps track of which square is currently being updated
            for(int j=0; j<boardRows[i].length(); j++) {
                //ITERATING FOR However Long the String given in the input is
                char currentChar = boardRows[i].charAt(j);
                switch(currentChar) {
                    case 'r' -> {
                        // black rook
                        board[i][squareCounter] = new Piece("rook", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'n' -> {
                        // black knight
                        board[i][squareCounter] = new Piece("knight", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'b' -> {
                        // black bishop
                        board[i][squareCounter] = new Piece("bishop", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'q' -> {
                        // black queen
                        board[i][squareCounter] = new Piece("queen", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'k' -> {
                        // black king
                        board[i][squareCounter] = new Piece("king", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'p' -> {
                        // black pawn
                        board[i][squareCounter] = new Piece("pawn", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'P' -> {
                        // white pawn
                        board[i][squareCounter] = new Piece("pawn", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'K' -> {
                        // white king
                        board[i][squareCounter] = new Piece("king", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'Q' -> {
                        //white queen
                        board[i][squareCounter] = new Piece("queen", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'B' -> {
                        // white bishop
                        board[i][squareCounter] = new Piece("bishop", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'N' -> {
                        // white knight
                        board[i][squareCounter] = new Piece("knight", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'R' -> {
                        // white rook
                        board[i][squareCounter] = new Piece("rook", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    // Handle empty squares (represented by numbers)
                    case '1', '2', '3', '4', '5', '6', '7', '8' -> {
                        // Convert the character to an integer and skip that number of squares
                        int emptySquares = Character.getNumericValue(currentChar);
                        squareCounter += emptySquares; // Subtract 1 because the loop will increment j
                        break;
                    }
                    
                    default -> {
                        System.out.println("something went wrong with the FEN notation processing");
                        // For the non-valid input, the board is set to a normal state
                        processString("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                        break outerLoop;
                    }
                } // END SWITCH
            } // END INNER LOOP
        } // END OUTER LOOP
    
        
        
    } // END PROCESS STRING
    public void printBoardState() {
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
    
    
    
    
    /*
    
    
    ALRIGHTY TIME FOR THE MINIMAX
    
    
    */
    
    
    
    public void updateMoves() {
        
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
            PROMOTE
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
        
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                
                
                
                
                
            } // END INNERLOOP
        } // END OUTERLOOP
        
        
        
        
        
    } // END UPDATEMOVES
    
    // Getters and Setters
    public Piece[][] getBoard() { return board; }
    public char getActiveColor() { return activeColor; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveClock() { return fullMoveClock; }
    public double getEvaluation() { return evaluation; }
    public double getTruncatedEvaluation() {
        String truncatedNumberString = String.format("%.5f", evaluation);
        // Parse the formatted string back to a double if needed
        double truncatedNumber = Double.parseDouble(truncatedNumberString);
        return truncatedNumber;
    }
} // END ENGINE
