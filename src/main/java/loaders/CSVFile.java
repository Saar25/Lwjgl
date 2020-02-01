package loaders;

import java.util.List;

public class CSVFile {

    private final List<String[]> file;

    private CSVFile(List<String[]> file) {
        this.file = file;
    }

    public static CSVFile parseFile(String fileName) throws Exception {
        return CSVLoader.loadCSVFIle(fileName);
    }
}
