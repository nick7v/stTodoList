package com.lnick7v.sttodolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: ArrayList<Note>): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    fun setNotes(notes: ArrayList<Note>) {
        this.notes = notes
        //notifyDataSetChanged() // сообщаем адаптеру что данные изменились, чтобы он обновился.
        // Но у меня все работает и без выхова данного метода, из-за того что я при вызове адаптера передаю ему Database
    }



    //в методе нужно указать как создавать View (или даже набор View, если у нас макет xml содержит
    // не один View, а несколько) из layout xml файла. Метод возвращает ViewHolder
    // (держатель View) по сути это тот же View, но уже конкретизированный. В методе мы получаем
    // 10-12 (сколько отображается на экране) View объектов из макета xml и далее, передавая
    // эти view в качестве параметра в класс NotesViewHolder, мы получаем и тут же возвращаем
    // 10-12 ViewHolder-ов
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        // контекст получаем из ViewGroup на котором наш view будет отображаться - parent
        // но его можно так же получать у любого view элемента из этой ViewGroup
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NotesViewHolder(view)
    }



    //метод отвечает за обновление атрибутов View элементов при первом формировании и прокрутке экрана,
    // в качестве параметра получает ViewHolder, которые создавались в onCreate... и номер позиции элемента
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position] // по номеру позиции получаем заметку из массива, которую нужно отобразить

        holder.textViewNote.text = note.text

        val colorResId = when (note.priority) {
            0 -> android.R.color.holo_green_light
            1 -> android.R.color.holo_orange_light
            else -> android.R.color.holo_red_light
        }
        holder.textViewNote.background = ContextCompat.getDrawable(holder.itemView.context, colorResId)
    }



    //позволяет RecyclerView определить общее количество элементов в списке
    override fun getItemCount(): Int {
        return notes.size
    }


    // описываем класс ViewHolder - для последующего создания объектов - ViewHolder-ов,
    // которые по сути являются конкретизированными View объектами (или даже наборами View объектов)
    // для наших элементов списка на экране. Внутри класса мы указываем конкретные view, что имеются
    // на xml макете, и к которым нам далее нужен будет доступ через ViewHolder. В примере на макете
    // только 1 View. Будет создано примерно 10-12 объектов класса (по кол-ву элементов отображаемых на экране)
    class NotesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var textViewNote: TextView = view.findViewById<TextView>(R.id.textViewNote)
    }

}
