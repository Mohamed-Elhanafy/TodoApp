package com.example.todoapp.Fragments.Add

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Data.Models.Priority
import com.example.todoapp.Data.Models.ToDoData
import com.example.todoapp.Data.ViewModel.ToDoViewModel
import com.example.todoapp.Fragments.SharedViewModel
import com.example.todoapp.R

class AddFragment : Fragment() {

    lateinit var title_et: EditText
    lateinit var description_et: EditText
    lateinit var priorities: Spinner
    lateinit var mToDoViewModel: ToDoViewModel
    lateinit var mSharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        //initialise the components
        title_et = view.findViewById(R.id.title_et)
        description_et = view.findViewById(R.id.description_et)
        priorities = view.findViewById(R.id.priorities)
        mToDoViewModel = ToDoViewModel(application = requireActivity().application)
        mSharedViewModel = SharedViewModel(application = requireActivity().application)


        //set menu
        setHasOptionsMenu(true)


        priorities.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {

        val mTitle = title_et.text.toString()
        val mPriority = priorities.selectedItem.toString()
        val mDescription = description_et.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)


        if (validation) {
            // Insert Data to Database
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT)
                .show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }


}