package converter.parser;

import converter.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class Parser {
    public String payload;
    public List<Node> tree = new LinkedList<>();
    public Deque<Node> parents = new ArrayDeque<>();
    abstract public String[] splitPayload(String s);
    abstract public void payloadToNodes();
}