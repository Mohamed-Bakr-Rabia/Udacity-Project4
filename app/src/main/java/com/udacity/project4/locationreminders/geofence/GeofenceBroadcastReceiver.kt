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
