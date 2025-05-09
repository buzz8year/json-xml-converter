package converter.parser.impl;

import converter.node.Node;
import converter.parser.Parser;
import converter.util.JsonString;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonParser extends Parser
{
    public List<Node> heap = new LinkedList<>();
    public String brokenPair = "";

    public void payloadToNodes()
    {
        for (String s : splitPayload())
        {
            if (!brokenPair.isBlank())
                s = brokenPair + s;

            if (JsonString.isOpening(s))
                handleParent(s);

            // NOTE: splitPayload() regex break-characters include "{", that may also be found within key-value pair,
            // e.g.: "starred_url": "https://api.github.com/users/buzz8year/starred{/owner}{/repo}".
            // So we may get broken string to handle, and the next string would also be meaningless/broken chunk.
            else if (JsonString.isPairBroken(s))
                brokenPair = s;

            else if (JsonString.isPair(s))
                handlePair(s);

            else if (s.isBlank())
                handleEmpty();

            else if (JsonString.isClosing(s))
                parents.pop();

            else handleSingle(s);
        }

        for (Node node : this.heap)
        {
            determineNodeUnity(node);
            if (node.isUnified)
                childrenToAttributes(node);
        }
        handleMisfits();
    }

    public void handleEmpty()
    {
        if (!parents.isEmpty())
            parents.peek().value = "";
    }

    public void handleParent(String s)
    {
        Node node = new Node(s);
        node.name = s.split(":")[0].replaceAll("\"", "").trim();
        node.value = "no-show";

        if (node.name.equals("["))
            node.name = "array";

        else if (node.name.equals("{"))
            node.name = "object";


        if (parents.isEmpty())
            tree.add(node);

        else {
            Node parent = parents.peek();
            parent.children.add(node);
            node.parent = parent;
        }

        parents.push(node);
        heap.add(node);
    }

    public void handlePair(String s)
    {
        brokenPair = "";

        Node parent = parents.peek();
        String[] pair = s.split(":", 2);

        Node node = new Node(s);
        node.name = JsonString.trimQuotes(pair[0]);
        node.value = JsonString.trimQuotes(pair[1]);

        if (parent == null)
            return;

        String name = node.name.replaceAll("[#@]", "");

        Iterator<Node> iterator = parent.children.iterator();
        while (iterator.hasNext())
        {
            String compare = iterator.next().name.replaceAll("[#@]", "");
            if (name.equals(compare))
                iterator.remove();
        }
        parent.children.add(node);
        node.parent = parent;
    }

    public void handleSingle(String s)
    {
        Node node = new Node(s);
        node.value = s.trim();
        node.name = "element";

        Node parent = parents.peek();
        if (parent == null)
            return;

        parent.children.add(node);
        node.parent = parent;
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
                if (!node.name.equals(compare))
                    node.isUnified = false;
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

    public void handleMisfits()
    {
        Iterator<Node> iterator = heap.iterator();

        while (iterator.hasNext())
        {
            Node parent = iterator.next();
            if (parent.name.isBlank())
                iterator.remove();
            else handleChildMisfits(parent);
        }
    }

    public void handleChildMisfits(Node node)
    {
        List<String> list = new ArrayList<>();
        Iterator<Node> iterator = node.children.iterator();

        while (iterator.hasNext())
        {
            Node child = iterator.next();

            if (child.name.contains("@"))
            {
                String name = child.name.replaceFirst("@", "");
                if (list.contains(name))
                    iterator.remove();
            }

            if (child.name.length() > 1)
                child.name = child.name.replaceFirst("[@#]", "");
            else iterator.remove();

            list.add(child.name);
        }
    }


    public String[] splitPayload()
    {
        return payload.split(",|(?<=[{\\[])|(?=[]}])");
    }
}
