package converter.file;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Reader
{
    public static String read(String path) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File(path));
        StringBuilder payload = new StringBuilder();

        while (scanner.hasNextLine())
            payload.append(scanner.nextLine().trim());
        scanner.close();

        return payload.toString();
    }
}
