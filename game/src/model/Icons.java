/**
 * @group Cetus
 * @members Gokul Babu, Kevin Littell, Robert Randolph, Austin Tarango, Nicholas Whites
 * @class COSC3011
 * 
 * Icons.java
 * Primary Authors: Gokul Babu and Kevin Littell
 * The model holds all the icons for the maze pieces for the GameWindow.
 * 
 * Can load maze icons from a given file for the GameWindow.
 * Will load either an "unplayed" or "played" maze file.
 * If loading an "unplayed" maze it will randomly position and rotate the...
 * icons into the side panes.
 * If loading a "played" maze it will position and rotate the icons...
 * to be exactly how they were when saved.
 * 
 * Can save icons from the current maze from the GameWindow.
 * Will save the current positions and rotations of all the tiles...
 * into a file.
 * 
 * Keeps track of the amount of time played of the maze before...
 * any changes were made.
 * 
 * All save and load files are in a special format.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Icons {

    // Static variables for checking the file format.
    private static final byte[] UNPLAYED_MAZE = { (byte) 0xca, (byte) 0xfe, (byte) 0xbe, (byte) 0xef };
    private static final byte[] PLAYED_MAZE = { (byte) 0xca, (byte) 0xfe, (byte) 0xde, (byte) 0xed };

    private TileIcon[] icons;       // The Icons for each of the tiles.
    private long defaultTimePlayed; // The time played for the current maze at load time.
    
    // Returns the array of icons that were created.
    public TileIcon[] getIcons() {
        return icons;
    }

    /*
     * Reads a byte file given its name.
     * The file is of a special format so that Icons can be created.
     * If the file was read successfully it then creates the Icons.
     * Returns true or false for if the file was read successfully.
     */
    public boolean loadIcons(File file) {
        byte[] heldBytes; // Bytes read from the file.

        try {
            // Opening file.
            FileInputStream inFile = new FileInputStream(file.getAbsoluteFile());
            heldBytes = new byte[inFile.available()];
            inFile.read(heldBytes); // Reading file.
            inFile.close();         // Closing file.
        } catch (IOException e) {
            return false;
        }

        // Creating the icons.
        return createIcons(heldBytes);
    }

    // Save icon data to a file.
    // Returns true if there were no problems writing to the file.
    public boolean saveIcons(File file, long timePlayed) {
        try {
            // Opening file to write to.
            FileOutputStream fileOs = new FileOutputStream(file);

            byte[] array; // Bytes of a converted number.

            // TileData
            int numberOfTiles = this.getIcons().length;
            int currentPosition = 0;
            int currentRotation = 0;
            int numberOfLines = 0;
            float coordinate = 0;

            // Writing file type.
            fileOs.write(PLAYED_MAZE);

            // Writing number of tiles.
            array = ByteConversion.convertToByteArray(numberOfTiles);
            fileOs.write(array);
            
            // Writing the time played for the maze.
            array = ByteConversion.convertToByteArray(timePlayed);
            fileOs.write(array);

            // Going through each tile.
            for (TileIcon tile : icons) {

                // Writing tiles current position
                currentPosition = tile.getCurrentPosition();
                array = ByteConversion.convertToByteArray(currentPosition);
                fileOs.write(array);

                // Writing tiles current rotation
                currentRotation = tile.getCurrentRotation();
                array = ByteConversion.convertToByteArray(currentRotation);
                fileOs.write(array);

                // Writing tiles number of lines.
                ArrayList<Float> tileData = tile.getTileData();
                numberOfLines = (int) (float) tile.getTileData().get(2);
                array = ByteConversion.convertToByteArray(numberOfLines);
                fileOs.write(array);

                // Writing tiles coordinates.
                for (int i = 3; i < tileData.size(); ++i) {
                    coordinate = tileData.get(i);
                    array = ByteConversion.convertToByteArray(coordinate);
                    fileOs.write(array);
                }
            }
            fileOs.close(); // Closing file.

        } catch (IOException e) {
            return false; // :(
        }
        return true;
    }

    // Creates the Icons based on the data read from the byte file.
    // Returns true if the icons were created successfully.
    private boolean createIcons(byte[] heldBytes) {
        int numberOfTiles;  // Number of tiles in the file.
        long timePlayed;    // The time played for the maze.
        int tilePosition;   // Position of tile.
        int tileRotation;   // Rotation of the tile
        int numberOfLines;  // Number of lines for each tile.

        byte[] loadType = new byte[4];  // The first four bytes of the file.
        boolean playedMaze = false;     // True if the loaded maze was a played maze.

        // The buffer to extract byte data into meaningful numbers.
        ByteBuffer buffer = ByteBuffer.wrap(heldBytes);

        // Getting first four bytes from file.
        // Used to check if the file format is valid.
        // Also dictates what the file format is.
        for (int i = 0; i < 4; i++)
            loadType[i] = buffer.get();

        // Checking if the maze format is correct.
        // Also checking which maze format it is.
        if (Arrays.equals(loadType, PLAYED_MAZE))
            playedMaze = true;
        else if (!Arrays.equals(loadType, UNPLAYED_MAZE))
            return false;
        
        // Getting number of tiles from the file.
        // Allocating the memory needed for the number of tiles.
        numberOfTiles = buffer.getInt();
        icons = new TileIcon[numberOfTiles];
        
        // Getting the time played for the maze.
        // sets the time to 0 if the maze was unplayed.
        // Otherwise sets it to the amount of time played.
        timePlayed = buffer.getLong();
        if (playedMaze)
            defaultTimePlayed = timePlayed;
        else
            defaultTimePlayed = 0;

        // Going through each of the tiles in the file.
        for (int tile = 0; tile < numberOfTiles; tile++) {
            // Data of the current tile.
            ArrayList<Float> tileData = new ArrayList<Float>();

            tilePosition = buffer.getInt();     // Getting the tile ID.
            tileRotation = buffer.getInt();     // Getting the tile rotation
            numberOfLines = buffer.getInt();    // Getting the number of lines associated with the tile.

            // Adding data to the associated tile.
            tileData.add((float) tilePosition);
            tileData.add((float) tileRotation);
            tileData.add((float) numberOfLines);

            // Going through each line of the associated tile.
            for (int line = 0; line < numberOfLines; line++) {

                // Going through each point of the associated line.
                for (int point = 0; point < 4; point++)
                    // Adding the coordinates of each line to the associated tile.
                    tileData.add(buffer.getFloat());
            }
            // Creating the tile icon based on tileData.
            icons[tile] = new TileIcon(tileData);
        }

        // Checking if the maze is unplayed.
        if (!playedMaze)
            scramble(); // Randomizing tileIcon positions and rotations.

        return true;
    }

    // Randomly rearranges the tiles default positions and rotations.
    private void scramble() {
        Random r = new Random();    // Random number generator.
        int tempPosition;           // Temp Position.
        int indexOne, indexTwo;     // Random indexes of tiles to swap.
        int size = icons.length;    // Number of icons.

        // Randomizing positions.
        // Simply swaps positions of two randomly selected tiles...
        // over and over again.
        for (int i = 0; i < size * 4; i++) {
            indexOne = r.nextInt(size);     // The first tile.
            indexTwo = r.nextInt(size);     // The second tile.

            // Gets position of first tile.
            // Sets position of first tile to that of the second.
            // Sets position of second tile to that of the first.
            tempPosition = icons[indexOne].getDefaultPosition();
            icons[indexOne].setDefaultPosition(icons[indexTwo].getDefaultPosition());
            icons[indexTwo].setDefaultPosition(tempPosition);
        }

        // Randomizing rotations.
        for (TileIcon icon : icons)
            icon.setDefaultRotation(r.nextInt(4));
    }
    
    // Returns the original amount of time played before any moves were made.
    public long getDefaultTimePlayed() {
        return defaultTimePlayed;
    }
    
    /*
     * Returns true if the maze is solved.
     * The maze is solved if all tiles are in the center grid...
     * have a rotation of 0 and are positioned in order of when...
     * the tiles were loaded.
     */
    public boolean isSolved() {
        int tile = 16;
        
        for (TileIcon icon : icons)
            if (icon.getCurrentRotation() != 0 || icon.getCurrentPosition() != tile++)
                return false;
        
        return true;
    }
}
