package com.lnick7v.sttodolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class AddNoteActivity : AppCompatActivity() {
    private lateinit var editTextNote: EditText
    private lateinit var radioButtonLow: RadioButton
    private lateinit var radioButtonMedium: RadioButton
    private lateinit var radioButtonHigh: RadioButton
    private lateinit var buttonSave: Button
    private lateinit var viewModel: AddNoteViewModel
    //*** НЕ НУЖНО после использования ViewModel
    /*private lateinit var noteDatabase: NoteDatabase // переменная БД
    private val handler = Handler(Looper.getMainLooper())*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        viewModel = ViewModelProvider(this).get(AddNoteViewModel::class.java)
        viewModel.getCloseScreen().observe(this, Observer { closeScreen ->
            if(closeScreen) finish()
        })

        //*** НЕ НУЖНО после использования ViewModel
        //noteDatabase = NoteDatabase.getInstance(application)  //присваиваем переменной объект БД
        initViews()

        buttonSave.setOnClickListener {
            saveNote()
        }




    }

    private fun saveNote() {
        if (editTextNote.text.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_text, Toast.LENGTH_SHORT).show()
        } else {
            val text = editTextNote.text.toString().trim()
            val priority = getPriority()
            //id не передаем т.к. autogenerate класса Note сгенерирует id самостоятельно (если получит 0)
            val note = Note(text, priority)
            viewModel.saveNote(note)

            //*** НЕ НУЖНО после использования ViewModel
            /*Thread { // в новом фоновом потоке
                noteDatabase.notesDao().add(note) // добавляем заметку в БД
                handler.post { finish() }  // через handler главного потока закрываем активити
            }.start()*/

        }
    }

    private fun getPriority() = when {
        radioButtonLow.isChecked -> 0
        radioButtonMedium.isChecked -> 1
        else -> 2
    }

    private fun initViews() {
        editTextNote = findViewById(R.id.editTextNote)
        radioButtonLow = findViewById(R.id.radioButtonLow)
        radioButtonMedium = findViewById(R.id.radioButtonMedium)
        radioButtonHigh = findViewById(R.id.radioButtonHigh)
        buttonSave = findViewById(R.id.buttonSave)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddNoteActivity::class.java)
        }
    }
}