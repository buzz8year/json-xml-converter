package converter.builder;

import converter.Node;

public class XmlBuilder extends Builder {

    public void buildTree() {
        for (var node : this.tree)
            this.result += this.build(node);

        if (this.tree.size() > 1)
            this.result = this.embraceWithRoot(this.result);
    }

    public String build(Node node) {
        StringBuilder sb = new StringBuilder(String.format("<%s", node.name));
        if (!node.attributes.isEmpty())
            sb.append(attributesToXml(node));

        if (node.children.isEmpty()) {
            if (node.value == null || node.value.equals("null"))
                sb.append(" />");
            else {
                String v = node.value;
                String e = v.equals("no-show") || v.equals("\"\"") ? "" : v;
                sb.append(String.format(">%s</%s>", e, node.name));
            }
        }
        else {
            sb.append(">");
            for (Node child : node.children)
                sb.append(this.build(child));
            sb.append(String.format("</%s>", node.name));
        }
        return sb.toString();
    }

    public String attributesToXml(Node node) {
        StringBuilder sb = new StringBuilder();
        for (var pair : node.attributes.entrySet()) {
            String v = pair.getValue();
            String e = v.equals("null") || v.equals("no-show") ? "" : v;
            sb.append(String.format(" %s=\"%s\"", pair.getKey(), e));
        }
        return sb.toString();
    }

    public String embraceWithRoot(String s) {
        return String.format("<root>%s</root>", s);
    }
}
