package com.example.jdm.jtab.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jdm.jtab.R;

import org.w3c.dom.Text;

/**
 *
 * Created by JDM on 2016/6/2.
 */
public class QQTipView extends FrameLayout {
    // 默认圆半径
    public static final float DEFAULT_RADIUS = 35;

    private Paint mPaint;
    private Path path;

    /** 消息数 */
    private String tipNumber = "1";

    // 手势坐标
    float x = 0;
    float y = 0;

    // 锚点坐标
    float anchorX = 0;
    float anchorY = 0;

    // 起点坐标
    float startX = 0;
    float startY = 0;

    float thisX = 0;
    float thisY = 0;

    // 定点圆半径
    float radius = DEFAULT_RADIUS;

    int tipSize = 70;

    // 判断是否开始爆炸动画
    boolean isExplore;

    // 判断是否开始拖动
    boolean isDrag;

    ImageView exploredImageView;
    View tipTextView;


    public QQTipView(Context context) {
        this(context,null);
    }

    public QQTipView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQTipView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        setBackgroundColor(Color.TRANSPARENT);
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

        addView(exploredImageView);
    }

    public void setPaintColor(int color){
        mPaint.setColor(color);
    }

    private void calculate() {
        // 计算起点位置到手势坐标的距离
        float distance = (float) Math.sqrt(Math.pow(y-startY,2) + Math.pow(x-startX,2));
        radius = - distance / 15 + DEFAULT_RADIUS;

        if (radius < 7){
            isExplore = true;
        } else {
            isExplore = false;
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculate();
        if (isExplore || !isDrag || tipTextView == null){
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

    public static Bitmap view2Bitmap(View v) {
        Bitmap bm = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        v.draw(canvas);
        return bm;
    }

    public void attach(final View attachView,TipListener listener){
        attach(attachView,new TipInvoke<View>(){
            @Override
            public View invoke() {
                Bitmap bm = view2Bitmap(attachView);
                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bm);
                return imageView;
            }
        },listener);
    }


    public void attach(final View attachView,final TipInvoke<View> copyViewCreator,final TipListener listener){
        attachView.setOnTouchListener(new OnTouchListener() {
            protected void init(){
                int[] attachLocation = new int[2];
                attachView.getLocationOnScreen(attachLocation);
                int[] thisLocation = new int[2];
                QQTipView.this.getLocationOnScreen(thisLocation);

                startX = attachLocation[0] - thisLocation[0] + attachView.getWidth() / 2;
                startY = attachLocation[1] - thisLocation[1] + attachView.getHeight() / 2;

                x = startX;
                y = startY;

                tipTextView = copyViewCreator.invoke();

                tipTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                QQTipView.this.addView(tipTextView);
                tipTextView.measure(0,0);

                tipTextView.setX(startX - tipTextView.getMeasuredWidth() / 2);
                tipTextView.setY(startY - tipTextView.getMeasuredHeight() / 2);

                if (listener != null) {
                    listener.onStart();
                }
            }

            protected void destory() {
                QQTipView.this.removeView(tipTextView);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    init();
                    isDrag = true;
                    int[] location = new int[2];
                    QQTipView.this.getLocationOnScreen(location);
                    thisX = location[0];
                    thisY = location[1];

                    invalidate();
                    return true;
                }
                if (!isDrag)
                    return false;
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    isDrag = false;
                    destory();

                    if (isExplore) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isExplore = false;
                                if (listener != null) {
                                    listener.onComplete();
                                }
                            }
                        }, 1000);
                        exploredImageView.setX(x - exploredImageView.getWidth() / 2);
                        exploredImageView.setY(y - exploredImageView.getHeight() / 2);
                        exploredImageView.setVisibility(View.VISIBLE);
                        exploredImageView.setImageResource(R.drawable.tip_anim);
                        ((AnimationDrawable) exploredImageView.getDrawable()).stop();
                        ((AnimationDrawable) exploredImageView.getDrawable()).start();
                    } else {
                        if (listener != null) {
                            listener.onCancel();
                        }

//                        float endX = startX - tipTextView.getWidth() / 2;
//                        float endY = startY - tipTextView.getHeight() / 2;
//                        ObjectAnimator tipXAnimator = ObjectAnimator.ofFloat(tipTextView,"x",event.getX(),endX).setDuration(600);
//                        ObjectAnimator tipYAnimator = ObjectAnimator.ofFloat(tipTextView,"y",event.getY(),endY).setDuration(600);
//                        AnimatorSet animatorSet = new AnimatorSet();
//                        animatorSet.setInterpolator(new OvershootInterpolator(3));
//                        animatorSet.play(tipXAnimator).with(tipYAnimator);
//                        animatorSet.start();
                    }
                }

                anchorX = (event.getRawX() - thisX + startX) / 2;
                anchorY = (event.getRawY() - thisY + startY) / 2;
                x = event.getRawX() - thisX;
                y = event.getRawY() - thisY;

                tipTextView.setX(x - tipTextView.getWidth() / 2);
                tipTextView.setY(y - tipTextView.getHeight() / 2);

                invalidate();
                return true;
            }
        });
    }

    public interface TipInvoke<T> {
        T invoke();
    }

    public static interface TipListener {
        void onStart();

        void onComplete();

        void onCancel();
    }

    public static QQTipView init(Context context,FrameLayout rootView) {
        QQTipView tipsView = new QQTipView(context);
        rootView.addView(tipsView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        return tipsView;
    }

    public static QQTipView init(Context context,RelativeLayout rootView) {
        QQTipView tipsView = new QQTipView(context);
        rootView.addView(tipsView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        return tipsView;
    }
}
