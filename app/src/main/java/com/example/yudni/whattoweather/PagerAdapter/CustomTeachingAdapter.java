package com.example.yudni.whattoweather.PagerAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yudni.whattoweather.Model.ModelTeaching;
import com.example.yudni.whattoweather.R;

/**
 * Created by yudni on 27.10.2017.
 */

public class CustomTeachingAdapter extends CustomPagerAdapter {
    private Context mContext;

    public CustomTeachingAdapter(android.content.Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelTeaching modelObject = ModelTeaching.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return ModelTeaching.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelTeaching customPagerEnum = ModelTeaching.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
