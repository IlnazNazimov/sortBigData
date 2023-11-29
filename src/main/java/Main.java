public class Main {

    public static final int BATCH_SIZE = 1000000;  // Размер части файла, который помещается в память
    public static final String INPUT_FILE_PATH = null;   // Путь к входному файлу
    public static final String OUTPUT_FILE_DIR = null;   // Директория к выходному файлу
    public static void main(String[] args) {
        SortFileWithBigData.multiStageFileSorting(INPUT_FILE_PATH, OUTPUT_FILE_DIR, BATCH_SIZE);
    }
}
