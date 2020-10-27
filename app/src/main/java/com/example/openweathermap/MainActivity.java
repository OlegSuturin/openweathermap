package com.example.openweathermap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = "http://api.openweathermap.org/data/2.5/weather?q=Ulan-Ude,ru&APPID=1d2fcf0922433a497b5fb9fe3e2c3742";

        DownloadTaskJSON taskJSON = new DownloadTaskJSON();

        taskJSON.execute(address);


    }

    static class DownloadTaskJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder strBuffer = new StringBuilder();

            URL url = null;
            HttpURLConnection urlConnection = null;


            try {

                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    strBuffer.append(line);
                    line = bufferedReader.readLine();
                }
                return strBuffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("!@#", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String name = jsonObject.getString("name");   //получаем элемент JSON
                Log.i("!@#", name);

                JSONObject jsonMain = jsonObject.getJSONObject("main");   // получаем объект JSON
                String temp = jsonMain.getString("temp");
                String pressure = jsonMain.getString("pressure");
                float tempC = Float.parseFloat(temp) - 273; //сконветритовали Келивин в Цельсий
                Log.i("!@#", " температура - " + tempC + " " + "давление - " + pressure);

                JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");  //получаем массив объектов JSON
                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0); //получаем объект JSON из массива
                String main = jsonObjectWeather.getString("main");
                String description = jsonObjectWeather.getString("description");
                Log.i("!@#", main + " " + description);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }    // end of DownloadTaskJSON

}