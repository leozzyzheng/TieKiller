package com.leozzyzheng.tiekiller.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by leozzyzheng on 2014/11/26.
 * 通用的简单Viewpager的Adapter
 */
public class SimplePageAdapter extends PagerAdapter {

    public List<View> mListViews;


    public SimplePageAdapter(List<View> list) {
        mListViews = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mListViews.get(position);
        container.addView(v, 0);//这里必须是0否则会崩溃
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (o);
    }

    public View getView(int index) {
        View view;
        try {
            view = mListViews.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return view;
    }
}
