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
public class Dama extends Fichas{
    public Dama(){
        
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
         // Cuerpo
         g.fillPolygon(new int[]{40, 60, 70, 30}, new int[]{40, 40, 70, 70}, 4);
         // Corona
         g.fillPolygon(new int[]{25, 35, 50, 65, 75, 65, 35}, new int[]{15, 30, 10, 30, 15, 40, 40}, 7);
    }
}
