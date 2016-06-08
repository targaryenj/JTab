package com.example.jdm.jtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jdm.jtab.R;

/**
 * Created by JDM on 2016/5/27.
 */
public class CommonFragment extends Fragment{
    private View mView;
    private int fragmentIndex;

    private TextView mTextView;

    public static CommonFragment newInstance(int fragmentId){
        CommonFragment fragment = new CommonFragment();
        fragment.fragmentIndex = fragmentId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null){
            return mView;
        }
        mView = inflater.inflate(R.layout.common_fragment,container,false);
        mTextView = (TextView) mView.findViewById(R.id.fragment_title_label);
        mTextView.setText("Fragment " + fragmentIndex);
        return  mView;
    }



























}
