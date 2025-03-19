/*
Brustur-Buksa Beatrice
521/2
* */
import java.util.stream.IntStream;

public class GameController {
    private int lives;
    private boolean solved;
    private GamePanel gamePanel;
    private PuzzleLoader puzzleLoader;

    public GameController(GamePanel gamePanel,PuzzleLoader pp) {
        this.gamePanel = gamePanel;
        this.puzzleLoader=pp;
        this.lives = 3;
        this.solved = false;
    }

    public void incorrectMove() {
        lives=puzzleLoader.getLife();
        lives-=1;
        gamePanel.updateLives(lives);
    }

    private boolean checkSolved(boolean[][] solution, Integer[][] userSolution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution.length; j++) {
                if (solution[i][j] && userSolution[i][j] != 1) {
                    return false;
                }
                else if (!solution[i][j] && userSolution[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void isSolved(boolean[][] solution, Integer[][] userSolution) {
        solved= checkSolved(solution, userSolution);
    }

    private boolean isRowSolved(int row, boolean[][] solution, Integer[][] userSolution) {
        /*for (int col = 0; col < solution.length; col++) {
            if (solution[row][col] && userSolution[row][col] != 1) {
                return false;
            }
            if (!solution[row][col] && userSolution[row][col] == 1) {
                return false;
            }
        }
        return true;*/

        return IntStream.range(0, solution.length)
                .allMatch(col ->
                        (solution[row][col] && userSolution[row][col] == 1) ||
                                (!solution[row][col] && userSolution[row][col] != 1)
                );
    }

    private boolean isColumnSolved(int col, boolean[][] solution, Integer[][] userSolution) {
        /*for (int row = 0; row < solution.length; row++) {
            if (solution[row][col] && userSolution[row][col] != 1) {
                return false;
            }
            if (!solution[row][col] && userSolution[row][col] == 1) {
                return false;
            }
        }
        return true;*/
        return IntStream.range(0, solution.length)
                .allMatch(row ->
                        (solution[row][col] && userSolution[row][col] == 1) ||
                                (!solution[row][col] && userSolution[row][col] != 1)
                );
    }

    public boolean getSolved(boolean[][] solution, Integer[][] userSolution) {
        isSolved(solution, userSolution);
        return solved;
    }

    public boolean getCSolved(int col, boolean[][] solution, Integer[][] userSolution) {
        return isColumnSolved(col, solution, userSolution);
    }

    public boolean getRSolved(int row, boolean[][] solution, Integer[][] userSolution) {
        return isRowSolved(row, solution, userSolution);
    }

}
