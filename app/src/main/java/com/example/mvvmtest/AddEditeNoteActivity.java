package com.example.mvvmtest;
//所对应的layout
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditeNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.mvvmtest.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mvvmtest.EXTRA_TITLE";
    public static final String EXTRA_DES = "com.example.mvvmtest.EXTRA_DES";
    public static final String EXTRA_PRIORITY = "com.example.mvvmtest.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDes;
    private NumberPicker numberPickerPriority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextDes=findViewById(R.id.edit_Des);
        editTextTitle=findViewById(R.id.edit_text_title);
        numberPickerPriority=findViewById(R.id.numberPicker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            //在空白添上数据
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDes.setText(intent.getStringExtra(EXTRA_DES));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }else {
            setTitle("ADD Note");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_but:
                saveButton();
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
        launcher.launch(intent);
    }

    private void saveButton() {
        String title = editTextTitle.getText().toString();
        String Des = editTextDes.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty()||Des.trim().isEmpty()){
            Toast.makeText(this, "please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 把想要传递的数据暂存在intent中，当另一个活动启动后，再把这些数据从intent缓存中取出即可。
         * putExtra("A", B)方法中，AB为键值对，第一个参数为键名，第二个参数为键对应的值，这个值才是真正要传递的数据
         */
        Intent data  = new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DES,Des);
        data.putExtra(EXTRA_PRIORITY,priority);

        int id =getIntent().getIntExtra(EXTRA_ID,-1);
        if (id!=-1){
            data.putExtra(EXTRA_ID,id);
        }

        //传数据时采用setResult方法，并且之后要调用finish方法。
        setResult(RESULT_OK,data);
        finish();

    }



    ActivityResultLauncher<Intent> launcher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });
}