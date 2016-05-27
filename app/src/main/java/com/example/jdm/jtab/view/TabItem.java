package com.example.jdm.jtab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by JDM on 2016/5/27.
 */
public class TabItem extends View {
    private static final String TAG = TabItem.class.getSimpleName();

    private int mTextSize = 12;
    private int mTextColorSelect = 0xff45c01a;
    private int mTextColorNormal = 0xff777777;
    private Paint mTextPaintSelect;
    private Paint mTextPaintNormal;

    private Paint mIconPaintSelect;
    private Paint mIconPaintNormal;

    // TabItem的高度，宽度
    private int mViewHeight, mViewWidth;
    private String mTextValue;
    private Bitmap mIconSelect;
    private Bitmap mIconNormal;

    private Rect mBoundText;


    public TabItem(Context context) {
        this(context,null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mBoundText = new Rect();

        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,mTextSize,getResources().getDisplayMetrics());

        mTextPaintNormal = new Paint();
        mTextPaintNormal.setTextSize(textSize);
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAntiAlias(true);
        mTextPaintNormal.setAlpha(0xff);

        mTextPaintSelect = new Paint();
        mTextPaintSelect.setTextSize(textSize);
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAntiAlias(true);
        mTextPaintSelect.setAlpha(0);

        mIconPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintNormal.setAlpha(0xff);

        mIconPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintSelect.setAlpha(0);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 基本上固定写法
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int width = 0, height = 0;
        measureText();
        // 内容宽度
        int contentWidth = Math.max(mBoundText.width(),mIconNormal.getWidth());
        int desireWidth = getPaddingLeft() + getPaddingRight() + contentWidth;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, desireWidth);
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = desireWidth;
                break;

        }
        Log.i(TAG,"BoundText =" + mBoundText.height() + ",IconNormalHeight=" + mIconNormal.getHeight());
        Log.i(TAG,"PaddingTop =" + getPaddingTop() + ",getPaddingBottom=" + getPaddingBottom());

        // 内容高度
        int contentHeight = mBoundText.height() + mIconNormal.getHeight();
        int desireHeight = getPaddingTop() + getPaddingBottom() + contentHeight;
        switch (heightMode){
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize,desireHeight);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = contentHeight;
                break;
        }

        setMeasuredDimension(width,height);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        Log.i(TAG,"ViewWidth =" + mViewWidth + ",ViewHeight=" + mViewHeight);

    }

    private void measureText() {
        mTextPaintNormal.getTextBounds(mTextValue,0,mTextValue.length(),mBoundText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        float x = (mViewWidth - mBoundText.width()) / 2.0f;
        float y = (mViewHeight + mIconNormal.getHeight() + mBoundText.height()) / 2.0f;
        canvas.drawText(mTextValue,x,y,mTextPaintNormal);
        canvas.drawText(mTextValue,x,y,mTextPaintSelect);
    }

    private void drawBitmap(Canvas canvas) {
        int left = (mViewWidth - mIconNormal.getWidth()) / 2;
        int top = (mViewHeight - mIconNormal.getHeight() - mBoundText.height()) / 2;
        canvas.drawBitmap(mIconNormal,left,top,mIconPaintNormal);
        canvas.drawBitmap(mIconSelect,left,top,mIconPaintSelect);
    }


    public void setTextSize(int textSize){
        this.mTextSize = textSize;
        mTextPaintNormal.setTextSize(textSize);
        mTextPaintSelect.setTextSize(textSize);
    }

    public void setTextColorSelect(int mTextColorSelect) {
        this.mTextColorSelect = mTextColorSelect;
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAlpha(0);
    }

    public void setTextColorNormal(int mTextColorNormal) {
        this.mTextColorNormal = mTextColorNormal;
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAlpha(0xff);
    }

    public void setTextValue(String TextValue) {
        this.mTextValue = TextValue;
    }
    public void setIconText(int[] iconSelId,String TextValue) {
        this.mIconSelect = BitmapFactory.decodeResource(getResources(), iconSelId[0]);
        this.mIconNormal = BitmapFactory.decodeResource(getResources(), iconSelId[1]);
        this.mTextValue = TextValue;
    }

    public void setTabAlpha(float alpha){
        int paintAlpha = (int)(alpha*255) ;
        mIconPaintSelect.setAlpha(paintAlpha);
        mIconPaintNormal.setAlpha(255-paintAlpha);
        mTextPaintSelect.setAlpha(paintAlpha);
        mTextPaintNormal.setAlpha(255-paintAlpha);
        invalidate();
    }
}
