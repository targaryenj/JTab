package com.example.jdm.jtab.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jdm.jtab.R;

/**
 *
 * Created by JDM on 2016/6/2.
 */
public class QQMessagePointView extends FrameLayout {
    // 默认圆半径
    public static final float DEFAULT_RADIUS = 35;

    private Paint mPaint;
    private Path path;

    /** 消息数 */
    private String tipNumber = "1";

    // 手势坐标
    float x = 300;
    float y = 300;

    // 锚点坐标
    float anchorX = 200;
    float anchorY = 300;

    // 起点坐标
    float startX = 200;
    float startY = 200;

    // 定点圆半径
    float radius = DEFAULT_RADIUS;

    int tipSize = 70;

    // 判断动画是否开始
    boolean isAnimStart;

    // 判断是否开始拖动
    boolean isDrag;

    ImageView exploredImageView;
    TextView tipTextView;


    public QQMessagePointView(Context context) {
        this(context,null);
    }

    public QQMessagePointView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQMessagePointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getResources().obtainAttributes(attrs,R.styleable.QQMessagePointView);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0;i < indexCount;i++){
            switch (typedArray.getIndex(i)){
                case R.styleable.QQMessagePointView_tip_start_x:
                    startX = typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,startX,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.QQMessagePointView_tip_start_y:
                    startY = typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,startY,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.QQMessagePointView_tip_size:
                    tipSize = (int)typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,tipSize,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.QQMessagePointView_tip_radius:
                    radius = typedArray.getDimension(i, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,tipSize,getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    /**
     * 初始化画笔，消息爆炸View，消息View
     */
    private void init() {
        path = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        exploredImageView = new ImageView(getContext());
        exploredImageView.setLayoutParams(params);
        exploredImageView.setImageResource(R.drawable.tip_anim);
        exploredImageView.setVisibility(View.INVISIBLE);

        LayoutParams tipParams = new LayoutParams(tipSize,tipSize);
        tipTextView = new TextView(getContext());
        tipTextView.setLayoutParams(tipParams);
        tipTextView.setText(tipNumber);
        tipTextView.setTextColor(Color.WHITE);
        tipTextView.setTextSize(16f);
        tipTextView.setGravity(Gravity.CENTER);
        tipTextView.setBackgroundResource(R.drawable.oval_message);

        addView(tipTextView);
        addView(exploredImageView);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        exploredImageView.setX(startX - exploredImageView.getWidth() / 2);
        exploredImageView.setY(startY - exploredImageView.getHeight() / 2);

        tipTextView.setX(startX - tipTextView.getWidth() / 2 );
        tipTextView.setY(startY - tipTextView.getHeight() / 2 );
        super.onLayout(changed, left, top, right, bottom);
    }

    private void calculate() {
        // 计算起点位置到手势坐标的距离
        float distance = (float) Math.sqrt(Math.pow(y-startY,2) + Math.pow(x-startX,2));
        radius = - distance / 15 + DEFAULT_RADIUS;

        if (radius < 9){
            isAnimStart = true;

            exploredImageView.setVisibility(View.VISIBLE);
            exploredImageView.setImageResource(R.drawable.tip_anim);

            ((AnimationDrawable)exploredImageView.getDrawable()).stop();
            ((AnimationDrawable)exploredImageView.getDrawable()).start();

            tipTextView.setVisibility(View.GONE);
        }

        // 根据角度算出四角形的四个点
        float offsetX = (float)(radius * Math.sin( Math.atan((y-startY)/(x - startX))  ));
        float offsetY = (float)(radius * Math.cos( Math.atan((y-startY)/(x - startX))  ));

        float x1 = startX - offsetX;
        float y1 = startY + offsetY;

        float x2 = x - offsetX;
        float y2 = y + offsetY;

        float x3 = x + offsetX;
        float y3 = y - offsetY;

        float x4 = startX + offsetX;
        float y4 = startY - offsetY;

        path.reset();
        path.moveTo(x1,y1);
        path.quadTo(anchorX,anchorY,x2,y2);
        path.lineTo(x3,y3);
        path.quadTo(anchorX,anchorY,x4,y4);
        path.lineTo(x1,y1);

        // 更改消息位置
        tipTextView.setX(x - tipTextView.getWidth()/2);
        tipTextView.setY(y - tipTextView.getHeight()/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isAnimStart || !isDrag){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        } else {
            calculate();
            canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.OVERLAY);
            canvas.drawPath(path,mPaint);
            canvas.drawCircle(startX,startY,radius,mPaint);
            canvas.drawCircle(x,y,radius,mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 判断触摸点是否在tipTextView中
            Rect rect = new Rect();
            int[] location = new int[2];

            tipTextView.getDrawingRect(rect);
            tipTextView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.right + location[0];
            rect.bottom = rect.bottom + location[1];

            if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                isDrag = true;
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            isDrag = false;

            // 判断触摸点是否在tipTextView中
            Rect rect = new Rect();
            int[] location = new int[2];

            tipTextView.getDrawingRect(rect);
            tipTextView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.right + location[0];
            rect.bottom = rect.bottom + location[1];

            if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {

                float endX = startX - tipTextView.getWidth() / 2;
                float endY = startY - tipTextView.getHeight() / 2;

                ObjectAnimator tipXAnimator = ObjectAnimator.ofFloat(tipTextView,"x",event.getX(),endX).setDuration(600);
                ObjectAnimator tipYAnimator = ObjectAnimator.ofFloat(tipTextView,"y",event.getY(),endY).setDuration(600);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setInterpolator(new OvershootInterpolator(3));
                animatorSet.play(tipXAnimator).with(tipYAnimator);
                animatorSet.start();

            }
        }
        invalidate();
        if (isAnimStart) {
            return super.onTouchEvent(event);
        }
        anchorX = (event.getX() + startX) / 2;
        anchorY = (event.getY() + startY) / 2;
        x = event.getX();
        y = event.getY();
        return true;
    }
}
