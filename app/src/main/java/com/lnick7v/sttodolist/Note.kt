package com.lnick7v.sttodolist

import androidx.room.Entity
import androidx.room.PrimaryKey

//анотация @Entity делает из класса таблицу со столбцами свойств класса (id, text, priority).
// Параметр tableName отвечает за название таблицы, которая будет создана на устройстве пользователя
@Entity (tableName = "notes")
// аннотация @PrimaryKey сообщает что столбец id является "Первичным ключем", т.е. теперь id должно быть
// уникально для каждой записи. В таблице может быть только 1 запись с определенным id,
//при попытке добавить запись с таким же id, то в зависимости от настроек поведения таблицы, она либо
//не будет добавлена, либо будет добавлена, а старая удалится. Параметр autoGenerate, отвечает
// генерировать ли первичный ключ автоматически или нет
class Note (@field:PrimaryKey(autoGenerate = true) val id: Int, val text: String, val priority: Int)