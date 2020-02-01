package loaders;

import engine.fileLoaders.TextFileLoader;

import java.util.List;

public class CSVLoader {

    public static CSVFile loadCSVFIle(String fileName) throws Exception {
        if (!fileName.endsWith(".csv")) {
            throw new IllegalArgumentException("Input given in CSVLoader class is illegal, file: " + fileName);
        }
        List<String> data = TextFileLoader.readAllLines(fileName);

        return null;
    }

}
