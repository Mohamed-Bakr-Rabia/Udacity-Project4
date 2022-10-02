package com.udacity.project4.locationreminders.geofence

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
         val jobIntentServiceIntent by lazy {
            Intent(context,GeofenceTransitionsJobIntentService::class.java)
        }
        if(intent.action == SaveReminderFragment.ACTION_GEOFENCE_EVENT)
        {
            val geofenceEvent = GeofencingEvent.fromIntent(intent)
            if (geofenceEvent!!.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                val geofenceId = geofenceEvent.triggeringGeofences!![0]!!.requestId

                jobIntentServiceIntent.putExtra("ID",geofenceId)
                GeofenceTransitionsJobIntentService.enqueueWork(context,jobIntentServiceIntent)

            }
        }

    }
}