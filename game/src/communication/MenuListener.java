/**
 * group Cetus
 * members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * class COSC3011
 * 
 * MenuListener.java
 * Primary Authors: Robert Randolph and Austin Tarango
 * A listener interface that gives communication between the menu pane and the GameWindow.
 */
package communication;

public interface MenuListener {
    public void fileClicked(MenuEvent e);
    public void resetClicked(MenuEvent e);
    public void quitClicked(MenuEvent e);
}
