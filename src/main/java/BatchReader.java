import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс для представления читателя файла
 */
public class BatchReader implements Comparable<BatchReader> {
    private final BufferedReader reader;
    private String currentLine;

    public BatchReader(BufferedReader reader) throws IOException {
        this.reader = reader;
        this.currentLine = reader.readLine();
    }

    public String readLine() {
        String lineToReturn = currentLine;
        try {
            currentLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineToReturn;
    }

    public boolean hasMoreLines() {
        return currentLine != null;
    }

    @Override
    public int compareTo(BatchReader other) {
        return this.currentLine.compareTo(other.currentLine);
    }

}