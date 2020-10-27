package com.example.openweathermap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=1d2fcf0922433a497b5fb9fe3e2c3742";

        DownloadTaskJSON taskJSON = new DownloadTaskJSON();
        try {
            result = taskJSON.execute(address).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result != null)
            Log.i("!@#", result);
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
    }    // end of DownloadTaskJSON

}