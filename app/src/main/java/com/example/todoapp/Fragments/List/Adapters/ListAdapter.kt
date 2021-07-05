package com.example.todoapp.Fragments.List.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Data.Models.Priority
import com.example.todoapp.Data.Models.ToDoData
import com.example.todoapp.Fragments.List.ListFragmentDirections
import com.example.todoapp.R

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        val priorityIndicator: CardView = itemView.findViewById(R.id.priority_indicator)
        val rowBackGround: ConstraintLayout = itemView.findViewById(R.id.row_background)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // sets the text to the textview from our itemHolder class
        holder.titleText.text = dataList[position].title
        holder.descriptionText.text = dataList[position].description
        holder.rowBackGround.setOnClickListener {
            val action =
                ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)

        }

        val priority = dataList[position].priority

        when (priority) {
            Priority.HIGH -> holder.priorityIndicator.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))

            Priority.MEDIUM -> holder.priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.yellow
                )
            )


            Priority.LOW -> holder.priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.green
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList,toDoData)
        val toDoDiffResult= DiffUtil.calculateDiff(toDoDiffUtil)

        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
        Log.d("RV2", "setData: $dataList")
    }


}


