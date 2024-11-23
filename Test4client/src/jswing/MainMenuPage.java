package jswing;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import testapp.Client;
import static testapp.Client.obj;
import testapp.matchData;
import chesslogic.ChessGame;

public class MainMenuPage extends JFrame {
    private String username;
    private static Client client;
    
    // Custom colors (matching LoginSignupPage)
    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color FIELD_BACKGROUND = new Color(60, 60, 60);
    private static final Color GREEN_BUTTON = new Color(92, 184, 92);
    private static final Color TEXT_COLOR = new Color(200, 200, 200);
    
    // Uniform component dimensions
    private static final Dimension COMPONENT_SIZE = new Dimension(280, 40);
    private static final int VERTICAL_SPACING = 15;
    
    public MainMenuPage(Client client, String username) {
        this.client = client;
        this.username = username;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Chess Game - Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Create components
        JLabel welcomeLabel = createStyledLabel("Welcome, " + username + "!", true);
        JLabel statsLabel = createStatsLabel();
        JButton startGameButton = createStyledButton("Start Game", true);
        JButton logoutButton = createStyledButton("Logout", false);
        
        // Center align components
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with spacing
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(welcomeLabel);
        mainPanel.add(statsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING*2)));
        mainPanel.add(startGameButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
        mainPanel.add(logoutButton);
        mainPanel.add(Box.createVerticalGlue());
        
        // Add action listeners
        startGameButton.addActionListener(e -> startGameAction());
        logoutButton.addActionListener(e -> logoutAction());
        
        // Add main panel to frame
        add(mainPanel);
        
        // Set frame background
        getContentPane().setBackground(DARK_BACKGROUND);
    }
    
    private JLabel createStyledLabel(String text, boolean isTitle) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", isTitle ? Font.BOLD : Font.PLAIN, isTitle ? 24 : 14));
        
        // Set size for consistent layout
        Dimension labelSize = new Dimension(COMPONENT_SIZE.width, isTitle ? 40 : 20);
        label.setPreferredSize(labelSize);
        label.setMaximumSize(labelSize);
        
        return label;
    }
    
    private JLabel createStatsLabel() {
        String stats = client.obj.getUserStats(username);
        JLabel statsLabel = new JLabel(stats, SwingConstants.CENTER);
        statsLabel.setForeground(TEXT_COLOR);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Set size for consistent layout
        Dimension labelSize = new Dimension(COMPONENT_SIZE.width, 60);
        statsLabel.setPreferredSize(labelSize);
        statsLabel.setMaximumSize(labelSize);
        
        return statsLabel;
    }
    
    private JButton createStyledButton(String text, boolean isGreen) {
        JButton button = new JButton(text);
        if (isGreen) {
            button.setBackground(GREEN_BUTTON);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(FIELD_BACKGROUND);
            button.setForeground(TEXT_COLOR);
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Set fixed size
        button.setPreferredSize(COMPONENT_SIZE);
        button.setMaximumSize(COMPONENT_SIZE);
        button.setMinimumSize(COMPONENT_SIZE);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(isGreen ? GREEN_BUTTON.darker() : FIELD_BACKGROUND.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(isGreen ? GREEN_BUTTON : FIELD_BACKGROUND);
            }
        });
        
        return button;
    }
    
    public void startGameAction() {
    JOptionPane.showMessageDialog(this, "Game starting soon!", "Game Start", JOptionPane.INFORMATION_MESSAGE);
    matchData res = client.obj.matchMe();
    client.id = res.id;
    if (res.k == 1) 
        client.isWhitePlayer = true;
    else
        client.isWhitePlayer = false;

    System.out.println("You have been paired. Your id = " + res.id);
    launchChessGame(); // Now calls the instance method
}
    
    private void logoutAction() {
        // Style the confirmation dialog
        UIManager.put("OptionPane.background", DARK_BACKGROUND);
        UIManager.put("Panel.background", DARK_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginSignupPage(client).setVisible(true);
            this.dispose();
        }
    }
    
    // Change the method from static to instance method
private void launchChessGame() {
    System.out.println("Attempting to launch chess game...");
    SwingUtilities.invokeLater(() -> {
        try {
            System.out.println("Initializing ChessGame...");
            new ChessGame( client,username);
            System.out.println("ChessGame initialized successfully.");
            // Dispose of the MainMenuPage window
            this.dispose();
        } catch (Exception e) {
            System.out.println("Error launching chess game: " + e);
            e.printStackTrace();
        }
    });
}
}