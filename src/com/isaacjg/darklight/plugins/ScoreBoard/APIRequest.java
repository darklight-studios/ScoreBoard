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

import com.ijg.darklight.sdk.utils.JaJ.JaJ;
import com.ijg.darklight.sdk.utils.JaJ.JsonData;
import com.ijg.darklight.sdk.utils.JaJ.JsonObject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class APIRequest {
    private URI requestURI;
    private URL requestURL;
    private URLConnection connection;

    private JsonObject response;

    public APIRequest(String protocol, String server, String requestURL, String query) {
        try {
            requestURI = new URI(protocol, server, requestURL, query, null);
            this.requestURL = requestURI.toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println("[ScoreBoard] Error creating URI/URL");
            e.printStackTrace();;
        }
    }

    public void send() {
        try {
            connection = requestURL.openConnection();

            StringBuilder rawResponse = new StringBuilder();

            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) rawResponse.append(scanner.next());
            scanner.close();

            ArrayList<JsonData> data = JaJ.load(rawResponse.toString());
            if (data.size() >= 1) response = new JsonObject("data", data);
            else System.out.println("[ScoreBoard] Error parsing response");
        } catch (IOException e) {
            System.out.println("[ScoreBoard] Error sending request");
            e.printStackTrace();
        }
    }

    public JsonObject getResponse() {
        return response;
    }

    public JsonData get(String data) {
        return response.get(data);
    }
}
