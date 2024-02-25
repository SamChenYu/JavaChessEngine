
import java.util.HashMap;

public final class Main {


    private long board[] = new long[12];

    private HashMap<Integer, String> intPointers; // e.g. 0 -> a1
    private HashMap<String, Long> stringPointers; // e.g. a1 -> 0b1L



    private char activeColor = ' ';
    private boolean whiteCastleKingSide = false;
    private boolean whiteCastleQueenSide = false;
    private boolean blackCastleKingSide = false;
    private boolean blackCastleQueenSide = false;
    private String enPassant = "-";
    private int halfMoveClock = 0; // turns since last capture for 50 move rule
    private int fullMoveClock = 1; // full amount of turns taken

    public static void main(String[] args) {
        Main main = new Main("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println(main.getArrayIndexOf("h8"));
    }

    public Main(String input) {
        processString(input);
        initPointers();
    }


    public void initPointers() {
        stringPointers = new HashMap<>();
        // now for easier development, you can input the square and then this hashmap will point towards the array index
        // the pointers are placed in with the decimal representation of the binary number
        // you can use Long.toBinaryString(result) to get the binary representation of the number
        long mask = 1L;
        for (char file = 'a'; file <= 'h'; file++) {
            for (int rank = 1; rank <= 8; rank++) {
                String square = String.valueOf(file) + rank;
                stringPointers.put(square, mask);
                mask <<= 1;
            }
        }

        intPointers = new HashMap<>();
        char file;
        int rank;
        for (int i = 0; i < 64; i++) {
            file = (char) ('a' + i % 8);
            rank = 8 - (i / 8);
            String square = Character.toString(file) + rank;
            intPointers.put(i, square);
        }
    }


    public long getArrayIndexOf(String square) {
        if(stringPointers.containsKey(square)) {
            return stringPointers.get(square);
        } else {
            return -1;
        }
    }

    public long getArrayIndexOf(int square) {
        if(intPointers.containsKey(square)) {
            return stringPointers.get((intPointers.get(square)));
        } else {
            return -1;
        }
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
        // Now we can process the board

        int currentSquare = 0;
        for(int i=0; i<boardRows.length; i++) {

            for (int j = 0; j < boardRows[i].length(); j++) {
                // building the binary string to OR to add a new piece
                int zeroPreceding = i;
                int zeroSubsequent = 62 - i;
                String binaryString = "0".repeat(zeroPreceding) + "1" + "0".repeat(zeroSubsequent);
                System.out.println(binaryString);
                long result = Long.parseLong(binaryString, 2);

                char currentChar = boardRows[i].charAt(j);
                switch (currentChar) {
                    case 'P' -> { board[0] = board[0] | result; }
                    case 'N' -> { board[1] = board[1] | result; }
                    case 'B' -> { board[2] = board[2] | result; }
                    case 'R' -> { board[3] = board[3] | result; }
                    case 'Q' -> { board[4] = board[4] | result; }
                    case 'K' -> { board[5] = board[5] | result; }
                    case 'p' -> { board[6] = board[6] | result; }
                    case 'n' -> { board[7] = board[7] | result; }
                    case 'b' -> { board[8] = board[8] | result; }
                    case 'r' -> { board[9] = board[9] | result; }
                    case 'q' -> { board[10] = board[10] | result; }
                    case 'k' -> { board[11] = board[11] | result; }
                    case '1', '2', '3', '4', '5', '6', '7', '8' -> {
                        // Convert the character to an integer and skip that number of squares
                        int emptySquares = Character.getNumericValue(currentChar);
                        i += emptySquares; // Subtract 1 because the loop will increment j
                        break;
                    } // end case 1,2,3,4...

                } // end switch
                currentSquare++;
            } // end inner loop
        } // end outer loop










    } // END PROCESS STRING


//    public void printBoardState() {
//
//
//
//        System.out.println("Complete Board");
//
//        for(int i=0; i<64; i++) {
//            String piece = "";
//            long square = getArrayIndexOf(i);
//            for(int j=0; j<12; j++) {
//
//
//
//
//            }
//
//        }
//
//        for(int i=0; i<8; i++) {
//            for(int j=0; j<8; j++) {
//                char c = board[j][i].getName().charAt(0);
//                if(board[j][i].getColor() == 'w') {
//                    c = Character.toUpperCase(c);
//                }
//                System.out.print("\t " +  c + " \t");
//            }
//            System.out.print("\n");
//        }
//
//        System.out.println("Active Color: " + activeColor);
//        System.out.println("White Castle King Side " + whiteCastleKingSide);
//        System.out.println("White Castle Queen Side " + whiteCastleQueenSide);
//        System.out.println("Black Castle King Side " + blackCastleKingSide);
//        System.out.println("Black Castle Queen Side " + blackCastleQueenSide);
//        System.out.println("En Passant: " + enPassant);
//        System.out.println("Half Move Clock " + halfMoveClock);
//        System.out.println("Full Move Clock " + fullMoveClock);
//    }




}