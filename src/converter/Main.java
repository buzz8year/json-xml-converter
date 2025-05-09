package converter;

import java.io.IOException;

import converter.parser.ParserFactory;
import converter.builder.BuilderFactory;
import converter.file.Reader;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Converter converter = new Converter(new ParserFactory(), new BuilderFactory());
        String payload = Reader.read("test.json");
        String result = converter.convert(payload);

        //Github.searchTreeForNotFollowers(converter.getParser().getTree());
        System.out.println(result);
    }
}