public class Main {

    public static final int BATCH_SIZE = 1000000;  // ������ ����� �����, ������� ���������� � ������
    public static final String INPUT_FILE_PATH = null;   // ���� � �������� �����
    public static final String OUTPUT_FILE_DIR = null;   // ���������� � ��������� �����

    public static void main(String[] args) {
        SortFileWithBigData.multiStageFileSorting(INPUT_FILE_PATH, OUTPUT_FILE_DIR, BATCH_SIZE);
    }
}
