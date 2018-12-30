package com.example.yudni.whattoweather.Model;

import com.example.yudni.whattoweather.R;

/**
 * Created by yudni on 09.10.2017.
 */

public enum ModelTeaching implements ModelObject{

    RED(R.string.red, R.layout.teaching_1),
    BLUE(R.string.blue, R.layout.teaching_2),
    GREEN(R.string.green, R.layout.teaching_3),
    WHITE(R.string.white, R.layout.teaching_4),
    YELLOW(R.string.yellow, R.layout.teaching_5);

    private int mTitleResId;
    private int mLayoutResId;

    ModelTeaching(int titleResId, int layoutResId) {
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