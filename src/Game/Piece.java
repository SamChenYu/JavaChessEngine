
package Game;

import chessengine.Move;
import chessengine.MovesGenerator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Piece implements Cloneable {
    
    private String name;
    private int value;
    private char color = '.';
    private String filepath;
    private BufferedImage image;
    MovesGenerator mg;

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

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            // This should never happen since Piece implements Cloneable
            throw new AssertionError();
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
    
    
    public BufferedImage getImage() {
        return image;
    }
    
    public boolean isEmpty() {
        return name.equals("-");
    }
    
    
    public boolean isEnemy(char testColor) {
        
        if(color == testColor || color == '.') {
            return false;
        }  {
            return true;
        }
    }

    public ArrayList<Move> update(Game game, int i, int y) {
        // default case: no piece is in place
        return null;
    }

}
