package converter.converter;

import converter.builder.BuilderFactory;
import converter.parser.ParserFactory;
import converter.builder.Builder;
import converter.parser.Parser;

public class Converter implements ConvertStrategy
{
    private final ParserFactory parserFactory;
    private final BuilderFactory builderFactory;

    public Converter(ParserFactory parserFactory, BuilderFactory builderFactory)
    {
        this.parserFactory = parserFactory;
        this.builderFactory = builderFactory;
    }

    public String convert(String payload)
    {
        Parser parser = parserFactory.getParser(payload);
        Builder builder = builderFactory.getBuilder(payload);

        parser.setPayload(payload);
        parser.payloadToNodes();

        builder.setTree(parser.getTree());
        builder.buildTree();

        return builder.getResult();
    }

}
