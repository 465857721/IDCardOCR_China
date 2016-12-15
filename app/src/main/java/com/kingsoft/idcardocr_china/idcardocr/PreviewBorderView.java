package com.kingsoft.idcardocr_china.idcardocr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kingsoft.idcardocr_china.R;


public class PreviewBorderView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String DEFAULT_TIPS_TEXT = "请将身份证照片面置于框内扫描，并尽量对齐边框";
    private static final int DEFAULT_TIPS_TEXT_SIZE = 18;
    private static final int DEFAULT_TIPS_TEXT_COLOR = Color.GREEN;
    private int mScreenH;
    private int mScreenW;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mPaintLine;
    private SurfaceHolder mHolder;
    private Thread mThread;
    /**
     * 自定义属性
     */
    private float tipTextSize;
    private int tipTextColor;
    private String tipText;

    public PreviewBorderView(Context context) {
        this(context, null);
    }

    public PreviewBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    /**
     * 初始化自定义属性
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreviewBorderView);
        try {
            tipTextSize = a.getDimension(R.styleable.PreviewBorderView_tipTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIPS_TEXT_SIZE, getResources().getDisplayMetrics()));
            tipTextColor = a.getColor(R.styleable.PreviewBorderView_tipTextColor, DEFAULT_TIPS_TEXT_COLOR);
            tipText = a.getString(R.styleable.PreviewBorderView_tipText);
            if (tipText == null) {
                tipText = DEFAULT_TIPS_TEXT;
            }
        } finally {
            a.recycle();
        }


    }

    /**
     * 初始化绘图变量
     */
    private void init() {
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);
        setZOrderMediaOverlay(true);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPaintLine = new Paint();
        this.mPaintLine.setColor(Color.WHITE);
        this.mPaintLine.setStrokeWidth(3.0F);
        setKeepScreenOn(true);
    }

    /**
     * 绘制取景框
     */
    private void draw() {
        try {
            this.mCanvas = this.mHolder.lockCanvas();
            this.mCanvas.drawARGB(100, 0, 0, 0);
//            this.mScreenW = (this.mScreenH * 4 / 3);
            Log.e("TAG", "mScreenW:" + mScreenW + " mScreenH:" + mScreenH);
            this.mCanvas.drawRect(new RectF(this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH * 1 / 6, this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6), this.mPaint);
            this.mCanvas.drawLine(this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH * 1 / 6, this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH * 1 / 6 + 150, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH * 1 / 6, this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6 + 150, this.mScreenH * 1 / 6, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH * 1 / 6, this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH * 1 / 6 + 150, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH * 1 / 6, this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6 - 150, this.mScreenH * 1 / 6, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6, this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6 - 150, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6, this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6 + 150, this.mScreenH - this.mScreenH * 1 / 6, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6, this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6 - 150, this.mPaintLine);
            this.mCanvas.drawLine(this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6, this.mScreenH - this.mScreenH * 1 / 6, this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6 - 150, this.mScreenH - this.mScreenH * 1 / 6, this.mPaintLine);

            mPaintLine.setTextSize(tipTextSize);
            mPaintLine.setAntiAlias(true);
            mPaintLine.setDither(true);
            float length = mPaintLine.measureText(tipText);

            this.mCanvas.drawText(tipText, this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6 + this.mScreenH / 2 - length / 2, this.mScreenH * 1 / 2 - tipTextSize, mPaintLine);
            Log.e("TAG", "left:" + (this.mScreenW / 2 - this.mScreenH * 2 / 3 + this.mScreenH * 1 / 6));
            Log.e("TAG", "top:" + (this.mScreenH * 1 / 6));
            Log.e("TAG", "right:" + (this.mScreenW / 2 + this.mScreenH * 2 / 3 - this.mScreenH * 1 / 6));
            Log.e("TAG", "bottom:" + (this.mScreenH - this.mScreenH * 1 / 6));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.mCanvas != null) {
                this.mHolder.unlockCanvasAndPost(this.mCanvas);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //获得宽高，开启子线程绘图
        this.mScreenW = getWidth();
        this.mScreenH = getHeight();
        this.mThread = new Thread(this);
        this.mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止线程
        try {
            mThread.interrupt();
            mThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //子线程绘图
        draw();
    }
}
