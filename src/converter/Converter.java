package converter;

import converter.builder.BuilderFactory;
import converter.parser.ParserFactory;
import converter.builder.Builder;
import converter.parser.Parser;
import lombok.Getter;

@Getter
public class Converter
{
    private final ParserFactory parserFactory;
    private final BuilderFactory builderFactory;

    private Parser parser;
    private Builder builder;

    public Converter(ParserFactory parserFactory, BuilderFactory builderFactory)
    {
        this.parserFactory = parserFactory;
        this.builderFactory = builderFactory;
    }

    public String convert(String payload)
    {
        parser = parserFactory.getParser(payload);
        builder = builderFactory.getBuilder(payload);

        parser.setPayload(payload);
        parser.payloadToNodes();

        builder.setTree(parser.getTree());
        builder.buildTree();

        return builder.getResult();
    }
}
