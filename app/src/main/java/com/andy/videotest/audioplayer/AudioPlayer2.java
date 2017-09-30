package com.andy.videotest.audioplayer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by andy on 2017/9/30.
 */

public class AudioPlayer2 implements Player {

    private static final String TAG="AudioPlayer2";
    private static final int FREQUENCY=16000;
    private static final int CHANNEL= AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING=AudioFormat.ENCODING_PCM_16BIT;
    private static final String FILE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.wav";
    private AudioTrack mTrack;

    public void open(int musicLength){
        mTrack=new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY, AudioFormat.CHANNEL_CONFIGURATION_MONO, ENCODING, musicLength*2, AudioTrack.MODE_STREAM);
        Log.d(TAG, "初始化Track成功");
    }

    @Override
    public void play() {
        Log.d(TAG, "进入播放录音方法中。。。");
        File file=new File(FILE_PATH);
        int musicLength = (int) (file.length() / 2);
        short[] music=new short[musicLength];
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            int i=0;
            while(dis.available()>0){
                Log.d(TAG, "录音播放中...");
                music[i] = dis.readShort();
                i++;
            }
            dis.close();
            open(musicLength);
            mTrack.play();
            mTrack.write(music, 0, musicLength);
            mTrack.stop();
            Log.d(TAG, "录音播放结束。。。");
        } catch (Exception e) {
            Log.e(TAG, "异常："+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        mTrack.stop();
        Log.d(TAG, "点击了停止播放按钮");
    }
}
