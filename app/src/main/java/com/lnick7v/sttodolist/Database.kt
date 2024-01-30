package com.lnick7v.sttodolist

import kotlin.random.Random

object Database {
    private val notes = ArrayList<Note>()

    init {
        for (i in 0..20) {
            val note = Note(i, "Note $i", Random.nextInt(3))
            notes.add(note)
        }
    }

    fun add(note: Note) {
        notes.add(note)
    }

    fun remove(id: Int) {
        notes.removeIf { note -> note.id == id }
    }

    fun getNotes(): ArrayList<Note> {
        return ArrayList<Note>(notes)
    }

}