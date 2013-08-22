ScoreBoard
==========

Plugin for Darklight Nova Core to report scores to a web server

# API Spec
This is the specification of the API that this plugin requires (implemented in [Darklight Nova Web](https://github.com/darklight-studios/darklight-nova-web)

All responses are sent as valid JSON.

## Endpoints
### /api/:session_name/auth
* **Function**
    * Create a new team with the given name
    * Generate a key for the team, which is returned
* **Query**
    * **name:** team/individual name
* **Response**
    * **key:** a 512-bit unique key to use for future requests
* **Status**
    * **201**: success
    * **400**: 'name' was not received in the query
    * **404**: the name is invalid
    * **409**: the specified name is already in use

### /api/:session_name/update
* **Function**
    * Update a team's score and found issues
* **Query**
    * **key:** team's unique key returned from auth
    * **issues:** JSON list of found issues
* **Response**
    * **description:** if the request failed, this will have a description of the failure, or a stacktrace
* **Status**
    * **200**: nothing was changed
    * **201**: success
    * **404**: the key is invalid
    * **500**: the issues list is improperly formatted
