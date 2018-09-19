/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * MenuPane.java
 * Primary Authors: Robert Randolph
 * Menu bar for the GameWindow.
 * Positioned on the left and right sides of the GameWindow.
 * Contains three buttons that influence the GameWindow.
 * The file button gives a load or save option for the maze.
 * The reset button resets all tiles default position and rotation.
 * The quit button closes the program.
 * 
 * File and quit prompt user to save maze if changes were made.
 */
package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import communication.MenuEvent;
import communication.MenuListener;

@SuppressWarnings("serial")
public class MenuPane extends JPanel implements ActionListener {

    // Holds listeners to send events to.
    ArrayList<MenuListener> listeners = new ArrayList<MenuListener>();

    JButton file, reset, quit; // The buttons.

    public MenuPane() {

        // Setting up pane.
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        setLayout(flowLayout);
        setBorder(BorderFactory.createRaisedBevelBorder());

        Dimension dim = new Dimension(100, 30);

        // Creating buttons.
        file = new JButton("File");
        file.setPreferredSize(dim);
        file.addActionListener(this);

        reset = new JButton("Reset");
        reset.setPreferredSize(dim);
        reset.addActionListener(this);

        quit = new JButton("Quit");
        quit.setPreferredSize(dim);
        quit.addActionListener(this);

        // Adding buttons to pane.
        add(file);
        add(reset);
        add(quit);
    }

    // Adds a listener to the pane to send events to.
    public void addMenuListener(MenuListener listener) {
        listeners.add(listener);
    }

    /*
     * Sends an event to each listener. (Specifically the GameWindow) Event is sent
     * if the file button has been clicked.
     */
    private void fireFileClicked() {
        for (MenuListener listener : listeners)
            listener.fileClicked(new MenuEvent(this));
    }

    /*
     * Sends an event to each listener. (Specifically the GameWindow) Event is sent
     * if the reset button has been clicked.
     */
    private void fireResetClicked() {
        for (MenuListener listener : listeners)
            listener.resetClicked(new MenuEvent(this));
    }

    /*
     * Sends an event to each listener. (Specifically the GameWindow) Event is sent
     * if the quit button has been clicked.
     */
    private void fireQuitClicked() {
        for (MenuListener listener : listeners)
            listener.quitClicked(new MenuEvent(this));
    }

    /*
     * Receives events from the JButtons. Performs actions or sends events depending
     * on which button was clicked.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == file) {
            fireFileClicked();
        }
        else if (e.getSource() == reset) {
            fireResetClicked();
        }
        else if (e.getSource() == quit)
            fireQuitClicked();
    }
}
