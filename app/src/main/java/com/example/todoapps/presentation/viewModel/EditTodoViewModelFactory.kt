package com.example.todoapps.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapps.data.dao.TodosDao

class EditTodoViewModelFactory(private val todoId: Long, private val dao: TodosDao )
    : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Long::class.java, TodosDao::class.java).newInstance(todoId, dao)
    }

}