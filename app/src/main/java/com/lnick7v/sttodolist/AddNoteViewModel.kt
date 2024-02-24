package com.lnick7v.sttodolist

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class AddNoteViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NoteDatabase.getInstance(application).notesDao()
    private var shouldCloseScreen = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable() // создание коллекции объектов disposable
    //private lateinit var disposable: Disposable  // создание объекта disposable в случае если он нужен
    //только один для VM. Методод subscribe возвращает объект типа disposable, используя методы
    // данного объекта, можно управлять ЖЦ подписки

    fun getCloseScreen(): LiveData<Boolean> = shouldCloseScreen


    //метод сохранения новой заметки в БД
    fun saveNote(note: Note) {
        //вариант с RxJava - вызываем метод добавления заметки в БД, если не подписаться (subscribe)
        // на объект Completable, что возвращает данный метод, то ничего происходить не будет, т.е.
        // subscribe запускает объект Completable в работу (метод add).
        //Добавление заметки (работа с БД) должно происходить в фоновом потоке,
        val disposable = notesDao.add(note)
            .delay(5, TimeUnit.SECONDS) // сначала добавиться заметка и после 5 сек паузы, выполнится остальной код
            .subscribeOn(Schedulers.io()) // метод subscribeOn указывает на каком потоке будет происходить
            // подписка, т.е. добавление заметки (add), для фонового указываем - Schedulers.io() (io - input/output)
            .observeOn(AndroidSchedulers.mainThread()) // метод определяет в каком потоке будет выполняться
            //код ниже него, для главного указываем - AndroidSchedulers.mainThread()
            .subscribe(object: Action { //если нас интересует завершилась работа метода (add) или нет,
                // то внутрь subscribe добавляем callback - объект анонимного класса интерфейса Action
                override fun run() { // метод вызывается если добавление (add) заметки прошло успешно
                    Log.d("AddNoteViewModel", "subscribe") //добавили лог перед закрытием Активити
                    shouldCloseScreen.value = true  // описываем поведение в случае успешного добавления заметки
                }
            // лаконичная запись метода .subscribe: .subscribe { shouldCloseScreen.value = true }
            // если допустим нам после кода выше нужно выполнить какие то действия на фоновом потоке
            // мы пишем .observeOn(Schedulers.io()) и в строках ниже пишем действия, так можно переключаться
            // сколько угодно раз
        })
        compositeDisposable.add(disposable) // добавляем объект disposable в коллекцию



        /*Thread {  ******* БЕЗ RxJava (с использованием Thread)
            notesDao.add(note)
            shouldCloseScreen.postValue(true) // менять значение MutableLiveData нельзя из
            // фонового потока, метод  .postValue, позволяет это сделать
        }.start()*/
    }

    // метод ЖЦ ViewModel - происходит при уничтожении VM
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose() // отменяем подписку
    }
}