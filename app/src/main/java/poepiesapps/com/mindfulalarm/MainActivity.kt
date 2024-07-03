package poepiesapps.com.mindfulalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
    private lateinit var alarmNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTimePicker = findViewById(R.id.startTimePicker)
        endTimePicker = findViewById(R.id.endTimePicker)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        alarmNameEditText = findViewById(R.id.alarmNameEditText)

        val viewAlarmsButton: Button = findViewById(R.id.viewAlarmsButton)
        viewAlarmsButton.setOnClickListener {
            val intent = Intent(this, ActiveAlarmsActivity::class.java)
            startActivity(intent)
        }

        setAlarmButton.setOnClickListener {
            val alarmName = alarmNameEditText.text.toString()
            if (alarmName.isBlank()) {
                // Show an error message
                alarmNameEditText.error = "Alarm name cannot be empty"
                return@setOnClickListener
            }
            val alarmItem = AlarmItem(
                hash = 0,
                name = alarmName.toString(),
                startHour = startTimePicker.hour,
                startMinute = startTimePicker.minute,
                endHour = endTimePicker.hour,
                endMinute = endTimePicker.minute,
                randomTime = getRandomTimeInMillis(startTimePicker.hour, startTimePicker.minute, endTimePicker.hour, endTimePicker.minute)
            )
            val alarmId = alarmItem.hashCode()
            alarmItem.hash = alarmId

            setAlarm(alarmItem)
            saveAlarmItem(this, alarmItem)
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

    private fun setAlarm(alarmItem: AlarmItem) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        // Try out different requestCodes
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            alarmItem.hash,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmItem.randomTime, pendingIntent)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmItem.randomTime, AlarmManager.INTERVAL_DAY, pendingIntent)

        val formattedDateTime = convertMillisToDateTime(alarmItem.randomTime)
        println("squeek Alarm ${alarmItem.hash} set on: $formattedDateTime")

        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_LONG).show()
    }

    private fun convertMillisToDateTime(timeInMillis: Long): String {
        val instant = Instant.ofEpochMilli(timeInMillis)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        return dateTime.format(formatter)
    }

    private fun saveAlarmItem(context: Context, alarmItem: AlarmItem) {
        val sharedPreferences = context.getSharedPreferences("AlarmApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("alarmItem_${alarmItem.name}_hash", alarmItem.hash)
        editor.putString("alarmItem_${alarmItem.name}_name", alarmItem.name)
        editor.putInt("alarmItem_${alarmItem.name}_startHour", alarmItem.startHour)
        editor.putInt("alarmItem_${alarmItem.name}_startMinute", alarmItem.startMinute)
        editor.putInt("alarmItem_${alarmItem.name}_endHour", alarmItem.endHour)
        editor.putInt("alarmItem_${alarmItem.name}_endMinute", alarmItem.endMinute)
        editor.putLong("alarmItem_${alarmItem.name}_randomTime", alarmItem.randomTime)
        editor.apply()
    }

}