package com.lnick7v.sttodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAddNote: FloatingActionButton
    private lateinit var noteDatabase: NoteDatabase   // переменная для доступа к БД
    private lateinit var recyclerViewNotes: RecyclerView   //строка для последующего доступа к RV
    private lateinit var notesAdapter: NotesAdapter //переменная для доступа к адаптеру

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteDatabase = NoteDatabase.getInstance(application) // получение экземпляра БД
        initViews()
        notesAdapter = NotesAdapter() //присваивание переменной объект адаптера

        //устанавливаем ClickListener на адаптер (точнее передаем адаптеру слушатель), который в свою очередь у себя в коде
        // устанаваливает слушатель на элементы списка. Сеттер вызывается 1 раз, а метод внутри него onNoteClick каждый раз при нажатии на элемент списка
        notesAdapter.setOnNoteClickListener(object : NotesAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note) { //удаление нажатием пока отключил
                /*database.remove(note.id)
                showNotes()*/
            }
        })

        recyclerViewNotes.adapter = notesAdapter // устанавливаем адаптер для RV
        //recyclerViewNotes.layoutManager = LinearLayoutManager(this) // устанавливаем LO manager для указания формата отображения RW - здесь в проекте это прописал в xml


        // описываеем itemTouch для реализации удаления элемента из RV свайпом. В конструктор SimpleCallback()
        // нужно передать 2 параметра: 1. направление перемещения, т.к. не испуользуем = 0   2. направление свайпа
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            //вызывается при перемещении элемента с одного места на другое, нам это не нужно, возвращаем - false
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //вызывается при свайпе
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // получаем N позиции элемента RV из ViewHoldera
                val note = notesAdapter.getNotes().get(position) // получаем массив заметок из адаптера и по позиции получаем элемент с нужным индексом
                noteDatabase.notesDao().remove(note.id) // удаляем строку из БД c указанным id
                showNotes()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes) // прикрепляем itemTouch к RV


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

    /* fun for updating notes on main activity */
    private fun showNotes() {
        notesAdapter.setNotes(noteDatabase.notesDao().getNotes()) // обновляем базу данных адаптера
    }
}