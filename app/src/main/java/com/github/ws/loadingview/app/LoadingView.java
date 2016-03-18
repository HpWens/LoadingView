package com.github.ws.loadingview.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.github.ws.loadingview.R;

/**
 * Created by Administrator on 3/18 0018.
 */
public class LoadingView extends View {

    private Paint mPaint;
    private int ballColor;
    private int angle = 0;//旋转角度  0~360

    private int centerPointX;
    private int centerPointY;
    private int ballNumber; //默认小球数量为6

    private int loadRadius;
    private int ballRadius;

    private boolean isShow = false;

    private static final float LOADING_RADIUS = 1 / 16F;
    private static final float POINT_RADIUS = 1 / 8F;
    private static final int MOVE_ANGLE = 8;

    private static final int MSG_BALL_GONE = 2;
    private static final int MSG_BALL_VISIBLE = 1;

    private static final int BALLS_DISTANCE = 32;
    private static final int CIRCLE_ANGLE = 360;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_BALL_VISIBLE) {
                angle += MOVE_ANGLE;
                handler.removeMessages(MSG_BALL_VISIBLE);
                invalidate();
            } else if (msg.what == MSG_BALL_GONE) {
                isShow = true;
                angle = 0;
                handler.removeMessages(MSG_BALL_GONE);
                invalidate();
            }
        }
    };

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, defStyleAttr, 0);
        ballColor = ta.getColor(R.styleable.LoadingView_ballColor, Color.parseColor("#ff0000"));
        ballNumber = ta.getInteger(R.styleable.LoadingView_ballNumber, 6);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        this.setBackgroundColor(Color.parseColor("#9fffffff"));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ballColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getDefaultMeasureSpec()[0], getDefaultMeasureSpec()[1]);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getDefaultMeasureSpec()[0], heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, getDefaultMeasureSpec()[1]);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        loadRadius = (int) (Math.min(w, h) * LOADING_RADIUS);
        ballRadius = (int) (loadRadius * POINT_RADIUS);
        centerPointX = w / 2;
        centerPointY = h / 2;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private int[] getDefaultMeasureSpec() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < ballNumber; i++) {
            //90度 开始转动
            float x = (float) (centerPointX + loadRadius * Math.cos(Math.toRadians(90 + angle + BALLS_DISTANCE * (i - (ballNumber - 1)))));
            float y = (float) (centerPointY + loadRadius * Math.sin(Math.toRadians(90 + angle + BALLS_DISTANCE * (i - (ballNumber - 1)))));
            if ((angle + BALLS_DISTANCE * (i - (ballNumber - 1))) > CIRCLE_ANGLE) {
                mPaint.setAlpha(0);
                if (angle > CIRCLE_ANGLE + BALLS_DISTANCE * (ballNumber - 1)) {
                    handler.sendEmptyMessageDelayed(MSG_BALL_GONE, 500);
                }
            } else {
                mPaint.setAlpha(255);
            }
            if (isShow) {
                if (angle + BALLS_DISTANCE * i < BALLS_DISTANCE * (ballNumber - 1)) {
                    mPaint.setAlpha(0);
                }
                if (angle > BALLS_DISTANCE * (ballNumber - 1)) {
                    isShow = false;
                }
            }
            canvas.drawCircle(x, y, ballRadius, mPaint);
        }
        handler.sendEmptyMessageDelayed(MSG_BALL_VISIBLE, 15);  //旋转快慢  合适自己调整
    }

    /**
     * 小球颜色
     *
     * @param ballColor
     */
    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
        if (mPaint != null) {
            mPaint.setColor(ballColor);
        }
    }

    /**
     * 小球的数量
     *
     * @param ballNumber
     */
    public void setBallNumber(int ballNumber) {
        this.ballNumber = ballNumber;
    }


}
