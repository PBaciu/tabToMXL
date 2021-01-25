package TabToMXL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The purpose of this class is to provide utility for parsing .txt files
 */
public class Parser {

    private final Path path;

    public Parser(Path path) {
        this.path = path;
    }

    public List<String> read_tab() {
        List<String> res = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(String.valueOf(this.path)))){
            while(scanner.hasNext()) {
                res.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
}
