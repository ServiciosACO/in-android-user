package co.kubo.indiesco.utils

import java.util.*


class DateUtil {

    companion object {

        const val HOUR_LIMIT = "13:00"

        @JvmStatic
        fun roundedCurrentDate(): Calendar {
            val calendar: Calendar = Calendar.getInstance()
            val minutes: Int = calendar.get(Calendar.MINUTE)
            if (minutes <= 30) {
                calendar.set(Calendar.MINUTE, 30)
            } else {
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1)
            }
            return calendar
        }

        @JvmStatic
        fun convertLimitToCalendar(limit: String): Calendar {
            val parts = limit.split(":").toTypedArray()
            val calendarLimit = Calendar.getInstance()
            calendarLimit[Calendar.HOUR_OF_DAY] = parts[0].toInt()
            calendarLimit[Calendar.MINUTE] = parts[1].toInt()
            return calendarLimit
        }

        @JvmStatic
        fun calculateDifferenceBetweenToDated(date1: Date, date2: Date): Long {
            return date2.time - date1.time
        }

        @JvmStatic
        fun convertMillisecondsToHours(milliseconds: Long): Int {
            return (milliseconds / (1000 * 60 * 60) % 24).toInt()
        }
    }
}