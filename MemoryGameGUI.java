import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class MemoryGameGUI extends JFrame {
    private static final int SIZE = 4; // Number of rows and columns
    private final JButton[][] buttons = new JButton[SIZE][SIZE];
    private final char[][] board = new char[SIZE][SIZE];
    private final boolean[][] flipped = new boolean[SIZE][SIZE];
    private final List<Character> symbols = new ArrayList<>();
    private JButton firstButton = null;
    private JButton secondButton = null;
    private int matches = 0;
    private Timer flipBackTimer;
    private JLabel scoreLabel;
    private JLabel nameLabel;
    private String playerName;

    public MemoryGameGUI(String playerName) {
        this.playerName = playerName;

        // Initialize symbols
        char[] allSymbols = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};
        int numPairs = SIZE * SIZE / 2;
        for (int i = 0; i < numPairs; i++) {
            symbols.add(allSymbols[i]);
            symbols.add(allSymbols[i]);
        }
        Collections.shuffle(symbols);

        // Setup the frame
        setTitle("Memory Game");
        setSize(400, 450); // Increase height to accommodate labels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create top panel for name and score labels
        JPanel topPanel = new JPanel(new BorderLayout());
        nameLabel = new JLabel("Player: " + playerName);
        scoreLabel = new JLabel("Score: 0");
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Create buttons and add them to the frame
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton button = new JButton("*");
                button.setFont(new Font("Arial", Font.PLAIN, 40));
                button.setFocusPainted(false);
                button.setActionCommand(i + "," + j);
                button.addActionListener(new ButtonClickListener());
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Initialize board
        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = symbols.get(index);
                index++;
            }
        }

        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] coords = e.getActionCommand().split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);

            if (flipped[row][col] || (firstButton != null && secondButton != null)) {
                return; // Ignore if already flipped or if two buttons are already selected
            }

            JButton button = buttons[row][col];
            button.setText(String.valueOf(board[row][col]));
            flipped[row][col] = true;

            if (firstButton == null) {
                firstButton = button;
            } else {
                secondButton = button;
                checkForMatch();
            }
        }

        private void checkForMatch() {
            if (firstButton != null && secondButton != null) {
                int r1 = Integer.parseInt(firstButton.getActionCommand().split(",")[0]);
                int c1 = Integer.parseInt(firstButton.getActionCommand().split(",")[1]);
                int r2 = Integer.parseInt(secondButton.getActionCommand().split(",")[0]);
                int c2 = Integer.parseInt(secondButton.getActionCommand().split(",")[1]);

                if (board[r1][c1] == board[r2][c2]) {
                    matches++;
                    scoreLabel.setText("Score: " + matches); // Update score label
                    firstButton = null;
                    secondButton = null;
                    if (matches == (SIZE * SIZE / 2)) {
                        JOptionPane.showMessageDialog(null, "You win!");
                        System.exit(0);
                    }
                } else {
                    // Delay for a short period to show the mismatched pair
                    if (flipBackTimer != null && flipBackTimer.isRunning()) {
                        flipBackTimer.stop(); // Stop any previous timer
                    }
                    flipBackTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            firstButton.setText("*");
                            secondButton.setText("*");
                            flipped[Integer.parseInt(firstButton.getActionCommand().split(",")[0])]
                                    [Integer.parseInt(firstButton.getActionCommand().split(",")[1])] = false;
                            flipped[Integer.parseInt(secondButton.getActionCommand().split(",")[0])]
                                    [Integer.parseInt(secondButton.getActionCommand().split(",")[1])] = false;
                            firstButton = null;
                            secondButton = null;
                        }
                    });
                    flipBackTimer.setRepeats(false);
                    flipBackTimer.start();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NameEntryFrame::new);
    }
}
