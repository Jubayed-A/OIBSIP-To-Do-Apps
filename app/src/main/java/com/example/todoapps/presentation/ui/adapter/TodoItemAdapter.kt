package com.example.todoapps.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapps.data.entity.Todo
import com.example.todoapps.databinding.ItemTodoBinding
import com.example.todoapps.presentation.util.ColorProvider

class TodoItemAdapter(private val clickListener: (todoId: Long) -> Unit) :
    ListAdapter<Todo, TodoItemAdapter.TodoItemViewHolder>(TodoItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = getItem(position)
        val cardView = holder.binding.root as CardView
        holder.bind(item, clickListener)
        cardView.setCardBackgroundColor(ColorProvider.getColorResourceId(position))
    }

    class TodoItemViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun inflateFrom(parent: ViewGroup): TodoItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTodoBinding.inflate(layoutInflater, parent, false)
                return TodoItemViewHolder(binding)
            }
        }

        fun bind(item: Todo, clickListener: (todoId: Long) -> Unit) {
            binding.todo = item
            binding.root.setOnClickListener { clickListener(item.id) }
        }

    }

}