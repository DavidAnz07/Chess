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
public class Rey extends Fichas {
    
    public Rey(){
        
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
         g.fillRect(40, 40, 20, 30);
         // Base de la corona
         g.fillRect(30, 30, 40, 10);
         // Cruz horizontal
         g.fillRect(35, 15, 30, 5);
         // Cruz vertical
         g.fillRect(47, 5, 6, 25);
    }
}
