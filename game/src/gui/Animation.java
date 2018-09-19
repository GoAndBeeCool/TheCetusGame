/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * Animation.java
 * Primary Authors: Gokul Babu
 * A class with a static method that will vibrate a given frame.
 * The frame (GameWindow) vibrates if an illegal move was made when...
 * attempting to move tiles.
 * The move is illegal if a tile is attempted to move to another tile...
 * and not an empty place holder.
 */
package gui;

import java.awt.Frame;

public class Animation {

    private static int vibrationTime = 20;
    private static int vibrationDistance = 5;

    public static void vibrate(Frame frame, int duration) {

        try {
            int startingX = frame.getLocationOnScreen().x;
            int startingY = frame.getLocationOnScreen().y;
            for (int i = 0; i < vibrationTime; i++) {
                Thread.sleep(duration);
                frame.setLocation(startingX + vibrationDistance, startingY);
                Thread.sleep(duration);
                frame.setLocation(startingX, startingY);
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
