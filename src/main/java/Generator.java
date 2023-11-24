import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
    private static final int countLines = 5000000;

    public static void main(String[] args) throws IOException {
        generate();
    }

    private static void generate() throws IOException {
        String symbols = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(Main.INPUT_FILE_PATH))) {
            for (int i = 0; i < countLines; i++) {
                for (int j = 0; j < symbols.length(); j++) {
                    sb.append(symbols.charAt(random.nextInt(symbols.length() - 1)));
                }
                sb.append(":").append(i).append("\n");
                writer.write(sb.toString());
                sb.delete(0,sb.length());
            }
        }
    }
}
