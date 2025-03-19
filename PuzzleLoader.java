/*
Brustur-Buksa Beatrice
521/2
* */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PuzzleLoader {
    private int level;
    private int life;
    private boolean[][] puzzle;
    private Map<Integer, List<List<Integer>>> hints;
    private Integer[][] userSolution;
    private String USER_SOLUTION_PATH = "nonomrams/usersolved.txt";
    private Set<Integer> solvedLevels;
    private long time;

    public PuzzleLoader(int puzzleNumber) throws IOException {
        level = puzzleNumber;
        puzzle = loadPuzzle(level);
        hints = getHints(puzzle);
        loadPuzzle(level);
        loadSavedUsersolution();
        solvedLevels=loadSolvedLevels();
    }

    private static boolean[][] loadPuzzle(int puzzleNumber) throws IOException {
        String filePath = String.format("nonomrams/puzzle%d.txt", puzzleNumber);

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        int size = Integer.parseInt(lines.getFirst().trim());

        boolean[][] matrix = new boolean[size][size];

        for (int i = 1; i <= size; i++) {
            String[] values = lines.get(i).split("\\s+");

            for (int j = 0; j < size; j++) {
                matrix[i - 1][j] = values[j].equals("1");
            }
        }

        return matrix;
    }

    private static Map<Integer, List<List<Integer>>> getHints(boolean[][] matrix){
        Map<Integer, List<List<Integer>>> hints = new HashMap<>();
        int n=matrix.length;
        List<List<Integer>> r=new ArrayList<>();
        List<List<Integer>> c=new ArrayList<>();

        for(int i=0; i<n; i++){
            List<Integer> row=new ArrayList<>();
            List<Integer> col=new ArrayList<>();
            int count_r=0;
            int count_c=0;
            for(int j=0; j<n; j++){
                if(matrix[i][j]){
                    count_r++;
                }
                else{
                    if(count_r>0){
                        row.add(count_r);
                        count_r=0;
                    }
                }
                if(matrix[j][i]){
                    count_c++;
                }
                else{
                    if(count_c>0){
                        col.add(count_c);
                        count_c=0;
                    }
                }
            }
            if(count_r>0){
                row.add(count_r);
            }
            if(count_c>0){
                col.add(count_c);
            }
            r.add(row);
            c.add(col);
        }
        hints.put(1, r);
        hints.put(2, c);
        return hints;
    }

    private void loadSavedUsersolution(){
        int size = puzzle.length;
        userSolution=new Integer[size][size];

        File file = new File(USER_SOLUTION_PATH);
        if (!file.exists()) {
            resetUserSolution();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int savedLevel = Integer.parseInt(reader.readLine());
            life=Integer.parseInt(reader.readLine());
            Integer.parseInt(reader.readLine());
            time=Long.parseLong(reader.readLine());

            if (savedLevel != level) {
                resetUserSolution();
                return;
            }

            for (int i = 0; i < size; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < size; j++) {
                    userSolution[i][j] = Integer.parseInt(line[j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resetUserSolution();
        }
    }

    public static void updateUserSolutionFile(Integer[][] userSolution, int level, int life) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("nonomrams/usersolved.txt"))) {
            writer.write(""+level);
            writer.newLine();
            writer.write(""+life);
            writer.newLine();
            writer.write(""+userSolution.length);
            writer.newLine();
            writer.write(""+0);
            writer.newLine();
            for (Integer[] row : userSolution) {
                for (Integer cell : row) {
                    writer.write(cell + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUserSolution() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_SOLUTION_PATH))) {
           writer.println(level);
            writer.println(life);
            writer.println(puzzle.length);
            writer.println(time);
            for (Integer[] row : userSolution) {
                for (Integer cell : row) {
                    writer.print(cell + " ");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetUserSolution() {
        int size=puzzle.length;
        life=3;
        userSolution = new Integer[size][size];
        for (Integer[] row : userSolution) {
            Arrays.fill(row, 2);
        }
        time=0;
        saveUserSolution();
    }

    private static Set<Integer> loadSolvedLevels() {
        Set<Integer> solvedLevels = new HashSet<>();
        File file = new File("nonomrams/solvedlevels.txt");

        if (!file.exists()) {
            return solvedLevels;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                    int level = Integer.parseInt(line.trim());
                    solvedLevels.add(level);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return solvedLevels;
    }

    private void markLevelAsSolved() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("nonomrams/solvedlevels.txt", true))) {
            writer.println(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(boolean[][] puzzle) {
        this.puzzle = puzzle;
    }

    public Map<Integer, List<List<Integer>>> getHints() {
        return hints;
    }

    public void setHints(Map<Integer, List<List<Integer>>> hints) {
        this.hints = hints;
    }

    public Integer[][] getUserSolution() {
        loadSavedUsersolution();
        return userSolution;
    }

    public void setUserSolution(Integer[][] puzzleMatrix) {
        this.userSolution = puzzleMatrix;
    }

    public int getSavedLife(){
        return life;
    }

    public Set<Integer> getSolvedLevels() {
        return solvedLevels;
    }

    public void setLevelasSolved(){
        markLevelAsSolved();
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time=time;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
