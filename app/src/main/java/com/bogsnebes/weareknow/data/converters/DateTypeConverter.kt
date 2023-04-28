package com.bogsnebes.weareknow.data.converters

import androidx.room.TypeConverter
import java.sql.Timestamp

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp): Long {
        return timestamp.time
    }

    @TypeConverter
    fun toTimestamp(time: Long): Timestamp {
        return Timestamp(time)
    }

}