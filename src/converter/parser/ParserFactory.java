package converter.parser;

import converter.parser.impl.JsonParser;
import converter.parser.impl.XmlParser;
import converter.util.Recognizer;

public class ParserFactory
{
    public Parser getParser(String payload)
    {
        if (Recognizer.isXml(payload))
            return new XmlParser();

        if (Recognizer.isJson(payload))
            return new JsonParser();

        throw new IllegalArgumentException("Unsupported payload format");
    }
}