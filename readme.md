# ������ SortFile �������� �� ���������� ������ � �������� �������. �� ���������� ������� ���������� ��� ��������� ������, ������� �� ����� ����������� � ����������� ������ �������.

## �������� ���������

������� ���������� ����� ���������� ��������� �������:
1. ������� ���� ����������� �������� �� `Main.BATCH_SIZE` �����, ������� ���������� � ����������� ������.
2. ������ ������ ����������� � ����������� ������ � ����������� � ��������� ����.
3. ��������������� ����� ������������ � �������� ���� :
   - ��������� ������ PriorityQueue ��� �������� ����� �� ������ ������ � ��������������� �������.
   - ��� ������� ���������������� ����� :
     - ��������� ������ BufferedReader ��� ������ ����� �� ����� � ����������� � `BatchReader`.
     - � ������������ `BatchReader` ����������� ������ ������ � ������������ � ���� `currentLine`.
     - `BatchReader` ���������� � PriorityQueue, �������� ���������� �������� �������� ��������� ����� `currentLine`.
   - ����� ���� �� ���������� ������� � PriorityQueue :
     - �� PriorityQueue ����������� `BatchReader` � ����� �������(�� compare()) ������� � ������������ � �������� ����.
     - ���� `currentLine` ������ `BatchReader` ������������� ��������� ������.
     - ���� `currentLine != null`, �� `BatchReader` ����� ���������� � PriorityQueue.
4. ��� ��������� ����� ���������.

# ���������� :
   - ����� `mergeSortedFiles` ���������� ������������ ������� (PriorityQueue), ����� �������������, ��� ������ �� ������ ������ ����� �������� � �������� ���� � ���������� �������. ������������ ������� ������������� ��������� �������� �� �������� `currentLine`.

## ������ ������

1. ���� ���� � �������� ������� ��� ����, �� ���������� ������� ���� � ���� � ������ `Main` � ���� `INPUT_FILE_PATH`.
2. ���� ����� � �������� ������� ���, �� ����� ������������� ������:
    - ������� ����� `Generator`.
    - ������� � ���� `countLines` ���������� ������������ �����.
    - ��������� ����� `main` � ������ `Generator`.
    - ��������������� ������ �������� � `*/src/main/resources/input.txt`.
3. ������� ���������� �����, ������� ������� ������ � ������ `Main` � ���� `BATCH_SIZE`.
4. ��������� ����� `main` � ������ `Main`.
5. ��������������� ������ �������� � `*/src/main/resources/output.txt`.