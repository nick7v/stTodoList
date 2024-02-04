package com.lnick7v.sttodolist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


//описание интерфейса и его методов для работы с БД, обычно такие интерфейсы называют DAO - Data Access Object
//аннотация Dao - room генерирует реализацию интерфейса
@Dao
interface NotesDao {
    @Query("SELECT * FROM notes") //аннотация к методу getNotes() - запрос в БД ("выбрать все из таблицы notes")
    fun getNotes(): List<Note>


    // аннотация к методу add() - вставка объекта в таблицу, запрос писать не нужно, room это реализует сам
    //по желанию можно указать тип поведения БД onConflict - что делать если запись с таким id уже есть? - здесь мы ее заменяем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(note: Note)

    //аннотация к remove() - запрос в БД ("удалить все из таблицы notes, где id = id (из параметров метода)")
    // т.е. удалит 1 строчку из таблицы где id совпадет с id что передалось в параметре remove()
    @Query("DELETE FROM notes WHERE id = :id")
    fun remove(id: Int)
}