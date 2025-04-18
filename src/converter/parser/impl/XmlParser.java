package converter.parser.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import converter.node.Node;
import converter.parser.Parser;

public class XmlParser extends Parser
{
    public void payloadToNodes()
    {
        for (var s : this.splitPayload(this.payload))
        {
            if (s.contains("</"))
            {
                Node parent = parents.pop();
                if (parent.value == null)
                    parent.value = "";
            }
            else if (s.contains("<"))
            {
                Node node = new Node(s);
                this.processElement(node);
                this.setParent(node);

                if (s.contains("/>"))
                    parents.pop();
            }
            else if (!parents.isEmpty())
                parents.peek().value = s;
        }

        for (Node node : this.tree)
            this.checkForArray(node);
    }

    public void setParent(Node node)
    {
        if (parents.isEmpty())
            this.tree.add(node);
        else {
            parents.peek().children.add(node);
            node.parent = parents.peek();
        }
        this.parents.push(node);
    }

    public void processElement(Node node)
    {
        Matcher m = Pattern.compile("<[ -=\\w\"'/]+>").matcher(node.payload);

        while (m.find())
        {
            String s = m.group().replaceAll("\\s*=\\s*","=")
                    .replaceAll("[</>]","");

            for (var part : s.split(" "))
            {
                if (!part.contains("="))
                    node.name = part;
                else attributeToMap(node, part.replaceAll("\\s",""));
            }
        }
    }

    public void attributeToMap(Node node, String s)
    {
        Matcher name = Pattern.compile("\\w+=").matcher(s);
        Matcher value = Pattern.compile("[\"'].*[\"']").matcher(s);

        if (name.find() && value.find())
            node.attributes.put(
                    name.group().replaceAll("=",""),
                    value.group().replaceAll("[\"']",""));
    }

    public void checkForArray(Node node)
    {
        if (node.children.size() > 1)
        {
            node.isArray = true;
            String name = null;

            for (Node child : node.children)
            {
                if (name == null)
                    name = child.name;

                else if (!child.name.equals(name))
                    node.isArray = false;
            }
        }
        node.children.forEach(this::checkForArray);
    }

    public String[] splitPayload(String s)
    {
        return s.replaceFirst("<\\?.*\\?>", "").split("(?<=>)|(?=<)");
    }

}
