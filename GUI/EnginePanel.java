
package GUI;

import chessengine.Engine;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class EnginePanel{
    
    JFrame frame = new JFrame();
    JPanel panel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw the board image
        if (boardImage != null) {
            g2.drawImage(boardImage, 0, 0, 800, 760, null);
            g2.drawImage(blackKing,40,40,100,100,null);
        }
    }
    };
    
    Engine engine;
    
    BufferedImage boardImage;
    BufferedImage whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing;
    BufferedImage blackRook, blackKnight, blackBishop, blackQueen, blackKing;
    
    
    
    public EnginePanel(String input) {
        engine = new Engine(input);
        initUI();
        panel.repaint();
    }
    
    public void initUI() {
        
        
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setTitle("Engine Panel");
        
        panel.setBackground(Color.BLACK);
        loadImages();


    }
        
    public void loadImages() {
        try {
            String filepath = "/res/chessboard.jpg";
            boardImage = ImageIO.read(getClass().getResourceAsStream(filepath));
            
            filepath = "/res/b-rook.png";
            blackRook = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/b-knight.png";
            blackKnight = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/b-bishop.png";
            blackBishop = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/b-queen.png";
            blackQueen = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/b-king.png";
            blackKing = ImageIO.read(getClass().getResourceAsStream(filepath));
            
            filepath = "/res/w-rook.png";
            whiteRook = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/w-knight.png";
            whiteKnight = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/w-bishop.png";
            whiteBishop= ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/w-queen.png";
            whiteQueen = ImageIO.read(getClass().getResourceAsStream(filepath));
            filepath = "/res/w-king.png";
            whiteKing = ImageIO.read(getClass().getResourceAsStream(filepath));
            
            
            
        } catch(IOException e) {
            System.out.println("image loading failed");
        }
    }
    

}
