/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import java.util.ArrayList;
import java.util.List;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * 
 * Абстрактный сущность на море
 */
public abstract class AbstractLineObject {
    
    public static enum Orientation { Horizontal, Vertical };
    
    protected SeaArea _sea;
    protected List<Cell> _positions = new ArrayList<Cell>();
    protected int _length;
    protected Orientation _orient;
    
    /**
     * Конструктор абстрактного сущности на море
     * @param sea - море, принадлежит судности
     * @param length - длина сущности
     */
    public AbstractLineObject(SeaArea sea, int length) {
        _sea = sea;
        _length = length;
        _orient = Orientation.Horizontal;
    }
    
    /**
     * Повернуть 90 градусов
     */
    public void rotate90() {
        if (_orient == Orientation.Horizontal)
            _orient = Orientation.Vertical;
        else
            _orient = Orientation.Horizontal;
    }
    
    /**
     * Получить ориентацию сущности
     * @return оприентация сущности
     */
    public Orientation getOrientation() {
        return _orient;
    }
    
    //------------ position -----------------
    
    /**
     * Получить позиции, занимает сущность
     * @return - список позиций
     */
    public List<Cell> getPositions() {
        return _positions;
    }
    
    /**
     * Установить позиции на море для сущности
     * @param pos - начальная позиция
     * @return истина при успешно, иначе ложь
     */
    public boolean setPosition(Cell pos) {
        if (pos != null) {
            int h = _length+2, w = 3, hs = _length, ws = 1;
            if (_orient == Orientation.Horizontal) {
                h = 3;
                hs = 1;
                ws = _length;
                w = _length + 2;
            }
            // checking freedom
            List<Cell> around = _sea.getCellsRectangle(pos.row()-1, pos.column()-1, h, w);
            for (Cell cell : around) {
                if (!cell.isFree()) {
                    return false;
                }
            }
            // set position
            _positions.clear();
            List<Cell> poss = _sea.getCellsRectangle(pos.row(), pos.column(), hs, ws);
            if (poss.size() != _length)
                return false;
            _positions.addAll(poss);
            for (Cell cell : _positions) {
                cell.setObject(this);
            }
            return true;
        }
        for (Cell cell : _positions) {
            cell.free();
        }
        _positions.clear();
        return true;
    }
    
    /**
     * Получить длину сущности
     * @return - длина сущности
     */
    public int getLength() {
        return _length;
    }
    
    //--------------- checking ------------------
        
    /**
     * Проверить занимать ли сущность эту позицию
     * @param cell - позиция, нужна проверить
     * @return истина если сущность занимает позицию, иначе ложь
     */
    public boolean isHasCell(Cell cell) {
        for (Cell p : _positions) {
            if (p.equals(cell)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Проверить польностью ли сущность разбитый
     * @return истина если польностью разбитый, иначе ложь
     */
    public boolean isDestroyed() {
        for (Cell cell : _positions) {
            if (!cell.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
    
    //-------------- clear --------------------
    
    /**
     * Очистить позиции и ориентацию
     */
    public void clear() {
        for (Cell cell : _positions) {
            cell.free();
        }
        _positions.clear();
        _orient = Orientation.Horizontal;
    }
    
}
