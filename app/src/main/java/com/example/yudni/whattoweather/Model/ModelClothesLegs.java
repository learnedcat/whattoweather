package com.example.yudni.whattoweather.Model;

import com.example.yudni.whattoweather.R;

/**
 * Created by yudni on 27.10.2017.
 */

public enum ModelClothesLegs implements ModelObject{
    IMAGE1(R.string.image_legs_1, R.layout.clothes_legs_1),
    IMAGE2(R.string.image_legs_2, R.layout.clothes_legs_2);

    private int mTitleResId;
    private int mLayoutResId;

    ModelClothesLegs(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
