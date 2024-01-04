package chessengine;

import GUI.ControlPanel;
import javax.swing.SwingUtilities;

public class ChessEngine {

    public ChessEngine() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlPanel controlPanel = new ControlPanel();
            
            // Using a lambda expression to handle the button click event
            controlPanel.setButtonClickListener(e -> {
                // Handle button click event in the main class
                System.out.println("Button clicked!");
                System.out.println(controlPanel.getString());
            });
        });
    }
    
    
}
