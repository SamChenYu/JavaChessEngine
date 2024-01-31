
package Game;

import chessengine.Move;
import java.util.ArrayList;


public final class Game {
    
    public void comments() {
    /**********************************************
    
    
    Things to taken note:
    * The array implementation is actually backwards to align the array indices
    * with the chess board pieces
        
   
     
    Typical Chess Board:
    [A8][B8][C8][D8][E8][F8][G8][H8]
    [A7][B7][C7][D7][E7][F7][G7][H7]
    [A6][B6][C6][D6][E6][F6][G6][H6]
    [A5][B5][C5][D5][E5][F5][G5][H5]
    [A4][B4][C4][D4][E4][F4][G4][H4]
    [A3][B3][C3][D3][E3][F3][G3][H3]
    [A2][B2][C2][D2][E2][F2][G2][H2]
    [A1][B1][C1][D1][E1][F1][G1][H1]
    * 
    * 
    * 
     ARRAY REFERENCE TABLE
     * WHITE
     [A1][B1][C1][D1][E1][F1][G1][H1]
     [A2][B2][C2][D2][E2][F2][G2][H2]
     [A3][B3][C3][D3][E3][F3][G3][H3] 
     [A4][B4][C4][D4][E4][F4][G4][H4]
     [A5][B5][C5][D5][E5][F5][G5][H5]
     [A6][B6][C6][D6][E6][F6][G6][H6]
     [A7][B7][C7][D7][E7][F7][G7][H7] 
     [A8][B8][C8][D8][E8][F8][G8][H8]
     * BLACK
     * 
    [0,0][1,0][2,0][3,0][4,0][5,0][6,0][7,0]
    [0,1][1,1][2,1][3,1][4,1][5,1][6,1][7,1]
    [0,2][1,2][2,2][3,2][4,2][5,2][6,2][7,2]
    [0,3][1,3][2,3][3,3][4,3][5,3][6,3][7,3]
    [0,4][1,4][2,4][3,4][4,4][5,4][6,4][7,4]
    [0,5][1,5][2,5][3,5][4,5][5,5][6,5][7,5]
    [0,6][1,6][2,6][3,6][4,6][5,6][6,6][7,6]
    [0,7][1,7][2,7][3,7][4,7][5,7][6,7][7,7]
    
    A File = [0,y]      B File = [1,y]
    C File = [2,y]      D File = [3,y]
    E File = [4,y]      F File = [5,y]
    G File = [6,y]      H File = [7,y]

    1st Rank = [x,0]    2nd Rank = [x,4]
    3rd Rank = [x,1]    4th Rank = [x,5]
    5th Rank = [x,2]    6th Rank = [x,6]
    7th Rank = [x,3]    8th Rank = [x,7]
    **********************************************/
    }

    private double evaluation = 0;
    
    private final Piece[][] board = new Piece[8][8];
    private final Piece[][] flippedBoard = new Piece[8][8]; // this is sent out to the panels to print out
    private char activeColor = ' ';
    private boolean whiteCastleKingSide = false;
    private boolean whiteCastleQueenSide = false;
    private boolean blackCastleKingSide = false;
    private boolean blackCastleQueenSide = false;
    private String enPassant = "-";
    private int enPassantX = -1;
    private int enPassantY = -1;
    private int halfMoveClock = 0; // turns since last capture for 50 move rule
    private int fullMoveClock = 1; // full amount of turns taken
    
    public ArrayList<Move> moves; // list of possible moves for the current board state
    
    
    public Game(String input) {
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = new Piece();
                flippedBoard[i][j] = new Piece();
            }
        }
        moves = new ArrayList<Move>();
        processString(input);
    }
    
    public Game() { // most likely created for a copy
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = new Piece();
                flippedBoard[i][j] = new Piece();
            }
        }
        moves = new ArrayList<Move>();
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
        if(enPassant.length() == 2) {
            char row = enPassant.charAt(0);
            char file = enPassant.charAt(1);
            
            switch(row) {
                case 'a' -> { enPassantX = 0; break;}
                case 'b' -> { enPassantX = 1; break;}
                case 'c' -> { enPassantX = 2; break;}
                case 'd' -> { enPassantX = 3; break;}
                case 'e' -> { enPassantX = 4; break;}
                case 'f' -> { enPassantX = 5; break;}
                case 'g' -> { enPassantX = 6; break;}
                case 'h' -> { enPassantX = 7; break;}
            }
            
            enPassantY = Character.getNumericValue(file);
            enPassantY--;
        }

        halfMoveClock = Integer.parseInt(fenParts[4]);
        fullMoveClock = Integer.parseInt(fenParts[5]);
        
        String[] boardRows = fenParts[0].split("/");
        
        // Flipping the board files so that the row number match the array indicies
        // Copy over the array to flip
        String[] temp = new String[boardRows.length];
        for(int i=0; i<boardRows.length; i++) {
            temp[i] = boardRows[i];
        }
        // flipping the board
        int backwardCounter = boardRows.length-1;
        for(int i=0; i<8; i++) {
            boardRows[i] = temp[backwardCounter];
            backwardCounter--;
        }
        
        
        
        
        
        

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
                        board[squareCounter][i] = new Piece("rook", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'n' -> {
                        // black knight
                        board[squareCounter][i] = new Piece("knight", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'b' -> {
                        // black bishop
                        board[squareCounter][i] = new Piece("bishop", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'q' -> {
                        // black queen
                        board[squareCounter][i] = new Piece("queen", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'k' -> {
                        // black king
                        board[squareCounter][i] = new Piece("king", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'p' -> {
                        // black pawn
                        board[squareCounter][i] = new Piece("pawn", 'b');
                        squareCounter++;
                        break;
                    }
                    
                    case 'P' -> {
                        // white pawn
                        board[squareCounter][i] = new Piece("pawn", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'K' -> {
                        // white king
                        board[squareCounter][i] = new Piece("king", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'Q' -> {
                        //white queen
                        board[squareCounter][i] = new Piece("queen", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'B' -> {
                        // white bishop
                        board[squareCounter][i] = new Piece("bishop", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'N' -> {
                        // white knight
                        board[squareCounter][i] = new Piece("knight", 'w');
                        squareCounter++;
                        break;
                    }
                    
                    case 'R' -> {
                        // white rook
                        board[squareCounter][i] = new Piece("rook", 'w');
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
    
        initFlippedBoard();
    } // END PROCESS STRING
        
    public void initFlippedBoard() {
        
        
        int backwardCounter = 7;
        for(int i=0; i<8; i++) {
            backwardCounter = 7;
            for(int j=0; j<8;j++) {
                flippedBoard[i][backwardCounter] = board[i][j];
                backwardCounter--;
            }
        }
        
    }
    
    // Getters and Setters
    public void setBoard(Piece[][] newBoard) {
        for(int i=0; i<8;i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = newBoard[i][j];
            }
        }
    }
    
    public void setActiveColor(char color) {
        activeColor = color;
    }
    
    public void setWhiteCastleKingSide(boolean bool) {
        whiteCastleKingSide = bool;
    }
    
    public void setWhiteCastleQueenSide(boolean bool) {
        whiteCastleQueenSide = bool;
    }
    
    public void setBlackCastleKingSide(boolean bool) {
        blackCastleKingSide = bool;
    }
    
    public void setBlackCastleQueenSide(boolean bool) {
        blackCastleQueenSide = bool;
    }
    
    public void setEnPassant(String enP) {
        enPassant = enP;
        
        if(enPassant.length() == 2) {
            char row = enPassant.charAt(0);
            char file = enPassant.charAt(1);
            
            switch(row) {
                case 'a' -> { enPassantX = 0; break;}
                case 'b' -> { enPassantX = 1; break;}
                case 'c' -> { enPassantX = 2; break;}
                case 'd' -> { enPassantX = 3; break;}
                case 'e' -> { enPassantX = 4; break;}
                case 'f' -> { enPassantX = 5; break;}
                case 'g' -> { enPassantX = 6; break;}
                case 'h' -> { enPassantX = 7; break;}
            }
            
            enPassantY = Character.getNumericValue(file);
            enPassantY--;
        }
        
    }
    
    
    public void setEnPassantXY(int x, int y) {
        enPassantX = x;
        enPassantY = y;
        
        String tempEnPassant = "";
        
        switch (x) {
            case 0 ->  tempEnPassant = "a";
            case 1 ->  tempEnPassant = "b";
            case 2 ->  tempEnPassant = "c";
            case 3 ->  tempEnPassant = "d";
            case 4 ->  tempEnPassant = "e";
            case 5 ->  tempEnPassant = "f";
            case 6 ->  tempEnPassant = "g";
            case 7 ->  tempEnPassant = "h";
            default -> tempEnPassant = "-";
        }
        
        int temp;
        if(activeColor == 'w') {
            temp = enPassantY +1;
        } else {
            temp = enPassantY -1;
        }
        
        tempEnPassant = tempEnPassant + ( "" + temp);
        
        enPassant = tempEnPassant;
    }
    
    public void setEnPassantX(int x) {
        enPassantX = x;
    }
    
    public void setEnPassantY(int y) {
        enPassantY = y;
        
    }
    
    public void setHalfMoveClock(int x) {
        halfMoveClock = x;
    }
    
    public void setFullMoveClock(int x) {
        fullMoveClock = x;
    }
    
    
    public void flipColor() {
        
        if(activeColor == 'w') {
            activeColor = 'b';
        } else {
            activeColor = 'w';
        }
        
    }
    
    public Piece[][] getBoard() { return board; }
    public Piece[][] getFlippedBoard() { initFlippedBoard(); return flippedBoard; }
    public char getActiveColor() { return activeColor; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveClock() { return fullMoveClock; }
    public void incrementFullMoveClock() { fullMoveClock++; }
    public void incrementHalfMoveClock() { halfMoveClock++; }
    public void revertFullMoveClock() { fullMoveClock--; }
    public void revertHalfMoveClock() { halfMoveClock--; }
    public String getEnPassant() { return enPassant; }
    public int getEnPassantX() { return enPassantX; }
    public int getEnPassantY() { return enPassantY; }
    public boolean getWhiteCastleKingSide() { return whiteCastleKingSide; }
    public boolean getWhiteCastleQueenSide() { return whiteCastleQueenSide; }
    public boolean getBlackCastleKingSide() { return blackCastleKingSide; }
    public boolean getBlackCastleQueenSide() { return blackCastleQueenSide; }
    public ArrayList<Move> getMoveList() { return moves; }
    public double getEvaluation() { return evaluation; }
    public void setEvaluation(double eval) { evaluation = eval; }
} // end class
