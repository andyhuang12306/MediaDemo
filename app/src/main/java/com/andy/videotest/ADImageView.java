package com.andy.videotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by andy on 2017/9/28.
 */

public class ADImageView extends android.support.v7.widget.AppCompatImageView {
    Paint mPaint=new Paint();
    Bitmap mBitmap;
    public ADImageView(Context context) {
        super(context);
        init();
    }

    public ADImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ADImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null!=mBitmap){
            canvas.drawBitmap(mBitmap, 200, 200, mPaint);
        }
    }
}
