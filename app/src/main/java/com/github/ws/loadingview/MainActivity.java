package com.github.ws.loadingview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ws.loadingview.app.LoadingView;

public class MainActivity extends AppCompatActivity {


    private  LoadingView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv= (LoadingView) findViewById(R.id.lv);
        lv.setBallColor(Color.parseColor("#303F9F"));
        lv.setBallNumber(6);
    }
}
