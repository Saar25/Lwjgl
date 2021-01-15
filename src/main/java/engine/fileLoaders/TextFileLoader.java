package engine.fileLoaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public final class TextFileLoader {

    private TextFileLoader() {

    }

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = TextFileLoader.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(TextFileLoader.class.getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static String connectStringList(List<String> list) {
        StringBuilder string = new StringBuilder();
        for (String s : list) {
            string.append(s).append('\n');
        }
        return string.toString();
    }
}