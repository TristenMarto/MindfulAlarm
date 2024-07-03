package poepiesapps.com.mindfulalarm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActiveAlarmsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var activeAlarms: List<AlarmItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_alarms)

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load active alarms from a source (e.g., shared preferences, database)
        activeAlarms = loadActiveAlarms(this)

        // Initialize the adapter
        alarmAdapter = AlarmAdapter(activeAlarms)
        recyclerView.adapter = alarmAdapter
    }

    private fun loadActiveAlarms(context: Context): List<AlarmItem> {
        val sharedPreferences = context.getSharedPreferences("AlarmApp", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        val alarmItems = mutableListOf<AlarmItem>()
        val names = mutableSetOf<String>()

        for ((key, _) in allEntries) {
            if (key.startsWith("alarmItem_") && key.endsWith("_name")) {
                val name = key.removePrefix("alarmItem_").removeSuffix("_name")
                names.add(name)
            }
        }

        for (name in names) {
            val alarmName = sharedPreferences.getString("alarmItem_${name}_name", null) ?: continue
            val startHour = sharedPreferences.getInt("alarmItem_${name}_startHour", -1)
            val startMinute = sharedPreferences.getInt("alarmItem_${name}_startMinute", -1)
            val endHour = sharedPreferences.getInt("alarmItem_${name}_endHour", -1)
            val endMinute = sharedPreferences.getInt("alarmItem_${name}_endMinute", -1)
            val randomTime = sharedPreferences.getLong("alarmItem_${name}_randomTime", -1L)

            if (startHour != -1 && startMinute != -1 && endHour != -1 && endMinute != -1 && randomTime != -1L) {
                alarmItems.add(AlarmItem(alarmName, startHour, startMinute, endHour, endMinute, randomTime))
            }
        }

        return alarmItems
    }

}
