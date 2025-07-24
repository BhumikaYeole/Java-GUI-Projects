import javax.swing.*;
import java.awt.*;

class Landing extends JFrame {
    Landing() {
        // Fonts
        Font f = new Font("futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        // Background image
        ImageIcon backgroundImage = new ImageIcon("C:\\Pictures\\050.jpg"); // Replace with your image path
        JLabel background = new JLabel(backgroundImage);
        background.setBounds(0, 0, 800, 550);

        // Components
        JLabel l1 = new JLabel("Virtual Banking System", JLabel.CENTER);
        JButton b1 = new JButton("Admin");
        JButton b2 = new JButton("Existing Customer");
        JButton b3 = new JButton("New Customer");

        l1.setFont(f);
        b1.setFont(f2);
        b2.setFont(f2);
        b3.setFont(f2);

        // Set layout
        Container c = getContentPane();
        c.setLayout(null);

        // Set bounds for components
        l1.setBounds(150, 50, 500, 50);
        b1.setBounds(300, 150, 200, 50);
        b2.setBounds(300, 230, 200, 50);
        b3.setBounds(300, 310, 200, 50);

        // Add components to background
        background.add(l1);
        background.add(b1);
        background.add(b2);
        background.add(b3);

        // Add background to container
        c.add(background);

        // Button actions
        b1.addActionListener(
                a -> {
                    new Alogin();
                    dispose();
                }
        );

        b2.addActionListener(
                a -> {
                    new Elogin();
                    dispose();
                }
        );

        b3.addActionListener(
                a -> {
                    new Nlogin();
                    dispose();
                }
        );

        // Frame settings
        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Landing Page");
    }

    public static void main(String[] args) {
        Landing a = new Landing();
    }
}
