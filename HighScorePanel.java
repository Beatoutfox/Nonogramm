/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class HighScorePanel extends JPanel {
    private MainFrame frame;
    private JTable highscoreTable;
    private String[] columnNames = {"Rank", "Level", "Date", "Score", "Time"};
    private Object[][] data;
    private BackgroundImg backgroundImage;

    public HighScorePanel(MainFrame mainFrame) {
        this.frame = mainFrame;
        setLayout(null);

        backgroundImage = new BackgroundImg("img/highscore.jpg", new Color(0, 0, 139, 120), new Color(0, 0, 64, 200));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                AudioManager.playSound("music/highscoreopen.wav");
            }
        });
        
        data = new Object[5][5];

        
        Buttons backButton = new Buttons();
        backButton.setBackButton(e -> frame.showView("Menu"));
        add(backButton.getBackButton());


        List<HighScore> highscores = HighScore.loadHighscores();

        // Prepare data for the table
        data = new Object[highscores.size()][5];
        if (!highscores.isEmpty()) {
            for (int i = 0; i < highscores.size(); i++) {
                HighScore hs = highscores.get(i);
                data[i][0] = hs.getRank();
                data[i][1] = hs.getLevel();
                data[i][2] = hs.getDate();
                data[i][3] = hs.getScore();
                data[i][4] = formatTime(hs.getTime());
            }
        }
        else {
            data = new Object[][]{{"No high scores available", "", "", "", ""}};
        }

        highscoreTable = new JTable(data, columnNames);
        highscoreTable.setModel(new DefaultTableModel(data, columnNames));
        highscoreTable.setFillsViewportHeight(true);
        highscoreTable.setDefaultEditor(Object.class, null);

        highscoreTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        highscoreTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        highscoreTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        highscoreTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        highscoreTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        
        JScrollPane scrollPane = new JScrollPane(highscoreTable);
        scrollPane.setBounds(100, 100, 600, 400);
        add(scrollPane);
    }

    public void refreshHighscores() {

        List<HighScore> highscores = HighScore.loadHighscores();
        if (!highscores.isEmpty()) {
            data = new Object[highscores.size()][5];
            for (int i = 0; i < highscores.size(); i++) {
                HighScore hs = highscores.get(i);
                data[i][0] = hs.getRank();
                data[i][1] = hs.getLevel();
                data[i][2] = hs.getDate();
                data[i][3] = hs.getScore();
                data[i][4] = formatTime(hs.getTime());
            }
        } else {
            data = new Object[][]{{"No high scores available", "", "", "", ""}};
        }

        highscoreTable.setModel(new DefaultTableModel(data, columnNames));
        highscoreTable.setFillsViewportHeight(true);
        highscoreTable.setDefaultEditor(Object.class, null);
    }
    

    private String formatTime(long timeInSeconds) {
        long minutes = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        backgroundImage.paint(g, this);
    }
}
