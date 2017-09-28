package com.andy.videotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.drag);
        tv.setOnClickListener(this);
        findViewById(R.id.audio_record).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.drag:
                startActivity(new Intent(this, SurfaceViewDragActivity.class));
                break;
            case R.id.audio_record:
                startActivity(new Intent(this, AudioRecordActivity.class));
                break;
        }
    }
}
