package com.exa.android.quakereport

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date


class CustomAdapter(context: Context, val resource: Int, val customArray: ArrayList<CustomText>):
         ArrayAdapter<CustomText>(context, resource,customArray){

         private val locator = " of "  // used to split place and offset, before of and after of

             override fun getView(position : Int, contextView : View?, parent : ViewGroup): View {
                 var listItemView = contextView
                 if(listItemView == null){
                     //listitemview is null -> that we dont have any data to present on current view
                     listItemView = LayoutInflater.from(context).inflate(resource, parent, false)
                     // fetching layout -> retriving view on which data is to be present
                 }
                 // resouce - listitem here, this is style in which data is present
                 // parent - earth_activity here, this is view on which data in listitem is present
                 //false - showing that resource is not yet connected to parent

                 // get the current index data from earthArray that is to be represent
                 val currentItem  = getItem(position)


                 // Since, listitemview is view on which data is to present, then we retrive views from listitemview (parent->resource->textview)
                 // then set data on view that is get form earthArray which of class custom text that have get method



                 val text1 = listItemView?.findViewById<TextView>(R.id.magnitude)
                 text1!!.text = formatMag(currentItem!!.get_mag())// call fun for converting to single decimal digit

                 //get the mag and as per mag val set the back color

                 // val magBack = text1.background
                 //val magColor = magnitudeColor(currentItem.get_mag())
                 //magBack.setColor(magColor) -> this is used in java but in kotlin setcolor doesn't exist

                 // to do so we have another method as
                 // val magColor = magnitudeColor(currentItem.get_mag())
                 //val colorDrawable = ColorDrawable(magColor)
                 //text1.background = colorDrawable -> but here we have specified a shape in drawable of oval
                 // but it  returns 33*36(dimension we have given) square shape instead of our custom shape

                 // therefore, we use below code where we get shape and find color then using
                 //PorterDuff.Mode>SRC_IN -> we have change the tint of shape and apply as background to magnitude
                 //About Porter read at end

                 val magColor = magnitudeColor(currentItem.get_mag())
                 val shapeDrawable = ContextCompat.getDrawable(context, R.drawable.magnitude_color)
                 shapeDrawable?.let {
                     it.setColorFilter(magColor, PorterDuff.Mode.SRC_IN)
                     text1.background = it
                 }



                 // Splitting address in offset and location
                 val originalLoc = currentItem.get_loc()

                 var locOffset : String? = null
                 var priLoc : String? = null

                 if(originalLoc.contains(locator)){
                     val parts = originalLoc.split(locator)
                     locOffset = parts[0]+locator
                     priLoc = parts[1]
                 }else{
                     locOffset  = context.getString(R.string.near_the)
                     priLoc = originalLoc
                 }

                 val text2_1 = listItemView?.findViewById<TextView>(R.id.location_offset)
                 text2_1?.text = locOffset

                 val text2_2 = listItemView?.findViewById<TextView>(R.id.location_primary)
                 text2_2?.text = priLoc

                 val dateObject = Date(currentItem.get_timeinmilli())

                 // creating a date object by passing millisecond time as constructor
                 // formatting date in mon day year, it can format date in various format

                 val text3 = listItemView?.findViewById<TextView>(R.id.date)
                 text3?.text = formatDate(dateObject)

                 // format time in hour and min,
                 val text4 = listItemView?.findViewById<TextView>(R.id.time)
                 text4?.text = formatTime(dateObject)

                 return listItemView!!
             }

            private fun magnitudeColor(mag: Double): Int {
                val newMag =  mag.toInt()
               val color = when(newMag){
                   1 or 0 -> R.color.magnitude1
                   2 -> R.color.magnitude2
                   3 -> R.color.magnitude3
                   4 -> R.color.magnitude4
                   5-> R.color.magnitude5
                   6 -> R.color.magnitude6
                   7 -> R.color.magnitude7
                   8 -> R.color.magnitude8
                   9 -> R.color.magnitude9
                   else -> R.color.magnitude10plus
               }
                return ContextCompat.getColor(context, color)
            }

            fun formatDate( dateObj : Date) : String{
                 val dateformat = SimpleDateFormat("LLL dd,yyyy") // this pattern can be any , taken from date referece docs
                 return dateformat.format(dateObj)
             }

             fun formatTime(dateObj : Date) : String{
                 val timeFormat = SimpleDateFormat("h:mm a")
                 return timeFormat.format(dateObj)
             }
             fun formatMag(mag : Double) : String {
                 val magFormat = DecimalFormat("0.0")// formatting double to single decimal digit
                 return magFormat.format(mag)
             }
         }

   // PorterDuff ->  PorterDuff.Mode.SRC_IN is a mode used in Android graphics operations, particularly in color filtering. It defines how the source pixels (the color being applied) and destination pixels (the pixels of the drawable being drawn onto) are combined.
//
//In the context of color filtering with SRC_IN:
//
//Source pixels (color being applied): The color specified by magColor.
//Destination pixels (pixels of the drawable being drawn onto): The existing pixels of the drawable.
//When SRC_IN is used as the mode in a color filter, the resulting color of each pixel in the drawable will be the combination of the source and destination pixels according to the following rules:
//
//Only the overlapping parts of the source and destination pixels are kept.
//The color of the resulting pixel is determined by multiplying the color channels of the source and destination pixels.
//Here's a breakdown of how it works:
//
//If a pixel in the source drawable has an alpha value of 0 (fully transparent), it won't have any effect on the destination pixel.
//If a pixel in the source drawable has an alpha value of 1 (fully opaque), it will fully replace the corresponding pixel in the destination with the color of the source.
//In simpler terms, SRC_IN effectively applies the source color to the overlapping areas of the destination drawable, while leaving the non-overlapping areas unchanged. It's commonly used for tinting or applying color effects to drawables.
