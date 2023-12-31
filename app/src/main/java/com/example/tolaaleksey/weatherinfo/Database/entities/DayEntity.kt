package com.example.tolaaleksey.weatherinfo.Database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tolaaleksey.weatherinfo.classes.Weather
import java.util.UUID

@Entity(tableName = DayEntity.TableName)
class DayEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val temperature: Int,
    val humidity: Int,
    val cloudiness: Int,
    val chanceOfRain: Int,
    var description: String
) {
    companion object {
        const val TableName = "days"
    }
}