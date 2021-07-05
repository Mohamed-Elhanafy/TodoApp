package com.example.todoapp.Fragments.Update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.Data.Models.ToDoData
import com.example.todoapp.Data.ViewModel.ToDoViewModel
import com.example.todoapp.Fragments.SharedViewModel
import com.example.todoapp.R


class UpdateFragment : Fragment() {
    lateinit var current_title_et: EditText
    lateinit var current_description_et: EditText
    lateinit var current_priorities: Spinner
    lateinit var mSharedViewModel: SharedViewModel
    lateinit var mToDoViewModel: ToDoViewModel

    val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)

        current_title_et = view.findViewById(R.id.current_title_et)
        current_description_et = view.findViewById(R.id.current_description_et)
        current_priorities = view.findViewById(R.id.current_priorities)

        mSharedViewModel = SharedViewModel(application = requireActivity().application)
        mToDoViewModel = ToDoViewModel(application = requireActivity().application)

        current_title_et.setText(args.currentItem.title)
        current_description_et.setText(args.currentItem.description)
        current_priorities.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        current_priorities.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemovable()
        }
        return super.onOptionsItemSelected(item)
    }




    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val getpriority = current_priorities.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, description)

        if (validation) {
            //update items
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getpriority),
                description
            )

            mToDoViewModel.updateData(updateItem)
            Toast.makeText(activity, "Successfully updated!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(activity, "pls fill out all fields", Toast.LENGTH_SHORT).show()

        }
    }
    private fun confirmItemRemovable() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("YES") { _, _ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(activity, "Successfully removed!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("NO") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}' ?")
        builder.setMessage("Are you sure you want delete '${args.currentItem.title}' note ?")
        builder.create().show()

    }
}