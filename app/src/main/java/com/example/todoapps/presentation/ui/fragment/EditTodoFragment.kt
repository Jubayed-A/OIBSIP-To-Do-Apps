package com.example.todoapps.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.todoapps.data.databases.TodosDatabase
import com.example.todoapps.databinding.FragmentEditTodoBinding
import com.example.todoapps.presentation.viewModel.EditTodoViewModel
import com.example.todoapps.presentation.viewModel.EditTodoViewModelFactory

class EditTodoFragment : Fragment() {

    private lateinit var binding: FragmentEditTodoBinding
    private var _binding: FragmentEditTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTodoBinding.inflate(layoutInflater, container, false)

        val todoId = EditTodoFragmentArgs.fromBundle(requireArguments()).todoId
        val application = requireNotNull(this.activity).application
        val dao = TodosDatabase.getInstance(application).todosDao
        val viewModelFactory = EditTodoViewModelFactory(todoId, dao)
        val viewModel = ViewModelProvider(this, viewModelFactory)[EditTodoViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.navigateToList.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                val action = EditTodoFragmentDirections.actionEditTodoFragmentToToDoFragment()
                view?.findNavController()?.navigate(action)
                viewModel.onNavigatedToList()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}