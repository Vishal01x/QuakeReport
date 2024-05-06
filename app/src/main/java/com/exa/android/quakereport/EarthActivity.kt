package com.exa. android.quakereport

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class EarthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earth_activity)

        val earth_data = QueryUtils.extractEarthquakes()

        val listview = findViewById<ListView>(R.id.list)

        val earth_adapter = CustomAdapter(this, R.layout.listitem, earth_data)

        listview.adapter = earth_adapter

        listview.setOnItemClickListener { parent, view, position, id ->
            // get the cur view on which we click
            val curEarth = earth_adapter.getItem(position)
            // convert url to uri as intent support uri
            val earthUri = Uri.parse(curEarth?.get_url())
            //apply action that is to be performed
            val webIntent = Intent(Intent.ACTION_VIEW, earthUri)
            startActivity(webIntent)

        }
    }
}