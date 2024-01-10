
package chessengine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Piece {
    
    private String name;
    private int value;
    private char color = '.';
    private String filepath;
    private BufferedImage image;
    private ArrayList<Move> possibleMoves = new ArrayList<>();
    
    
    
    
    public Piece(String name, char color) {
        this.name = name;
        this.color = color;
        
        switch(name) {
            
            case "king" -> {
                value = Integer.MAX_VALUE;
                if(color == 'w' ) {
                    filepath = "/res/w-king.png";
                } else {
                    filepath = "/res/b-king.png";
                }
                break;
            }
            
            case "queen" -> {
                value = 9;
                if(color == 'w' ) {
                    filepath = "/res/w-queen.png";
                } else {
                    filepath = "/res/b-queen.png";
                }
                break;
            }
            
            case "rook" -> {
                value = 5;
                if(color == 'w') {
                    filepath = "/res/w-rook.png";
                } else {
                    filepath = "/res/b-rook.png";
                }
                break;
            }
            
            case "knight" -> {
                value = 3;
                if(color == 'w') {
                    filepath = "/res/w-knight.png";
                } else {
                    filepath = "/res/b-knight.png";
                }
                break;
            }
            
            case "bishop" -> {
                value = 3;
                if(color == 'w') {
                    filepath = "/res/w-bishop.png";
                } else {
                    filepath = "/res/b-bishop.png";
                }
                break;
            }
            
            case "pawn" -> {
                value = 1;
                if(color == 'w') {
                    filepath = "/res/w-pawn.png";
                } else {
                    filepath = "/res/b-pawn.png";
                }
                break;
            }
            
        }
        
        
        loadImage();
    }
    // Empty Square
    public Piece() {
        name = "-";
        value = 0;
        image = null;
    }
    
    public void loadImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(filepath));
        } catch(IOException e) {
            System.out.println(e);
            System.out.println("piece image loading failed");
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getName() {
        return name;
    }
    
    public int getValue() {
        return value;
    }
    
    public char getColor() {
        return color;
    }
    
    public ArrayList<Move> getMoves() {
        return possibleMoves;
    }
    
    public BufferedImage getImage() {
        return image;
    }
}
