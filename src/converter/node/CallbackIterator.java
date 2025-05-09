package converter.node;

import lombok.Setter;
import java.util.List;
import java.util.function.Consumer;

@Setter
public class CallbackIterator
{
    private Consumer<Node> callback;
    private int level;

    public CallbackIterator(int level) {
        this.level = level;
    }

    public void iterateTree(List<Node> tree)
    {
        for (Node node : tree)
            iterateNode(node);
    }

    public void iterateNode(Node node)
    {
        if (level-- < 1)
            callback.accept(node);

        for (Node child : node.children)
            iterateNode(child);
    }

}
