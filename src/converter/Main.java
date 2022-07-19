package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Converter converter = new Converter();
        converter.setPayload(readFile("test.txt"));
        converter.convert();

        System.out.println(converter.getResult());
    }

    public static String readFile(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        StringBuilder payload = new StringBuilder();

        while (scanner.hasNextLine())
            payload.append(scanner.nextLine().trim());
        scanner.close();

        return payload.toString();
    }
}