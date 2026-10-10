/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Fichas;

import Tablero.Fichas;
import java.awt.Graphics;

/**
 *
 * @author user
 */
public class Alfil extends Fichas {
    
    public Alfil(){
        
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object a) {
        Fichas b = (Fichas) a;
        return true;
    }
    
    @Override
    public void dibujar(Graphics g){
         // Base
         g.fillRect(30, 70, 40, 10);
         // Cuerpo
         g.fillRect(45, 40, 10, 30);
         // Cabeza
         g.fillOval(35, 20, 30, 40);
         // Bola superior
         g.fillOval(45, 10, 10, 10);
    }
}
