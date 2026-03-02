import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    private static final int MIN_RANDOM_SCRAMBLE_STEPS = 10;
    private static final int MAX_RANDOM_SCRAMBLE_STEPS = 50;

    private static class ResultadoHeuristica {
        final String nombre;
        final long tiempoMs;
        final long nodosExpandidos;
        final int movimientos;
        final String estado;
        final Node solucion;

        ResultadoHeuristica(String nombre, long tiempoMs, long nodosExpandidos, int movimientos, String estado, Node solucion) {
            this.nombre = nombre;
            this.tiempoMs = tiempoMs;
            this.nodosExpandidos = nodosExpandidos;
            this.movimientos = movimientos;
            this.estado = estado;
            this.solucion = solucion;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        byte[] goal = NodeUtil.goalState();

        System.out.println("=== 24-Puzzle (5x5) ===");
        System.out.println("Estado objetivo:");
        NodeUtil.printBoard(goal);

        System.out.println("\nEstado inicial:");
        System.out.println("1) Manual");
        System.out.println("2) Aleatorio resoluble");
        int modo = Integer.parseInt(sc.nextLine().trim());

        byte[] start;
        if (modo == 1) {
            start = leerEstadoManual(sc);
            if (start == null) return;
        } else if (modo == 2) {
            Random rnd = new Random();
            int scramble = MIN_RANDOM_SCRAMBLE_STEPS + rnd.nextInt(MAX_RANDOM_SCRAMBLE_STEPS - MIN_RANDOM_SCRAMBLE_STEPS + 1);
            start = generarEstadoAleatorio(goal, scramble, rnd);
        } else {
            System.out.println("Opcion invalida.");
            return;
        }

        System.out.println("\nEstado inicial:");
        NodeUtil.printBoard(start);

        System.out.println("\nModo de ejecucion:");
        System.out.println("1) IDA* (elegir heuristica)");
        System.out.println("2) Tabla comparativa");
        int opcion = Integer.parseInt(sc.nextLine().trim());

        if (opcion == 1) {
            ejecutarIdaIndividual(sc, start, goal);
        } else if (opcion == 2) {
            ejecutarTablaComparativa(start, goal);
        } else {
            System.out.println("Opcion invalida.");
        }
    }

    private static void ejecutarIdaIndividual(Scanner sc, byte[] start, byte[] goal) {
        System.out.println("\nHeuristica a utilizar:");
        System.out.println("1) Distancia de Manhattan");
        System.out.println("2) Manhattan + Conflicto Lineal");
        int h = Integer.parseInt(sc.nextLine().trim());

        Heuristic heuristicaSeleccionada;
        String nombreHeuristica;

        if (h == 1) {
            heuristicaSeleccionada = Heuristic.MANHATTAN;
            nombreHeuristica = "Manhattan";
        } else if (h == 2) {
            heuristicaSeleccionada = Heuristic.MANHATTAN_LINEAR_CONFLICT;
            nombreHeuristica = "Manhattan + Conflicto Lineal";
        } else {
            System.out.println("Opcion invalida.");
            return;
        }

        ResultadoHeuristica resultado = ejecutarIda(start, goal, heuristicaSeleccionada, nombreHeuristica);

        System.out.println("\nAlgoritmo: IDA* (" + resultado.nombre + ")");
        System.out.println("Expandidos: " + resultado.nodosExpandidos);
        System.out.println("Tiempo(ms): " + resultado.tiempoMs);
        System.out.println("Movimientos: " + (resultado.movimientos >= 0 ? resultado.movimientos : "-"));
        if (!"Resuelto".equals(resultado.estado)) {
            System.out.println("Estado: " + resultado.estado);
        }

        if (resultado.solucion != null) {
            System.out.print("Animar solucion? (1=si, 0=no): ");
            int an = Integer.parseInt(sc.nextLine().trim());
            if (an == 1) SearchTree.animateSolution(resultado.solucion, 250);
        } else {
            System.out.println("No se encontro solucion con la heuristica seleccionada (o se aborto).");
        }
    }

    private static void ejecutarTablaComparativa(byte[] start, byte[] goal) {
        ResultadoHeuristica resManhattan = ejecutarIda(start, goal, Heuristic.MANHATTAN, "Manhattan");
        ResultadoHeuristica resManhattanLC = ejecutarIda(start, goal, Heuristic.MANHATTAN_LINEAR_CONFLICT, "Manhattan + Conflicto Lineal");
        imprimirTablaRendimiento(new ResultadoHeuristica[] { resManhattan, resManhattanLC });
    }

    private static ResultadoHeuristica ejecutarIda(byte[] start, byte[] goal, Heuristic heuristic, String nombre) {
        SearchTree st = new SearchTree(new Node(start.clone()));

        try {
            Node sol = st.idaStar(goal, heuristic);

            String estado;
            if (sol != null) estado = "Resuelto";
            else if (st.aborted) estado = "Abortado";
            else estado = "Sin solucion";

            int movimientos = (sol != null) ? longitudSolucion(sol) : -1;
            return new ResultadoHeuristica(nombre, st.timeMs, st.expandedNodes, movimientos, estado, sol);
        } catch (OutOfMemoryError e) {
            System.gc();
            return new ResultadoHeuristica(nombre, st.timeMs, st.expandedNodes, -1, "OOM", null);
        }
    }

    private static void imprimirTablaRendimiento(ResultadoHeuristica[] resultados) {
        System.out.println("\n-----------------------------------------------------------------------------------------------");
        System.out.println(" Heuristica                   | Tiempo(ms) | Nodos expandidos | Movimientos | Estado");
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (ResultadoHeuristica r : resultados) {
            String mov = r.movimientos >= 0 ? String.valueOf(r.movimientos) : "-";
            System.out.printf(" %-28s | %9d | %16d | %11s | %s%n",
                    r.nombre, r.tiempoMs, r.nodosExpandidos, mov, r.estado);
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    private static byte[] leerEstadoManual(Scanner sc) {
        System.out.println("Ingresa 25 numeros (0..24) separados por espacio. 0 = vacio:");
        String line = sc.nextLine().trim();
        String[] parts = line.split("\\s+");

        if (parts.length != 25) {
            System.out.println("Error: se requieren exactamente 25 numeros.");
            return null;
        }

        boolean[] seen = new boolean[25];
        byte[] start = new byte[25];

        for (int i = 0; i < 25; i++) {
            int v;
            try {
                v = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                System.out.println("Error: entrada no numerica en posicion " + i + ".");
                return null;
            }

            if (v < 0 || v > 24 || seen[v]) {
                System.out.println("Error: valor invalido o repetido: " + v);
                return null;
            }

            seen[v] = true;
            start[i] = (byte) v;
        }

        if (!NodeUtil.isSolvable(start)) {
            System.out.println("Error: ese estado no es resoluble para 5x5.");
            return null;
        }

        return start;
    }

    private static byte[] generarEstadoAleatorio(byte[] goal, int scramble, Random rnd) {
        byte[] cur = goal.clone();
        int prevBlank = -1;

        for (int i = 0; i < scramble; i++) {
            int blank = NodeUtil.findBlank(cur);
            List<NodeUtil.Succ> suc = NodeUtil.getSuccessors(cur);
            List<NodeUtil.Succ> candidatos = new ArrayList<>();

            for (NodeUtil.Succ s : suc) {
                int newBlank = NodeUtil.findBlank(s.state);
                if (newBlank != prevBlank) candidatos.add(s);
            }

            if (candidatos.isEmpty()) candidatos = suc;

            NodeUtil.Succ elegido = candidatos.get(rnd.nextInt(candidatos.size()));
            prevBlank = blank;
            cur = elegido.state;
        }

        return cur;
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
}
