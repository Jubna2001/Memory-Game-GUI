import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameEntryFrame extends JFrame {
    private JTextField nameField;
    private JButton startButton;

    public NameEntryFrame() {
        setTitle("Enter Your Name");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Enter your name:");
        nameField = new JTextField(15);
        startButton = new JButton("Start Game");

        add(nameLabel);
        add(nameField);
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(NameEntryFrame.this, "Please enter your name.");
                } else {
                    new MemoryGameGUI(playerName);
                    dispose(); // Close the name entry frame
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NameEntryFrame::new);
    }
}
