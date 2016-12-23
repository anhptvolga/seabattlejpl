
package seabattle.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import seabattle.model.AbstractLineObject;
import seabattle.model.ObjectFactory;
import seabattle.model.ObjectSet;
import seabattle.model.SeaArea;
import seabattle.model.Ship;
import seabattle.model.Submarine;
import seabattle.model.navigation.Cell;

public class ArangeShipsFieldPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
    
    private static final int CELL_SIZE = 30;
    private static final int X_TEXT = 2;
    private static final int X_SET = 100;
    private static final int X_FIELD = X_SET + CELL_SIZE * 5;
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
    
    private ObjectSet _objectSet = null;
    private AbstractLineObject _curObj = null;
    private int _xCurObj, _yCurObj;
    private SeaArea _seaArea = null;
    
    public ArangeShipsFieldPanel(){

        addMouseListener(this);
        addMouseMotionListener(this);      
    }    
    
    public ArangeShipsFieldPanel(int heightField, int widthField){
        int width  = GAP + widthField * CELL_SIZE + X_FIELD;//getWidth();
        int height = GAP + heightField * CELL_SIZE + GAP;//getHeight();
        
        _heightField = heightField;
        _widthField = widthField;
        
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.RED);
        
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void setObjectSet(ObjectSet set) {
        _objectSet = set;
    }
    
    public void setSeaArea(SeaArea sea) {
        _seaArea = sea;
    }
    
    public boolean isDone() {
        return _objectSet.isEmpty();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        int width  = getWidth();
        int height = getHeight();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        darwGrid(g);
        g.setColor(Color.BLACK);   
        // draw sets
        if (_objectSet != null) {
            g.drawString("Подлодка " + _objectSet.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.SubmarieFour), X_TEXT, CELL_SIZE);
            g.drawString("Линкор " + _objectSet.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.ShipFour), X_TEXT, CELL_SIZE*2 + 10);
            g.drawString("Крейсер " + _objectSet.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.ShipThree), X_TEXT, CELL_SIZE*3 + 20);
            g.drawString("Эсминец " + _objectSet.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.ShipTwo), X_TEXT, CELL_SIZE*4 + 30);
            g.drawString("Катер " + _objectSet.getCurrentAmountObject(ObjectSet.TYPE_OBJECT.ShipOne), X_TEXT, CELL_SIZE*5 + 40);
            drawSetOf(g, ObjectSet.TYPE_OBJECT.SubmarieFour, X_SET, Y_SUBMARINE);
            drawSetOf(g, ObjectSet.TYPE_OBJECT.ShipFour, X_SET, Y_SHIPFOUR);
            drawSetOf(g, ObjectSet.TYPE_OBJECT.ShipThree, X_SET, Y_SHIPTHREE);
            drawSetOf(g, ObjectSet.TYPE_OBJECT.ShipTwo, X_SET, Y_SHIPTWO);
            drawSetOf(g, ObjectSet.TYPE_OBJECT.ShipOne, X_SET, Y_SHIPONE);
        }
        // draw ship on field
        List<AbstractLineObject> objs = _seaArea.objects();
        if (!objs.isEmpty()) {
            for (AbstractLineObject obj : objs) {
                drawShip(g, obj);
            }
        }
        // draw mouse
        if (_curObj != null)
            drawShip(g, _curObj, _xCurObj-CELL_SIZE/2, _yCurObj-CELL_SIZE/2);
    }
    
    private void drawSetOf(Graphics g, ObjectSet.TYPE_OBJECT type, int xleft, int yleft) {
        if (_objectSet.getCurrentAmountObject(type) > 0) {
            drawShip(g, ObjectFactory.CreateLineObject(type, null), xleft, yleft);
        }
    }
    
    private void drawShip(Graphics g, AbstractLineObject obj) {
        List<Cell> poss = obj.getPositions();
        if (!poss.isEmpty()) {
            int x = X_FIELD + GAP + poss.get(0).row() * CELL_SIZE;
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
        int width  = GAP + _widthField * CELL_SIZE + X_FIELD;//getWidth();
        int height = GAP + _heightField * CELL_SIZE;//getHeight();

        g.setColor(GRID_COLOR);
        
        for(int i = 0; i <= _widthField; i++)   {
            int x = GAP + CELL_SIZE*i;
            g.drawLine(x + X_FIELD, GAP, x + X_FIELD, height);
        }

        for(int i = 0; i <= _heightField; i++)   {
            int y = GAP + CELL_SIZE*i;
            g.drawLine(X_FIELD + GAP, y, width, y);
        }

    }
    
    private Point leftTopCell(Cell pos) {
        
        int left = GAP + CELL_SIZE * (pos.column()-1);
        int top = GAP + CELL_SIZE * (pos.row()-1);
        
        return new Point(left, top);
    }

    private int checkSubmaireSet(int x, int y) {
        if (Y_SUBMARINE + GAP < y && y < Y_SUBMARINE + GAP + CELL_SIZE
                && X_SET + GAP < x && x < X_SET + GAP + 4*CELL_SIZE) {
            return (int)(x-(X_SET + GAP))/CELL_SIZE;
        }
        return -1;
    }
    
    private int checkShipFourSet(int x, int y) {
        if (Y_SHIPFOUR + GAP < y && y < Y_SHIPFOUR + GAP + CELL_SIZE
                && X_SET + GAP < x && x < X_SET + GAP + 4*CELL_SIZE) {
            return 1;
        }
        return -1;
    }
    
    private int checkShipThreeSet(int x, int y) {
        if (Y_SHIPTHREE + GAP < y && y < Y_SHIPTHREE + GAP + CELL_SIZE
                && X_SET + GAP < x && x < X_SET + GAP + 3*CELL_SIZE) {
            return 1;
        }
        return -1;
    }
    
    private int checkShipTwoSet(int x, int y) {
        if (Y_SHIPTWO + GAP < y && y < Y_SHIPTWO + GAP + CELL_SIZE
                && X_SET + GAP < x && x < X_SET + GAP + 2*CELL_SIZE) {
            return 1;
        }
        return -1;
    }
    
    private int checkShipOneSet(int x, int y) {
        if (Y_SHIPONE + GAP < y && y < Y_SHIPONE + GAP + CELL_SIZE
                && X_SET + GAP < x && x < X_SET + GAP + 1*CELL_SIZE) {
            return 1;
        }
        return -1;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (_curObj != null) {
            _xCurObj = e.getX();
            _yCurObj = e.getY();
            repaint();
        }
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        _xCurObj = e.getX();
        _yCurObj = e.getY();
        int submarine = checkSubmaireSet(_xCurObj, _yCurObj);
        int four = checkShipFourSet(_xCurObj, _yCurObj);
        int three = checkShipThreeSet(_xCurObj, _yCurObj);
        int two = checkShipTwoSet(_xCurObj, _yCurObj);
        int one = checkShipOneSet(_xCurObj, _yCurObj);
        
        if (_curObj == null) {
            if (submarine >= 0) {
                _curObj = _objectSet.getObject(ObjectSet.TYPE_OBJECT.SubmarieFour);
                ((Submarine)_curObj).setTowerSubmarineDistance(submarine);
            } else if (four > 0) {
                _curObj = _objectSet.getObject(ObjectSet.TYPE_OBJECT.ShipFour);
            } else if (three > 0) {
                _curObj = _objectSet.getObject(ObjectSet.TYPE_OBJECT.ShipThree);
            } else if (two > 0) {
                _curObj = _objectSet.getObject(ObjectSet.TYPE_OBJECT.ShipTwo);
            } else if (one > 0) {
                _curObj = _objectSet.getObject(ObjectSet.TYPE_OBJECT.ShipOne);
            } else {
                int x = e.getX();
                int y = e.getY();
                if (X_FIELD + GAP < x && x < X_FIELD + GAP + CELL_SIZE * _widthField &&
                        GAP < y && y < GAP + CELL_SIZE * _heightField) {
                    x = (int) ((x-X_FIELD - GAP) / CELL_SIZE);
                    y = (int) ((y-GAP)/CELL_SIZE);
                    
                    _curObj = _seaArea.object(new Cell(x,y));
                    if (_curObj != null ) {
                        _seaArea.removeObject(_curObj);
                        repaint();
                    }
                }
            }
            repaint();
        } else {
            int x = e.getX();
            int y = e.getY();
            if (X_FIELD + GAP < x && x < X_FIELD + GAP + CELL_SIZE * _widthField &&
                    GAP < y && y < GAP + CELL_SIZE * _heightField) {
                x = (int) ((x-X_FIELD - GAP) / CELL_SIZE);
                y = (int) ((y-GAP)/CELL_SIZE);
                if (_seaArea.addObject(new Cell(x,y), _curObj)) {
                    repaint();
                    _curObj = null;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            if (_curObj != null) {
                _objectSet.returnObject(_curObj);
                _curObj = null;
                repaint();
            }
        }
        else if (code == KeyEvent.VK_SPACE) { // rotate
            if (_curObj != null) {
                _curObj.rotate90();
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}