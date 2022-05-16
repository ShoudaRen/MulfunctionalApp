package com.example.mvvmtest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button to_do;
    private Button notes;
    private Button memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        notes = findViewById(R.id.notes);
        to_do = findViewById(R.id.to_do);
        memory = findViewById(R.id.memory);

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addNotes.class);
                launcher.launch(intent);
            }
        });

    }
//    private static final int TIME_EXIT=2000;
//    private long mBackPressed;
//
//    @Override
//    public void onBackPressed(){     //onBackPressed() 捕获后退键按钮back的信息
//        if(mBackPressed+TIME_EXIT>System.currentTimeMillis()){ //currentTimeMillis,返回毫秒级别的系统时间
//            super.onBackPressed();
//            return;
//        }else {
//            Toast.makeText(this,"再点击一次返回退出程序", Toast.LENGTH_SHORT
//            ).show();
//            mBackPressed=System.currentTimeMillis();
//        }
//
//
//
//    }


    private long lastback;
    @Override
    public void onBackPressed() {
        if (lastback == 0 || System.currentTimeMillis() - lastback > 2000) {
            Toast.makeText(this,"再点击一次返回退出程序", Toast.LENGTH_SHORT
            ).show();
            lastback = System.currentTimeMillis();
            return;
        }
        //直接返回到桌面
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }




    ActivityResultLauncher<Intent> launcher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });

}