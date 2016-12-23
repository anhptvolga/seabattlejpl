/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model.events;

import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 */
public interface MissObjectListener {
    void missObject(MissObjectEvent e, Cell pos);
}
