/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import seabattle.model.AbstractLineObject;

/**
 *
 * @author anton
 * 
 * Позиция ячейки 
 */
public class Cell {
    
    // -- Диапазоны возможных значений по горизонтали и вертикали для всех позиций --

    private static CellRange _horizontalRange = new CellRange(0, 0);
    private static CellRange _verticalRange = new CellRange(0, 0);

    public static void setHorizontalRange(int min, int max){
        if(CellRange.isValidRange(min, max))
        { _horizontalRange = new CellRange(min, max); }
    }
    
    public static CellRange horizontalRange(){
      return _horizontalRange;
    }

    public static void setVerticalRange(int min, int max){
        if(CellRange.isValidRange(min, max))
        { _verticalRange = new CellRange(min, max); }
    }
    
    public static CellRange verticalRange(){
        return _verticalRange;
    }
    
    // ------------------ Позиция внутри диапазона ---------------------

    private int _row;// = _verticalRange.min();
    private int _column;// = _horizontalRange.min();
    
    public Cell(int row, int col)
    {
        if(!isValid(row, col))
        {  //  TODO породить исключение
        }
        _row = row;
        _column = col;
    }
    
    public int row(){
        
        if(!isValid())
        {  //  TODO породить исключение 
        }
        return _row;
    }

    public int column(){

        if(!isValid())
        {  //  TODO породить исключение 
        }
        return _column;
    }
    
    public Cell next(int direction) {
        int[] xx = { 0, 1, 0,-1};
        int[] yy = {-1, 0, 1, 0};
        if (0 <= direction && direction <= 3) {
            int x = _row + xx[direction];
            int y = _column + yy[direction];
            if (y < 0) {
                y = _verticalRange.max();
                x = x + 1;
            }
            if (x > _horizontalRange.max()) {
                x = 0;
                y = y + 1;
            }
            if (y > _verticalRange.max()) {
                y = 0;
                x = x - 1;
            }
            if (x < 0) {
                x = _horizontalRange.max();
                y = y - 1;
            }
            if (isValid(x, y)) {
                return new Cell(x, y);
            }
            else {
                return null;
            }
        }
        return null;
    }
    
    // Позиция может стать невалидной, если изменились диапазоны допустимых значений
    public boolean isValid(){
        return isValid(_row, _column);
    }
    
    public static boolean isValid(int row, int col){
        return _horizontalRange.contains(col) && _verticalRange.contains(row);
   }
    
    @Override
    public Cell clone(){
        return new Cell(_row, _column);
    }
    
    // ------------------ Сравнение позиций ---------------------
    
    @Override
    public boolean equals(Object other){
        if(!isValid())
        {  //  TODO породить исключение 
        }
        if(other instanceof Cell) {
            // Типы совместимы, можно провести преобразование
            Cell otherPosition = (Cell)other;
            // Возвращаем результат сравнения углов
            return _row == otherPosition._row && _column == otherPosition._column;
        }
        return false;
    }
    
    // ------------------ Признак разбитый ячейк ---------------------
    private boolean _isDestroyed = false;
    
    public boolean isDestroyed() {
        return _isDestroyed;
    }
    
    public void destroy() {
        _isDestroyed = true;
    }
    // ----------------
    private boolean _isHit = false;
    
    public boolean isHitted() {
        return _isHit;
    }
    
    public void hit() {
        _isHit = true;
    }
    
    // ------------------------------------
    private AbstractLineObject _object = null;
    
    public boolean setObject(AbstractLineObject obj) {
        if (_object == null) {
            _object = obj;
            return true;
        }
        return false;
    }
    
    public AbstractLineObject getObject() {
        return _object;
    }
    
    public void free() {
        _object = null;
    }
    
    public boolean isFree() {
        return _object == null;
    }
    
}
