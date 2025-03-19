/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class LevelsPanel extends JPanel {
    private Set<Integer> solvedLevels;
    private MainFrame frame;

    public LevelsPanel(MainFrame mainframe) {
        frame=mainframe;
        setLayout(new GridLayout(5, 5, 10, 10));
        setBackground(new Color(30, 30, 30));

        try {
            PuzzleLoader puzzleLoader = new PuzzleLoader(1);
            
            solvedLevels = puzzleLoader.getSolvedLevels();

            int numberofLevels = 24;
            for (int i = 1; i <= numberofLevels; i++) {
                JButton levelButton = getjButton( i);

                add(levelButton);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Buttons backButton = new Buttons();
        backButton.setBackButton(e -> frame.showView("Menu"));
        add(backButton.getBackButton());
    }

    private JButton getjButton(int i) {
        JButton levelButton = new JButton("" + i);
        levelButton.setFont(new Font("Arial", Font.BOLD, 12));
        levelButton.setForeground(Color.WHITE);
        levelButton.setBackground(solvedLevels.contains(i) ? new Color(93, 245, 88) : new Color(91, 223, 235));
        levelButton.setFocusPainted(false);

        levelButton.addActionListener(e -> frame.showGame(i));
        return levelButton;
    }

    public void updateLevelStatus(int level) {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton button) {
                if (button.getText().equals("" + level)) {
                    button.setBackground(Color.GREEN);
                }
            }
        }
    }
    
}
