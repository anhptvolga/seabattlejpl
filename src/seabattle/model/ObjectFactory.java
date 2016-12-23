/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

/**
 *
 * @author anton
 * Фабрика объектов на море
 */
public class ObjectFactory {
    
    /**
     * Получить новый объект на море
     * @param type - тип объект
     * @param area - море, принадлежит объект
     * @return - новый объект
     */
    public static AbstractLineObject CreateLineObject(ObjectSet.TYPE_OBJECT type, SeaArea area) {
        AbstractLineObject res = null;
        switch (type) {
            case ShipOne:
                res = new Ship(area, 1);
                break;
            case ShipTwo:
                res = new Ship(area, 2);
                break;
            case ShipThree:
                res = new Ship(area, 3);
                break;
            case ShipFour:
                res = new Ship(area, 4);
                break;
            case SubmarieFour:
                res = new Submarine(area, 4);
                break;
        };
        return res;
    }
    
}
