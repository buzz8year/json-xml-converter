package converter.parser.impl;

import converter.node.Node;
import converter.parser.Parser;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonParser extends Parser
{
    public List<Node> heap = new LinkedList<>();

    public void payloadToNodes()
    {
        for (var s : this.splitPayload(this.payload))
        {
            if (isOpening(s)) this.processParent(s);
            else if (isPair(s)) this.processPair(s);
            else if (s.isBlank()) this.processEmpty();
            else if (isClosing(s)) parents.pop();
            else this.processSingle(s);
        }

        for (Node node : this.heap)
        {
            this.determineNodeUnity(node);
            if (node.isUnified)
                this.childrenToAttributes(node);
        }
        this.processMisfits();
    }

    public void processEmpty()
    {
        if (!parents.isEmpty())
            parents.peek().value = "";
    }

    public void processParent(String s)
    {
        Node node = new Node(s);
        node.name = s.split(":")[0].replaceAll("\"", "").trim();
        node.value = "no-show";

        if (node.name.equals("[") || node.name.equals("{"))
            node.name = "element";

        if (parents.isEmpty()) this.tree.add(node);
        else {
            Node parent = parents.peek();
            parent.children.add(node);
            node.parent = parent;
        }

        this.parents.push(node);
        this.heap.add(node);
    }

    public void processPair(String s)
    {
        Node parent = parents.peek();
        String[] pair = s.split(":");

        Node node = new Node(s);
        node.name = this.trimQuotes(pair[0]);
        node.value = this.trimQuotes(pair[1]);

        if (parent != null)
        {
            String name = node.name.replaceAll("[#@]", "");

            Iterator<Node> iterator = parent.children.iterator();
            while (iterator.hasNext())
            {
                String compare = iterator.next().name.replaceAll("[#@]", "");
                if (name.equals(compare)) iterator.remove();
            }
            parent.children.add(node);
            node.parent = parent;
        }
    }

    public void processSingle(String s)
    {
        Node node = new Node(s);
        node.value = s.trim();
        node.name = "element";

        Node parent = parents.peek();
        if (parent != null)
        {
            parent.children.add(node);
            node.parent = parent;
        }
    }

    public void determineNodeUnity(Node node)
    {
        for (Node child : node.children)
        {
            if (node.children.size() < 2 && !child.name.startsWith("#"))
                node.isUnified = false;

            if (child.name.startsWith("#"))
            {
                String compare = child.name.replaceFirst("#", "");
                if (!node.name.equals(compare)) node.isUnified = false;
            }
            else if (child.name.startsWith("@"))
            {
                if (child.name.length() < 2 || !child.children.isEmpty())
                    node.isUnified = false;
            }
            else node.isUnified = false;
        }
    }

    public void childrenToAttributes(Node node)
    {
        Iterator<Node> iterator = node.children.iterator();

        while (iterator.hasNext())
        {
            Node child = iterator.next();

            if (child.name.startsWith("#"))
            {
                if (child.children.isEmpty())
                {
                    node.value = child.value;
                    iterator.remove();
                }
                else substituteNode(node, child);
            }
            else if (child.name.startsWith("@"))
            {
                childToAttribute(node, child);
                iterator.remove();
            }
        }
    }

    public void childToAttribute(Node parent, Node child)
    {
        String name = child.name.replaceFirst("@", "");
        parent.attributes.put(name, child.value);
    }

    public void substituteNode(Node parent, Node child)
    {
        child.attributes = parent.attributes;
        child.parent = parent.parent;
        int i = parent.parent.children.indexOf(parent);
        parent.parent.children.set(i, child);
    }

    public void processMisfits()
    {
        Iterator<Node> iterator = this.heap.iterator();

        while (iterator.hasNext()) {
            Node parent = iterator.next();
            if (parent.name.isBlank()) iterator.remove();
            else this.processChildMisfits(parent);
        }
    }

    public void processChildMisfits(Node node)
    {
        List<String> list = new ArrayList<>();
        Iterator<Node> iterator = node.children.iterator();

        while (iterator.hasNext())
        {
            Node child = iterator.next();

            if (child.name.contains("@"))
            {
                String name = child.name.replaceFirst("@", "");
                if (list.contains(name)) iterator.remove();
            }

            if (child.name.length() < 2)
                iterator.remove();
            else child.name = child.name.replaceFirst("[@#]", "");

            list.add(child.name);
        }
    }

    public boolean isOpening(String s) {
        return s.contains("{") || s.contains("[");
    }

    public boolean isClosing(String s) {
        return s.contains("}") || s.contains("]");
    }

    public boolean isPair(String s) {
        return !isOpening(s) && !isClosing(s) && s.contains(":");
    }

    public String trimPayload(String s) {
        return s.replaceFirst("\\{", "").replaceAll("}$", "");
    }

    public String trimQuotes(String s) {
        return s.trim().replaceAll("\"", "");
    }

    public String[] splitPayload(String s) {
        return this.trimPayload(s).split(",|(?<=[{\\[])|(?=[]}])");
    }
}
