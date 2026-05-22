import java.util.LinkedList;

public class Estado {
    private static final int LADO_TABLERO = 3;
    private static final int TOTAL_CASILLAS = LADO_TABLERO * LADO_TABLERO;
    private static final int[][] MOVIMIENTOS = {
            {1, 3},
            {0, 2, 4},
            {1, 5},
            {0, 4, 6},
            {1, 3, 5, 7},
            {2, 4, 8},
            {3, 7},
            {4, 6, 8},
            {5, 7}
    };

    public String estado;
    public Estado anterior;
    public int profundidad;
    public int costo;

    public Estado(String estado) {
        this(estado, 0, null);
    }

    public Estado(String estado, int profundidad, Estado anterior) {
        this.estado = limpiar(estado);
        this.profundidad = profundidad;
        this.anterior = anterior;
        this.costo = profundidad;
    }

    public LinkedList<Estado> expandir() {
        LinkedList<Estado> sucesores = new LinkedList<>();
        int posicionHueco = this.estado.indexOf(' ');

        if (posicionHueco < 0 || posicionHueco >= MOVIMIENTOS.length) {
            return sucesores;
        }

        int nuevoNivel = this.profundidad + 1;
        for (int destino : MOVIMIENTOS[posicionHueco]) {
            String nuevoEstado = intercambiar(this.estado, posicionHueco, destino);
            sucesores.add(new Estado(nuevoEstado, nuevoNivel, this));
        }

        return sucesores;
    }

    public void mostrarRuta() {
        if (this.anterior != null) {
            this.anterior.mostrarRuta();
        }

        System.out.println("Tablero:");
        for (int i = 0; i < TOTAL_CASILLAS; i++) {
            System.out.print(this.estado.charAt(i) + " ");
            if ((i + 1) % LADO_TABLERO == 0) System.out.println();
        }

        System.out.println();
        System.out.println("Nivel: " + this.profundidad);
        System.out.println("________________________________");
        System.out.println();
    }

    private String intercambiar(String texto, int origen, int destino) {
        char[] caracteres = texto.toCharArray();
        char temporal = caracteres[origen];
        caracteres[origen] = caracteres[destino];
        caracteres[destino] = temporal;
        return new String(caracteres);
    }

    private static String limpiar(String texto) {
        if (texto == null) return "";

        texto = texto.replace('0', ' ');

        StringBuilder limpio = new StringBuilder();
        for (int k = 0; k < texto.length(); k++) {
            char caracter = texto.charAt(k);
            if (esParteDelTablero(caracter)) {
                limpio.append(caracter);
            }
        }

        if (limpio.length() == TOTAL_CASILLAS) return limpio.toString();
        return texto;
    }

    private static boolean esParteDelTablero(char caracter) {
        return (caracter >= '1' && caracter <= '8') || caracter == ' ';
    }
}
