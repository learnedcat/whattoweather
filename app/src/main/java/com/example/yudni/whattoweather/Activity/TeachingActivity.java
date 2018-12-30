package com.example.yudni.whattoweather.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.yudni.whattoweather.PagerAdapter.CustomTeachingAdapter;
import com.example.yudni.whattoweather.R;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class TeachingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomTeachingAdapter(this));
        //PageIndicator linePageIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        //linePageIndicator.setViewPager(viewPager);
    }

    public void letsStart(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String[] weather = new String[4];

        weather[0] = getIntent().getStringExtra("value");
        weather[1] = getIntent().getStringExtra("condition");
        weather[2] = getIntent().getStringExtra("code");
        weather[3] = getIntent().getStringExtra("feel");
        String connectionErrorFlag = getIntent().getStringExtra("connection_error");

        String fromSettings = getIntent().getStringExtra("from_settings");
        if (fromSettings == null)
            fromSettings = "false";
        if (fromSettings.equals("true"))
            connectionErrorFlag = "false";

        intent.putExtra("value", weather[0]);
        intent.putExtra("condition", weather[1]);
        intent.putExtra("code", weather[2]);
        intent.putExtra("feel", weather[3]);
        intent.putExtra("connection_error", String.valueOf(connectionErrorFlag));
        startActivity(intent);
    }
}
