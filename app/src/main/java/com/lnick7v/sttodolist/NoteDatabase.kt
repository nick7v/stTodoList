package com.lnick7v.sttodolist

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_NAME = "notes.db"  // const для хранения имени БД, имя и расширение могут быть любыми

//реализует БД SQLite посредством room. Абстрактный класс, наследуется от RoomDatabase, подкласс абстракного класса
//библиотека room создаст под капотом. В параметры анотации передаем: 1. entities - массив всех таблиц,
// что будут находиться в этой БД, 2. версия БД, при первом создании обычно 1, если далее производили
//изменения каких либо полей или таблиц, номер версии нужно повышать. 3. необяз-ый параметр exportSchema=false -
// хранить ли историю изменеия версий БД на устройстве (помогает, если ошибки при запуске)
@Database(entities = [Note::class], version = 1) //exportSchema здесь не указан, по умолчанию он true
abstract class NoteDatabase: RoomDatabase() {
    companion object {
        private var instance: NoteDatabase? = null // переменная - объект базы данных
        // реализуем свой паблик метод для получения экземпляра БД, в аргумент получаем контекст приложения
        fun getInstance(application: Application): NoteDatabase {
            if (instance == null) {
                //создание объекта БД через Room.databaseBuilder. В параметры передаем: 1. контекст приложения,
                //2. наш класс БД, 3. имя БД
                instance = Room.databaseBuilder(application, NoteDatabase::class.java, DB_NAME)
                    .build()
            }
            return instance!!  // возвращаем БД
        }
    }

    // абстрактный метод, для возврата экземпляра интерфейса NotesDao - room сгенерирет реализацию интерфейса
    abstract fun notesDao(): NotesDao
}