
package chessengine;

import java.util.ArrayList;

public final class Engine {
    
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
    
    private double evaluation = 0;
    
    private ArrayList<Move> moves = new ArrayList<>();
    
    public Engine(String input) {
        
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                board[i][j] = new Piece();
                flippedBoard[i][j] = new Piece();
            }
        }
        
        processString(input);
        evaluate(board);
        updateMoves(board,activeColor);
        printMoves(board,moves);
        
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
 
    
    // Algoritmic Functions:
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
    public void updateMoves(Piece[][] board, char activeColor) {
        
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
        
        
        
        
        // To check if the king is under check
        ArrayList<String> squaresAttacked = new ArrayList<>();
        //ArrayList<Move> moves = new ArrayList<>();
        
        
        
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
                                            move.causesEnPassant();
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
                }
            } // END INNERLOOP
        } // END OUTERLOOP
    } // END UPDATEMOVES
    
    
    // Testing Functions:
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
            String newPiece = moves.get(i).getPromotePiece();
            boolean isCastle = moves.get(i).getIsCastle();
            boolean isKingSide = moves.get(i).getIsKingSide();
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
    public Piece[][] getBoard() { return board; }
    public Piece[][] getFlippedBoard() { return flippedBoard; }
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
