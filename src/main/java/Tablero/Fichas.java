/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tablero;

import java.awt.Graphics;

/**
 *
 * @author user
 */
public abstract class Fichas {
    @Override
    public abstract String toString();
    @Override
    public abstract boolean equals(Object a);
    public abstract void dibujar(Graphics g);
}
