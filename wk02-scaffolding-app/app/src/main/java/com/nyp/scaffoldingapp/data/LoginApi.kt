package com.nyp.scaffoldingapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// Simpleton object
object LoginApi{

    //LINKTO JASONMOMOAHUSERS.JSON
    private const val ENDPOINT = "ltkg9l.mockapi.dog/"
    private const val TIMEOUT_MS = 10_000 //failsafe, I don't fucking want it to freeze

    suspend fun isRegistered(userId: String): Boolean = withContext(Dispatchers.IO) {
        // withContext(Dispatchers.IO) moves this off the main thread so the UI never freezes.

        val connection = URL(ENDPOINT).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = TIMEOUT_MS // max time to reach the server
            connection.readTimeout = TIMEOUT_MS    // max time to wait for the reply

            // Anything other than 200 OK is a failure (404, 500, ...).
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("Server returned ${connection.responseCode}")
            }

            // Read the whole body as text; 'use' closes the stream for us.
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            containsRegisteredUser(body, userId)
        } finally {
            connection.disconnect() // always release the connection
        }
    }

    // Separate function so each one does ONE job: parse and check.
    private fun containsRegisteredUser(body: String, userId: String): Boolean {
        val users = JSONArray(body) // throws JSONException if the body is not a JSON array

        // Look through every entry for a matching id.
        for (index in 0 until users.length()) {
            val user = users.getJSONObject(index)
            // optString turns a numeric id like 3 into "3", so it matches what the user typed.
            val sameId = user.optString("id") == userId
            // optBoolean gives false if "registered" is missing, so missing data means NOT registered.
            val registered = user.optBoolean("registered", false)
            if (sameId && registered) return true
        }
        return false // id not in the list, or listed but not registered
    }
}

