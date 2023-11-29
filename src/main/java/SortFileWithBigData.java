import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class SortFileWithBigData {

    /**
     * ��������� ������ �� inputFilePath ��������� � ���������� � ����� ����
     * @param inputFilePath ���� �������� �����
     * @param outputFileDir ���� ��������� �����
     * @param batchSize ���������� �����, ������� ������� ������
     */
    public static void multiStageFileSorting(String inputFilePath, String outputFileDir, int batchSize) {
        System.out.println("�������� ����������...");

        List<String> batchFilePaths = new ArrayList<>();
        if (inputFilePath == null) throw new RuntimeException("Не указан путь к входному файлу");
        splitIntoSmallSortedFiles(inputFilePath, batchSize, batchFilePaths);

        // ������� ��������������� ������
        if (!batchFilePaths.isEmpty()) {
            mergeSortedFiles(batchFilePaths, outputFileDir);
        } else {
            throw new RuntimeException("�� ������� ����� ����������� ������");
        }

        cleanTempFiles(batchFilePaths);

        System.out.println("������ � ����� ���� �������������.");
    }

    /**
     * ��������� ������� ���� �� ��������� ����� � ���������������� �������
     * @param inputFilePath ���� �������� �����
     * @param batchSize ���������� �����, ������� ������� ������
     * @param batchFilePaths ������ ����� � ������
     */
    private static void splitIntoSmallSortedFiles(String inputFilePath, int batchSize, List<String> batchFilePaths) {
        Set<String> batch = new TreeSet<>();
        String line;
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            while ((line = reader.readLine()) != null) {
                batch.add(line);
                lineCount++;

                // ������ ��� ����� ����������� batchSize ����� ���������� ������ � ����� ����
                if (lineCount == batchSize) {
                    writeInFile(batchFilePaths, batch);

                    // ���������� ������� � ������� ������ ��� ��������� ������ ������
                    lineCount = 0;
                    batch.clear();
                }
            }

            // ��������� ��������� ������, ������� ����� ���� ������ batchSize
            if (!batch.isEmpty()) {
                writeInFile(batchFilePaths, batch);
            }
        } catch (IOException e) {
            throw new RuntimeException("�������� ������ ��� ��������� ����� : " + e.getMessage());
        }
    }

    /**
     * ���������� ������ ����� � ����� ����
     * @param batchFilePaths ������ ����� � ������
     * @param batch ������ �����
     */
    private static void writeInFile(List<String> batchFilePaths, Set<String> batch) throws IOException {
        String batchFilePath = "batch_" + batchFilePaths.size() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFilePath))) {
            for (String line : batch) {
                writer.write(line);
                writer.newLine();
            }
        }
        batchFilePaths.add(batchFilePath);
    }

    /**
     * ���������� ��� ����� �� batchFilePaths � ����
     * @param batchFilePaths ������ ����� � ������
     * @param outputFileDir ���� ��������� �����
     */
    public static void mergeSortedFiles(List<String> batchFilePaths, String outputFileDir) {
        List<BufferedReader> readers = new ArrayList<>();
        PriorityQueue<BatchReader> queueBatchReaders = new PriorityQueue<>(batchFilePaths.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileDir + "/output.txt"))) {
            // ��������� ��������� ��� ������ ������
            for (String filePath : batchFilePaths) {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                readers.add(reader);
                BatchReader batchReader = new BatchReader(reader);
                queueBatchReaders.offer(batchReader);
            }

            while (!queueBatchReaders.isEmpty()) {
                // ����� �� ������� BatchReader, ������� ����� ����� ������� ������(�� compare)
                BatchReader currentReader = queueBatchReaders.poll();
                String line = currentReader.readLine();

                // ���������� ��� ������ � ����
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }

                // ���� ������� ��� ������, �� ����� �������� BatchReader � �������
                if (currentReader.hasMoreLines()) {
                    queueBatchReaders.offer(currentReader);
                }
            }

            // ��������� ���������
            for (BufferedReader reader : readers) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ������� ��������� �����
     * @param batchFilePaths ������ ����� � ������
     */
    private static void cleanTempFiles(List<String> batchFilePaths) {
        for (String filePath : batchFilePaths) {
            File file = new File(filePath);
            file.delete();
        }
    }
}
