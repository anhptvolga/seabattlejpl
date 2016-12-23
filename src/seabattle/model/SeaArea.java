/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * Класс игровое море
 * 
 */
public class SeaArea {
    
    //-------------- Size -----------------
    private int _height;
    private int _width;
    
    /**
     * Конструктор моря
     * @param height - высота
     * @param width - ширина
     */
    public SeaArea(int height, int width) {
        _height = height;
        _width = width;
        setNewCells();
        _objects = new HashMap<Class, List<AbstractLineObject> >();
    }
    
    /**
     * Получить высоту
     * @return - высота
     */
    public int height() {
        return _height;
    }
    
    /**
     * Получить ширину
     * @return - ширина
     */
    public int width() {
        return _width;
    }
    // ------------- ячейки --------------
    private List<Cell> _cells = new ArrayList<Cell>();
    
    /**
     * Создать ячейки моря
     */
    private void setNewCells() {
        _cells.clear();
        for (int i = 0; i < _height; ++i) {
            for (int j = 0; j < _width; ++j) {
                _cells.add(new Cell(j,i));
            }
        }
    }
    
    /**
     * Получить все ячейки на море
     * @return 
     */
    public List<Cell> getAllCells() {
        return _cells;
    }
    
    /**
     * Получить одну конкретный ячейку
     * @param row - x-координат ячейки
     * @param column - у-координат ячейки
     * @return - ячейку на море при успешно, иначе null
     */
    public Cell getCell(int row, int column) {
        int index = column * _width + row;
        if (index < _cells.size()) {
            return _cells.get(index);
        }
        return null;
    }
    
    /**
     * Получить ячейки на прямоугонике
     * @param xstart - верхний левый х-координат прямоуголника
     * @param ystart - верхний левый у-координат прямоуголника
     * @param height - высота прямоуголника
     * @param width - ширина прямоуголника
     * @return - список ячейков на прямоуголнике
     */
    public List<Cell> getCellsRectangle(int xstart, int ystart, int height, int width) {
        List<Cell> res = new ArrayList<Cell>();
        if (xstart < 0) {
            width += xstart;
            xstart = 0;
        }
        xstart = (xstart >= _height) ? _height-1 : xstart;
        
        if (ystart < 0) {
            height += ystart;
            ystart = 0;
        }
        ystart = (ystart >= _width) ? _width-1 : ystart;
        
        if (height > 0 &&  width > 0) {       
            height = (ystart + height >= _height) ? _height-ystart : height;
            width = (xstart + width >= _width) ? _width-xstart : width;
            for (int i = ystart; i < ystart + height; ++i) 
                for (int j = xstart; j < xstart + width; ++j) {
                    res.add(_cells.get(i*_width + j));
            }
        }
    
        return res;
    }
    
    //------------------- Objects ----------------
    private HashMap<Class, List<AbstractLineObject> > _objects = new HashMap<Class, List<AbstractLineObject> >();
    
    /**
     * Добавить объект на море
     * @param pos - позиция на море
     * @param obj- объект
     * @return истина при успехно, иначе ложь
     */
    public boolean addObject(Cell pos, AbstractLineObject obj) {
        Class objClass = obj.getClass();
        if (obj.setPosition(pos)) {
            if( _objects.containsKey(objClass))  {     	
                _objects.get(objClass).add(obj);
            } else {
                List<AbstractLineObject> objList = new ArrayList<AbstractLineObject>();
                objList.add(obj);
                _objects.put(objClass, objList);
            }
            return true;
        } 
        return false;
    }

    /**
     * Извлечь объект из моря
     * @param obj - объект
     * @return истина при успехно, иначе ложь
     */
    public boolean removeObject(AbstractLineObject obj){
        boolean success = false;
        Class objClass = obj.getClass();
        if (_objects.containsKey(objClass)) {
            success = _objects.get(objClass).remove(obj);
            if(success) 
                obj.setPosition(null);
        }
        return success;
    }
    
    /**
     * Получить объект, занимает позицию
     * @param pos - позиция, занимает объект
     * @return объект при успешно, иначе null
     */
    public AbstractLineObject object(Cell pos) {
        for (Map.Entry<Class, List<AbstractLineObject> > entry : _objects.entrySet()) { 
            for (AbstractLineObject obj : entry.getValue()) {
                if (obj.isHasCell(pos)) {
                    return obj;
                }
            }
        }
        return null;
    }

    /**
     * Получить объекты одного типа
     * @param objType - тип объекта
     * @return - списпок объектов
     */
    public List<AbstractLineObject> objects(Class objType) {   
        List<AbstractLineObject> objList = new ArrayList<AbstractLineObject>();
        if(_objects.containsKey(objType)) { 
            objList.addAll( _objects.get(objType) );
        }
        return objList;
    }

    /**
     * Получить все объекты на море
     * @return - список объектов
     */
    public List<AbstractLineObject> objects() {
        List<AbstractLineObject> objList = new ArrayList<AbstractLineObject>();
        for (Map.Entry<Class, List<AbstractLineObject> > entry : _objects.entrySet()) { 
            objList.addAll( entry.getValue() );
        }
        return objList;
    }
    
    // ---------------------------- Очистка ----------------------------
    /**
     * Отчистить море
     */
    public void clear() {
        _cells.clear();
        _objects.clear();
    }
    
}
