package poepiesapps.com.mindfulalarm

data class AlarmItem (
    var hash: Int,
    var name: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val randomTime: Long,
)
