ScoreBoard
==========

Plugin for Darklight Nova Core to report scores to a web server

# Note that this plugin is a work in progress, and is not ready for use yet

# API Spec
This is the specification of the API that this plugin requires (implemented in [Darklight Nova Web](https://github.com/darklight-studios/darklight-nova-web)

All responses are sent as valid JSON.

## Endpoints
### /api/:session_name/auth
* **Function**
	* If the team does not already exist
    	* Create a new team with the given name
    	* Generate a key for the team, which is returned
	* If the team does exist
		* Return the team's unique key
* **Query**
    * **name:** team/individual name
* **Response**
    * **key:** a 512-bit unique key to use for future requests
* **Status**
	* **200**: success (team was fetched)
    * **201**: success (the team was created)
    * **400**: 'name' was not received in the query
    * **404**: the name is invalid
    * **409**: the specified name is already in use
* **Sample Query**
    * https://darklight-nova-web.herokuapp.com/api/testsession/auth?name=teamname

### /api/update
* **Function**
    * Update a team's score and found issues
* **Query**
    * **key:** team's unique key returned from auth
    * **issues:** JSON object of found issues
* **Response**
    * **description:** if the request failed, this will have a description of the failure, or a stacktrace
* **Status**
    * **200**: success (nothing was changed)
    * **201**: success
    * **404**: the key is invalid
    * **500**: the issues list is improperly formatted
* **Sample Query**
    * https://darklight-nova-web.herokuapp.com/api/update?key=keyreturnedfromauth&issues={"issue1": "description of issue1"}
    * Note that the URL must be properly encoded