
package GUI;

import chessengine.Engine;
import Game.Piece;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
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
            g2.drawImage(boardImage, 0, 0, 800, 760, null);

            // Drawing the individual pieces on the board
            // For some reason there are some approximation issues with drawing the individual
            // Pieces so the bottom of the board becomes slightly misaligned
            final int spriteWidth = 90;
            final int spriteHeight = 86;
            final int initialSquareOffsetY = 36;    
            final int initialSquareOffsetX = 39;

            Piece[][] board = engine.getFlippedBoard();
            for(int x=0; x<8; x++) {
                for(int y=0; y<8; y++) {
                    int spriteX = (x*spriteWidth)+initialSquareOffsetX;
                    int spriteY = (y*spriteHeight)+initialSquareOffsetY;
                    g2.drawImage(board[x][y].getImage(),spriteX,spriteY,spriteWidth,spriteHeight,null);
//                    g2.setColor(Color.red);
//                    g2.drawRect(spriteX,spriteY,spriteWidth,spriteHeight);
                }
            }
            final Font font = new Font("Arial",Font.BOLD,30);
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString("Evaluation: "+engine.getTruncatedEvaluation(), 850, 50);
            g2.drawString("Turn: "+engine.getActiveColor(),850,150);
            g2.drawString("Full Move Clock: "+engine.getFullMoveClock(),850,200);
            g2.drawString("Half Move Clock: "+engine.getHalfMoveClock(),850,250);

            g2.drawString("Move #1: n/a",850,400);
            g2.drawString("Move #2: n/a",850,450);
            g2.drawString("Move #3: n/a",850,500);
        }
    };
    
    Engine engine;
    
    BufferedImage boardImage;
    
    
    
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
        } catch(IOException e) {
            System.out.println("board image loading failed");
        }
    }
    

}
