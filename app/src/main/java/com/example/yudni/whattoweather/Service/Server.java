package com.example.yudni.whattoweather.Service;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import static com.example.yudni.whattoweather.R.id.pictureCondition;
import static com.example.yudni.whattoweather.R.id.textCondition;
import static com.example.yudni.whattoweather.R.id.textTemperature;

public class Server extends AsyncTask<Double[], Void, String[]>{

    private String getUrl(double latitude, double longitude){//default F
        return "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D%22("+String.valueOf(latitude)+"%2C%20"+String.valueOf(longitude)+")%22)%20and%20u%3D%22"+'F'+"%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
    }

    public String[] getWeather(double latitude, double longitude) throws Exception {//0-feelslike, 1-real
        URL url = new URL(getUrl(latitude, longitude));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        if (responseCode != 200) return null;

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseText = response.toString();
        JSONObject jsonWeather = new JSONObject(responseText);
        jsonWeather = jsonWeather.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
        String temp;
        String tempFeel;
        tempFeel = jsonWeather.getJSONObject("wind").getString("chill");
        temp = jsonWeather.getJSONObject("item").getJSONObject("condition").getString("temp");

        String textWeather = jsonWeather.getJSONObject("item").getJSONObject("condition").getString("text");
        String conditionCode = jsonWeather.getJSONObject("item").getJSONObject("condition").getString("code");
        String[] result = {temp, textWeather, conditionCode, tempFeel};
        return result;
    }

    @Override
    protected String[] doInBackground(Double[]... params) {
        if(!isOnline())
            return null;
        try {return getWeather(params[0][0], params[0][1]);}
        catch (Exception e) {return null;}
    }

    @Override
    protected void onPostExecute(String[] weather) {
    }

    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }
}
