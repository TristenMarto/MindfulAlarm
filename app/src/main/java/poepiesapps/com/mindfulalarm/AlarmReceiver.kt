package poepiesapps.com.mindfulalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        context?.startForegroundService(serviceIntent)
        println("Starting the alarm service here")

        Toast.makeText(context, "Time for mindfulness", Toast.LENGTH_LONG).show()
    }
}