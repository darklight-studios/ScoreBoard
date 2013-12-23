package com.isaacjg.darklight.plugins.ScoreBoard;

/*
 * Copyright (C) 2013  Isaac Grant
 *
 * This file is part of ScoreBoard, a plugin for Darklight Nova Core.
 *
 * ScoreBoard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScoreBoard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ScoreBoard.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handle retrieving team name
 * First this tries to read the name from the name file, and if the
 * name file does not exist or contains an invalid name, a name prompt
 * will be displayed
 */
public class NamePrompt {
    private static String DEFAULT = "{unset}";

    private String[] validNames;
    private String name = DEFAULT;

    /**
     * Default constructor
     */
    public NamePrompt() {}

    /**
     * Constructor with valid names as a parameter, so {@link #setValidNames(String[])} does not need to be subsequently called
     * @param validNames Array of valid team names to use
     */
    public NamePrompt(String[] validNames) {
        this.validNames = validNames;
    }

    /**
     * Set valid team names, if this is not called then validation is not used
     * @param validNames Array of valid team names to use
     */
    public void setValidNames(String[] validNames) {
        this.validNames = validNames;
    }

    /**
     * Name prompt
     * First checks if a valid name can be read from the name file, if not then
     * it will prompt for a team name, and will save the input name
     */
    public void prompt() {
        if (readName()) {
            if (!name.equals(DEFAULT)) {
                return;
            }
        } else {
            String testName = "";
            try {
                testName = JOptionPane.showInputDialog(null, "Enter your team name", "DNC ScoreBoard Name Entry", 1);
            } catch (Exception e) {
                if (e instanceof NullPointerException) {
                    System.out.println("[ScoreBoard] No name was input, terminating...");
                    System.exit(0);
                }
                System.out.println("[ScoreBoard] Error on name prompt");
                e.printStackTrace();
                prompt();
                return;
            }

            if (!setName(testName.trim())) {
                prompt();
                return;
            } else {
                try {
                    writeName();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read the name file
     * @return True if a valid name is found, otherwise false
     */
    private boolean readName() {
        File nameFile = new File("name");
        boolean retr = false;
        try (Scanner scanner = new Scanner(nameFile)) {
            setName(scanner.nextLine().trim());
            retr = true;
        } catch (FileNotFoundException e) {
            System.out.println("[ScoreBoard] Error: name file not found");
            e.printStackTrace();
        }
        return retr;
    }

    /**
     * Write the team name to the name file
     * @throws IOException If there was an error writing the file
     */
    private void writeName() throws IOException {
        if (!name.equals(DEFAULT)) {
            File nameFile = new File("name");
            FileWriter out = new FileWriter(nameFile);
            out.write(name);
            out.close();
            System.out.println("[ScoreBoard] Wrote \"" + name + "\" to the name file");
        }
        throw new IOException("[ScoreBoard] Could not write name to name file");
    }

    /**
     * Check validity and set the team name
     * @param name The desired team name
     * @return True if the name was valid and successfully set, otherwise false
     */
    private boolean setName(String name) {
        if (validNames.length > 0) {
            for (String validName : validNames) {
                if (name.equals(validName)) {
                    System.out.println("[ScoreBoard] Set name: \"" + name + "\"");
                    this.name = name;
                    return true;
                }
            }
        } else {
            System.out.println("[ScoreBoard] Set name: \"" + name + "\"");
            this.name = name;
            return true;
        }
        System.out.println("[ScoreBoard] Error: \"" + name + "\" is an invalid name");
        return false;
    }

    /**
     * @return Team name
     */
    public String getName() {
        return name;
    }
}
