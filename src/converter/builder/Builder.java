package converter.builder;

import converter.node.Node;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public abstract class Builder
{
    @Setter
    protected List<Node> tree;

    @Getter
    protected String result = "";

    abstract public void buildTree();
    abstract public String build(Node node);
}
