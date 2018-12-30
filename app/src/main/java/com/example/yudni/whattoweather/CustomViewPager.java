package com.example.yudni.whattoweather;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

public class CustomViewPager extends ViewPager {

    private boolean paging;
    private boolean firstNull;
    private boolean secondNull;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paging = true;
        this.firstNull = false;
        this.secondNull = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.paging) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.paging) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void checkLook(int pos, String clothes) {
        setPaging(true);

        if (pos == 0) {
            if (clothes.contains("nothing"))
                firstNull = true;
            else
                firstNull = false;
        }

        if (pos == 1) {
            if (clothes.contains("nothing"))
                secondNull = true;
            else
                secondNull = false;
        }

        if (!firstNull && !secondNull)
            setPaging(true);

        if (firstNull) {
            setPaging(false);
            setCurrentItem(1);
        }

        if (secondNull) {
            setPaging(false);
            setCurrentItem(0);
        }
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public boolean getPaging() {
        return paging;
    }
}
