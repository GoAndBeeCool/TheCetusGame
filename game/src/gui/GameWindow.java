/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * GameWindow.java
 * Primary Authors: All Members
 * Main window that holds all the panes and components.
 * Handles all communication between the different panes...
 * acting like a middle-man between everything.
 * 
 * Handles moving the appropriate tiles.
 * Handles the start of the program and its conditions.
 * Notifies the user if they solved the maze.
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import communication.MenuEvent;
import communication.MenuListener;
import communication.TileClickedEvent;
import communication.TileListener;
import model.Icons;

@SuppressWarnings("serial")
public class GameWindow extends JFrame
implements TileListener, MouseListener, MenuListener, WindowListener, ActionListener {

    private TileClickedEvent previousEvent; // Holds the last tile event that it received.

    protected static JFrame frame;          // The GameWindow
    private Icons icons;                    // The tile icons.

    // The different panes.
    private MenuPane menu;
    private SidePane leftPane, rightPane;
    private CenterGrid centerGrid;
    
    // Items pertaining to the time played.
    private JLabel timePlayed;      // The time played label.
    private Timer timer;            // Timer to increment time played.
    private boolean timeStarted;    // Control to start the timer when a change was made.
    private long timeElapsed;       // The amount of time actually elapsed.
    
    // Check to ensure that the tiles have been loaded into the game.
    private boolean iconsLoaded = false;
    // Check for if changes occurred to the current maze.
    private boolean iconsChanged = false;

    // Constructor
    public GameWindow() {

        // Setting up window.
        frame = new JFrame("Group C Maze");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(900, 1000);
        frame.getContentPane().setBackground(new Color(41, 157, 196));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        frame.addMouseListener(this);
        frame.addWindowListener(this);

        // Setting up panes and timer.
        setUpPanes();
        frame.setVisible(true);

        // Attempts to load the default icons.
        // If default icons couldn't be loaded it then prompts the user...
        // for a maze file to load.
        // If the load still fails then it starts the game without any tiles.
        // In this case the only buttons that will work are the file and quit buttons.
        icons = new Icons();
        if (!attemptLoadMaze(new File("default.mze"))) {
            JOptionPane.showMessageDialog(this, "Could not load maze.\nDefault maze not found."
                    + "\nPlease select a maze to load.", "Error", JOptionPane.ERROR_MESSAGE);
            attemptLoadMaze(FileDialog.loadFile());
        }
            
    }

/////////////////////////////////////////////////////
// GameWindow setup and other methods
/////////////////////////////////////////////////////

    // Creates and adds the panes to the GameWindow.
    // Also creates and sets up the timer.
    private void setUpPanes() {

        // Creating Panes
        menu = new MenuPane();
        leftPane = new SidePane();
        rightPane = new SidePane();
        centerGrid = new CenterGrid();
        
        // Creating timer
        timePlayed = new JLabel("Time Played: 0:00:00");
        timePlayed.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        timer = new Timer(1000, this);

        // Adding listeners to panes.
        menu.addMenuListener(this);
        leftPane.addTileListener(this);
        rightPane.addTileListener(this);
        centerGrid.addTileListener(this);

        // Adding panes to the GameWindow.
        GridBagConstraints gc = new GridBagConstraints();

        // Setting up constraint defaults
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;
        gc.weighty = 0;

        // Row 1

        // Left Pane: Extends to row 3.
        gc.gridheight = 3;
        gc.anchor = GridBagConstraints.LINE_START;
        frame.add(leftPane, gc);

        // Timer: Center of row 1.
        gc.gridx = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        frame.add(timePlayed, gc);

        // Right Pane: Extends to row 3.
        gc.gridx = 2;
        gc.gridheight = 3;
        gc.anchor = GridBagConstraints.LINE_END;
        frame.add(rightPane, gc);

        // Row 2
        
        // Menu: Center of row 2.
        gc.gridy = 1;
        gc.gridx = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.PAGE_START;
        frame.add(menu, gc);
        
        // Row 3

        // Center Grid: Center of row two.
        gc.gridy = 2;
        gc.gridx = 1;
        gc.gridheight = 1;
        gc.weightx = .5;
        gc.anchor = GridBagConstraints.CENTER;
        frame.add(centerGrid, gc);

        return;
    }

    // Attempts to load and setup the tile icons.
    // If successful resets the timer.
    // Gives an error message if the load failed.
    private boolean attemptLoadMaze(File file) {
        if (file == null)
            return false;
        if (icons.loadIcons(file)) {
            // Settings tiles
            leftPane.setTiles(icons.getIcons());
            rightPane.setTiles(icons.getIcons());
            centerGrid.setTiles(icons.getIcons());
            iconsLoaded = true;
            iconsChanged = false;
            
            // Setting up timer.
            timeStarted = false;
            timer.stop();
            timer.setInitialDelay(1000);
            timeElapsed = icons.getDefaultTimePlayed();
            repaintTimer();
            
            return true;
        }
        if (iconsLoaded)
            JOptionPane.showMessageDialog(this, "Could not load maze.\nFile is not a maze.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    // Repaints the timer to show the appropriate time change.
    private void repaintTimer() {
        long hours = timeElapsed / 3600;
        long minutes = (timeElapsed % 3600) / 60;
        long seconds = (timeElapsed % 3600) % 60;
        timePlayed.setText("Time Played: "
                + hours + ":"
                + ((minutes < 10) ? 0 : "") + minutes + ":"
                + ((seconds < 10) ? 0 : "") + seconds);
    }

/////////////////////////////////////////////////////
// Event Handling
/////////////////////////////////////////////////////

    /*
     * Receives events from the tile panes.
     * Stores the previous tile event that occurred.
     * If a tile and an empty place holder are found to be selected it...
     * moves the tile to the empty place holder.
     */
    public void tileClicked(TileClickedEvent e) {
        if (e.getSource() == centerGrid) {
            if (leftPane.isSelected()) {
                leftPane.moveTile(e);
                centerGrid.moveTile(previousEvent);
            }
            else if (rightPane.isSelected()) {
                rightPane.moveTile(e);
                centerGrid.moveTile(previousEvent);
            }
            else
                previousEvent = e;
        }
        else if (e.getSource() == leftPane) {
            if (rightPane.isSelected()) {
                rightPane.moveTile(e);
                leftPane.moveTile(previousEvent);
            }
            else if (centerGrid.isSelected()) {
                centerGrid.moveTile(e);
                leftPane.moveTile(previousEvent);
            }
            else
                previousEvent = e;
        }
        else if (e.getSource() == rightPane) {
            if (leftPane.isSelected()) {
                leftPane.moveTile(e);
                rightPane.moveTile(previousEvent);
            }
            else if (centerGrid.isSelected()) {
                centerGrid.moveTile(e);
                rightPane.moveTile(previousEvent);
            }
            else
                previousEvent = e;
        }
    }

    /*
     * Receives events from the tile panes.
     * Keeps track if the current maze has been changed.
     * Starts the maze timer if not already on.
     * Also checks to see if the maze has been solved after each change.
     * If the maze has been solved it will stop the timer and notify the user.
     * The notification will give show the amount of time taken to solve the maze.
     */
    public void tileChanged(EventObject e) {
        
        // Updating icons changed control
        if (!iconsChanged)
            iconsChanged = true;
        
        // Starting timer.
        if (!timeStarted) {
            timeStarted = true;
            timer.start();
        }
        
        // Checking if the maze was solved.
        // If it was notifies the user.
        if (icons.isSolved()) {
            timeStarted = false;
            timer.stop();
            long hours = timeElapsed / 3600;
            long minutes = (timeElapsed % 3600) / 60;
            long seconds = (timeElapsed % 3600) % 60;
            String h = (hours == 1) ? " hour" : " hours";
            String m = (minutes == 1) ? " minute" : " minutes";
            String s = (seconds == 1) ? " second." : " seconds.";
            String timePlayed =
                    ((hours != 0) ? hours + ((minutes != 0 || seconds != 0) ? h + ", " : h + ".") : "")
                    +((minutes != 0) ? minutes + ((seconds != 0) ? m + ", " : m + ".") : "")
                    + ((seconds != 0) ? seconds + s : "");
            JOptionPane.showMessageDialog(this,
                    "Congratulations!  You have solved the maze in\n" + timePlayed,
                    "Congratulations!", JOptionPane.INFORMATION_MESSAGE, null);
        }
    }

    /*
     * Receives events from the menu pane.
     * If the file button is clicked then a dialog box will prompt the user to...
     * either load or save a maze.
     * 
     * If load is clicked then:
     * 1) If changes were made it will prompt the user to save their current maze.
     *    a) Yes: Saves maze then prompts user to load if save was successful.
     *       a.1) If save failed it will end the load process.
     *    b) No: Skips save and prompts user to load.
     *    c) Cancel: Ends load process.
     * 2) If changes were not made it will prompt the user to load.
     * 3) If load succeeded it will reset the timer and iconsChanged check.
     * 4) If load failed it will give an error message.
     * 
     * If save is clicked then:
     * 1) It will prompt the user to save their current maze.
     *    a) If canceled it will end save process.
     * 2) If save succeeded it will reset the iconsChanged check.
     * 3) If save failed it will give an error message.
     * 
     * Note: Save process will cancel if there is no maze to save...
     *       and give an error message.
     * 
     * If cancel is clicked then nothing will happen.
     */
    public void fileClicked(MenuEvent e) {
        // Prompting user.
        Object[] options = { "Save", "Load", "Cancel" };
        int response = JOptionPane.showOptionDialog(this, "Save or Load Maze", "File", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        // Checking response
        switch (response) {
        // Save (Prompts save)
        case 0:
            if (iconsLoaded) {
                File file = FileDialog.saveFile();
                if (file != null)
                    if (!icons.saveIcons(file, timeElapsed))
                        JOptionPane.showMessageDialog(GameWindow.frame, "Could not save maze.\nFile IO error", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    else
                        iconsChanged = false;
            }
            else
                JOptionPane.showMessageDialog(GameWindow.frame, "Could not save maze.\nNo maze to save.\n", "Error",
                        JOptionPane.ERROR_MESSAGE);
            break;
        // Load (Prompts load, save if changes have been made)
        // If user tries to save with no maze to save it will prompt a load.
        case 1:
            // Checking if any changes have been made to the current maze.
            // If so prompts user to save.
            if (iconsChanged && iconsLoaded) {
                response = JOptionPane.showConfirmDialog(this,
                        "Changes have been made.\nDo you want to save your maze?");

                // Checking response
                switch (response) {
                // Yes (Prompts save, then load) (If save was canceled, or failed to write the
                // entire thing is canceled)
                case 0:
                    File file = FileDialog.saveFile();
                    if (file != null) {
                        if (!icons.saveIcons(file, timeElapsed))
                            JOptionPane.showMessageDialog(GameWindow.frame, "Could not save maze.\nFile IO error",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        else {
                            iconsChanged = false;
                            attemptLoadMaze(FileDialog.loadFile());
                        }
                    }
                    break;
                // No (Prompts load)
                case 1:
                    attemptLoadMaze(FileDialog.loadFile());
                    break;
                }
                // Cancel (Does nothing)
                break;
            } else
                attemptLoadMaze(FileDialog.loadFile());
        }
    }

    /*
     * Receives events from the menu pane.
     * If the reset button is clicked then if tiles have been loaded it will...
     * reset all the tiles positions and rotations to their defaults.
     * The timer will also reset to a state before any changes were made.
     * The iconsChanged check will be set to false.
     * Reset won't work if no tiles have been loaded.
     */
    public void resetClicked(MenuEvent e) {
        if (iconsLoaded) {
            // Reseting tiles.
            leftPane.reset();
            rightPane.reset();
            centerGrid.reset();
            
            // Resetting changes check
            iconsChanged = false;
            
            // Reseting timer.
            timeStarted = false;
            timer.stop();
            timer.setInitialDelay(1000);
            timeElapsed = icons.getDefaultTimePlayed();
            repaintTimer();
        }
    }

    /*
     * Receives events from the menu pane.
     * If the quit button is clicked then:
     * 1) If changes were made it will prompt the user to save their current maze.
     *    a) Yes: Saves maze then quit the game.
     *       a.1) If save failed it will end the quit process.
     *    b) No: Skips save and quits the game.
     *    c) Cancel: Ends quit process.
     * 2) If changes were not made it will quit the game.
     */
    public void quitClicked(MenuEvent e) {
        if (iconsChanged) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Changes have been made.\nDo you want to save your maze?");

            // Checking response
            switch (response) {
            // Yes (Prompts save, then quits) (If save was canceled the entire thing is
            // canceled)
            case 0:
                File file = FileDialog.saveFile();
                if (file != null) {
                    icons.saveIcons(file, timeElapsed);
                    iconsChanged = false;
                    System.exit(0);
                }
                break;
            // No (quits)
            case 1:
                System.exit(0);
            }
            // Cancel (Does nothing)
        } else
            System.exit(0);
    }

    /*
     * Receives events from the window. Event is received if the JFrame "x" is...
     * clicked.
     * Enacts the quit dialog.
     */
    public void windowClosing(WindowEvent e) {
        quitClicked(new MenuEvent(this));
    }

    /*
     * Receives events from the mouse.
     * Listens for clicks on the GameWindow.
     * If a click occurs sends an event to each of the panes saying that...
     * the mouse was clicked.
     * 
     * Purpose is to deselect tiles that have been selected.
     * Only implements the mouseClicked event.
     */
    public void mouseClicked(MouseEvent e) {
        leftPane.mouseClicked();
        rightPane.mouseClicked();
        centerGrid.mouseClicked();
    }
    
    /*
     * Receives an event every second from the timer.
     * 
     * If the timer has started it will increment the time elapsed by one second.
     * Afterwards it will repaint the timer to show the change.
     */
    public void actionPerformed(ActionEvent e) {
        if (timeStarted) {
            timeElapsed++;
            repaintTimer();
        }
    }

    public void mouseEntered(MouseEvent e) {} // Does nothing.
    public void mouseExited(MouseEvent e) {} // Does nothing.
    public void mousePressed(MouseEvent e) {} // Does nothing.
    public void mouseReleased(MouseEvent e) {} // Does nothing.
    public void windowActivated(WindowEvent e) {} // Does nothing.
    public void windowClosed(WindowEvent e) {} // Does nothing.
    public void windowDeactivated(WindowEvent e) {} // Does nothing.
    public void windowDeiconified(WindowEvent e) {} // Does nothing.
    public void windowIconified(WindowEvent e) {} // Does nothing.
    public void windowOpened(WindowEvent e) {} // Does nothing.
};
