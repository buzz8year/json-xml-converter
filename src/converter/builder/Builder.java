package converter.builder;

import converter.node.Node;

import java.util.List;

public abstract class Builder
{
    public List<Node> tree;
    public String result = "";

    abstract public void buildTree();
    abstract public String build(Node node);
}
