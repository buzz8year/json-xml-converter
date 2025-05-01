package converter.parser;

import converter.node.Node;
import java.util.ArrayDeque;
import java.util.Deque;

import java.util.LinkedList;
import java.util.List;

public abstract class Parser
{
    protected String payload;
    protected List<Node> tree = new LinkedList<>();
    protected Deque<Node> parents = new ArrayDeque<>();

    abstract public String[] splitPayload(String payload);
    abstract public void payloadToNodes();

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public List<Node> getTree() {
        return tree;
    }
}