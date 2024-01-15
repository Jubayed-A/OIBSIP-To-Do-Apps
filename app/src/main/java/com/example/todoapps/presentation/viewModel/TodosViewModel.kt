package com.example.todoapps.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.todoapps.data.dao.TodosDao
import com.example.todoapps.data.entity.Todo
import kotlinx.coroutines.launch

class TodosViewModel(val dao : TodosDao) : ViewModel() {
    var newTodosTitle = ""
    val todos = dao.getAll()
    private val _navigateToTodo = MutableLiveData<Long?>()
    val navigateToTodo : LiveData<Long?> get() = _navigateToTodo

     fun addTodo() {
        viewModelScope.launch {
            val todo = Todo()
            todo.title = newTodosTitle
            dao.insert(todo)
        }
    }

    fun onTodoItemClicked(todoId : Long){
        _navigateToTodo.value = todoId
    }

    fun onTodoItemNavigate(){
        _navigateToTodo.value = null
    }

}