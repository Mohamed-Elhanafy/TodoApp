package com.example.todoapp.Fragments.List

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.todoapp.Data.Models.ToDoData
import com.example.todoapp.Data.ViewModel.ToDoViewModel
import com.example.todoapp.Fragments.List.Adapters.ListAdapter
import com.example.todoapp.Fragments.SharedViewModel
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

    lateinit var listLayout: ConstraintLayout
    lateinit var mToDoViewModel: ToDoViewModel
    lateinit var mSharedViewModel: SharedViewModel
    lateinit var noDataImageView: ImageView
    lateinit var NoDatatextView: TextView

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    val adapter: ListAdapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //initialise the Views
        listLayout = binding.listLayout
        noDataImageView = binding.noDataImageView
        NoDatatextView = binding.NoDatatextView
        mToDoViewModel = ToDoViewModel(application = requireActivity().application)
        mSharedViewModel = SharedViewModel(application = requireActivity().application)

        //set up the recyclerView
        setupRecyclerView()

        //observe the Live data
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })


        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemovale()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer {
                adapter.setData(it)
            })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer {
                adapter.setData(it)
            })
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun confirmRemovale() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("YES") { _, _ ->
            mToDoViewModel.deleteAllData()
            Toast.makeText(activity, "Successfully removed!", Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("NO") { _, _ -> }
        builder.setTitle("Delete all the data")
        builder.setMessage("Are you sure you want delete all the data you have ?")
        builder.create().show()
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            noDataImageView.visibility = View.VISIBLE
            NoDatatextView.visibility = View.VISIBLE
        } else {
            noDataImageView.visibility = View.INVISIBLE
            NoDatatextView.visibility = View.INVISIBLE
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {

        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedIte: ToDoData, position: Int) {
        val snackbar: Snackbar = Snackbar.make(
            view,
            "Deleted",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            mToDoViewModel.insertData(deletedIte)
            adapter.notifyItemChanged(position)
        }
        snackbar.show()

    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"
        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })


    }


}