/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private BackgroundImg backgroundImage;
    private MainFrame frame;

    public MenuPanel(MainFrame mainframe) {
        frame=mainframe;

        backgroundImage = new BackgroundImg("img/mainframebackground.jpg", new Color(0, 0, 0, 150), new Color(0, 0, 0, 200));

        setLayout(null);

        Buttons startButton = new Buttons();
        startButton.setMenuButton("Levels", 300, 100, 200, 50, e -> frame.showView("Levels"));

        Buttons helpButton = new Buttons();
        helpButton.setMenuButton("Help", 300, 230, 200, 50, e -> {
            frame.showView("Help");
        });

        Buttons scoreButtons = new Buttons();
        scoreButtons.setMenuButton("High Score", 300, 360, 200, 50, e -> frame.showView("HighScore"));
        Buttons exitButton = new Buttons();
        exitButton.setMenuButton("Exit", 300, 500, 200, 50, e -> System.exit(0));

        add(startButton.getMenuButton());
        add(helpButton.getMenuButton());
        add(scoreButtons.getMenuButton());
        add(exitButton.getMenuButton());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        backgroundImage.paint(g, this);
    }
}
