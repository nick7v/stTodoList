package com.lnick7v.sttodolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val noteDatabase = NoteDatabase.getInstance(application)

    fun getNotes(): LiveData<List<Note>> {
        return noteDatabase.notesDao().getNotes()
    }

    fun remove(note: Note) {
        Thread { noteDatabase.notesDao().remove(note.id) }.start()
    }
}