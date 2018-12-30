package com.example.yudni.whattoweather.Clothes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.yudni.whattoweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClothesList extends ArrayAdapter<String> {

    private Activity context;
    private String gender;
    private List<ClothesItem> imageList;

    private JSONObject getJSON(String name) {
        JSONObject jsonObject = null;
        try {
            int resourceId = context.getResources().getIdentifier(name, "raw", context.getPackageName());
            if (resourceId == 0) return null;
            InputStream is = context.getResources().openRawResource(resourceId);
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

            String jsonString = writer.toString();
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    public ClothesList(Activity context, String gender, String type) {
        super(context, R.layout.single_list);
        this.context = context;
        this.gender = gender;
        imageList = new ArrayList<ClothesItem>();
        List<String> imageNameList = new ArrayList<String>();
        try {
            JSONObject jsonClothes = getJSON(type).getJSONObject(gender);
            Iterator<?> iteratorTemp = jsonClothes.keys();
            while (iteratorTemp.hasNext()) {
                String tempKey = (String) iteratorTemp.next();
                JSONObject jsonTemp = jsonClothes.getJSONObject(tempKey);
                Iterator<?> iteratorCond = jsonTemp.keys();
                while (iteratorCond.hasNext()) {
                    String condKey = (String) iteratorCond.next();
                    String imageName = jsonTemp.getString(condKey);
                    if (!imageNameList.contains(imageName) && imageName.length() > 0) {
                        imageNameList.add(imageName);
                        imageList.add((new ClothesItem(imageName)));
                    }
                }
            }
        } catch (JSONException e) {
        }
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    public String getItemName(int position) {
        return imageList.get(position).getImageName();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_list, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        int iconID = context.getResources().getIdentifier(gender + "_" + imageList.get(position).getImageName() + "_icon", "drawable", context.getPackageName());
        if (iconID == 0)
            iconID = context.getResources().getIdentifier(gender + "_" + imageList.get(position).getImageName(), "drawable", context.getPackageName());
        imageView.setImageResource(iconID);
        return rowView;
    }
}
