package Tablero;

import Fichas.Alfil;
import Fichas.Caballo;
import Fichas.Dama;
import Fichas.Peon;
import Fichas.Rey;
import Fichas.Torre;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class Logica_Tablero extends JPanel {
    public static boolean turnoIAEnProgreso = false;
    String celda;
    int x, y;

    public Logica_Tablero(int posicion, String celda, int x, int y) {
        this.celda = celda;
        this.x = x;
        this.y = y;

        // Emparejar cuadros
        if ((posicion % 2 == 0) && (posicion <= 8) || ((posicion % 2 == 0) && (posicion > 16) && (posicion <= 24))
                || ((posicion % 2 == 0) && (posicion > 32) && (posicion <= 40))
                || ((posicion % 2 == 0) && (posicion > 48) && (posicion <= 56))) {
            this.setBackground(Color.BLACK);
        } else if (((posicion % 2 != 0) && (posicion > 8) && (posicion <= 16))
                || ((posicion % 2 != 0) && (posicion > 24) && (posicion <= 32))
                || ((posicion % 2 != 0) && (posicion > 40) && (posicion <= 48))
                || ((posicion % 2 != 0) && (posicion > 56) && (posicion <= 64))) {
            this.setBackground(Color.BLACK);
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Inicio();

            }
        });
    }

    private boolean enLimites(int nx, int ny) {
        return nx >= 0 && nx < 8 && ny >= 0 && ny < 8;
    }

    private void limpiarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Tablero_Main.a[i][j].equals("+")) {
                    Tablero_Main.a[i][j] = " ";
                } else if (Tablero_Main.a[i][j].contains("+")) {
                    Tablero_Main.a[i][j] = Tablero_Main.a[i][j].replace("+", "");
                }
            }
        }
    }

    public void Inicio() {
        if (turnoIAEnProgreso) {
            return;
        }

        // En partidas contra la IA, el usuario solo puede mover las piezas blancas
        String modoActual = Tablero_Main.getDificultad();
        if (!"2 jugadores".equals(modoActual) && Tablero_Main.movimientos % 2 == 1) {
            return;
        }

        String actual = Tablero_Main.a[this.x][this.y];

        limpiarTablero();

        if (actual.contains("+")) {
            String piezaMovida = Tablero_Main.a[Tablero_Main.xant][Tablero_Main.yant];
            String piezaCapturada = actual.replace("+", "");

            Tablero_Main.a[this.x][this.y] = piezaMovida;
            Tablero_Main.a[Tablero_Main.xant][Tablero_Main.yant] = " ";
            Tablero_Main.movimientos++;

            if (this.y == 7 && "p".equals(piezaMovida)) {
                Tablero_Main.a[this.x][this.y] = "d";
            }
            if (this.y == 0 && "p!".equals(piezaMovida)) {
                Tablero_Main.a[this.x][this.y] = "d!";
            }

            if (getParent() != null)
                getParent().repaint();

            // Comprobar captura de rey
            if (piezaCapturada.contains("r!")) {
                Tablero_Main.notificarEstado("¡Victoria de las Blancas! Rey Negro capturado.");
                return;
            } else if (piezaCapturada.contains("r")) {
                Tablero_Main.notificarEstado("¡Victoria de las Negras! Rey Blanco capturado.");
                return;
            }

            if (!"2 jugadores".equals(modoActual)) {
                turnoIAEnProgreso = true;
                Tablero_Main.notificarEstado("Modo: " + modoActual + " | IA pensando...");
                javax.swing.Timer timerIA = new javax.swing.Timer(250, ev -> {
                    ((javax.swing.Timer) ev.getSource()).stop();
                    new Thread(() -> {
                        Movimiento.ejecutarMinimaxIA();
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            turnoIAEnProgreso = false;
                        });
                    }).start();
                });
                timerIA.setRepeats(false);
                timerIA.start();
            } else {
                if (Tablero_Main.movimientos % 2 == 0) {
                    Tablero_Main.notificarEstado("Modo: 2 Jugadores | Turno: Jugador 1 (Blancas)");
                } else {
                    Tablero_Main.notificarEstado("Modo: 2 Jugadores | Turno: Jugador 2 (Negras)");
                }
            }
            return;
        }

        if (!Tablero_Main.a[this.x][this.y].equals(" ")) {
            mostrarmovimiento();
        } else {
            if (getParent() != null)
                getParent().repaint();
        }
    }

    public void mostrarmovimiento() {

        String ma = Tablero_Main.a[this.x][this.y];

        if (Tablero_Main.movimientos % 2 == 0) {
            switch (ma) {
                case "p" -> {

                    if (enLimites(this.x, this.y + 1) && Tablero_Main.a[this.x][this.y + 1].equals(" ")) {
                        Tablero_Main.a[this.x][this.y + 1] = "+";
                    }

                    if (this.y == 1) {
                        if (enLimites(this.x, this.y + 2) && Tablero_Main.a[this.x][this.y + 2].equals(" ")) {
                            Tablero_Main.a[this.x][this.y + 2] = "+";
                        }
                    }
                    if (enLimites(this.x + 1, this.y + 1) && !" ".equals(Tablero_Main.a[this.x + 1][this.y + 1])
                            && Tablero_Main.a[this.x + 1][this.y + 1].contains("!")) {
                        String u = Tablero_Main.a[this.x + 1][this.y + 1];
                        Tablero_Main.a[this.x + 1][this.y + 1] = u + "+";
                    }

                    if (enLimites(this.x - 1, this.y + 1) && !" ".equals(Tablero_Main.a[this.x - 1][this.y + 1])
                            && Tablero_Main.a[this.x - 1][this.y + 1].contains("!")) {
                        String u = Tablero_Main.a[this.x - 1][this.y + 1];
                        Tablero_Main.a[this.x - 1][this.y + 1] = u + "+";
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "c" -> {
                    int[][] saltos = { { 2, -1 }, { 1, -2 }, { 2, 1 }, { 1, 2 }, { -2, 1 }, { -1, 2 }, { -1, -2 },
                            { -2, -1 } };
                    for (int[] salto : saltos) {
                        int nx = this.x + salto[0];
                        int ny = this.y + salto[1];
                        if (enLimites(nx, ny)) {
                            if (" ".equals(Tablero_Main.a[nx][ny])) {
                                Tablero_Main.a[nx][ny] = "+";
                            } else if (Tablero_Main.a[nx][ny].contains("!")) {
                                Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                            }
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "a" -> {
                    int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "t" -> {
                    int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "d" -> {
                    int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { 1, 0 },
                            { -1, 0 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "r" -> {
                    int[][] pasos = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 },
                            { -1, 1 } };
                    for (int[] paso : pasos) {
                        int nx = this.x + paso[0];
                        int ny = this.y + paso[1];
                        if (enLimites(nx, ny)) {
                            if (" ".equals(Tablero_Main.a[nx][ny])) {
                                Tablero_Main.a[nx][ny] = "+";
                            } else if (Tablero_Main.a[nx][ny].contains("!")) {
                                Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                            }
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

            }
        }

        if (Tablero_Main.movimientos % 2 == 1) {
            switch (ma) {
                case "p!" -> {
                    if (enLimites(this.x, this.y - 1) && Tablero_Main.a[this.x][this.y - 1].equals(" ")) {
                        Tablero_Main.a[this.x][this.y - 1] = "+";
                    }

                    if (this.y == 6) {
                        if (enLimites(this.x, this.y - 2) && Tablero_Main.a[this.x][this.y - 2].equals(" ")) {
                            Tablero_Main.a[this.x][this.y - 2] = "+";
                        }
                    }
                    if (enLimites(this.x + 1, this.y - 1) && !" ".equals(Tablero_Main.a[this.x + 1][this.y - 1])
                            && !Tablero_Main.a[this.x + 1][this.y - 1].contains("!")) {
                        String u = Tablero_Main.a[this.x + 1][this.y - 1];
                        Tablero_Main.a[this.x + 1][this.y - 1] = u + "+";
                    }

                    if (enLimites(this.x - 1, this.y - 1) && !" ".equals(Tablero_Main.a[this.x - 1][this.y - 1])
                            && !Tablero_Main.a[this.x - 1][this.y - 1].contains("!")) {
                        String u = Tablero_Main.a[this.x - 1][this.y - 1];
                        Tablero_Main.a[this.x - 1][this.y - 1] = u + "+";
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "c!" -> {
                    int[][] saltos = { { 2, -1 }, { 1, -2 }, { 2, 1 }, { 1, 2 }, { -2, 1 }, { -1, 2 }, { -1, -2 },
                            { -2, -1 } };
                    for (int[] salto : saltos) {
                        int nx = this.x + salto[0];
                        int ny = this.y + salto[1];
                        if (enLimites(nx, ny)) {
                            if (" ".equals(Tablero_Main.a[nx][ny])) {
                                Tablero_Main.a[nx][ny] = "+";
                            } else if (!Tablero_Main.a[nx][ny].contains("!")) {
                                Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                            }
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "a!" -> {
                    int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && !Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "t!" -> {
                    int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && !Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "d!" -> {
                    int[][] dirs = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 }, { 0, 1 }, { 0, -1 }, { 1, 0 },
                            { -1, 0 } };
                    for (int[] dir : dirs) {
                        int nx = this.x + dir[0];
                        int ny = this.y + dir[1];
                        while (enLimites(nx, ny) && " ".equals(Tablero_Main.a[nx][ny])) {
                            Tablero_Main.a[nx][ny] = "+";
                            nx += dir[0];
                            ny += dir[1];
                        }
                        if (enLimites(nx, ny) && !" ".equals(Tablero_Main.a[nx][ny])
                                && !Tablero_Main.a[nx][ny].contains("!")) {
                            Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

                case "r!" -> {
                    int[][] pasos = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { 1, -1 }, { 1, 1 },
                            { -1, 1 } };
                    for (int[] paso : pasos) {
                        int nx = this.x + paso[0];
                        int ny = this.y + paso[1];
                        if (enLimites(nx, ny)) {
                            if (" ".equals(Tablero_Main.a[nx][ny])) {
                                Tablero_Main.a[nx][ny] = "+";
                            } else if (!Tablero_Main.a[nx][ny].contains("!")) {
                                Tablero_Main.a[nx][ny] = Tablero_Main.a[nx][ny] + "+";
                            }
                        }
                    }
                    if (getParent() != null)
                        getParent().repaint();
                    Tablero_Main.xant = this.x;
                    Tablero_Main.yant = this.y;
                }

            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.celda = Tablero_Main.a[this.x][this.y];
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        Fichas c = null;
        switch (this.celda) {
            case "p", "p+" -> {
                c = new Peon();
                if ("p+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "c", "c+" -> {
                c = new Caballo();
                if ("c+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "a", "a+" -> {
                c = new Alfil();
                if ("a+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "t", "t+" -> {
                c = new Torre();
                if ("t+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "d", "d+" -> {
                c = new Dama();
                if ("d+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "r", "r+" -> {
                c = new Rey();
                if ("r+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "p!", "p!+" -> {
                g.setColor(Color.darkGray);
                c = new Peon();
                if ("p!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "c!", "c!+" -> {
                g.setColor(Color.darkGray);
                c = new Caballo();
                if ("c!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "a!", "a!+" -> {
                g.setColor(Color.darkGray);
                c = new Alfil();
                if ("a!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "t!", "t!+" -> {
                g.setColor(Color.darkGray);
                c = new Torre();
                if ("t!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "d!", "d!+" -> {
                g.setColor(Color.darkGray);
                c = new Dama();
                if ("d!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "r!", "r!+" -> {
                g.setColor(Color.darkGray);
                c = new Rey();
                if ("r!+".equals(this.celda)) {
                    g.setColor(Color.red);
                }
            }
            case "+" -> {
                g.setColor(Color.red);
                g.fillOval(getWidth() / 2, getHeight() / 2, 20, 20);
            }
        }
        if (c != null) {
            c.dibujar(g);
        }
    }
}
