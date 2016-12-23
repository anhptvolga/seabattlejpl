/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * Класс подводной лодки
 */
public class Submarine extends AbstractLineObject {

    //private Cell _towerPosition = null;
    private int _posTower = 0;
    
    /**
     * Конструктор подлодки
     * @param sea - море, принадлежит кораблей
     * @param length - длина подлодки
     */
    public Submarine(SeaArea sea, int length) {
        super(sea, length);
    }
    
    /**
     * Установить позиции рубки
     * @param pos - растояние от верхний левый позиции до рубки
     * @return истина при успешно, иначе ложь
     */
    public boolean setTowerSubmarineDistance(int pos) {
        if (pos < this._length){
            _posTower = pos;
            return true;
        }
        return false;
    }
    
    /**
     * Получить ячейку рубки
     * @return ячейк рубки при установили, иначе null
     */
    public Cell getTowerSubmarine() {
        if (!_positions.isEmpty()) {
            return _positions.get(_posTower);
        }
        return null;
    }
    
    /**
     * Поулчить растояние от верхний левый позиции до рубки
     * @return - растояние от верхний левый позиции до рубки
     */
    public int getTowerDistance() {
        return _posTower;
    }
}
