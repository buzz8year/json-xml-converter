package converter.builder;

import converter.node.Node;

import java.util.List;

public abstract class Builder
{
    protected List<Node> tree;
    protected String result = "";

    abstract public void buildTree();
    abstract public String build(Node node);

    public void setTree(List<Node> tree) {
        this.tree = tree;
    }

    public String getResult() {
        return result;
    }
}
