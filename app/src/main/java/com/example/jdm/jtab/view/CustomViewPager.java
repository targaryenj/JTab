package com.example.jdm.jtab.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义ViewPager，重写onInterceptTouchEvent，解决子视图和ViewPager的滑动冲突
 * Created by JDM on 2016/6/3.
 */
public class CustomViewPager extends ViewPager {

    // 子视图Id
    private int childId;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (childId > 0){
            View childView = findViewById(childId);
            if (childView != null){
                Rect rect = new Rect();
                childView.getHitRect(rect);
                // 判断事件是否在子视图内
                if (rect.contains((int)ev.getX(),(int)ev.getY())){
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }
}
