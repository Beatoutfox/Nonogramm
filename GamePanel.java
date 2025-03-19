/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GamePanel extends JPanel {
    private MainFrame frame;
    private BackgroundImg background;
    private GameController controller;
    private int lives;
    private boolean[][] solution;
    private JLabel livesLabel;
    private Integer[][] userSolution;
    private int level;
    private PuzzleLoader puzzleLoader;
    private LevelsPanel levelsPanel;
    private HighScorePanel highScorePanel;
    private AudioManager audioManager;
    private Timer timer;
    private long elapsedTime;
    private JLabel timerLabel;
    Map<Integer, List<List<Integer>>> hints;

    public GamePanel(MainFrame frame,int level,LevelsPanel levelsPanel,HighScorePanel highScorePanel) {
        this.levelsPanel = levelsPanel;
        this.highScorePanel=highScorePanel;
        this.frame = frame;
        this.level=level;
        
        this.audioManager = new AudioManager();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                audioManager.playMusic("music/game.wav");
            }
            @Override
            public void componentHidden(ComponentEvent e) {
                audioManager.stopMusic();
            }
        });

        background = new BackgroundImg("img/gameback.jpg", new Color(0, 0, 0, 150), new Color(0, 0, 0, 200));
        setLayout(null);

        addResetButton();
        try {
            puzzleLoader = new PuzzleLoader(level);

            this.controller=new GameController(this,puzzleLoader);
            Buttons backButton = new Buttons();
            backButton.setBackButton(e -> {
                puzzleLoader.setLife(lives);
                stopTimer();
                puzzleLoader.setTime(elapsedTime);
                puzzleLoader.saveUserSolution();
                audioManager.stopMusic();
                frame.showView("Levels");
            });
            add(backButton.getBackButton());

            solution = puzzleLoader.getPuzzle();
            hints = puzzleLoader.getHints();
            userSolution = puzzleLoader.getUserSolution();
            lives=puzzleLoader.getSavedLife();
            livesLabel = createLivesLabel();
            updateLives(lives);
            add(livesLabel);

            this.elapsedTime = puzzleLoader.getTime();
            this.timerLabel = createTimerLl();
            add(timerLabel);
            startTimer();
        }
        catch (Exception e) {
            e.printStackTrace();
            showError();
            return;
        }

        addHintsAndGrid(solution.length, hints);
    }

    private JLabel createTimerLl() {
        JLabel label = new JLabel(formatTime(elapsedTime));
        label.setForeground(Color.WHITE);
        label.setBounds(600, 100, 200, 30);
        return label;
    }
    private void startTimer() {
        if (timer != null && timer.isRunning()) return;
    
        timer = new Timer(1000, e -> {
            elapsedTime++;
            timerLabel.setText(formatTime(elapsedTime));
        });
        timer.start();
    }
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
        puzzleLoader.setTime(elapsedTime);
    }
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("Time: %02d:%02d", minutes, remainingSeconds);
    }

    private JLabel createLivesLabel() {
        JLabel label = new JLabel("Lives: ❤❤❤");
        label.setForeground(Color.RED);
        label.setBounds(600, 500, 100, 30);
        return label;
    }

    private void addResetButton() {
        Buttons resetButton = new Buttons();
        resetButton.setResetButton();
        JButton res=resetButton.getResetButton();
        res.addActionListener(e-> {
            try {
                resetLevel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(res);
    }

    private void resetLevel() throws IOException {
        puzzleLoader.resetUserSolution();
        removeAll();

        stopTimer();
        elapsedTime = 0;
        startTimer();

        this.audioManager = new AudioManager();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                audioManager.playMusic("music/game.wav");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                audioManager.stopMusic();
            }
        });

        background = new BackgroundImg("img/gameback.jpg", new Color(0, 0, 0, 150), new Color(0, 0, 0, 200));
        setLayout(null);

        addResetButton();
        try {
            puzzleLoader = new PuzzleLoader(level);

            this.controller=new GameController(this,puzzleLoader);
            Buttons backButton = new Buttons();
            backButton.setBackButton(e -> {
                puzzleLoader.setLife(lives);
                stopTimer();
                puzzleLoader.setTime(elapsedTime);
                puzzleLoader.saveUserSolution();
                audioManager.stopMusic();
                frame.showView("Levels");
            });
            add(backButton.getBackButton());

            solution = puzzleLoader.getPuzzle();
            hints = puzzleLoader.getHints();
            userSolution = puzzleLoader.getUserSolution();
            lives=puzzleLoader.getSavedLife();
            livesLabel = createLivesLabel();
            updateLives(lives);
            add(livesLabel);

            this.elapsedTime = puzzleLoader.getTime();
            this.timerLabel = createTimerLl();
            add(timerLabel);
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
            showError();
            return;
        }
        addHintsAndGrid(solution.length, hints);

        revalidate();
        repaint();
    }

    private void addHintsAndGrid(int size, Map<Integer, List<List<Integer>>> hints) {
        int cellSize = 400 / size;
        int hintAreaSize = 100;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (userSolution == null || userSolution[i][j] == null) {
                    throw new IllegalStateException("userSolution is not properly initialized!");
                }
                JButton button = Buttons.setCellButton(hintAreaSize + j * cellSize, hintAreaSize + i * cellSize, cellSize,level,userSolution,solution,i,j,controller,this);
                
                if ((i + 1) % 5 == 0 && (i + 1) != size) {
                    button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.GRAY));
                }
                if ((j + 1) % 5 == 0 && (j + 1) != size) {
                    button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.GRAY));
                }
                if ((i + 1) % 5 == 0 && (j + 1) % 5 == 0 && (i + 1) != size && (j + 1) != size) {
                    button.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.GRAY));
                }
                add(button);
            }
        }

        List<List<Integer>> rowHints = hints.get(1);
        IntStream.range(0, size).forEach(i -> {
            JLabel hintLabel = new JLabel(formatHint(rowHints.get(i)));
            hintLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            hintLabel.setBounds(0, hintAreaSize + i * cellSize, hintAreaSize - 10, cellSize);
            hintLabel.setForeground(Color.PINK);
            add(hintLabel);
        });
        /*for (int i = 0; i < size; i++) {
            JLabel hintLabel = new JLabel(formatHint(rowHints.get(i)));
            hintLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            hintLabel.setBounds(0, hintAreaSize + i * cellSize, hintAreaSize - 10, cellSize);
            hintLabel.setForeground(Color.PINK);
            add(hintLabel);
        }*/

        List<List<Integer>> columnHints = hints.get(2);
        /*
        for (int j = 0; j < size; j++) {
            int hintHeight = 20;
            List<Integer> colHint = columnHints.get(j);
            for (int k = 0; k < colHint.size(); k++) {
                JLabel hintLabel = new JLabel(String.valueOf(colHint.get(k)));
                hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
                hintLabel.setBounds(hintAreaSize + j * cellSize, hintAreaSize - (colHint.size() - k) * hintHeight, cellSize, hintHeight);
                hintLabel.setForeground(Color.PINK);
                add(hintLabel);
            }
            if (colHint.isEmpty()) {
                JLabel hintLabel = new JLabel("0");
                hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
                hintLabel.setBounds(hintAreaSize + j * cellSize,hintAreaSize - hintHeight, cellSize, hintHeight);
                hintLabel.setForeground(Color.PINK);
                add(hintLabel);
            }
        }
         */
        IntStream.range(0, size).forEach(j -> {
            int hintHeight = 20;
            List<Integer> colHint = columnHints.get(j);
            IntStream.range(0, colHint.size()).forEach(k -> {
                JLabel hintLabel = new JLabel(String.valueOf(colHint.get(k)));
                hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
                hintLabel.setBounds(hintAreaSize + j * cellSize, hintAreaSize - (colHint.size() - k) * hintHeight, cellSize, hintHeight);
                hintLabel.setForeground(Color.PINK);
                add(hintLabel);
            });

            if (colHint.isEmpty()) {
                JLabel hintLabel = new JLabel("0");
                hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
                hintLabel.setBounds(hintAreaSize + j * cellSize, hintAreaSize - hintHeight, cellSize, hintHeight);
                hintLabel.setForeground(Color.PINK);
                add(hintLabel);
            }
        });

        setBackground(new Color(0, 0, 0, 0));
    }

    private String formatHint(List<Integer> hints) {
        return hints.isEmpty() ? "0" : hints.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }

    private void showError() {
        JLabel errorLabel = new JLabel("Failed to load puzzle.");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(300, 300, 200, 50);
        add(errorLabel);
    }
    
    public void updateLives(int livesLost) {
        lives=livesLost;
        if (livesLost>=0)
        {
            livesLabel.setText("Lives: " + "❤".repeat(livesLost));
        }
        
        if (livesLost <= 0) {
            AudioManager.playSound("music/lose.wav");
            showGameOverMessage();
        }
    }
    
    private void showGameOverMessage() {
        stopTimer();
        puzzleLoader.setTime(elapsedTime);
        try {
            resetLevel();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        JOptionPane.showMessageDialog(this, "You lost all your lives!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

    }

    private int validateHints(boolean[][] solution, Integer[][] userSolution) {
        int size = solution.length;

        /*for (int i = 0; i < size; i++) {
            if (controller.isRowSolved(i, solution, userSolution)) {
                colorRowHints(i, true);
                sp++;
            }
            else {
                colorRowHints(i, false);
            }
        }*/
        int solvedRows = (int) IntStream.range(0, size)
                .peek(i -> colorRowHints(i, controller.getRSolved(i, solution, userSolution)))
                .filter(i -> controller.getRSolved(i, solution, userSolution))
                .count();
        int solvedColumns = (int) IntStream.range(0, size)
                .peek(j -> colorColumnHints(j, controller.getCSolved(j, solution, userSolution)))
                .filter(j -> controller.getCSolved(j, solution, userSolution))
                .count();

        return solvedRows + solvedColumns;

        /*for (int j = 0; j < size; j++) {
            if (controller.isColumnSolved(j, solution, userSolution)) {
                colorColumnHints(j, true);
                sp++;
            } 
            else {
                colorColumnHints(j, false);
            }
        }
        return sp;
         */
    }

    public int getCRgood(boolean[][] solution, Integer[][] userSolution) {
        return validateHints(solution, userSolution);
    }

    private void colorRowHints(int row, boolean solved) {
        Component[] components = getComponents();

        for (Component component : components) {
            if (component instanceof JLabel hintLabel) {
                Rectangle bounds = hintLabel.getBounds();
                if (bounds.y == (100 + row * (400 / solution.length)) && bounds.x == 0) {
                    hintLabel.setForeground(solved ? Color.GREEN : Color.PINK);
                }
            }
        }
    }
    
    private void colorColumnHints(int col, boolean solved) {
        int cellSize = 400 / solution.length;
        int hintAreaSize = 100;
        int startX = hintAreaSize + col * cellSize;

        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JLabel hintLabel) {
                Rectangle bounds = hintLabel.getBounds();
                if (bounds.x == startX && bounds.y < hintAreaSize) {
                    hintLabel.setForeground(solved ? Color.GREEN : Color.PINK);
                }
            }
        }
    }

    public void onPuzzleSolved(int level) {
        stopTimer();
        puzzleLoader.setTime(elapsedTime);
        puzzleLoader.saveUserSolution();

        AudioManager.playSound("music/win.wav");
        JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle!");

        puzzleLoader.setLevelasSolved();
        levelsPanel.updateLevelStatus(level);

        HighScore.addScore(level, elapsedTime, solution.length,lives);
        highScorePanel.refreshHighscores();
        new LevelsPanel(frame);
        
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.paint(g, this);
    }
}