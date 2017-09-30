package com.andy.videotest.audiorecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by andy on 2017/9/30.
 */

public class AudioRecorder2 implements Recorder {

    private static final String TAG       = "AudioRecorder2";
    private boolean isRecording           =false;
    public static final int FREQUENCY     =16000;
    public static final int CHANNEL       = AudioFormat.CHANNEL_IN_MONO;
    public static final int ENCODING      =AudioFormat.ENCODING_PCM_16BIT;
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.wav";
    private AudioRecord mRecord;

    public boolean open(int bufferSize){
        mRecord=new AudioRecord(MediaRecorder.AudioSource.DEFAULT, FREQUENCY, CHANNEL, ENCODING, bufferSize);
        if(mRecord.getState()==AudioRecord.STATE_INITIALIZED){
            Log.d("AudioRecord2", "audiorecord initialized");
            mRecord.startRecording();
            Log.d(TAG, "开启录音录制功能成功。");
            return true;
        }
        Log.d(TAG, "开启录音录制功能失败。");
        return false;
    }

    public void close(){
        if(mRecord!=null){
            if(mRecord.getState()==AudioRecord.STATE_INITIALIZED){
                mRecord.stop();
            }
            mRecord.release();
            mRecord=null;
        }
    }

    @Override
    public void startRecord() {
        Log.d(TAG, "startRecord方法开始。。。。");
        File file=creatFile();
        int  minBufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
        if(!open(minBufferSize)){
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
            short[] buffer=new short[minBufferSize];
            mRecord.startRecording();
            isRecording=true;
            while (isRecording){
                int read = mRecord.read(buffer, 0, minBufferSize);
                Log.d(TAG, "循环录制进行中，当前大小："+buffer.length);
                for(int i=0; i<read; i++){
                    dataOutputStream.writeShort(buffer[i]);
                }
            }
            Log.d(TAG, "循环录制结束");
            mRecord.stop();
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopRecord() {
        isRecording=false;
        Log.d(TAG, "点击了停止录制按钮");
    }

    private File creatFile() {
        File file = new File(FILE_PATH);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            Log.d(TAG, "文件创建成功，路径为："+file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "文件创建失败："+e.toString());
        }
        return file;
    }
}
