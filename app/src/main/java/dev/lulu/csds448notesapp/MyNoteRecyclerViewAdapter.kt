package dev.lulu.csds448notesapp

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation

import dev.lulu.csds448notesapp.placeholder.PlaceholderContent.PlaceholderItem
import dev.lulu.csds448notesapp.databinding.FragmentNoteListBinding
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel
import java.security.AccessController.getContext

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyNoteRecyclerViewAdapter(
    private val model: NoteModel


) : RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentNoteListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note: Note = model.getNotes()[position]

//        holder.idView.text = item.id
        holder.contentView.text = note.header
    }

    override fun getItemCount(): Int = model.getCount()

    inner class ViewHolder(binding: FragmentNoteListBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        init {
            contentView.setOnClickListener{
                Log.d("AdapterTest", getBindingAdapterPosition().toString())
                val positionBundle = Bundle()
                positionBundle.putString("position", (getBindingAdapterPosition() + 1).toString())

                Navigation.findNavController(contentView).navigate(R.id.action_noteListFragment_to_noteEditFragment, positionBundle)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}