/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Tablero;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class Tablero_Main extends JFrame {

    static String[][] a = {
            { "t", "p", " ", " ", " ", " ", "p!", "t!" },
            { "c", "p", " ", " ", " ", " ", "p!", "c!" },
            { "a", "p", " ", " ", " ", " ", "p!", "a!" },
            { "r", "p", " ", " ", " ", " ", "p!", "r!" },
            { "d", "p", " ", " ", " ", " ", "p!", "d!" },
            { "a", "p", " ", " ", " ", " ", "p!", "a!" },
            { "c", "p", " ", " ", " ", " ", "p!", "c!" },
            { "t", "p", " ", " ", " ", " ", "p!", "t!" }
    };

    JPanel inicio, seleccion, jugadas;
    JButton selec;
    JComboBox<String> dificultad;
    JLabel ini1;
    static int xant = 0, yant = 0;
    static int movimientos = 0;
    public static String dificultadActual = "Facil";
    public static Tablero_Main instancia;

    public static String getDificultad() {
        return dificultadActual;
    }

    public static void repintarTablero() {
        if (instancia != null && instancia.inicio != null) {
            instancia.inicio.revalidate();
            instancia.inicio.repaint();
        }
    }

    public static void notificarEstado(String mensaje) {
        if (instancia != null) {
            instancia.setTitle("Ajedrez - " + mensaje);
        }
    }

    public Tablero_Main() {
        instancia = this;
        int posicion = 0;
        String[] d = { "Facil", "Medio", "Dificil", "Muy Dificil", "2 jugadores" };

        this.setLayout(new BorderLayout());
        this.setSize(900, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // tablero
        inicio = new JPanel(new GridLayout(8, 8, 0, 0));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                posicion++;
                inicio.add(new Logica_Tablero(posicion, a[j][i], j, i));
            }
        }

        // seleccion dificultad
        seleccion = new JPanel();
        seleccion.setLayout(new GridBagLayout());
        GridBagConstraints conf = new GridBagConstraints();
        dificultad = new JComboBox<>(d);
        ini1 = new JLabel("Escoja el nivel de dificultad: ");
        selec = new JButton("Elegir");
        conf.gridx = 0;
        conf.gridy = 0;
        seleccion.add(ini1, conf);
        conf.gridx = 1;
        conf.gridy = 0;
        seleccion.add(dificultad);
        conf.gridx = 0;
        conf.gridy = 1;
        conf.gridwidth = 2;
        conf.fill = GridBagConstraints.HORIZONTAL;
        seleccion.add(selec, conf);

        // estadisticas
        jugadas = new JPanel(new GridLayout(10, 0, 0, 0));

        this.add(inicio, BorderLayout.CENTER);
        this.add(seleccion, BorderLayout.NORTH);

        selec.addActionListener(e -> SeleccionarDificultad());

    }

    public void SeleccionarDificultad() {
        String eleccion = (String) dificultad.getSelectedItem();
        if (eleccion == null) {
            return;
        }

        // Si hay una partida en curso, confirmar antes de reiniciar el tablero
        if (movimientos > 0) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "Hay una partida en curso. ¿Deseas reiniciar el tablero para cambiar a dificultad: " + eleccion
                            + "?",
                    "Reiniciar Partida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                // Restaurar la seleccion previa en el ComboBox
                dificultad.setSelectedItem(dificultadActual);
                return;
            }
        }

        // Reiniciar tablero usando Actualizar() y preparar la dificultad seleccionada
        Actualizar();
        dificultadActual = eleccion;
        aplicarDificultad(eleccion);
        Juego();
    }

    public void aplicarDificultad(String nivel) {
        dificultadActual = nivel;

        switch (nivel) {
            case "Facil" -> {
                // Minimax Profundidad 1 configurado en Logica_Tablero
            }
            case "Medio" -> {
                // Minimax Profundidad 2 con Poda Alfa-Beta en Logica_Tablero
            }
            case "Dificil" -> {
                // Minimax Profundidad 3 con Tablas Posicionales en Logica_Tablero
            }
            case "Muy Dificil" -> {
                // Minimax Profundidad 4 con Poda y Ordenamiento MVV-LVA en Logica_Tablero
            }
            case "2 jugadores" -> {
                Op2jugadores();
            }
        }
    }

    public void Actualizar() {
        a = new String[][] {
                { "t", "p", " ", " ", " ", " ", "p!", "t!" },
                { "c", "p", " ", " ", " ", " ", "p!", "c!" },
                { "a", "p", " ", " ", " ", " ", "p!", "a!" },
                { "r", "p", " ", " ", " ", " ", "p!", "r!" },
                { "d", "p", " ", " ", " ", " ", "p!", "d!" },
                { "a", "p", " ", " ", " ", " ", "p!", "a!" },
                { "c", "p", " ", " ", " ", " ", "p!", "c!" },
                { "t", "p", " ", " ", " ", " ", "p!", "t!" }
        };
        movimientos = 0;
        xant = 0;
        yant = 0;
        Logica_Tablero.turnoIAEnProgreso = false;

        if (inicio != null) {
            inicio.revalidate();
            inicio.repaint();
        }
    }

    public void Juego() {

    }

    public void Op2jugadores() {

    }

    public static void main(String[] args) {
        Tablero_Main po = new Tablero_Main();
        po.setVisible(true);
    }
}
