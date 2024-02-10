package chessengine;

import GUI.ControlPanel;
import GUI.EngineFrame;
import javax.swing.SwingUtilities;

public final class Main {
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlPanel controlPanel = new ControlPanel();
            
            // Using a lambda expression to handle the button click event
            controlPanel.setButtonClickListener(e -> {
                // Handle button click event in the main class
                EngineFrame engineF = new EngineFrame(controlPanel.getString());

            });
        });
    }
    
    
}
