package com.example.gustavo.aeunidad4.util;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by gustavo on 5/12/16.
 */

public class HttpRequest extends AsyncTask<String,Integer,String> {
    private String httpMetohd;
    private String url;
    //private Thread thread;

    public HttpRequest(String httpMetohd, String url){
        this.httpMetohd=httpMetohd;
        this.url=url;
        //this.thread=thread;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection;
        String result="";
        try {
            connection=(HttpURLConnection) new URL(url).openConnection();
            //connection.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
            result=e.getMessage();
            connection=null;
        }
        if(httpMetohd.equalsIgnoreCase("post") || httpMetohd.equalsIgnoreCase("put") || httpMetohd.equalsIgnoreCase("delete")){
            try {
                connection.setRequestMethod(httpMetohd.toUpperCase());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.connect();
                JSONObject json = new JSONObject();
                for(String x:params){
                    //Aquí puede haber excepcion
                    String [] value=x.split(":");
                    json.put(value[0],value[1]);
                }
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(json.toString());
                wr.flush();
                wr.close();
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //falta response
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line).append('\n');
                }
                result=response.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
                result=e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                result=e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                result=e.getMessage();
            }
        }
        else if(httpMetohd.equalsIgnoreCase("get")){
            try {
                connection.setRequestMethod(httpMetohd.toUpperCase());
                //connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //falta response
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line).append('\n');
                }
                result=response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result=e.toString();
            }
        }
        else{
                result="invalid http method";
        }

        return result;
    }

}
