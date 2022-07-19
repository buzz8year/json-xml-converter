package converter;

import converter.parser.Parser;
import converter.parser.JsonParser;
import converter.parser.XmlParser;

import converter.builder.Builder;
import converter.builder.JsonBuilder;
import converter.builder.XmlBuilder;

public class Converter {
    public String payload, result;
    public Builder builder;
    public Parser parser;

    public void convert() {
        if (Recognizer.isXml(payload)) {
            parser = new XmlParser();
            builder = new JsonBuilder();
        }
        else if (Recognizer.isJson(payload)) {
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
