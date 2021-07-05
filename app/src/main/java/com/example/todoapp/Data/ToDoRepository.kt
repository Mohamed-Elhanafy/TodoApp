package com.example.todoapp.Data

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import com.example.todoapp.Data.Models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()
    val sortByHighPriority: LiveData<List<ToDoData>> =toDoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> =toDoDao.sortByLowPriority()

        suspend fun insertData(toDoData:ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }
    suspend fun deleteItem(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }
    suspend fun deleteAllData(){
        toDoDao.deleteAllData()
    }
    fun searchDatabase(searchQuery: String):LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(searchQuery)

    }

}