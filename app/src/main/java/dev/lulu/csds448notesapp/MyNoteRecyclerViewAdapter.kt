package dev.lulu.csds448notesapp

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.navigation.Navigation

import dev.lulu.csds448notesapp.placeholder.PlaceholderContent.PlaceholderItem
import dev.lulu.csds448notesapp.databinding.FragmentNoteListBinding
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel
import java.security.AccessController.getContext

class MyNoteRecyclerViewAdapter() : RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder>() {

    interface AdapterDelegate {
        fun didSelectRow(index:Int)
    }

    var adapterDelegate: AdapterDelegate? = null

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
        val note = NoteModel.getNotes()[position]
        Log.d("NoteModel", "OnBind " + note.header)
//        holder.idView.text = item.id
        holder.contentView.text = note.header
    }

    override fun getItemCount(): Int = NoteModel.getCount()

    inner class ViewHolder(binding: FragmentNoteListBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        init {
            contentView.setOnClickListener{
                Log.d("AdapterTest", getBindingAdapterPosition().toString())

                val row = layoutPosition
                adapterDelegate?.didSelectRow(row)

//                val positionBundle = Bundle()
//                positionBundle.putString("position", (getBindingAdapterPosition() + 1).toString())
//                Navigation.findNavController(contentView).navigate(R.id.action_noteListFragment_to_noteEditFragment, positionBundle)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}