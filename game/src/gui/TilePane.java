/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * TilePane.java
 * Primary Authors: Robert Randolph
 * Generic abstract class meant to be a direct extension to subclasses that...
 * contain TileButtons, specifically the SidePane and CenterGrid.
 * Primarily created to merge duplicated code between the separate panes.
 * 
 * Contains methods that directly affect tile events and their interactions...
 * across the current tile pane and other tile panes.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JPanel;

import communication.TileClickedEvent;
import communication.TileListener;
import model.TileIcon;

@SuppressWarnings("serial")
abstract public class TilePane extends JPanel implements ActionListener {

    // Holds listeners to send events to.
    private ArrayList<TileListener> listeners = new ArrayList<TileListener>();

    private TileButton selectedTile;    // The reference to the selected tile.
    private int id;                     // The index where the selected tile is.
    protected TileButton[] tileButtons; // The array of TileButtons.
    protected TileIcon[] icons;         // The icons for the tiles.

    // Constructor to create the array of TileButtons
    // Creates the array of TileButtons with default settings.
    public TilePane(int numTiles) {
        Dimension dim = new Dimension(100, 100);
        tileButtons = new TileButton[numTiles];

        for (int i = 0; i < numTiles; i++) {
            tileButtons[i] = new TileButton("");
            tileButtons[i].setPreferredSize(dim);
            tileButtons[i].addActionListener(this);
            add(tileButtons[i]);
        }
        selectedTile = tileButtons[0];
    }

/////////////////////////////////////////////////////
// Abstract methods
/////////////////////////////////////////////////////

    // The tiles will go to their default positions and rotations.
    abstract public void reset();

/////////////////////////////////////////////////////
// Tile Manipulation
/////////////////////////////////////////////////////

    // Sets the icons for the pane.
    public void setTiles(TileIcon[] icons) {
        this.icons = icons;
        reset();
    }

    /*
     * Event received from GameWindow. Event is received if two tiles or if a tile
     * and an empty place holder... from two separate tile panes have been selected.
     * 
     * If two tiles were selected then gives a vibrate to indicate an illegal move.
     * Otherwise it moves the selected tile to the empty place holder and moves the
     * empty place holder to where the selected tile originated from.
     * 
     * Sends an event to the GameWindow that the maze changed if it was a valid
     * move.
     */
    public void moveTile(TileClickedEvent tile) {

        // Deselecting tile.
        if (selectedTile.isSelected())
            selectedTile.toggleIsSelected();

        // Getting tile to move.
        TileButton t = tile.getTileButton();
        TileIcon i = (TileIcon) t.getIcon();

        // Checking if the tile is moving to an empty place holder.
        // If it's not then it doesn't move the tile...
        // and gives a vibrate to indicate an illegal move.
        if (!t.isHolding() || !selectedTile.isHolding()) {

            // Moving tile
            add(t, id);             // Adding tile to pane
            tileButtons[id] = t;    // Updating array index.
            selectedTile = t;       // Marking tile as selected.

            // Updating current position.
            if (i != null) {
                switch (tileButtons.length) {
                // SidePane
                case 8:
                    SidePane s = (SidePane) t.getParent();
                    int pane = s.getPaneNumber();
                    i.setCurrentPosition(id + pane * 8);
                    break;
                // Center Grid
                case 16:
                    i.setCurrentPosition(id + 16);
                    break;
                }
            }

            // Updating action listener for tile.
            // Removes source pane listener.
            // Adds current pane listener.
            t.removeActionListener((ActionListener) tile.getSource());
            t.addActionListener(this);

            // Only sends out the event for the tile and not the empty place holder.
            if (selectedTile.isHolding())
                fireTileChanged(); // Sending out event.
        } else
            // Vibrating to indicate an illegal move.
            Animation.vibrate(GameWindow.frame, 5);
    }

    /*
     * Private function to handle cases where a tile and... an empty place holder
     * from the same pane are selected. Moves the tile to the empty place holder.
     * Moves the empty place holder to where the selected tile originated from.
     */
    private void moveTile(TileButton tile) {
        int index = getComponentZOrder(tile);

        // Moving tile to empty place holder.
        add(tile, id);
        add(selectedTile, index);

        // Updating array indices
        tileButtons[id] = tile;
        tileButtons[index] = selectedTile;
    }

    /*
     * Event received from GameWindow. Event is received if the mouse didn't click a
     * tile. Deselects any selected tiles.
     */
    public void mouseClicked() {
        if (selectedTile.isSelected())
            selectedTile.toggleIsSelected();
    }

    // Returns whether a tile has been selected or not.
    public boolean isSelected() {
        return selectedTile.isSelected();
    }

/////////////////////////////////////////////////////
// Events and Listeners
/////////////////////////////////////////////////////

    // Adds a listener to the pane to send events to.
    public void addTileListener(TileListener listener) {
        listeners.add(listener);
    }

    /*
     * Sends an event to each listener. (Specifically the GameWindow) Event is sent
     * if two tiles from separate panes were clicked. Event sent contains the tile
     * that was clicked.
     */
    private void fireTileClicked() {
        for (TileListener listener : listeners)
            listener.tileClicked(new TileClickedEvent(this, selectedTile));
    }

    /*
     * Sends an event to each listener. (Specifically the GameWindow) Event is sent
     * if a tile was moved.
     */
    protected void fireTileChanged() {
        for (TileListener listener : listeners)
            listener.tileChanged(new EventObject(this));
    }

    /*
     * Receives events from tileButtons. If a tile is already selected, deselects
     * the tile. If a tile and an empty place holder from the same pane are...
     * selected, moves the tile to the empty place holder. If a tile hasn't already
     * been selected, selects the tile if valid... and sends out a TileClickedEvent.
     * Tile is valid if it's not an empty place holder.
     * 
     * If two tiles are found to have been selected then gives a... vibrate to the
     * user to indicate an illegal move.
     * 
     * Sends an event to the GameWindow that the maze changed if it was a valid
     * move.
     */
    public void actionPerformed(ActionEvent e) {
        // The tile that was clicked.
        TileButton clickedTile = (TileButton) e.getSource();

        /*
         * First checks to see if a tile has already been selected. If already selected
         * it then checks if the selected tile is the same. if so it deselects the tile.
         * Otherwise checks to see if the new tile is an empty place holder. If it is
         * then moves the tile to the empty place holder. If not then gives a vibrate to
         * the user to indicate an illegal move.
         */
        if (selectedTile.isSelected()) {
            selectedTile.toggleIsSelected();
            
            if (!(selectedTile == clickedTile))
                if (!clickedTile.isHolding()) {
                    moveTile(clickedTile);  // Moving tile.
                    fireTileChanged();      // Sending out event.
                }
                else{
                    // Vibrating to indicate an illegal move.
                    Animation.vibrate(GameWindow.frame, 10);
                }
        }
        // A tile hasn't already been selected.
        else {
            selectedTile = clickedTile;
            id = getComponentZOrder(selectedTile);

            // Checking if the selected tile is valid.
            // Tile is valid if it's not an empty place holder.
            if (selectedTile.isHolding())
                selectedTile.toggleIsSelected();

            fireTileClicked(); // Sending out event.
        }
    }
}