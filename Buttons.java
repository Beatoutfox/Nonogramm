/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Buttons {
    private JButton backButton;
    private JButton menuButton;
    private JButton resetButton;

    private static JButton createBackButton(ActionListener actionListener) {
        JButton backButton = new JButton("←Back");
        backButton.setBounds(5, 5, 80, 30);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                backButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                backButton.setForeground(Color.WHITE);
            }
        });

        backButton.addActionListener(actionListener);
        return backButton;
    }

    private static JButton createResetButton(){
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBounds(600, 50, 100, 30);
        resetButton.setForeground(Color.WHITE);
        resetButton.setContentAreaFilled(false);
        resetButton.setBorderPainted(false);

        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                resetButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                resetButton.setForeground(Color.WHITE);
            }
        });

        return resetButton;
    }

    private static JButton createMenuButton(String text, int x, int y, int width, int height, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        button.addActionListener(actionListener);
        return button;
    }

    private static JButton createCellButton(int x, int y, int cellSize, int level, Integer[][] userSolution, boolean[][] solution, int i, int j, GameController gameController, GamePanel gamePanel) {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(cellSize, cellSize));
        button.setBounds(x, y, cellSize, cellSize);
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
        
        loadinSavedButtonState(i,j,userSolution,button);

        gamePanel.getCRgood(solution, userSolution);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int sp1=gamePanel.getCRgood(solution, userSolution);
                boolean ok=true;
                if (userSolution[i][j] == 3) {
                    AudioManager.playSound("music/press.wav");
                    return;
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (userSolution[i][j] == 0) {
                        //0->2
                        userSolution[i][j] = 2;
                        button.setBackground(Color.WHITE);
                        button.setText("");
                    }
                    else if (userSolution[i][j] == 1) {
                        //1->0
                        userSolution[i][j] = 0;
                        button.setBackground(Color.WHITE);
                        button.setText("X");
                        button.setForeground(Color.GRAY);
                        button.setFont(new Font("Arial", Font.BOLD, 16));
                    }
                    else if (userSolution[i][j] == 2) {
                        //2 -> 0
                        userSolution[i][j] = 0;
                        button.setBackground(Color.WHITE);
                        button.setText("X");
                        button.setForeground(Color.GRAY);
                        button.setFont(new Font("Arial", Font.BOLD, 16));
                    }
                }
                else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (userSolution[i][j] == 2 && solution[i][j]) {
                        //2->1
                        userSolution[i][j] = 1;
                        button.setBackground(Color.BLACK);
                        button.setText("");
                    }
                    else if (userSolution[i][j] == 2 && !solution[i][j]) {
                        //2->3
                        userSolution[i][j] = 3;
                        button.setBackground(Color.RED);
                        button.setText("X");
                        button.setForeground(Color.BLACK);
                        button.setFont(new Font("Arial", Font.BOLD, 16));
                        gameController.incorrectMove();
                        ok=false;
                    }
                    else if (userSolution[i][j] == 1) {
                        //1->2
                        userSolution[i][j] = 2;
                        button.setBackground(Color.WHITE);
                        button.setText("");
                    }
                    else if (userSolution[i][j] == 0 && solution[i][j]) {
                        //0->1
                        userSolution[i][j] = 1;
                        button.setBackground(Color.BLACK);
                        button.setText("");
                    }
                    else if (userSolution[i][j] == 0 && !solution[i][j]) {
                        //0->3
                        userSolution[i][j] = 3;
                        button.setBackground(Color.RED);
                        button.setText("X");
                        button.setForeground(Color.BLACK);
                        button.setFont(new Font("Arial", Font.BOLD, 16));
                        gameController.incorrectMove();
                        ok=false;
                    }
                }
                int count = (int) Arrays.stream(userSolution).flatMap(Arrays::stream).filter(num -> num == 3).count();
                count=3-count;
                PuzzleLoader.updateUserSolutionFile(userSolution, level,count);
                gamePanel.updateLives(count);

                int sp2=gamePanel.getCRgood(solution, userSolution);
                if(sp2>sp1){
                    AudioManager.playSound("music/good.wav");
                }
                else if(ok){
                    AudioManager.playSound("music/press.wav");
                }
                else{
                    AudioManager.playSound("music/wrong.wav");
                }
                boolean solved = gameController.getSolved(solution, userSolution);
                if (solved) {
                    gamePanel.onPuzzleSolved(level);
                }
            }
        });

        return button;
    }

    private static void loadinSavedButtonState(int i,int j, Integer[][] userSolution,JButton button){
        if (userSolution[i][j] == 0) {
            button.setBackground(Color.WHITE);
            button.setText("X");
            button.setForeground(Color.GRAY);
            button.setFont(new Font("Arial", Font.BOLD, 16));
        }
        else if (userSolution[i][j] == 1) {
            button.setBackground(Color.BLACK);
            button.setText("");
        }
        else if (userSolution[i][j] == 2) {
            button.setBackground(Color.WHITE);
            button.setText("");
        }
        else if (userSolution[i][j] == 3) {
            button.setBackground(Color.RED);
            button.setText("X");
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Arial", Font.BOLD, 16));
        }
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(ActionListener actionListener) {
        this.backButton = createBackButton(actionListener);
    }

    public JButton getResetButton(){
        return resetButton;
    }

    public void setResetButton(){
        this.resetButton=createResetButton();
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(String text, int x, int y, int width, int height, ActionListener actionListener) {
        this.menuButton = createMenuButton(text, x, y, width, height, actionListener);
    }

    public static JButton setCellButton(int x, int y, int cellSize,int level, Integer[][] userSolution,boolean[][] solution,int  i, int j,GameController gameController,GamePanel gamePanel){
        return createCellButton(x, y, cellSize,level, userSolution, solution, i, j, gameController, gamePanel);
    }
}

 