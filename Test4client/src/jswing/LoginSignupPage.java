package jswing;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import testapp.Client;

public class LoginSignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;
    
    // Custom colors
    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color FIELD_BACKGROUND = new Color(60, 60, 60);
    private static final Color GREEN_BUTTON = new Color(92, 184, 92);
    private static final Color TEXT_COLOR = new Color(200, 200, 200);
    
    // Uniform component dimensions
    private static final Dimension COMPONENT_SIZE = new Dimension(280, 40);
    private static final int VERTICAL_SPACING = 15;
    // Validation constants
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    
    public LoginSignupPage(Client client) {
        this.client = client;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Chess Game - Login/Signup");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(DARK_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Add components vertically
        JLabel usernameLabel = createStyledLabel("Username:");
        usernameField = createStyledTextField();
        JLabel passwordLabel = createStyledLabel("Password:");
        passwordField = createStyledPasswordField();
        
        // Center align all components
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with vertical spacing
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Create and add buttons
        JButton signupButton = createStyledButton("Sign Up", true);
        JButton loginButton = createStyledButton("Log In", false);
        JButton exitButton = createStyledButton("Exit", false);
        
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(signupButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(exitButton);
        mainPanel.add(Box.createVerticalGlue());
        
        // Add action listeners
        loginButton.addActionListener(e -> loginAction());
        signupButton.addActionListener(e -> signupAction());
        exitButton.addActionListener(e -> System.exit(0));
        
        // Add main panel to frame
        add(mainPanel);
        
        // Set frame background
        getContentPane().setBackground(DARK_BACKGROUND);
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        // Set preferred size for consistent layout but don't restrict maximum size
        label.setPreferredSize(new Dimension(COMPONENT_SIZE.width, 20));
        label.setMaximumSize(new Dimension(COMPONENT_SIZE.width, 20));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(FIELD_BACKGROUND.darker(), 1),
            new EmptyBorder(5, 10, 5, 10)));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Set fixed size
        field.setPreferredSize(COMPONENT_SIZE);
        field.setMaximumSize(COMPONENT_SIZE);
        field.setMinimumSize(COMPONENT_SIZE);
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(FIELD_BACKGROUND.darker(), 1),
            new EmptyBorder(5, 10, 5, 10)));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Set fixed size
        field.setPreferredSize(COMPONENT_SIZE);
        field.setMaximumSize(COMPONENT_SIZE);
        field.setMinimumSize(COMPONENT_SIZE);
        
        return field;
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
    
    private boolean validateInputs(String username, String password, boolean isSignup) {
        if (username == null || username.trim().isEmpty()) {
            showErrorMessage("Username cannot be empty!");
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            showErrorMessage("Password cannot be empty!");
            return false;
        }
        
        // Additional password length check for signup
        if (isSignup && password.length() < MIN_PASSWORD_LENGTH) {
            showErrorMessage("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long!");
            return false;
        }
        
        return true;
    }
    
    private void loginAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (!validateInputs(username, password, false)) {
            return;
        }
        
        if (client.obj.login(username, password)) {
            new MainMenuPage(client, username).setVisible(true);
            this.dispose();
        } else {
            showErrorMessage("Login failed! Invalid credentials.");
        }
    }
    
    private void signupAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (!validateInputs(username, password, true)) {
            return;
        }
        
        if (client.obj.signup(username, password)) {
            loginAction();
        } else {
            showErrorMessage("Signup failed! Username already exists.");
        }
    }
    
    private void showErrorMessage(String message) {
        UIManager.put("OptionPane.background", DARK_BACKGROUND);
        UIManager.put("Panel.background", DARK_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}