/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * TileButton.java
 * Primary Authors: Robert Randolph and Nicholas Whites
 * A tile button that extends the normal JButton.
 * This is to give added functionality to the normal JButton tiles.
 * 
 * The TileButton by default doesn't hold a tile.
 * Keeps track of whether it is holding a tile, or if it's an empty place holder.
 * Keeps track of whether it is currently selected or not.
 * Will rotate the tile icon (if it's holding a tile) by 90 degrees clockwise if right-clicked.
 * 
 * Highlights if selected.
 * Will have a small border around it if it's not holding a tile to represent...
 * an empty place holder.
 * Tile won't have a border around if it it's holding a tile.
 */
package gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import model.TileIcon;

@SuppressWarnings("serial")
public class TileButton extends JButton implements MouseListener {

    private boolean selected;   // Control for if the tile is selected.
    private boolean holding;    // Control for if the TileButton contains a tile.

    // Constructor.
    // Creating the default empty place holder.
    public TileButton(String name) {
        super(name);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        addMouseListener(this);
        selected = false;
        holding = false;
    }

    /*
     * Toggle to select or deselect the tile. Tile is highlighted if selected.
     */
    public void toggleIsSelected() {
        if (selected) {
            selected = false;

            if (holding)
                setBorder(null);
            else
                setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }
        else {
            selected = true;
            setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        }
    }

    /*
     * Toggle for if the the TileButton is holding a tile or not. If it's holding a
     * tile there is no border. If isn't holding a tile there is a small border.
     */
    public void toggleIsHolding() {
        if (holding) {
            holding = false;
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }
        else {
            holding = true;
            setBorder(null);
        }
    }

    /*
     * If the place holder is holding a tile it... resets the tile icon rotation to
     * its default rotation. The tile is repainted and validated after its reset.
     */
    public void resetIconRotation() {
        if (holding) {
            TileIcon t = (TileIcon) getIcon();
            t.resetRotation();
            repaint();
            validate();
        }
    }

    // Returns whether or not the tile is selected.
    public boolean isSelected() {
        return selected;
    }

    // Returns whether or not it contains a tile.
    public boolean isHolding() {
        return holding;
    }

    /*
     * Receives events from the mouse. If the place holder was right clicked and was
     * holding a tile... then it rotates the tile icon by 90 degrees clockwise. The
     * tile is repainted and validated after being rotated.
     * 
     * Sends an event to the GameWindow through it's parent saying that the maze
     * changed.
     * 
     * Only implements the mouseClicked event.
     */
    public void mouseClicked(MouseEvent e) {
        if (holding && e.getButton() == MouseEvent.BUTTON3) {
            TileIcon t = (TileIcon) getIcon();
            t.rotate();
            repaint();
            validate();

            // Sending event.
            TilePane t2 = (TilePane) getParent();
            t2.fireTileChanged();
        }
    }

    public void mouseEntered(MouseEvent e) {} // Does nothing.
    public void mouseExited(MouseEvent e) {} // Does nothing.
    public void mousePressed(MouseEvent e) {} // Does nothing.
    public void mouseReleased(MouseEvent e) {} // Does nothing.
}
