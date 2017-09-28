package com.andy.videotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by andy on 2017/9/28.
 */

public class SurfaceViewDragActivity extends AppCompatActivity implements View.OnTouchListener{

private SurfaceView   mSurfaceView;
private SurfaceHolder mHolder;
private Bitmap        mBitmap;
private boolean canDrag =false;
private Rect    mRect   =new Rect(0, 0, 500, 500);
private boolean running =true;
private Point   mPoint  =new Point();
private int     offsetX =0, offsetY =0;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mHolder = mSurfaceView.getHolder();
        mSurfaceView.setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(new TodoThings());
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mSurfaceView.setOnTouchListener(this);
        }

private class TodoThings implements SurfaceHolder.Callback {

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Toast.makeText(SurfaceViewDragActivity.this, "created===", Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run() {
                while (running){
                    Canvas canvas = mHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE);
                    Paint  paint  =new Paint();
                    canvas.drawBitmap(mBitmap, mRect.left, mRect.top, paint);
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Toast.makeText(SurfaceViewDragActivity.this, "changed===", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Toast.makeText(SurfaceViewDragActivity.this, "destoryed===", Toast.LENGTH_SHORT).show();
        running=false;
    }

}

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoint.x = (int) motionEvent.getX();
                mPoint.y = (int) motionEvent.getY();
                if (mRect.contains(mPoint.x, mPoint.y)) {
                    canDrag = true;
                    offsetX = mPoint.x - mRect.left;
                    offsetY = mPoint.y - mRect.top;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (canDrag) {
                    mRect.left = (int) motionEvent.getX() - offsetX;
                    mRect.top = (int) motionEvent.getY() - offsetY;
                    mRect.right = mRect.left + mBitmap.getWidth();
                    mRect.bottom = mRect.top + mBitmap.getHeight();
                    if (mRect.left < 0) {
                        mRect.left = 0;
                        mRect.right = mRect.left + mBitmap.getWidth();
                    }
                    if (mRect.right > getMessuredWidth()) {
                        mRect.right = getMessuredWidth();
                        mRect.left = mRect.right - mBitmap.getWidth();
                    }
                    if (mRect.top < 0) {
                        mRect.top = 0;
                        mRect.bottom = mRect.top + mBitmap.getHeight();
                    }
                    if (mRect.bottom > getMessuredHeight()) {
                        mRect.bottom = getMessuredHeight();
                        mRect.top = getMessuredHeight() - mBitmap.getHeight();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                canDrag=false;
                break;
        }
        return true;
    }


    private int getMessuredWidth() {
        return ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public int getMessuredHeight() {
        return ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }
}
