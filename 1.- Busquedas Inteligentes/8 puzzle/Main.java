public class Main {
    private static final String ESTADO_INICIAL = "1238 4765";
    private static final String ESTADO_OBJETIVO = "1284376 5";

    public static void main(String[] args) {
        Estado inicial = new Estado(ESTADO_INICIAL);
        Buscador buscador = new Buscador(inicial);

        Estado solucion = buscador.bfs(ESTADO_OBJETIVO);

        if (solucion == null) {
            System.out.println("No se encontro solucion.");
            return;
        }

        System.out.println("Estado Inicial: " + solucion.estado);
        System.out.println("Profundidad: " + solucion.profundidad);
        System.out.println("\n");
        solucion.mostrarRuta();
    }
}
