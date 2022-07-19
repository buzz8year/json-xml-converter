package converter;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {
    public Map<String, String> attributes = new LinkedHashMap<>();
    public List<Node> children = new ArrayList<>();
    public Node parent;

    public boolean isUnified = true, isArray = false;
    public String payload, name, value;

    public Node(String payload) {
        this.payload = payload;
    }
}
