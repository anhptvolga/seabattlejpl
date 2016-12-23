/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import java.util.ArrayList;
import java.util.List;
import seabattle.model.events.HitObjectEvent;
import seabattle.model.events.HitObjectListener;
import seabattle.model.events.MissObjectEvent;
import seabattle.model.events.MissObjectListener;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * 
 * Абстрактный игрок
 */
public abstract class Player {
    
    /**
     * Конструктор класса
     */
    public Player() {
        _name = "";
    }
    
    // ---------- name of player ------------
    private String _name;
    
    /**
     * Установить имя игрка
     * @param name - имя игрка
     */
    public void setName(String name) {
        _name = name;
    }
    
    /**
     * Получить имя игрка
     * @return имя игрка
     */
    public String getName() {
        return _name;
    }
    
    // ---------------------------------------
    private SeaArea _sea = null;
    
    /**
     * Установить море соперника
     * @param sea - море соперника
     */
    void setTargetSea(SeaArea sea) {
        _sea = sea;
    }
    
    //-------------------------------------------------------------
    List<Cell> _hittedCells = new ArrayList<Cell>();
    List<Cell> _destroyedCells = new ArrayList<Cell>();
    
    /**
     * Стрелять море соперника
     * @param target - позиция удара
     */
    public boolean fire(Cell target) {
        if (target == null) {
            notifyMiss(target);
            return false;
        }
        
        target = _sea.getCell(target.row(), target.column());
        if (!target.isDestroyed()) {    // not destroyed
            if (!_hittedCells.contains(target)) {
                _hittedCells.add(target);
                target.hit();
            }
            AbstractLineObject obj = target.getObject(); 
            if (obj != null) {          // cell has object
                if (obj.getClass() == Ship.class) { 
                    target.destroy();
                    _destroyedCells.add(target);
                    notifyHit(target);
                    return true;
                }
            } else {    // cell empty
                notifyMiss(target);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Получить позиции, которые стреляли
     * @return - позиции, которые стреляли
     */
    public List<Cell> getHittedCell() {
        return _hittedCells;
    }
    
    /**
     * Получить позиции, которые разбитые
     * @return - позиции, которые разбитые
     */
    public List<Cell> getDestroyedCells() {
        return _destroyedCells;
    }
    
    //---------------------- listener -------------
    private List<HitObjectListener> _hitListeners = new ArrayList<HitObjectListener>();
    private List<MissObjectListener> _missListeners = new ArrayList<MissObjectListener>();
    private HitObjectEvent _hitEvent = new HitObjectEvent(this);
    private MissObjectEvent _missEvent = new MissObjectEvent(this);
    
    /**
     * Добавить слушатели при попадание
     * @param obj - слушатель 
     */
    public void addHitListener(HitObjectListener obj) {
        _hitListeners.add(obj);
    }
    
    /**
     * Добавить слушатели при промахе
     * @param obj - слушатель
     */
    public void addMissListener(MissObjectListener obj) {
        _missListeners.add(obj);
    }
    
    /**
     * Сообщать слушателям при попадании
     * @param pos - позиция попадания
     */
    void notifyHit(Cell pos) {
        for (HitObjectListener obj : _hitListeners) {
            obj.hitObject(_hitEvent, pos);
        }
    }
    
    /**
     * Сообщать слушателям при промахе
     * @param pos - позиция промаха
     */
    void notifyMiss(Cell pos) {
        for (MissObjectListener obj : _missListeners) {
            obj.missObject(_missEvent, pos);
        }
    }

}
