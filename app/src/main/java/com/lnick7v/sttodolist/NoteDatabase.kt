package com.lnick7v.sttodolist

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_NAME = "notes.db"  // const для хранения имени БД, имя и расширение могут быть любыми

//реализует БД SQLite посредством room. Абстрактный класс, наследуется от RoomDatabase, подкласс абстракного класса
//библиотека room создаст под капотом. В параметры анотации передаем: 1. entities - массив всех таблиц,
// что будут находиться в этой БД, 2. версия БД, при первом создании обычно 1, если далее производили
//изменения каких либо полей или таблиц, номер версии нужно повышать
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var instance: NoteDatabase? = null // переменная - объект базы данных
        fun getInstance(application: Application): NoteDatabase {
            if (instance == null) {
                //создание объекта БД через Room.databaseBuilder. В параметры передаем: 1. контекст приложения,
                //2. наш класс БД, 3. имя БД
                instance = Room.databaseBuilder(application, NoteDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries().build()
            }
            return instance!!
        }
    }

    // абстрактный метод, для возврата экземпляра интерфейса NotesDao - room сгенерирет реализацию интерфейса
    abstract fun notesDao(): NotesDao
}