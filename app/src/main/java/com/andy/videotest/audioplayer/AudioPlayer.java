package com.andy.videotest.audioplayer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.andy.videotest.audiorecord.AudioRecorder;
import com.andy.videotest.wav.WavFileReader;

import java.io.IOException;

/**
 * Created by andy on 2017/9/29.
 */

public class AudioPlayer implements Player {

    public static final String TAG="AudioPlayer";
    public static final int DEFAULT_MEDIA_TYPE= AudioManager.STREAM_MUSIC;
    public static final int DEFAULT_AUDIO_MODE= AudioTrack.MODE_STREAM;
    private AudioTrack mAudioTrack;
    private boolean mIsPlayStarted=false;
    private WavFileReader mFileReader;
    private boolean mIsLoopExit=false;

    public AudioPlayer(WavFileReader reader){
        mFileReader=reader;
        init();
    }

    private void init() {
        int minBufferSize = AudioTrack.getMinBufferSize(AudioRecorder.DEFAULT_SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioRecorder.DEFAULT_DATA_FORMAT);
        if(minBufferSize==AudioTrack.ERROR_BAD_VALUE){
            Log.e(TAG, "Error: Bad value!");
            return;
        }
        mAudioTrack = new AudioTrack(DEFAULT_MEDIA_TYPE, AudioRecorder.DEFAULT_SAMPLE_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioRecorder.DEFAULT_DATA_FORMAT,
                minBufferSize, DEFAULT_AUDIO_MODE);
    }

    @Override
    public void play(){
        new AudioPlayTask().start();
        mIsLoopExit=true;
    }

    private void playAudio(byte[] data, int offSetSize, int size) {
        if(mIsPlayStarted){
            return;
        }
        if(null!=mAudioTrack){
            if(mAudioTrack.write(data, offSetSize, size)!=size){
               Log.e(TAG, "Error: Could not write all the samples to the device!");
            }
            mAudioTrack.play();
            mIsPlayStarted=true;
        }else {
            init();
            play();
        }
    }

    @Override
    public void stop() {
        if(!mIsPlayStarted){
            return;
        }
        if(null!=mAudioTrack&&mAudioTrack.getPlayState()==AudioTrack.PLAYSTATE_PLAYING){
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack=null;
            mIsPlayStarted=false;
        }
    }


    private class AudioPlayTask extends Thread{

        @Override
        public void run() {
            byte[] buffer=new byte[AudioRecorder.SAMPLES_PER_FRAME*2];
            while (mIsLoopExit&&mFileReader.readData(buffer, 0, buffer.length)>0){
                playAudio(buffer, 0, buffer.length);
                Log.d(TAG, "读取数据："+buffer.length);
            }
            AudioPlayer.this.stop();
            Log.d(TAG, "读取数据结束");
            mIsLoopExit=false;
            try {
                mFileReader.closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
