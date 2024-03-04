package Main;

import java.util.ArrayList;
import Move.Move;

public final class Game {


    private long board[] = new long[12];
    private long occupiedSquares = 0L;
    private long whiteSquares = 0L;
    private long blackSquares = 0L;

    // Constants for each piece
    public static final int W_PAWN = 0;
    public static final int W_KNIGHT = 1;
    public static final int W_BISHOP = 2;
    public static final int W_ROOK = 3;
    public static final int W_QUEEN = 4;
    public static final int W_KING = 5;
    public static final int B_PAWN = 6;
    public static final int B_KNIGHT = 7;
    public static final int B_BISHOP = 8;
    public static final int B_ROOK = 9;
    public static final int B_QUEEN = 10;
    public static final int B_KING = 11;



    private char activeColor = ' ';
    private boolean whiteCastleKingSide = false;
    private boolean whiteCastleQueenSide = false;
    private boolean blackCastleKingSide = false;
    private boolean blackCastleQueenSide = false;
    private String enPassant = "-";
    private int halfMoveClock = 0; // turns since last capture for 50 move rule
    private int fullMoveClock = 1; // full amount of turns taken

    public ArrayList<Move> moves = new ArrayList<>();

    public Game(String input) {
        processString(input);
        //initialiseStartingPosition();
    }


    // Initialize the starting position
    public void initialiseStartingPosition() {
        // Clear all bitboards
        for (int i = 0; i < 12; i++) {
            board[i] = 0L;
        }

        // Set up white pieces
        board[W_PAWN] = 0x000000000000FF00L;
        board[W_KNIGHT] = 0x0000000000000042L;
        board[W_BISHOP] = 0x0000000000000024L;
        board[W_ROOK] = 0x0000000000000081L;
        board[W_QUEEN] = 0x0000000000000008L;
        board[W_KING] = 0x0000000000000010L;

        // Set up black pieces
        board[B_PAWN] = 0x00FF000000000000L;
        board[B_KNIGHT] = 0x4200000000000000L;
        board[B_BISHOP] = 0x2400000000000000L;
        board[B_ROOK] = 0x8100000000000000L;
        board[B_QUEEN] = 0x0800000000000000L;
        board[B_KING] = 0x1000000000000000L;

        updateOccupiedSquares();
    }

    public void updateOccupiedSquares() {
        // Set occupied squares
        occupiedSquares = 0L;
        whiteSquares = 0L;
        blackSquares = 0L;
        for (int i = 0; i < 12; i++) {
            occupiedSquares |= board[i];
        }

        for(int i = 0; i < 6; i++) {
            whiteSquares |= board[i];
        }

        for(int i = 6; i < 12; i++) {
            blackSquares |= board[i];
        }
    }




    // Output the board in FEN notation
    public void printBoardState() {
        StringBuilder fen = new StringBuilder();

        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;
            for (int file = 0; file < 8; file++) {
                long square = 1L << (file + rank * 8);
                boolean isOccupied = (occupiedSquares & square) != 0;

                if (!isOccupied) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append('-').append('\t');
                        emptyCount = 0;
                    }
                    fen.append(pieceAt(square)).append('\t');
                }
            }

            // Handle the case where the last square in a rank is empty
            if (emptyCount > 0) {
                fen.append('-').append('\t');
            }

            if (rank > 0) {
                fen.append('\n');
            }
        }
        System.out.println("Complete Board");
        System.out.println(fen.toString());




        System.out.println("Active Color: " + activeColor);
        System.out.println("White Castle King Side " + whiteCastleKingSide);
        System.out.println("White Castle Queen Side " + whiteCastleQueenSide);
        System.out.println("Black Castle King Side " + blackCastleKingSide);
        System.out.println("Black Castle Queen Side " + blackCastleQueenSide);
        System.out.println("En Passant: " + enPassant);
        System.out.println("Half Move Clock " + halfMoveClock);
        System.out.println("Full Move Clock " + fullMoveClock);
    }



    // Helper method to determine piece type at a given square
    private char pieceAt(long square) {
        for (int i = 0; i < 12; i++) {
            if ((board[i] & square) != 0) {
                return pieceTypeToChar(i);
            }
        }
        return '-';
    }

    // Helper method to convert piece type index to FEN notation
    private char pieceTypeToChar(int pieceType) {
        switch (pieceType) {
            case W_PAWN:
                return 'P';
            case W_KNIGHT:
                return 'N';
            case W_BISHOP:
                return 'B';
            case W_ROOK:
                return 'R';
            case W_QUEEN:
                return 'Q';
            case W_KING:
                return 'K';
            case B_PAWN:
                return 'p';
            case B_KNIGHT:
                return 'n';
            case B_BISHOP:
                return 'b';
            case B_ROOK:
                return 'r';
            case B_QUEEN:
                return 'q';
            case B_KING:
                return 'k';
            default:
                return '-';
        }
    }


    public void processString(String input) {
        // Split the FEN string using space as the delimiter
        String[] fenParts = input.split(" ");

        activeColor = fenParts[1].charAt(0);

        // Process castling rights
        for (int i = 0; i < fenParts[2].length(); i++) {
            char currentChar = fenParts[2].charAt(i);
            switch (currentChar) {
                case 'K':
                    whiteCastleKingSide = true;
                    break;
                case 'Q':
                    whiteCastleQueenSide = true;
                    break;
                case 'k':
                    blackCastleKingSide = true;
                    break;
                case 'q':
                    blackCastleQueenSide = true;
                    break;
                case '-':
                    break; // No castling rights for this side
                default:
                    throw new IllegalArgumentException("Invalid FEN string: castling rights");
            }
        }
        enPassant = fenParts[3];
        halfMoveClock = Integer.parseInt(fenParts[4]);
        fullMoveClock = Integer.parseInt(fenParts[5]);

        // Initialize the board
        int rank = 7; // Start from the 8th rank (index 7) and go down to the 1st rank (index 0)
        for (String row : fenParts[0].split("/")) {
            int file = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    // Skip empty squares
                    file += Character.getNumericValue(c);
                } else {
                    long square = 1L << (file + rank * 8);
                    switch (c) {
                        case 'P':
                            board[W_PAWN] |= square;
                            break;
                        case 'N':
                            board[W_KNIGHT] |= square;
                            break;
                        case 'B':
                            board[W_BISHOP] |= square;
                            break;
                        case 'R':
                            board[W_ROOK] |= square;
                            break;
                        case 'Q':
                            board[W_QUEEN] |= square;
                            break;
                        case 'K':
                            board[W_KING] |= square;
                            break;
                        case 'p':
                            board[B_PAWN] |= square;
                            break;
                        case 'n':
                            board[B_KNIGHT] |= square;
                            break;
                        case 'b':
                            board[B_BISHOP] |= square;
                            break;
                        case 'r':
                            board[B_ROOK] |= square;
                            break;
                        case 'q':
                            board[B_QUEEN] |= square;
                            break;
                        case 'k':
                            board[B_KING] |= square;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid FEN string: board setup");
                    }
                    file++;
                }
            }
            rank--; // Move to the next rank
        }
        updateOccupiedSquares();
    }


    // Getters and setters

    public char getActiveColor() { return activeColor; }
    public long[] getBoard() { return board; }
    public long getWhiteOccupiedSquares() { return whiteSquares; }
    public long getBlackOccupiedSquares() { return blackSquares; }
    public long getOccupiedSquares() { return occupiedSquares; }



}