import java.util.ArrayList;
import java.util.List;

public class Node {
    private byte[] state;                 // 25 tiles (0..24), 0 = blank
    private List<Node> children = new ArrayList<>();
    private Node parent;

    private int cost;                     // g(n)
    private int estimatedCostToGoal;      // h(n)
    private int totalCost;                // f(n)=g+h
    private int depth;

    // (opcional) guardar el movimiento que llevó a este nodo
    private MovementType moveFromParent;

    public Node(byte[] state) {
        this.state = state;
    }

    public byte[] getState() { return state; }
    public void setState(byte[] state) { this.state = state; }

    public List<Node> getChildren() { return children; }
    public void addChild(Node child) { this.children.add(child); }

    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public int getEstimatedCostToGoal() { return estimatedCostToGoal; }
    public void setEstimatedCostToGoal(int estimatedCostToGoal) { this.estimatedCostToGoal = estimatedCostToGoal; }

    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public void setTotalCost(int cost, int estimatedCost) { this.totalCost = cost + estimatedCost; }

    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }

    public MovementType getMoveFromParent() { return moveFromParent; }
    public void setMoveFromParent(MovementType moveFromParent) { this.moveFromParent = moveFromParent; }
}