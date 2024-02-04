package com.lnick7v.sttodolist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

class AddNoteActivity : AppCompatActivity() {
    private lateinit var editTextNote: EditText
    private lateinit var radioButtonLow: RadioButton
    private lateinit var radioButtonMedium: RadioButton
    private lateinit var radioButtonHigh: RadioButton
    private lateinit var buttonSave: Button
    private lateinit var noteDatabase: NoteDatabase // переменная БД


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        noteDatabase = NoteDatabase.getInstance(application)  //присваиваем переменной объект БД
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
            noteDatabase.notesDao().add(note)
            finish()
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