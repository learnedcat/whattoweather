package com.example.yudni.whattoweather.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.yudni.whattoweather.Model.ModelClothesLegs;

/**
 * Created by yudni on 27.10.2017.
 */

public class CustomClothesLegsAdapter extends CustomPagerAdapter {
    private Context mContext;

    public CustomClothesLegsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelClothesLegs modelObject = ModelClothesLegs.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return ModelClothesLegs.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelClothesLegs customPagerEnum = ModelClothesLegs.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
