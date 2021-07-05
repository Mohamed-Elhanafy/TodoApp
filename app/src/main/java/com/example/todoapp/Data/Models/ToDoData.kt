package com.example.todoapp.Data.Models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todoapp.Data.Utils.Converter
import kotlinx.parcelize.Parcelize


@Entity(tableName = "todo_table")
@TypeConverters(Converter::class)
@Parcelize
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String
):Parcelable
