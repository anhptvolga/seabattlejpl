/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model.events;

import java.util.EventObject;

/**
 *
 * @author anton
 */
public class GameOverEvent extends EventObject {

    String winner;
    public GameOverEvent(Object source) {
        super(source);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
    
}
