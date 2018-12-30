package com.example.yudni.whattoweather.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.yudni.whattoweather.Model.ModelClothesBody;

/**
 * Created by yudni on 27.10.2017.
 */

public class CustomClothesBodyAdapter extends CustomPagerAdapter {
    private Context mContext;

    public CustomClothesBodyAdapter(android.content.Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        ModelClothesBody modelObject = ModelClothesBody.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return ModelClothesBody.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelClothesBody customPagerEnum = ModelClothesBody.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
