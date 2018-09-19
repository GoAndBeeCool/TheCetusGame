/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * TileListener.java
 * Primary Authors: Robert Randolph and Nicholas Whites
 * A listener interface that gives communication between the content panes and the GameWindow.
 */
package communication;

import java.util.EventObject;

public interface TileListener {
    public void tileClicked(TileClickedEvent e);
    public void tileChanged(EventObject e);
}
