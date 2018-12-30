package com.example.yudni.whattoweather.Model;

import com.example.yudni.whattoweather.R;

/**
 * Created by yudni on 27.10.2017.
 */

public enum ModelClothesBody implements ModelObject{
    IMAGE1(R.string.image_body_1, R.layout.clothes_body_1),
    IMAGE2(R.string.image_body_2, R.layout.clothes_body_2);

    private int mTitleResId;
    private int mLayoutResId;

    ModelClothesBody(int titleResId, int layoutResId) {
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
