package com.andy.videotest.audiorecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by andy on 2017/9/29.
 */

public class AudioRecorder implements Recorder{

    public static final String TAG="AudioRecorder";

    public static final int DEFAULT_SOURCE= MediaRecorder.AudioSource.MIC;
    public static final int DEFAULT_SAMPLE_RATE=4410;
    public static final int DEFAULT_CHANNEL_CONFIG=AudioFormat.CHANNEL_IN_MONO;
    public static final int DEFAULT_DATA_FORMAT= AudioFormat.ENCODING_PCM_16BIT;
    public static final int SAMPLES_PER_FRAME=1024;

    private AudioRecord mAudioRecord;
    private OnAudioCapturedListener mCapturedListener;

    private boolean mIsRecording=false;
    private boolean mIsLooping=false;

    public AudioRecorder(){
        init();
    }

    public AudioRecorder(int sampleRate, int channelConfig, int dataForamt){
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, dataForamt);
        if(minBufferSize==AudioRecord.ERROR_BAD_VALUE){
            Log.e(TAG, "Invalid parameter!");
            return;
        }
        mAudioRecord=new AudioRecord(DEFAULT_SOURCE, sampleRate, channelConfig, dataForamt, minBufferSize);
    }

    private void init() {
        int minBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_DATA_FORMAT);
        if(minBufferSize==AudioRecord.ERROR_BAD_VALUE){
            Log.e(TAG, "Invalid parameter!");
            return;
        }
        mAudioRecord = new AudioRecord(DEFAULT_SOURCE, DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_DATA_FORMAT, minBufferSize);
    }


    @Override
    public void startRecord() {
        if(mIsRecording){
            return;
        }
        if(null!=mAudioRecord){
            mAudioRecord.startRecording();
            new RecordTask().start();
            mIsRecording=true;
            mIsLooping=true;
        }else {
            init();
            startRecord();
        }
    }

    @Override
    public void stopRecord() {
        if(!mIsRecording){
            return;
        }
        if(null!=mAudioRecord){
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord=null;
            mIsLooping=false;
            mIsRecording=false;
        }
    }

    private class RecordTask extends Thread{

        @Override
        public void run() {
            byte[] bytes=new byte[SAMPLES_PER_FRAME*2];
            while (mIsLooping){
                int read = mAudioRecord.read(bytes, 0, bytes.length);
                if(read==AudioRecord.ERROR_INVALID_OPERATION){
                    Log.e(TAG, "Error: Invalid operation!");
                    return;
                }else if(read==AudioRecord.ERROR_BAD_VALUE){
                    Log.e(TAG, "Error: Bad value!");
                    return;
                }else{
                    Log.d(TAG, "Audio captured: length is:"+bytes.length);
                    if(null!=mCapturedListener){
                        mCapturedListener.onAudioCaptured(bytes);
                    }
                }
            }
        }
    }

    public interface OnAudioCapturedListener{
        void onAudioCaptured(byte[] data);
    }

    public void setOnAudioCapturedListener(OnAudioCapturedListener listener){
        mCapturedListener=listener;
    }
}
