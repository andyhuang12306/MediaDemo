package com.andy.videotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

/**
 * Created by andy on 2017/9/28.
 */

public class AudioRecordActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaRecorder mMediaRecorder;
    private boolean isRecording=false;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        findViewById(R.id.start_record).setOnClickListener(this);
        findViewById(R.id.start_play).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.start_record:
                if(!isRecording){
                    startRecord();
                }else {
                    stopRecord();
                }
                break;
            case R.id.start_play:
                startPlay();
                break;
        }
    }

    private void stopRecord() {
        if(mMediaRecorder!=null){
            mMediaRecorder.release();
        }
    }

    private void startPlay() {
        if(mMediaPlayer==null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.3pg");
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }else {
                mMediaPlayer.pause();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecord() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10001);
        }else {
            record();
        }
    }

    private void record() {
        if(null==mMediaRecorder){
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setAudioChannels(1);
        }
        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.3pg");
        try {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10002);
            }else {
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                isRecording=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10001:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    record();
                }
                break;
            case 10002:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaRecorder!=null){
            mMediaRecorder.release();
            mMediaRecorder=null;
        }

        if(mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
        }

    }
}
