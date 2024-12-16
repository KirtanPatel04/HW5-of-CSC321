import java.io.*;
import java.util.*;

public class ScoreKeeper {
    private List<Integer> scores;
    private final String fileName = "scores.txt";

    public ScoreKeeper() {
        scores = new ArrayList<>();
        loadScores();
    }

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.out.println("No previous scores found.");
        }
    }

    public void saveScore(int score) {
        scores.add(score);
        Collections.sort(scores);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int s : scores) {
                writer.write(s + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving scores.");
        }
    }

    public void displayTopScores(int n) {
        System.out.println("Top " + n + " Scores:");
        for (int i = 0; i < Math.min(n, scores.size()); i++) {
            System.out.println((i + 1) + ". " + scores.get(i));
        }
    }
}