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
public class Torre extends Fichas {
    
    public Torre(){
        
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
         g.fillRect(35, 30, 30, 40);
         // Base superior
         g.fillRect(30, 25, 40, 5);
         // Almenas
         g.fillRect(30, 15, 10, 10);
         g.fillRect(45, 15, 10, 10);
         g.fillRect(60, 15, 10, 10);
    }
}
