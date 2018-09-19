/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * CenterGrid.java
 * Primary Authors: Austin Tarango
 * A 4x4 Holding area for tiles in the GameWindow.
 * Positioned in the center of the GameWindow.
 * Tiles by default are empty place holders.
 * 
 * Can set tiles when given tile icons.
 */
package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;

import model.TileIcon;

@SuppressWarnings("serial")
public class CenterGrid extends TilePane {

    // Constructor
    public CenterGrid() {
        super(16);

        GridLayout gridLayout = new GridLayout(4, 4);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setLayout(gridLayout);
    }

    // Resets the tiles positions and rotations to their defaults.
    public void reset() {

        // Array to verify that a corresponding tile is in the grid.
        boolean[] tileIsThere = new boolean[16];
        Arrays.fill(tileIsThere, false);

        // Going through every icon (tile) to see if...
        // it's default position resides in the center grid.
        // This part places the tiles into the center grid.
        for (TileIcon icon : icons) {
            int position = icon.getDefaultPosition();

            // Checking if tile is in center grid.
            if (position >= 16 && position < 32) {
                int index = position - 16;  // Index of tile.
                tileIsThere[index] = true;  // Recognizing tile is there.
                TileButton tile = (TileButton) getComponent(index); // The tile place holder.

                // Updating holding state and setting tile icon.
                if (!tile.isHolding())
                    tile.toggleIsHolding();
                tile.setIcon(icon);
            }
        }

        // Going through the remainder of the tile place holders.
        // This part removes tiles from the center grid.
        // Deselects any selected tiles.
        // If the tile isn't supposed to be here, removes it.
        // Also resets the rotation of tiles that are in the center grid.
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
}
