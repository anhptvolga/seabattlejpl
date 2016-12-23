/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jdk.internal.dynalink.beans.CallerSensitiveDetector;
import seabattle.model.events.HitObjectEvent;
import seabattle.model.events.HitObjectListener;
import seabattle.model.events.MissObjectEvent;
import seabattle.model.events.MissObjectListener;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 * Класс компьютерный игрок
 */
public class ComputerPlayer extends Player {;
    
    /**
     * Конструктор класса
     */
    public ComputerPlayer(int heigth, int width) {
        setName("Super Computer");
        _heightSea = heigth;
        _widthSea = width;
        _fireResults = new int [heigth][width];
        // init all cells not fire
        for (int i = 0; i < _heightSea; ++i) {
            for (int j = 0; j < _widthSea; ++j) {
                _fireResults[i][j] = 0;
            }
        }
     
    }
    
    // ----------- fire -----------------
    private int _heightSea = 0;
    private int _widthSea = 0;
    private int [][] _fireResults;
    
    private Cell _lastFireCell = null;
    private Cell _baseHittedCell = null;
    private int _fireDirection = -1;
    private List<Cell> _curBittingShip = new ArrayList<Cell>();
    //private int _state;
    private boolean isHorizontalShip = false;
    
    /**
     * Поменять направления стрельки
     * @return - новое направление
     * 0 - вверх
     * 1 - вправо
     * 2 - вниз
     * 3 - влево
     *   0
     * 3   1
     *   2
     */
    private int callNextDirect() {
        switch (_fireDirection) {
            case -1:
                return 0;
            case 0:
                _lastFireCell = _baseHittedCell;
                return 2;
            case 1:
                _lastFireCell = _baseHittedCell;
                return 3;
            case 2:
                if (!isHorizontalShip) {
                    _lastFireCell = _baseHittedCell;
                    return 1;
                }
                else {
                    return -1;
                }
            case 3: 
                return -1;
        }
        return -1;
    }
    
    /**
     * Взять ячейки, сумма индексов строки и столбца делится на число
     * @param d - число надо делиться
     * @return - Список ячейки соответсвует условие
     */
    private Cell getCellSumColRowDivisible(int d) {
        List<Cell> res = new ArrayList<Cell>();
        for (int i = 0; i < _heightSea; i++) {
            for (int j = 0; j < _widthSea; j++) {
                if ((i+j+1)%d == 0 && _fireResults[i][j] == 0) {
                    res.add(new Cell(j,i));
                }
            }
        }
        if (res.isEmpty()) { 
            return null;
        }
        java.util.Collections.shuffle(res);
        return res.get(0);
    }
    
    /**
     * Вычислить следующюю позицию удара
     * @return позиция удара
     */
    public Cell nextFire() {
        Cell res = null;
        if (_fireDirection != -1 && _lastFireCell != null) { 
             res = _lastFireCell.next(_fireDirection);
             while (res == null || Math.abs(res.row()-_lastFireCell.row() + res.column() - _lastFireCell.column()) != 1 || 
                     _fireResults[res.column()][res.row()] > 1 ||
                     _fireResults[res.column()][res.row()] < -1) {
                 _fireDirection = callNextDirect();
                 if (_fireDirection != -1)
                    res = _lastFireCell.next(_fireDirection);
                 else
                     break;
             }
             if (_fireDirection != -1)
                 return res;
        }
        /*
        do {
            res = _fireList.remove(0);
            if (!_destroyedCells.contains(res) && !_hittedCells.contains(res)) {
                return res;
            }
        } while (!_fireList.isEmpty());
        return res;
        */
        res = getCellSumColRowDivisible(4);
        if (res == null) {
            res = getCellSumColRowDivisible(2);
            if (res == null)
                res = getCellSumColRowDivisible(1);
        }
        return res;
    }
    
    /**
     * Стрелять поле другого игрока
     * @param target - null - позиция автоматически вычилить
     */
    public boolean fire(Cell target) {
        Cell pos = nextFire();
        System.out.printf("%d %d\n", pos.row(), pos.column());
        if (super.fire(pos)) {
            _fireResults[pos.column()][pos.row()] += 1;
            _curBittingShip.add(pos);
            if (_fireDirection == 0 || _fireDirection == 2) {
                isHorizontalShip = true;
            }
            if (_fireDirection == -1) {
                _fireDirection = 0;
                _lastFireCell = pos;
                _baseHittedCell = pos;
            }
            _lastFireCell = pos;
            // fire again
            fire(null);
        }
        else {
            _fireResults[pos.column()][pos.row()] -= 1;
            _lastFireCell = null;
            if (_fireDirection >= 0)
                _fireDirection = callNextDirect();
            if (_fireDirection == -1) {
                _baseHittedCell = null;
                isHorizontalShip = false;
                markingAroundDestroyedShip();
                _curBittingShip.clear();
            }
        }
        return false;
    }
    
    /**
     * Отмечить ячейки вокруг забитого корабли
     */
    private void markingAroundDestroyedShip() {
        Cell tmp;
        int [] x = {-1,  0,  1, 1, 1, 0, -1, -1};
        int [] y = {-1, -1, -1, 0, 1, 1,  1,  0};
        for (Cell cell : _curBittingShip) {
            for (int i = 0; i < 8; ++i) {
                if (Cell.isValid(cell.row() + x[i], cell.column() + y[i])) {
                    _fireResults[cell.column() + y[i]][cell.row() + x[i]] = -10;
                }
            }
        }
    }
    
    /**
     * Разполагать кораблей на поле
     * @param sea - поле, на котором разполагать кораблей
     */
    public void rangeObjectsOnSea(SeaArea sea) {
        ObjectSet set = new ObjectSet(sea);
        int height = sea.height();
        int width = sea.width();
        Random randomer = new Random();
        int direct = randomer.nextInt(4);
        // starting position
        Cell cell = new Cell(0, height-1);
        if (direct == 1) {
            cell = new Cell(0,0);
        } else if (direct == 2) {
            cell = new Cell(width-1, 0);
        } else if (direct == 3) {
            cell = new Cell(width-1, height-1);
        }
        arangeShipType(set, sea, ObjectSet.TYPE_OBJECT.SubmarieFour, direct, cell);
        arangeShipType(set, sea, ObjectSet.TYPE_OBJECT.ShipFour, direct, cell);
        arangeShipType(set, sea, ObjectSet.TYPE_OBJECT.ShipThree, direct, cell);
        arangeShipType(set, sea, ObjectSet.TYPE_OBJECT.ShipTwo, direct, cell);
        arangeShipOne(set, sea, cell, direct);
    }
    
    /**
     * Располагать кораблей, длина которого равна 1
     * @param set - набор кораблей
     * @param sea - море, на котором располагать корабли
     * @param cell - начинающий ячейк
     * @param direct - направление свободного пространства
     */
    private void arangeShipOne(ObjectSet set, SeaArea sea, Cell cell, int direct) {
        int xl = cell.row() + 2;
        int xr = Cell.horizontalRange().max() - 1;
        int yl = 0;
        int yr = Cell.verticalRange().max() - 1;
        
        if (direct == 1) {
            xl = 0;
            yl = cell.column() + 2;
        } else if (direct == 2) {
            xl = 0;
            xr = cell.row() - 2;
        } else if (direct == 3) {
            xl = 0;
            yr = cell.column() - 2;
        }
        
        int amount = set.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.ShipOne);
        Random randomer = new Random();
        int rangex = (xr-xl) / 2;
        int rangey = (yr-yl) / 2;
        while (amount > 0) {
            AbstractLineObject obj = set.getObject(ObjectSet.TYPE_OBJECT.ShipOne);
            do {
                cell = new Cell(xl + 2 * randomer.nextInt(rangex),
                        yl + 2 * randomer.nextInt(rangey));
            } while (!sea.addObject(cell, obj));
            amount--;
        }        
    }
    
    /**
     * Располагать одного типа кораблей
     * @param set - набор кораблей
     * @param sea - море, на котором располагать корабли
     * @param type - тип кораблей
     * @param direct - направление свободного пространства
     * @param cell - начинающий ячейк
     */
    private void arangeShipType(ObjectSet set, SeaArea sea, ObjectSet.TYPE_OBJECT type, int direct, Cell cell) {
        int amount = set.getCurrentAmountObject(type);
        while (amount > 0) {
            AbstractLineObject obj = set.getObject(type);
            if (obj.getClass() == Submarine.class) {
                Random randomer = new Random();
                ((Submarine)obj).setTowerSubmarineDistance(randomer.nextInt(4));
            }
            if (direct == 0 || direct == 2) {
                    obj.rotate90();
            }
            while (!sea.addObject(cell, obj)) {
                cell = cell.next(direct);
            }
            amount--;
        }
    }
    
}
