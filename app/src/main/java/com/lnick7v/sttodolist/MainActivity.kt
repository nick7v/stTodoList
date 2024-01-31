package com.lnick7v.sttodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAddNote: FloatingActionButton
    private val database = Database
    private lateinit var recyclerViewNotes: RecyclerView   //строка для последующего доступа к RV
    private val notesAdapter = NotesAdapter(database.getNotes())  //создание объекта адаптера, при первом создании мы ему передаем массив Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        recyclerViewNotes.adapter = notesAdapter // устанавливаем адаптер для RW
        //recyclerViewNotes.layoutManager = LinearLayoutManager(this) // устанавливаем LO manager для указания формата отображения RW - здесь в проекте это прописал в xml

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
        notesAdapter.setNotes(database.getNotes()) // обновляем базу данных адаптера
    }
}