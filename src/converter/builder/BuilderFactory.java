package converter.builder;

import converter.builder.impl.JsonBuilder;
import converter.builder.impl.XmlBuilder;
import converter.util.Recognizer;

public class BuilderFactory
{
    public Builder getBuilder(String payload)
    {
        if (Recognizer.isXml(payload))
            return new JsonBuilder();

        if (Recognizer.isJson(payload))
            return new XmlBuilder();

        throw new IllegalArgumentException("Unsupported payload format");
    }
}