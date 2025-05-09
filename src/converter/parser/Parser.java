package converter.parser;

import converter.node.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

import java.util.LinkedList;
import java.util.List;

public abstract class Parser
{
    @Setter
    protected String payload;

    @Getter
    protected List<Node> tree = new LinkedList<>();

    protected Deque<Node> parents = new ArrayDeque<>();

    abstract public String[] splitPayload();
    abstract public void payloadToNodes();
}