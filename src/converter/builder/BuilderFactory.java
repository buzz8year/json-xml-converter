package converter.builder;

import converter.builder.impl.JsonBuilder;
import converter.builder.impl.XmlBuilder;
import converter.util.Validator;

public class BuilderFactory
{
    public Builder getBuilder(String payload)
    {
        if (Validator.isXml(payload))
            return new JsonBuilder();

        if (Validator.isJson(payload))
            return new XmlBuilder();

        throw new IllegalArgumentException("Unsupported payload format");
    }
}