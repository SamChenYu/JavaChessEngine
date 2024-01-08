
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

    
    public Engine(String input) {
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = new Piece();
            }
        }
        
        processString(input);
        printBoardState();
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
            
            for(int j=0; j<8; j++) {
                //ITERATING FOR 8 SQUARES IN THE ROW
                char currentChar = boardRows[i].charAt(j);
                switch(currentChar) {
                    case 'r' -> {
                        // black rook
                        board[i][j] = new Piece("rook", 'b');
                        break;
                    }
                    
                    case 'n' -> {
                        // black knight
                        board[i][j] = new Piece("knight", 'b');
                        break;
                    }
                    
                    case 'b' -> {
                        // black bishop
                        board[i][j] = new Piece("bishop", 'b');
                        break;
                    }
                    
                    case 'q' -> {
                        // black queen
                        board[i][j] = new Piece("queen", 'b');
                        break;
                    }
                    
                    case 'k' -> {
                        // black king
                        board[i][j] = new Piece("king", 'b');
                        break;
                    }
                    
                    case 'p' -> {
                        // black pawn
                        board[i][j] = new Piece("pawn", 'b');
                        break;
                    }
                    
                    
                    case 'P' -> {
                        // white pawn
                        board[i][j] = new Piece("pawn", 'w');
                        break;
                    }
                    
                    case 'K' -> {
                        // white king
                        board[i][j] = new Piece("king", 'w');
                        break;
                    }
                    
                    case 'Q' -> {
                        //white queen
                        board[i][j] = new Piece("queen", 'w');
                        break;
                    }
                    
                    case 'B' -> {
                        // white bishop
                        board[i][j] = new Piece("bishop", 'w');
                        break;
                    }
                    
                    case 'N' -> {
                        // white knight
                        board[i][j] = new Piece("knight", 'w');
                        break;
                    }
                    
                    case 'R' -> {
                        // white rook
                        board[i][j] = new Piece("rook", 'w');
                        break;
                    }
                    
                    // Handle empty squares (represented by numbers)
                    case '1', '2', '3', '4', '5', '6', '7', '8' -> {
                        // Convert the character to an integer and skip that number of squares
                        int emptySquares = Character.getNumericValue(currentChar);
                        j += emptySquares - 1; // Subtract 1 because the loop will increment j
                        break;
                    
                    
                    }
                    
                    default -> {
                        // for the non valid input, the board is set to a normal state
                        input = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
                        processString(input);
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
                System.out.print(board[i][j].name + " ");
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
    }
} // END ENGINE
