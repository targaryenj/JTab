package com.example.jdm.jtab.fragment;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.example.jdm.jtab.R;

/**
 * 日落动画
 * Created by admin on 2016/6/3.
 */
public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset,container,false);
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        return view;
    }

    /**
     * 开始执行动画
     */
    private void startAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        // 日落动画
        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(mSunView,"y",sunYStart,sunYEnd).setDuration(3000);
        // 加速插值器
        heightAnimator.setInterpolator(new AccelerateInterpolator());


        // 添加天空黄昏变化动画
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator.ofInt(mSkyView,"backgroundColor",mBlueSkyColor,mSunsetSkyColor).setDuration(3000);
        // ObjectAnimator不能直接处理非简单数值的动画，所以需要设置一个TypeEvaluator
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        // 添加天空夜晚变化动画
        ObjectAnimator nightSkyAnimator = ObjectAnimator.ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mNightSkyColor).setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(heightAnimator).with(sunsetSkyAnimator).before(nightSkyAnimator);
        animatorSet.start();

    }


















}
