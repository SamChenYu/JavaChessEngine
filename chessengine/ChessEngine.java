package chessengine;

import GUI.ControlPanel;
import GUI.EnginePanel;
import javax.swing.SwingUtilities;

public final class ChessEngine {

    public ChessEngine() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlPanel controlPanel = new ControlPanel();
            
            // Using a lambda expression to handle the button click event
            controlPanel.setButtonClickListener(e -> {
                // Handle button click event in the main class
                EnginePanel engineP = new EnginePanel(controlPanel.getString());
            });
        });
    }
    
    
}
