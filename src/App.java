import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            File file = new File(args[0]);
            if (file.exists()) {
                String json = readFile(file);
                System.out.print(json);
                writeFile(json);
            } else {
                System.out.println("File not found. Please. Give a valid file path.");
            }
        } else {
            System.out.println("Is needed to give a file path as argument.");
        }
    }

    private static String readFile(File file) throws FileNotFoundException, IOException {
        String lineToCheck = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(replaceAll(line));
                line = br.readLine();
            }

            lineToCheck = sb.toString();
        }
        return lineToCheck;
    }

    private static String replaceAll(String lineToCheck) {
        String output = "";
        if (lineToCheck.matches("^<[A-Z]+>$")) {
            output = lineToCheck.replace("<", "{\"");

            if (lineToCheck.contains("CATALOG"))
                output = output.replace(">", "\":[\n");
            else
                output = output.replace(">", "\":{\n");
        }

        if (lineToCheck.matches("<[A-Z]+>.*<\\/[A-Z]+>")) {
            output = lineToCheck.replaceFirst("<", "\"").replaceFirst(">", "\":\"").replaceFirst("<\\/[A-Z]+>",
                    "\",\n");
        }

        if (lineToCheck.matches("<\\/[A-Z]+>")) {
            if (lineToCheck.contains("CATALOG"))
                output = lineToCheck.replaceFirst("<\\/[A-Z]+>", "]\n}");
            else
                output = lineToCheck.replaceFirst("<\\/[A-Z]+>", "},\n},\n");
        }
        return output;
    }

    private static void writeFile(String content) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("output.json", "UTF-8");
        writer.println(content);
        writer.close();
    }
}
