package com.lnick7v.sttodolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single


//описание интерфейса и его методов для работы с БД, обычно такие интерфейсы называют DAO - Data Access Object
//аннотация Dao - room генерирует реализацию интерфейса
@Dao
interface NotesDao {
    @Query("SELECT * FROM notes") //аннотация к методу getNotes() - запрос в БД ("выбрать все из таблицы notes")
    //fun getNotes(): LiveData<List<Note>> //Помещаем объект List<Note> в LiveData, чтобы можно было
    // где либо в приложении подписаться на изменения объекта. Room под капотом реализует отправку измененных данных.
    //при помещении объекта в LiveDate запрос в БД будет АВТОМАТИЧЕСКИ ПРОИСХОДИТЬ В ФОНОВОМ ПОТОКЕ

    //fun getNotes(): Single<List<Note>> // пример работы класса Single RxJava, мы так же можем на него подписаться,
    //при этом в отличии от Completable, он может возвращать данные, в примере List<Note>

    fun getNotes(): List<Note>  //пример для дальнейшего самостоятельного создания объектов классов RxJava



    // аннотация к методу add() - вставка объекта в таблицу, запрос писать не нужно, room это реализует сам
    //по желанию можно указать тип поведения БД onConflict - что делать если запись с таким id уже есть? - здесь мы ее заменяем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(note: Note):Completable  //в RxJava есть множество классов, на объекты которых мы можем подписаться
    // если от объекта RxJava нам нужно только знать завершилась работа или нет мы
    // устанаваливаем возвращаемый тип метода - Completable

    //fun add(note: Note) //БЕЗ RxJava (с использованием Thread)


    //аннотация к remove() - запрос в БД ("удалить все из таблицы notes, где id = id (из параметров метода)")
    // т.е. удалит 1 строчку из таблицы где id совпадет с id что передалось в параметре remove()
    @Query("DELETE FROM notes WHERE id = :id")
    fun remove(id: Int)//: Completable // RxJava
}