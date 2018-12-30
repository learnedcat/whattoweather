package com.example.yudni.whattoweather.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudni.whattoweather.Clothes.ClothesList;
import com.example.yudni.whattoweather.CustomViewPager;
import com.example.yudni.whattoweather.PagerAdapter.CustomClothesBodyAdapter;
import com.example.yudni.whattoweather.PagerAdapter.CustomClothesLegsAdapter;

import com.example.yudni.whattoweather.R;
import com.example.yudni.whattoweather.Service.Server;
import com.example.yudni.whattoweather.Service.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private ListView listHead;
    private ListView listBody;
    private ListView listUnderBody;
    private ListView listLegs;
    private ListView listUnderLegs;
    private ListView listShoes;

    private TextView textCondition;
    private TextView pictureCondition;
    private TextView textTemperature;
    private TextView textAddTemperature;
    private TextView textTips;

    private ImageView imageHead;
    private ImageView imageBody;
    private ImageView imageBodyIcon;
    private ImageView imageUnderBody;
    private ImageView imageUnderBodyIcon;
    private ImageView imageLegs;
    private ImageView imageLegsIcon;
    private ImageView imageUnderLegs;
    private ImageView imageUnderLegsIcon;
    private ImageView imageShoes;
    private ImageView imageGloves;
    private ImageView imageUmbrella;

    private SharedPreferences settings;

    private String gender;
    private String unit;
    private String tempType;
    private long currentTemp;
    private int currentCode;

    private ClothesList adapterHead;
    private ClothesList adapterBody;
    private ClothesList adapterUnderBody;
    private ClothesList adapterLegs;
    private ClothesList adapterUnderLegs;
    private ClothesList adapterShoes;

    private JSONObject jsonHead;
    private JSONObject jsonBody;
    private JSONObject jsonLegs;
    private JSONObject jsonShoes;
    private JSONObject jsonUnderLegs;
    private JSONObject jsonUnderBody;
    private JSONObject jsonGloves;
    private JSONObject jsonUmbrella;

    private String currentCondition;
    private String currentTempString;

    private String clothesHead;
    private String clothesBody;
    private String clothesUnderBody;
    private String clothesLegs;
    private String clothesUnderLegs;
    private String clothesShoes;
    private String clothesGloves;
    private String clothesUmbrella;

    private boolean afterOnCreate = false;

    private CustomViewPager viewPagerBody;
    private CustomViewPager viewPagerLegs;
    private boolean connectionErrorFlag = false;

    private String getConditionForClothes(int code) {
        String conditionForClothes = null;
        if (Arrays.binarySearch(new int[]{0, 2}, code) >= 0) conditionForClothes = "tornado";
        if (Arrays.binarySearch(new int[]{1, 14, 40}, code) >= 0)
            conditionForClothes = "storm showers";
        if (Arrays.binarySearch(new int[]{3, 4, 37, 38, 39, 47}, code) >= 0)
            conditionForClothes = "thunderstorm";
        if (Arrays.binarySearch(new int[]{5, 13, 15, 16, 41, 42, 43, 46}, code) >= 0)
            conditionForClothes = "snow";
        if (Arrays.binarySearch(new int[]{6, 7, 35}, code) >= 0) conditionForClothes = "rain mix";
        if (Arrays.binarySearch(new int[]{8, 9}, code) >= 0) conditionForClothes = "sprinkle";
        if (Arrays.binarySearch(new int[]{10, 17, 18, 25}, code) >= 0) conditionForClothes = "hail";
        if (Arrays.binarySearch(new int[]{11, 12}, code) >= 0) conditionForClothes = "showers";
        if (Arrays.binarySearch(new int[]{19, 23}, code) >= 0) conditionForClothes = "cloudy gusts";
        if (Arrays.binarySearch(new int[]{20, 21}, code) >= 0) conditionForClothes = "fog";
        if (Arrays.binarySearch(new int[]{24}, code) >= 0) conditionForClothes = "cloudy windy";
        if (Arrays.binarySearch(new int[]{25}, code) >= 0) conditionForClothes = "thermometer";
        if (Arrays.binarySearch(new int[]{26, 44}, code) >= 0) conditionForClothes = "cloudy";
        if (Arrays.binarySearch(new int[]{27, 29}, code) >= 0) conditionForClothes = "night cloudy";
        if (Arrays.binarySearch(new int[]{28, 30}, code) >= 0) conditionForClothes = "day cloudy";
        if (Arrays.binarySearch(new int[]{31, 33}, code) >= 0) conditionForClothes = "night clear";
        if (Arrays.binarySearch(new int[]{32, 34, 36}, code) >= 0) conditionForClothes = "day sunny";
        if (Arrays.binarySearch(new int[]{22}, code) >= 0)
            conditionForClothes = "day sunny overcast";
        if (Arrays.binarySearch(new int[]{45}, code) >= 0) conditionForClothes = "lightning";
        return conditionForClothes;
    }

    public JSONObject getJSON(String name) {
        JSONObject jsonObject = null;
        try {
            String yourFilePath = getFilesDir() + "/" + name + ".json";
            File yourFile = new File(yourFilePath);
            FileInputStream is;
            String jsonString = "";
            try {
                is = new FileInputStream(yourFile);
                jsonString = readString(is);
                if (jsonString.length() == 0) throw new FileNotFoundException();
            } catch (FileNotFoundException eFnF) {
                InputStream isRaw = getResources().openRawResource(getResources().getIdentifier(name, "raw", getPackageName()));
                jsonString = readString(isRaw);

                OutputStream os = null;
                try {
                    os = new FileOutputStream(yourFile);
                } catch (FileNotFoundException eFnF2) {
                }
                try {
                    os.write(jsonString.getBytes());
                } catch (IOException eIO2) {
                }
            }

            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    private String readString(InputStream is) {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
        }
        return writer.toString();
    }

    private void setWeatherClothes() {
        currentCondition = getConditionForClothes(currentCode);
        currentTempString = "";

        if (currentTemp <= -30) currentTempString = "<-30";
        if (currentTemp <= -25 && currentTemp > -30) currentTempString = "-30<-25";
        if (currentTemp <= -20 && currentTemp > -25) currentTempString = "-25<-20";
        if (currentTemp <= -15 && currentTemp > -20) currentTempString = "-20<-15";
        if (currentTemp <= -10 && currentTemp > -15) currentTempString = "-15<-10";
        if (currentTemp <= -5 && currentTemp > -10) currentTempString = "-10<-5";
        if (currentTemp <= 0 && currentTemp > -5) currentTempString = "-5<0";
        if (currentTemp <= 5 && currentTemp > 0) currentTempString = "0<5";
        if (currentTemp <= 10 && currentTemp > 5) currentTempString = "5<10";
        if (currentTemp <= 15 && currentTemp > 10) currentTempString = "10<15";
        if (currentTemp <= 20 && currentTemp > 15) currentTempString = "15<20";
        if (currentTemp <= 25 && currentTemp > 20) currentTempString = "20<25";
        if (currentTemp <= 30 && currentTemp > 25) currentTempString = "25<30";
        if (currentTemp > 30) currentTempString = ">30";

        jsonHead = setClothes("head", null, null, imageHead);
        jsonBody = setClothes("body", viewPagerBody, 0, imageBody);
        jsonUnderBody = setClothes("under_body", viewPagerBody, 1, imageUnderBody);
        jsonLegs = setClothes("legs", viewPagerLegs, 0, imageLegs);
        jsonUnderLegs = setClothes("under_legs", viewPagerLegs, 1, imageUnderLegs);
        jsonShoes = setClothes("shoes", null, null, imageShoes);
        jsonGloves = setClothes("gloves", null, null, imageGloves);
        jsonUmbrella = setClothes("umbrella", null, null, imageUmbrella);

        setViewPagerPages();
        setIcons();
    }

    private void setIcons() {
        Drawable image = ((ImageView) viewPagerBody.getChildAt(1).findViewById(imageUnderBody.getId())).getDrawable();
        if (clothesUnderBody.contains("nothing")) {
            int id = getResources().getIdentifier("male_nothing_icon", "drawable", getPackageName());
            image = getDrawableById(id);
        }
        setIcon(((ImageView) viewPagerBody.getChildAt(0).findViewById(imageBodyIcon.getId())), image);

        image = ((ImageView) viewPagerBody.getChildAt(0).findViewById(imageBody.getId())).getDrawable();
        if (clothesBody.contains("nothing")) {
            int id = getResources().getIdentifier("male_nothing_icon", "drawable", getPackageName());
            image = getDrawableById(id);
        }
        setIcon(((ImageView) viewPagerBody.getChildAt(1).findViewById(imageUnderBodyIcon.getId())), image);

        image = ((ImageView) viewPagerLegs.getChildAt(1).findViewById(imageUnderLegs.getId())).getDrawable();
        if (clothesUnderLegs.contains("nothing")) {
            int id = getResources().getIdentifier("male_nothing_icon", "drawable", getPackageName());
            image = getDrawableById(id);
        }
        setIcon(((ImageView) viewPagerLegs.getChildAt(0).findViewById(imageLegsIcon.getId())), image);

        image = ((ImageView) viewPagerLegs.getChildAt(0).findViewById(imageLegs.getId())).getDrawable();
        if (clothesLegs.contains("nothing")) {
            int id = getResources().getIdentifier("male_nothing_icon", "drawable", getPackageName());
            image = getDrawableById(id);
        }
        setIcon(((ImageView) viewPagerLegs.getChildAt(1).findViewById(imageUnderLegsIcon.getId())), image);
    }

    private void setIcon(ImageView icon, Drawable image) {
        icon.setImageDrawable(image);
    }

    private void setViewPagerPages() {
        try {
            if (((ImageView) viewPagerBody.getChildAt(0).findViewById(R.id.imageBody)).getDrawable() == null)
                viewPagerBody.setCurrentItem(1);
            if (((ImageView) viewPagerBody.getChildAt(1).findViewById(R.id.imageUnderBody)).getDrawable() == null)
                viewPagerBody.setCurrentItem(0);
            if (((ImageView) viewPagerLegs.getChildAt(0).findViewById(R.id.imageLegs)).getDrawable() == null)
                viewPagerLegs.setCurrentItem(1);
            if (((ImageView) viewPagerLegs.getChildAt(1).findViewById(R.id.imageUnderLegs)).getDrawable() == null)
                viewPagerLegs.setCurrentItem(0);
        } catch (NullPointerException ignored) {
        }
    }

    private JSONObject setClothes(String type, CustomViewPager viewPager, Integer pos, ImageView image) {
        String clothes = "";
        JSONObject json = null;
        try {
            json = getJSON(type);
            clothes = json.getJSONObject(gender).getJSONObject(currentTempString).getString(currentCondition);
            if (type.equals("body") || type.equals("under_body") || type.equals("legs") || type.equals("under_legs")) {
                //if (!firstImageLoad)
                //    ((CustomPagerAdapter) viewPager.getAdapter()).addImageSource(pos, getResources().getIdentifier(gender + "_" + clothes, "drawable", getPackageName()));
                //else
                ((ImageView) viewPager.getChildAt(pos).findViewById(image.getId())).setImageResource(getResources().getIdentifier(gender + "_" + clothes, "drawable", getPackageName()));
                viewPager.checkLook(pos, clothes);
            } else
                image.setImageResource(getResources().getIdentifier(gender + "_" + clothes, "drawable", getPackageName()));

            if (type.equals("head")) clothesHead = clothes;
            if (type.equals("body")) clothesBody = clothes;
            if (type.equals("under_body")) clothesUnderBody = clothes;
            if (type.equals("legs")) clothesLegs = clothes;
            if (type.equals("under_legs")) clothesUnderLegs = clothes;
            if (type.equals("shoes")) clothesShoes = clothes;
            if (type.equals("gloves")) clothesGloves = clothes;
            if (type.equals("umbrella")) clothesUmbrella = clothes;
        } catch (JSONException e) {
            if (type.equals("body") || type.equals("under_body") || type.equals("legs") || type.equals("under_legs"))
                //if (!firstImageLoad)
                //    ((CustomPagerAdapter) viewPager.getAdapter()).addImageSource(pos, android.R.color.transparent);
                //else
                ((ImageView) viewPager.getChildAt(pos).findViewById(image.getId())).setImageResource(android.R.color.transparent);
            else
                image.setImageResource(android.R.color.transparent);
        }
        return json;
    }

    private boolean visibleListExistence() {
        if (listHead.getVisibility() == View.VISIBLE || listBody.getVisibility() == View.VISIBLE ||
                listLegs.getVisibility() == View.VISIBLE || listShoes.getVisibility() == View.VISIBLE || listUnderBody.getVisibility() == View.VISIBLE ||
                listUnderLegs.getVisibility() == View.VISIBLE)
            return true;
        return false;
    }

    private void setListAdapters() {
        adapterHead = new ClothesList(MainActivity.this, gender, "head");
        adapterBody = new ClothesList(MainActivity.this, gender, "body");
        adapterUnderBody = new ClothesList(MainActivity.this, gender, "under_body");
        adapterLegs = new ClothesList(MainActivity.this, gender, "legs");
        adapterUnderLegs = new ClothesList(MainActivity.this, gender, "under_legs");
        adapterShoes = new ClothesList(MainActivity.this, gender, "shoes");
        listHead.setAdapter(adapterHead);
        listBody.setAdapter(adapterBody);
        listUnderBody.setAdapter(adapterUnderBody);
        listLegs.setAdapter(adapterLegs);
        listUnderLegs.setAdapter(adapterUnderLegs);
        listShoes.setAdapter(adapterShoes);
        setListOnClickListeners();
    }

    private void initLists() {
        listHead = (ListView) findViewById(R.id.listHead);
        listBody = (ListView) findViewById(R.id.listBody);
        listUnderBody = (ListView) findViewById(R.id.listUnderBody);
        listLegs = (ListView) findViewById(R.id.listLegs);
        listUnderLegs = (ListView) findViewById(R.id.listUnderLegs);
        listShoes = (ListView) findViewById(R.id.listShoes);
        setListVisibility(null);
    }

    private AdapterView.OnItemClickListener newOnItemClickListener(final String type, final CustomViewPager viewPager, final Integer pos, final ClothesList adapter, final JSONObject json, final ImageView image) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imageName = adapter.getItemName(position);
                try {
                    json.getJSONObject(gender).getJSONObject(currentTempString).put(currentCondition, imageName);
                    if (type.equals("body") || type.equals("under_body") || type.equals("legs") || type.equals("under_legs")) {
                        ((ImageView) viewPager.getChildAt(pos).findViewById(image.getId())).setImageResource(getResources().getIdentifier(gender + "_" + imageName, "drawable", getPackageName()));
                        viewPager.checkLook(pos, imageName);
                    } else
                        image.setImageResource(getResources().getIdentifier(gender + "_" + imageName, "drawable", getPackageName()));
                    OutputStream os = null;
                    if (type.equals("head")) clothesHead = imageName.replace("_icon", "");
                    if (type.equals("body")) clothesBody = imageName.replace("_icon", "");
                    if (type.equals("under_body"))
                        clothesUnderBody = imageName.replace("_icon", "");
                    if (type.equals("legs")) clothesLegs = imageName.replace("_icon", "");
                    if (type.equals("under_legs"))
                        clothesUnderLegs = imageName.replace("_icon", "");
                    if (type.equals("shoes")) clothesShoes = imageName.replace("_icon", "");
                    if (afterOnCreate) setIcons();
                    try {
                        os = new FileOutputStream(getFilesDir() + "/" + type + ".json");
                    } catch (FileNotFoundException ignored) {
                    }
                    try {
                        os.write(json.toString().getBytes());
                    } catch (IOException ignored) {
                    }
                } catch (JSONException ignored) {
                }
            }
        };
    }

    private void initTexts() {
        textCondition = (TextView) findViewById(R.id.textCondition);
        textCondition.setTextSize(15);
        //textCondition.setTextColor(Color.parseColor("#01c3f6"));
        //textCondition.setTextColor(Color.parseColor("#94dffe"));
        textCondition.setVisibility(View.INVISIBLE);

        pictureCondition = (TextView) findViewById(R.id.pictureCondition);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/weatherFont.ttf");
        pictureCondition.setTypeface(custom_font);
        pictureCondition.setTextSize(35);
        //pictureCondition.setTextColor(Color.parseColor("#94dffe"));
        pictureCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textCondition.setVisibility(View.VISIBLE);
            }
        });

        textTemperature = (TextView) findViewById(R.id.textTemperature);
        textTemperature.setTextSize(70);
        //textTemperature.setTextColor(Color.parseColor("#94dffe"));
        textTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textAddTemperature.setVisibility(View.VISIBLE);
            }
        });

        textAddTemperature = (TextView) findViewById(R.id.textAddTemperature);
        textAddTemperature.setTextSize(17);
        //textAddTemperature.setTextColor(Color.parseColor("#94dffe"));
        textAddTemperature.setVisibility(View.INVISIBLE);

        textTips = (TextView) findViewById(R.id.textTips);
        textTips.setVisibility(View.INVISIBLE);
        textTips.setTextSize(15);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        TextView TextViewNewFont = new TextView(MainActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNewFont.setLayoutParams(layoutparams);
        TextViewNewFont.setText(getString(R.string.app_name));
        TextViewNewFont.setTextColor(Color.WHITE);
        TextViewNewFont.setGravity(Gravity.LEFT);
        TextViewNewFont.setTextSize(27);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/titleFont.ttf");
        //custom_font = Typeface.create("sans-serif-smallcaps", Typeface.NORMAL);
        TextViewNewFont.setTypeface(custom_font);
        if (actionbar != null) {
            actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionbar.setCustomView(TextViewNewFont);
        }
    }

    private void initImages() {
        imageHead = (ImageView) findViewById(R.id.imageHead);
        imageLegs = (ImageView) findViewById(R.id.imageLegs);
        imageShoes = (ImageView) findViewById(R.id.imageShoes);
        imageGloves = (ImageView) findViewById(R.id.imageGloves);
        imageUmbrella = (ImageView) findViewById(R.id.imageUmbrella);

        LayoutInflater factoryBody = getLayoutInflater();
        View textEntryViewBody = factoryBody.inflate(R.layout.clothes_body_1, null);
        imageBody = (ImageView) textEntryViewBody.findViewById(R.id.imageBody);
        imageBodyIcon = (ImageView) textEntryViewBody.findViewById(R.id.imageBodyIcon);
        View textEntryViewUnderBody = factoryBody.inflate(R.layout.clothes_body_2, null);
        imageUnderBody = (ImageView) textEntryViewUnderBody.findViewById(R.id.imageUnderBody);
        imageUnderBodyIcon = (ImageView) textEntryViewUnderBody.findViewById(R.id.imageUnderBodyIcon);

        LayoutInflater factoryLegs = getLayoutInflater();
        View textEntryViewLegs = factoryLegs.inflate(R.layout.clothes_legs_1, null);
        imageLegs = (ImageView) textEntryViewLegs.findViewById(R.id.imageLegs);
        imageLegsIcon = (ImageView) textEntryViewLegs.findViewById(R.id.imageLegsIcon);
        View textEntryViewUnderLegs = factoryLegs.inflate(R.layout.clothes_legs_2, null);
        imageUnderLegs = (ImageView) textEntryViewUnderLegs.findViewById(R.id.imageUnderLegs);
        imageUnderLegsIcon = (ImageView) textEntryViewUnderLegs.findViewById(R.id.imageUnderLegsIcon);

        imageHead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("head");
                return true;
            }
        });

        imageLegs.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("legs");
                return true;
            }
        });
        imageShoes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("shoes");
                return true;
            }
        });

        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesHead));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        /*imageLegs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesLegs));
                textTips.setVisibility(View.VISIBLE);
            }
        });*/

        imageShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesShoes));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        imageGloves.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                if (clothesGloves.equals("gloves_on"))
                    clothesGloves = "gloves_off";
                else
                    clothesGloves = "gloves_on";

                imageGloves.setImageResource(getResources().getIdentifier(gender + "_" + clothesGloves, "drawable", getPackageName()));
                try {
                    jsonGloves.getJSONObject(gender).getJSONObject(currentTempString).put(currentCondition, clothesGloves);
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(getFilesDir() + "/gloves.json");
                    } catch (FileNotFoundException ignored) {
                    }
                    try {
                        os.write(jsonGloves.toString().getBytes());
                    } catch (IOException ignored) {
                    }
                } catch (JSONException ignored) {
                }
                setListVisibility(null);
                return true;
            }
        });

        imageGloves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesGloves));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        imageUmbrella.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                if (clothesUmbrella.equals("umbrella_on"))
                    clothesUmbrella = "umbrella_off";
                else
                    clothesUmbrella = "umbrella_on";

                imageUmbrella.setImageResource(getResources().getIdentifier(gender + "_" + clothesUmbrella, "drawable", getPackageName()));
                try {
                    jsonUmbrella.getJSONObject(gender).getJSONObject(currentTempString).put(currentCondition, clothesUmbrella);
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(getFilesDir() + "/umbrella.json");
                    } catch (FileNotFoundException eFnF2) {
                    }
                    try {
                        os.write(jsonUmbrella.toString().getBytes());
                    } catch (IOException eIO2) {
                    }
                } catch (JSONException e) {
                }
                setListVisibility(null);
                return true;
            }
        });

        imageUmbrella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesUmbrella));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        View relativeLayout = findViewById(R.id.activity_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
            }
        });
    }

    private void setTextUnvisible() {
        textCondition.setVisibility(View.INVISIBLE);
        textAddTemperature.setVisibility(View.INVISIBLE);
        textTips.setVisibility(View.INVISIBLE);
    }

    private boolean setSettings() {
        boolean settingsChanged = false;
        settings = getDefaultSharedPreferences(getApplicationContext());
        String newUnit = settings.getString("unit", null);
        gender = settings.getString("gender", null);
        String newTempType = settings.getString("temp", null);

        if (newUnit == null) {
            unit = "C";
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("unit", unit);
            editor.apply();
            settingsChanged = true;
        } else if (!newUnit.equals(unit)) {
            settingsChanged = true;
            unit = newUnit;
        }

        if (gender == null) {
            gender = "female";
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("gender", gender);
            editor.apply();
        }

        if (newTempType == null) {
            tempType = "real";
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("temp", tempType);
            editor.apply();
            settingsChanged = true;
        } else if (!newTempType.equals(tempType)) {
            settingsChanged = true;
            tempType = newTempType;
        }
        return settingsChanged;
    }

    private void setListVisibility(String name) {
        if (name == null) {
            findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
            return;
        }
        switch (name) {
            case "head":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.VISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
            case "body":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.VISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
            case "under_body":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.VISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
            case "legs":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.VISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
            case "under_legs":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.VISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
            case "shoes":
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.VISIBLE);
                break;
            default:
                findViewById(R.id.linearLayoutListHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderBody).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListUnderLegs).setVisibility(View.INVISIBLE);
                findViewById(R.id.linearLayoutListShoes).setVisibility(View.INVISIBLE);
                break;
        }
    }

    private String getSymbolCode(int conditionCode) {
        String symbol = "";
        switch (conditionCode) {
            case 0:
                symbol = "\uf056";
                break;
            case 1:
                symbol = "\uf00e";
                break;
            case 2:
                symbol = "\uf073";
                break;
            case 3:
                symbol = "\uf01e";
                break;
            case 4:
                symbol = "\uf01e";
                break;
            case 5:
                symbol = "\uf01b";
                break;
            case 6:
                symbol = "\uf017";
                break;
            case 7:
                symbol = "\uf017";
                break;
            case 8:
                symbol = "\uf01c";
                break;
            case 9:
                symbol = "\uf01c";
                break;
            case 10:
                symbol = "\uf015";
                break;
            case 11:
                symbol = "\uf01a";
                break;
            case 12:
                symbol = "\uf01a";
                break;
            case 13:
                symbol = "\uf01b";
                break;
            case 14:
                symbol = "\uf01d";
                break;
            case 15:
                symbol = "\uf01b";
                break;
            case 16:
                symbol = "\uf01b";
                break;
            case 17:
                symbol = "\uf015";
                break;
            case 18:
                symbol = "\uf015";
                break;
            case 19:
                symbol = "\uf011";
                break;
            case 20:
                symbol = "\uf014";
                break;
            case 21:
                symbol = "\uf014";
                break;
            case 22:
                symbol = "\uf014";
                break;
            case 23:
                symbol = "\uf011";
                break;
            case 24:
                symbol = "\uf012";
                break;
            case 25:
                symbol = "\uf055";
                break;
            case 26:
                symbol = "\uf013";
                break;
            case 27:
                symbol = "\uf031";
                break;
            case 28:
                symbol = "\uf002";
                break;
            case 29:
                symbol = "\uf031";
                break;
            case 30:
                symbol = "\uf002";
                break;
            case 31:
                symbol = "\uf02e";
                break;
            case 32:
                symbol = "\uf00d";
                break;
            case 33:
                symbol = "\uf02e";
                break;
            case 34:
                symbol = "\uf00c";
                break;
            case 35:
                symbol = "\uf015";
                break;
            case 36:
                symbol = "\uf00d";
                break;
            case 37:
                symbol = "\uf01e";
                break;
            case 38:
                symbol = "\uf01e";
                break;
            case 39:
                symbol = "\uf01e";
                break;
            case 40:
                symbol = "\uf01d";
                break;
            case 41:
                symbol = "\uf01b";
                break;
            case 42:
                symbol = "\uf01b";
                break;
            case 43:
                symbol = "\uf01b";
                break;
            case 44:
                symbol = "\uf013";
                break;
            case 45:
                symbol = "\uf016";
                break;
            case 46:
                symbol = "\uf01b";
                break;
            case 47:
                symbol = "\uf01e";
                break;
            case 3200:
                symbol = "\uf07b";
                break;
        }
        if (symbol == "")
            symbol = "\uf013";//"\uf083";
        return symbol;
    }

    public void getWeather() {
        Tracker tracker = new Tracker(getApplicationContext());
        if (!tracker.locationIsNull) {
            double latitude = tracker.getLatitude();
            double longitude = tracker.getLongitude();
            Server server = new Server();
            tempType = settings.getString("temp", null);
            Double[] locationAndTemperatureParameters = {latitude, longitude};
            server.execute(locationAndTemperatureParameters);
            try {
                String[] weather = server.get();
                if (weather == null) {
                    weather = getOldWeatherData();
                    Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
                } else
                    setWeatherDataInSettings(weather);
                setWeatherData(weather);
                setWeatherClothes();
            } catch (Exception e) {
                pictureCondition.setText(getSymbolCode(3200));
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.location_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (afterOnCreate)
            if (setSettings())
                runOnUiThread(new Runnable() {
                    public void run() {
                        getWeather();
                        setWeatherClothes();
                        setListAdapters();
                        setListVisibility(null);
                    }
                });
            else {
                setWeatherClothes();
                setListAdapters();
                setListVisibility(null);
            }
        else {
            setSettings();
            //setWeatherClothes();
        }
    }

    private void setWeatherData(String[] weather) {
        Double tempValue;
        Double tempAddValue;
        if (weather[0] == null)
            weather = getOldWeatherData();
        if (tempType.equals("real")) {
            tempValue = Double.parseDouble(weather[0]);
            tempAddValue = Double.parseDouble(weather[3]);
        } else {
            tempValue = Double.parseDouble(weather[3]);
            tempAddValue = Double.parseDouble(weather[0]);
        }
        String tempUnit = settings.getString("unit", null);
        if (tempUnit == null) tempUnit = "C";
        if (tempUnit.equals("C"))
            tempValue = (tempValue - 32.0) * 5.0 / 9;
        if (tempUnit.equals("C"))
            tempAddValue = (tempAddValue - 32.0) * 5.0 / 9;
        currentCode = Integer.parseInt(weather[2]);
        long tempValueForView = Math.round(tempValue);
        long tempAddValueForView = Math.round(tempAddValue);
        String conditionTextForView = weather[1].toLowerCase();
        if (tempUnit.equals("F"))
            tempValue = (tempValue - 32.0) * 5.0 / 9;
        currentTemp = Math.round(tempValue);

        String strTemp = String.valueOf(tempValueForView);
        if (strTemp.length() <= 2)
            strTemp = " " + strTemp;
        textTemperature.setText(strTemp + "°");

        if (tempType.equals("real"))
            textAddTemperature.setText(getString(R.string.feels_like_text) + ": " + String.valueOf(tempAddValueForView + "°"));
        else
            textAddTemperature.setText(getString(R.string.real_text) + ": " + String.valueOf(tempAddValueForView + "°"));

        pictureCondition.setText(getSymbolCode(currentCode));

        conditionTextForView = getConditionForTextView(currentCode);
        //conditionTextForView = conditionTextForView.replace("(", "");
        //conditionTextForView = conditionTextForView.replace(")", "");
        int conditionId = getResources().getIdentifier(conditionTextForView, "string", getPackageName());
        String condition = getString(conditionId);
        textCondition.setText(condition);
        setBackground(getConditionForClothes(currentCode), (new SimpleDateFormat("MM")).format(new Date()));
    }

    private String getConditionForTextView(int code) {
        if (code == 3200)
            code = 48;
        String[] conditions = {"tornado", "tropical_storm", "hurricane", "severe_thunderstorms", "thunderstorms", "mixed_rain_and_snow",
                "mixed_rain_and_sleet", "mixed_snow_and_sleet", "freezing_drizzle", "drizzle", "freezing_rain", "showers", "showers",
                "snow_flurries", "light_snow_showers", "blowing_snow", "snow", "hail", "sleet", "dust",
                "foggy", "haze", "smoky", "blustery", "windy", "cold",
                "cloudy", "mostly_cloudy_night", "mostly_cloudy_day", "partly_cloudy_night", "partly_cloudy_day",
                "clear_night", "sunny", "fair_night", "fair_day", "mixed_rain_and_hail", "hot", "isolated_thunderstorms",
                "scattered_thunderstorms", "scattered_thunderstorms", "scattered_showers", "heavy_snow", "scattered_snow_showers", "heavy_snow", "partly_cloudy",
                "thunderstorms", "snow_showers", "isolated_thundershowers", "not_available"
        };

        return conditions[code];
    }

    public int getColorIDByName(String name) {
        int colorId = 0;

        try {
            Class res = R.color.class;
            Field field = res.getField(name);
            colorId = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colorId;
    }

    public int getStyleIDByName(String name) {
        int styleId = 0;

        try {
            Class res = R.style.class;
            Field field = res.getField(name);
            styleId = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return styleId;
    }

    private void setBackground(String condition, String mm) {
        String season = "";
        Integer month = Integer.valueOf(mm);
        if (month >= 6 && month <= 8) season = "summer";
        if (month >= 9 && month <= 11) season = "autumn";
        if (month == 12 || month == 1 || month == 2) season = "winter";
        if (month >= 3 && month <= 5) season = "spring";

        String bg = "";

        if (condition.equals("tornado") || condition.equals("storm showers") || condition.equals("rain mix") ||
                condition.equals("sprinkle") || condition.equals("showers"))
            bg = "rain";

        if (condition.equals("snow") || condition.equals("hail"))
            bg = "snow";

        if (condition.equals("cloudy gusts") || condition.equals("cloudy windy") || condition.equals("cloudy") ||
                condition.equals("day cloudy"))
            bg = "day_cloudy";

        if (condition.equals("thermometer") || condition.equals("day sunny overcast"))
            bg = "day_sunny_overcast";

        if (condition.equals("night cloudy"))
            bg = "night_cloudy";

        if (condition.equals("day sunny"))
            bg = "day_sunny";

        if (condition.equals("night clear"))
            bg = "night_clear";

        if (condition.equals("fog"))
            bg = "fog";

        if (condition.equals("thunderstorm") || condition.equals("lightning"))
            bg = "lightning";

        View layout = findViewById(R.id.activity_main);

        GradientDrawable gd = new GradientDrawable();

        // Set the color array to draw gradient
        int colorIdUP = getColorIDByName(bg + "_up");
        int colorIdDOWN = getColorIDByName(bg + "_down");
        int[] colors = {
                ResourcesCompat.getColor(getResources(), colorIdUP, null),
                ResourcesCompat.getColor(getResources(), colorIdDOWN, null)
        };
        gd.mutate();
        gd.setColors(colors);

        // Set the GradientDrawable gradient type linear gradient
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        // Set GradientDrawable shape is a rectangle
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set GradientDrawable width and in pixels
        gd.setSize(layout.getWidth(), layout.getHeight());

        // Set GradientDrawable as ImageView source image
        layout.setBackground(gd);

        int textColorID = getColorIDByName(bg+"_text");
        int textColor = ResourcesCompat.getColor(getResources(), textColorID, null);

        textCondition.setTextColor(textColor);
        pictureCondition.setTextColor(textColor);
        textTemperature.setTextColor(textColor);
        textAddTemperature.setTextColor(textColor);

        ImageView background = (ImageView) findViewById(R.id.imageBackground);
        bg = "bg_" + bg+"_"+season;

        int backgroundId = (getResources().getIdentifier(bg, "drawable", getPackageName()));
        background.setBackgroundResource(backgroundId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSettings();
        //setListVisibility(null);
        initTexts();
        //setViewPagerAdapters();
        initImages();
        setWeatherData(getWeatherFromIntent());
        //setWeatherClothes();
        initLists();
        setListAdapters();

        //test();
        //setListOnClickListeners();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewPagerAdapters();
                setWeatherClothes();
                setListOnClickListeners();
                setViewPagerPages();
                checkConnectionErrorFlag();
                //PopupWindow popupWindow = new PopupWindow(getApplicationContext());
                //popupWindow.setContentView(viewPagerBody);
                //popupWindow.showAtLocation((RelativeLayout)findViewById(R.id.activity_main), Gravity.CENTER,0,0);
                afterOnCreate = true;
            }
        }, 10);
    }

    /*private void test() {
        final String[] data = {"day_cloudy", "day_sunny_overcast", "day_sunny", "fog", "lightning", "night_clear", "night_cloudy", "rain", "snow"};
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTestBg(data[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

    private void setTestBg(String bg) {
        View layout = findViewById(R.id.activity_main);

        GradientDrawable gd = new GradientDrawable();

        // Set the color array to draw gradient
        int colorIdUP = getColorIDByName(bg + "_up");
        int colorIdDOWN = getColorIDByName(bg + "_down");
        int[] colors = {
                ResourcesCompat.getColor(getResources(), colorIdUP, null),
                ResourcesCompat.getColor(getResources(), colorIdDOWN, null)
        };
        gd.mutate();
        gd.setColors(colors);

        // Set the GradientDrawable gradient type linear gradient
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        // Set GradientDrawable shape is a rectangle
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set GradientDrawable width and in pixels
        gd.setSize(layout.getWidth(), layout.getHeight());

        // Set GradientDrawable as ImageView source image
        layout.setBackground(gd);

        int textColorID = getColorIDByName(bg+"_text");
        int textColor = ResourcesCompat.getColor(getResources(), textColorID, null);

        textCondition.setTextColor(textColor);
        pictureCondition.setTextColor(textColor);
        textTemperature.setTextColor(textColor);
        textAddTemperature.setTextColor(textColor);


        ImageView background = (ImageView) findViewById(R.id.imageBackground);
        bg = "bg_" + bg+"_"+"winter";

        int backgroundId = (getResources().getIdentifier(bg, "drawable", getPackageName()));
        background.setBackgroundResource(backgroundId);
    }

    private void checkConnectionErrorFlag() {
        if (connectionErrorFlag)
            Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
        connectionErrorFlag = false;
    }

    private String generateTip(String clothes) {
        clothes = clothes.replace("female_", "");
        clothes = clothes.replace("male_", "");
        int tipId = getResources().getIdentifier(clothes, "string", getPackageName());
        return getString(tipId);
    }

    private void setListOnClickListeners() {
        listHead.setOnItemClickListener(newOnItemClickListener("head", null, null, adapterHead, jsonHead, imageHead));
        listBody.setOnItemClickListener(newOnItemClickListener("body", viewPagerBody, 0, adapterBody, jsonBody, imageBody));
        listUnderBody.setOnItemClickListener(newOnItemClickListener("under_body", viewPagerBody, 1, adapterUnderBody, jsonUnderBody, imageUnderBody));
        listLegs.setOnItemClickListener(newOnItemClickListener("legs", viewPagerLegs, 0, adapterLegs, jsonLegs, imageLegs));
        listUnderLegs.setOnItemClickListener(newOnItemClickListener("under_legs", viewPagerLegs, 1, adapterUnderLegs, jsonUnderLegs, imageUnderLegs));
        listShoes.setOnItemClickListener(newOnItemClickListener("shoes", null, null, adapterShoes, jsonShoes, imageShoes));
    }

    private void setViewPagerAdapters() {
        viewPagerBody = (CustomViewPager) findViewById(R.id.viewpagerBody);
        viewPagerBody.setAdapter(new CustomClothesBodyAdapter(getApplicationContext()));

        ((ImageView) viewPagerBody.getChildAt(0).findViewById(R.id.imageBody)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesBody));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        ((ImageView) viewPagerBody.getChildAt(1).findViewById(R.id.imageUnderBody)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesUnderBody));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        ((ImageView) viewPagerBody.getChildAt(0).findViewById(R.id.imageBody)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("body");
                return true;
            }
        });

        ((ImageView) viewPagerBody.getChildAt(0).findViewById(R.id.imageBodyIconActive)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewPagerBody.getPaging()) return true;
                setTextUnvisible();
                setListVisibility("under_body");
                return true;
            }
        });

        ((ImageView) viewPagerBody.getChildAt(1).findViewById(R.id.imageUnderBody)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("under_body");
                return true;
            }
        });

        ((ImageView) viewPagerBody.getChildAt(1).findViewById(R.id.imageUnderBodyIconActive)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewPagerBody.getPaging()) return true;
                setTextUnvisible();
                setListVisibility("body");
                return true;
            }
        });

        viewPagerLegs = (CustomViewPager) findViewById(R.id.viewpagerLegs);
        viewPagerLegs.setAdapter(new CustomClothesLegsAdapter(getApplicationContext()));

        ((ImageView) viewPagerLegs.getChildAt(0).findViewById(R.id.imageLegs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesLegs));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        ((ImageView) viewPagerLegs.getChildAt(1).findViewById(R.id.imageUnderLegs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListVisibility(null);
                setTextUnvisible();
                textTips.setText(generateTip(clothesUnderLegs));
                textTips.setVisibility(View.VISIBLE);
            }
        });

        ((ImageView) viewPagerLegs.getChildAt(0).findViewById(R.id.imageLegsIconActive)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewPagerLegs.getPaging()) return true;
                setTextUnvisible();
                setListVisibility("under_legs");
                return true;
            }
        });

        ((ImageView) viewPagerLegs.getChildAt(0).findViewById(R.id.imageLegs)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("legs");
                return true;
            }
        });

        ((ImageView) viewPagerLegs.getChildAt(1).findViewById(R.id.imageUnderLegs)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setTextUnvisible();
                setListVisibility("under_legs");
                return true;
            }
        });

        ((ImageView) viewPagerLegs.getChildAt(1).findViewById(R.id.imageUnderLegsIconActive)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (viewPagerLegs.getPaging()) return true;
                setTextUnvisible();
                setListVisibility("legs");
                return true;
            }
        });
    }

    private String[] getWeatherFromIntent() {
        String[] weather = new String[4];
        weather[0] = getIntent().getStringExtra("value");
        weather[1] = getIntent().getStringExtra("condition");
        weather[2] = getIntent().getStringExtra("code");
        weather[3] = getIntent().getStringExtra("feel");
        if (weather[0] == null)
            connectionErrorFlag = true;
        if (getIntent().getStringExtra("connection_error").equals("true"))
            connectionErrorFlag = true;
        if (getIntent().getStringExtra("connection_error").equals("false"))
            connectionErrorFlag = false;
        //когдас интента  там уже из сеттингс берутся данные!
        return weather;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
                if (checkTimeout() == 0)
                    getWeather();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int checkTimeout() {
        String serverTime = settings.getString("servertime", null);
        if (serverTime == null) {
            serverTime = String.valueOf(System.currentTimeMillis());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("servertime", serverTime);
            editor.apply();
            return 0;
        }

        long start = Long.parseLong(serverTime);
        long finish = System.currentTimeMillis();

        serverTime = String.valueOf(System.currentTimeMillis());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("servertime", serverTime);
        editor.apply();

        if (finish - start <= 5 * 60 * 1000)
            return 0;

        return -1;
    }

    public Drawable getDrawableById(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }

    public String[] getOldWeatherData() {
        String[] oldWeatherData = new String[4];
        oldWeatherData[0] = settings.getString("temp_value", null);
        oldWeatherData[1] = settings.getString("text_weather_value", null);
        oldWeatherData[2] = settings.getString("condition_code_value", null);
        oldWeatherData[3] = settings.getString("temp_feel_value", null);
        if (oldWeatherData[0] == null) {
            Toast.makeText(getApplicationContext(), R.string.first_connection_error, Toast.LENGTH_SHORT).show();
            //Норм????
            //android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        }
        return oldWeatherData;
    }

    public void setWeatherDataInSettings(String[] weather) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("temp_value", weather[0]);
        editor.apply();

        editor.putString("text_weather_value", weather[1]);
        editor.apply();

        editor.putString("condition_code_value", weather[2]);
        editor.apply();

        editor.putString("temp_feel_value", weather[3]);
        editor.apply();
    }
}
