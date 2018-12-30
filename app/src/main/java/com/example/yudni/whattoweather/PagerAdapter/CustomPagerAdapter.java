package com.example.yudni.whattoweather.PagerAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.yudni.whattoweather.Model.ModelTeaching;

/**
 * Created by yudni on 27.10.2017.
 */

public abstract class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;

    public CustomPagerAdapter(android.content.Context context) {
        mContext = context;
    }

    public CustomPagerAdapter(){}

    @Override
    public abstract Object instantiateItem(ViewGroup collection, int position);

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public abstract int getCount();

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelTeaching customPagerEnum = ModelTeaching.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
