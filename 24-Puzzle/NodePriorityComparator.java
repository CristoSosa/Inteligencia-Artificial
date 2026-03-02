import java.util.Comparator;

public class NodePriorityComparator implements Comparator<Node> {
    @Override
    public int compare(Node x, Node y) {
        return Integer.compare(x.getTotalCost(), y.getTotalCost());
    }
}