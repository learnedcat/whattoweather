package com.example.yudni.whattoweather.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yudni.whattoweather.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setFont();
        settings = getDefaultSharedPreferences(getApplicationContext());

        ImageButton buttonTeach = (ImageButton) findViewById(R.id.buttonTeach);
        buttonTeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeachingActivity.class);
                intent.putExtra("from_settings", "true");
                startActivity(intent);
            }
        });

        final RadioButton radioButtonC = (RadioButton)findViewById(R.id.radioButtonC);
        final RadioButton radioButtonF = (RadioButton)findViewById(R.id.radioButtonF);
        String tempUnit = settings.getString("unit", null);
        if (tempUnit == null) tempUnit = "C";
        if (tempUnit.equals("C"))
            radioButtonC.setChecked(true);
        if (tempUnit.equals("F"))
            radioButtonF.setChecked(true);
        radioButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonC.isChecked()) {
                    radioButtonC.setChecked(true);
                    radioButtonF.setChecked(false);

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("unit", "C");
                    editor.apply();
                }
            }
        });
        radioButtonF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonF.isChecked()) {
                    radioButtonF.setChecked(true);
                    radioButtonC.setChecked(false);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("unit", "F");
                    editor.apply();
                }
            }
        });

        final RadioButton radioButtonFemale = (RadioButton)findViewById(R.id.radioButtonFemale);
        final RadioButton radioButtonMale = (RadioButton)findViewById(R.id.radioButtonMale);
        String gender = settings.getString("gender", null);
        if (gender == null) gender = "female";
        if (gender.equals("female"))
            radioButtonFemale.setChecked(true);
        if (gender.equals("male"))
            radioButtonMale.setChecked(true);
        radioButtonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonFemale.isChecked()) {
                    radioButtonFemale.setChecked(true);
                    radioButtonMale.setChecked(false);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("gender", "female");
                    editor.apply();
                }
            }
        });
        radioButtonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonMale.isChecked()) {
                    radioButtonMale.setChecked(true);
                    radioButtonFemale.setChecked(false);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("gender", "male");
                    editor.apply();
                }
            }
        });

        final RadioButton radioButtonTR = (RadioButton)findViewById(R.id.radioButtonTR);
        final RadioButton radioButtonTFl = (RadioButton)findViewById(R.id.radioButtonTFl);
        String temp = settings.getString("temp", null);
        if (temp == null) temp = "feelslike";
        if (temp.equals("real"))
            radioButtonTR.setChecked(true);
        if (temp.equals("feelslike"))
            radioButtonTFl.setChecked(true);
        radioButtonTR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonTR.isChecked()) {
                    radioButtonTR.setChecked(true);
                    radioButtonTFl.setChecked(false);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("temp", "real");
                    editor.apply();
                }
            }
        });
        radioButtonTFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonTFl.isChecked()) {
                    radioButtonTFl.setChecked(true);
                    radioButtonTR.setChecked(false);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("temp", "feelslike");
                    editor.apply();
                }
            }
        });
    }

    private void setFont() {
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView TextViewNewFont = new TextView(SettingsActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNewFont.setLayoutParams(layoutparams);
        TextViewNewFont.setText(getString(R.string.settings));
        TextViewNewFont.setTextColor(Color.WHITE);
        TextViewNewFont.setGravity(Gravity.LEFT);
        TextViewNewFont.setTextSize(27);
        //Typeface custom_font = Typeface.create("sans-serif-smallcaps", Typeface.NORMAL);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/titleFont.ttf");
        TextViewNewFont.setTypeface(custom_font);
        if (actionbar != null) {
            actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionbar.setCustomView(TextViewNewFont);
        }
    }
}
