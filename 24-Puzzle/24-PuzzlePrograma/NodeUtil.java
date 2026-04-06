import java.util.ArrayList;
import java.util.List;

public class NodeUtil {
    public static final int N = 5;
    public static final int SIZE = 25;
    public static final byte BLANK = 0;

    // posiciones meta para Manhattan
    private static final int[] GOAL_R = new int[25];
    private static final int[] GOAL_C = new int[25];

    static {
        for (int v = 1; v <= 24; v++) {
            int idx = v - 1;
            GOAL_R[v] = idx / N;
            GOAL_C[v] = idx % N;
        }
        GOAL_R[0] = 4; GOAL_C[0] = 4;
    }

    public static final class Key {
        public final long hi;
        public final long lo;
        private final int hash;

        public Key(long hi, long lo) {
            this.hi = hi;
            this.lo = lo;
            this.hash = (int)(hi ^ (hi >>> 32) ^ lo ^ (lo >>> 32));
        }
        @Override public int hashCode() { return hash; }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Key)) return false;
            Key k = (Key)o;
            return hi == k.hi && lo == k.lo;
        }
    }

   public static Key keyOf(byte[] s) {
    long lo = 0L, hi = 0L;
    int bit = 0; 

    for (int i = 0; i < SIZE; i++) {
        long v = (long)(s[i] & 31); 

        if (bit <= 59) {
            lo |= (v << bit);
        } else if (bit >= 64) {
            hi |= (v << (bit - 64));
        } else {
            int loBits = 64 - bit;             
            long loMask = (1L << loBits) - 1L;  

            lo |= ((v & loMask) << bit);        
            hi |= (v >>> loBits);              
        }

        bit += 5;
    }

    return new Key(hi, lo);
}

    public static int findBlank(byte[] s) {
        for (int i = 0; i < SIZE; i++) if (s[i] == BLANK) return i;
        return -1;
    }

    public static byte[] goalState() {
        byte[] g = new byte[SIZE];
        for (int i = 0; i < SIZE - 1; i++) g[i] = (byte)(i + 1);
        g[SIZE - 1] = 0;
        return g;
    }

    public static boolean isGoal(byte[] s) {
        for (int i = 0; i < SIZE - 1; i++) if (s[i] != (byte)(i + 1)) return false;
        return s[SIZE - 1] == 0;
    }

    // Sucesores 5x5 + movimiento asociado
    public static List<Succ> getSuccessors(byte[] state) {
        int pos = findBlank(state);
        int r = pos / N, c = pos % N;

        List<Succ> out = new ArrayList<>(4);

        // UP
        if (r > 0) out.add(makeSwap(state, pos, (r - 1) * N + c, MovementType.UP));
        // DOWN
        if (r < N - 1) out.add(makeSwap(state, pos, (r + 1) * N + c, MovementType.DOWN));
        // LEFT
        if (c > 0) out.add(makeSwap(state, pos, r * N + (c - 1), MovementType.LEFT));
        // RIGHT
        if (c < N - 1) out.add(makeSwap(state, pos, r * N + (c + 1), MovementType.RIGHT));

        return out;
    }

    public static final class Succ {
        public final byte[] state;
        public final MovementType move;
        public Succ(byte[] s, MovementType m) { state = s; move = m; }
    }

    private static Succ makeSwap(byte[] s, int a, int b, MovementType m) {
        byte[] n = s.clone();
        byte tmp = n[a];
        n[a] = n[b];
        n[b] = tmp;
        return new Succ(n, m);
    }

    public static int heuristic(byte[] s, Heuristic h) {
        int man = manhattan(s);
        if (h == Heuristic.MANHATTAN) return man;
        return man + linearConflict(s);
    }

    public static int manhattan(byte[] s) {
        int sum = 0;
        for (int i = 0; i < SIZE; i++) {
            int v = s[i] & 0xFF;
            if (v == 0) continue;
            int r = i / N, c = i % N;
            sum += Math.abs(r - GOAL_R[v]) + Math.abs(c - GOAL_C[v]);
        }
        return sum;
    }

    // Conflicto lineal: +2 por cada par invertido en misma fila/col correcta
    public static int linearConflict(byte[] s) {
        int conflicts = 0;

        // filas
        for (int r = 0; r < N; r++) {
            int base = r * N;
            for (int i = 0; i < N; i++) {
                int a = s[base + i] & 0xFF;
                if (a == 0 || GOAL_R[a] != r) continue;
                for (int j = i + 1; j < N; j++) {
                    int b = s[base + j] & 0xFF;
                    if (b == 0 || GOAL_R[b] != r) continue;
                    if (GOAL_C[a] > GOAL_C[b]) conflicts += 2;
                }
            }
        }

        // columnas
        for (int c = 0; c < N; c++) {
            for (int i = 0; i < N; i++) {
                int a = s[i * N + c] & 0xFF;
                if (a == 0 || GOAL_C[a] != c) continue;
                for (int j = i + 1; j < N; j++) {
                    int b = s[j * N + c] & 0xFF;
                    if (b == 0 || GOAL_C[b] != c) continue;
                    if (GOAL_R[a] > GOAL_R[b]) conflicts += 2;
                }
            }
        }

        return conflicts;
    }

    // Solubilidad 5x5 (N impar): inversiones pares
    public static boolean isSolvable(byte[] s) {
        int inv = 0;
        for (int i = 0; i < SIZE; i++) {
            int a = s[i] & 0xFF;
            if (a == 0) continue;
            for (int j = i + 1; j < SIZE; j++) {
                int b = s[j] & 0xFF;
                if (b == 0) continue;
                if (a > b) inv++;
            }
        }
        return inv % 2 == 0;
    }

    public static void printBoard(byte[] s) {
        for (int i = 0; i < SIZE; i++) {
            int v = s[i] & 0xFF;
            System.out.printf("%2s ", v == 0 ? "·" : String.valueOf(v));
            if ((i + 1) % N == 0) System.out.println();
        }
    }
}
