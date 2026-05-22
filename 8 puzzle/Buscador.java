import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Buscador {

    public Estado inicio;

    public Buscador(Estado inicio) {
        this.inicio = inicio;
    }

    public Estado bfs(String objetivo) {
        if (inicio == null) return null;

        String estadoObjetivo = objetivo.replace('0', ' ');

        Set<String> visitados = new HashSet<>();
        Queue<Estado> pendientes = new LinkedList<>();

        pendientes.add(inicio);
        visitados.add(inicio.estado);

        while (!pendientes.isEmpty()) {
            Estado actual = pendientes.poll();

            if (actual.estado.equals(estadoObjetivo)) {
                return actual;
            }

            for (Estado siguiente : actual.expandir()) {
                if (visitados.add(siguiente.estado)) {
                    pendientes.add(siguiente);
                }
            }
        }

        return null;
    }
}
