package com.lnick7v.sttodolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddNoteViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NoteDatabase.getInstance(application).notesDao()

    private var shouldCloseScreen = MutableLiveData<Boolean>()

    fun getCloseScreen(): LiveData<Boolean> = shouldCloseScreen


    //метод сохранения новой заметки в БД
    fun saveNote(note: Note) {
        Thread {
            notesDao.add(note)
            shouldCloseScreen.postValue(true) // менять значение MutableLiveData нельзя из
            // фонового потока, метод  .postValue, позволяет это сделать
        }.start()

    }
}