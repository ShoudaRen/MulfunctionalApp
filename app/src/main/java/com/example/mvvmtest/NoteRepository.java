package com.example.mvvmtest;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void  insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void updata(Note note){
        new updatetNoteAsyncTask(noteDao).execute(note);
    }
    public void  delete(Note note){
        new deleteNoteAsyncTask(noteDao).execute(note);
    }
    public void  deleteAllNotes(){
        new deleteAllNoteAsyncTask(noteDao).execute();
    }
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }





    /**
     *
     *
     *
     *
     *   用来执行异步操作的方法，代替thread和handle;
     */
    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        //插入具体方法的实现,这个方法通过execute()调用来自动执行
        protected Void doInBackground(Note... notes) {
            //尽管参数是一个数组但只传递一个note
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class updatetNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        private updatetNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    private static class deleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao noteDao;
        private deleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class deleteAllNoteAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;
        private  deleteAllNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllnotes();
            return null;
        }
    }

}
