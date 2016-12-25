/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import java.util.ArrayList;
import java.util.List;
import seabattle.model.events.GameOverEvent;
import seabattle.model.events.GameOverListener;
import seabattle.model.events.HitObjectEvent;
import seabattle.model.events.HitObjectListener;
import seabattle.model.events.MissObjectEvent;
import seabattle.model.events.MissObjectListener;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * Класс 
 */
public class GameModel {
    Player _activePlayer, _passivePlayer;
    SeaArea _activeSeaArea, _passiveSeaArea;
    
    /**
     * Конструктор
     * @param player1 - первый игрок
     * @param player2 - второй игрок
     * @param height - высота игрового поля
     * @param width - ширина игрового поля
     */
    public GameModel(Player player1, Player player2, int height, int width) {
        // set range of cells
        Cell.setHorizontalRange(0, width-1);
        Cell.setVerticalRange(0, height-1);
        // generate players
        _activePlayer = player1;
        _passivePlayer = player2;
        // generate seas
        _activeSeaArea = new SeaArea(height, width);
        _passiveSeaArea = new SeaArea(height, width);
        // set target seas
        _activePlayer.setTargetSea(_passiveSeaArea);
        _passivePlayer.setTargetSea(_activeSeaArea);
        // add listeners
        _activePlayer.addHitListener(new GameModel.ActionHitObject());
        _activePlayer.addMissListener(new GameModel.ActionMissObject());
        _passivePlayer.addHitListener(new GameModel.ActionHitObject());
        _passivePlayer.addMissListener(new GameModel.ActionMissObject());
    }
    
    /**
     * Получить поле активного игрока
     * @return - поле активного игрока
     */
    public SeaArea getActiveSeaArea() {
        return _activeSeaArea;
    }
    
    /**
     * Получить поле пассивного игрока, которое получить удар
     * @return - поле пассивного игрока
     */
    public SeaArea getPassiveSeaArea() {
        return _passiveSeaArea;
    }
    
    /**
     * Получить текущий активный игрок - будет стрелять
     * @return - активный игрок
     */
    public Player getActivePlayer() {
        return _activePlayer;
    }
    
    /**
     * Получить пассивный игрок
     * @return пассинвый игрок
     */
    public Player getPassivePlayer() {
        return _passivePlayer;
    }
    
    /**
     * Поменять ход игрков
     */
    protected void exchangeTurn() {
        // exchange player
        Player p = _activePlayer;
        _activePlayer = _passivePlayer;
        _passivePlayer = p;
        // exchange sea
        SeaArea s = _activeSeaArea;
        _activeSeaArea = _passiveSeaArea;
        _passiveSeaArea = s;
    }
    
    /**
     * Проверить окончение игры
     */
    protected void determineWinner() {
        boolean isDestroyAll = true;
        // checking all objects in sea
        List<AbstractLineObject> objs = _passiveSeaArea.objects();
        for (AbstractLineObject obj : objs) {
            isDestroyAll = obj.isDestroyed() && isDestroyAll;
        }       
        if (isDestroyAll) {
            _gameoverEvent.setWinner(_activePlayer.getName());
            notifyGameOver();
        }
    }
    
    // -------- gameover listeners ---------
    List<GameOverListener> _gameoverListeners = new ArrayList<GameOverListener>();
    GameOverEvent _gameoverEvent = new GameOverEvent(this);
    
    /**
     * Добавить случатель окончения игры
     * @param ls - слушатель
     */
    public void addGameOverListener(GameOverListener ls) {
        _gameoverListeners.add(ls);
    }
    
    /**
     * Сообщать слушателям окончания игры
     */
    void notifyGameOver() {
        for (GameOverListener ls : _gameoverListeners) {
            ls.GameOverPerform(_gameoverEvent);
        }
    }
    
    // ------------ class perform -----
    /**
     * Класс выполнить действие при получении сообытии попадания
     */
    private class ActionHitObject implements HitObjectListener {

        @Override
        public void hitObject(HitObjectEvent e, Cell pos) {
            determineWinner();
        }
    }
    
    /**
     * Класс выполнить действие при получении сообытии промаха
     */
    private class ActionMissObject implements MissObjectListener {

        @Override
        public void missObject(MissObjectEvent e, Cell pos) {
            exchangeTurn();
        }
    }
   
}
