/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LevelsPanel levelsPanel;
    HighScorePanel highScorePanel;

    public MainFrame() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        setTitle("Nonogram Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new CardLayout());

        levelsPanel=new LevelsPanel(this);
        highScorePanel = new HighScorePanel(this);

        cardPanel.add(new MenuPanel(this), "Menu");
        cardPanel.add(levelsPanel, "Levels");
        cardPanel.add(new GamePanel(this, 1,levelsPanel,highScorePanel), "Game");
        cardPanel.add(new HelpPanel(this), "Help");
        cardPanel.add(highScorePanel, "HighScore");

        add(cardPanel);

        showView("Menu");
        setVisible(true);
    }

    public void showView(String view) {
        cardLayout.show(cardPanel, view);
        cardPanel.revalidate();
        cardPanel.repaint();
        this.revalidate();
        this.repaint();
    }


    public void showGame(int level) {
        cardPanel.add(new GamePanel(this, level,levelsPanel,highScorePanel), "Game");
        cardLayout.show(cardPanel, "Game");
    }



    private static void resetGameFiles() {
        try {
            Files.write(Paths.get("nonomrams/solvedlevels.txt"), "0\n0\n0\n".getBytes());
            Files.write(Paths.get("nonomrams/usersolved.txt"), "0\n0\n0\n0\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        resetGameFiles();
        SwingUtilities.invokeLater(MainFrame::new);
    }

}