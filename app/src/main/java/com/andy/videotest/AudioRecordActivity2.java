package com.andy.videotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.andy.videotest.audioplayer.AudioPlayer2;
import com.andy.videotest.audiorecord.AudioRecorder2;

/**
 * Created by andy on 2017/9/30.
 */

public class AudioRecordActivity2 extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AudioRecordActivity2";
    private AudioRecorder2 mAudioRecorder;
    private AudioPlayer2 mAudioPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record_2);
        findViewById(R.id.start_record).setOnClickListener(this);
        findViewById(R.id.stop_record).setOnClickListener(this);
        findViewById(R.id.play_record).setOnClickListener(this);

        init();
    }

    private void init() {
        mAudioRecorder = new AudioRecorder2();
        mAudioPlayer = new AudioPlayer2();
        Log.d(TAG, "初始化AudioPlayer, AudioRecorder 完成");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.start_record:
                Log.d(TAG, "Record线程开启中。。。");
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10008);
                    }else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "开始进入startRecord方法");

                                mAudioRecorder.startRecord();
                            }
                        }).start();
                        Log.d(TAG, "Record线程开启成功");
                    }
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "开始进入startRecord方法");

                            mAudioRecorder.startRecord();
                        }
                    }).start();
                    Log.d(TAG, "Record线程开启成功");
                }
                break;
            case R.id.stop_record:
                mAudioRecorder.stopRecord();
                mAudioRecorder.close();
                break;
            case R.id.play_record:
                mAudioPlayer.play();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10008:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "开始进入startRecord方法");
                            mAudioRecorder.startRecord();
                        }
                    }).start();
                    Log.d(TAG, "Record线程开启成功");
                }
                break;
        }
    }
}
