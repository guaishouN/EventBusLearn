package com.guaishou.eventbuslearnapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.guaishou.eventbuslibrary.EventBus;
import com.guaishou.eventbuslibrary.Subsribe;
import com.guaishou.eventbuslibrary.ThreadMode;
import com.guaishou.gysologlib.GuaishouLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GuaishouLog.setTag("test-event");
        GuaishouLog.print("MainActivity onCreate");
        EventBus.getDefualt().register(this);
    }

    @Subsribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Book book){
        GuaishouLog.print("MainActivity onEvent = "+book.name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onMainClick(View view) {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
}
