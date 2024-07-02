package poepiesapps.com.mindfulalarm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {
    private lateinit var stopAlarmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        stopAlarmButton = findViewById(R.id.stopAlarmButton)

//        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//        val ringtone = RingtoneManager.getRingtone(this, alarmUri)
//        ringtone.play()

        stopAlarmButton.setOnClickListener {
            stopAlarm()
        }
    }

    private fun stopAlarm() {
        val serviceIntent = Intent(this, AlarmService::class.java)
        stopService(serviceIntent)
        finish()
    }
}