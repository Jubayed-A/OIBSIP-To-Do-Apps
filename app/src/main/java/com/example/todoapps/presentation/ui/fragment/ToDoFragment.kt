package com.example.todoapps.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapps.R
import com.example.todoapps.data.databases.TodosDatabase
import com.example.todoapps.databinding.FragmentEditTodoBinding
import com.example.todoapps.databinding.FragmentToDoBinding
import com.example.todoapps.presentation.ui.adapter.TodoItemAdapter
import com.example.todoapps.presentation.viewModel.TodosViewModel
import com.example.todoapps.presentation.viewModel.TodosViewModelFactory


class ToDoFragment : Fragment() {

    private var _binding: FragmentEditTodoBinding? = null
    private lateinit var binding: FragmentToDoBinding
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(layoutInflater, container, false)

        searchView = binding.searchView

        val application = requireNotNull(this.activity).application
        val dao = TodosDatabase.getInstance(application).todosDao
        val viewModelFactory = TodosViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[TodosViewModel::class.java]
        val adapter = TodoItemAdapter { todoId ->
            viewModel.onTodoItemClicked(todoId)
        }
        binding.todoList.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.todos.observe(viewLifecycleOwner) { todosList ->
            todosList?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToTodo.observe(viewLifecycleOwner) { todoId ->
            todoId?.let {
                val action = ToDoFragmentDirections.actionToDoFragmentToEditTodoFragment(todoId)
                this.findNavController().navigate(action)
                viewModel.onTodoItemNavigate()
            }
        }

//        Search view code here

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searchTodos(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the entered text

                return true
            }
        })

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            // Update your adapter with the new search results
            adapter.submitList(searchResults)
        }


        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Check if you are on the home fragment (TodosFragment)
                    if (findNavController().currentDestination?.id == R.id.toDoFragment) {
                        // If on the home fragment, exit the app
                        requireActivity().finish()
                    } else {
                        // If on other fragments, navigate back to the home fragment (TodosFragment)
                        findNavController().navigate(R.id.toDoFragment)
                    }
                }
            })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}