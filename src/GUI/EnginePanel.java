
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
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public final class EnginePanel extends JPanel {

    private Engine engine;
    private BufferedImage boardImage;
    private final JLabel posSearchedLabel;
    private final JButton startButton;
    private final JButton move1, move2, move3;
    private final JTextField newFENField;
    private final JButton newFENButton;
    private final JButton[][] squareButtons = new JButton[8][8];
    private final Font font = new Font("Arial",Font.BOLD,30);
    private final Font moveFont = new Font("Arial",Font.BOLD,20);

    // Manually moving pieces
    private int pieceSelectedX = -1, pieceSelectedY = -1;



    
    public EnginePanel(String input) {

        engine = new Engine(input, this);
        setLayout(null);

        // Init all JSwing Elements
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
                move2.setEnabled(false);
                move3.setEnabled(false);
                engine.makeMove1();
                repaint();
                startButton.setEnabled(true);
            }
        });
        add(move1);

        move2 = new JButton("#2: N/A");
        move2.setBounds(850,300,325,50);
        move2.setFont(moveFont);
        move2.setForeground(Color.BLACK);
        move2.setBackground(Color.WHITE);
        move2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable button to prevent multiple searches
                move1.setEnabled(false);
                move2.setEnabled(false);
                move3.setEnabled(false);
                engine.makeMove2();
                repaint();
                startButton.setEnabled(true);
            }
        });
        add(move2);

        move3 = new JButton("#2: N/A");
        move3.setBounds(850,350,325,50);
        move3.setFont(moveFont);
        move3.setForeground(Color.BLACK);
        move3.setBackground(Color.WHITE);
        move3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable button to prevent multiple searches
                move1.setEnabled(false);
                move2.setEnabled(false);
                move3.setEnabled(false);
                engine.makeMove3();
                repaint();
                startButton.setEnabled(true);
            }
        });
        add(move3);

        posSearchedLabel = new JLabel();
        posSearchedLabel.setText("0");
        posSearchedLabel.setBounds(850,450,300,50);
        posSearchedLabel.setFont(font);
        posSearchedLabel.setForeground(Color.WHITE);
        posSearchedLabel.setBackground(Color.BLACK);
        add(posSearchedLabel);


        startButton = new JButton("Search");
        startButton.setBounds(850,510,150,50);
        startButton.setFont(font);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);

                // Perform search in a separate thread
                Thread searchThread = new Thread(new Runnable() {
                    public void run() {
                        engine.startSearch();
                    }
                });
                searchThread.start();
            }
        });
        add(startButton);

        newFENField = new JTextField("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        newFENField.setBackground(Color.BLACK);
        newFENField.setForeground(Color.WHITE);
        newFENField.setFont(new Font("Arial",Font.BOLD,10));
        newFENField.setBounds(850,650,325,50);
        add(newFENField);
        newFENButton = new JButton("Enter New FEN");
        newFENButton.setBounds(850,700,325,50);
        newFENButton.setFont(font);
        newFENButton.setForeground(Color.BLACK);
        newFENButton.setBackground(Color.WHITE);
        newFENButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                newFEN();
                startButton.setEnabled(true);
            }
        });
        add(newFENButton);


        // initialisating the invisible JButtons

        Piece[][] board = engine.getFlippedBoard();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                final int finalCounterX = x; // Capture the current value of counter
                final int finalCounterY = y;
                int spriteX = (x * 90) + 36;
                int spriteY = (y * 85) + 36;
                squareButtons[x][y] = new JButton();
                squareButtons[x][y].setBounds(spriteX, spriteY, 94, 90);
                squareButtons[x][y].setOpaque(false);
                squareButtons[x][y].setContentAreaFilled(false);
                squareButtons[x][y].setBorderPainted(false);
                squareButtons[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if(pieceSelectedX == -1) {
                            pieceSelectedX = finalCounterX;
                            pieceSelectedY = finalCounterY;
                        } else {
                            // send the data to engine to make a move
                            engine.moveFromPanel(pieceSelectedX, pieceSelectedY, finalCounterX, finalCounterY);
                            pieceSelectedX = -1;
                            pieceSelectedY = -1;
                            repaint();
                        }
                    }
                });
                add(squareButtons[finalCounterX][finalCounterY]);
            }
        }


        setBackground(Color.BLACK);
        loadImages();
        repaint();    // repaint to the user isn't just blank
    }

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
                    //g2.setColor(Color.red);
                    //g2.drawRect(spriteX,spriteY,spriteWidth,spriteHeight);
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

    public void updateGamesSearched(int gamesSearched) {
        posSearchedLabel.setText(""+gamesSearched);
        repaint();
    }

    public void updateMove1(String move) {
        move1.setText("#1: "+move);
        move1.setEnabled(true);
        move2.setEnabled(true);
        move3.setEnabled(true);

        repaint();
    }

    public void updateMove2(String move) {
        move2.setText("#2: "+move);
        move1.setEnabled(true);
        move2.setEnabled(true);
        move3.setEnabled(true);

        repaint();
    }

    public void updateMove3(String move) {
        move3.setText("#3: "+move);
        move1.setEnabled(true);
        move2.setEnabled(true);
        move3.setEnabled(true);

        repaint();
    }

    public void newFEN() {
        engine = new Engine(newFENField.getText(), this);
        repaint();
    }

        
    public void loadImages() {
        try {
            String filepath = "/src/res/chessboard.jpg";
            
            InputStream stream = getClass().getResourceAsStream("/res/chessboard.jpg");
            assert stream != null;
            boardImage = ImageIO.read(stream);
            
        } catch(IOException e) {
            System.out.println("board image loading failed");
        }
    }

} // End EnginePanel
