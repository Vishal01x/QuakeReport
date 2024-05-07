package com.exa. android.quakereport

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.exa.android.quakereport.CustomAdapter
import com.exa.android.quakereport.CustomText
import com.exa.android.quakereport.QueryUtils
import com.exa.android.quakereport.QueryUtils.fetchFromEarthQuake
import com.exa.android.quakereport.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EarthActivity : AppCompatActivity() {
     var earth_data : ArrayList<CustomText> ? = null
    private val USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earth_activity)

        CoroutineScope(Dispatchers.Main).launch {
            earth_data = fetchFromEarthQuake(USGS_URL) ?: ArrayList()

            val listview = findViewById<ListView>(R.id.list)
            val earth_adapter = CustomAdapter(this@EarthActivity, R.layout.listitem, earth_data!!)
            listview.adapter = earth_adapter

            listview.setOnItemClickListener { parent, view, position, id ->
                val curEarth = earth_adapter.getItem(position)
                val earthUri = Uri.parse(curEarth?.get_url())
                val webIntent = Intent(Intent.ACTION_VIEW, earthUri)
                startActivity(webIntent)
            }
        }
    }
}
