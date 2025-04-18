package converter;

import converter.converter.Converter;
import converter.file.FileReader;

import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Converter converter = new Converter();
        converter.setPayload(FileReader.read("../test.json"));
        converter.convert();

        System.out.println(converter.getResult());
    }
}