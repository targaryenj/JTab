package com.example.jdm.jtab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jdm.jtab.fragment.CommonFragment;
import com.example.jdm.jtab.fragment.QQListFragment;
import com.example.jdm.jtab.fragment.SunsetFragment;
import com.example.jdm.jtab.view.CustomViewPager;
import com.example.jdm.jtab.view.QQTipView;
import com.example.jdm.jtab.view.TabView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String[] mTitle = {"微信", "通讯录", "发现", "我"};
    private int[] mIconSelect = {R.drawable.al_, R.drawable.al8, R.drawable.alb, R.drawable.ald};
    private int[] mIconNormal = {R.drawable.ala, R.drawable.al9, R.drawable.alc, R.drawable.ale};
    private CustomViewPager mViewPager ;
    private TabView mTabView ;
    private Map<Integer,Fragment> mFragmentMap ;

    //
    private QQTipView qqTipView;
    private QQListFragment qqListFragment = QQListFragment.newInstance(1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentMap = new HashMap<>();

        qqTipView = (QQTipView) findViewById(R.id.qq_tip);
        qqListFragment.setTipsView(qqTipView);

        mViewPager = (CustomViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTabView = (TabView) findViewById(R.id.id_tab);
        mTabView.setViewPager(mViewPager);
    }

    class PageAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener{

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }
        @Override
        public int[] onIconSelect(int position) {
            int icon[] = new int[2] ;
            icon[0] = mIconSelect[position] ;
            icon[1] = mIconNormal[position] ;
            return icon;
        }
        @Override
        public String onTextSelect(int position) {
            return mTitle[position];
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }
    }

    private Fragment getFragment(int position){
        Fragment fragment = mFragmentMap.get(position) ;
        if(fragment == null){
            switch (position){
                case 0:
                    fragment = SunsetFragment.newInstance();
                    break;
                case 1:
                    fragment = qqListFragment;
                    break;
                default:
                    fragment = CommonFragment.newInstance(position);
                    break;
            }
            mFragmentMap.put(position,fragment) ;
        }
        return fragment ;
    }
}
