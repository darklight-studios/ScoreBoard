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

public class NamePrompt {
    private static String DEFAULT = "{unset}";

    private String[] validNames;
    private String name = DEFAULT;

    public NamePrompt() {}
    public NamePrompt(String[] validNames) {
        this.validNames = validNames;
    }

    public void setValidNames(String[] validNames) {
        this.validNames = validNames;
    }

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

    public String getName() {
        return name;
    }
}
