/*
Brustur-Buksa Beatrice
521/2
* */
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class HighScore {
    private static String HIGHSCORE_FILE = "nonomrams/highscore.txt";

    private int rank;
    private int level;
    private long time;//seconds
    private long score;
    private String date;

    public HighScore(int rank, int level, long time, long score, String date) {
        this.rank = rank;
        this.level = level;
        this.time = time;
        this.score = score;
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public int getLevel() {
        return level;
    }

    public long getTime() {
        return time;
    }

    public long getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public static List<HighScore> loadHighscores() {
        List<HighScore> highscores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int rank = Integer.parseInt(parts[0]);
                int level = Integer.parseInt(parts[1]);
                long time = Long.parseLong(parts[2]);
                long score = Long.parseLong(parts[3]);
                String date = parts[4];

                highscores.add(new HighScore(rank, level, time, score, date));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highscores;
    }

    public static void saveHighscores(List<HighScore> highscores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (HighScore hs : highscores) {
                writer.printf("%d,%d,%d,%d,%s%n", hs.getRank(), hs.getLevel(), hs.getTime(), hs.getScore(), hs.getDate());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long calculateScore(long time, int puzzleSize,int lives) {
        return (3600 - time+ 30L *lives) * puzzleSize;
    }

    public static void addScore(int level, long time, int puzzleSize,int lives) {
        long score = calculateScore(time, puzzleSize,lives);
        List<HighScore> highscores = loadHighscores();
        
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        HighScore newHighscore = new HighScore(highscores.size() + 1, level, time, score, date);
        highscores.add(newHighscore);
        
        highscores.sort((hs1, hs2) -> Long.compare(hs2.getScore(), hs1.getScore()));
        
        for (int i = 0; i < highscores.size(); i++) {
            highscores.get(i).rank = i + 1;
        }

        saveHighscores(highscores);
    }
}
