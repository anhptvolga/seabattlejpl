/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

/**
 *
 * @author anton
 * Класс кораблей на море
 */
public class Ship extends AbstractLineObject {
    
    /**
     * Конструктор класса
     * @param sea - море, принадлежит корабли
     * @param length - длина корабли
     */
    public Ship(SeaArea sea, int length) {
        super(sea, length);
    }
}
