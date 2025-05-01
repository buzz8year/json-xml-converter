package converter;

import converter.builder.BuilderFactory;
import converter.converter.Converter;
import converter.file.FileReader;
import converter.parser.ParserFactory;

import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Converter converter = new Converter(new ParserFactory(), new BuilderFactory());
        String result = converter.convert(FileReader.read("test.iml"));

        System.out.println(result);
    }
}