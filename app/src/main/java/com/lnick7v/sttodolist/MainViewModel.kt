package com.lnick7v.sttodolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Callable

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val noteDatabase = NoteDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    private var notes: MutableLiveData<List<Note>> = MutableLiveData() // код для примера работы объекта класса Single RxJava

    fun getNotes(): LiveData<List<Note>> {
        return notes  // код для примера работы объекта класса Single RxJava
        //return noteDatabase.notesDao().getNotes() //Room без RxJava - действительный код
    }

    //***** блок кода для примера работы объекта класса Single RxJava
    // т.к. в примере LiveData больше не используется в Room, нам нужен этот метод, для обновления
    //списка заметок в MainActivity после его изменения
    fun refreshList() {
        //val disposable = noteDatabase.notesDao().getNotes()  //правильный код
        val disposable = getNotesRx() //строка для примера самостоятельного создания объектов классов RxJava
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            // в отличии от объекта класса Completable, в объект Single прилетают загруженные данные
            // после успешного выполнения операции мы их берем в подписке из объекта Consumer из параметров
            // метода accept и уже далее используем эти данные
            .subscribe(object: Consumer<List<Note>>{
                override fun accept(notesFromDb: List<Note>) {
                    notes.value = notesFromDb
                }
                //блок обработки исключений, если при работе метода getNotesRx(): Single<List<Note>> произошло исключение
            }, object: Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    Log.d("MainViewModel", "Error refreshList()") // описываем действия в случае исключения
                }
            })
        // лаконичная запись:
        // .subscribe({ notesFromDb -> notes.value = notesFromDb }
        // ) { Log.d("MainViewModel", "Error refreshList()") }
        compositeDisposable.add(disposable)
    }
    //******

    //***** блок кода для примера самостоятельного создания объектов класса Single RxJava, в случае
    // если Room их не реализовывает, а нам нужно выполнять какие то операции в разных потоках
    fun getNotesRx(): Single<List<Note>> {
        return Single.fromCallable(object: Callable<List<Note>> {
            override fun call(): List<Note> {
                return noteDatabase.notesDao().getNotes() //именно эту операцию далее в подписке мы можем выполнить в другом потоке
            }
        } ) //лаконичная запись return Single.fromCallable { noteDatabase.notesDao().getNotes() }
    }
    //*****

    fun remove(note: Note) {
        //Thread { noteDatabase.notesDao().remove(note.id) }.start() ******* БЕЗ RxJava (с использованием Thread)

        //Реализация работы метода с RxJava - подробное описание см. в классе AddNoteViewModel
        //val disposable = noteDatabase.notesDao().remove(note.id)
        val disposable = removeRx(note) //строка для примера самостоятельного создания объектов классов RxJava
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("MainViewModel", "Removed: note with id ${note.id}")
                refreshList() // код для примера работы объекта класса Single RxJava
            }) { Log.d("MainViewModel", "Error remove()") } // это лаконичная запись object: Consumer<Throwable> { override fun accept(t: Throwable)
        compositeDisposable.add(disposable)
    }

    //***** блок кода для примера самостоятельного создания объектов класса Completable RxJava, в случае
    // если Room их не реализовывает, а нам нужно выполнять какие то операции в разных потоках
    fun removeRx(note: Note): Completable { // функция, которая возвращает объект Completable RxJava
        return Completable.fromAction(object: Action {
            override fun run() {
                noteDatabase.notesDao().remove(note.id) //именно эту операцию далее в подписке мы можем выполнить в другом потоке
            }
        }) // лаконичная запись return Completable.fromAction { noteDatabase.notesDao().remove(note.id) }
    }
    //*****

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}