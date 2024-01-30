package com.lnick7v.sttodolist

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAddNote: FloatingActionButton
    private val database = Database
    private lateinit var recyclerViewNotes: RecyclerView   //строка для последующего доступа к RV
    private val notesAdapter = NotesAdapter(database.getNotes())  //создания адаптера

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        recyclerViewNotes.adapter = notesAdapter // устанавливаем адаптер для RW
        recyclerViewNotes.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, null )

        buttonAddNote.setOnClickListener {
            startActivity(AddNoteActivity.newIntent(this))
        }
    }

    override fun onResume() {
        super.onResume()
        showNotes()
    }



    private fun initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
        buttonAddNote = findViewById(R.id.buttonAddNote)
    }

    /*** fun for updating notes on main activity ***/
    private fun showNotes() {
        linearLayoutNotes.removeAllViews()

        //takes all notes from database, converts each from xml-layout to View objects
        // and places all views(notes) on ScrollView Linear Layout
        database.getNotes().forEach { note ->
            val view = layoutInflater.inflate(R.layout.note_item, linearLayoutNotes, false)

            view.setOnClickListener{
                database.remove(note.id)
                showNotes()
            }

            //val texViewNote = view.findViewById<TextView>(R.id.textViewNote)
            //val texViewNote = (view as TextView)
            (view as TextView).text = note.text

            val colorResId = when (note.priority) {
                0 -> android.R.color.holo_green_light
                1 -> android.R.color.holo_orange_light
                else -> android.R.color.holo_red_light
            }
            view.background = ContextCompat.getDrawable(this, colorResId)

            linearLayoutNotes.addView(view)
        }
    }
}