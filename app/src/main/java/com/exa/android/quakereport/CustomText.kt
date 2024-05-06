package com.exa.android.quakereport

class CustomText(private val mag : Double, private val loc : String, private val timeinmilli : Long, val url : String) {
    fun get_mag() : Double {
        return mag
    }
    fun get_loc() :String {
        return loc
    }
    fun get_timeinmilli() : Long{
        return timeinmilli
    }
    fun get_url() : String{
        return url
    }
}