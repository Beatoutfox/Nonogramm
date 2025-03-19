/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class HelpPanel extends JPanel {
    private BackgroundImg backgroundImage;
    private AudioManager audioManager;
    private MainFrame frame;

    public HelpPanel(MainFrame mainframe) {
        frame=mainframe;
        backgroundImage = new BackgroundImg("img/helpbackround.jpg", new Color(0, 0, 139, 120), new Color(0, 0, 64, 200));
        setLayout(null);

        this.audioManager = new AudioManager();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                audioManager.playMusic("music/help.wav");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                audioManager.stopMusic();
            }

        });



        Buttons backButton = new Buttons();
        backButton.setBackButton(e -> frame.showView("Menu"));


        JLabel helpText = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to Nonogram Game!<br><br>"
                + "1. Fill cells based on the numbers.<br>"
                + "2. Each number represents consecutive filled cells.<br>"
                + "3. Solve to match the correct pattern.<br>"
                + "</div></html>");
        helpText.setHorizontalAlignment(SwingConstants.CENTER);
        helpText.setVerticalAlignment(SwingConstants.CENTER);
        helpText.setFont(new Font("Arial", Font.BOLD, 16));
        helpText.setForeground(Color.WHITE);
        helpText.setBounds(0, 0, 800, 600); // Center the text in the panel

        add(backButton.getBackButton());
        add(helpText);
        this.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        backgroundImage.paint(g, this);
    }
}
