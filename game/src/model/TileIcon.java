/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * TileIcon.java
 * Primary Authors: Kevin Littell
 * The unique icon that represents a maze piece.
 * 
 * Will draw the icon based on the tile data given.
 * The icon can be rotated as it's drawn in 90 degree increments clockwise.
 * Rotation is indicated by numbers 0-3 inclusive...
 * where 0 is 0 degrees, 1 is 90 degrees, 2 is 180 degrees and 3 is 270 degrees.
 * Keeps track of its default rotation and position before...
 * the user rotated or moved the tile.
 */
package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.Icon;

public class TileIcon implements Icon {

    private int height = 100;           // Height of the icon.
    private int width = 100;            // Width of the icon.'
    private int defaultPosition = 0;    // The default position of the tile.
    private int currentPosition = 0;    // The current position of the tile.
    private int defaultRotation = 0;    // The default rotation of the tile.
    private int currentRotation = 0;    // The current rotation of the tile.
    private ArrayList<Float> tileData;  // The data associated with the icon.

    // Constructor
    // TileData: TilePosition|TileRotation|#Lines|Points
    public TileIcon(ArrayList<Float> tileData) {
        this.tileData = tileData;
        setDefaultPosition((int) (float) tileData.get(0));
        setDefaultRotation((int) (float) tileData.get(1));
    }

    public TileIcon() {

    }

/////////////////////////////////////////////////////
// Setters
/////////////////////////////////////////////////////

    // Sets the default position of the tile.
    // Ensures that the given value is 0-31 inclusive.
    public void setDefaultPosition(int p) {
        if (p >= 0 && p < 32) {
            defaultPosition = p;
            currentPosition = p;
        }
    }

    // Sets the current position of the tile.
    // Ensures that the given value is 0-31 inclusive.
    public void setCurrentPosition(int p) {
        if (p >= 0 && p < 32) {
            currentPosition = p;
        }
    }

    // Setting the default rotation of the tile.
    // Ensures that the given value is 0-3 inclusive.
    public void setDefaultRotation(int r) {
        if (r >= 0 && r < 4) {
            defaultRotation = r;
            currentRotation = r;
        }
    }

    // Rotates the tile 90 degrees clockwise.
    public void rotate() {
        if (currentRotation >= 3)
            currentRotation = 0;
        else
            currentRotation++;
    }

    // Resets the current rotation to it's default.
    public void resetRotation() {
        currentRotation = defaultRotation;
    }

/////////////////////////////////////////////////////
// Getters
/////////////////////////////////////////////////////

    // Returns the height of the icon.
    public int getIconHeight() {
        return height;
    }

    // Returns the width of the icon.
    public int getIconWidth() {
        return width;
    }

    // Returns the tiles default position.
    public int getDefaultPosition() {
        return defaultPosition;
    }

    // Returns the tiles current position.
    public int getCurrentPosition() {
        return currentPosition;
    }

    // Returns the tiles current rotation.
    public int getCurrentRotation() {
        return currentRotation;
    }

    // Returns the tile data array.
    // TileData: TilePosition|TileRotation|#Lines|Points
    public ArrayList<Float> getTileData() {
        return tileData;
    }

/////////////////////////////////////////////////////
// Tile icon
/////////////////////////////////////////////////////

    // Paints the icon based on the tile data given.
    // Rotates the icon based on the current rotation.
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g.create();

        // Rotating the icon.
        gr.rotate(Math.toRadians(90) * currentRotation, width / 2, height / 2);

        // Drawing background.
        gr.setColor(new Color(5, 46, 48));
        gr.fillRect(0, 0, width, height);

        // Drawing lines.
        gr.setColor(new Color(15, 165, 173));
        gr.setStroke(new BasicStroke(3));
        for (int point = 3; point < tileData.size(); point += 4)
            gr.drawLine((int) (float) tileData.get(point), (int) (float) tileData.get(point + 1),
                    (int) (float) tileData.get(point + 2), (int) (float) tileData.get(point + 3));

        gr.dispose();
    }
}
