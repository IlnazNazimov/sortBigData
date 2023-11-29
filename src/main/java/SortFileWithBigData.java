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
     * Считывает данные из inputFilePath сортирует и записывает в новый файл
     * @param inputFilePath Путь входного файла
     * @param outputFileDir Путь выходного файла
     * @param batchSize Количество строк, который вмещает память
     */
    public static void multiStageFileSorting(String inputFilePath, String outputFileDir, int batchSize) {
        System.out.println("Начинаем сортировку...");

        List<String> batchFilePaths = new ArrayList<>();
        if (inputFilePath == null) throw new RuntimeException("Не указан путь к входному файлу");
        splitIntoSmallSortedFiles(inputFilePath, batchSize, batchFilePaths);

        // Слияние отсортированных файлов
        if (!batchFilePaths.isEmpty()) {
            mergeSortedFiles(batchFilePaths, outputFileDir);
        } else {
            throw new RuntimeException("Во входном файле отсутствуют данные");
        }

        cleanTempFiles(batchFilePaths);

        System.out.println("Данные в файле были отсортированы.");
    }

    /**
     * Разбивает входной файл на небольшие файлы с отсортированными данными
     * @param inputFilePath Путь входного файла
     * @param batchSize Количество строк, который вмещает память
     * @param batchFilePaths Список путей к файлам
     */
    private static void splitIntoSmallSortedFiles(String inputFilePath, int batchSize, List<String> batchFilePaths) {
        Set<String> batch = new TreeSet<>();
        String line;
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            while ((line = reader.readLine()) != null) {
                batch.add(line);
                lineCount++;

                // Каждый  раз после прохождения batchSize строк записываем партию в новый файл
                if (lineCount == batchSize) {
                    writeInFile(batchFilePaths, batch);

                    // Сбрасываем счетчик и очищаем партию для следующей порции данных
                    lineCount = 0;
                    batch.clear();
                }
            }

            // Обработка последней партии, которая может быть меньше batchSize
            if (!batch.isEmpty()) {
                writeInFile(batchFilePaths, batch);
            }
        } catch (IOException e) {
            throw new RuntimeException("Возникла ошибка при разбиении файла : " + e.getMessage());
        }
    }

    /**
     * Записывает партию строк в новый файл
     * @param batchFilePaths Список путей к файлам
     * @param batch Партия строк
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
     * Объединяет все файлы из batchFilePaths в один
     * @param batchFilePaths Список путей к файлам
     * @param outputFileDir Путь выходного файла
     */
    public static void mergeSortedFiles(List<String> batchFilePaths, String outputFileDir) {
        List<BufferedReader> readers = new ArrayList<>();
        PriorityQueue<BatchReader> queueBatchReaders = new PriorityQueue<>(batchFilePaths.size());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileDir + "/output.txt"))) {
            // Открываем читателей для каждой партии
            for (String filePath : batchFilePaths) {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                readers.add(reader);
                BatchReader batchReader = new BatchReader(reader);
                queueBatchReaders.offer(batchReader);
            }

            while (!queueBatchReaders.isEmpty()) {
                // Берем из очереди BatchReader, который имеет самую большую строку(по compare)
                BatchReader currentReader = queueBatchReaders.poll();
                String line = currentReader.readLine();

                // Записываем эту строку в файл
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }

                // Если имеются еще строки, то снова помещаем BatchReader в очередь
                if (currentReader.hasMoreLines()) {
                    queueBatchReaders.offer(currentReader);
                }
            }

            // Закрываем читателей
            for (BufferedReader reader : readers) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет временные файлы
     * @param batchFilePaths Список путей к файлам
     */
    private static void cleanTempFiles(List<String> batchFilePaths) {
        for (String filePath : batchFilePaths) {
            File file = new File(filePath);
            file.delete();
        }
    }
}
