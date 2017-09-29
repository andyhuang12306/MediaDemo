package com.andy.videotest;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.andy.videotest.audioplayer.AudioPlayer;
import com.andy.videotest.audiorecord.AudioRecorder;
import com.andy.videotest.wav.WavFileReader;
import com.andy.videotest.wav.WavFileWriter;

import java.io.IOException;

/**
 * Created by andy on 2017/9/29.
 */

public class AudioRecordActivity extends AppCompatActivity implements View.OnClickListener
, AudioRecorder.OnAudioCapturedListener{

    public static final String FILE_PATH= Environment.getExternalStorageDirectory()+"/test.wav";
    private AudioRecorder mRecorder;
    private WavFileWriter mWavFileWriter;
    private AudioPlayer mAudioPlayer;
    private WavFileReader mWavFileReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        findViewById(R.id.start_record).setOnClickListener(this);
        findViewById(R.id.start_play).setOnClickListener(this);
        init();
    }

    private void init() {
        mRecorder = new AudioRecorder();
        mRecorder.setOnAudioCapturedListener(this);
        mWavFileWriter = new WavFileWriter();
        try {
            mWavFileWriter.openFile(FILE_PATH, AudioRecorder.DEFAULT_SAMPLE_RATE, AudioRecorder.DEFAULT_CHANNEL_CONFIG, AudioRecorder.DEFAULT_DATA_FORMAT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mWavFileReader = new WavFileReader();
        try {
            mWavFileReader.openFile(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAudioPlayer = new AudioPlayer(mWavFileReader);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.start_record:
                //开始录音
                mRecorder.startRecord();
                break;
            case R.id.start_play:

                //停止录音
                try {
                    mWavFileWriter.closeFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecorder.stopRecord();
                //播放录音
                mAudioPlayer.play();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAudioCaptured(byte[] data) {
        mWavFileWriter.writeData(data, 0, data.length);
    }
}
