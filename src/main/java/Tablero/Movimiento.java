package Tablero;

import java.util.ArrayList;
import java.util.List;

public class Movimiento {
    public int desdeX, desdeY;
    public int haciaX, haciaY;
    public String pieza;
    public String captura;

    public Movimiento(int desdeX, int desdeY, int haciaX, int haciaY, String pieza, String captura) {
        this.desdeX = desdeX;
        this.desdeY = desdeY;
        this.haciaX = haciaX;
        this.haciaY = haciaY;
        this.pieza = pieza;
        this.captura = (captura != null) ? captura : " ";
    }

    private static boolean enLimitesVirtual(int nx, int ny) {
        return nx >= 0 && nx < 8 && ny >= 0 && ny < 8;
    }

    public static String[][] clonarTablero(String[][] orig) {
        String[][] copia = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copia[i][j] = orig[i][j].replace("+", "").trim();
                if (copia[i][j].isEmpty()) {
                    copia[i][j] = " ";
                }
            }
        }
        return copia;
    }

    public static void aplicarMovimientoVirtual(String[][] tab, Movimiento m) {
        String p = tab[m.desdeX][m.desdeY];
        tab[m.haciaX][m.haciaY] = p;
        tab[m.desdeX][m.desdeY] = " ";
        if ("p!".equals(p) && m.haciaY == 0) {
            tab[m.haciaX][m.haciaY] = "d!";
        } else if ("p".equals(p) && m.haciaY == 7) {
            tab[m.haciaX][m.haciaY] = "d";
        }
    }

    public static int valorPieza(String p) {
        if (p == null)
            return 0;
        String clean = p.replace("!", "").replace("+", "").trim();
        return switch (clean) {
            case "p" -> 100;
            case "c" -> 320;
            case "a" -> 330;
            case "t" -> 500;
            case "d" -> 900;
            case "r" -> 20000;
            default -> 0;
        };
    }

    public static List<Movimiento> generarMovimientos(String[][] tab, boolean esNegras) {
        List<Movimiento> movs = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String pieza = tab[i][j].replace("+", "").trim();
                if (pieza.isEmpty() || pieza.equals(" ")) {
                    continue;
                }

                if (esNegras) {
                    if (!pieza.contains("!")) {
                        continue;
                    }

                    switch (pieza) {
                        case "p!" -> {
                            if (enLimitesVirtual(i, j - 1) && tab[i][j - 1].replace("+", "").trim().isEmpty()) {
                                movs.add(new Movimiento(i, j, i, j - 1, pieza, " "));
                                if (j == 6 && tab[i][j - 2].replace("+", "").trim().isEmpty()) {
                                    movs.add(new Movimiento(i, j, i, j - 2, pieza, " "));
                                }
                            }
                            if (enLimitesVirtual(i + 1, j - 1)) {
                                String cap = tab[i + 1][j - 1].replace("+", "").trim();
                                if (!cap.isEmpty() && !cap.contains("!")) {
                                    movs.add(new Movimiento(i, j, i + 1, j - 1, pieza, cap));
                                }
                            }
                            if (enLimitesVirtual(i - 1, j - 1)) {
                                String cap = tab[i - 1][j - 1].replace("+", "").trim();
                                if (!cap.isEmpty() && !cap.contains("!")) {
                                    movs.add(new Movimiento(i, j, i - 1, j - 1, pieza, cap));
                                }
                            }
                        }
                        case "c!" -> {
                            int[][] saltos = { { 2, -1 }, { 1, -2 }, { 2, 1 }, { 1, 2 }, { -2, 1 }, { -1, 2 },
                                    { -1, -2 }, { -2, -1 } };
                            for (int[] s : saltos) {
                                int nx = i + s[0];
                                int ny = j + s[1];
                                if (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else if (!cap.contains("!")) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                    }
                                }
                            }
                        }
                        case "a!" -> {
                            int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (!cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "t!" -> {
                            int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (!cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "d!" -> {
                            int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { 1, 0 },
                                    { -1, 0 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (!cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "r!" -> {
                            int[][] pasos = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 },
                                    { -1, 1 } };
                            for (int[] p : pasos) {
                                int nx = i + p[0];
                                int ny = j + p[1];
                                if (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else if (!cap.contains("!")) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (pieza.contains("!")) {
                        continue;
                    }

                    switch (pieza) {
                        case "p" -> {
                            if (enLimitesVirtual(i, j + 1) && tab[i][j + 1].replace("+", "").trim().isEmpty()) {
                                movs.add(new Movimiento(i, j, i, j + 1, pieza, " "));
                                if (j == 1 && tab[i][j + 2].replace("+", "").trim().isEmpty()) {
                                    movs.add(new Movimiento(i, j, i, j + 2, pieza, " "));
                                }
                            }
                            if (enLimitesVirtual(i + 1, j + 1)) {
                                String cap = tab[i + 1][j + 1].replace("+", "").trim();
                                if (!cap.isEmpty() && cap.contains("!")) {
                                    movs.add(new Movimiento(i, j, i + 1, j + 1, pieza, cap));
                                }
                            }
                            if (enLimitesVirtual(i - 1, j + 1)) {
                                String cap = tab[i - 1][j + 1].replace("+", "").trim();
                                if (!cap.isEmpty() && cap.contains("!")) {
                                    movs.add(new Movimiento(i, j, i - 1, j + 1, pieza, cap));
                                }
                            }
                        }
                        case "c" -> {
                            int[][] saltos = { { 2, -1 }, { 1, -2 }, { 2, 1 }, { 1, 2 }, { -2, 1 }, { -1, 2 },
                                    { -1, -2 }, { -2, -1 } };
                            for (int[] s : saltos) {
                                int nx = i + s[0];
                                int ny = j + s[1];
                                if (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else if (cap.contains("!")) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                    }
                                }
                            }
                        }
                        case "a" -> {
                            int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "t" -> {
                            int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "d" -> {
                            int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { 1, 0 },
                                    { -1, 0 } };
                            for (int[] d : dirs) {
                                int nx = i + d[0];
                                int ny = j + d[1];
                                while (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else {
                                        if (cap.contains("!")) {
                                            movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                        }
                                        break;
                                    }
                                    nx += d[0];
                                    ny += d[1];
                                }
                            }
                        }
                        case "r" -> {
                            int[][] pasos = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 },
                                    { -1, 1 } };
                            for (int[] p : pasos) {
                                int nx = i + p[0];
                                int ny = j + p[1];
                                if (enLimitesVirtual(nx, ny)) {
                                    String cap = tab[nx][ny].replace("+", "").trim();
                                    if (cap.isEmpty()) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, " "));
                                    } else if (cap.contains("!")) {
                                        movs.add(new Movimiento(i, j, nx, ny, pieza, cap));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return movs;
    }

    public static boolean estaReyBajoAtaque(String[][] tab, boolean esNegras) {
        String reyBuscado = esNegras ? "r!" : "r";
        int rx = -1, ry = -1;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String c = tab[i][j].replace("+", "").trim();
                if (reyBuscado.equals(c)) {
                    rx = i;
                    ry = j;
                    break;
                }
            }
            if (rx != -1) break;
        }

        // Si el rey ya no esta en el tablero, esta capturado
        if (rx == -1) {
            return true;
        }

        // Generar movimientos del oponente para ver si alguno alcanza la posicion del rey
        List<Movimiento> ataquesRival = generarMovimientos(tab, !esNegras);
        for (Movimiento atk : ataquesRival) {
            if (atk.haciaX == rx && atk.haciaY == ry) {
                return true;
            }
        }
        return false;
    }

    public static List<Movimiento> generarMovimientosLegales(String[][] tab, boolean esNegras) {
        List<Movimiento> pseudos = generarMovimientos(tab, esNegras);
        List<Movimiento> legales = new ArrayList<>();

        for (Movimiento m : pseudos) {
            String[][] tabSim = clonarTablero(tab);
            aplicarMovimientoVirtual(tabSim, m);
            // El movimiento es legal unicamente si no deja al propio rey bajo ataque
            if (!estaReyBajoAtaque(tabSim, esNegras)) {
                legales.add(m);
            }
        }
        return legales;
    }

    public static int evaluarTablero(String[][] tab, String dificultad) {
        int score = 0;
        boolean usarPosicion = "Dificil".equals(dificultad) || "Muy Dificil".equals(dificultad);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                String celda = tab[x][y].replace("+", "").trim();
                if (celda.isEmpty() || celda.equals(" ")) {
                    continue;
                }

                int baseVal = valorPieza(celda);
                int posBonus = 0;

                if (usarPosicion) {
                    String clean = celda.replace("!", "");
                    if ("p".equals(clean)) {
                        posBonus = celda.contains("!") ? (6 - y) * 12 : (y - 1) * 12;
                    } else if ("c".equals(clean)) {
                        int dist = Math.abs(x - 3) + Math.abs(y - 3);
                        posBonus = (6 - dist) * 6;
                    } else if ("d".equals(clean) || "a".equals(clean)) {
                        int dist = Math.abs(x - 3) + Math.abs(y - 3);
                        posBonus = (6 - dist) * 4;
                    }
                }

                if (celda.contains("!")) {
                    score += (baseVal + posBonus);
                } else {
                    score -= (baseVal + posBonus);
                }
            }
        }
        return score;
    }

    public static void ordenarMovimientos(List<Movimiento> movs) {
        movs.sort((m1, m2) -> {
            int score1 = 0;
            int score2 = 0;
            if (!m1.captura.trim().isEmpty() && !m1.captura.equals(" ")) {
                score1 = valorPieza(m1.captura) * 10 - valorPieza(m1.pieza);
            }
            if (!m2.captura.trim().isEmpty() && !m2.captura.equals(" ")) {
                score2 = valorPieza(m2.captura) * 10 - valorPieza(m2.pieza);
            }
            return Integer.compare(score2, score1);
        });
    }

    public static int minimax(String[][] tab, int profundidad, int alfa, int beta, boolean maximizando,
            String dificultad) {
        if (profundidad == 0) {
            return evaluarTablero(tab, dificultad);
        }

        List<Movimiento> movs = generarMovimientosLegales(tab, maximizando);
        if (movs.isEmpty()) {
            boolean jaque = estaReyBajoAtaque(tab, maximizando);
            if (jaque) {
                // Jaque Mate
                return maximizando ? (-100000 - profundidad * 100) : (100000 + profundidad * 100);
            } else {
                // Tablas por Rey Ahogado
                return 0;
            }
        }

        ordenarMovimientos(movs);

        if (maximizando) {
            int maxEval = Integer.MIN_VALUE;
            for (Movimiento m : movs) {
                String[][] tabSim = clonarTablero(tab);
                aplicarMovimientoVirtual(tabSim, m);
                int eval = minimax(tabSim, profundidad - 1, alfa, beta, false, dificultad);
                maxEval = Math.max(maxEval, eval);
                alfa = Math.max(alfa, eval);
                if (beta <= alfa) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Movimiento m : movs) {
                String[][] tabSim = clonarTablero(tab);
                aplicarMovimientoVirtual(tabSim, m);
                int eval = minimax(tabSim, profundidad - 1, alfa, beta, true, dificultad);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alfa) {
                    break;
                }
            }
            return minEval;
        }
    }

    public static Movimiento obtenerMejorMovimiento(String dificultad) {
        String[][] tabActual = clonarTablero(Tablero_Main.a);
        List<Movimiento> movs = generarMovimientosLegales(tabActual, true);
        if (movs.isEmpty()) {
            return null;
        }

        // Nivel Facil: 60% probabilidad de jugada aleatoria legal
        if ("Facil".equals(dificultad)) {
            if (Math.random() < 0.60) {
                return movs.get((int) (Math.random() * movs.size()));
            }
        }

        int profundidad;
        switch (dificultad) {
            case "Facil" -> profundidad = 1;
            case "Medio" -> profundidad = 2;
            case "Dificil" -> profundidad = 3;
            case "Muy Dificil" -> profundidad = 4;
            default -> profundidad = 2;
        }

        ordenarMovimientos(movs);

        Movimiento mejorMov = null;
        int mejorPuntaje = Integer.MIN_VALUE;
        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Movimiento m : movs) {
            String[][] tabSim = clonarTablero(tabActual);
            aplicarMovimientoVirtual(tabSim, m);
            int puntajeReal = minimax(tabSim, profundidad - 1, alfa, beta, false, dificultad);

            int puntajeCandidato = puntajeReal;
            if ("Facil".equals(dificultad)) {
                puntajeCandidato += (int) (Math.random() * 80 - 40);
            }

            if (puntajeCandidato > mejorPuntaje) {
                mejorPuntaje = puntajeCandidato;
                mejorMov = m;
            }
            alfa = Math.max(alfa, puntajeReal);
        }

        return mejorMov != null ? mejorMov : movs.get((int) (Math.random() * movs.size()));
    }

    public static void ejecutarMinimaxIA() {
        String dif = Tablero_Main.getDificultad();
        Movimiento mejor = obtenerMejorMovimiento(dif);
        if (mejor != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (Tablero_Main.a[i][j].equals("+")) {
                        Tablero_Main.a[i][j] = " ";
                    } else if (Tablero_Main.a[i][j].contains("+")) {
                        Tablero_Main.a[i][j] = Tablero_Main.a[i][j].replace("+", "");
                    }
                }
            }

            String pieza = Tablero_Main.a[mejor.desdeX][mejor.desdeY];
            String capturada = Tablero_Main.a[mejor.haciaX][mejor.haciaY].replace("+", "").trim();

            Tablero_Main.a[mejor.haciaX][mejor.haciaY] = pieza;
            Tablero_Main.a[mejor.desdeX][mejor.desdeY] = " ";

            // Coronacion de peon negro al llegar a fila 0
            if (mejor.haciaY == 0 && "p!".equals(pieza)) {
                Tablero_Main.a[mejor.haciaX][mejor.haciaY] = "d!";
            }

            Tablero_Main.xant = mejor.desdeX;
            Tablero_Main.yant = mejor.desdeY;
            Tablero_Main.movimientos++;

            Tablero_Main.repintarTablero();

            // Comprobar captura del Rey Blanco
            if ("r".equals(capturada)) {
                Tablero_Main.notificarEstado("¡Victoria de las Negras (IA)! Rey Blanco capturado.");
                return;
            }

            // Comprobar estado de las Blancas despues de la jugada de la IA
            List<Movimiento> movsBlancas = generarMovimientosLegales(Tablero_Main.a, false);
            if (movsBlancas.isEmpty()) {
                if (estaReyBajoAtaque(Tablero_Main.a, false)) {
                    Tablero_Main.notificarEstado("¡Jaque Mate! Victoria de las Negras (IA).");
                } else {
                    Tablero_Main.notificarEstado("¡Tablas por Rey Ahogado!");
                }
            } else if (estaReyBajoAtaque(Tablero_Main.a, false)) {
                Tablero_Main.notificarEstado("Modo: " + dif + " | ¡Jaque al Rey Blanco! Turno: Blancas");
            } else {
                Tablero_Main.notificarEstado("Modo: " + dif + " | Turno: Blancas (Tú)");
            }
        } else {
            // La IA no tiene movimientos legales posibles
            if (estaReyBajoAtaque(Tablero_Main.a, true)) {
                Tablero_Main.notificarEstado("¡Victoria de las Blancas! Jaque Mate a las Negras.");
            } else {
                Tablero_Main.notificarEstado("¡Tablas! La IA esta ahogada.");
            }
        }
    }
}