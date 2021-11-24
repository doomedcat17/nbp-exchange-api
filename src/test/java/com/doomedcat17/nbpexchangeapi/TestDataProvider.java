package com.doomedcat17.nbpexchangeapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestDataProvider {

    public static String jsonStringFromFile(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
