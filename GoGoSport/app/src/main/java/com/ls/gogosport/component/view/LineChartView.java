package com.ls.gogosport.component.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ls.gogosport.R;
import com.ls.gogosport.util.LogUtil;

/**
 * 自定义折线图
 *
 * @author liushuang 2018-12-01
 */
public class LineChartView extends View {

    private static final int POINT_NUMBER = 7;

    private Paint pointPaint;
    private Paint linePaint;
    private Paint textPaint;
    private Paint rectPaint;

    private float linePointRadius;
    private float lineWidth;
    private float testSize;
    private float marginBottom;
    private float touchX;

    private int[] stepData; //历史步数
    private float[] heightRate; //高度比例
    private String[] weekDay; //日期
    private int touchIndex = -1; //触摸折线的区间

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔和绘制数据
     */
    private void init() {
        linePointRadius = getResources().getDimension(R.dimen.count_step_line_point_radius);
        lineWidth = getResources().getDimension(R.dimen.count_step_line_width);
        testSize = getResources().getDimension(R.dimen.count_step_line_view_text_size);
        marginBottom = testSize * 2;

        //折线点的画笔
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setARGB(255, 236, 184, 138);
        pointPaint.setStyle(Paint.Style.FILL);
        //折线画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 236, 184, 138);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineWidth);
        //文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 135, 127, 108);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(testSize);
        //弹窗画笔
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setARGB(180, 0, 0, 0);
        rectPaint.setStyle(Paint.Style.FILL);

        stepData = new int[POINT_NUMBER];
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        drawText(canvas);
        drawLine(canvas);
        drawPopupWindow(canvas);
    }

    /**
     * 绘制日期文字
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float interval = width / POINT_NUMBER;
        for (int i = 0; i < POINT_NUMBER; ++i) {
            canvas.drawText(weekDay[i], interval * (i + 0.5f) - testSize, height - marginBottom, textPaint);
        }
    }

    /**
     * 绘制折线
     *
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas) {
        int width = getWidth();
        float height = getHeight() - marginBottom - 20; //处理与日期文字的间隔
        float interval = width / POINT_NUMBER;

        int len = heightRate.length;
        for (int i = 0; i < POINT_NUMBER; ++i) {
            if (len < i) {
                return;
            }
            //画折线的点
            canvas.drawCircle(interval * (POINT_NUMBER - i - 0.5f), height * heightRate[i], linePointRadius, pointPaint);
            if (i < POINT_NUMBER - 1) {
                //画折线,从右往左画
                canvas.drawLine(interval * (POINT_NUMBER - i - 0.5f), height * heightRate[i],
                        interval * (POINT_NUMBER - i - 1 - 0.5f), height * heightRate[i + 1], linePaint);
            }
        }
    }

    private void drawPopupWindow(Canvas canvas) {
        if (touchIndex != -1) {
            float interval = getWidth() / POINT_NUMBER;
            float height = getHeight() - marginBottom;
            canvas.drawRect(interval * touchIndex, height * heightRate[touchIndex], interval * (touchIndex + 1) - 10, height * heightRate[touchIndex] + 140, rectPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理wrap_content情况下的尺寸
        if (widthSpecMode == MeasureSpec.AT_MOST && heightMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, 600);
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 600);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
            case MotionEvent.ACTION_UP:
                LogUtil.d("LineView", "action up");
                touchIndex = findTouchInterval(touchX);
                invalidate();
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    private int findTouchInterval(float x) {
        float interval = getWidth() / POINT_NUMBER;
        for (int i = 1; i < POINT_NUMBER; ++i) {
            if (x < interval * i) {
                return i - 1;
            }
        }
        return -1;
    }

    public void setStepData(int[] stepData) {
        this.stepData = stepData;
    }

    public void setHeightRate(float[] heightRate) {
        this.heightRate = heightRate;
    }

    public void setWeekDay(String[] weekDay) {
        this.weekDay = weekDay;
    }
}
