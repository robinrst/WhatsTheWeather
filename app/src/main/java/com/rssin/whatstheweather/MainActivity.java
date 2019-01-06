package com.rssin.whatstheweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText  cityName;
    TextView weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById(R.id.cityName);
        weather=(TextView)findViewById(R.id.weatherText);

    }
    public void findWeather(View view){
        weather.setText("");
        DownloadTask task =new DownloadTask();
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try{
            String res=task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ cityName.getText().toString()+"&APPID=b2c109c9c5a53f3834b880cdd5e3ff70").get();
            try {
                JSONObject jsonObject=new JSONObject(res);
                String weatherInfo=jsonObject.getString("weather");
                JSONArray arr=new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String main="";
                    String des="";
                    String mess="";
                    main=jsonPart.getString("main");
                    des=jsonPart.getString("description");

                    if(main!="" && des!=""){
                        mess+=main+":"+des;

                    }
                    if(mess!=""){
                        weather.setText(mess);
                    }



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            String res="";
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char curr=(char)data;
                    res+=curr;
                    data=reader.read();
                }
                return res;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
//
//        @Override
//        protected void onPostExecute(String res) {
//            super.onPostExecute(res);
//
//
//        }
    }
}
