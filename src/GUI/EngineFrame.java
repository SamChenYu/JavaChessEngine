package GUI;

import javax.swing.*;

public class EngineFrame extends JFrame {

    EnginePanel panel;

    public EngineFrame(String input) {
        panel = new EnginePanel(input);
        setSize(1200,800);
        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
        setResizable(false);
        setTitle("Engine Panel");
        ImageIcon icon = new ImageIcon("src/res/b-pawn.png");
        setIconImage(icon.getImage());

    }

}
