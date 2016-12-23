
package seabattle.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import seabattle.model.AbstractLineObject;
import seabattle.model.Ship;
import seabattle.model.Submarine;
import seabattle.model.navigation.Cell;
import sun.swing.SwingUtilities2;

public class ObjectView extends JPanel {
    
    private final int CELL_SIZE = 30;
    private final int GAP = 2;
  
    private Color _backgroundColor = new Color(172, 172, 172);
    private int _towerPos = -1;
    private static HashMap<Class, Color> _colorShip = new HashMap<Class, Color>() {
        {put(Ship.class, Color.red); put(Submarine.class, Color.white); } };
    
    private AbstractLineObject _object;
    
    public ObjectView() {
        
    }
    
    public ObjectView(AbstractLineObject obj) {    
        _object = obj;
        setPreferredSize(new Dimension(CELL_SIZE * obj.getLength(), CELL_SIZE));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        int width  = getWidth();
        int height = getHeight();
        g.setColor(_backgroundColor);
        g.fillRect(0, 0, width, height);
        g.setColor(_colorShip.get(_object.getClass()));
        if (_object.getOrientation() == AbstractLineObject.Orientation.Horizontal) {
            for (int i = 0; i < _object.getLength(); ++i) {
                drawDiamond(g, 0, i * CELL_SIZE);
            }
        } else {
            for (int i = 0; i < _object.getLength(); ++i) {
                drawDiamond(g, i * CELL_SIZE, 0);
            }
        }
        
    }
    
    private void drawDiamond(Graphics g, int xleft, int yleft) {
        g.drawLine(xleft+CELL_SIZE/2, yleft, xleft, yleft+CELL_SIZE/2);
        
        g.drawLine(xleft, yleft+CELL_SIZE/2, xleft+CELL_SIZE/2, yleft+CELL_SIZE);
        
        g.drawLine(xleft+CELL_SIZE/2, yleft, xleft+CELL_SIZE, yleft+CELL_SIZE/2);
        
        g.drawLine(xleft+CELL_SIZE, yleft+CELL_SIZE/2, xleft+CELL_SIZE/2, yleft);
    }
}