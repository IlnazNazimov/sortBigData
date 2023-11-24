import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SortFileWithBigDataTest {

    @Test
    public void sortingWithLessDataThanBatchSize() throws IOException {
        // Создаем временный входной файл
        int batchSize = 10;
        Path tempInputFile = Files.createTempFile("tempInput", ".txt");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempInputFile.toFile()));
        tempWriter.write("three:3\n");
        tempWriter.write("one:1\n");
        tempWriter.write("two:2\n");
        tempWriter.write("four:4\n");
        tempWriter.close();

        // Создаем временный выходной файл
        Path tempOutputFile = Files.createTempFile("tempOutput", ".txt");

        SortFileWithBigData.multiStageFileSorting(tempInputFile.toString(), tempOutputFile.toString(), batchSize);

        // Проверяем, что данные в файле отсортированы
        List<String> sortedLines = Files.readAllLines(tempOutputFile);
        assertLinesMatch(Arrays.asList("four:4", "one:1", "three:3", "two:2"), sortedLines);
    }

    @Test
    public void sortingWithMoreDataThanBatchSize() throws IOException {
        // Создаем временный входной файл
        int batchSize = 2;
        Path tempInputFile = Files.createTempFile("tempInput", ".txt");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempInputFile.toFile()));
        tempWriter.write("three:3\n");
        tempWriter.write("one:1\n");
        tempWriter.write("two:2\n");
        tempWriter.write("four:4\n");
        tempWriter.close();

        // Создаем временный выходной файл
        Path tempOutputFile = Files.createTempFile("tempOutput", ".txt");

        SortFileWithBigData.multiStageFileSorting(tempInputFile.toString(), tempOutputFile.toString(), batchSize);

        // Проверяем, что данные в файле отсортированы
        List<String> sortedLines = Files.readAllLines(tempOutputFile);
        assertLinesMatch(Arrays.asList("four:4", "one:1", "three:3", "two:2"), sortedLines);
    }

    @Test
    public void sortingWhenAmountOfDataIsIncompleteInLastBatch() throws IOException {
        // Создаем временный входной файл
        int batchSize = 2;
        Path tempInputFile = Files.createTempFile("tempInput", ".txt");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempInputFile.toFile()));
        tempWriter.write("three:3\n");
        tempWriter.write("one:1\n");
        tempWriter.write("two:2\n");
        tempWriter.write("four:4\n");
        tempWriter.write("five:5\n");
        tempWriter.close();

        // Создаем временный выходной файл
        Path tempOutputFile = Files.createTempFile("tempOutput", ".txt");

        SortFileWithBigData.multiStageFileSorting(tempInputFile.toString(), tempOutputFile.toString(), batchSize);

        // Проверяем, что данные в файле отсортированы
        List<String> sortedLines = Files.readAllLines(tempOutputFile);
        assertLinesMatch(Arrays.asList("five:5", "four:4", "one:1", "three:3", "two:2"), sortedLines);
    }

    @Test
    public void sortingEmptyData() throws IOException {
        // Создаем временный входной файл
        int batchSize = 2;
        Path tempInputFile = Files.createTempFile("tempInput", ".txt");
        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempInputFile.toFile()));
        tempWriter.close();

        // Создаем временный выходной файл
        Path tempOutputFile = Files.createTempFile("tempOutput", ".txt");

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> SortFileWithBigData.multiStageFileSorting(tempInputFile.toString(), tempOutputFile.toString(), batchSize)
        );
        assertEquals("Во входном файле отсутствуют данные", runtimeException.getMessage());
    }
}
