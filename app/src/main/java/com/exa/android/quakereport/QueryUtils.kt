package com.exa.android.quakereport

import android.net.http.UrlRequest
import android.text.TextUtils
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

 private val TAG = "Errorr!"
object QueryUtils {


    suspend fun fetchFromEarthQuake(urlRequest: String): ArrayList<CustomText>? {
        return withContext(Dispatchers.IO) {
            val url = createUrl(urlRequest)
            var jsResponse: String? = null

            try {
                jsResponse = url?.let { makeHttpRequest(it) }
            } catch (e: IOException) {
                Log.e(TAG, "Error making HTTP request", e)
            }

            jsResponse?.let { extractEarthquakes(it) }
        }
    }


    fun createUrl(stringurl : String) : URL? {
        var url: URL? = null // Initialize with null

        try {
            url = URL(stringurl)
        } catch (e: MalformedURLException) {
            Log.e(TAG, "Problem in url creation", e)
        }

        return url
    }

    private fun makeHttpRequest(url : URL):String?{
        var jsresponse : String ? = null
        if(url == null)return null
        var urlconnection : HttpURLConnection? = null
        var inputstream : InputStream ? = null
        try{
            urlconnection = url.openConnection() as HttpURLConnection
            urlconnection.readTimeout = 1000
            urlconnection.connectTimeout = 1500
            urlconnection.requestMethod = "GET"
            urlconnection.content

            if(urlconnection.responseCode == 200){
                inputstream = urlconnection.inputStream
                jsresponse = readFromStream(inputstream)
            }else{
                Log.e(TAG,"errro message"+urlconnection.responseCode)
            }
        }catch (e : IOException){
            Log.e(TAG, "problem in recieving eathquke json object")
        }finally {
            if(urlconnection != null){
                urlconnection.disconnect()
            }
            if(inputstream != null){
                inputstream.close()
            }
        }
        return jsresponse
    }


/**
     * Return a list of [Earthquake] objects that has been built up from
     * parsing a JSON response.
     */

private fun readFromStream(inputStream: InputStream): String {
    val output = StringBuilder() // Initialize StringBuilder

    if(inputStream != null) {
        val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
        var line = reader.readLine()

        while(line != null) {
            output.append(line)
            line = reader.readLine() // Read the next line
        }
    }

    return output.toString()
}


    fun extractEarthquakes(earthJson : String): ArrayList<CustomText>? {

        if(TextUtils.isEmpty(earthJson))return null

        // Create an empty ArrayList that we can start adding earthquakes to
        val earthquakes = ArrayList<CustomText>()

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            val root = JSONObject(earthJson)
            // extracting an array from json bec we get '['
            val earthArray = root.getJSONArray("features")

            var i =0
            while(i<earthArray.length()){
                // extracting obj at each index
                val curEarth = earthArray.optJSONObject(i++)
                val properties = curEarth.optJSONObject("properties")
                val magnitude = properties.getDouble("mag")
                val place = properties.getString("place")
                val time = properties.getLong("time")
                val url = properties.getString("url")

                earthquakes.add(CustomText(magnitude, place,time,url))
            }

        } catch (e: JSONException) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e)
        }

        // Return the list of earthquakes
        return earthquakes
    }
}
