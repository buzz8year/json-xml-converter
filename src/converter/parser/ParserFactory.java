package converter.parser;

import converter.parser.impl.JsonParser;
import converter.parser.impl.XmlParser;
import converter.util.Validator;

public class ParserFactory
{
    public Parser getParser(String payload)
    {
        if (Validator.isXml(payload))
            return new XmlParser();

        if (Validator.isJson(payload))
            return new JsonParser();

        throw new IllegalArgumentException("Unsupported payload format");
    }
}