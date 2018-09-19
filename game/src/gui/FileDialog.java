/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * FileDialog.java
 * Primary Authors: Austin Tarango
 * Class with static methods to call up a save or load dialog box...
 * to either help save or load a file.
 * 
 * For loading:
 * It will return the selected file if it exists.
 * If it doesn't then it will give an error message.
 * Will return null if the procedure is canceled.
 * 
 * For Saving:
 * It will create the file if it doesn't exist.
 * If it does exist it will ask the user to confirm overriding it.
 * Will return null if the procedure is canceled.
 */
package gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileDialog {

    // File chooser opening at given path.
    // File chooser keeps track of the last place it was at.
    // Used for both open and save dialogs.
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

    public static File loadFile() {
        fileChooser.setDialogTitle("Load File");    // Setting title.
        File file = null;                           // Initializing

        // Opening "Open" file chooser.
        // Checks if the user actually selected a file or canceled.
        if (fileChooser.showOpenDialog(GameWindow.frame) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile(); // Getting file path.

            // Checking if the file exists.
            if (!file.exists()) {
                JOptionPane.showMessageDialog(GameWindow.frame, "Could not load maze.\nFile not found.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                file = null;
            }
        }
        return file;
    }

    public static File saveFile() {
        fileChooser.setDialogTitle("Save File");    // Setting title.
        File file = null;                           // Initializing

        // Opening "Save" file chooser.
        // Checks if the user actually selected a file or canceled. (Also includes if
        // you just typed in a file name that isn't there)
        if (fileChooser.showSaveDialog(GameWindow.frame) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();

            // Checking if the file exists.
            // If it doesn't then creates it to save to it.
            if (!file.exists())
                try {
                    file.createNewFile(); // Creating file.
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(GameWindow.frame, "Could not save maze.\nCould not create file.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    file = null; // File creation failed.
                }
            else {
                // File exists.
                // Checking if the user wants to override the selected file.
                Object[] options = { "Yes", "Cancel" };
                int response = JOptionPane.showOptionDialog(GameWindow.frame,
                        "Are you sure you want to overide file: " + file.getName() + "?", "Overide Conformation",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

                // Checking response.
                switch (response) {
                // Yes (Does nothing)
                // Cancel (Goes back to file save dialog.
                case 1:
                    file = saveFile();
                }
            }
        }
        return file;
    }
}
