package com.exa.android.quakereport

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

import com.exa.android.quakereport.QueryUtils.fetchFromEarthQuake
import com.exa.android.quakereport.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EarthActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<CustomText>> {

    private lateinit var earth_adapter: CustomAdapter
    lateinit var empty_view : TextView
    lateinit var progress_bar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earth_activity)

        // Initialize the adapter with an empty list
        earth_adapter = CustomAdapter(this@EarthActivity, R.layout.listitem, ArrayList())

        val listview = findViewById<ListView>(R.id.list)
        listview.adapter = earth_adapter

         empty_view = findViewById(R.id.empty_view)
        listview.emptyView = empty_view

        listview.setOnItemClickListener { parent, view, position, id ->
            val curEarth = earth_adapter.getItem(position)
            val earthUri = Uri.parse(curEarth?.get_url())
            val intent = Intent(Intent.ACTION_VIEW, earthUri)
            startActivity(intent)
        }

        // Initialize the loader
        supportLoaderManager.initLoader(ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<CustomText>> {
        return EarthLoader(this, USGS_URL)
    }

    override fun onLoaderReset(loader: Loader<ArrayList<CustomText>>) {
        // Clear the adapter data when loader is reset
        earth_adapter.clear()
    }

    override fun onLoadFinished(
        loader: Loader<ArrayList<CustomText>>,
        data: ArrayList<CustomText>?
    ) {

        progress_bar = findViewById(R.id.bar)
        progress_bar.visibility = View.GONE

        empty_view.text = "No earthquake found"
        // Clear the adapter data
        earth_adapter.clear()

        // Update the adapter with the new data
        if (data != null) {
            earth_adapter.addAll(data)
        }
    }

    companion object {
        private const val ID = 1
        private val USGS_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10"
    }
}
