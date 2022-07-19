package converter.builder;

import converter.Node;

public class JsonBuilder extends Builder {
    public void buildTree() {
        for (var node : this.tree)
            this.result += this.embraceJson(this.build(node));

        this.result = this.trimExcessiveCommas(this.result);
    }

    public String build(Node node) {
        StringBuilder s = new StringBuilder();

        if (node.parent == null || !node.parent.isArray)
            s.append(String.format("\"%s\": ", node.name));

        if (node.attributes.isEmpty() && node.children.isEmpty()) {
            if (node.value == null || node.value.equals("null")) s.append("null, ");
            else s.append(String.format("\"%s\", ", node.value));
        }

        if (!node.attributes.isEmpty() && node.children.isEmpty()) {
            appendObjectOpen(s);
            s.append(attributesToJson(node));
            if (node.value == null || node.value.equals("null"))
                s.append(String.format("\"#%s\": null", node.name));
            else s.append(String.format("\"#%s\": \"%s\"", node.name, node.value));
            appendObjectClose(s);
        }
        else if (!node.attributes.isEmpty()) {
            appendObjectOpen(s);
            s.append(attributesToJson(node));
            s.append(String.format("\"#%s\":", node.name));
            appendChildren(node, s);
            appendObjectClose(s);
        }
        else if (!node.children.isEmpty())
            appendChildren(node, s);

        return s.toString();
    }

    public String attributesToJson(Node node) {
        StringBuilder s = new StringBuilder();

        for (var pair : node.attributes.entrySet()) {
            if (pair.getValue() == null || pair.getValue().equals("null"))
                s.append(String.format("\"@%s\": null, ", pair.getKey()));
            else s.append(String.format("\"@%s\": \"%s\", ", pair.getKey(), pair.getValue()));
        }
        return s.toString();
    }

    public void appendChildren(Node node, StringBuilder s) {
        appendConditionalOpen(node, s);
        for (Node child : node.children)
            s.append(this.build(child));
        appendConditionalClose(node, s);
    }

    public void appendConditionalOpen(Node node, StringBuilder s) {
        if (node.isArray) appendArrayOpen(s);
        else appendObjectOpen(s);
    }

    public void appendConditionalClose(Node node, StringBuilder s) {
        if (node.isArray) appendArrayClose(s);
        else appendObjectClose(s);
    }

    public void appendObjectOpen(StringBuilder s) {
        s.append(" { ");
    }

    public void appendArrayOpen(StringBuilder s) {
        s.append(" [ ");
    }

    public void appendArrayClose(StringBuilder s) {
        s.append(" ], ");
    }

    public void appendObjectClose(StringBuilder s) {
        s.append(" }, ");
    }

    public String trimExcessiveCommas(String s) {
        return s.replaceAll(",\\s*(?=[]}])", " ");
    }

    public String embraceJson(String s) {
        return String.format("{ %s }", s);
    }
}