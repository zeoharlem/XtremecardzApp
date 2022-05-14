package ng.com.zeoharlem.swopit.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.text.format.DateUtils
import android.util.Log
import ng.com.zeoharlem.swopit.utils.MyCustomExtUtils.capitalizeWords
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


object TrackingUtility {


    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("LogNotTimber")
    fun getCityNameAddress(context: Context, latitude: Double, longitude: Double): String{
        try{
            var cityName: String    = ""
            var countryName: String = ""
            var areaStreetName:String   = ""
            var stateName: String   = ""
            val geocoder            = Geocoder(context, Locale.getDefault())
            val address             = geocoder.getFromLocation(latitude, longitude, 1)
            //cityName                = if(address[0].locality == null) address[0]?.subAdminArea!! else address[0]!!.locality
            stateName               = address[0].adminArea
            countryName             = address[0].countryName
//            areaStreetName          = address[0].getAddressLine(0)
            Log.e("AddressState", "getCityNameAddress: $address")
//        return "${areaStreetName.capitalizeWords()}, " +
//                "${cityName.capitalizeWords()}, " +
//                "${stateName.capitalizeWords()}, " +
//                countryName.capitalizeWords()
            return "$stateName, $countryName".capitalizeWords()
        }
        catch (e: Exception){
            return e.message.toString()
        }

    }

    @SuppressLint("LogNotTimber")
    fun getCurrentStateName(context: Context, latitude: Double, longitude: Double): String {
        return try{
            var stateName: String   = ""
            val geocoder            = Geocoder(context, Locale.getDefault())
            val address             = geocoder.getFromLocation(latitude, longitude, 1)
            stateName               = address[0].adminArea
            Log.e("AddressState", "current state: $stateName")
            stateName.replace(" State", "")
        }
        catch (e: Exception){
            e.message.toString()
        }

    }

    fun timesElapsedDiff(dateTime: String): String {
        //val format  = "EEE, d MMM yyyy HH:mm:ss"
        val format  = "yyyy-MM-dd HH:mm:ss"
        val sdf     = SimpleDateFormat(format, Locale.getDefault())
        return sdf.parse(dateTime).toString()
    }

    @SuppressLint("LogNotTimber")
    fun formatTimeAgo(date1: String): String {
        var conversionTime  = ""
        try{
            val format  = "yyyy-MM-dd hh:mm:ss"
            val sdf     = SimpleDateFormat(format, Locale.getDefault())

            val datetime    = Calendar.getInstance()
            val date2       = sdf.format(datetime.time).toString()

            val dateObj1    = sdf.parse(date1)
            val dateObj2    = sdf.parse(date2)
            val diff        = dateObj2?.time?.minus(dateObj1!!.time)

            val diffDays    = diff?.div((24 * 60 * 60 * 1000))
            val diffHours   = diff?.div((60 * 60 * 1000))
            val diffMin     = diff?.div((60 * 1000))
            val diffSec     = diff?.div(1000)

            when {
                diffDays!! >= 360 -> {
                    val results = diffDays / 360
                    conversionTime += if(results > 1) "$results years " else "$results year "
                }
                diffDays > 30 -> {
                    val results = diffDays / 30
                    conversionTime += if(results > 1) "$results months " else "$results month "
                }
                diffDays > 6 -> {
                    val results = diffDays / 7
                    conversionTime += if(results > 1) "$results weeks " else "$results week "
                }
                diffDays > 1 -> {
                    conversionTime += if(diffDays > 1) "$diffDays days " else "$diffDays day "
                }
                diffHours!! > 1 -> {
                    val results = diffHours - diffDays * 24
                    conversionTime += if(results > 1) "$results hrs " else results.toString() + "hr "
                }
                diffMin!! > 1 -> {
                    val results = diffMin - diffHours * 60
                    conversionTime += if(results > 1) "$results mins " else "$results min "
                }
                diffSec!! > 1 -> {
                    val results = diffSec - diffMin * 60
                    Log.e("TrackingUtility", "formatTimeAgo: $results")
                    conversionTime += if(results > 1) "$results secs " else "$results sec "
                }
//                diffSec > 0 -> {
//                    val results = diffSec - diffMin * 60
//                    Log.e("TrackingUtilityGre", "formatTimeAgo: $results")
//                    conversionTime += if(results > 0) "$results secs " else "$results sec "
//                }
            }
        }
        catch (ex:java.lang.Exception){
            Log.e("formatTimeAgo",ex.toString())
        }
//        if(conversionTime != ""){
//            conversionTime += "Ago"
//        }
        return conversionTime
    }

    fun calculateTimeAgo(date: String): String{
        val format  = "yyyy-MM-dd HH:mm:ss"
        val sdf     = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone    = TimeZone.getTimeZone("GMT")
        try {
            val time: Long = sdf.parse(date)!!.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            return ago.toString()
        }
        catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }


    fun getDisplayDateTime(serverDate: String): String {
        return try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
            val date = simpleDateFormat.parse(serverDate)
            val convertDateFormat = SimpleDateFormat("EEEE.LLLL(dd).yyyy KK:mm:ss aaa", Locale.getDefault())
            convertDateFormat.format(date!!)
        }
        catch (e: Exception) {
            e.message.toString()
        }
    }

    fun getDisplayDate(serverDate: String): String {
        return try {
            val simpleDateFormat    = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = simpleDateFormat.parse(serverDate)
            val convertDateFormat   = SimpleDateFormat("EEEE.LLLL(dd).yyyy", Locale.getDefault())
            convertDateFormat.format(date!!)
        }
        catch (e: Exception) {
            e.message.toString()
        }
    }

    fun formatDateTimeAgo(date: String): String {
//        val format = "yyyy-MM-dd HH:mm:ss"
//        val sdf = SimpleDateFormat(format, Locale.getDefault())
//        val time: Long = sdf.parse(date)!!.time
//        val prettyTime = PrettyTime(Locale.getDefault())
//        return prettyTime.format(Date(time))
        return ""
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getDistanceDiff(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta   = lon1 - lon2
        var dist    = (sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + (cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))))

        dist        = acos(dist)
        dist        = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}