
package chessengine;

import java.awt.image.BufferedImage;

public class Piece {
    String name;
    char color;
    BufferedImage image;
    
    public Piece(String name, char color) {
        this.name = name;
        this.color = color;
        
    }
    
    public Piece() {
        name = "";
    }
}
