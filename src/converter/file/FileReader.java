package converter.file;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader
{
    public static String read(String path) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new java.io.File(path));
        StringBuilder payload = new StringBuilder();

        while (scanner.hasNextLine())
            payload.append(scanner.nextLine().trim());
        scanner.close();

        return payload.toString();
    }
}
