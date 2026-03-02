import java.util.*;

public class SearchTree {
    private Node root;

    // limites para que no crashee
    private static final int BFS_LIMIT = 80_000;
    private static final int DFS_LIMIT = 80_000;
    private static final int DFS_EXPANSION_LIMIT = 300_000;
    private static final int UCS_LIMIT = 120_000;

    // stats
    public long expandedNodes = 0;
    public long timeMs = 0;
    public boolean aborted = false;

    public SearchTree(Node root) {
        this.root = root;
    }

    public Node getRoot() { return root; }

    //BFS
    public Node breadthFirstSearch(byte[] goal) {
        long t0 = System.nanoTime();
        expandedNodes = 0;
        aborted = false;

        ArrayDeque<Node> q = new ArrayDeque<>();
        HashSet<NodeUtil.Key> visited = new HashSet<>();

        q.add(root);
        visited.add(NodeUtil.keyOf(root.getState()));

        while (!q.isEmpty()) {
            Node cur = q.poll();
            expandedNodes++;

            if (Arrays.equals(cur.getState(), goal)) {
                timeMs = (System.nanoTime() - t0) / 1_000_000;
                return cur;
            }

            for (NodeUtil.Succ suc : NodeUtil.getSuccessors(cur.getState())) {
                NodeUtil.Key k = NodeUtil.keyOf(suc.state);
                if (visited.contains(k)) continue;

                if (visited.size() >= BFS_LIMIT) {
                    aborted = true;
                    timeMs = (System.nanoTime() - t0) / 1_000_000;
                    return null;
                }

                Node child = new Node(suc.state);
                child.setParent(cur);
                child.setDepth(cur.getDepth() + 1);
                child.setCost(cur.getCost() + 1);
                child.setMoveFromParent(suc.move);

                q.add(child);
                visited.add(k);
            }
        }

        timeMs = (System.nanoTime() - t0) / 1_000_000;
        return null;
    }

    // DFS con limite
    public Node depthFirstSearch(byte[] goal, int depthLimit) {
        long t0 = System.nanoTime();
        if (depthLimit > DFS_LIMIT) depthLimit = DFS_LIMIT;
        expandedNodes = 0;
        aborted = false;

        class Frame {
            Node node;
            List<NodeUtil.Succ> successors;
            int next;
            boolean entered;

            Frame(Node node) {
                this.node = node;
                this.successors = NodeUtil.getSuccessors(node.getState());
                this.next = 0;
                this.entered = false;
            }
        }

        Stack<Frame> stack = new Stack<>();
        HashSet<NodeUtil.Key> onPath = new HashSet<>();

        onPath.add(NodeUtil.keyOf(root.getState()));
        stack.push(new Frame(root));

        while (!stack.isEmpty()) {
            Frame f = stack.peek();
            Node cur = f.node;

            if (!f.entered) {
                f.entered = true;
                expandedNodes++;

                if (expandedNodes >= DFS_EXPANSION_LIMIT) {
                    aborted = true;
                    timeMs = (System.nanoTime() - t0) / 1_000_000;
                    return null;
                }

                if (Arrays.equals(cur.getState(), goal)) {
                    timeMs = (System.nanoTime() - t0) / 1_000_000;
                    return cur;
                }

                if (cur.getDepth() >= depthLimit) {
                    stack.pop();
                    onPath.remove(NodeUtil.keyOf(cur.getState()));
                    continue;
                }
            }

            if (f.next >= f.successors.size()) {
                stack.pop();
                onPath.remove(NodeUtil.keyOf(cur.getState()));
                continue;
            }

            NodeUtil.Succ suc = f.successors.get(f.next++);
            NodeUtil.Key k = NodeUtil.keyOf(suc.state);
            if (onPath.contains(k)) continue;

            if (onPath.size() >= DFS_LIMIT) {
                aborted = true;
                timeMs = (System.nanoTime() - t0) / 1_000_000;
                return null;
            }

            Node child = new Node(suc.state);
            child.setParent(cur);
            child.setDepth(cur.getDepth() + 1);
            child.setCost(cur.getCost() + 1);
            child.setMoveFromParent(suc.move);

            onPath.add(k);
            stack.push(new Frame(child));
        }

        timeMs = (System.nanoTime() - t0) / 1_000_000;
        return null;
    }

    // UCS 
    public Node uniformCostSearch(byte[] goal) {
        long t0 = System.nanoTime();
        expandedNodes = 0;
        aborted = false;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getCost));
        HashMap<NodeUtil.Key, Integer> bestG = new HashMap<>();

        root.setCost(0);
        pq.add(root);
        bestG.put(NodeUtil.keyOf(root.getState()), 0);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            expandedNodes++;

            NodeUtil.Key ck = NodeUtil.keyOf(cur.getState());
            Integer bg = bestG.get(ck);
            if (bg == null || bg != cur.getCost()) continue;

            if (Arrays.equals(cur.getState(), goal)) {
                timeMs = (System.nanoTime() - t0) / 1_000_000;
                return cur;
            }

            for (NodeUtil.Succ suc : NodeUtil.getSuccessors(cur.getState())) {
                int ng = cur.getCost() + 1;
                NodeUtil.Key nk = NodeUtil.keyOf(suc.state);

                Integer old = bestG.get(nk);
                if (old == null || ng < old) {
                    if (bestG.size() >= UCS_LIMIT) {
                        aborted = true;
                        timeMs = (System.nanoTime() - t0) / 1_000_000;
                        return null;
                    }

                    Node child = new Node(suc.state);
                    child.setParent(cur);
                    child.setDepth(cur.getDepth() + 1);
                    child.setCost(ng);
                    child.setMoveFromParent(suc.move);

                    bestG.put(nk, ng);
                    pq.add(child);
                }
            }
        }

        timeMs = (System.nanoTime() - t0) / 1_000_000;
        return null;
    }

    //IDA*
    public Node idaStar(byte[] goal, Heuristic heuristic) {
        long t0 = System.nanoTime();
        expandedNodes = 0;
        aborted = false;
        foundNode = null;

        int bound = NodeUtil.heuristic(root.getState(), heuristic);
        HashSet<NodeUtil.Key> onPath = new HashSet<>();
        onPath.add(NodeUtil.keyOf(root.getState()));

        while (true) {
            int t = idaDfs(root, goal, 0, bound, heuristic, onPath);
            if (t == FOUND) {
                timeMs = (System.nanoTime() - t0) / 1_000_000;
                return foundNode;
            }
            if (t == Integer.MAX_VALUE) {
                timeMs = (System.nanoTime() - t0) / 1_000_000;
                return null;
            }
            bound = t;
        }
    }

    private static final int FOUND = -1;
    private Node foundNode = null;

    private int idaDfs(Node cur, byte[] goal, int g, int bound, Heuristic heu, HashSet<NodeUtil.Key> onPath) {
        expandedNodes++;

        int h = NodeUtil.heuristic(cur.getState(), heu);
        int f = g + h;

        if (f > bound) return f;
        if (Arrays.equals(cur.getState(), goal)) {
            foundNode = cur;
            return FOUND;
        }

        int min = Integer.MAX_VALUE;

        List<NodeUtil.Succ> sucList = NodeUtil.getSuccessors(cur.getState());
        sucList.sort(Comparator.comparingInt(s -> NodeUtil.heuristic(s.state, heu)));

        for (NodeUtil.Succ suc : sucList) {
            NodeUtil.Key k = NodeUtil.keyOf(suc.state);
            if (onPath.contains(k)) continue;

            Node child = new Node(suc.state);
            child.setParent(cur);
            child.setDepth(cur.getDepth() + 1);
            child.setCost(g + 1);
            child.setMoveFromParent(suc.move);

            onPath.add(k);
            int t = idaDfs(child, goal, g + 1, bound, heu, onPath);
            if (t == FOUND) return FOUND;
            if (t < min) min = t;
            onPath.remove(k);
        }

        return min;
    }

    // Animacion
    public static void animateSolution(Node goalNode, int delayMs) {
        if (goalNode == null) {
            System.out.println("No hay solucion para animar.");
            return;
        }

        List<Node> path = new ArrayList<>();
        Node cur = goalNode;
        while (cur != null) {
            path.add(cur);
            cur = cur.getParent();
        }
        Collections.reverse(path);

        System.out.println("\n=== Animacion de solucion ===");
        for (int i = 0; i < path.size(); i++) {
            System.out.println("Paso " + i + (i == 0 ? "" : " (mov: " + path.get(i).getMoveFromParent() + ")"));
            NodeUtil.printBoard(path.get(i).getState());
            System.out.println("------");
            try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
        }
        System.out.println("Total movimientos: " + (path.size() - 1));
    }
}



