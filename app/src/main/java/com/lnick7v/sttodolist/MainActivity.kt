package com.lnick7v.sttodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAddNote: FloatingActionButton
    private lateinit var recyclerViewNotes: RecyclerView   //строка для последующего доступа к RV
    private lateinit var notesAdapter: NotesAdapter //переменная для доступа к адаптеру
    private lateinit var viewModel: MainViewModel

    /*//!!!!!!! НЕ НУЖЕН после добавления LiveData !!!!!!!!!!!
    private val handler = Handler(Looper.getMainLooper()) // инициализируем объект Handler, который хранит ссылку на главный поток (ее мы передали в параметры)
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MainViewModel(application) // создание объекта ViewModel, в параметры передаем контекст приложения
        initViews()
        notesAdapter = NotesAdapter() //присваивание переменной объект адаптера

        //устанавливаем ClickListener на адаптер (точнее передаем адаптеру слушатель), который в свою очередь
        // у себя в коде устанаваливает слушатель на элементы списка. Сеттер вызывается 1 раз,
        // а метод внутри него onNoteClick каждый раз при нажатии на элемент списка
        //!!!!!!!!!!!!  удаление нажатием пока отключил, заменил на свайп
       /* notesAdapter.setOnNoteClickListener(object : NotesAdapter.OnNoteClickListener
						{
            override fun onNoteClick(note: Note) {
                viewModel.remove(note)
            }
        })*/


        recyclerViewNotes.adapter = notesAdapter // устанавливаем адаптер для RV
        //recyclerViewNotes.layoutManager = LinearLayoutManager(this) // устанавливаем LO manager для указания
        // формата отображения RW - здесь в проекте это прописал в xml


        //получив от БД посредством метода getNotes объект LiveData, используя метод observe() мы подписываемся
        // на обновления объекта, что хранит LiveData. В параметры observe передаем: 1. объект с интерфесом
        // LifecycleOwner (т.е. объект у которого есть ЖЦ - Activity, фрагменты....) - this - MainActivity
        //2. это непосредственно подписчик, т.е. колбэк, в который LiveData будет отправлять данные, это
        // наш объект Observer, в нем только один метод onChanged. Итого если произошли изменения в БД и
        // если Activity при этом было активно, у Observer вызовется метод onChanged, в параметры которого
        // прилетят изменения, которые мы в теле метода уже установим в адаптер
        viewModel.getNotes().observe(this, object: Observer<List<Note>> {
            override fun onChanged(value: List<Note>) {
                notesAdapter.setNotes(value)
            }
        } )//!!!!!!блок кода выше можно заменить более лаконичным кодом (выше показано, чтобы было понятно объяснение):
        /* noteDatabase.notesDao().getNotes().observe(this, Observer { notes ->
             notesAdapter.setNotes(notes)
         })*/


        // описываеем itemTouch для реализации удаления элемента из RV свайпом. В конструктор SimpleCallback()
        // нужно передать 2 параметра: 1. направление перемещения, т.к. не испуользуем = 0   2. направление свайпа
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            //вызывается при перемещении элемента с одного места на другое, нам это не нужно, возвращаем - false
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //вызывается при свайпе
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // получаем N позиции элемента RV из ViewHoldera
                val note = notesAdapter.getNotes()[position] // получаем массив заметок из адаптера и по позиции получаем элемент с нужным индексом
                viewModel.remove(note) // удаляем строку из БД через ViewModel

                /*Thread { // в новом фоновом потоке // !!!!!! НЕ НУЖЕН после добавления ViewModel
                    noteDatabase.notesDao().remove(note.id) // сначала удаляем строку из БД c указанным id
                    */ /*  //!!!!!!! НЕ НУЖЕН после добавления LiveData !!!!!!!!!!!
                    handler.post { showNotes() } // после отправляем handler-у главного потока сообщение
                    чтобы он в своем потоке (главном) вызвал метод showNotes(), т.е. обновил БД в адаптере
                    RV (т.е. обновляем список заметок)\
                    */ /*
                }.start()  // запускаем фоновый поток*/
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes) // прикрепляем itemTouch к RV


        buttonAddNote.setOnClickListener {
            startActivity(AddNoteActivity.newIntent(this))
        }
    }

    private fun initViews() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
        buttonAddNote = findViewById(R.id.buttonAddNote)
    }


    /*    override fun onResume() { //!!!!!!! НЕ НУЖЕН после добавления LiveData !!!!!!!!!!!
            super.onResume()
            showNotes()
        }*/


    /*private fun showNotes() {  //!!!!!!! НЕ НУЖЕН после добавления LiveData !!!!!!!!!!!
        Thread { // в новом фоновом потоке
            val notes: List<Note> = noteDatabase.notesDao().getNotes() // сначала получаем БД
            handler.post { notesAdapter.setNotes(notes)  } // после отправляем handler-у главного потока сообщение чтобы он
            // в своем потоке (главном) установил (обновил) БД в адаптер RV (т.е. обновляем список заметок)
        }.start()
    }*/
}