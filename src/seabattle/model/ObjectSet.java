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
import java.util.Stack;

/**
 *
 * @author anton
 * Класс набор сущностей, которые должны размещать на море
 * 
 */
public class ObjectSet {
    
    public static enum TYPE_OBJECT { ShipOne, ShipTwo, ShipThree, 
                                     ShipFour, SubmarieFour };
    
    private static HashMap<TYPE_OBJECT, Integer> SET = new HashMap<TYPE_OBJECT, Integer>() {
        {   put(TYPE_OBJECT.SubmarieFour, 1);
            put(TYPE_OBJECT.ShipFour, 1);
            put(TYPE_OBJECT.ShipThree, 2);
            put(TYPE_OBJECT.ShipTwo, 3);
            put(TYPE_OBJECT.ShipOne, 4); }};
    
    /**
     * Изменить количество одного типа сущностей на море
     * @param type - тип сущностей
     * @param amount - колиечество этого типа
     */
    public static void changeSet(TYPE_OBJECT type, int amount) {
        SET.put(type, amount);
    }
    
    // ===================================
    private HashMap<TYPE_OBJECT, Stack<AbstractLineObject> > _objects = new HashMap<TYPE_OBJECT, Stack<AbstractLineObject> >();
    
    /**
     * Конструктор класса
     * @param area - море, на котором должно размещать сущности
     */
    public ObjectSet(SeaArea area) {
        for (Map.Entry<TYPE_OBJECT, Integer> entry : SET.entrySet()) {
            Stack<AbstractLineObject> st = new Stack<AbstractLineObject>();
            _objects.put(entry.getKey(), st);
            for (int i = 0; i < entry.getValue(); ++i) {
                st.push(ObjectFactory.CreateLineObject(entry.getKey(), area));
            }
        }
    }
    
    /**
     * Проверить пустой ли набор
     * @return - истина если набор пустой, иначе ложь
     */
    public boolean isEmpty() {
        for (Map.Entry<TYPE_OBJECT, Stack<AbstractLineObject>> entry : _objects.entrySet()) {
            if (!entry.getValue().isEmpty())
                return false;
        }
        return true;
    }
    
    /**
     * Взять объект из набора
     * @param type - тип объекта нужно взять
     * @return - объект этого типа при успешно, иначе null
     */
    public AbstractLineObject getObject(TYPE_OBJECT type) {
        AbstractLineObject res = null;
        if (_objects.containsKey(type) &&
                !_objects.get(type).isEmpty()) {
            res = _objects.get(type).pop();
        }
        return res;
    }
    
    /**
     * Проверить тип объекта
     * @param obj- объект, нужен проверить
     * @return тип объекта
     */
    private TYPE_OBJECT determiteType(AbstractLineObject obj) {
        if (obj.getClass() == Submarine.class) {
            return TYPE_OBJECT.SubmarieFour;
        } else if (obj.getClass() == Ship.class) {
            if (obj.getLength() == 1) {
                return TYPE_OBJECT.ShipOne;
            }
            else if (obj.getLength() == 2) {
                return TYPE_OBJECT.ShipTwo;
            }
            else if (obj.getLength() == 3) {
                return TYPE_OBJECT.ShipThree;
            }
            return TYPE_OBJECT.ShipFour;
        }
        return null;
    }
    
    /**
     * Возвращать объект в набор
     * @param obj- объект нужен возвращать
     * @return истина если успешно, иначе ложь
     */
    public boolean returnObject(AbstractLineObject obj) {
        TYPE_OBJECT type = determiteType(obj);
        if (_objects.containsKey(type)) {
            obj.clear();
            _objects.get(type).push(obj);
            return true;
        }
        return false;
    }
    
    /**
     * Получить текущее количество одного типа
     * @param type - тип, количество которого нужен узнать
     * @return - текущее количество типа
     */
    public int getCurrentAmountObject(TYPE_OBJECT type) {
        int res = 0;
        if (_objects.containsKey(type))
            res = _objects.get(type).size();
        return res;
    }    
    
}
