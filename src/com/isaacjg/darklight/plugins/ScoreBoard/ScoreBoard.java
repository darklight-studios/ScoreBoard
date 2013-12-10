package com.isaacjg.darklight.plugins.ScoreBoard;

/*
 * ScoreBoard - A plugin for Darklight Nova Core
 * Copyright © 2013 Isaac Grant
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.ijg.darklight.sdk.core.AccessHandler;
import com.ijg.darklight.sdk.core.Plugin;

/**
 * ScoreBoard is a plugin for Darklight Nova Core that allows
 * for interaction between the DNC client and the DNW server for
 * score and stat reporting
 *
 * @author Isaac Grant
 */
public class ScoreBoard extends Plugin {

    public ScoreBoard(AccessHandler accessHandler) {
        super(accessHandler);
    }

    @Override
    protected void start() {

    }

    @Override
    protected void kill() {

    }
}
