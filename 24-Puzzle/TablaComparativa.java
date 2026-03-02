public class TablaComparativa {
    private static final int DFS_LIMIT_COMPARATIVA = 25;
    private static final boolean INCLUIR_DFS = true;

    private static class Resultado {
        final String metodo;
        final long tiempoMs;
        final long nodos;
        final int movimientos;
        final String estado;

        Resultado(String metodo, long tiempoMs, long nodos, int movimientos, String estado) {
            this.metodo = metodo;
            this.tiempoMs = tiempoMs;
            this.nodos = nodos;
            this.movimientos = movimientos;
            this.estado = estado;
        }
    }

    public static void ejecutarComparativa(byte[] start, byte[] goal) {
        Resultado dfs = INCLUIR_DFS
                ? ejecutar("DFS (lim=" + DFS_LIMIT_COMPARATIVA + ")", start, goal, 2)
                : new Resultado("DFS", 0, 0, -1, "Omitido");

        Resultado[] resultados = new Resultado[] {
                ejecutar("BFS", start, goal, 1),
                dfs,
                ejecutar("UCS", start, goal, 3),
                ejecutar("IDA* (Manhattan)", start, goal, 4),
                ejecutar("IDA* (Manhattan+LC)", start, goal, 5)
        };

        imprimirTabla(resultados);
        System.out.println("IDA* reduce memoria frente a A* porque evita mantener una frontera global enorme.");
    }

    private static Resultado ejecutar(String nombre, byte[] start, byte[] goal, int algoritmo) {
        SearchTree st = new SearchTree(new Node(start.clone()));

        try {
            Node sol = ejecutarAlgoritmo(st, goal, algoritmo);

            String estado;
            if (sol != null) estado = "Resuelto";
            else if (st.aborted) estado = "Abortado";
            else estado = "Sin solucion";

            int movimientos = (sol != null) ? longitudSolucion(sol) : -1;
            return new Resultado(nombre, st.timeMs, st.expandedNodes, movimientos, estado);
        } catch (OutOfMemoryError e) {
            return new Resultado(nombre, st.timeMs, st.expandedNodes, -1, "OOM");
        } catch (Throwable t) {
            return new Resultado(nombre, st.timeMs, st.expandedNodes, -1, "Error");
        }
    }

    private static Node ejecutarAlgoritmo(SearchTree st, byte[] goal, int algoritmo) {
        if (algoritmo == 1) return st.breadthFirstSearch(goal);
        if (algoritmo == 2) return st.depthFirstSearch(goal, DFS_LIMIT_COMPARATIVA);
        if (algoritmo == 3) return st.uniformCostSearch(goal);
        if (algoritmo == 4) return st.idaStar(goal, Heuristic.MANHATTAN);
        if (algoritmo == 5) return st.idaStar(goal, Heuristic.MANHATTAN_LINEAR_CONFLICT);
        return null;
    }

    private static int longitudSolucion(Node goalNode) {
        int len = 0;
        Node cur = goalNode;
        while (cur != null && cur.getParent() != null) {
            len++;
            cur = cur.getParent();
        }
        return len;
    }

    private static void imprimirTabla(Resultado[] resultados) {
        System.out.println("\n------------------------------------------------------------------------------------------------");
        System.out.println(" Metodo                    | Tiempo(ms) | Nodos expandidos | Movimientos | Estado");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (Resultado r : resultados) {
            String mov = r.movimientos >= 0 ? String.valueOf(r.movimientos) : "-";
            System.out.printf(" %-25s | %9d | %16d | %11s | %s%n",
                    r.metodo, r.tiempoMs, r.nodos, mov, r.estado);
        }

        System.out.println("------------------------------------------------------------------------------------------------");
    }
}
