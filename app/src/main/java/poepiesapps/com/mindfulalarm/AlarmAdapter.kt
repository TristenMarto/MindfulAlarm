package poepiesapps.com.mindfulalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlarmAdapter(
    private val alarms: List<AlarmItem>,
    private val onCancelClick: (AlarmItem) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarmItem = alarms[position]
        "Alarm ${alarmItem.name} set: ${alarmItem.startHour}:${alarmItem.startMinute} - ${alarmItem.endHour}:${alarmItem.endMinute}".also { holder.alarmTextView.text = it }
        holder.cancelButton.setOnClickListener { onCancelClick(alarmItem) }
    }

    override fun getItemCount(): Int = alarms.size

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmTextView: TextView = itemView.findViewById(R.id.alarmTextView)
        val cancelButton: Button = itemView.findViewById(R.id.cancelButton)
    }
}