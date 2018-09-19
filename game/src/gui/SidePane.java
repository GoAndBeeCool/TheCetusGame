/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * SidePane.java
 * Primary Authors: Gokul Babu and Kevin Littell
 * A 8x1 Holding area for tiles in the GameWindow.
 * Positioned on the left and right sides of the GameWindow.
 * Tiles by default are empty place holders.
 * 
 * Can set tiles when given tile icons.
 */
package gui;

import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;

import model.TileIcon;

@SuppressWarnings("serial")
public class SidePane extends TilePane {

    // Static variable to differentiate between separate side panes.
    private static int paneCount = 0;

    // The current pane.
    private int pane = paneCount++;

    // Constructor
    public SidePane() {
        super(8);

        // Setting up pane.
        GridLayout gridLayout = new GridLayout(0, 1);
        gridLayout.setVgap(10);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setLayout(gridLayout);
    }

    // Resets the tiles positions and rotations to their defaults.
    public void reset() {
        int lowerBound = pane * 8;          // Lower index bound for array.
        int upperBound = lowerBound + 8;    // Upper index bound for array.

        // Array to verify that a corresponding tile is in the side pane.
        boolean[] tileIsThere = new boolean[8];
        Arrays.fill(tileIsThere, false);

        // Going through every tile icon to see if...
        // its default position resides in the side pane.
        // This part places the tiles into the side pane.
        for (TileIcon icon : icons) {
            int position = icon.getDefaultPosition();

            // Checking if tile is in the side pane.
            if (position >= lowerBound && position < upperBound) {
                int index = position - lowerBound;                  // Index of tile.
                tileIsThere[index] = true;                          // Recognizing tile is there.
                TileButton tile = (TileButton) getComponent(index); // The tile place holder.

                // Updating holding state and setting tile icon.
                if (!tile.isHolding())
                    tile.toggleIsHolding();
                tile.setIcon(icon);
            }
        }

        // Going through the remainder of the tile place holders.
        // This part removes tiles from the side pane.
        // Deselects any selected tiles.
        // If the tile isn't supposed to be here, removes it.
        // Also resets the rotation of tiles that are in the side pane.
        int index = 0;
        for (TileButton tileButton : tileButtons) {
            if (tileButton.isSelected())
                tileButton.toggleIsSelected();

            // Checking if the tile is supposed to be there.
            if (!tileIsThere[index++]) {
                if (tileButton.isHolding())
                    tileButton.toggleIsHolding();
                tileButton.setIcon(null);
            }
            else
                tileButton.resetIconRotation();
        }
    }

    // Returns the pane number.
    public int getPaneNumber() {
        return pane;
    }
}
