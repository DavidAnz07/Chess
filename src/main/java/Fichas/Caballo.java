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
public class Caballo extends Fichas{
    public Caballo(){
        
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
         g.fillRect(25, 70, 50, 10);
         // Cuello
         g.fillRect(40, 40, 20, 30);
         // Cabeza (hocico hacia la izquierda)
         g.fillRect(25, 20, 35, 20);
         // Oreja
         g.fillPolygon(new int[]{50, 55, 60}, new int[]{20, 5, 20}, 3);
    }
}
