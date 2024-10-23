package jswing;

import javax.swing.*;
import java.awt.*;
import testapp.Client;
import static testapp.Client.obj;
import testapp.matchData;
import chesslogic.ChessGame;
public class MainMenuPage extends JFrame {
    private String username;
    private static Client client;

    public MainMenuPage(Client client, String username) {
        this.client = client;
        this.username = username;
        initializeUI();
    }
    
    private JLabel createStatsLabel() {
    String stats = client.obj.getUserStats(username);
    JLabel statsLabel = new JLabel("Stats: " + stats, SwingConstants.CENTER);
    statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    return statsLabel;
}

    private void initializeUI() {
        setTitle("Chess Game - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = createWelcomeLabel();
        JPanel buttonPanel = createButtonPanel();
        JLabel statsLabel = createStatsLabel(); // New label for stats
            


        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(statsLabel, BorderLayout.CENTER); // Add the stats label
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createWelcomeLabel() {
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        return welcomeLabel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton startGameButton = new JButton("Start Game");
        JButton logoutButton = new JButton("Logout");

        startGameButton.addActionListener(e -> startGameAction());
        logoutButton.addActionListener(e -> logoutAction());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(startGameButton, gbc);

        gbc.gridy = 1;
        panel.add(logoutButton, gbc);

        return panel;
    }

    private void startGameAction() {
        // TODO: Implement the game starting logic
        JOptionPane.showMessageDialog(this, "Game starting soon!", "Game Start", JOptionPane.INFORMATION_MESSAGE);
            matchData res = client.obj.matchMe();
            client.id=res.id;
            if (res.k==1) 
                client.isWhitePlayer=true;
            
            System.out.println("u have been paired . your id = " + res.id);

            // Launch the chess game interface
            launchChessGame();

    }

    private void logoutAction() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            new LoginSignupPage(client).setVisible(true);
            this.dispose();
        }
    }
    
        private static void launchChessGame() {
        System.out.println("Attempting to launch chess game...");
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Initializing ChessGame...");
                ChessGame.main(new String[0],client);
                System.out.println("ChessGame initialized successfully.");
            } catch (Exception e) {
                System.out.println("Error launching chess game: " + e);
                e.printStackTrace();
            }
        });
    }
}