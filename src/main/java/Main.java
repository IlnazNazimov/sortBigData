public class Main {

    public static final int BATCH_SIZE = 1000000;  // ������ ����� �����, ������� ���������� � ������
    public static final String INPUT_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/input.txt";   // ���� � �������� �����
    public static final String OUTPUT_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/output.txt";   // ���� � ��������� �����

    public static void main(String[] args) {
        SortFileWithBigData.multiStageFileSorting(INPUT_FILE_PATH, OUTPUT_FILE_PATH, BATCH_SIZE);
    }
}
