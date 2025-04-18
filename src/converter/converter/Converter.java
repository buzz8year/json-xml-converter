package converter.converter;

import converter.util.Validator;
import converter.parser.Parser;
import converter.parser.impl.JsonParser;
import converter.parser.impl.XmlParser;

import converter.builder.Builder;
import converter.builder.impl.JsonBuilder;
import converter.builder.impl.XmlBuilder;

public class Converter implements ConvertStrategy
{
    public String payload, result;
    public Builder builder;
    public Parser parser;

    public void convert()
    {
        if (Validator.isXml(payload)) {
            parser = new XmlParser();
            builder = new JsonBuilder();
        }
        else if (Validator.isJson(payload)) {
            parser = new JsonParser();
            builder = new XmlBuilder();
        }

        parser.payload = this.payload;
        parser.payloadToNodes();

        builder.tree = parser.tree;
        builder.buildTree();

        this.result = builder.result;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getResult() {
        return this.result;
    }

}
