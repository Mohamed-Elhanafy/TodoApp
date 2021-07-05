package com.example.todoapp.Data.Utils

import androidx.room.TypeConverter
import com.example.todoapp.Data.Models.Priority

class Converter {

    //this function convert from Priority to string
    @TypeConverter
    fun fromPriority(priority: Priority):String{

        return priority.name
    }

    @TypeConverter
    fun toPriority(string:String): Priority {

        return Priority.valueOf(string)
    }

}