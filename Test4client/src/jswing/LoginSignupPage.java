package jswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import testapp.Client;

public class LoginSignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Client client;

    public LoginSignupPage(Client client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Chess Game - Login/Signup");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        JButton exitButton = new JButton("Exit");

        loginButton.addActionListener(e -> loginAction());
        signupButton.addActionListener(e -> signupAction());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(loginButton);
        panel.add(signupButton);
        panel.add(exitButton);

        return panel;
    }

    private void loginAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (client.obj.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new MainMenuPage(client, username).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login failed! Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signupAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (client.obj.signup(username, password)) {
            JOptionPane.showMessageDialog(this, "Signup successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed! Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}