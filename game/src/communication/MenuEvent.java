/**
 * group Cetus
 * members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * class COSC3011
 * 
 * MenuEvent.java
 * Primary Authors: Robert Randolph and Austin Tarango
 * An event to indicate that a button from the menu pane has been clicked.
 * Keeps the source of where the event came from.
 */
package communication;

import java.util.EventObject;

@SuppressWarnings("serial")
public class MenuEvent extends EventObject {

    public MenuEvent(Object source) {
        super(source);
    }
}
