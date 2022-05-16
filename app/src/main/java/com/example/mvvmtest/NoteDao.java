package com.example.mvvmtest;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
    @Query("Delete from note_table")
    void deleteAllnotes();

    @Query("SELECT * FROM note_table order by priority DESC")
    //当note_table的内容发生改变时，Livedata会自动更新（调用观察者），因此我们只要结果返回Livedata就可以了
    LiveData<List<Note>> getAllNotes();
}
