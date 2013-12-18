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
import com.ijg.darklight.sdk.core.IssueData;
import com.ijg.darklight.sdk.core.Plugin;
import com.ijg.darklight.sdk.utils.JaJ.JsonPrimitive;
import com.ijg.darklight.sdk.utils.JaJ.JsonString;

import java.util.HashMap;
import java.util.Map;

/**
 * ScoreBoard is a plugin for Darklight Nova Core that allows
 * for interaction between the DNC client and the DNW server for
 * score and stat reporting
 *
 * @author Isaac Grant
 */
public class ScoreBoard extends Plugin {
    private String name;
    private String protocol, server, session;
    private String sessionKey;

    public ScoreBoard(AccessHandler accessHandler) {
        super(accessHandler);
    }

    @Override
    protected void start() {

    }

    @Override
    protected void kill() {

    }

    private boolean auth() {
        boolean retrVal = false;

        APIRequest request = new APIRequest(protocol, server, "/api/" + session + "/auth", "name=" + name);
        request.send();

        long statusCode = ((JsonPrimitive) request.get("status")).getData();
        switch ((int) statusCode) {
            case 200:
            case 201:
                sessionKey = ((JsonString) request.get("key")).getData();
                retrVal = true;
                break;
            case 400: System.out.println("[ScoreBoard] Error: bad auth request, status 400");
                break;
            case 404: System.out.println("[ScoreBoard] Error: Team name invalid, status 404");
                break;
            case 409: System.out.println("[ScoreBoard] Error: Team name is already in use, status 409");
                break;
            default: System.out.println("[ScoreBoard] Error: Unknown status code received from the server");
        }

        return retrVal;
    }

    private boolean update() {
        boolean retrVal = false;

        String issueString = "{";
        IssueData[] issues = accessHandler.getFixedIssues();
        for (int i = 0; i < issues.length-1; i++) {
            issueString += '\"' + issues[i].getName() + "\": \"" + issues[i].getDescription() + "\", ";
        }
        issueString += '\"' + issues[issues.length-1].getName() + "\": \"" + issues[issues.length-1].getDescription() + "\"}";

        APIRequest request = new APIRequest(protocol, server, "/api/update", "key=" + sessionKey + "&issues=" + issueString);
        request.send();;

        long statusCode = ((JsonPrimitive) request.get("status")).getData();
        switch ((int) statusCode) {
            case 200:
            case 201: retrVal = true;
                break;
            case 404: System.out.println("[ScoreBoard] Error: Invalid key");
                System.out.println(((JsonString) request.get("description")).getData());
                break;
            case 500: System.out.println("[ScoreBoard] Error: The issues list is improperly formatted");
                System.out.println(((JsonString) request.get("description")).getData());
                break;
            default: System.out.println("[ScoreBoard] Error: Unknown status code received from the server");
        }

        return retrVal;
    }

    private String makeQueryString(HashMap<String, String> parameters) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> pair : parameters.entrySet()) {
            query.append(pair.getKey() + "=" + pair.getValue() + "&");
        }
        return query.toString();
    }

    public void setTeamName(String name) { this.name = name; }
    public String getTeamName() { return name; }

    public void setServerInfo(String protocol, String server, String session) {
        this.protocol = protocol;
        this.server = server;
        this.session = session;
    }
    public String getProtocol() { return protocol; }
    public String getServer() { return server; }
    public String getSession() { return session; }
}
