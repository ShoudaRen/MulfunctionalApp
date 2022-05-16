package com.example.mvvmtest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class addNotes extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDit_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //页面之间传递
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addNotes.this, AddEditeNoteActivity.class);
                launcher.launch(intent);
            }
        });

        //初始化recycleView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        //创建ViewModel
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        //创建 livedata对象 储存数据
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerVIew/ Update the UI
                adapter.submitList(notes);
            }
        });
        /**
         * ItemTouchHelper是一个工具类，可实现侧滑删除和拖拽移动
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                adapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //viewHolder.getAdapterPosition() 返回当前viewholder的位置
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(addNotes.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        /**
         * 更改数据首先要在adpater上实现
         */
        adapter.setOnClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Note note) {
                Intent intent =  new Intent(addNotes.this, AddEditeNoteActivity.class);
                intent.putExtra(AddEditeNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditeNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditeNoteActivity.EXTRA_DES,note.getDescription());
                intent.putExtra(AddEditeNoteActivity.EXTRA_PRIORITY,note.getPriority());
                launcher2.launch(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null) {
                        String title = result.getData().getStringExtra(AddEditeNoteActivity.EXTRA_TITLE);
                        String des = result.getData().getStringExtra(AddEditeNoteActivity.EXTRA_DES);
                        int priority = result.getData().getIntExtra(AddEditeNoteActivity.EXTRA_PRIORITY, 1);
                        Note note = new Note(title, des, priority);
                        noteViewModel.insert(note);
                        Toast.makeText(addNotes.this, "Note saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(addNotes.this, "Note not save", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    ActivityResultLauncher<Intent> launcher2 = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                   Intent intent = result.getData();
                    if (intent != null) {
                        int id =intent.getIntExtra(AddEditeNoteActivity.EXTRA_ID,-1);
                        if (id==-1){
                            Toast.makeText(addNotes.this, "Note cann't be updated", Toast.LENGTH_SHORT).show();
                            //什么都不做 return
                            return;
                        }
                        String title = result.getData().getStringExtra(AddEditeNoteActivity.EXTRA_TITLE);
                        String des = result.getData().getStringExtra(AddEditeNoteActivity.EXTRA_DES);
                        int priority = result.getData().getIntExtra(AddEditeNoteActivity.EXTRA_PRIORITY, 1);
                        Note note = new Note(title, des, priority);
                        note.setId(id);
                        noteViewModel.update(note);
                        Toast.makeText(addNotes.this, "Note updated", Toast.LENGTH_SHORT).show();

                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_ALL_notes:
                noteViewModel.deleteAll();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ic_home:
                goHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        launcher3.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher3 = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });
}
