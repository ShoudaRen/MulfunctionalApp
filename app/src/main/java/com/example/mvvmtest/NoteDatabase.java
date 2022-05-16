package com.example.mvvmtest;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//更改数据要升级数据库的版本  从1开始
@Database(entities = {Note.class},version=1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;
   //调用Dao层的接口
    public abstract NoteDao noteDao();
   //只需要对getInstance方法加锁，那么在一个时间点只能有一个线程能够进入该方法，从而避免了实例化多次Instance。
    public static synchronized NoteDatabase getInstance(Context context){
        if (instance ==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"note_datebase")
                    .addCallback(roomCallBack)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


    //创建表格/数据
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //真实创建
            new PopulateDbAsynTask(instance).execute();
        }
    };


    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private PopulateDbAsynTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1 ","Des 1",1));
            noteDao.insert(new Note("Title 2 ","Des 2",2));
            noteDao.insert(new Note("Title 3 ","Des 3",3));
            return null;
        }
    }
}
