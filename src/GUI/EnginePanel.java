
package GUI;

import chessengine.Engine;
import Game.Piece;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public final class EnginePanel{

    final Font font = new Font("Arial",Font.BOLD,30);
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

            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString("Evaluation: "+engine.getTruncatedEvaluation(), 850, 50);
            g2.drawString("Turn: "+engine.getActiveColor(),850,150);
            g2.drawString("Full Move Clock: "+engine.getFullMoveClock(),850,200);
            g2.drawString("Half Move Clock: "+engine.getHalfMoveClock(),850,250);

            g2.drawString("Move #1: n/a",850,400);
            g2.drawString("Move #2: n/a",850,450);
            g2.drawString("Move #3: n/a",850,500);
            g2.drawString("Positions searched: ",850,600);
        }
    };
    
    Engine engine;
    
    BufferedImage boardImage;

    
    
    
    public EnginePanel(String input) {
        engine = new Engine(input);
        initUI();

        panel.repaint();    // repaint to the user isn't just blank
        panel.repaint();    // repaint to verify the original position is correct
    }
    
    public void initUI() {
        panel.setLayout(null);
        JLabel posSearchedLabel = new JLabel(engine.getGamesSearched());
        posSearchedLabel.setBounds(850,620,100,50);
        posSearchedLabel.setFont(font);
        posSearchedLabel.setForeground(Color.WHITE);
        posSearchedLabel.setBackground(Color.BLACK);
        panel.add(posSearchedLabel);
        JButton startButton = new JButton("Search");
        startButton.setBounds(850,700,150,50);
        startButton.setFont(font);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.startSearch();
            }
        });
        panel.add(startButton);
        panel.setBackground(Color.BLACK);

        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setTitle("Engine Panel");
        ImageIcon icon = new ImageIcon("src/res/b-pawn.png");
        frame.setIconImage(icon.getImage());
        loadImages();


    }
        
    public void loadImages() {
        try {
            String filepath = "/src/res/chessboard.jpg";
            
            InputStream stream = getClass().getResourceAsStream("/res/chessboard.jpg");
            boardImage = ImageIO.read(stream);
            
        } catch(IOException e) {
            System.out.println("board image loading failed");
        }
    }
    

}
