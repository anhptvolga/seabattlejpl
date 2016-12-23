/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattle.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import seabattle.model.AbstractLineObject;
import seabattle.model.ObjectFactory;
import seabattle.model.ObjectSet;
import seabattle.model.Player;
import seabattle.model.SeaArea;
import seabattle.model.Ship;
import seabattle.model.Submarine;
import seabattle.model.navigation.Cell;

/**
 *
 * @author anton
 */
public class BattlePlayerSeaPanel extends JPanel {
    
    private static final int CELL_SIZE = 30;
    private static final int GAP = 2;
    private static final int Y_SUBMARINE = 10;
    private static final int Y_SHIPFOUR = CELL_SIZE + 20;
    private static final int Y_SHIPTHREE = CELL_SIZE*2 + 30;
    private static final int Y_SHIPTWO = CELL_SIZE*3 + 40;
    private static final int Y_SHIPONE = CELL_SIZE*4 + 50;
  
    private static final Color BACKGROUND_COLOR = new Color(0, 101, 255);
    private static final Color SHIP_BACKGROUND_COLOR = new Color(172, 172, 172);
    private static final Color GRID_COLOR = Color.BLUE;

    private int _heightField = 10;
    private int _widthField = 10;
    
    private static HashMap<Class, Color> _colorShip = new HashMap<Class, Color>() {
        {put(Ship.class, Color.red); put(Submarine.class, Color.white); } };
    
    private Player _player = null;
    
    public BattlePlayerSeaPanel(){
    }
    
    public BattlePlayerSeaPanel(int heightField, int widthField, Player player){
        int width  = GAP + widthField * CELL_SIZE + GAP;//getWidth();
        int height = GAP + heightField * CELL_SIZE + GAP;//getHeight();

        _heightField = heightField;
        _widthField = widthField;
        _player = player;
        
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.RED);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        int width  = getWidth();
        int height = getHeight();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        darwGrid(g);
        g.setColor(Color.BLACK);
        drawHittedCell(g);
        drawDestroyedCells(g);
    }

    private void drawHittedCell(Graphics g) {
        g.setColor(Color.RED);
        List<Cell> list = _player.getHittedCell();
        if (!list.isEmpty()) {
            for (Cell cell : list) {
                g.drawOval(cell.row()*CELL_SIZE+5*GAP, cell.column()*CELL_SIZE+5*GAP, CELL_SIZE-8*GAP, CELL_SIZE-8*GAP);
            }
        }
    }
    
    private void drawDestroyedCells(Graphics g) {
        g.setColor(Color.RED);
        List<Cell> list = _player.getDestroyedCells();
        if (!list.isEmpty()) {
            for (Cell cell : list) {
                g.drawLine(cell.row()*CELL_SIZE+2*GAP, cell.column()*CELL_SIZE+2*GAP,
                        (1+cell.row())*CELL_SIZE, (1+cell.column())*CELL_SIZE);
                g.drawLine(cell.row()*CELL_SIZE+2*GAP, (1+cell.column())*CELL_SIZE,
                        (1+cell.row())*CELL_SIZE, cell.column()*CELL_SIZE+2*GAP);
            }
        }
    }
    
    private void drawShip(Graphics g, AbstractLineObject obj) {
        List<Cell> poss = obj.getPositions();
        if (!poss.isEmpty()) {
            int x = GAP + poss.get(0).row() * CELL_SIZE;
            int y = GAP + poss.get(0).column() * CELL_SIZE;
            drawShip(g, obj, x+GAP, y+GAP);
        }
    }
    
    private void drawShip(Graphics g, AbstractLineObject obj, int xleft, int yleft) {
        g.setColor(Color.YELLOW);
        
        g.setColor(SHIP_BACKGROUND_COLOR);
        if (obj.getOrientation() == AbstractLineObject.Orientation.Vertical) {
            g.fillOval(xleft, yleft, CELL_SIZE, obj.getLength()*CELL_SIZE);
            g.setColor(_colorShip.get(obj.getClass()));
            for (int i = 0; i < obj.getLength(); ++i) {
                drawDiamond(g, xleft, yleft + i * CELL_SIZE);
            }
            if (obj.getClass() == Submarine.class) {
                Submarine tmp = (Submarine)obj;
                g.fillRect(xleft + 3*CELL_SIZE/8, yleft + CELL_SIZE*tmp.getTowerDistance() + 3*CELL_SIZE/8, CELL_SIZE/4+2, CELL_SIZE/4+2);
            }
        } else { // ngang
            g.fillOval(xleft, yleft, obj.getLength()*CELL_SIZE, CELL_SIZE);
            g.setColor(_colorShip.get(obj.getClass()));
            for (int i = 0; i < obj.getLength(); ++i) {
                drawDiamond(g, xleft + i * CELL_SIZE, yleft);
            }
            if (obj.getClass() == Submarine.class) {
                Submarine tmp = (Submarine)obj;
                g.fillRect(xleft + CELL_SIZE*tmp.getTowerDistance() + 3*CELL_SIZE/8, yleft + 3*CELL_SIZE/8, CELL_SIZE/4+2, CELL_SIZE/4+2);
            }
        }
        
    }
    
    private void drawDiamond(Graphics g, int xleft, int yleft) {
        //xleft = xleft + GAP;
        //yleft = yleft + GAP;
        g.drawLine(xleft+CELL_SIZE/2, yleft+5, xleft+5, yleft+CELL_SIZE/2);
        
        g.drawLine(xleft+5, yleft+CELL_SIZE/2, xleft+CELL_SIZE/2, yleft+CELL_SIZE-5);
        
        g.drawLine(xleft+CELL_SIZE/2, yleft+CELL_SIZE-5, xleft+CELL_SIZE-5, yleft+CELL_SIZE/2);
        
        g.drawLine(xleft+CELL_SIZE-5, yleft+CELL_SIZE/2, xleft+CELL_SIZE/2, yleft+5);
        
    }    
    
    private void darwGrid(Graphics g) {
        int width  = GAP + _widthField * CELL_SIZE;//getWidth();
        int height = GAP + _heightField * CELL_SIZE;//getHeight();

        g.setColor(GRID_COLOR);
        
        for(int i = 0; i <= _widthField; i++)   {
            int x = GAP + CELL_SIZE*i;
            g.drawLine(x, GAP, x, height);
        }

        for(int i = 0; i <= _heightField; i++)   {
            int y = GAP + CELL_SIZE*i;
            g.drawLine(GAP, y, width, y);
        }

    }
         
    public Cell getCell(int x, int y) {
        if (GAP < x && x < GAP + CELL_SIZE * _widthField &&
                GAP < y && y < GAP + CELL_SIZE * _heightField)
            return new Cell( (int) ((x-GAP) / CELL_SIZE), (int) ((y-GAP) / CELL_SIZE));
        return null;
    }

}
