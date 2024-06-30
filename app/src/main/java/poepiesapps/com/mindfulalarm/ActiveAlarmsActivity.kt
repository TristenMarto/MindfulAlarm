package poepiesapps.com.mindfulalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActiveAlarmsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var activeAlarms: List<String> // This would typically be a list of your Alarm data class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_alarms)

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load active alarms from a source (e.g., shared preferences, database)
        activeAlarms = loadActiveAlarms()

        // Initialize the adapter
        alarmAdapter = AlarmAdapter(activeAlarms)
        recyclerView.adapter = alarmAdapter
    }

    private fun loadActiveAlarms(): List<String> {
        // Placeholder for loading active alarms, replace with actual implementation
        return listOf("Alarm 1", "Alarm 2", "Alarm 3")
    }
}
