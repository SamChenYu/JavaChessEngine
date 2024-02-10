
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

public final class EnginePanel {
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
            g2.drawString("Turn: "+engine.getActiveColor(),850,100);
            g2.drawString("Full Move Clock: "+engine.getFullMoveClock(),850,150);
            g2.drawString("Half Move Clock: "+engine.getHalfMoveClock(),850,200);

            g2.drawString("Positions searched: ",850,450);
        }

    };
    
    Engine engine;
    BufferedImage boardImage;
    JLabel posSearchedLabel;
    JButton startButton;
    JButton move1, move2, move3;
    JTextField newFENField;
    JButton newFENButton;
    final Font font = new Font("Arial",Font.BOLD,30);
    final Font moveFont = new Font("Arial",Font.BOLD,20);
    
    
    public EnginePanel(String input) {
        engine = new Engine(input, this);
        initUI();

        panel.repaint();    // repaint to the user isn't just blank
        panel.repaint();    // repaint to verify the original position is correct
    }
    
    public void initUI() {


        panel.setLayout(null);

        move1 = new JButton("#1: N/A");
        move1.setBounds(850,250,325,50);
        move1.setFont(moveFont);
        move1.setForeground(Color.BLACK);
        move1.setBackground(Color.WHITE);
        move1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable button to prevent multiple searches
                move1.setEnabled(false);
                engine.makeMove1();
                panel.repaint();
                startButton.setEnabled(true);
            }
        });
        panel.add(move1);

        move2 = new JButton("#2: N/A");
        move2.setBounds(850,300,325,50);
        move2.setFont(moveFont);
        move2.setForeground(Color.BLACK);
        move2.setBackground(Color.WHITE);
        panel.add(move2);

        move3 = new JButton("#2: N/A");
        move3.setBounds(850,350,325,50);
        move3.setFont(moveFont);
        move3.setForeground(Color.BLACK);
        move3.setBackground(Color.WHITE);
        panel.add(move3);

        posSearchedLabel = new JLabel();
        posSearchedLabel.setText("0");
        posSearchedLabel.setBounds(850,450,300,50);
        posSearchedLabel.setFont(font);
        posSearchedLabel.setForeground(Color.WHITE);
        posSearchedLabel.setBackground(Color.BLACK);
        panel.add(posSearchedLabel);


        startButton = new JButton("Search");
        startButton.setBounds(850,510,150,50);
        startButton.setFont(font);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable button to prevent multiple searches
                startButton.setEnabled(false);

                // Perform search in a separate thread
                Thread searchThread = new Thread(new Runnable() {
                    public void run() {
                        // Call your engine's startSearch() method here
                        engine.startSearch();

                        // Enable the button after search completes
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                move1.setEnabled(true);
                                move2.setEnabled(true);
                                move3.setEnabled(true);

                            }
                        });
                    }
                });

                searchThread.start();

            }
        });
        panel.add(startButton);

        newFENField = new JTextField("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        newFENField.setBackground(Color.BLACK);
        newFENField.setForeground(Color.WHITE);
        newFENField.setFont(new Font("Arial",Font.BOLD,10));
        newFENField.setBounds(850,650,325,50);
        panel.add(newFENField);
        newFENButton = new JButton("Enter New FEN");
        newFENButton.setBounds(850,700,325,50);
        newFENButton.setFont(font);
        newFENButton.setForeground(Color.BLACK);
        newFENButton.setBackground(Color.WHITE);
        newFENButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFEN();
            }
        });
        panel.add(newFENButton);




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

    public void updateGamesSearched(int gamesSearched) {
        posSearchedLabel.setText(""+gamesSearched);
        panel.repaint();
    }

    public void updateMove1(String move) {
        move1.setText("#1: "+move);
        panel.repaint();
    }

    public void newFEN() {
        engine = new Engine(newFENField.getText(), this);
        panel.repaint();
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
