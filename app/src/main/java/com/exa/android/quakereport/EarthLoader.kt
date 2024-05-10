package com.exa.android.quakereport

import android.content.Context
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import com.exa.android.quakereport.QueryUtils.fetchFromEarthQuake

class EarthLoader(context: Context, url: String) : AsyncTaskLoader<ArrayList<CustomText>>(context) {
    val mUrl = url
    override fun onStartLoading() {

        forceLoad()

    }

    override fun loadInBackground(): ArrayList<CustomText>? {
        if (mUrl == null) return null

        return fetchFromEarthQuake(mUrl)
    }

}