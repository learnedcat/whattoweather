package com.example.yudni.whattoweather.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudni.whattoweather.R;
import com.example.yudni.whattoweather.Service.Server;
import com.example.yudni.whattoweather.Service.Tracker;

import java.io.File;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences settings;
    private boolean connectionErrorFlag = false;
    private boolean atFirstFlag = false;

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences settings = getDefaultSharedPreferences(getApplicationContext());
        String start = settings.getString("atfirst", null);
        if (start == null) {
            start = "true";
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("atfirst", "true");
            editor.apply();
        }

        if (start.equals("true"))
            clearFilesDirectory();

        final String atFirst = String.valueOf(atFirstFlag);

        setTitleFont();

        ImageView img = (ImageView) findViewById(R.id.animationImage);
        img.setBackgroundResource(R.drawable.start_animation);

        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*while (!permissionsStatus()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {};
                }*/
                Intent intent;
                long time1 = System.currentTimeMillis();
                String[] weather = getWeather();
                long time2 = System.currentTimeMillis();
                long delta = time2 - time1;
                if (delta < 1000)
                    try {
                        Thread.sleep(1000 - delta);
                    } catch (InterruptedException ignored) {}
                weather = checkErrors(weather);
                if (atFirst.equals("true")) {
                    intent = new Intent(getApplicationContext(), TeachingActivity.class);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("atfirst", "false");
                    editor.apply();
                } else
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                if (weather != null) {
                    intent.putExtra("value", weather[0]);
                    intent.putExtra("condition", weather[1]);
                    intent.putExtra("code", weather[2]);
                    intent.putExtra("feel", weather[3]);
                    intent.putExtra("connection_error", String.valueOf(connectionErrorFlag));
                }
                startActivity(intent);
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        settings = getDefaultSharedPreferences(getApplicationContext());

        checkPermissions();
        //finishAffinity();

        // while (!permissionsStatus()) {}
        //testFirstInternetConnection();



        /*while (true) {
            if (firstInternetConnectionError)
                firstInternetConnectionErrorMessage();
        }*/
    }

    private String[] checkErrors(String[] weather) {
        if (weather == null) {
            weather = getOldWeatherData();
            if (weather[0] == null)
                firstInternetConnectionErrorMessage();
            else
                connectionErrorFlag = true;
        } else
            setWeatherDataInSettings(weather);
        return weather;
    }

    private void firstInternetConnectionErrorMessage() {
        // if (!Server.isOnline() && settings.getString("temp_value", null) == null) {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(getApplicationContext(), R.string.first_connection_error, Toast.LENGTH_LONG).show();
               finish();
           }
       });
        try {
            Thread.sleep(3500);
        } catch (InterruptedException ignored) {}
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void setTitleFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/titleFont.ttf");

        TextView titleTop = (TextView) findViewById(R.id.textStartTop);
        TextView titleBottom = (TextView) findViewById(R.id.textStartBottom);

        titleTop.setTypeface(custom_font);
        titleTop.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorStartTitle, null));

        titleBottom.setTypeface(custom_font);
        titleBottom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorStartTitle, null));
    }

    private void clearFilesDirectory() {
        File dir = getFilesDir();
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                new File(dir, aChildren).delete();
            }
        }
    }

    private String[] getWeather() {
        Tracker tracker = new Tracker(getApplicationContext());
        if (!tracker.locationIsNull) {
            double latitude = tracker.getLatitude();
            double longitude = tracker.getLongitude();
            Server server = new Server();
            String tempType = settings.getString("temp", null);
            if (tempType == null) {
                tempType = "real";
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("temp", tempType);
                editor.apply();
            }
            Double[] locationAndTemperatureParameters = {latitude, longitude};
            server.execute(locationAndTemperatureParameters);
            String[] weather = new String[4];
            try {
                weather = server.get();
            } catch (Exception e) {
                // weather[2] = "3200";
            }
            return weather;
        } else {
            Toast.makeText(getApplicationContext(), R.string.location_error, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public String[] getOldWeatherData() {
        String[] oldWeatherData = new String[4];
        oldWeatherData[0] = settings.getString("temp_value", null);
        oldWeatherData[1] = settings.getString("text_weather_value", null);
        oldWeatherData[2] = settings.getString("condition_code_value", null);
        oldWeatherData[3] = settings.getString("temp_feel_value", null);
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

    private boolean permissionsStatus() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermissions() {
        while (!permissionsStatus()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            atFirstFlag = true;
        }
        // String permission = "Manifest.permission.ACCESS_FINE_LOCATION";
        //int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        //return (res == PackageManager.PERMISSION_GRANTED);
    }
}
