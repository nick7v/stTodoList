package com.lnick7v.sttodolist

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayoutNotes: LinearLayout
    private lateinit var buttonAddNote: FloatingActionButton
    private val database = Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        buttonAddNote.setOnClickListener {
            startActivity(AddNoteActivity.newIntent(this))
        }
    }

    override fun onResume() {
        super.onResume()
        showNotes()
    }



    private fun initViews() {
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes)
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