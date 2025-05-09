package converter.file;

import java.io.FileWriter;
import java.io.IOException;

public class Writer
{
    public static void append(String s, String path) throws IOException
    {
        FileWriter writer = new FileWriter(path, true);
        writer.write(s);
        writer.close();
    }
}
