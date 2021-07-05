package com.example.todoapp.Fragments

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.Data.Models.Priority
import com.example.todoapp.Data.Models.ToDoData
import com.example.todoapp.R

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase :MutableLiveData<Boolean> = MutableLiveData(false)

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {


        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.red
                        )
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.yellow
                        )
                    )
                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.green
                        )
                    )
                }
            }

            Log.d("onItemSelected", "onItemSelected: $position")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }


    }

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            return false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High priorities" -> {
                Priority.HIGH
            }
            "Medium priorities" -> {
                Priority.MEDIUM
            }
            "Low priorities" -> {
                Priority.LOW
            }
            else -> Priority.MEDIUM

        }

    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }

    }

    fun checkIfDatabaseEmpty(toDoData:List<ToDoData>){

        emptyDatabase.value = toDoData.isEmpty()
    }
}