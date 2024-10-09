/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chesslogic;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    public MainFrame() {
        // Set the title of the window
        super("Swing Application");

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(400, 300);

        // Center the window
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Use the event dispatch thread for Swing components
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create an instance of MainFrame and make it visible
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}