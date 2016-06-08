package com.example.jdm.jtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jdm.jtab.R;
import com.example.jdm.jtab.view.QQTipView;

/**
 * Created by JDM on 2016/5/27.
 */
public class QQListFragment extends ListFragment {
    private int itemCount = 20;
    private QQTipView qqTipView;
    private BaseAdapter baseAdapter;

    private ImageView headImageView;
    private TextView nameTextView;

    public static QQListFragment newInstance(int fragmentId){
        QQListFragment fragment = new QQListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindList(inflater);
        return  super.onCreateView(inflater,container,savedInstanceState);
    }

    private void bindList(final LayoutInflater inflater) {
        setListAdapter(baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return itemCount;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.simple_adapter_item, null);
                    convertView.setTag(convertView.findViewById(R.id.notify_text));

                    headImageView = (ImageView) convertView.findViewById(R.id.header);
                    headImageView.setImageResource(R.drawable.ic_launcher);

                    nameTextView = (TextView) convertView.findViewById(R.id.name);
                    nameTextView.setText("Name" + position);

                }
                final TextView textView = (TextView) convertView.getTag();
                textView.setText("" + (int) (Math.random() * 10) + 1 );
                if (qqTipView != null) {
                    qqTipView.attach(textView, new QQTipView.TipListener() {
                        @Override
                        public void onStart() {
                            textView.setVisibility(View.INVISIBLE);
                            getListView().requestDisallowInterceptTouchEvent(true);
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onCancel() {
                            textView.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return convertView;
            }
        });

    }


    public void setTipsView(QQTipView tipView) {
        this.qqTipView = tipView;
        if(baseAdapter!=null)
            baseAdapter.notifyDataSetChanged();
    }

}
