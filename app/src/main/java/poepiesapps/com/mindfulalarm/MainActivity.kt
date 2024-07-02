package poepiesapps.com.mindfulalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker
    private lateinit var setAlarmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTimePicker = findViewById(R.id.startTimePicker)
        endTimePicker = findViewById(R.id.endTimePicker)
        setAlarmButton = findViewById(R.id.setAlarmButton)

        val viewAlarmsButton: Button = findViewById(R.id.viewAlarmsButton)
        viewAlarmsButton.setOnClickListener {
            val intent = Intent(this, ActiveAlarmsActivity::class.java)
            startActivity(intent)
        }

        setAlarmButton.setOnClickListener {
            val startHour = startTimePicker.hour
            val startMinute = startTimePicker.minute
            val endHour = endTimePicker.hour
            val endMinute = endTimePicker.minute

            val randomTimeInMillis = getRandomTimeInMillis(startHour, startMinute, endHour, endMinute)
            setAlarm(randomTimeInMillis)
        }
    }

    private fun getRandomTimeInMillis(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int): Long {
        val start = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
        }
        val end = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, endHour)
            set(Calendar.MINUTE, endMinute)
        }

        if (end.before(start)) {
            end.add(Calendar.DAY_OF_MONTH, 1)
        }

        val startTimeInMillis = start.timeInMillis
        val endTimeInMillis = end.timeInMillis

        val randomTimeInMillis = startTimeInMillis + (Math.random() * (endTimeInMillis - startTimeInMillis)).toLong()
        return randomTimeInMillis
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        // Try out different requestCodes
        val pendingIntent = PendingIntent.getBroadcast(this, timeInMillis.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        val formattedDateTime = convertMillisToDateTime(timeInMillis)
        println("Alarm on: $formattedDateTime")

        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_LONG).show()
    }

    private fun convertMillisToDateTime(timeInMillis: Long): String {
        val instant = Instant.ofEpochMilli(timeInMillis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        return dateTime.format(formatter)
    }
}