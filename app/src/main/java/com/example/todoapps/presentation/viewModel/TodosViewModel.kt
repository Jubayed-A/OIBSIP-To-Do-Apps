package com.example.todoapps.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
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

    private val _searchResults = MutableLiveData<List<Todo>>()
    val searchResults: LiveData<List<Todo>> get() = _searchResults

//        private var _allTodos: List<Todo> = emptyList()
    private val _allTodos = MutableLiveData<List<Todo>>()
    init {
        loadAllTodos() // Initialize _allTodos
    }
    private fun loadAllTodos() {
        viewModelScope.launch {
            dao.getAll().observeForever { allTodosList ->
                // Update _allTodos when the LiveData emits a value
                _allTodos.value = allTodosList
            }
        }
    }

    fun resetSearch() {
        _allTodos.value?.let { todosList ->
            _searchResults.value = todosList
        }
    }

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

    fun searchTodos(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val results = withContext(Dispatchers.IO) {
                    dao.searchTodos("%$query%") // Use % to enable partial matching
                }
                _searchResults.postValue(results)
            } else {
                // Handle empty query
                _searchResults.postValue(emptyList())
            }
        }
    }

}