package chessengine;


import Game.Game;
import Game.Piece;


import java.util.ArrayList;

public class MovesGenerator {

    Game game;
    Engine engine;

    public MovesGenerator(Game game, Engine engine) {
        this.game = game;
        this.engine = engine;
    }

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
                Piece piece = board[i][j];
                String pieceName = piece.getName();
                char color = piece.getColor();

                if (activeColor == color) {

                    switch (pieceName) {

                        case "-" -> { continue; }

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

}
