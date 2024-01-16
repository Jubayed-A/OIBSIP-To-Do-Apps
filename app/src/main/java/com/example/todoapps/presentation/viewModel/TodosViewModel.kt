package com.example.todoapps.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapps.data.dao.TodosDao
import com.example.todoapps.data.entity.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(val dao: TodosDao) : ViewModel() {
    var newTodosTitle = ""
    val todos = dao.getAll()

    private val _navigateToTodo = MutableLiveData<Long?>()
    val navigateToTodo: LiveData<Long?> get() = _navigateToTodo

    fun addTodo() {
        viewModelScope.launch {
            val todo = Todo()
            todo.title = newTodosTitle
            dao.insert(todo)
        }
    }

    fun onTodoItemClicked(todoId: Long) {
        _navigateToTodo.value = todoId
    }

    fun onTodoItemNavigate() {
        _navigateToTodo.value = null
    }

    private val _searchResults = MutableLiveData<List<Todo>>()
    val searchResults: LiveData<List<Todo>> get() = _searchResults

    fun searchTodos(query: String) {
        viewModelScope.launch {
            // Switch to IO dispatcher
            withContext(Dispatchers.IO) {
                val results = dao.searchTodos("%$query%") // Use % to enable partial matching
                // Switch back to the main dispatcher before updating LiveData
                withContext(Dispatchers.Main) {
                    _searchResults.postValue(results)
                }
            }
        }
    }

}