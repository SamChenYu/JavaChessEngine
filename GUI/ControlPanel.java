
package GUI;




import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

    

public final class ControlPanel extends JFrame {
    
    
    final JPanel ui = new JPanel();
    final JLabel mainLabel = new JLabel("<html><div style='text-align: center; vertical-align: middle;'>Enter FEN notation chess position or press<br>enter for a default position</div></html>");
    final JTextField inputTextField = new JTextField(15);
    
    final JButton button = new JButton("Enter");
    private ActionListener buttonClickListener;
    
    public String input ="";
    
    public ControlPanel() {
        
        setSize(400,400);
        setLocationRelativeTo(null);
        initUI();
        add(ui);
        setVisible(true);
        setResizable(false);
        setTitle("Chess Engine Control Panel");
    }
    
    
    public void initUI() {
        
        ui.setLayout(new GridBagLayout() );
        ui.setBackground(Color.BLACK);
        mainLabel.setForeground(Color.WHITE);
        inputTextField.setBackground(Color.WHITE);
        inputTextField.setForeground(Color.BLACK);
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 2;
        labelConstraints.insets = new Insets(30,50,30,50);
        ui.add(mainLabel, labelConstraints);
        
        
        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 0;
        textFieldConstraints.gridy = 5;
        textFieldConstraints.insets = new Insets(30,50,30,50);
        ui.add(inputTextField, textFieldConstraints);
        
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonClickListener != null) {
                    buttonClickListener.actionPerformed(e);
                }
                
                input = inputTextField.getText();
            }
        });
        
        
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 7;
        buttonConstraints.insets = new Insets(30,50,30,50);
        ui.add(button,buttonConstraints);
        
        
    }
    
    public void setButtonClickListener(ActionListener listener) {
        this.buttonClickListener = listener;
    }
    
    public String getString() {
        return input;
    }
    
}
