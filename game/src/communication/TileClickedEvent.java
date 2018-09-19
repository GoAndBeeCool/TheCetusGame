/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * TileClickedEvent.java
 * Primary Authors: Robert Randolph and Nicholas Whites
 * An event that holds the tile that was clicked.
 */
package communication;

import java.util.EventObject;

import gui.TileButton;

@SuppressWarnings("serial")
public class TileClickedEvent extends EventObject {

    private TileButton tile;

    // Constructor
    public TileClickedEvent(Object source, TileButton tile) {
        super(source);
        this.tile = tile;
    }

    // Returns the reference to the tile that was clicked.
    public TileButton getTileButton() {
        return tile;
    }
}
